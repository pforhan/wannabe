package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.IteratingIterator
import java.util.ArrayList

/** A Grid that aggregates other Grids.  */
class GroupGrid(
  private val name: String
) : Grid {
  private val sources: MutableList<Grid> = mutableListOf()
  private var dirty = true

  override fun iterator(): Iterator<Voxel> {
    dirty = false
    return IteratingIterator(sources.iterator())
  }

  override val isDirty: Boolean
    get() = dirty || sources.any { it.isDirty }

  override val size: Int
    get() = sources.fold(0) { acc, grid ->
      acc + grid.size
    }

  fun grids(): Iterable<Grid> = sources

  /** Gets the indexth grid in this group.  */
  fun grid(index: Int): Grid = sources[index]

  fun add(grid: Grid) {
    require(!sources.contains(grid)) { "Trying to add grid $grid a second time." }
    require(grid !== this) { "Trying to add self" }
    dirty = true
    sources.add(grid)
  }

  fun remove(grid: Grid) {
    val removed = sources.remove(grid)
    require(removed) { "Trying to remove $grid not in Group" }
  }

  var sum = AllNeighbors()

  // Ask each child grid about neighbors for this voxel. We need to aggregate all answers, however,
  // effectively forming a bitwise OR over them all. If any of them report a neighbor we keep it.
  // TODO can this be more efficient?  Even if child grids are caching, this one is not.
  override fun neighbors(voxel: Voxel): AllNeighbors {
    sum.clear()
    return sources.fold(sum) { sum, grid ->
      sum.apply { orWith(grid.neighbors(voxel)) }
    }
  }

  override fun toString(): String = "$name; children #${sources.size} (size: $size)"
}
