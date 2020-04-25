package wannabe.grid.iterators

import wannabe.Bounds
import wannabe.Voxel
import wannabe.grid.Grid
import java.util.ArrayList

// TODO this should be a streaming solution not a caching one
class InBoundsIterator(
  source: Grid,
  bounds: Bounds
) : MutableIterator<Voxel> {
  private val realIterator: MutableIterator<Voxel>

  init {
    val inBounds = source.filter { bounds.contains(it.position) }
        .toMutableList()
    realIterator = inBounds.iterator()
  }

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel = realIterator.next()

  // TODO should I support remove?
  override fun remove() = realIterator.remove()
}
