// Copyright 2015 Patrick Forhan
package wannabe.grid;

import wannabe.Voxel;

/** A grid that supports changes to its voxels. */
public interface MutableGrid extends Grid {
  /** Adds a single {@link Voxel}. */
  void add(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  boolean remove(Voxel v);

  void clear();

  /** Perform some operation (such as sort by painter's algorithm, etc) on the grid. */
  void optimize();

}
