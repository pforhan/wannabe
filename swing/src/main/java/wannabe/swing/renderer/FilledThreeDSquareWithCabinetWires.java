package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.swing.SwingProjected;

public class FilledThreeDSquareWithCabinetWires extends SwingRenderer {
  Sides sides = new Sides();
  @Override public void draw(Graphics g, SwingProjected p) {
    sides.wireSides(g, p);
    g.fill3DRect(p.left, p.top, p.size, p.size, true);
  }
}