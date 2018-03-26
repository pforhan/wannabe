package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledRoundedSquare extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    int arc = r.size / 3;
    g.fillRoundRect(r.left, r.top, r.size, r.size, arc, arc);
  }
}
