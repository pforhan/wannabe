package wannabe.grid

import wannabe.Voxel

/**
 * A collection of [Voxel]s. Voxels do not move within to the grid, although the grid's
 * set of voxels may change (see [MutableGrid]) or the translation of the grid itself
 * may change.
 */
interface Grid : Iterable<Voxel> {
  /** Returns an iterator over all [Voxel]s in this grid. */
  override fun iterator(): Iterator<Voxel>

  /** Number of Voxels this grid contains.  */
  val size: Int

  /**
   * Returns `true` if anything about this grid has changed (such as voxels or translation)
   * since the last time [.iterator] was called.
   */
  val isDirty: Boolean

  /**
   * Gets the neighbor information for the specified [Voxel]. Implementations may reuse the
   * returned instance, so it's only valid to use until the next [neighbors()] call
   */
  fun neighbors(voxel: Voxel): AllNeighbors
}
