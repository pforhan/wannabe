package wannabe.grid

import wannabe.Bounds
import wannabe.Pos
import wannabe.Position
import wannabe.Translation
import wannabe.Voxel
import wannabe.grid.AllNeighbors.RelativePosition.Companion.CENTER
import wannabe.grid.AllNeighbors.RelativePosition.Companion.EAST
import wannabe.grid.AllNeighbors.RelativePosition.Companion.NORTH
import wannabe.grid.AllNeighbors.RelativePosition.Companion.NORTHEAST
import wannabe.grid.AllNeighbors.RelativePosition.Companion.NORTHWEST
import wannabe.grid.AllNeighbors.RelativePosition.Companion.SOUTH
import wannabe.grid.AllNeighbors.RelativePosition.Companion.SOUTHEAST
import wannabe.grid.AllNeighbors.RelativePosition.Companion.SOUTHWEST
import wannabe.grid.AllNeighbors.RelativePosition.Companion.WEST
import java.util.Comparator
import java.util.HashMap
import java.util.TreeMap

/** [Grid] implementation that has a simple map of [Voxel]s and a means to sort.  */
class SimpleGrid(
  private val name: String
) : MutableGrid {

  private val positionToVoxels: MutableMap<Pos, Voxel> = TreeMap(zxyIncreasing)
  private val neighborCache: MutableMap<Voxel, AllNeighbors?> = mutableMapOf()
  private val translation = Translation(0, 0, 0)

  override var isDirty = false
    private set

  override fun iterator(): Iterator<Voxel> {
    isDirty = false
    return positionToVoxels.values.iterator()
  }

  /** Sorts the voxels by z order, from lowest to highest.  */
  override fun optimize() = Unit

  override fun put(v: Voxel) {
    requireNotNull(v) { "Voxel may not be null." }
    markDirty()
    positionToVoxels[v.position] = v
  }

  override fun remove(v: Voxel) {
    markDirty()
    positionToVoxels.remove(v.position)
  }

  override fun clear() {
    markDirty()
    positionToVoxels.clear()
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    var theNeighbors = neighborCache[voxel]
    if (theNeighbors == null) {
      theNeighbors = createAndPopulateNeighbors(voxel)
      neighborCache[voxel] = theNeighbors
    }
    return theNeighbors
  }

  private fun markDirty() {
    isDirty = true
    neighborCache.clear()
  }

  private val workhorse = Translation(0, 0, 0)

  private fun createAndPopulateNeighbors(voxel: Voxel): AllNeighbors {
    val theNeighbors = AllNeighbors()
    workhorse.set(voxel.position)
    // Above:
    workhorse.z++
    theNeighbors.above.set(CENTER, workhorse in positionToVoxels)

    // Below:
    workhorse.z -= 2
    theNeighbors.below.set(CENTER, workhorse in positionToVoxels)
    workhorse.z++
    // Now scan 8 surrounding positions on the same plane, starting at north, going clockwise.
    workhorse.y--
    theNeighbors.same.set(NORTH, workhorse in positionToVoxels)
    workhorse.x++
    theNeighbors.same.set(NORTHEAST, workhorse in positionToVoxels)
    workhorse.y++
    theNeighbors.same.set(EAST, workhorse in positionToVoxels)
    workhorse.y++
    theNeighbors.same.set(SOUTHEAST, workhorse in positionToVoxels)
    workhorse.x--
    theNeighbors.same.set(SOUTH, workhorse in positionToVoxels)
    workhorse.x--
    theNeighbors.same.set(SOUTHWEST, workhorse in positionToVoxels)
    workhorse.y--
    theNeighbors.same.set(WEST, workhorse in positionToVoxels)
    workhorse.y--
    theNeighbors.same.set(NORTHWEST, workhorse in positionToVoxels)
    return theNeighbors
  }

  override val size: Int
    get() = positionToVoxels.size

  override fun toString() = "$name(size: $size)"

  /** Returns `true` if a voxel is not surrounded, or if it has neighbors out of bounds.  */
  private fun notSurroundedInBounds(
    bounds: Bounds,
    voxel: Voxel
  ): Boolean {
    val theNeighbors = neighbors(voxel)
    return !theNeighbors.isSurrounded || !bounds.containsAll(voxel.position, theNeighbors)
  }

  private fun exportNoHidden(
    grid: MutableGrid,
    bounds: Bounds
  ) {
    if (translation.isZero) {
      // We can skip cloning and translation.
      for (voxel in positionToVoxels.values) {
        if (bounds.contains(voxel.position)
            && notSurroundedInBounds(bounds, voxel)
        ) {
          grid.put(voxel)
        }
      }
      return
    }

    // Otherwise, we have to translate all the things.
    val workhorse = Translation(0, 0, 0)
    for (voxel in positionToVoxels.values) {
      workhorse.set(voxel.position)
          .add(translation)
      if (bounds.contains(workhorse)
          && notSurroundedInBounds(bounds, voxel)
      ) {
        // TODO double check if we need to handle translation with bounds
        grid.put(Voxel(workhorse.asPosition(), voxel.value))
      }
    }
  }

  // TODO this is a bit of an ugly near-duplication, but seems good for clarity and perf.
  // TODO consider if this should just be the api instead of the bool param.
  private fun exportWithHidden(
    grid: MutableGrid,
    bounds: Bounds
  ) {
    if (translation.isZero) {
      // We can skip cloning and translation.
      for (voxel in positionToVoxels.values) {
        if (bounds.contains(voxel.position)) {
          grid.put(voxel)
        }
      }
      return
    }

    // Otherwise, we have to translate all the things.
    val workhorse = Translation(0, 0, 0)
    for ((position, value) in positionToVoxels.values) {
      workhorse.set(position)
          .add(translation)
      if (bounds.contains(workhorse)) {
        // TODO double check if we need to handle translation with bounds
        grid.put(Voxel(workhorse.asPosition(), value))
      }
    }
  }

  companion object {
    /** Sorts by Z increasing then X increasing then Y increasing.  */
    private val zxyIncreasing = Comparator { o1: Pos, o2: Pos ->
        val zCmp = o1.z() - o2.z()
        if (zCmp != 0) {
          return@Comparator zCmp
        }
        val xCmp = o1.x() - o2.x()
        if (xCmp != 0) {
          return@Comparator xCmp
        }
        o1.y() - o2.y()
      }
  }
}
