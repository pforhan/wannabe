package wannabe

import wannabe.grid.Grid
import wannabe.projection.Projection

/** Implementors know how to render a [Grid] to a device.  */
interface UI {
  var camera: Camera

  fun setSize(
    width: Int,
    height: Int
  )

  fun setGrid(grid: Grid)
  fun setProjection(projection: Projection)

  /** Called when the clock advances and the UI should be rendered.  */
  fun render()
}
