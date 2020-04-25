package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledSquareWithCabinetSides : SwingRenderer() {
  private val sides = Sides()

  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    sides.fillSides(g, p)
    if (!p.neighborAbove) g.fillRect(p.left, p.top, p.size, p.size)
  }
}
