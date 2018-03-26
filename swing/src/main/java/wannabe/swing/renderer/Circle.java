package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class Circle extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.drawOval(r.left, r.top, r.size, r.size);
  }
}
