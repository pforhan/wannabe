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

  private final Map<Position, Voxel> positionToVoxel = new HashMap<>();
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
    if (positionToVoxel.containsKey(v.position)) {
      if (ignoreDuplicatePositions) return;
      throw new IllegalArgumentException("Duplicate voxel at " + v.position);
    }
    positionToVoxel.put(v.position, v);
    voxels.add(v);
    populateNeighbors(v);
  }

  @Override public void remove(Voxel v) {
    positionToVoxel.remove(v.position);
    // TODO need to unset all neighbors both on v and any neighbors
    voxels.remove(v);
  }

  @Override public void clear() {
    // TODO need to unset all neighbors
    positionToVoxel.clear();
    voxels.clear();
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation. // TODO probably not any more with neighbors, sigh
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
      if (bounds.contains(workhorse)) {
        // TODO should check surrounded too
        grid.add(new Voxel(workhorse.asPosition(), voxel.color));
      }
    }
  }

  /** Returns {@code true} if a voxel is not surrounded, or if it has neighbors out of bounds. */
  private boolean notSurroundedInBounds(Bounds bounds, Voxel voxel) {
    Neighbors neighbors = positionToVoxel.get(voxel.position).neighbors;
    return neighbors.isNotSurrounded() || !bounds.containsAll(neighbors);
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

  private void populateNeighbors(Voxel v) {
    Translation workhorse = new Translation(v.position);
    // Above:
    workhorse.z++;
    Voxel neighbor = positionToVoxel.get(workhorse);
    v.neighborAbove(neighbor);

    // Below:
    workhorse.z -= 2;
    neighbor = positionToVoxel.get(workhorse);
    v.neighborBelow(neighbor);

    // North:
    workhorse.z++;
    workhorse.y--;
    neighbor = positionToVoxel.get(workhorse);
    v.neighborNorth(neighbor);

    // South:
    workhorse.y += 2;
    neighbor = positionToVoxel.get(workhorse);
    v.neighborSouth(neighbor);

    // East:
    workhorse.y--;
    workhorse.x++;
    neighbor = positionToVoxel.get(workhorse);
    v.neighborEast(neighbor);

    // West:
    workhorse.x -= 2;
    neighbor = positionToVoxel.get(workhorse);
    v.neighborWest(neighbor);
  }
}
