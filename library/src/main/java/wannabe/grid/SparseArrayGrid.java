package wannabe.grid;

import android.util.SparseArray;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import wannabe.Bounds;
import wannabe.Translation;
import wannabe.Voxel;

import static wannabe.grid.AllNeighbors.RelativePosition.CENTER;
import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

/**
 * {@link Grid} implementation that has uses three SpareArrays for x, y, and z
 * to store {@link Voxel}s.
 */
public class SparseArrayGrid implements MutableGrid {

  static final Comparator<Voxel> zxyIncreasing = (o1, o2) -> {
    int zCmp = o1.position.z - o2.position.z;
    if (zCmp != 0) {
      return zCmp;
    }

    int xCmp = o1.position.x() - o2.position.x();
    if (xCmp != 0) {
      return xCmp;
    }

    return o1.position.y() - o2.position.y();
  };

  private final Z zMap = new Z();
  private final String name;
  // TODO delete this flag, we just can run this as a map
  private final boolean ignoreDuplicatePositions;
  private final Set<Voxel> all = new TreeSet<>(zxyIncreasing); // TODO maybe zxyIncreasing?
  private final AllNeighbors theNeighbors = new AllNeighbors();
  private boolean dirty;

  public SparseArrayGrid(String name, boolean ignoreDuplicatePositions) {
    this.name = name;
    this.ignoreDuplicatePositions = ignoreDuplicatePositions;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;
    return all.iterator();
  }

  @Override public boolean isDirty() {
    return dirty;
  }

  @Override public void optimize() {
    // TODO maybe dirty=true?
//    Collections.sort(all, zIncreasing);
  }

  @Override public void put(Voxel v) {
    dirty = true;
    Y yMap = zMap.get(v.position.z);
    if (yMap == null) {
      yMap = new Y();
      zMap.put(v.position.z, yMap);
    }

    X xMap = yMap.get(v.position.y);
    if (xMap == null) {
      xMap = new X();
      yMap.put(v.position.y, xMap);
      // Don't have to check for a duplicate since we know there isn't one.
      xMap.put(v.position.x, v);

    } else {
      if (ignoreDuplicatePositions || xMap.get(v.position.x) == null) {
        xMap.put(v.position.x, v);
      } else {
        throw new IllegalArgumentException("Duplicate voxel at " + v.position);
      }
    }

    all.add(v);
  }

  @Override public void remove(Voxel v) {
    dirty = true;
    Y yMap = zMap.get(v.position.z);
    if (yMap == null) {
      return;
    }

    X xMap = yMap.get(v.position.y);
    if (xMap == null) {
      return;
    } else {
      xMap.remove(v.position.x);
    }

    all.remove(v);
  }

  @Override public void clear() {
    dirty = true;
    all.clear();
    int zSize = zMap.size();
    for (int i = 0; i < zSize; i++) {
      Y yMap = zMap.valueAt(i);
      int ySize = yMap.size();
      for (int j = 0; j < ySize; j++) {
        yMap.valueAt(j).clear();
      }
    }
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    populateNeighbors(voxel);
    return theNeighbors;
  }

  @Override public int size() {
    return all.size();
  }

  @Override public String toString() {
    return name + "(size: " + size() + ")";
  }

  /** Returns {@code true} if a voxel is not surrounded, or if it has neighbors out of bounds. */
  private boolean notSurroundedInBounds(Bounds bounds, Voxel voxel) {
    workhorse.set(voxel.position);
    populateNeighbors(voxel);
    return !theNeighbors.isSurrounded() || !bounds.containsAll(voxel.position, theNeighbors);
  }

  final Translation workhorse = new Translation(0, 0, 0);

  void populateNeighbors(Voxel voxel) {
    // TODO convert to neighbor cache style used in simpleGrid, and populate more directions
    theNeighbors.clear();
    workhorse.set(voxel.position);
    // Above:
    workhorse.z++;
    theNeighbors.above.set(CENTER, get(workhorse) != null);

    // Below:
    workhorse.z -= 2;
    theNeighbors.below.set(CENTER, get(workhorse) != null);

    // North:
    workhorse.z++;
    workhorse.y--;
    theNeighbors.same.set(NORTH, get(workhorse) != null);

    // South:
    workhorse.y += 2;
    theNeighbors.same.set(SOUTH, get(workhorse) != null);

    // East:
    workhorse.y--;
    workhorse.x++;
    theNeighbors.same.set(EAST, get(workhorse) != null);

    // West:
    workhorse.x -= 2;
    theNeighbors.same.set(WEST, get(workhorse) != null);
  }

  private Voxel get(Translation translation) {
    Y yMap = zMap.get(translation.z);
    if (yMap == null) {
      return null;
    }

    X xMap = yMap.get(translation.y);
    if (xMap == null) {
      return null;
    } else {
      return xMap.get(translation.x);
    }
  }

  /** Contains all Voxels for a given Z and Y value. */
  private static class X extends SparseArray<Voxel> {
  }

  /** This structure maps a Y value to the X data structure. */
  private static class Y extends SparseArray<X> {
  }

  /** This structure maps a Z value to the Y data structure. */
  private static class Z extends SparseArray<Y> {
  }
}
