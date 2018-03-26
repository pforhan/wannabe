package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledThreeDSquare extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.fill3DRect(r.left, r.top, r.size, r.size, true);
  }
}
