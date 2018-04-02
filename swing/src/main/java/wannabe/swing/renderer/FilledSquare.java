package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledSquare extends SwingRenderer {
  @Override public void draw(Graphics g, SwingProjected p) {
    g.fillRect(p.left, p.top, p.size, p.size);
  }
}
