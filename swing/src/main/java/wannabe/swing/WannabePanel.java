package wannabe.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

  private Grid grid = EMPTY_GRID;
  /** Camera is fixed to the center of the widget. */
//  private Camera camera = new Camera(PLAYFIELD_WIDTH / 2, PLAYFIELD_HEIGHT / 2, 0);
  private Camera camera = new Camera(0, 0, 0);
  private Projection projection = new Isometric();

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
        // TODO We should probably honor the case where preferredSize.WIDTH != preferredSize.HEIGHT
        realPixelSize = (int) (PIXEL_SIZE * scale);
        widthCells = UIs.pxToCells(widthPx, realPixelSize);
        heightCells = UIs.pxToCells(heightPx, realPixelSize);
        projection.setPixelSize(realPixelSize);
      }
    });
    addKeyListener(new KeyAdapter() {
      @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            camera.position.y++;
            break;
          case KeyEvent.VK_DOWN:
            camera.position.y--;
            break;
          case KeyEvent.VK_LEFT:
            camera.position.x++;
            break;
          case KeyEvent.VK_RIGHT:
            camera.position.x--;
            break;
          case KeyEvent.VK_Z:
            camera.position.z++;
            break;
          case KeyEvent.VK_X:
            camera.position.z--;
            break;
          default:
            // Don't care.
            break;
        }
      }
    });
  }

  @Override public void setGrid(Grid grid) {
    this.grid = grid;
  }

  @Override public void setCamera(Camera camera) {
    this.camera = camera;
  }

  @Override public void setPerspective(Projection projection) {
    this.projection = projection;
  }

  /** Updates timer, requests refresh. */
  @Override public void render() {
    repaint();
  }

  @Override public Dimension getPreferredSize() {
    return PREFERRED_SIZE;
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
      Rendered r = projection.render(camera, voxel.position);
      g.setColor(new Color(voxel.color)); // TODO cache colors
      g.fillOval(r.left, r.top, r.size, r.size); // TODO offer alternate rendering modes
    }

    // Now, draw the camera position:
    g.setColor(Color.WHITE);
    int halfSize = realPixelSize >> 1;
    g.drawRect(centerX - halfSize, centerY - halfSize, realPixelSize, realPixelSize);

    // Timing info:
    long end = System.currentTimeMillis() - start;
    if (end > 100) System.out.println("render took " + end);
  }
}
