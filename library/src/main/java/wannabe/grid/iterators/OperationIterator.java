package wannabe.grid.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import wannabe.Voxel;
import wannabe.grid.operations.Operation;

/**
 * Applies the specified Operation or operations to the given iterator.  Any Voxel not included
 * is excluded from iteration.
 */
public class OperationIterator implements Iterator<Voxel> {

  private final Iterator<Voxel> realIterator;
  private final Operation op;

  public OperationIterator(Iterator<Voxel> realIterator, Operation operation) {
    this.realIterator = realIterator;
    op = operation;
  }

  @Override public boolean hasNext() {
    return maybeAdvance();
  }

  @Override public Voxel next() {
    return advance();
  }

  @Override public void remove() {
    throw new UnsupportedOperationException("Remove not supported");
  }

  /**
   * Bookkeeping; do not use outside of maybeAdvance and advance. If not null, represents the next
   * value of the iterator.
   */
  private Voxel next;

  /**
   * Scans the source iterator for the next included Voxel. Safe to call at the end of the iterator.
   */
  private boolean maybeAdvance() {
    if (next != null) {
      return true;
    }

    while (realIterator.hasNext()) {
      Voxel temp = realIterator.next();
      if (op.includes(temp)) {
        next = temp;
        return true;
      }
    }
    return false;
  }

  /** Scans the source iterator for the next valid Voxel, or throws NoSuchElementException. */
  private Voxel advance() {
    // See if we've already found the next one.
    if (next != null) {
      Voxel temp = next;
      next = null;
      return temp;
    }

    while (realIterator.hasNext()) {
      Voxel temp = realIterator.next();
      if (op.includes(temp)) {
        // No need to set current here, just return the answer.
        return temp;
      }
    }

    throw new NoSuchElementException();
  }
}
