package wannabe.grid.operations

import wannabe.Voxel

/** A unit of work to perform on a Voxel. May optionally perform filtering as well.  */
interface Operation {
  /** Whether this operation has changed since the last time it was used.  */
  open val isDirty: Boolean
    get() = false

  /** Whether the given Voxel should be included.  */
  open fun includes(voxel: Voxel): Boolean = true

  /** Performs the operation on a Voxel, returning a new Voxel instance.  */
  abstract fun apply(voxel: Voxel): Voxel
}
