package wannabe.grid.iterators

import wannabe.Translation
import wannabe.Voxel

class TranslatingIterator(
  private val realIterator: Iterator<Voxel>,
  private val translation: Translation
) : Iterator<Voxel> {

  /** Just a reused object to do math for us.  */
  private val workhorse = Translation(0, 0, 0)

  init {
    check(!translation.isZero) { "Don't use a translatingIterator unless you need to!" }
  }

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel {
    val (position, value) = realIterator.next()
    return Voxel(
        workhorse.set(position)
            .add(translation)
            .asPosition(), value
    )
  }
}
