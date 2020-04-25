package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.EmptyIterator

/** A Grid that can hide or show its voxels.  */
class VisibilityGrid(
  private val name: String,
  private val source: Grid
) : Grid {
  private var visible = true
  private var dirty = true

  override fun iterator(): Iterator<Voxel> {
    dirty = false
    return if (visible) source.iterator() else EmptyIterator()
  }

  override val isDirty: Boolean
    get() = realIsDirty()

  override val size: Int
    get() = if (visible) source.size else 0

  fun show() {
    dirty = !visible
    visible = true
  }

  fun hide() {
    dirty = visible
    visible = false
  }

  /** Reverses visibility state.  */
  fun toggle() {
    dirty = true
    visible = !visible
  }

  private fun realIsDirty(): Boolean {
    return dirty || source.isDirty
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    return if (visible) source.neighbors(voxel) else NO_NEIGHBORS
  }

  override fun toString(): String {
    return "$name; visible? $visible (size: $size)"
  }

  companion object {
    private val NO_NEIGHBORS = AllNeighbors()
  }
}
