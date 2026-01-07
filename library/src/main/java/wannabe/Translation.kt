package wannabe

/**
 * Effectively the same as a [Position] but mutable. Produces the same hashcode and equals
 * results as a Position for the same values of x, y, and z.
 */
data class Translation(
  override var x: Int = 0,
  override var y: Int = 0,
  override var z: Int = 0
) : Pos {

  constructor(pos: Pos) : this(
      x = pos.x,
      y = pos.y,
      z = pos.z
  )

  fun add(offset: Pos): Translation {
    x += offset.x
    y += offset.y
    z += offset.z
    return this
  }

  fun subtract(offset: Pos): Translation {
    x -= offset.x
    y -= offset.y
    z -= offset.z
    return this
  }

  fun asPosition(): Position {
    return Position(x, y, z)
  }

  fun set(pos: Pos): Translation {
    x = pos.x
    y = pos.y
    z = pos.z
    return this
  }
  
  fun set(
    newX: Int = 0,
    newY: Int = 0,
    newZ: Int = 0
  ): Translation {
    x = newX
    y = newY
    z = newZ
    return this
  }

  fun zero(): Translation {
    x = 0
    y = 0
    z = 0
    return this
  }

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
}
