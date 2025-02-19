package wannabe.grid.iterators

import wannabe.Voxel
import wannabe.Position
import wannabe.Translation
import kotlin.math.cos
import kotlin.math.sin

class RotatingIterator(
  private val realIterator: Iterator<Voxel>,
  rotation: RotationDegrees,
  private val around: Position = Position.ZERO
) : Iterator<Voxel> {
  private val xRad: Double = Math.toRadians(rotation.x.toDouble())
  private val yRad: Double = Math.toRadians(rotation.y.toDouble())
  //zRad = Math.toRadians(rotation.z); // not used, probably the 0 term
  private val noRotate: Boolean = rotation.isZero
  private val workhorse = Translation()

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel {
    val real = realIterator.next()

    // Special case, if we have nothing to do just use the real voxel.
    if (noRotate) {
      return real
    }

    // Move towards the custom origin:
    workhorse.set(real.position).subtract(around)
    val (x, y, z) = workhorse

    // from https://www.opengl.org/discussion_boards/showthread.php/139444-Easiest-way-to-rotate-point-in-3d-using-trig
    // only rotates about the x and y axes
    // TODO let a matrix lib handle this
    val newX = (cos(yRad) * x
        + sin(yRad) * sin(xRad) * y
        - sin(yRad) * cos(xRad) * z).toInt()
    val newY = (0 + cos(xRad) * y + sin(xRad) * z).toInt()
    val newZ = (sin(yRad) * x + cos(yRad) * -sin(xRad) * y + cos(yRad) * cos(xRad) * z).toInt()

    // move back to original location:
    workhorse.set(newX, newY, newZ).add(around)
    return Voxel(workhorse.asPosition(), real.value)
  }
}

data class RotationDegrees(
  val x: Int = 0,
  val y: Int = 0,
  val z: Int = 0,
) {
  val isZero: Boolean = x == 0 && y == 0 && z == 0
}
