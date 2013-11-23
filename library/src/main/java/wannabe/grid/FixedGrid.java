// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wannabe.Position;
import wannabe.Voxel;

/** A grid optimized for subGrid usage.  It assumes that none of its voxels ever change. */
public class FixedGrid implements Grid {
  private final List<Voxel> voxels = new ArrayList<Voxel>();
  private final FixedGrid subGrid = new FixedGrid();

  public FixedGrid(Grid baseGrid) {
    // TODO try to finish this... optimizing for x and y is at odds with z-sorting
    // Ideally it ends up something like String.subString and can operate on the same base data structure
  }


  @Override public Iterator<Voxel> iterator() {
    return voxels.iterator();
  }

  @Override public Grid subGrid(int x, int y, int width, int height) {
    // TODO implement me!
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

  @Override public boolean add(Voxel v) {
    return false;
  }

  @Override public boolean remove(Voxel v) {
    return false;
  }

  @Override public int size() {
    return voxels.size();
  }

  @Override public Grid unmodifyableGrid() {
    return this;
  }

  private static class Limits {
    int min, max;
  }

}
