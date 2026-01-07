package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class Circle : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.drawOval(p.left, p.top, p.size, p.size)
  }
}
