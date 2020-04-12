package wannabe.swing.sample;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wannabe.Camera;
import wannabe.Translation;
import wannabe.grid.CachingGrid;
import wannabe.grid.FrameAnimatedGrid;
import wannabe.grid.Grid;
import wannabe.grid.GroupGrid;
import wannabe.grid.RotateGrid;
import wannabe.grid.TranslateGrid;
import wannabe.grid.VisibilityGrid;
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

  private static GroupGrid allGrids = new GroupGrid("Demo Group");
  /** Maps a grid to its visibility grid */
  private static Map<Grid, VisibilityGrid> gridToVis = new HashMap<>();
  /** Maps a grid to its rotation grid */
  private static Map<Grid, RotateGrid> gridToRot = new HashMap<>();
  private static Grid currentGrid;
  private static boolean exportHidden;

  // Player grid stuff
  private static FrameAnimatedGrid playerGridFrames = SampleGrids.megaManRunning();
  private static TranslateGrid playerGridTranslate =
      new TranslateGrid("Translate Player", playerGridFrames);
  private static VisibilityGrid playerGridVisibility =
      new VisibilityGrid("Player Visibility", playerGridTranslate);
  private static boolean movingPlayer;

  // Rotating stuff
  private static RotateGrid rotateGrid;
  private static int startDragX, startDragY;

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
        moveToGrid(newGrid);
      }
    });

    mainLayout.add(panel, BorderLayout.CENTER);
    mainLayout.add(settings, BorderLayout.EAST);

    // Wrap each demo grid in a visibility and rotate control. Hide each.
    for (Grid grid : SwingGrids.GRIDS) {
      RotateGrid rot = new RotateGrid("Rotate", grid);
      VisibilityGrid vis =
          new VisibilityGrid("vis for " + grid.toString(),
              new CachingGrid("rot cache for " + grid.toString(), rot));
      //TODO something wrong with caching grid?
      vis.hide();
      allGrids.add(vis);
      gridToVis.put(grid, vis);
      gridToRot.put(grid, rot);
    }

    allGrids.add(playerGridVisibility);
    playerGridVisibility.hide();
    // Show the first grid:
    currentGrid = SwingGrids.GRIDS.get(0);
    moveToGrid(currentGrid);

    panel.setGrid(allGrids);
    settings.gridSelected(currentGrid);
    panel.setProjection(Projections.values()[0].projection);
    settings.projectionsSelected(Projections.values()[0]);
    settings.renderTypeSelected(panel.getRenderer());

    frame.setSize(mainLayout.getPreferredSize());
    frame.setVisible(true);

    panel.addKeyListener(new KeyAdapter() {
      @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_UP);
            } else {
              camera.position.y--;
            }
            break;
          case KeyEvent.VK_DOWN:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_DOWN);
            } else {
              camera.position.y++;
            }
            break;
          case KeyEvent.VK_LEFT:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_LEFT);
            } else {
              camera.position.x--;
            }
            break;
          case KeyEvent.VK_RIGHT:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_RIGHT);
            } else {
              camera.position.x++;
            }
            break;
          case KeyEvent.VK_Z:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_LOWER);
            } else {
              camera.position.z--;
            }
            break;
          case KeyEvent.VK_X:
            if (movingPlayer) {
              playerGridTranslate.translate(TRANSLATE_HIGHER);
            } else {
              camera.position.z++;
            }
            break;
          case KeyEvent.VK_SPACE:
            movingPlayer = !movingPlayer;
            break;
          case KeyEvent.VK_A:
            playerGridVisibility.toggle();
            System.out.println("player state: " + playerGridVisibility);
            break;
          case KeyEvent.VK_B:
            // TODO could reimplement this with two different root groups maybe?
            break;
          case KeyEvent.VK_G:
            moveToGrid(SwingGrids.next(currentGrid));
            settings.gridSelected(currentGrid);
            break;
          case KeyEvent.VK_R:
            SwingRenderer nextRenderType = settings.nextRenderer();
            panel.setRenderer(nextRenderType);
            settings.renderTypeSelected(nextRenderType);
            break;
          case KeyEvent.VK_P:
            Projections next = Projections.withProjection(panel.getProjection()).next();
            panel.setProjection(next.projection);
            settings.projectionsSelected(next);
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
          case KeyEvent.VK_SLASH:
            gridToRot.get(currentGrid).setRotate(0,0,0);
          default:
            // Don't care.
            break;
        }
      }
    });

    panel.addMouseListener(new MouseAdapter() {
      @Override public void mousePressed(MouseEvent e) {
        startDragX = e.getX();
        startDragY = e.getY();
      }
    });

    panel.addMouseMotionListener(new MouseMotionAdapter() {
      @Override public void mouseDragged(MouseEvent e) {
        // Odd ordering here is just because it looks best, since most content is down-right of origin
        rotateGrid.setRotate(e.getY() - startDragY, startDragX - e.getX(), 0);
      }
    });

    // noinspection InfiniteLoopStatement
    while (true) {
      playerGridFrames.nextFrame();
      panel.render();
      Thread.sleep(100);
    }
  }

  private static void moveToGrid(Grid newGrid) {
    // TODO make this support multiselect
    hideGrid(currentGrid);
    currentGrid = newGrid;
    rotateGrid = gridToRot.get(currentGrid);
    showGrid(currentGrid);
  }

  private static void showGrid(Grid grid) {
    gridToVis.get(grid).show();
  }

  private static void hideGrid(Grid grid) {
    gridToVis.get(grid).hide();
  }
}
