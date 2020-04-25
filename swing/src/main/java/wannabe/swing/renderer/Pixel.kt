package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

class Pixel : SwingRenderer() {
  override fun draw(
    g: Graphics,
    p: SwingProjected
  ) {
    g.drawLine(p.left, p.top, p.left, p.top)
  }
}
