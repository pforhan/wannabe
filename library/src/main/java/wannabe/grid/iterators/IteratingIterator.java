package wannabe.grid.iterators;

import java.util.Iterator;
import wannabe.Voxel;
import wannabe.grid.Grid;

/** Iterates over a list of iterators, advancing to the next one as each is exhausted. */
public class IteratingIterator implements Iterator<Voxel> {
  private final Iterator<Grid> gridIterator;
  private Iterator<Voxel> current = new EmptyIterator();

  public IteratingIterator(Iterator<Grid> gridIterator) {
    this.gridIterator = gridIterator;
  }

  @Override public boolean hasNext() {
    maybeAdvance();
    return false;
  }

  @Override public Voxel next() {
    maybeAdvance();
    return current.next();
  }

  @Override public void remove() {
    throw new UnsupportedOperationException("Remove not supported");
  }

  private void maybeAdvance() {
    // If the current one is used up but there is another grid, advance:
    if (!current.hasNext() && gridIterator.hasNext()) {
      current = gridIterator.next().iterator();
    }
  }

}
