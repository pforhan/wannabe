package wannabe.grid

import java.util.BitSet

/** References to neighboring voxels.  */
class AllNeighbors {
  /** Positions in a 3x3 grid. */
  interface RelativePosition {
    companion object {
      const val NORTH = 0
      const val NORTHEAST = 1
      const val EAST = 2
      const val SOUTHEAST = 3
      const val SOUTH = 4
      const val SOUTHWEST = 5
      const val WEST = 6
      const val NORTHWEST = 7
      const val CENTER = 8
    }
  }

  /** Whether there's a neighbor at z+1, indexed by RelativePosition.ordinal.  */
  val above = BitSet(9)

  /** Whether there's a neighbor at z-1, indexed by RelativePosition.ordinal.  */
  val below = BitSet(9)

  /** Whether there's a neighbor at the same z, indexed by RelativePosition.ordinal.  */
  val same = BitSet(9)

  /** Resets all indicators to no neighbors.  */
  fun clear() {
    above.clear()
    below.clear()
    same.clear()
  }

  /** Returns `true` if the voxel can't be visible. Ignores "below" in this calculation.  */
  val isSurrounded: Boolean
    get() = (above[RelativePosition.CENTER]
        && same[RelativePosition.NORTH]
        && same[RelativePosition.EAST]
        && same[RelativePosition.SOUTH]
        && same[RelativePosition.WEST])

  fun orWith(other: AllNeighbors) {
    above.or(other.above)
    below.or(other.below)
    same.or(other.same)
  }
}
