package wannabe.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import wannabe.Bounds;
import wannabe.Position;
import wannabe.Translation;
import wannabe.Voxel;

/** {@link Grid} implementation that has a simple list of {@link Voxel}s and a means to sort. */
public class SimpleGrid implements MutableGrid {

  private static final Comparator<Voxel> zIncreasing = new Comparator<Voxel>() {
    @Override public int compare(Voxel o1, Voxel o2) {
      return o1.position.z - o2.position.z;
    }
  };

  private final Map<Position, Neighbors> positionToNeighbors = new HashMap<>();
  // TODO can I drop the list and just use the map? Could use a TreeMap with the same comparator
  private final List<Voxel> voxels = new ArrayList<>();
  private final Translation translation = new Translation(0, 0, 0);
  private final String name;
  private final boolean ignoreDuplicatePositions;

  public SimpleGrid(String name) {
    this(name, false);
  }

  public SimpleGrid(String name, boolean ignoreDuplicatePositions) {
    this.name = name;
    this.ignoreDuplicatePositions = ignoreDuplicatePositions;
  }

  @Override public Iterator<Voxel> iterator() {
    Iterator<Voxel> realIterator = voxels.iterator();
    return translation.isZero() ? realIterator : new TranslatingIterator(realIterator, translation);
  }

  /** Sorts the voxels by z order, from lowest to highest. */
  @Override public void optimize() {
    // TODO should I also order by x and y?
    Collections.sort(voxels, zIncreasing);
  }

  @Override public void add(Voxel v) {
    if (positionToNeighbors.containsKey(v.position)) {
      if (ignoreDuplicatePositions) return;
      throw new IllegalArgumentException("Duplicate voxel at " + v.position);
    }
    Neighbors neighbors = new Neighbors(v);
    positionToNeighbors.put(v.position, neighbors);
    voxels.add(v);
    populateNeighbors(neighbors);
  }

  @Override public void remove(Voxel v) {
    positionToNeighbors.remove(v.position);
    // TODO need to unset all neighbors both on v and any neighbors
    voxels.remove(v);
  }

  @Override public void clear() {
    // TODO need to unset all neighbors?
    positionToNeighbors.clear();
    voxels.clear();
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds, boolean includeHidden) {
    if (includeHidden) {
      exportWithHidden(grid, bounds);
      return;
    }

    exportNoHidden(grid, bounds);
  }

  @Override public Neighbors neighbors(Voxel voxel) {
    return positionToNeighbors.get(voxel.position);
  }

  @Override public void translate(Translation offset) {
    translation.add(offset);
  }

  @Override public void clearTranslation() {
    translation.zero();
  }

  @Override public int size() {
    return voxels.size();
  }

  @Override public String toString() {
    return name + "(size: " + size() + ")";
  }

  /** Returns {@code true} if a voxel is not surrounded, or if it has neighbors out of bounds. */
  private boolean notSurroundedInBounds(Bounds bounds, Voxel voxel) {
    Neighbors neighbors = positionToNeighbors.get(voxel.position);
    return neighbors.isNotSurrounded() || !bounds.containsAll(neighbors);
  }

  private void exportNoHidden(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (Voxel voxel : voxels) {
        if (bounds.contains(voxel.position)
            && notSurroundedInBounds(bounds, voxel)) {
          grid.add(voxel);
        }
      }
      return;
    }

    // Otherwise, we have to translate all the things.
    Translation workhorse = new Translation(0, 0, 0);
    for (Voxel voxel : voxels) {
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
      for (Voxel voxel : voxels) {
        if (bounds.contains(voxel.position)) {
          grid.add(voxel);
        }
      }
      return;
    }

    // Otherwise, we have to translate all the things.
    Translation workhorse = new Translation(0, 0, 0);
    for (Voxel voxel : voxels) {
      workhorse.set(voxel.position).add(translation);
      if (bounds.contains(workhorse)) {
        // TODO double check if we need to handle translation with bounds
        grid.add(new Voxel(workhorse.asPosition(), voxel.color));
      }
    }
  }


  private void populateNeighbors(Neighbors neighbors) {
    Translation workhorse = new Translation(neighbors.voxel.position);
    // Above:
    workhorse.z++;
    Neighbors otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborAbove(otherNeighbors);

    // Below:
    workhorse.z -= 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborBelow(otherNeighbors);

    // North:
    workhorse.z++;
    workhorse.y--;
    otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborNorth(otherNeighbors);

    // South:
    workhorse.y += 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborSouth(otherNeighbors);

    // East:
    workhorse.y--;
    workhorse.x++;
    otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborEast(otherNeighbors);

    // West:
    workhorse.x -= 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    neighbors.neighborWest(otherNeighbors);
  }
}
