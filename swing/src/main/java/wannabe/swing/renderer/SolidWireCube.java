package wannabe.swing.renderer;

import java.awt.Color;
import java.awt.Graphics;
import wannabe.swing.SwingProjected;

/** Cube filled with {@code fill} and outlined with each voxel's colors. */
public class SolidWireCube extends SwingRenderer {
  private final Color fill;
  final Sides sides = new Sides();

  public SolidWireCube(Color fill) {
    this.fill = fill;
  }

  @Override public void draw(Graphics g, SwingProjected p) {
    // Populate manually so that we only calculate it once.
    sides.populateCubeSidesPolygon(p);

    // First erase what we're going to write on:
    sides.fillSides(g, fill, fill);
    if (!p.neighborAbove) {
      g.setColor(fill);
      g.fillRect(p.left, p.top, p.size, p.size);
    }
    // Now draw the cube by wires:
    sides.wireSides(g, p.darkerColor, p.color);
    if (!p.neighborAbove) {
      g.setColor(p.color);
      g.drawRect(p.left, p.top, p.size, p.size);
    }
  }
}