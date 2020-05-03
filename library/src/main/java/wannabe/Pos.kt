package wannabe

/**
 * Simple interface that allows easy comparison of [Position] and [Translation].
 */
interface Pos {
  val x: Int
  val y: Int
  val z: Int
  val isZero: Boolean
      get() = x == 0 && y == 0 && z == 0
}
