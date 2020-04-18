package wannabe.grid.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import wannabe.Voxel;

public class EmptyIterator<T> implements Iterator<T> {
  @Override public boolean hasNext() {
    return false;
  }

  @Override public T next() {
    throw new NoSuchElementException("Empty iterator has no elements");
  }

  @Override public void remove() {
    throw new UnsupportedOperationException("Empty iterator has nothing to remove");
  }
}
