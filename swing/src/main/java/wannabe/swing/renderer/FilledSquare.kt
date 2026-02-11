package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledSquare : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.fillRect(p.left, p.top, p.size, p.size)
  }
}
