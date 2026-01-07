package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class FilledCircle : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.fillOval(p.left, p.top, p.size, p.size)
  }
}
