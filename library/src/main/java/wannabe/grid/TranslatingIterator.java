// Copyright 2015 Patrick Forhan
package wannabe.grid;

import java.util.Iterator;
import wannabe.Position;
import wannabe.Voxel;

public final class TranslatingIterator implements Iterator<Voxel> {
  private final Iterator<Voxel> realIterator;
  private final Position translation;
  private final Voxel workhorse = new Voxel(0, 0, 0, 0);

  public TranslatingIterator(Iterator<Voxel> realIterator, Position translation) {
    this.realIterator = realIterator;
    this.translation = translation;
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    Voxel real = realIterator.next();
    workhorse.color = real.color;
    workhorse.position.set(real.position);
    workhorse.position.add(translation);
//    System.out.println("Adding " + real.position + " and " + translation + " to yield " + workhorse.position);
    return workhorse;
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}