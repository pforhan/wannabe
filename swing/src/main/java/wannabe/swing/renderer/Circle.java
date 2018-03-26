package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class Circle extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.drawOval(p.left, p.top, p.size, p.size);
  }
}
