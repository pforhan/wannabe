package wannabe.grid

import wannabe.Voxel

/** A Grid that caches the output of the source Grid and delegates calls to the cache.  */
class CachingGrid(
  private val name: String,
  private val source: Grid
) : Grid {
  private val dest = SimpleGrid("cache")

  override fun iterator(): Iterator<Voxel> {
    maybeClearAndFillDest()
    return dest.iterator()
  }

  override val isDirty: Boolean
    get() {
      // Capture dirty status now, since if it is dirty we'll clean it by grabbing from source again.
      // TODO doesn't this mean a second call won't be right?  but maybe that's okay?
      val wasDirty = source.isDirty
      maybeClearAndFillDest()
      return wasDirty
    }

  override val size: Int
    get() {
      maybeClearAndFillDest()
      return dest.size
    }

  private fun maybeClearAndFillDest() {
    if (source.isDirty) {
      dest.clear()
      for (voxel in source) {
        dest.put(voxel)
      }
    }
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    maybeClearAndFillDest()
    return dest.neighbors(voxel)
  }

  override fun toString(): String = "$name cache (size: $size)"

}
