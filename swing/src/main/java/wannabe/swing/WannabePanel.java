// Copyright 2013 Patrick Forhan.
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
import wannabe.Camera;
import wannabe.Rendered;
import wannabe.UI;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;
import wannabe.projection.Isometric;
import wannabe.projection.Projection;
import wannabe.util.UIs;

/** Swing painting code to display a Wannabe {@link Grid}. */
@SuppressWarnings("serial") public class WannabePanel extends JPanel implements UI {
  private static final Grid EMPTY_GRID = new SimpleGrid("empty");
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
  final SparseArray<Color> colorCache = new SparseArray<Color>(256);

  // Playfield paraphanellia:
  private final List<Grid> grids = new ArrayList<Grid>();
  /** Camera is fixed to the center of the widget. */
  private Camera camera;
  private Projection projection = new Isometric();
  private RenderType renderType = RenderType.filledCircle;
  private boolean stats;

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

  public void setRenderType(RenderType renderType) {
    this.renderType = renderType;
  }

  public RenderType getRenderType() {
    return renderType;
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

    for (Grid grid : grids) {
      // Determine the voxels we care about:
      Grid paintGrid = grid.subGrid(camera.position.x - halfWidthCells, //
          camera.position.y - halfHeightCells, widthCells, heightCells);
      ((SimpleGrid) paintGrid).sortByPainters();
      long afterGrid = System.currentTimeMillis();

      for (Voxel voxel : paintGrid) {
        Rendered r = projection.render(camera, voxel.position, realPixelSize);
        // If it's going to be fully off-screen, don't bother drawing.
        if (r.left < -realPixelSize || r.left > widthPx //
            || r.top < -realPixelSize || r.top > heightPx) {
          continue;
        }
        g.setColor(getSwingColor(voxel.color));
        renderType.draw(g, r);
      }

      // Timing info:
      long end = System.currentTimeMillis() - start;
      if (end > 100) {
        long gridTime = afterGrid - start;
        System.out.println(String.format("render took %s; %s was grid, %s was render", end,
            gridTime, end - gridTime));
      }
    }

    if (stats) {
      g.setColor(Color.BLACK);
      String statString = "voxels on screen: " + 0 + "; render time " + 0;
      g.drawString(statString, 20, 20);
      g.setColor(Color.WHITE);
      g.drawString(statString, 19, 19);
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

}
