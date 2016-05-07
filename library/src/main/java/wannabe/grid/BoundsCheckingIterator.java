package wannabe.grid;

import java.util.Iterator;
import wannabe.Bounds;
import wannabe.Voxel;

public final class BoundsCheckingIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;
  private final Bounds bounds;

  public BoundsCheckingIterator(Iterator<Voxel> realIterator, Bounds bounds) {
    this.realIterator = realIterator;
    this.bounds = bounds;
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    Voxel real = realIterator.next();

    if (bounds.contains(real.position)) {
      return real
    }

    if (translation.isZero()) return real;
    Voxel newVox = new Voxel(workhorse.set(real.position).add(translation).asPosition(), real.color);
    return newVox;
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}