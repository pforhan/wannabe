package wannabe.grid;

import java.util.Iterator;

import wannabe.Voxel;
import wannabe.grid.iterators.EmptyIterator;

/** A Grid that can hide or show its voxels. */
public class VisibilityGrid implements Grid {
  private static final AllNeighbors NO_NEIGHBORS = new AllNeighbors();
  
  private final String name;
  private final Grid source;

  private boolean visible = true;
  private boolean dirty = true;

  public VisibilityGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;

    return visible ? source.iterator() : new EmptyIterator();
  }

  @Override public boolean isDirty() {
    return realIsDirty();
  }

  @Override public int size() {
    return visible ? source.size() : 0;
  }

  public void show() {
    dirty = !visible;
    visible = true;
  }

  public void hide() {
    dirty = visible;
    visible = false;
  }

  /** Reverses visibility state. */
  public void toggle() {
    dirty = true;
    visible = !visible;
  }

  private boolean realIsDirty() {
    return dirty || source.isDirty();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    return visible ? source.neighbors(voxel) : NO_NEIGHBORS;
  } 

  @Override public String toString() {
    return name + "; visible? " + visible + " (size: " + size() + ")";
  }
}
