package wannabe.grid.iterators;

import java.util.Iterator;
import wannabe.Translation;
import wannabe.Voxel;

public final class TranslatingIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;
  private final Translation translation;
  /** Just a reused object to do math for us. */
  private final Translation workhorse = new Translation(0, 0, 0);

  public TranslatingIterator(Iterator<Voxel> realIterator, Translation translation) {
    this.realIterator = realIterator;
    this.translation = translation;
    if (translation.isZero()) {
      throw new IllegalStateException("Don't use a translatingIterator unless you need to!");
    }
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    Voxel real = realIterator.next();
    return new Voxel(workhorse.set(real.position).add(translation).asPosition(), real.value);
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}
