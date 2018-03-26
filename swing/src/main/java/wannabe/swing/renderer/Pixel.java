package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class Pixel extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.drawLine(r.left, r.top, r.left, r.top);
  }
}
