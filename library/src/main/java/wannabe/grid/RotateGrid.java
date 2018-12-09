package wannabe.grid;

import java.util.Iterator;
import wannabe.Voxel;
import wannabe.grid.iterators.RotatingIterator;
import wannabe.grid.iterators.RotatingIterator.RotationDegrees;

/** A Grid that applies a rotation function to a source grid and caches the results. */
public class RotateGrid implements Grid {
  private final String name;
  private final Grid source;

  private boolean dirty = true;
  private RotationDegrees rotation = new RotationDegrees();

  public RotateGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    return new RotatingIterator(source.iterator(), rotation.clone());
  }

  @Override public boolean isDirty() {
    return realIsDirty();
  }

  @Override public int size() {
    return source.size();
  }

  public void setRotate(int xDegrees, int yDegrees, int zDegrees) {
    rotation.x = xDegrees;
    rotation.y = yDegrees;
    rotation.z = zDegrees;
    dirty = true;
  }

  private boolean realIsDirty() {
    return dirty || source.isDirty();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    // TODO this is probably wrong, source likely won't have this voxel. Throw? do a reverse op?
    return source.neighbors(voxel);
  }

  @Override public String toString() {
    return name + "; " + rotation + "(size: " + size() + ")";
  }

}
