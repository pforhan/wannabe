package wannabe.projection

import wannabe.Camera
import wannabe.Position

/**
 * Renders a sequence of projections, changing every call to tick()
 * 
 * @param projections The projections in order to display
 */
class ProjectionSequence(
  val projections: List<Projection>
 ) : Projection {
  private var index: Int = 0;

  override fun project(
    camera: Camera,
    position: Position,
    pixelSize: Int
  ) = projections[index].project(camera, position, pixelSize)

  override fun tick() {
    if (++index >= projections.size) index = 0
  }

  override fun toString(): String = "ProjectionSequence $index ${projections[index]}"
 }
