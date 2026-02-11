package wannabe.util

import wannabe.Position
import wannabe.Voxel
import wannabe.XYBounds
import wannabe.XZBounds
import wannabe.grid.Grid
import wannabe.grid.SimpleGrid
import wannabe.util.Voxels.EdgesZPlotter
import wannabe.util.Voxels.FloodFillZPlotter
import wannabe.util.Voxels.YPlotter
import wannabe.util.Voxels.line
import java.util.Random

/**
 * An attempt to make a scene with realistic elements. Should be "real" positioning as much as
 * possible without fakes or facades.
 *
 * Scene:
 *
 *  * House on the left with door, windows, roof.
 *  * Fenced yard with pathway and gate
 *  * Distant hills
 *  * Dark, cloudy sky
 *  * Animated rain coming in at an angle toward the house.
 *
 */
class HouseVignette {
  fun buildHouse(): Grid {
    val scene = SimpleGrid("House")

    // House
    house(scene)
    roof(scene)
    door(scene)
    windows(scene)

    // Yard
    yard(scene)
    path(scene)

    // Background
    hills(scene)
    clouds(scene)
    rain(scene)
    return scene
  }

  private fun house(scene: SimpleGrid) {
    // Flood-fill a 20x20x15 base house from -8,10,-5 to 12,30,10
    // Make the house hollow
    val bounds = XYBounds()
    bounds.setFromWidthHeight(-8, 10, 20, 20)
    for (z in -5..9) {
      bounds.plot(scene, EdgesZPlotter(bounds, z, HOUSE_COLOR))
    }

    // Left and right sides:
    bounds.plot(scene, FloodFillZPlotter(-5, HOUSE_COLOR))
    bounds.plot(scene, FloodFillZPlotter(9, HOUSE_COLOR))
  }

  private fun roof(scene: SimpleGrid) {
    // Add a roof. Extends 2 over the sides of the house. Spine runs along the x axis.
    // In practice, a triangular prism, just made out of lines stacked together.
    // TODO: eaves should be hollow, ie just the edges hang over
    // TODO ...could do that via some lines at the end
    // First level: full depth
    var y = 9
    for (z in -7..11) {
      val left = Position(-9, y, z)
      val right = Position(12, y, z)
      line(scene, left, right, ROOF_COLOR)
    }
    // Next level: -2 depth on each side
    y = 8
    for (z in -5..9) {
      val left = Position(-9, y, z)
      val right = Position(12, y, z)
      line(scene, left, right, ROOF_COLOR)
    }
    // Next level: -4 depth on each side
    y = 7
    for (z in -3..7) {
      val left = Position(-9, y, z)
      val right = Position(12, y, z)
      line(scene, left, right, ROOF_COLOR)
    }
    // Next level: -6 depth on each side
    y = 6
    for (z in -1..5) {
      val left = Position(-9, y, z)
      val right = Position(12, y, z)
      line(scene, left, right, ROOF_COLOR)
    }
    // Next level: -8 depth on each side
    y = 5
    for (z in 1..3) {
      val left = Position(-9, y, z)
      val right = Position(12, y, z)
      line(scene, left, right, ROOF_COLOR)
    }
  }

  private fun door(scene: SimpleGrid) {
    // Frame
    var topLeft = Position(11, 13, 7)
    var topRight = Position(11, 13, 2)
    var botLeft = Position(11, 29, 7)
    var botRight = Position(11, 29, 2)
    line(scene, topLeft, topRight, FRAMING_COLOR)
    line(scene, topLeft, botLeft, FRAMING_COLOR)
    line(scene, topRight, botRight, FRAMING_COLOR)

    // Doorknob
    scene.put(Voxel(12, 23, 6, DOOR_KNOB_COLOR))

    // Door window (T, B, L, R from the perspective of facing the door from the outside)
    topLeft = Position(11, 15, 5)
    botLeft = Position(11, 20, 5)
    topRight = Position(11, 15, 4)
    botRight = Position(11, 20, 4)
    line(scene, topLeft, botLeft, GLASS_COLOR)
    line(scene, topRight, botRight, GLASS_COLOR)
  }

  private fun windows(scene: SimpleGrid) {
    // "Front" window, beside the door (T, B, L, R from the perspective of facing the window outside)
    val windowTop = 14
    val windowBot = 25
    val topLeft = Position(11, windowTop, 0)
    val topRight = Position(11, windowTop, -4)
    val botLeft = Position(11, windowBot, 0)
    val botRight = Position(11, windowBot, -4)

    // - Frame
    line(scene, topLeft, topRight, FRAMING_COLOR)
    line(scene, topLeft, botLeft, FRAMING_COLOR)
    line(scene, topRight, botRight, FRAMING_COLOR)
    line(scene, botLeft, botRight, FRAMING_COLOR)
    // - Glass
    for (z in -3..-1) {
      line(
          scene, Position(11, windowTop + 1, z), Position(11, windowBot - 1, z),
          GLASS_COLOR
      )
    }

    // Left side rear window (facing from the outside)
    xyWindow(scene, -6, 9)

    // Left side fore window
    xyWindow(scene, 4, 9)

    // Right side rear window (unlike others, as facing window from inside)
    xyWindow(scene, -6, -5)

    // Right side fore window
    xyWindow(scene, 4, -5)
  }

  /** Draws a window on the x-y plane at the specified left and z values.  */
  private fun xyWindow(
    scene: SimpleGrid,
    left: Int,
    z: Int
  ) {
    val windowTop = 14
    val windowBot = 25
    val topLeft = Position(left, windowTop, z)
    val topRight = Position(left + 5, windowTop, z)
    val botLeft = Position(left, windowBot, z)
    val botRight = Position(left + 5, windowBot, z)
    // - Frame
    line(scene, topLeft, topRight, FRAMING_COLOR)
    line(scene, topLeft, botLeft, FRAMING_COLOR)
    line(scene, topRight, botRight, FRAMING_COLOR)
    line(scene, botLeft, botRight, FRAMING_COLOR)
    // - Glass
    for (x in left + 1 until left + 5) {
      line(
          scene, Position(x, windowTop + 1, z), Position(x, windowBot - 1, z),
          GLASS_COLOR
      )
    }
  }

  private fun yard(scene: SimpleGrid) {
    // Yard is just a flat bit of greens beneath the house, stretches 5 beyond the walls on
    // the sides and back, 25 out the front.
    val r = Random()
    val bounds = XZBounds()
    bounds.setFromWidthHeight(-13, -10, 50, 30)
    bounds.plot(scene, object : YPlotter {
      override fun plot(
        x: Int,
        z: Int
      ): Voxel? {
        return Voxel(x, 30, z, -0xff6000 + (r.nextInt(128) shl 16) + r.nextInt(128))
      }
    })
  }

  private fun path(scene: SimpleGrid) {
    // TODO Auto-generated method stub
  }

  private fun hills(scene: SimpleGrid) {
    // TODO Auto-generated method stub
  }

  private fun clouds(scene: SimpleGrid) {
    // TODO Auto-generated method stub
  }

  private fun rain(scene: SimpleGrid) {
    // TODO Auto-generated method stub
  }

  companion object {
    private const val FRAMING_COLOR = 0xFFFFFFFF.toInt()
    private const val DOOR_KNOB_COLOR = 0xFFFDCD50.toInt()
    private const val ROOF_COLOR = 0xFFAA4444.toInt()
    private const val HOUSE_COLOR = 0xFFAA9988.toInt()
    private const val GLASS_COLOR = 0x60888888.toInt()
  }
}
