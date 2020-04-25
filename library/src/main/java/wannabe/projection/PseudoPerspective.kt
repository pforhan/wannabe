package wannabe.projection

import wannabe.Camera
import wannabe.Position
import wannabe.Projected

/**
 * Renders [Voxel]s with a simple perspective shift for height. The farther from the camera,
 * and the higher the Z value, the greater the shift. Higher Z values also get a larger size. A
 * Voxel at the same x and y position as the camera would only get a shift due to its height.
 *
 * This class doesn't calculate a real perspective offset. Instead, it adds a simple x and/or y
 * pixel offset for increasing distance from the camera. All voxels at the same Z height have the
 * same size.
 * TODO change that last bit; as x or y offset increase, z should decrease a bit.
 * TODO xDiff and yDiff are too drastic.
 */
class PseudoPerspective : Projection {
  private val projected = Projected()
  override fun project(
    camera: Camera,
    position: Position,
    pixelSize: Int
  ): Projected {
    // Determine distance from camera:
    val xDiff = camera.position.x - position.x
    val yDiff = camera.position.y - position.y
    val zDiff = camera.position.z - position.z

    // Get the rough location to draw from:
    val (x, y, z) = camera.translate(position)
    projected.size = pixelSize - zDiff
    val halfSize = projected.size shr 1
    projected.left = pixelSize * x - z * xDiff + camera.uiPosition.left - halfSize
    projected.top = pixelSize * y - z * yDiff + camera.uiPosition.top - halfSize
    // TODO come up with better depth calcs... though this works decently
    projected.hDepth = xDiff
    projected.vDepth = yDiff
    return projected
  }

  override fun toString(): String {
    return "PseudoPerspective"
  }
}
