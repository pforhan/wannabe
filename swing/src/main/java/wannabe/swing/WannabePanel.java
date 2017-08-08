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
import wannabe.Rendered;
import wannabe.UI;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.MutableGrid;
import wannabe.grid.SimpleGrid;
import wannabe.projection.Cabinet;
import wannabe.projection.Projection;
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetSides;
import wannabe.swing.renderer.SwingRenderer;
import wannabe.util.UIs;

/** Swing painting code to display a Wannabe {@link Grid}. */
@SuppressWarnings("serial") public class WannabePanel extends JPanel implements UI {
  static final int DEFAULT_PIXEL_SIZE = 20;
  private static final int MIN_PLAYFIELD_HEIGHT = 50;
  private static final int MIN_PLAYFIELD_WIDTH = 50;
  private static final Dimension PREFERRED_SIZE
      = new Dimension(DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
          DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT);

  int realPixelSize = DEFAULT_PIXEL_SIZE;
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
  private final MutableGrid buffer = new SimpleGrid("buffer", true);
  /** Camera is fixed to the center of the widget. */
  private Camera camera;
  private Projection projection = new Cabinet();
  private SwingRenderer renderer = new FilledThreeDSquareWithCabinetSides();
  private boolean stats;
  private boolean exportHidden;

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
    grids.add(grid);
  }

  @Override public void removeGrid(Grid grid) {
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

  public void exportHidden(boolean exportHidden) {
    this.exportHidden = exportHidden;
  }

  @Override protected void paintBorder(Graphics g) {
  }

  @Override protected void paintChildren(Graphics g) {
  }

  @Override public final void paintComponent(Graphics g) {
    long start = System.currentTimeMillis();

    // Background:
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, widthPx, heightPx);

    buffer.clear();
    XYBounds bounds = new XYBounds();
    bounds.setFromWidthHeight(camera.position.x - halfWidthCells, //
        camera.position.y - halfHeightCells, //
        widthCells, heightCells);
    for (Grid grid : grids) {
      grid.exportTo(buffer, bounds, exportHidden);
    }
    buffer.optimize();

    // Determine the voxels we care about:
    long afterGrid = System.currentTimeMillis();

    for (Voxel voxel : buffer) {
      Rendered r = projection.render(camera, voxel.position, realPixelSize);
      // If it's going to be fully off-screen, don't bother drawing.
      if (r.left < -realPixelSize || r.left > widthPx //
          || r.top < -realPixelSize || r.top > heightPx) {
        continue;
      }
      r.color = getSwingColor(voxel.color);
      r.darkerColor = getDarkerColor(voxel.color);
      r.neighborsFrom(buffer.neighbors(voxel));

      // TODO it seems a bit weird that a) this class sets up some of rendered (though it is
      // awt colors in this case) and b) that it controls the context color
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
      String statistics = "voxels on screen: " + buffer.size() + "; time: grid " + gridTime
          + " render " + (total - gridTime);
      // Make a small shadow to help stand out:
      g.setColor(Color.BLACK);
      g.drawString(statistics, 20, 20);
      g.setColor(Color.WHITE);
      g.drawString(statistics, 19, 19);
    }
  }

  private Color getSwingColor(int rgb) {
    Color color = colorCache.get(rgb);
    if (color == null) {
      color = new Color(rgb);
      colorCache.put(rgb, color);
    }
    return color;
  }

  private Color getDarkerColor(int rgb) {
    Color color = darkerCache.get(rgb);
    if (color == null) {
      color = getSwingColor(rgb).darker();
      darkerCache.put(rgb, color);
    }
    return color;
  }

}
