package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class SquareWithWireSides extends SwingRenderer {
  final Sides sides = new Sides();

  @Override public void draw(Graphics g, SwingProjected p) {
    sides.wireSides(g, p);
    g.drawRect(p.left, p.top, p.size, p.size);
  }
}