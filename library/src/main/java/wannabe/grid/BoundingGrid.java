package wannabe.grid;

import java.util.Iterator;
import wannabe.Bounds;
import wannabe.Voxel;
import wannabe.grid.iterators.InBoundsIterator;

/** A Grid that removes all voxels that lie outside the specified bounds. */
public class BoundingGrid implements Grid {
  private final String name;
  private final Grid source;
  private final Bounds bounds;

  public BoundingGrid(String name, Grid source, Bounds bounds) {
    this.name = name;
    this.source = source;
    this.bounds = bounds;
  }

  @Override public Iterator<Voxel> iterator() {
    return new InBoundsIterator(source, bounds);
  }

  @Override public boolean isDirty() {
    return source.isDirty();
  }

  @Override public int size() {
    // TODO this is a lie
    return source.size();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    // TODO this is a lie
    return source.neighbors(voxel);
  }

  @Override public String toString() {
    return name + " no hidden (size: " + size() + ")";
  }
}
