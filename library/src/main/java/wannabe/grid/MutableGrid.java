package wannabe.grid;

import wannabe.Voxel;

/** A grid that supports adding or removing voxels. TODO we should be able to drop this */
public interface MutableGrid extends Grid {
  /** Places a single {@link Voxel}. */
  void put(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  void remove(Voxel v);

  void clear();

  /**
   * Indicates to the grid that now is an appropriate time to perform any optimizations on
   * this grid, such as sort by painter's algorithm, etc.
   */
  void optimize();

}
