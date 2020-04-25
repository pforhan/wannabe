package wannabe.grid.iterators

import wannabe.Voxel

class RotatingIterator(
  private val realIterator: Iterator<Voxel>,
  rotation: RotationDegrees
) : Iterator<Voxel> {
  private val xRad: Double
  private val yRad: Double
  private val noRotate: Boolean

  init {
    noRotate = rotation.isZero
    xRad = Math.toRadians(rotation.x.toDouble())
    yRad = Math.toRadians(rotation.y.toDouble())
    //zRad = Math.toRadians(rotation.z); // not used, probably the 0 term
  }

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel {
    val real = realIterator.next()

    // Special case, if we have nothing to do just use the real voxel.
    if (noRotate) {
      return real
    }

    val (x, y, z) = real.position

    // from https://www.opengl.org/discussion_boards/showthread.php/139444-Easiest-way-to-rotate-point-in-3d-using-trig
    // only rotates about the x and y axes
    // TODO let a matrix lib handle this
    val newX = (Math.cos(yRad) * x
        + Math.sin(yRad) * Math.sin(xRad) * y
        - Math.sin(yRad) * Math.cos(xRad) * z).toInt()
    val newY =
      (0 + Math.cos(xRad) * y + Math.sin(xRad) * z).toInt()
    val newZ =
      (Math.sin(yRad) * x + Math.cos(yRad) * -Math.sin(xRad) * y + Math.cos(yRad) * Math.cos(
          xRad
      ) * z).toInt()
    return Voxel(newX, newY, newZ, real.value)
  }
}

data class RotationDegrees(
  var x: Int = 0,
  var y: Int = 0,
  var z: Int = 0
) {
  val isZero: Boolean
    get() = x == 0 && y == 0 && z == 0
}
