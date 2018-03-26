package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class Pixel extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.drawLine(p.left, p.top, p.left, p.top);
  }
}
