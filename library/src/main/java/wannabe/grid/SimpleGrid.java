// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import wannabe.Position;
import wannabe.Voxel;

public class SimpleGrid implements Grid {
  private static final Comparator<Voxel> zIncreasing = new Comparator<Voxel>() {
    @Override public int compare(Voxel o1, Voxel o2) {
      return o1.position.z - o2.position.z;
    }
  };

  private final List<Voxel> voxels = new ArrayList<Voxel>();
  private final String name;
  private SimpleGrid subGrid;


  public SimpleGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    return voxels.iterator();
  }

  @Override public Grid subGrid(int x, int y, int width, int height) {
    if (subGrid == null) subGrid = new SimpleGrid("sub of " + name);
    subGrid.voxels.clear();

    for (Voxel vox : voxels) {
      Position pos = vox.position;
      if (pos.x >= x && pos.x < x + width //
          && pos.y >= y && pos.y < y + height) {
        subGrid.voxels.add(vox);
      }
    }
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

  @Override public Grid unmodifyableGrid() {
    // TODO implement me!
    return null;
  }

  @Override public String toString() {
    return name;
  }
}
