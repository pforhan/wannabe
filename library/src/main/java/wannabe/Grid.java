// Copyright 2013 Square, Inc.
package wannabe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Grid implements Iterable<Voxel> {
  private static final Comparator<Voxel> zIncreasing = new Comparator<Voxel>() {
    @Override public int compare(Voxel o1, Voxel o2) {
      return o1.position.z - o2.position.z;
    }
  };

  private List<Voxel> voxels = new ArrayList<Voxel>();
  private Grid subGrid;

  @Override public Iterator<Voxel> iterator() {
    return voxels.iterator();
  }

  /** May reuse the same instance. */
  public Grid subGrid(int x, int y, int width, int height) {
    if (subGrid == null) {
      subGrid = new Grid();
    }

    subGrid.voxels.clear();
    for (Voxel vox : voxels) {
      Position pos = vox.position;
      if (pos.x >= x && pos.x < x + width
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

  public boolean add(Voxel e) {
    return voxels.add(e);
  }

  public boolean remove(Object o) {
    return voxels.remove(o);
  }
}
