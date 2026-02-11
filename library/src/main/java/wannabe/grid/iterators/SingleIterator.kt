package wannabe.grid.iterators

import java.util.NoSuchElementException

class SingleIterator<T>(
  val single: T
) : Iterator<T> {
  var used = false

  override fun hasNext(): Boolean = !used

  override fun next(): T = if (used) {
      throw NoSuchElementException("Single iterator has no more elements") 
    } else {
      used = true
      single
    }
}
