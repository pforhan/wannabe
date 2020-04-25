package wannabe.grid

import wannabe.Voxel
import java.util.ArrayList

/** A Grid that implements animation by selecting one of its child grids at a time.  */
class FrameAnimatedGrid(
  private val name: String
) : Grid {
  private val frames: MutableList<Grid> = mutableListOf()
  private var current = 0
  private var dirty = false

  override fun iterator(): Iterator<Voxel> {
    dirty = false
    return frames[current].iterator()
  }

  override val isDirty: Boolean
    get() = dirty || frames[current].isDirty

  override val size: Int
    get() = frames[current].size

  fun nextFrame() {
    dirty = true
    current++
    if (current >= frames.size) {
      current = 0
    }
  }

  fun addFrame(frame: Grid) {
    require(frame !== this) { "Cannot have itself as a frame." }
    frames.add(frame)
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    return frames[current].neighbors(voxel)
  }

  override fun toString(): String = "$name; frame $current of ${frames.size}"
}
