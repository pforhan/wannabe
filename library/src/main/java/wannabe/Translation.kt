package wannabe

/**
 * Effectively the same as a [Position] but mutable. Produces the same hashcode and equals
 * results as a Position for the same values of x, y, and z.
 */
data class Translation(
  var x: Int = 0,
  var y: Int = 0,
  var z: Int = 0
) : Pos {

  constructor(position: Translation) : this(
      x = position.x,
      y = position.y,
      z = position.z
  )

  constructor(position: Position) : this(
      x = position.x,
      y = position.y,
      z = position.z
  )

  fun add(offset: Translation): Translation {
    x += offset.x
    y += offset.y
    z += offset.z
    return this
  }

  fun asPosition(): Position {
    return Position(x, y, z)
  }

  fun set(position: Translation): Translation {
    x = position.x
    y = position.y
    z = position.z
    return this
  }

  fun set(position: Position): Translation {
    x = position.x
    y = position.y
    z = position.z
    return this
  }

  fun zero(): Translation {
    x = 0
    y = 0
    z = 0
    return this
  }

  override fun x(): Int {
    return x
  }

  override fun y(): Int {
    return y
  }

  override fun z(): Int {
    return z
  }

  override val isZero: Boolean
    get() = x == 0 && y == 0 && z == 0

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
    if (other is Translation) {
      return x == other.x && y == other.y && z == other.z
    } else if (other is Position) {
      return x == other.x && y == other.y && z == other.z
    }
    return false
  }
}
