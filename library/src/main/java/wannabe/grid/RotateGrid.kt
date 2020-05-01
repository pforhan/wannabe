package wannabe.grid

import wannabe.Voxel
import wannabe.grid.iterators.RotatingIterator
import wannabe.grid.iterators.RotationDegrees

/** A Grid that applies a rotation function to a source grid and caches the results.  */
class RotateGrid(
  private val name: String,
  private val source: Grid
) : Grid {
  private var dirty = true
  private val rotation = RotationDegrees()

  override fun iterator(): Iterator<Voxel> {
    dirty = false
    return RotatingIterator(source.iterator(), rotation)
  }

  override val isDirty: Boolean
    get() = dirty || source.isDirty

  override val size: Int
    get() = source.size

  fun setRotate(
    xDegrees: Int,
    yDegrees: Int,
    zDegrees: Int
  ) {
    rotation.x = xDegrees
    rotation.y = yDegrees
    rotation.z = zDegrees
    dirty = true
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    throw IllegalStateException("RotateGrid $name cannot provide neighbors; "
            + "Did you forget to add a caching layer?"
    )
  }

  override fun toString(): String {
    return "$name; $rotation(size: $size)"
  }
}