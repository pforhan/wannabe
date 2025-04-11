package wannabe.grid.iterators

import org.jetbrains.kotlinx.multik.api.identity
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import wannabe.Position
import wannabe.Voxel
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class RotatingIterator(
  private val realIterator: Iterator<Voxel>,
  rotation: RotationDegrees,
  around: Position = Position.ZERO
) : Iterator<Voxel> {
  private val noRotate: Boolean = rotation.isZero
  private val transformationMatrix: D2Array<Double> =
      if (noRotate) mk.identity(4) else createTransformationMatrix(rotation, around)

  override fun hasNext(): Boolean = realIterator.hasNext()

  override fun next(): Voxel {
    val real = realIterator.next()

    // Special case, if we have nothing to do just use the real voxel.
    if (noRotate) {
      return real
    }

    // Create a workhorse vector for the position including the homogenous coordinate
    val (x, y, z) = real.position

    val workhorse = mk.ndarray(mk[x.toDouble(), y.toDouble(), z.toDouble(), 1.0], 4, 1)

    // Apply the transformation matrix to the workhorse vector
    val transformed = transformationMatrix.dot(workhorse)

    // Extract the new coordinates from the transformed vector
    val newX = transformed[0, 0].roundToInt()
    val newY = transformed[1, 0].roundToInt()
    val newZ = transformed[2, 0].roundToInt()

    return Voxel(newX, newY, newZ, real.value)
  }

  private fun createTransformationMatrix(
    rotation: RotationDegrees,
    around: Position
  ): D2Array<Double> {
    // Convert degrees to radians.
    val xRad = Math.toRadians(rotation.x.toDouble())
    val yRad = Math.toRadians(rotation.y.toDouble())
    val zRad = Math.toRadians(rotation.z.toDouble())

    // Create rotation matrices around each axis.
    val xCos = cos(xRad)
    val xSin = sin(xRad)
    val rx = mk.ndarray(
      mk[
        mk[1.0, 0.0, 0.0, 0.0],
        mk[0.0, xCos, -xSin, 0.0],
        mk[0.0, xSin, xCos, 0.0],
        mk[0.0, 0.0, 0.0, 1.0]
      ]
    )
    val yCos = cos(yRad)
    val ySin = sin(yRad)
    val ry = mk.ndarray(
      mk[
        mk[yCos, 0.0, ySin, 0.0],
        mk[0.0, 1.0, 0.0, 0.0],
        mk[-ySin, 0.0, yCos, 0.0],
        mk[0.0, 0.0, 0.0, 1.0]
      ]
    )
    val zCos = cos(zRad)
    val zSin = sin(zRad)
    val rz = mk.ndarray(
      mk[
        mk[zCos, -zSin, 0.0, 0.0],
        mk[zSin, zCos, 0.0, 0.0],
        mk[0.0, 0.0, 1.0, 0.0],
        mk[0.0, 0.0, 0.0, 1.0]
      ]
    )

    // Translation
    val xDbl = around.x.toDouble()
    val yDbl = around.y.toDouble()
    val zDbl = around.z.toDouble()
    val toOrigin = mk.ndarray(
      mk[
        mk[1.0, 0.0, 0.0, -xDbl],
        mk[0.0, 1.0, 0.0, -yDbl],
        mk[0.0, 0.0, 1.0, -zDbl],
        mk[0.0, 0.0, 0.0, 1.0]
      ]
    )

    val fromOrigin = mk.ndarray(
      mk[
        mk[1.0, 0.0, 0.0, xDbl],
        mk[0.0, 1.0, 0.0, yDbl],
        mk[0.0, 0.0, 1.0, zDbl],
        mk[0.0, 0.0, 0.0, 1.0]
      ]
    )

    // Combine the rotations and translations.
    // ZYX order is most common, but you can change it if needed.
    // order matters here!
    return fromOrigin.dot(rz.dot(ry.dot(rx.dot(toOrigin))))
  }
}

data class RotationDegrees(
  val x: Int = 0,
  val y: Int = 0,
  val z: Int = 0,
) {
  val isZero: Boolean = x == 0 && y == 0 && z == 0
}
