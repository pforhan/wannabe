package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class Square extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.drawRect(p.left, p.top, p.size, p.size);
  }
}
