package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Rendered;

public class FilledCircle extends SwingRenderer {
  @Override public void draw(Graphics g, Rendered r) {
    g.fillOval(r.left, r.top, r.size, r.size);
  }
}
