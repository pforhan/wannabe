package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledCircle extends SwingRenderer {
  @Override public void draw(Graphics g, Projected r) {
    g.fillOval(r.left, r.top, r.size, r.size);
  }
}
