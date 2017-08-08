package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class Circle extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    g.drawOval(r.left, r.top, r.size, r.size);
  }
}
