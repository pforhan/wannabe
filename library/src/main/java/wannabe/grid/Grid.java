// Copyright 2013 Patrick Forhan.
package wannabe.grid;

import java.util.Iterator;
import wannabe.Position;
import wannabe.Voxel;

/** A collection of {@link Voxel}s. */
public interface Grid extends Iterable<Voxel> {
  /**
   * Returns an iterator over all {@link Voxel}s in this grid. Note that optimized implementations
   * may return the same {@link Voxel} reference with every call to {@link Iterator#next()} so it is
   * not recommended that references are kept after use.
   */
  @Override Iterator<Voxel> iterator();

  /** Adds a single {@link Voxel}. */
  void add(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  boolean remove(Voxel v);

  void clear();

  /**
   * Copies all {@link Voxel}s from this grid to the specified grid.  Applies any translation to
   * each copied {@link Voxel}.  Voxels filtered based on supplied parameters.
   */
  void exportTo(Grid grid, int x, int y, int width, int height);

  /** Number of Voxels this grid contains. */
  int size();

  /** Translates every {@link Voxel} in this grid by the specified offset. */
  void translate(Position offset);

  /** Perform some operation (such as sort by painter's algorithm, etc) on the grid. */
  void optimize();
}