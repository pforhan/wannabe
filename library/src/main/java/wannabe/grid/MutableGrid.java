package wannabe.grid;

import wannabe.Voxel;

// TODO this may not need to be an interface, it could just be part of SimpleGrid.
/** A grid that supports adding or removing voxels. */
public interface MutableGrid extends Grid {
  /** Adds a single {@link Voxel}. */
  void add(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  void remove(Voxel v);

  void clear();

  /**
   * Indicates to the grid that now is an appropriate time to perform any optimizations on
   * this grid, such as sort by painter's algorithm, etc.
   */
  void optimize();

}
