package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class ThreeDSquare : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.draw3DRect(p.left, p.top, p.size, p.size, true)
  }
}
