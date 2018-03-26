package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class Square extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.drawRect(r.left, r.top, r.size, r.size);
  }
}
