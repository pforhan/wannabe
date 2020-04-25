package wannabe

import wannabe.grid.AllNeighbors
import wannabe.grid.AllNeighbors.RelativePosition
import wannabe.grid.MutableGrid
import wannabe.util.Voxels.YPlotter
import wannabe.util.Voxels.ZPlotter

interface Bounds {
  operator fun contains(pos: Position): Boolean
  operator fun contains(pos: Translation): Boolean

  /** Indicates whether all specified neighbors are within bounds.  */
  fun containsAll(
    pos: Position,
    neighbors: AllNeighbors
  ): Boolean
}

/** A [Bounds] that accepts all voxels.  */
val UNBOUNDED: Bounds = object : Bounds {
  override fun contains(pos: Position): Boolean = true
  override fun contains(pos: Translation): Boolean = true
  override fun containsAll(
    pos: Position,
    neighbors: AllNeighbors
  ): Boolean = true
}

/** A 2d [Bounds] on the XY plane.  */
class XYBounds : Bounds {
  var left = 0
  var right = 0
  var top = 0
  var bottom = 0

  fun setFromWidthHeight(
    x: Int,
    y: Int,
    width: Int,
    height: Int
  ) {
    left = x
    top = y
    right = x + width
    bottom = y + height
  }

  fun isEdge(x: Int, y: Int): Boolean =
    x == left || x == right - 1 || y == top || y == bottom - 1

  override fun contains(pos: Position): Boolean =
    pos.x >= left && pos.x < right && pos.y >= top && pos.y < bottom

  override fun contains(pos: Translation): Boolean =
    pos.x >= left && pos.x < right && pos.y >= top && pos.y < bottom

  /** This XY bounds ignores above and below.  */
  override fun containsAll(
    pos: Position,
    neighbors: AllNeighbors
  ): Boolean {
    // Must contain the N,S,E,W neighbors, if they exist.
    return (contains(pos)
        && (!neighbors.same[RelativePosition.WEST] || pos.x - 1 >= left)
        && (!neighbors.same[RelativePosition.EAST] || pos.x + 1 < right)
        && (!neighbors.same[RelativePosition.NORTH] || pos.y - 1 >= top)
        && (!neighbors.same[RelativePosition.SOUTH] || pos.y + 1 < bottom))
  }

  fun plot(
    grid: MutableGrid,
    plotter: ZPlotter
  ) {
    for (row in top until bottom) {
      for (col in left until right) {
        val plotted = plotter.plot(col, row) ?: continue
        grid.put(plotted)
      }
    }
  }
}

/** A 2d [Bounds] on the XZ plane.  */
// TODO can these be merged without being confusing?
class XZBounds : Bounds {
  var left = 0
  var right = 0
  var top = 0
  var bottom = 0

  fun setFromWidthHeight(
    x: Int,
    z: Int,
    width: Int,
    height: Int
  ) {
    left = x
    top = z
    right = x + width
    bottom = z + height
  }

  fun isEdge(x: Int, z: Int): Boolean =
    x == left || x == right - 1 || z == top || z == bottom - 1

  override fun contains(pos: Position): Boolean =
    pos.x >= left && pos.x < right && pos.y >= top && pos.y < bottom

  override fun contains(pos: Translation): Boolean =
    pos.x >= left && pos.x < right && pos.y >= top && pos.y < bottom

  /** This XZ bounds ignores above and below.  */
  override fun containsAll(
    pos: Position,
    neighbors: AllNeighbors
  ): Boolean {
    // TODO I don't think this is updated for XZ
    // Must contain the N,S,E,W neighbors, if they exist.
    return (contains(pos)
        && (!neighbors.same[RelativePosition.WEST] || pos.x - 1 >= left)
        && (!neighbors.same[RelativePosition.EAST] || pos.x + 1 < right)
        && (!neighbors.same[RelativePosition.NORTH] || pos.y - 1 >= top)
        && (!neighbors.same[RelativePosition.SOUTH] || pos.y + 1 < bottom))
  }

  fun plot(
    grid: MutableGrid,
    plotter: YPlotter
  ) {
    for (row in top until bottom) {
      for (col in left until right) {
        val plotted = plotter.plot(col, row) ?: continue
        grid.put(plotted)
      }
    }
  }
}

