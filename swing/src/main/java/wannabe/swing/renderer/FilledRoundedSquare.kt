package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledRoundedSquare : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    val arc = p.size / 3
    g.fillRoundRect(p.left, p.top, p.size, p.size, arc, arc)
  }
}
