package wannabe.grid;

import java.util.Iterator;
import wannabe.Bounds;
import wannabe.Translation;
import wannabe.Voxel;

/**
 * A collection of {@link Voxel}s. Voxels do not move within to the grid, although the grid's
 * set of voxels may change (see {@link MutableGrid}) or the translation of the grid itself
 * may change.
 */
public interface Grid extends Iterable<Voxel> {
  // TODO come up with a way to merge exportTo / iterator
  /**
   * Returns an iterator over all {@link Voxel}s in this grid. Note that optimized implementations
   * may return the same {@link Voxel} reference with every call to {@link Iterator#next()} so it is
   * not recommended that references are kept after use.
   */
  @Override Iterator<Voxel> iterator();

  /**
   * Copies all {@link Voxel}s from this grid to the specified grid.  Applies any translation to
   * each copied {@link Voxel}.  Voxels filtered based on supplied parameters.  If includeHidden
   * is true, then even surrounded voxels will be included.
   */
  void exportTo(MutableGrid grid, Bounds bounds, boolean includeHidden);

  /** Number of Voxels this grid contains. */
  int size();

  /**
   * Returns {@code true} if anything about this grid has changed (such as voxels or translation)
   * since the last time {@link #iterator()} or {@link #exportTo(MutableGrid, Bounds, boolean)}
   * was called.
   */
  boolean isDirty();

  /** Translates every {@link Voxel} in this grid by the specified offset. Additive. */
  void translate(Translation offset);

  /** Resets translation to zero. */
  void clearTranslation();

  AllNeighbors neighbors(Voxel voxel);

//  don't remember what I was doing here:
//  // Painting methods.
//  Voxel at(Translation translation);
}
