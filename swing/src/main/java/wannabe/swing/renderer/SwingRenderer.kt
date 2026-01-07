package wannabe.swing.renderer

import wannabe.swing.SwingProjected
import java.awt.Graphics

/** Displays a voxel in a swing UI.  */
abstract class SwingRenderer {
  abstract fun draw(
    g: Graphics,
    p: SwingProjected
  )

  override fun toString(): String = javaClass.simpleName

  override fun equals(other: Any?) = other != null && javaClass == other.javaClass

  override fun hashCode(): Int = javaClass.hashCode()
}
