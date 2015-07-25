// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import wannabe.Position;
import wannabe.Voxel;

/** {@link Grid} implementation that has a simple list of {@link Voxel}s and a means to sort. */
public class SimpleGrid implements Grid {
  private static final Comparator<Voxel> zIncreasing = new Comparator<Voxel>() {
    @Override public int compare(Voxel o1, Voxel o2) {
      return o1.position.z - o2.position.z;
    }
  };

  private final List<Voxel> voxels = new ArrayList<Voxel>();
  private final String name;
  private SimpleGrid subGrid;
  private final Position translation = new Position(0, 0, 0);

  public SimpleGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    final Iterator<Voxel> realIterator = voxels.iterator();
    final Voxel workhorse = new Voxel(0, 0, 0, 0);
    return new Iterator<Voxel>() {
      @Override public boolean hasNext() {
        return realIterator.hasNext();
      }

      @Override public Voxel next() {
        Voxel real = realIterator.next();
        workhorse.color = real.color;
        workhorse.position.set(real.position);
        workhorse.position.add(translation);
        return workhorse;
      }

      @Override public void remove() {
        // TODO should I support remove?
        realIterator.remove();
      }
    };
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

  @Override public void exportTo(Grid grid, int x, int y, int width, int height) {
    // TODO consider: how does a translation affect the above coordinates?
    addTranslated(grid, x, y, width, height, true);
  }

  private void addTranslated(Grid grid, int x, int y, int width, int height, boolean cloneVoxels) {
    Position workhorse = new Position(0, 0, 0);
    for (Voxel vox : voxels) {
      Position pos = vox.position;
      workhorse.set(pos);
      workhorse.add(translation);
      if (workhorse.x >= x && workhorse.x < x + width //
          && workhorse.y >= y && workhorse.y < y + height) {
        if (cloneVoxels) {
          grid.add(new Voxel(workhorse.clone(), vox.color));
        } else {
          grid.add(vox);
        }
      }
    }
  }


  @Override public void translate(Position offset) {
    // TODO should I translate from where I am, or just reset each time?
    translation.add(offset);
    if (subGrid != null) subGrid.translate(offset);
  }

  @Override public int size() {
    return voxels.size();
  }

  @Override public String toString() {
    return name + "(size: " + size() + ")";
  }
}
