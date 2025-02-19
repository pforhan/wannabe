package wannabe.grid

import wannabe.Bounds
import wannabe.Voxel
import wannabe.grid.iterators.inBounds

/** A Grid that removes all voxels that lie outside the specified bounds.  */
class BoundingGrid(
  private val name: String,
  private val source: Grid,
  private val bounds: Bounds
) : Grid {
  override fun iterator(): Iterator<Voxel> = inBounds(bounds)

  override val isDirty: Boolean
    get() = source.isDirty

  // TODO this is a lie
  override val size: Int
    get() = source.size

  // TODO this is a lie
  override fun neighbors(voxel: Voxel): AllNeighbors = source.neighbors(voxel)

  override fun toString(): String = "$name bounding (size: $size)"
}
