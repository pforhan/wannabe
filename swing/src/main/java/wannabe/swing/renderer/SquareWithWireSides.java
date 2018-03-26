package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class SquareWithWireSides extends SwingRenderer {
  final Sides sides = new Sides();

  @Override public void draw(Graphics g, Projected r) {
    sides.wireSides(g, r);
    g.drawRect(r.left, r.top, r.size, r.size);
  }
}