package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class RoundedSquare extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    int arc = p.size / 3;
    g.drawRoundRect(p.left, p.top, p.size, p.size, arc, arc);
  }
}
