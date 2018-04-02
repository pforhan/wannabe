package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledSquareWithCabinetSides extends SwingRenderer {
  final Sides sides = new Sides();
  @Override public void draw(Graphics g, SwingProjected p) {
    sides.fillSides(g, p);
    if (!p.neighborAbove) g.fillRect(p.left, p.top, p.size, p.size);
  }
}