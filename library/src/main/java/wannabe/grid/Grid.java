package wannabe.grid;

import java.util.Iterator;

import wannabe.Voxel;

/**
 * A collection of {@link Voxel}s. Voxels do not move within to the grid, although the grid's
 * set of voxels may change (see {@link MutableGrid}) or the translation of the grid itself
 * may change.
 */
public interface Grid extends Iterable<Voxel> {
  /**
   * Returns an iterator over all {@link Voxel}s in this grid. Note that optimized implementations
   * may return the same {@link Voxel} reference with every call to {@link Iterator#next()} so it is
   * not recommended that references are kept after use.
   */
  @Override Iterator<Voxel> iterator();

  /** Number of Voxels this grid contains. */
  int size();

  /**
   * Returns {@code true} if anything about this grid has changed (such as voxels or translation)
   * since the last time {@link #iterator()} was called.
   */
  boolean isDirty();

  AllNeighbors neighbors(Voxel voxel);

//  don't remember what I was doing here:
//  // Painting methods.
//  Voxel at(Translation translation);
}
