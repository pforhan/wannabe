package wannabe.grid.iterators

class FilteringIterator<T>(
  private val delegate: Iterator<T>,
  private val predicate: (T) -> Boolean
) : Iterator<T> {

  private var nextValid: T? = null

  override fun hasNext(): Boolean {
    while (delegate.hasNext() && nextValid == null) {
      val next = delegate.next()
      if (predicate(next)) {
        nextValid = next
      }
    }
    return nextValid != null
  }

  override fun next(): T {
    if (!hasNext()) {
      throw NoSuchElementException()
    }
    val result = nextValid!!
    nextValid = null
    return result
  }
}
