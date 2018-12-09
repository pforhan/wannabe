package wannabe.grid;

import java.util.Iterator;
import wannabe.Translation;
import wannabe.Voxel;
import wannabe.grid.iterators.TranslatingIterator;

/** A Grid that applies a rotation function to a source grid and caches the results. */
public class TranslateGrid implements Grid {
  private final String name;
  private final Grid source;
  private final Translation translation = new Translation(0, 0, 0);

  private boolean dirty = true;

  public TranslateGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;

    Iterator<Voxel> realIterator = source.iterator();
    return translation.isZero() ? realIterator : new TranslatingIterator(realIterator, translation);
  }

  @Override public boolean isDirty() {
    return realIsDirty();
  }

  @Override public int size() {
    return source.size();
  }

  /** Translates every {@link Voxel} in this grid by the specified offset. Additive. */
  public void translate(Translation offset) {
    if (offset.isZero()) return;
    dirty = true;
    translation.add(offset);
  }

  /** Resets translation to zero. */
  public void clearTranslation() {
    if (translation.isZero()) return;
    dirty = true;
    translation.zero();
  }

  private boolean realIsDirty() {
    return dirty || source.isDirty();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    // TODO this is wrong, isn't it?  I need to translate to source space, ie, subtract translation
    // TODO and what's more, a translated voxel won't even exist in the source grid...
    // TODO so what to do?  Could translate the position and make a new voxel (and add Voxel.equals)
    // TODO or could throw. Throwing would encourage lots of caching I presume?
    return source.neighbors(voxel);
  }

  @Override public String toString() {
    return name + "; " + translation + " (size: " + size() + ")";
  }
}
