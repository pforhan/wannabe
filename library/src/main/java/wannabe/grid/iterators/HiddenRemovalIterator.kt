package wannabe.grid.iterators

import wannabe.Voxel
import wannabe.grid.Grid
import java.util.ArrayList

// TODO this should be a streaming solution not a caching one
class HiddenRemovalIterator(
  source: Grid
) : MutableIterator<Voxel> {
  private val realIterator: MutableIterator<Voxel>

  init {
    val nonHidden = source.filter { !source.neighbors(it).isSurrounded }
        .toMutableList()
    realIterator = nonHidden.iterator()
  }

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel = realIterator.next()

  // TODO should I support remove?
  override fun remove() = realIterator.remove()
}
