package wannabe

/**
 * Non-mutable position in Cartesian space. For a mutable version, see [Translation].
 * Produces the same hashcode and equals results as a Translation for the same values of x, y, and z.
 */
data class Position(
  override val x: Int = 0,
  override val y: Int = 0,
  override val z: Int = 0
) : Pos {

  override fun hashCode(): Int {
    val prime = 31
    var result = 1
    result = prime * result + x
    result = prime * result + y
    result = prime * result + z
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other is Pos) {
      return x == other.x && y == other.y && z == other.z
    }
    return false
  }

  companion object {
    val ZERO = Position()
  }
}
