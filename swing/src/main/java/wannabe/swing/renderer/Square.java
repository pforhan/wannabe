package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class Square extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    g.drawRect(r.left, r.top, r.size, r.size);
  }
}
