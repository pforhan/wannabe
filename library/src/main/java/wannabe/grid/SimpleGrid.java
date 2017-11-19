package wannabe.grid;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import wannabe.Bounds;
import wannabe.Pos;
import wannabe.Position;
import wannabe.Translation;
import wannabe.Voxel;

import static wannabe.grid.AllNeighbors.RelativePosition.CENTER;
import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

/** {@link Grid} implementation that has a simple list of {@link Voxel}s and a means to sort. */
public class SimpleGrid implements MutableGrid {

  /** Sorts by Z increasing then X increasing then Y increasing. */
  private static final Comparator<Pos> zxyIncreasing = new Comparator<Pos>() {
    @Override public int compare(Pos o1, Pos o2) {
      int zCmp = o1.z() - o2.z();
      if (zCmp != 0) {
        return zCmp;
      }

      int xCmp = o1.x() - o2.x();
      if (xCmp != 0) {
        return xCmp;
      }

      return o1.y() - o2.y();
    }

  };

  private final Map<Position, Voxel> positionToVoxels = new TreeMap<>(zxyIncreasing);
  private final Translation translation = new Translation(0, 0, 0);
  private final String name;
  private final boolean ignoreDuplicatePositions;
  private boolean dirty;

  public SimpleGrid(String name) {
    this(name, false);
  }

  public SimpleGrid(String name, boolean ignoreDuplicatePositions) {
    this.name = name;
    this.ignoreDuplicatePositions = ignoreDuplicatePositions;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;
    Iterator<Voxel> realIterator = positionToVoxels.values().iterator();
    return translation.isZero() ? realIterator : new TranslatingIterator(realIterator, translation);
  }

  @Override public boolean isDirty() {
    return dirty;
  }

  /** Sorts the voxels by z order, from lowest to highest. */
  @Override public void optimize() {
  }

  @Override public void add(Voxel v) {
    dirty = true;
    if (positionToVoxels.containsKey(v.position)) {
      if (ignoreDuplicatePositions) return;
      throw new IllegalArgumentException("Duplicate voxel at " + v.position);
    }
    positionToVoxels.put(v.position, v);
  }

  @Override public void remove(Voxel v) {
    dirty = true;
    positionToVoxels.remove(v.position);
  }

  @Override public void clear() {
    dirty = true;
    positionToVoxels.clear();
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds, boolean includeHidden) {
    dirty = false;
    if (includeHidden) {
      exportWithHidden(grid, bounds);
      return;
    }

    exportNoHidden(grid, bounds);
  }

  final AllNeighbors theNeighbors = new AllNeighbors();
  final Translation workhorse = new Translation(0, 0, 0);

  @Override public AllNeighbors neighbors(Voxel voxel) {
    populateNeighbors(voxel);
    return theNeighbors;
  }

  void populateNeighbors(Voxel voxel) {
    theNeighbors.clear();
    workhorse.set(voxel.position);
    // Above:
    workhorse.z++;
    theNeighbors.above.set(CENTER, positionToVoxels.get(workhorse) != null);

    // Below:
    workhorse.z -= 2;
    theNeighbors.below.set(CENTER, positionToVoxels.get(workhorse) != null);

    // North:
    workhorse.z++;
    workhorse.y--;
    theNeighbors.same.set(NORTH, positionToVoxels.get(workhorse) != null);

    // South:
    workhorse.y += 2;
    theNeighbors.same.set(SOUTH, positionToVoxels.get(workhorse) != null);

    // East:
    workhorse.y--;
    workhorse.x++;
    theNeighbors.same.set(EAST, positionToVoxels.get(workhorse) != null);

    // West:
    workhorse.x -= 2;
    theNeighbors.same.set(WEST, positionToVoxels.get(workhorse) != null);
  }

  @Override public void translate(Translation offset) {
    dirty = true;
    translation.add(offset);
  }

  @Override public void clearTranslation() {
    dirty = true;
    translation.zero();
  }

  @Override public int size() {
    return positionToVoxels.size();
  }

  @Override public String toString() {
    return name + "(size: " + size() + ")";
  }

  /** Returns {@code true} if a voxel is not surrounded, or if it has neighbors out of bounds. */
  private boolean notSurroundedInBounds(Bounds bounds, Voxel voxel) {
    populateNeighbors(voxel);

    return !theNeighbors.isSurrounded() || !bounds.containsAll(voxel.position, theNeighbors);
  }

  private void exportNoHidden(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (Voxel voxel : positionToVoxels.values()) {
        if (bounds.contains(voxel.position)
            && notSurroundedInBounds(bounds, voxel)) {
          grid.add(voxel);
        }
      }
      return;
    }

    // Otherwise, we have to translate all the things.
    Translation workhorse = new Translation(0, 0, 0);
    for (Voxel voxel : positionToVoxels.values()) {
      workhorse.set(voxel.position).add(translation);
      if (bounds.contains(workhorse)
          && notSurroundedInBounds(bounds, voxel)) {
        // TODO double check if we need to handle translation with bounds
        grid.add(new Voxel(workhorse.asPosition(), voxel.color));
      }
    }
  }

  // TODO this is a bit of an ugly near-duplication, but seems good for clarity and perf.
  // TODO consider if this should just be the api instead of the bool param.
  private void exportWithHidden(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (Voxel voxel : positionToVoxels.values()) {
        if (bounds.contains(voxel.position)) {
          grid.add(voxel);
        }
      }
      return;
    }

    // Otherwise, we have to translate all the things.
    Translation workhorse = new Translation(0, 0, 0);
    for (Voxel voxel : positionToVoxels.values()) {
      workhorse.set(voxel.position).add(translation);
      if (bounds.contains(workhorse)) {
        // TODO double check if we need to handle translation with bounds
        grid.add(new Voxel(workhorse.asPosition(), voxel.color));
      }
    }
  }
}
