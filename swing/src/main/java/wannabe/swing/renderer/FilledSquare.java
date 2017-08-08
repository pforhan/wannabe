package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class FilledSquare extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    g.fillRect(r.left, r.top, r.size, r.size);
  }
}
