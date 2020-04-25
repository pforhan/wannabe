package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.OperationIterator
import wannabe.grid.operations.GroupOperation
import wannabe.grid.operations.Operation

/**
 * A Grid that caches the output of the specified operation(s) applied to the source Grid's data.
 */
class OperationGrid(
  private val source: Grid,
  private val op: Operation
) : Grid {

  constructor(
    source: Grid,
    vararg operations: Operation
  ) : this(source, GroupOperation(*operations))

  override fun iterator(): Iterator<Voxel> {
    return OperationIterator(source.iterator(), op)
  }

  override val isDirty: Boolean
    get() = source.isDirty && op.isDirty

  override val size: Int
    get() = throw IllegalStateException("Unable to determine size.")

  override fun neighbors(voxel: Voxel): AllNeighbors =
    throw IllegalStateException("Unable to determine neighbors.")

  // TODO include op names or something
  override fun toString(): String = "op grid"
}
