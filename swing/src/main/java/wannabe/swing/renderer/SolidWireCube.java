package wannabe.swing.renderer;

import java.awt.Color;
import java.awt.Graphics;
import wannabe.Rendered;

/** Cube filled with {@code fill} and outlined with each voxel's colors. */
public class SolidWireCube extends SwingRenderer {
  private final Color fill;
  final Sides sides = new Sides();

  public SolidWireCube(Color fill) {
    this.fill = fill;
  }

  @Override public void draw(Graphics g, Rendered r) {
    // Populate manually so that we only calculate it once.
    sides.populateCubeSidesPolygon(r);

    // First erase what we're going to write on:
    sides.fillSides(g, fill, fill);
    if (!r.neighborAbove) {
      g.setColor(fill);
      g.fillRect(r.left, r.top, r.size, r.size);
    }
    // Now draw the cube by wires:
    sides.wireSides(g, r.darkerColor, r.color);
    if (!r.neighborAbove) {
      g.setColor(r.color);
      g.drawRect(r.left, r.top, r.size, r.size);
    }
  }
}