// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import wannabe.Position;
import wannabe.Voxel;

// TODO make a Grid for a background or something, that's optimized for its voxels not changing.
//  Could have a really efficient subGrid implementation. How to organize that, though?
//  It'd have to be by x and y, then.
// The trick is that if there is a single Grid holding voxels, anything moving atop that will cause
//  changes.  My need to come up with a way of having a background grid and a foreground grid,
//  and making sure the stuff on foreground stays equal or higher z.
public class SimpleGrid implements Grid {
  private static final Comparator<Voxel> zIncreasing = new Comparator<Voxel>() {
    @Override public int compare(Voxel o1, Voxel o2) {
      return o1.position.z - o2.position.z;
    }
  };

  private final List<Voxel> voxels = new ArrayList<Voxel>();
  private final SimpleGrid subGrid = new SimpleGrid();

  @Override public Iterator<Voxel> iterator() {
    return voxels.iterator();
  }

  @Override public Grid subGrid(int x, int y, int width, int height) {
    subGrid.voxels.clear();
    for (Voxel vox : voxels) {
      Position pos = vox.position;
      if (pos.x >= x && pos.x < x + width //
          && pos.y >= y && pos.y < y + height) {
        subGrid.voxels.add(vox);
      }
    }
//    System.out.println(String.format("Subgrid %s, %s, %s, %s has size %s", x, y, width, height, subGrid.voxels.size()));
    return subGrid;
  }

  /** Sorts the voxels by z order, from lowest to highest. */
  public void sortByPainters() {
    Collections.sort(voxels, zIncreasing);
  }

  @Override public boolean add(Voxel v) {
    return voxels.add(v);
  }

  @Override public boolean remove(Voxel v) {
    return voxels.remove(v);
  }

  @Override public int size() {
    return voxels.size();
  }
}
