package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledThreeDSquareWithCabinetSides : SwingRenderer() {
  private var sides = Sides()

  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    sides.fillSides(g, p)
    if (!p.neighborAbove) g.fill3DRect(p.left, p.top, p.size, p.size, true)
  }
}
