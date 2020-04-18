package wannabe.grid;

import java.util.Iterator;
import wannabe.Voxel;

/** A Grid that caches the output of the source Grid and delegates calls to the cache. */
public class CachingGrid implements Grid {
  private final String name;
  private final Grid source;
  private final SimpleGrid dest = new SimpleGrid("cache");

  public CachingGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    maybeClearAndFillDest();
    return dest.iterator();
  }

  @Override public boolean isDirty() {
    // Capture dirty status now, since if it is dirty we'll clean it by grabbing from source again.
    // TODO doesn't this mean a second call won't be right?  but maybe that's okay?
    boolean wasDirty = source.isDirty();
    maybeClearAndFillDest();
    return wasDirty;
  }

  @Override public int size() {
    maybeClearAndFillDest();
    return dest.size();
  }

  private void maybeClearAndFillDest() {
    if (source.isDirty()) {
      dest.clear();
      for (Voxel voxel : source) {
        dest.put(voxel);
      }
    }
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    maybeClearAndFillDest();
    return dest.neighbors(voxel);
  }

  @Override public String toString() {
    return name + " cache (size: " + size() + ")";
  }
}
