package wannabe.grid;

import java.util.Comparator;
import java.util.HashMap;
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
import static wannabe.grid.AllNeighbors.RelativePosition.NORTHEAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTHWEST;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTHEAST;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTHWEST;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

/** {@link Grid} implementation that has a simple list of {@link Voxel}s and a means to sort. */
public class SimpleGrid implements MutableGrid {

  /** Sorts by Z increasing then X increasing then Y increasing. */
  private static final Comparator<Pos> zxyIncreasing = (o1, o2) -> {
    int zCmp = o1.z() - o2.z();
    if (zCmp != 0) {
      return zCmp;
    }

    int xCmp = o1.x() - o2.x();
    if (xCmp != 0) {
      return xCmp;
    }

    return o1.y() - o2.y();
  };

  private final Map<Position, Voxel> positionToVoxels = new TreeMap<>(zxyIncreasing);
  private final Map<Voxel, AllNeighbors> neighborCache = new HashMap<>();
  private final Translation translation = new Translation(0, 0, 0);
  private final String name;
  private final Translation workhorse = new Translation(0, 0, 0);

  private boolean dirty;

  public SimpleGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;
    return positionToVoxels.values().iterator();
  }

  @Override public boolean isDirty() {
    return dirty;
  }

  /** Sorts the voxels by z order, from lowest to highest. */
  @Override public void optimize() {
    // TODO why is this empty?
  }

  @Override public void put(Voxel v) {
    if (v == null) throw new IllegalArgumentException("Voxel may not be null.");
    markDirty();
    positionToVoxels.put(v.position, v);
  }

  @Override public void remove(Voxel v) {
    markDirty();
    positionToVoxels.remove(v.position);
  }

  @Override public void clear() {
    markDirty();
    positionToVoxels.clear();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    AllNeighbors theNeighbors = neighborCache.get(voxel);
    if (theNeighbors == null) {
      theNeighbors = createAndPopulateNeighbors(voxel);
      neighborCache.put(voxel, theNeighbors);
    }

    return theNeighbors;
  }

  private void markDirty() {
    dirty = true;
    neighborCache.clear();
  }

  private AllNeighbors createAndPopulateNeighbors(Voxel voxel) {
    AllNeighbors theNeighbors = new AllNeighbors();
    theNeighbors.clear();
    workhorse.set(voxel.position);
    // Above:
    workhorse.z++;
    theNeighbors.above.set(CENTER, positionToVoxels.get(workhorse) != null);

    // Below:
    workhorse.z -= 2;
    theNeighbors.below.set(CENTER, positionToVoxels.get(workhorse) != null);

    workhorse.z++;
    // Now scan 8 surrounding positions on the same plane, starting at north, going clockwise.
    workhorse.y--;
    theNeighbors.same.set(NORTH, positionToVoxels.get(workhorse) != null);
    workhorse.x++;
    theNeighbors.same.set(NORTHEAST, positionToVoxels.get(workhorse) != null);
    workhorse.y++;
    theNeighbors.same.set(EAST, positionToVoxels.get(workhorse) != null);
    workhorse.y++;
    theNeighbors.same.set(SOUTHEAST, positionToVoxels.get(workhorse) != null);
    workhorse.x--;
    theNeighbors.same.set(SOUTH, positionToVoxels.get(workhorse) != null);
    workhorse.x--;
    theNeighbors.same.set(SOUTHWEST, positionToVoxels.get(workhorse) != null);
    workhorse.y--;
    theNeighbors.same.set(WEST, positionToVoxels.get(workhorse) != null);
    workhorse.y--;
    theNeighbors.same.set(NORTHWEST, positionToVoxels.get(workhorse) != null);

    return theNeighbors;
  }

  @Override public int size() {
    return positionToVoxels.size();
  }

  @Override public String toString() {
    return name + "(size: " + size() + ")";
  }

  /** Returns {@code true} if a voxel is not surrounded, or if it has neighbors out of bounds. */
  private boolean notSurroundedInBounds(Bounds bounds, Voxel voxel) {
    AllNeighbors theNeighbors = neighbors(voxel);
    return !theNeighbors.isSurrounded() || !bounds.containsAll(voxel.position, theNeighbors);
  }

  private void exportNoHidden(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (Voxel voxel : positionToVoxels.values()) {
        if (bounds.contains(voxel.position)
            && notSurroundedInBounds(bounds, voxel)) {
          grid.put(voxel);
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
        grid.put(new Voxel(workhorse.asPosition(), voxel.value));
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
          grid.put(voxel);
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
        grid.put(new Voxel(workhorse.asPosition(), voxel.value));
      }
    }
  }
}
