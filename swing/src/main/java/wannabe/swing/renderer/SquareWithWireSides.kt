package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class SquareWithWireSides : SwingRenderer() {
  private val sides = Sides()

  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    sides.wireSides(g, p)
    g.drawRect(p.left, p.top, p.size, p.size)
  }
}
