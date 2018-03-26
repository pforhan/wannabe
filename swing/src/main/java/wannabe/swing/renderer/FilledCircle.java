package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledCircle extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.fillOval(p.left, p.top, p.size, p.size);
  }
}
