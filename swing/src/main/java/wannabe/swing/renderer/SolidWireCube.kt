package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Color
import java.awt.Graphics

/** Cube filled with `fill` and outlined with each voxel's colors.  */
class SolidWireCube(private val fill: Color) : SwingRenderer() {
  private val sides = Sides()
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    // Populate manually so that we only calculate it once.
    sides.populateCubeSidesPolygon(p)

    // First erase what we're going to write on:
    sides.fillSides(g, fill, fill)
    if (!p.neighborAbove) {
      g.color = fill
      g.fillRect(p.left, p.top, p.size, p.size)
    }
    // Now draw the cube by wires:
    sides.wireSides(g, p.darkerColor, p.color)
    if (!p.neighborAbove) {
      g.color = p.color
      g.drawRect(p.left, p.top, p.size, p.size)
    }
  }

}
