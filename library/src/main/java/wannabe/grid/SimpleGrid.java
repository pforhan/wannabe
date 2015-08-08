// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

  private final List<Voxel> voxels = new ArrayList<>();
  private final String name;
  private final Position translation = new Position(0, 0, 0);

  public SimpleGrid(String name) {
    this.name = name;
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
    voxels.add(v);
  }

  @Override public boolean remove(Voxel v) {
    return voxels.remove(v);
  }

  @Override public void clear() {
    voxels.clear();
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds) {
    if (translation.isZero()) {
      // We can skip cloning and translation.
      for (int i = 0; i < voxels.size(); i++) {
        Voxel voxel = voxels.get(i);
        if (bounds.contains(voxel.position)) {
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
}
