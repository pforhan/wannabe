// Copyright 2013 Patrick Forhan.
package wannabe.swing;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wannabe.Camera;
import wannabe.grid.Grid;
import wannabe.projection.Projection;
import wannabe.projection.Projections;
import wannabe.swing.SettingsPanel.Listener;
import wannabe.swing.WannabePanel.RenderType;
import wannabe.util.SampleGrids;

public class SwingWannabe {
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("SwingWannabe");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel mainLayout = new JPanel(new BorderLayout());
    frame.setContentPane(mainLayout);

    final WannabePanel panel = new WannabePanel();
    final SettingsPanel settings = new SettingsPanel();
    settings.setListener(new Listener() {
      @Override public void onRenderTypeChanged(RenderType newType) {
        panel.setRenderType(newType);
      }

      @Override public void onProjectionChanged(Projection newProjection) {
        panel.setProjection(newProjection);
      }

      @Override public void onGridChanged(Grid newGrid) {
        panel.setGrid(newGrid);
      }
    });

    mainLayout.add(panel, BorderLayout.CENTER);
    mainLayout.add(settings, BorderLayout.EAST);

    panel.setGrid(SampleGrids.GRIDS.get(0));
    settings.gridSelected(SampleGrids.GRIDS.get(0));
    panel.setProjection(Projections.PROJECTIONS.get(0));
    settings.projectionSelected(Projections.PROJECTIONS.get(0));
    settings.renderTypeSelected(panel.getRenderType());

    frame.setSize(mainLayout.getPreferredSize());
    frame.setVisible(true);

    final Camera camera = panel.getCamera();
    panel.addKeyListener(new KeyAdapter() {
      @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            camera.position.y--;
            break;
          case KeyEvent.VK_DOWN:
            camera.position.y++;
            break;
          case KeyEvent.VK_LEFT:
            camera.position.x--;
            break;
          case KeyEvent.VK_RIGHT:
            camera.position.x++;
            break;
          case KeyEvent.VK_Z:
            camera.position.z--;
            break;
          case KeyEvent.VK_X:
            camera.position.z++;
            break;
          case KeyEvent.VK_G:
            Grid nextGrid = SampleGrids.next(panel.getGrid());
            panel.setGrid(nextGrid);
            settings.gridSelected(nextGrid);
            break;
          case KeyEvent.VK_R:
            RenderType nextRenderType = panel.getRenderType().next();
            panel.setRenderType(nextRenderType);
            settings.renderTypeSelected(nextRenderType);
            break;
          case KeyEvent.VK_P:
            Projection next = Projections.next(panel.getProjection());
            panel.setProjection(next);
            settings.projectionSelected(next);
          default:
            // Don't care.
            break;
        }
      }
    });


    while (true) {
      panel.render();
      Thread.sleep(200);
    }
  }
}
