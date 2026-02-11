package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledThreeDSquare : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.fill3DRect(p.left, p.top, p.size, p.size, true)
  }
}
