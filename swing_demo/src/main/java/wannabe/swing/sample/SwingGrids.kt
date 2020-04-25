package wannabe.swing.sample

import wannabe.Voxel
import wannabe.grid.Grid
import wannabe.grid.MutableGrid
import wannabe.grid.SimpleGrid
import wannabe.util.SampleGrids
import java.awt.image.DataBufferByte
import java.io.IOException
import java.util.Arrays
import java.util.Random
import javax.imageio.ImageIO

object SwingGrids {
  /** Grid with the heightMap as a base, with "clouds" above and shadows below them.  */
  fun cloudySky(): Grid {
    val grid = heightMap("cloudy heightmap 256x256", false)
    val r = Random()
    for (row in 20..39) {
      for (col in 20..39) {
        // All clouds are about 50-60z in height. There are three for each x, y coordinate.
        // The higher a cloud, the lighter its color.
        val cloudHeight = r.nextInt(4) + 1 // 1-4
        val cloudColorComponent = (cloudHeight shl 3) + 199 // 199 - 255
        val cloudColor = ((0x7A shl 24) // alpha
            + (cloudColorComponent shl 16)
            + (cloudColorComponent shl 8)
            + cloudColorComponent) // 0x888888 - 0xFFFFFF
        grid.put(Voxel(col, row, 50 + cloudHeight, cloudColor))
        grid.put(Voxel(col, row, 51 + cloudHeight, cloudColor))
        grid.put(Voxel(col, row, 52 + cloudHeight, cloudColor))
      }
    }
    return grid
  }

  /**
   * Loads the sample-heightmap image into a Grid, with lighter pixels given a greater height
   * and a bluer color.
   */
  fun heightMap(
    name: String?,
    deep: Boolean
  ): MutableGrid {
    val grid: MutableGrid = SimpleGrid(name!!)
    try {
      val img =
        ImageIO.read(SwingGrids::class.java.getResourceAsStream("/example-heightmap.png"))
      val raster = img.raster
      val data = raster.dataBuffer as DataBufferByte
      val bytes = data.data
      val width = img.width
      for (i in bytes.indices) {
        val b = bytes[i]
        val x = i % width
        val y = i / width
        val z = (0xFF and b.toInt()) / 4
        var color = 0x888800 + b
        grid.put(Voxel(x, y, z, color))
        // Continue down to height 0 if deep:
        if (deep) {
          for (d in 0 until z) {
            color = 0x888800 + d * 4
            grid.put(Voxel(x, y, d, color))
          }
        }
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
    }
    return grid
  }

  val GRIDS = SampleGrids.GRIDS + listOf(
      heightMap("heightMap 256x256", false),
      heightMap("deep heightmap 256x256", true),
      cloudySky()
  )

  fun next(current: Grid): Grid {
    val nextIdx = GRIDS.indexOf(current) + 1
    return GRIDS[if (nextIdx < GRIDS.size) nextIdx else 0]
  }
}
