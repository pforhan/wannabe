package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledThreeDSquare extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.fill3DRect(p.left, p.top, p.size, p.size, true);
  }
}
