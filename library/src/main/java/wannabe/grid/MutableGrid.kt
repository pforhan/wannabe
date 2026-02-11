package wannabe.grid

import wannabe.Voxel

/** A grid that supports adding or removing voxels. TODO we should be able to drop this  */
interface MutableGrid : Grid {
  /** Places a single [Voxel].  */
  fun put(v: Voxel)

  /** Returns `true` if the specified voxel was removed.  */
  fun remove(v: Voxel)

  fun clear()

  /**
   * Indicates to the grid that now is an appropriate time to perform any optimizations on
   * this grid, such as sort by painter's algorithm, etc.
   */
  fun optimize()
}
