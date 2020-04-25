package wannabe.grid.operations

import wannabe.Voxel

/** Applies specified operations serially, applying only those that are included by all.  */
class GroupOperation(
  vararg val operations: Operation
) : Operation {
  override val isDirty: Boolean
    get() = operations.any { it.isDirty }

  /** Includes a voxel if all operations include it.  */
  override fun includes(voxel: Voxel): Boolean = operations.all { it.includes(voxel) }

  override fun apply(voxel: Voxel): Voxel = operations.fold(voxel) { out, op ->
    op.apply(out)
  }
}
