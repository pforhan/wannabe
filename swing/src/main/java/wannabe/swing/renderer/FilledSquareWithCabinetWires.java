package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledSquareWithCabinetWires extends SwingRenderer {
  Sides sides = new Sides();
  @Override public void draw(Graphics g, Projected r) {
    sides.wireSides(g, r);
    g.fillRect(r.left, r.top, r.size, r.size);
  }
}