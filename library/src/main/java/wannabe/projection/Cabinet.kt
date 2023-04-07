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

  companion object {
    fun horizontalSequence(
      vOffset: Int,
      hStart: Int,
      hEnd: Int,
      bounce: Boolean
    ): ProjectionSequence {
      var range = (hStart..hEnd).toList()
      // Remove the last entry since it'll be added again.
      // TODO should we also remove the last entry of the combined since it would match the first?
      println("range before $range")
      if (bounce) range = range.subList(0, range.size - 1) + range.reversed()
      println("range after $range")
      return ProjectionSequence(
        projections = range.map {
          Cabinet(
            vOffsetPerZ = vOffset,
            hOffsetPerZ = it
          )
        }
      )
    }
  }
}
