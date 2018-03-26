package wannabe.swing;

import android.util.SparseArray;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import wannabe.Bounds.XYBounds;
import wannabe.Camera;
import wannabe.Projected;
import wannabe.Translation;
import wannabe.UI;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.MutableGrid;
import wannabe.grid.SimpleGrid;
import wannabe.grid.SparseArrayGrid;
import wannabe.projection.Cabinet;
import wannabe.projection.Projection;
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetSides;
import wannabe.swing.renderer.SwingRenderer;
import wannabe.util.UIs;

/**
 * Swing painting code to display a Wannabe {@link Grid}. Treats {@link Voxel#value} as an ARGB
 * value.  If the alpha component is 0x00, it is treated as 0xFF (100% opaque).
 */
@SuppressWarnings("serial") public class WannabePanel extends JPanel implements UI {
  public static final int DEFAULT_PIXEL_SIZE = 20;
  private static final int MIN_PLAYFIELD_HEIGHT = 50;
  private static final int MIN_PLAYFIELD_WIDTH = 50;
  private static final Dimension PREFERRED_SIZE
      = new Dimension(DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
          DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT);

  public int realPixelSize = DEFAULT_PIXEL_SIZE;
  // Our dimensions:
  int widthPx;
  int heightPx;
  int widthCells;
  int heightCells;
  // Center cell:
  int halfWidthCells;
  int halfHeightCells;
  final SparseArray<Color> colorCache = new SparseArray<>(256);
  final SparseArray<Color> darkerCache = new SparseArray<>(256);

  // Playfield paraphanellia:
  private final List<Grid> grids = new ArrayList<>();
  private final MutableGrid[] buffers = {
      new SimpleGrid("buffer"),
      new SparseArrayGrid("buffer", true),
  };
  private int bufferOffset = 0;
  private final XYBounds bounds = new XYBounds();

  /** Camera is fixed to the center of the widget. */
  private Camera camera;
  private Projection projection = new Cabinet();
  private SwingRenderer renderer = new FilledThreeDSquareWithCabinetSides();
  private boolean stats;
  private boolean exportHidden;
  private boolean dirty;
  private final Translation lastCameraTranslation = new Translation(0,0,0);

  public WannabePanel(final Camera camera) {
    this.camera = camera;

    setOpaque(true);
    setFocusable(true);
    addComponentListener(new ComponentAdapter() {
      @Override public void componentResized(ComponentEvent e) {
        widthPx = getWidth();
        heightPx = getHeight();
        camera.uiPosition.left = widthPx >> 1;
        camera.uiPosition.top = heightPx >> 1;

        int shortest = Math.min(widthPx, heightPx);
        float scale = (float) shortest / (float) PREFERRED_SIZE.height;
        realPixelSize = (int) (DEFAULT_PIXEL_SIZE * scale);
        if (realPixelSize < 1) realPixelSize = 1;
        widthCells = UIs.pxToCells(widthPx, realPixelSize);
        heightCells = UIs.pxToCells(heightPx, realPixelSize);
        halfWidthCells = widthCells >> 1;
        halfHeightCells = heightCells >> 1;
      }
    });
  }

  @Override public void addGrid(Grid grid) {
    dirty = true;
    grids.add(grid);
  }

  @Override public void removeGrid(Grid grid) {
    dirty = true;
    grids.remove(grid);
  }

  @Override public void setCamera(Camera camera) {
    this.camera = camera;
  }

  @Override public void setProjection(Projection projection) {
    this.projection = projection;
  }

  public Projection getProjection() {
    return projection;
  }

  /** Shows advanced stats on the display. */
  public void showStats() {
    stats = true;
  }

  /** Updates timer, requests refresh. */
  @Override public void render() {
    repaint();
  }

  @Override public Dimension getPreferredSize() {
    return PREFERRED_SIZE;
  }

  public void setRenderer(SwingRenderer renderer) {
    this.renderer = renderer;
  }

  public SwingRenderer getRenderer() {
    return renderer;
  }

  public void nextBuffer() {
    dirty = true;
    bufferOffset = (bufferOffset + 1) % buffers.length;
  }

  public void exportHidden(boolean exportHidden) {
    dirty = true;
    this.exportHidden = exportHidden;
  }

  @Override protected void paintBorder(Graphics g) {
  }

  @Override protected void paintChildren(Graphics g) {
  }

  @Override public final void paintComponent(Graphics g) {
    long start = System.currentTimeMillis();

    bounds.setFromWidthHeight(camera.position.x - halfWidthCells, //
        camera.position.y - halfHeightCells, //
        widthCells, heightCells);

    MutableGrid activeBuffer = buffers[bufferOffset];
    for (Grid grid : grids) {
      if (grid.isDirty()) {
        dirty = true;
        break;
      }
    }
    if (!lastCameraTranslation.equals(camera.position)) {
      dirty = true;
    }
    if (dirty) {
      activeBuffer.clear();
      for (Grid grid : grids) {
        grid.exportTo(activeBuffer, bounds, exportHidden);
      }
      activeBuffer.optimize();
      dirty = false;
    }

    long afterGrid = System.currentTimeMillis();

    // Background:
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, widthPx, heightPx);

    for (Voxel voxel : activeBuffer) {
      Projected r = projection.project(camera, voxel.position, realPixelSize);
      // If it's going to be fully off-screen, don't bother drawing.
      if (r.left < -realPixelSize || r.left > widthPx //
          || r.top < -realPixelSize || r.top > heightPx) {
        continue;
      }
      // If it's too small don't draw:
      // TODO probably should put a specific bounds on when PsPerspective used, but anyway...
      // TODO and of course this affects pixel-sized rendering if we want to try that
      if (r.size <= 1) continue;
      r.color = getSwingColor(voxel.value);
      r.darkerColor = getDarkerColor(voxel.value);
      r.neighborsFrom(activeBuffer.neighbors(voxel));

      // TODO it seems a bit weird that a) this class sets up some of rendered (though it is
      // awt colors in this case) and b) that it controls the context value
      g.setColor(r.color);
      renderer.draw(g, r);
    }

    // Timing info:
    long total = System.currentTimeMillis() - start;
    if (total > 100) {
      long gridTime = afterGrid - start;
      System.out.println(String.format("render took %s; %s was grid, %s was render", total,
          gridTime, total - gridTime));
    }

    if (stats) {
      long gridTime = afterGrid - start;
      String statistics = "voxels on screen: " + activeBuffer.size() + "; time: grid "
          + gridTime + " render " + (total - gridTime) + " on "
          + activeBuffer.getClass().getSimpleName();
      // Make a small shadow to help stand out:
      g.setColor(Color.BLACK);
      g.drawString(statistics, 20, 20);
      g.setColor(Color.WHITE);
      g.drawString(statistics, 19, 19);
    }

    lastCameraTranslation.set(camera.position);
  }

  private Color getSwingColor(int argb) {
    Color color = colorCache.get(argb);
    if (color == null) {
      boolean hasAlpha = argb >> 24 > 0;
      color = new Color(argb, hasAlpha);
      colorCache.put(argb, color);
    }
    return color;
  }

  private Color getDarkerColor(int argb) {
    Color color = darkerCache.get(argb);
    if (color == null) {
      color = getSwingColor(argb).darker();
      darkerCache.put(argb, color);
    }
    return color;
  }

}
