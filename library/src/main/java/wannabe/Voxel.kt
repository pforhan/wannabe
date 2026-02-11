package wannabe

/** A value with a 3d position.  */
data class Voxel(
  val position: Position,
  /** Value of this voxel, could represent a texture or a color.  */
  val value: Int
) {

  constructor(
    x: Int,
    y: Int,
    z: Int,
    value: Int
  ) : this(Position(x, y, z), value)

}
