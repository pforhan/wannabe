// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import wannabe.Voxel;

/** A collection of {@link Voxel}s. */
public interface Grid extends Iterable<Voxel> {
  /** Implementations may reuse the same instance. */
  Grid subGrid(int x, int y, int width, int height);

  /** Returns {@code true} if the specified voxel was added. */
  boolean add(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  boolean remove(Voxel v);

  /** Number of Voxels this grid contains. */
  int size();

  /**
   * Returns an immutable version of this grid that is optimized for rendering since
   * it can assume it has no changing voxels.
   */
  Grid unmodifyableGrid();
}