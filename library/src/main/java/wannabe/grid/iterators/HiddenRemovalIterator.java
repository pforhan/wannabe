package wannabe.grid.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wannabe.Voxel;
import wannabe.grid.Grid;

public final class HiddenRemovalIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;

  public HiddenRemovalIterator(Grid source) {
    List<Voxel> nonHidden = new ArrayList<>(source.size());
    for (Voxel voxel : source) {
      if (!source.neighbors(voxel).isSurrounded()) {
        nonHidden.add(voxel);
      }
    }
    realIterator = nonHidden.iterator();
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
