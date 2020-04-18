package wannabe.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wannabe.Voxel;
import wannabe.grid.iterators.IteratingIterator;

/** A Grid that aggregates other Grids. */
public class GroupGrid implements Grid {
  private final String name;
  private final List<Grid> sources = new ArrayList<>();

  private boolean dirty = true;

  public GroupGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;
    return new IteratingIterator<>(sources.iterator());
  }

  @Override public boolean isDirty() {
    if (dirty) return true;

    for (Grid source : sources) {
      if (source.isDirty()) {
        return true;
      }
    }

    return false;
  }

  @Override public int size() {
    int total = 0;
    for (Grid source : sources) {
      total += source.size();
    }
    return total;
  }

  public Iterable<Grid> grids() {
    return sources;
  }

  /** Gets the indexth grid in this group. */
  public Grid grid(int index) {
    return sources.get(index);
  }

  public void add(Grid grid) {
    if (sources.contains(grid)) {
      throw new IllegalArgumentException("Trying to add grid " + grid + " a second time.");
    }
    if (grid == this) {
      throw new IllegalArgumentException("Trying to add self");
    }
    dirty = true;
    sources.add(grid);
  }

  public void remove(Grid grid) {
    boolean removed = sources.remove(grid);
    if (!removed) {
      throw new IllegalArgumentException("Trying to remove " + grid + " not in Group");
    }
  }

  AllNeighbors sum = new AllNeighbors();

  @Override public AllNeighbors neighbors(Voxel voxel) {
    // Ask each child grid about neighbors for this voxel. We need to aggregate all answers, however,
    // effectively forming a bitwise OR over them all. If any of them report a neighbor we keep it.
    // TODO can this be more efficient?  Even if child grids are caching, this one is not.

    sum.clear();

    for (Grid source : sources) {
      AllNeighbors allNeighbors = source.neighbors(voxel);
      sum.orWith(allNeighbors);
    }
    return sum;
  }

  @Override public String toString() {
    return name + "; children #" + sources.size() + " (size: " + size() + ")";
  }
}
