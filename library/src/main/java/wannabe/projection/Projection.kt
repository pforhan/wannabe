package wannabe.projection

import wannabe.Camera
import wannabe.Position
import wannabe.Projected

/** Converts a 3d Position to a 2d coordinate for rendering.  */
interface Projection {
  /**
   * Resolves a [Position] to a two-dimensional location relative to the specified
   * [Camera] with a height-0 size of `pixelSize`.  Projections should honor
   * [Camera.uiPosition] to allow the UI to render voxels appropriately.
   * Implementations may reuse the returned instance for performance.
   */
  fun project(
    camera: Camera,
    position: Position,
    pixelSize: Int
  ): Projected
}
