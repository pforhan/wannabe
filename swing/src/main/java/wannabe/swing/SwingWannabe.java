package wannabe.swing;

import wannabe.util.Grids;

import javax.swing.JFrame;

public class SwingWannabe {
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("SwingWannabe");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final WannabePanel panel = new WannabePanel();
    frame.setContentPane(panel);

    panel.setGrid(Grids.heightMap());
//    panel.setPerspective(new Flat());

    frame.setSize(panel.getPreferredSize());
    frame.setVisible(true);

    while (true) {
      panel.render();
      Thread.sleep(200);
    }
  }
}
