package wannabe.grid.iterators

import java.util.NoSuchElementException

class EmptyIterator<T> : Iterator<T> {
  override fun hasNext(): Boolean = false

  override fun next(): T = throw NoSuchElementException("Empty iterator has no elements")
}
