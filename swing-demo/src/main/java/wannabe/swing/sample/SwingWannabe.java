package wannabe.swing.sample;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wannabe.Camera;
import wannabe.Translation;
import wannabe.grid.FrameAnimatedGrid;
import wannabe.grid.Grid;
import wannabe.projection.Projection;
import wannabe.projection.Projections;
import wannabe.swing.WannabePanel;
import wannabe.swing.renderer.SwingRenderer;
import wannabe.swing.sample.SettingsPanel.Listener;
import wannabe.util.SampleGrids;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/** All the glue to make a sample swing Wannabe application. */
public class SwingWannabe {
  private static final Translation TRANSLATE_UP = new Translation(0, -1, 0);
  private static final Translation TRANSLATE_DOWN = new Translation(0, 1, 0);
  private static final Translation TRANSLATE_LEFT = new Translation(-1, 0, 0);
  private static final Translation TRANSLATE_RIGHT = new Translation(1, 0, 0);
  private static final Translation TRANSLATE_HIGHER = new Translation(0, 0, 1);
  private static final Translation TRANSLATE_LOWER = new Translation(0, 0, -1);

  private static Grid currentGrid;
  private static FrameAnimatedGrid playerGrid = SampleGrids.megaManRunning();
  private static boolean movingPlayer;
  private static boolean playerVisible;
  private static boolean exportHidden;

  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("SwingWannabe");
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    JPanel mainLayout = new JPanel(new BorderLayout());
    frame.setContentPane(mainLayout);

    final Camera camera = new Camera(18, 18, 0);
    final WannabePanel panel = new WannabePanel(camera);
    panel.showStats();
    final SettingsPanel settings = new SettingsPanel();
    // TODO rather than have this class be the go-between of settings and WannabePanel, just
    // have settings change it directly. That way we shouldn't have to set things three times.
    // *Maybe* I could even move the key listener code to there, too, so i don't have to edit
    // two classes when bindings change
    settings.setListener(new Listener() {
      @Override public void onRendererChanged(SwingRenderer newType) {
        panel.setRenderer(newType);
      }

      @Override public void onProjectionChanged(Projection newProjection) {
        panel.setProjection(newProjection);
      }

      @Override public void onGridChanged(Grid newGrid) {
        panel.removeGrid(currentGrid);
        currentGrid = newGrid;
        panel.addGrid(currentGrid);
      }
    });

    mainLayout.add(panel, BorderLayout.CENTER);
    mainLayout.add(settings, BorderLayout.EAST);

    currentGrid = SwingGrids.GRIDS.get(0);
    panel.addGrid(currentGrid);
    settings.gridSelected(currentGrid);
    panel.setProjection(Projections.PROJECTIONS.get(0));
    settings.projectionSelected(Projections.PROJECTIONS.get(0));
    settings.renderTypeSelected(panel.getRenderer());

    frame.setSize(mainLayout.getPreferredSize());
    frame.setVisible(true);

    panel.addKeyListener(new KeyAdapter() {
      @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_UP);
            } else {
              camera.position.y--;
            }
            break;
          case KeyEvent.VK_DOWN:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_DOWN);
            } else {
              camera.position.y++;
            }
            break;
          case KeyEvent.VK_LEFT:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_LEFT);
            } else {
              camera.position.x--;
            }
            break;
          case KeyEvent.VK_RIGHT:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_RIGHT);
            } else {
              camera.position.x++;
            }
            break;
          case KeyEvent.VK_Z:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_LOWER);
            } else {
              camera.position.z--;
            }
            break;
          case KeyEvent.VK_X:
            if (movingPlayer) {
              playerGrid.translate(TRANSLATE_HIGHER);
            } else {
              camera.position.z++;
            }
            break;
          case KeyEvent.VK_SPACE:
            movingPlayer = !movingPlayer;
            break;
          case KeyEvent.VK_A:
            if (playerVisible) {
              panel.removeGrid(playerGrid);
              playerVisible = false;
            } else {
              playerVisible = true;
              panel.addGrid(playerGrid);
            }
            break;
          case KeyEvent.VK_G:
            panel.removeGrid(currentGrid);
            currentGrid = SwingGrids.next(currentGrid);
            panel.addGrid(currentGrid);
            settings.gridSelected(currentGrid);
            break;
          case KeyEvent.VK_R:
            SwingRenderer nextRenderType = settings.nextRenderer();
            panel.setRenderer(nextRenderType);
            settings.renderTypeSelected(nextRenderType);
            break;
          case KeyEvent.VK_P:
            Projection next = Projections.next(panel.getProjection());
            panel.setProjection(next);
            settings.projectionSelected(next);
            break;
          case KeyEvent.VK_E:
            exportHidden = !exportHidden;
            panel.exportHidden(exportHidden);
            break;
          case KeyEvent.VK_1:
            panel.realPixelSize = 1;
            break;
          case KeyEvent.VK_2:
            panel.realPixelSize = 2;
            break;
          case KeyEvent.VK_BACK_QUOTE:
            panel.realPixelSize = WannabePanel.DEFAULT_PIXEL_SIZE;
            break;
          default:
            // Don't care.
            break;
        }
      }
    });

    while (true) {
      playerGrid.nextFrame();
      panel.render();
      Thread.sleep(200);
    }
  }
}
