package wannabe.grid.iterators

import wannabe.Voxel
import wannabe.grid.operations.Operation
import java.util.NoSuchElementException

/**
 * Applies the specified Operation or operations to the given iterator.  Any Voxel not included
 * is excluded from iteration.
 */
class OperationIterator(
  private val realIterator: Iterator<Voxel>,
  private val op: Operation
) : Iterator<Voxel> {
  /**
   * Bookkeeping; do not use outside of maybeAdvance and advance. If not null, represents the next
   * value of the iterator.
   */
  private var next: Voxel? = null


  override fun hasNext(): Boolean = maybeAdvance()

  override fun next(): Voxel = advance()

  /**
   * Scans the source iterator for the next included Voxel. Safe to call at the end of the iterator.
   */
  private fun maybeAdvance(): Boolean {
    if (next != null) {
      return true
    }
    while (realIterator.hasNext()) {
      val temp = realIterator.next()
      if (op.includes(temp)) {
        next = temp
        return true
      }
    }
    return false
  }

  /** Scans the source iterator for the next valid Voxel, or throws NoSuchElementException.  */
  private fun advance(): Voxel {
    // See if we've already found the next one.
    next?.let {
      next = null
      return it
    }

    while (realIterator.hasNext()) {
      val temp = realIterator.next()
      if (op.includes(temp)) {
        // No need to set current here, just return the answer.
        return temp
      }
    }
    throw NoSuchElementException()
  }

}
