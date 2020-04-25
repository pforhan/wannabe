package wannabe.projection

import wannabe.Camera
import wannabe.Position
import wannabe.Projected

/**
 * Renders [Voxel]s with a constant size, but with an offset to represent height.
 */
class Cabinet(
  /** Horizontal offset to use for each unit of Z. */
  val hOffsetPerZ: Int,
  /** Vertical offset to use for each unit of Z.  */
  val vOffsetPerZ: Int
) : Projection {
  private val projected = Projected()

  override fun project(
    camera: Camera,
    position: Position,
    pixelSize: Int
  ): Projected {
    val (x, y, z) = camera.translate(position)
    val horizOffset = hOffsetPerZ * z + pixelSize * x
    val vertOffset = vOffsetPerZ * z + pixelSize * y
    projected.left = camera.uiPosition.left + horizOffset
    projected.top = camera.uiPosition.top + vertOffset
    projected.size = pixelSize
    projected.hDepth = -hOffsetPerZ
    projected.vDepth = -vOffsetPerZ
    return projected
  }

  override fun toString(): String = "Cabinet $hOffsetPerZ, $vOffsetPerZ"
}
