package wannabe.grid.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wannabe.Bounds;
import wannabe.Voxel;
import wannabe.grid.Grid;

public final class InBoundsIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;

  public InBoundsIterator(Grid source, Bounds bounds) {
    List<Voxel> inBounds = new ArrayList<>(source.size());
    for (Voxel voxel : source) {
      if (bounds.contains(voxel.position)) {
        inBounds.add(voxel);
      }
    }
    realIterator = inBounds.iterator();
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    return realIterator.next();
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}
