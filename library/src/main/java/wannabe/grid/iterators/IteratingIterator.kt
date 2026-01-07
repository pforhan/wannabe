package wannabe.grid.iterators

import java.util.Arrays
import java.util.HashSet

/**
 * Depth-first iterates over a group of [Iterable]s, iterating each in turn, advancing
 * to the next one as each is exhausted.
 */
class IteratingIterator<T>(private val iterableIterator: Iterator<Iterable<T>>) :
    Iterator<T> {
  private var current: Iterator<T> = EmptyIterator()

  override fun hasNext(): Boolean {
    maybeAdvance()
    return current.hasNext()
  }

  override fun next(): T {
    maybeAdvance()
    return current.next()
  }

  private fun maybeAdvance() {
    // If the current one is used up but there is another grid, advance:
    while (!current.hasNext() && iterableIterator.hasNext()) {
      // Grab the next grid and its voxel iterator:\
      current = iterableIterator.next().iterator()
    }
  }

  companion object {
    @JvmStatic fun main(args: Array<String>) {
      val first: Set<String> = setOf("first one", "first two", "first three")
      val second: Set<String> = setOf("sec one", "sec two")
      val third: Set<String> = setOf("th one", "th two", "th three")
      val test = IteratingIterator(
          Arrays.asList(first, second, third).iterator()
      )
      while (test.hasNext()) {
        println("test " + test.next())
      }
    }
  }
}
