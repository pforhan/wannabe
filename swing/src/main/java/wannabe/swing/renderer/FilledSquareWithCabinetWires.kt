package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledSquareWithCabinetWires : SwingRenderer() {
  private var sides = Sides()

  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    sides.wireSides(g, p)
    g.fillRect(p.left, p.top, p.size, p.size)
  }
}
