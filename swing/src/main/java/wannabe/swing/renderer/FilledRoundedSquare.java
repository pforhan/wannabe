package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class FilledRoundedSquare extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    int arc = r.size / 3;
    g.fillRoundRect(r.left, r.top, r.size, r.size, arc, arc);
  }
}
