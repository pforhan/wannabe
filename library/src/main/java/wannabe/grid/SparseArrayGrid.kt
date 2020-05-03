package wannabe.grid

import android.util.SparseArray
import wannabe.Bounds
import wannabe.Translation
import wannabe.Voxel
import wannabe.grid.AllNeighbors.RelativePosition
import java.util.Comparator
import java.util.TreeSet

/**
 * [Grid] implementation that has uses three SpareArrays for x, y, and z
 * to store [Voxel]s.
 */
class SparseArrayGrid(
  private val name: String,
    // TODO delete this flag, we just can run this as a map
  private val ignoreDuplicatePositions: Boolean
) : MutableGrid {
  private val zMap = Z()

  private val all: MutableSet<Voxel> = TreeSet(zxyIncreasing)
  private val theNeighbors = AllNeighbors()
  override var isDirty = false
    private set

  override fun iterator(): Iterator<Voxel> {
    isDirty = false
    return all.iterator()
  }

  override fun optimize() {
    // TODO maybe dirty=true?
//    Collections.sort(all, zIncreasing);
  }

  override fun put(v: Voxel) {
    isDirty = true
    var yMap = zMap[v.position.z]
    if (yMap == null) {
      yMap = Y()
      zMap.put(v.position.z, yMap)
    }
    var xMap = yMap[v.position.y]
    if (xMap == null) {
      xMap = X()
      yMap.put(v.position.y, xMap)
      // Don't have to check for a duplicate since we know there isn't one.
      xMap.put(v.position.x, v)
    } else {
      if (ignoreDuplicatePositions || xMap[v.position.x] == null) {
        xMap.put(v.position.x, v)
      } else {
        throw IllegalArgumentException("Duplicate voxel at " + v.position)
      }
    }
    all.add(v)
  }

  override fun remove(v: Voxel) {
    isDirty = true
    val yMap = zMap[v.position.z] ?: return
    val xMap = yMap[v.position.y]
    if (xMap == null) {
      return
    } else {
      xMap.remove(v.position.x)
    }
    all.remove(v)
  }

  override fun clear() {
    isDirty = true
    all.clear()
    val zSize = zMap.size()
    for (i in 0 until zSize) {
      val yMap = zMap.valueAt(i)
      val ySize = yMap!!.size()
      for (j in 0 until ySize) {
        yMap.valueAt(j)
            .clear()
      }
    }
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    populateNeighbors(voxel)
    return theNeighbors
  }

  override val size: Int
    get() = all.size

  override fun toString(): String {
    return "$name(size: $size)"
  }

  /** Returns `true` if a voxel is not surrounded, or if it has neighbors out of bounds.  */
  private fun notSurroundedInBounds(
    bounds: Bounds,
    voxel: Voxel
  ): Boolean {
    workhorse.set(voxel.position)
    populateNeighbors(voxel)
    return !theNeighbors.isSurrounded || !bounds.containsAll(voxel.position, theNeighbors)
  }

  val workhorse = Translation(0, 0, 0)

  fun populateNeighbors(voxel: Voxel) {
    // TODO convert to neighbor cache style used in simpleGrid, and populate more directions
    theNeighbors.clear()
    workhorse.set(voxel.position)
    // Above:
    workhorse.z++
    theNeighbors.above[RelativePosition.CENTER] = get(workhorse) != null

    // Below:
    workhorse.z -= 2
    theNeighbors.below[RelativePosition.CENTER] = get(workhorse) != null

    // North:
    workhorse.z++
    workhorse.y--
    theNeighbors.same[RelativePosition.NORTH] = get(workhorse) != null

    // South:
    workhorse.y += 2
    theNeighbors.same[RelativePosition.SOUTH] = get(workhorse) != null

    // East:
    workhorse.y--
    workhorse.x++
    theNeighbors.same[RelativePosition.EAST] = get(workhorse) != null

    // West:
    workhorse.x -= 2
    theNeighbors.same[RelativePosition.WEST] = get(workhorse) != null
  }

  private operator fun get(translation: Translation): Voxel? {
    val yMap = zMap[translation.z] ?: return null
    val xMap = yMap[translation.y]
    return xMap?.get(translation.x)
  }

  /** Contains all Voxels for a given Z and Y value.  */
  private class X : SparseArray<Voxel>()

  /** This structure maps a Y value to the X data structure.  */
  private class Y : SparseArray<X>()

  /** This structure maps a Z value to the Y data structure.  */
  private class Z : SparseArray<Y>()

  companion object {
    val zxyIncreasing =
      Comparator<Voxel> { o1, o2 ->
        val zCmp = o1.position.z - o2.position.z
        if (zCmp != 0) {
          return@Comparator zCmp
        }
        val xCmp = o1.position.x - o2.position.x
        if (xCmp != 0) {
          xCmp
        } else {
          o1.position.y - o2.position.y
        }
      }
  }
}
