package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledRoundedSquare extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    int arc = p.size / 3;
    g.fillRoundRect(p.left, p.top, p.size, p.size, arc, arc);
  }
}
