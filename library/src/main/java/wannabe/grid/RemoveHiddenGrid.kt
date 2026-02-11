package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.hiddenRemoval

/**
 * A Grid that removes all hidden voxels -- those with neighbors to the top, bottom, left, right
 * and in above. Leaves only voxels that could be visible. See also
 * [AllNeighbors.isSurrounded] for more details.
 */
class RemoveHiddenGrid(
  private val name: String,
  private val source: Grid
) : Grid {
  override fun iterator(): Iterator<Voxel> = hiddenRemoval()

  override val isDirty: Boolean
    get() = source.isDirty

  // TODO this is a lie
  override val size: Int
    get() = source.size

  // Technically not true, but in practical terms may be handy to treat these as default
  override fun neighbors(voxel: Voxel): AllNeighbors = source.neighbors(voxel)

  override fun toString(): String {
    return "$name no hidden (size: $size)"
  }
}
