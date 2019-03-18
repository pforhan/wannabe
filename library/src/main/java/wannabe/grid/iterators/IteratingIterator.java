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
    return current.hasNext();
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
    while (!current.hasNext() && gridIterator.hasNext()) {
      // Grab the next grid and its voxel iterator:\
      current = gridIterator.next().iterator();
    }
  }
}
