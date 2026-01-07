package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class Square : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.drawRect(p.left, p.top, p.size, p.size)
  }
}
