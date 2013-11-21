package wannabe.swing;

import android.util.SparseArray;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import wannabe.Camera;
import wannabe.Grid;
import wannabe.Rendered;
import wannabe.UI;
import wannabe.Voxel;
import wannabe.projection.Isometric;
import wannabe.projection.Projection;
import wannabe.util.UIs;

@SuppressWarnings("serial") public class WannabePanel extends JPanel implements UI {
  public enum RenderType {
    circle, filledCircle, roundedSquare, filledRoundedSquare, square, filledSquare, threeDSquare, filledThreeDSquare;
    public void draw(Graphics2D g, Rendered r) {
      switch (this) {
        case circle:
          g.drawOval(r.left, r.top, r.size, r.size);
          break;
        case filledCircle:
          g.fillOval(r.left, r.top, r.size, r.size);
          break;
        case roundedSquare:
          int arc = r.size / 3;
          g.drawRoundRect(r.left, r.top, r.size, r.size, arc, arc);
          break;
        case filledRoundedSquare:
          arc = r.size / 3;
          g.fillRoundRect(r.left, r.top, r.size, r.size, arc, arc);
          break;
        case square:
          g.drawRect(r.left, r.top, r.size, r.size);
          break;
        case filledSquare:
          g.fillRect(r.left, r.top, r.size, r.size);
          break;
        case threeDSquare:
          g.draw3DRect(r.left, r.top, r.size, r.size, true);
          break;
        case filledThreeDSquare:
          g.fill3DRect(r.left, r.top, r.size, r.size, true);
          break;
        default:
          throw new IllegalArgumentException("Unknown RenderType: " + this);
      }
    }

    public RenderType next() {
      RenderType[] values = values();
      int next = ordinal() + 1;
      return next != values.length ? values[next] : values[0];
    }
  }

  // TODO allow custom render styles, cicle, rect, round-rect, etc.

  private static final Grid EMPTY_GRID = new Grid();
  private static final int PIXEL_SIZE = 20;
  private static final int MIN_PLAYFIELD_HEIGHT = 50;
  private static final int MIN_PLAYFIELD_WIDTH = 50;
  private static final Dimension PREFERRED_SIZE = new Dimension(PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
      PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT);

  int realPixelSize = PIXEL_SIZE;
  int widthPx;
  int heightPx;
  int widthCells;
  int heightCells;
  int centerX;
  int centerY;
  final SparseArray<Color> colorCache = new SparseArray<Color>(256);

  // Playfield paraphanellia:
  private Grid grid = EMPTY_GRID;
  /** Camera is fixed to the top-left of the widget. */
  private Camera camera = new Camera(25, 25, 0);
  private Projection projection = new Isometric();
  private RenderType renderType = RenderType.filledCircle;

  public WannabePanel() {
    setOpaque(true);
    setFocusable(true);
    addComponentListener(new ComponentAdapter() {
      @Override public void componentResized(ComponentEvent e) {
        widthPx = getWidth();
        heightPx = getHeight();
        centerX = widthPx >> 1;
        centerY = heightPx >> 1;

        int shortest = Math.min(widthPx, heightPx);
        float scale = (float) shortest / (float) PREFERRED_SIZE.height;
        realPixelSize = (int) (PIXEL_SIZE * scale);
        widthCells = UIs.pxToCells(widthPx, realPixelSize);
        heightCells = UIs.pxToCells(heightPx, realPixelSize);
      }
    });
  }

  @Override public void setGrid(Grid grid) {
    this.grid = grid;
  }

  @Override public void setCamera(Camera camera) {
    this.camera = camera;
  }

  @Override public Camera getCamera() {
    return camera;
  }

  @Override public void setProjection(Projection projection) {
    this.projection = projection;
  }

  public Projection getProjection() {
    return projection;
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

  @Override public final void paintComponent(Graphics realGraphics) {
    long start = System.currentTimeMillis();
    Graphics2D g = (Graphics2D) realGraphics.create();

    // Background:
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, widthPx, heightPx);

    // Determine the voxels we care about:
    Grid paintGrid = grid.subGrid(camera.position.x, camera.position.y, widthCells, heightCells);
    paintGrid.sortByPainters();

    for (Voxel voxel : paintGrid) {
      Rendered r = projection.render(camera, voxel.position, realPixelSize);
      g.setColor(getColor(voxel));
      renderType.draw(g, r);
    }

    // Now, draw the camera position:
    g.setColor(Color.WHITE);
    int halfSize = realPixelSize >> 1;
    g.drawRect(centerX - halfSize, centerY - halfSize, realPixelSize, realPixelSize);

    // Timing info:
    long end = System.currentTimeMillis() - start;
    if (end > 100) System.out.println("render took " + end);
  }

  protected Color getColor(Voxel voxel) {
    Color color = colorCache.get(voxel.color);
    if (color == null) {
      color = new Color(voxel.color);
      colorCache.put(voxel.color, color);
    }
    return color;
  }
}
