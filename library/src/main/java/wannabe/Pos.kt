package wannabe

/**
 * Parent interface that allows easy comparison of [Position] and [Translation].
 * TODO delete this class -- consider a special class that somehow hides mutability
 */
interface Pos {
  fun x(): Int
  fun y(): Int
  fun z(): Int
  val isZero: Boolean
}
