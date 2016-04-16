// Copyright 2013 Patrick Forhan.
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
  private final Position translation = new Position(0, 0, 0);
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
    voxels.add(v);
    populateNeighbors(v);
  }

  @Override public boolean remove(Voxel v) {
    positionToNeighbors.remove(v.position);
    return voxels.remove(v);
  }

  @Override public void clear() {
    positionToNeighbors.clear();
    voxels.clear();
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (int i = 0; i < voxels.size(); i++) {
        Voxel voxel = voxels.get(i);
        if (bounds.contains(voxel.position)
            && notSurroundedInBounds(voxel)) {
          grid.add(voxel);
        }
      }
      return;
    }

    // Otherwise, we have to translate all the things.
    Position workhorse = new Position(0, 0, 0);
    for (int i = 0; i < voxels.size(); i++) {
      Voxel vox = voxels.get(i);
      Position pos = vox.position;
      workhorse.set(pos);
      workhorse.add(translation);
      if (bounds.contains(workhorse)) {
        grid.add(new Voxel(workhorse.clone(), vox.color));
      }
    }
  }

  private boolean notSurroundedInBounds(Voxel voxel) {
    return positionToNeighbors.get(voxel.position).isNotSurrounded();
  }

  @Override public void translate(Position offset) {
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
    Neighbors myNeighbors = new Neighbors(v);
    positionToNeighbors.put(v.position, myNeighbors);

    Position workhorse = v.position.clone();
    // Above:
    workhorse.z++;
    Neighbors otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.above = otherNeighbors.voxel;
      otherNeighbors.below = v;
    }

    // Below:
    workhorse.z -= 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.below = otherNeighbors.voxel;
      otherNeighbors.above = v;
    }

    // North:
    workhorse.z++;
    workhorse.y--;
    otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.south = otherNeighbors.voxel;
      otherNeighbors.north = v;
    }

    // South:
    workhorse.y += 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.north = otherNeighbors.voxel;
      otherNeighbors.south = v;
    }

    // East:
    workhorse.y--;
    workhorse.x++;
    otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.east = otherNeighbors.voxel;
      otherNeighbors.west = v;
    }

    // West:
    workhorse.x -= 2;
    otherNeighbors = positionToNeighbors.get(workhorse);
    if (otherNeighbors != null) {
      myNeighbors.west = otherNeighbors.voxel;
      otherNeighbors.east = v;
    }
  }
}
