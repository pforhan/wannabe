// Copyright 2013 Patrick Forhan.
package wannabe.swing;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import wannabe.Camera;
import wannabe.projection.Projections;
import wannabe.util.SampleGrids;

public class SwingWannabe {
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("SwingWannabe");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel mainLayout = new JPanel(new BorderLayout());
    frame.setContentPane(mainLayout);

    final WannabePanel panel = new WannabePanel();
    mainLayout.add(panel, BorderLayout.CENTER);
    mainLayout.add(new HelpPanel(), BorderLayout.EAST);

    panel.setGrid(SampleGrids.GRIDS.get(0));
    panel.setProjection(Projections.PROJECTIONS.get(0));

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
            panel.setGrid(SampleGrids.next(panel.getGrid()));
            break;
          case KeyEvent.VK_R:
            panel.setRenderType(panel.getRenderType().next());
            break;
          case KeyEvent.VK_P:
            panel.setProjection(Projections.next(panel.getProjection()));
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
