package wannabe

import wannabe.grid.AllNeighbors
import wannabe.grid.AllNeighbors.RelativePosition

/**
 * Describes real pixel color, location, and size for a voxel, with a variety of hints
 * to be used in final rendering.
 */
// TODO should I just put the Voxel in here?
open class Projected {
  var left = 0
  var top = 0
  var size = 0

  /** Horizontal space to use to indicate height.  */
  var hDepth = 0

  /** Vertical space to use to indicate height.  */
  var vDepth = 0
  // Hints about whether there are neighboring voxels nearby.
  /** A voxel is present at x, y-1, z  */
  var neighborNorth = false

  /** A voxel is present at x+1, y-1, z  */
  var neighborNorthEast = false

  /** A voxel is present at x-1, y-1, z  */
  var neighborNorthWest = false

  /** A voxel is present at x, y+1, z  */
  var neighborSouth = false

  /** A voxel is present at x+1, y+1, z  */
  var neighborSouthEast = false

  /** A voxel is present at x-1, y+1, z  */
  var neighborSouthWest = false

  /** A voxel is present at x-1, y, z  */
  var neighborWest = false

  /** A voxel is present at x+1, y, z  */
  var neighborEast = false

  /** A voxel is present at x, y, z+1  */
  var neighborAbove = false
  // TODO do I care about below? Seems like it'd be nice to know if only as a reflexive of above.
  /** A voxel is present at x, y, z-1  */
  var neighborBelow = false

  /** Returns `true` if this voxel has neighbor on north, south, east, west, and above.  */
  val isSurrounded: Boolean
    get() = neighborNorth && neighborSouth && neighborWest && neighborEast && neighborAbove

  fun neighborsFrom(neighbors: AllNeighbors) {
    neighborNorth = neighbors.same[RelativePosition.NORTH]
    neighborNorthEast = neighbors.same[RelativePosition.NORTHEAST]
    neighborEast = neighbors.same[RelativePosition.EAST]
    neighborSouthEast = neighbors.same[RelativePosition.SOUTHEAST]
    neighborSouth = neighbors.same[RelativePosition.SOUTH]
    neighborSouthWest = neighbors.same[RelativePosition.SOUTHWEST]
    neighborWest = neighbors.same[RelativePosition.WEST]
    neighborNorthWest = neighbors.same[RelativePosition.NORTHWEST]
    neighborAbove = neighbors.above[RelativePosition.CENTER]
    neighborBelow = neighbors.below[RelativePosition.CENTER]
  }

  /** Copies core (ie, non-neighbor) fields from `other` to this object.  */
  fun copyCoreFrom(other: Projected) {
    left = other.left
    top = other.top
    size = other.size
    hDepth = other.hDepth
    vDepth = other.vDepth
  }
}
