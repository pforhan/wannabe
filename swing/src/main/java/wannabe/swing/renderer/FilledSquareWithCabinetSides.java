package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

public class FilledSquareWithCabinetSides extends SwingRenderer {
  final Sides sides = new Sides();
  @Override public void draw(Graphics g, Projected r) {
    sides.fillSides(g, r);
    if (!r.neighborAbove) g.fillRect(r.left, r.top, r.size, r.size);
  }
}