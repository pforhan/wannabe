package wannabe.grid

import wannabe.Translation
import wannabe.Voxel
import wannabe.grid.iterators.TranslatingIterator

/** A Grid that applies a rotation function to a source grid and caches the results.  */
class TranslateGrid(
  private val name: String,
  private val source: Grid
) : Grid {
  private val translation = Translation(0, 0, 0)
  private var dirty = true

  override fun iterator(): Iterator<Voxel> {
    dirty = false
    val realIterator = source.iterator()
    return if (translation.isZero) realIterator else TranslatingIterator(realIterator, translation)
  }

  override val isDirty: Boolean
    get() = realIsDirty()

  override val size: Int
    get() = source.size

  /** Translates every [Voxel] in this grid by the specified offset. Additive.  */
  fun translate(offset: Translation) {
    if (offset.isZero) return
    dirty = true
    translation.add(offset)
  }

  /** Resets translation to zero.  */
  fun clearTranslation() {
    if (translation.isZero) return
    dirty = true
    translation.zero()
  }

  private fun realIsDirty(): Boolean {
    return dirty || source.isDirty
  }

  override fun neighbors(voxel: Voxel): AllNeighbors {
    // TODO this is wrong, isn't it?  I need to translate to source space, ie, subtract translation
    // TODO and what's more, a translated voxel won't even exist in the source grid...
    // TODO so what to do?  Could translate the position and make a new voxel (and add Voxel.equals)
    // TODO or could throw. Throwing would encourage lots of caching I presume?
    return source.neighbors(voxel)
  }

  override fun toString(): String = "$name; $translation (size: $size)"

}
