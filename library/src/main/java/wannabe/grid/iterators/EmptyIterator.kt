package wannabe.grid.iterators

import java.util.NoSuchElementException

class EmptyIterator<T> : MutableIterator<T> {
  override fun hasNext(): Boolean = false

  override fun next(): T = throw NoSuchElementException("Empty iterator has no elements")

  override fun remove() =
    throw UnsupportedOperationException("Empty iterator has nothing to remove")
}
