package wannabe.grid;

import java.util.Iterator;
import wannabe.Translation;
import wannabe.Voxel;

public final class TranslatingIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;
  private final Translation translation;
  private final Translation workhorse = new Translation(0, 0, 0);

  public TranslatingIterator(Iterator<Voxel> realIterator, Translation translation) {
    this.realIterator = realIterator;
    this.translation = translation;
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    Voxel real = realIterator.next();
    if (translation.isZero()) return real;
    Voxel newVox = new Voxel(workhorse.set(real.position).add(translation).asPosition(), real.value);
    return newVox;
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}