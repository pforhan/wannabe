package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.SingleIterator

/** A Grid that has a single voxel in it. */
class SingleGrid(
  private val name: String,
  var voxel: Voxel = Voxel(0, 0, 0, 0)
) : Grid {

  private val noNeighbors = AllNeighbors()

  override fun iterator(): Iterator<Voxel> = SingleIterator(voxel)

  override val isDirty: Boolean = true // TODO just implement for real

  override val size: Int = 1

  override fun neighbors(voxel: Voxel): AllNeighbors = noNeighbors

  override fun toString(): String = "$name single"

}
