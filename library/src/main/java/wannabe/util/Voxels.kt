package wannabe.util

import wannabe.XYBounds
import wannabe.Position
import wannabe.Translation
import wannabe.Voxel
import wannabe.grid.MutableGrid

/** Voxel and Grid utilities.  */
object Voxels {
  /**
   * Parses a textmap to create a grid. newlines separate rows.  Any characters not in the value
   * map will act as spacers (leaves empty space in the final grid).
   *
   * @param grid Grid to populate
   * @param topLeft top-left position to start reading into.
   * @param textmap two-dimensional array of characters representing the desired output.
   * @param charToColor map of character to value describing what colors to use.
   */
  fun fromTextMap(
    grid: MutableGrid,
    topLeft: Position,
    textmap: String,
    charToColor: Map<Char, Int>
  ) {
    val workhorse = Translation(topLeft)
    for (i in 0 until textmap.length) {
      val chr = textmap[i]
      if (chr == '\n') {
        // Found a new line, so increment y, reset x.
        workhorse.x = 0
        workhorse.y++
        continue
      }
      if (charToColor.containsKey(chr)) {
        val color = charToColor[chr]!!
        grid.put(Voxel(workhorse.asPosition(), color))
      }
      workhorse.x++
    }
  }

  /**
   * Draw a Bresenham line, uses all three dimensions.
   * Adapted from: https://www.ict.griffith.edu.au/anthony/info/graphics/bresenham.procs
   */
  fun line(
    grid: MutableGrid,
    p1: Position,
    p2: Position,
    color: Int
  ) {
    var i: Int
    val dx: Int
    val dy: Int
    val dz: Int
    val l: Int
    val m: Int
    val n: Int
    val x_inc: Int
    val y_inc: Int
    val z_inc: Int
    var err_1: Int
    var err_2: Int
    val dx2: Int
    val dy2: Int
    val dz2: Int
    val pixel = Translation(p1)
    dx = p2.x - p1.x
    dy = p2.y - p1.y
    dz = p2.z - p1.z
    x_inc = if (dx < 0) -1 else 1
    l = Math.abs(dx)
    y_inc = if (dy < 0) -1 else 1
    m = Math.abs(dy)
    z_inc = if (dz < 0) -1 else 1
    n = Math.abs(dz)
    dx2 = l shl 1
    dy2 = m shl 1
    dz2 = n shl 1
    if (l >= m && l >= n) {
      err_1 = dy2 - l
      err_2 = dz2 - l
      i = 0
      while (i < l) {
        grid.put(Voxel(pixel.asPosition(), color))
        if (err_1 > 0) {
          pixel.y += y_inc
          err_1 -= dx2
        }
        if (err_2 > 0) {
          pixel.z += z_inc
          err_2 -= dx2
        }
        err_1 += dy2
        err_2 += dz2
        pixel.x += x_inc
        i++
      }
    } else if (m >= l && m >= n) {
      err_1 = dx2 - m
      err_2 = dz2 - m
      i = 0
      while (i < m) {
        grid.put(Voxel(pixel.asPosition(), color))
        if (err_1 > 0) {
          pixel.x += x_inc
          err_1 -= dy2
        }
        if (err_2 > 0) {
          pixel.z += z_inc
          err_2 -= dy2
        }
        err_1 += dx2
        err_2 += dz2
        pixel.y += y_inc
        i++
      }
    } else {
      err_1 = dy2 - n
      err_2 = dx2 - n
      i = 0
      while (i < n) {
        grid.put(Voxel(pixel.asPosition(), color))
        if (err_1 > 0) {
          pixel.y += y_inc
          err_1 -= dz2
        }
        if (err_2 > 0) {
          pixel.x += x_inc
          err_2 -= dz2
        }
        err_1 += dy2
        err_2 += dx2
        pixel.z += z_inc
        i++
      }
    }
    grid.put(Voxel(pixel.asPosition(), color))
  }

  fun drawPath(
    grid: MutableGrid,
    path: Path
  ) {
    var pos: Translation? = Translation(path.start())
    while (pos != null) {
      pos = path.drawAndMove(grid, pos)
    }
  }

  /**
   * Simple mechanism to iteratively add a set of voxels. See [.drawPath].
   * TODO Can I make an Iterable? Would that gain anything?
   */
  interface Path {
    /** Set up the path and return its starting Position.  */
    fun start(): Position

    /**
     * Adds voxel(s) at the specified location, and returns the next location, or `null` if
     * complete.
     */
    fun drawAndMove(
      grid: MutableGrid,
      current: Translation
    ): Translation?
  }

  /**
   * Constructs a voxel with position x, y, and a calculated z. Returns `null` if there is
   * nothing to plot at this x and y
   */
  interface ZPlotter {
    fun plot(
      x: Int,
      y: Int
    ): Voxel?
  }

  /** Fills every x,y with a voxel at z with value `value`.  */
  class FloodFillZPlotter(
    private val z: Int,
    private val value: Int
  ) : ZPlotter {
    override fun plot(
      x: Int,
      y: Int
    ): Voxel? {
      return Voxel(x, y, z, value)
    }
  }

  /**
   * If x or y matches either bound of `bounds` plots a voxel at z with value `value`,
   * otherwise returns null;
   */
  class EdgesZPlotter(
    val bounds: XYBounds,
    val z: Int,
    val value: Int
  ) : ZPlotter {
    override fun plot(
      x: Int,
      y: Int
    ): Voxel? {
      return if (bounds.isEdge(x, y)) Voxel(x, y, z, value) else null
    }
  }

  /**
   * Constructs a voxel with position x, z, and a calculated y. Returns `null` if there is
   * nothing to plot at this x and z
   */
  interface YPlotter {
    fun plot(
      x: Int,
      z: Int
    ): Voxel?
  }
}
