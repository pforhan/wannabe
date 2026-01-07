package wannabe.projection

import wannabe.Camera
import wannabe.Position
import wannabe.Projected

/**
 * Renders [Voxel]s with a constant center point, but with an differing a size to represent
 * height.
 */
class Flat : Projection {
  private val projected = Projected()
  override fun project(
    camera: Camera,
    position: Position,
    pixelSize: Int
  ): Projected {
    val (x, y) = camera.translate(position)
    val zOffset = position.z shr 2
    projected.left = pixelSize * x - zOffset + camera.uiPosition.left
    projected.top = pixelSize * y - zOffset + camera.uiPosition.top
    projected.size = pixelSize + zOffset + zOffset
    return projected
  }

  override fun toString(): String = "Flat"
}
