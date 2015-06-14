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

  /** Implementations may reuse the same instance. */
  Grid subGrid(int x, int y, int width, int height);

  /** Returns {@code true} if the specified voxel was added. */
  boolean add(Voxel v);

  /** Returns {@code true} if the specified voxel was removed. */
  boolean remove(Voxel v);

  void clear();

  /** Copies all {@link Voxel}s from the specified grid and places them in this one. */
  void importGrid(Grid grid);

  /** Number of Voxels this grid contains. */
  int size();

  /** Translates every {@link Voxel} in this grid by the specified offset. */
  void translate(Position offset);

  /**
   * Returns an immutable version of this grid that is optimized for rendering since
   * it can assume it has no changing voxels.
   */
  Grid unmodifyableGrid();
}