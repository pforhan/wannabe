package wannabe.grid;

import java.util.Iterator;
import wannabe.Voxel;
import wannabe.grid.iterators.HiddenRemovalIterator;

/**
 * A Grid that removes all hidden voxels -- those with neighbors to the top, bottom, left, right
 * and in front. Leaves only voxels that could be visible. See also
 * {@link AllNeighbors#isSurrounded()} for more details.
 */
public class RemoveHiddenGrid implements Grid {
  private final String name;
  private final Grid source;

  public RemoveHiddenGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    return new HiddenRemovalIterator(source);
  }

  @Override public boolean isDirty() {
    return source.isDirty();
  }

  @Override public int size() {
    // TODO this is a lie
    return source.size();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    // Technically not true, but in practical terms may be handy to treat these as default
    return source.neighbors(voxel);
  }

  @Override public String toString() {
    return name + " no hidden (size: " + size() + ")";
  }
}
