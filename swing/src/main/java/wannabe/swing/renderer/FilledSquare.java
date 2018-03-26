package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledSquare extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.fillRect(r.left, r.top, r.size, r.size);
  }
}
