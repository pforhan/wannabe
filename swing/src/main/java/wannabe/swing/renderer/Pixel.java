package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class Pixel extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    g.drawLine(r.left, r.top, r.left, r.top);
  }
}
