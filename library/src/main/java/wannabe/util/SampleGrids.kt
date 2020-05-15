package wannabe.util

import wannabe.Position
import wannabe.Translation
import wannabe.Voxel
import wannabe.XYBounds
import wannabe.grid.FrameAnimatedGrid
import wannabe.grid.Grid
import wannabe.grid.MutableGrid
import wannabe.grid.SimpleGrid
import wannabe.util.Voxels.Path
import wannabe.util.Voxels.ZPlotter
import wannabe.util.Voxels.drawPath
import wannabe.util.Voxels.fromTextMap
import wannabe.util.Voxels.line
import java.util.HashMap
import java.util.Random
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sin

object SampleGrids {
  fun originGrid(): Grid {
    val grid: MutableGrid = SimpleGrid("Origin grid on XY at z=0")
    grid.put(Voxel(0, 0, 0, 0xFFEEDD))
    grid.put(Voxel(-1, 0, 0, 0xEEDDCC))
    grid.put(Voxel(1, 0, 0, 0xEEDDCC))
    grid.put(Voxel(0, -1, 0, 0xEEDDCC))
    grid.put(Voxel(0, 1, 0, 0xEEDDCC))

    grid.put(Voxel(-5, 0, 0, 0xDDCCBB))
    grid.put(Voxel(5, 0, 0, 0xDDCCBB))
    grid.put(Voxel(0, -5, 0, 0xDDCCBB))
    grid.put(Voxel(0, 5, 0, 0xDDCCBB))

    grid.put(Voxel(-10, 0, 0, 0xCCBBAA))
    grid.put(Voxel(10, 0, 0, 0xCCBBAA))
    grid.put(Voxel(0, -10, 0, 0xCCBBAA))
    grid.put(Voxel(0, 10, 0, 0xCCBBAA))

    return grid
  }

  fun originGridZ(): Grid {
    val grid: MutableGrid = SimpleGrid("Origin grid on XZ at y=0")
    grid.put(Voxel(0, 0, 0, 0xFFEEDD))
    grid.put(Voxel(-1, 0, 0, 0xEEDDCC))
    grid.put(Voxel(1, 0, 0, 0xEEDDCC))
    grid.put(Voxel(0, 0, -1, 0xEEDDCC))
    grid.put(Voxel(0, 0, 1, 0xEEDDCC))

    grid.put(Voxel(-5, 0, 0, 0xDDCCBB))
    grid.put(Voxel(5, 0, 0, 0xDDCCBB))
    grid.put(Voxel(0, 0, -5, 0xDDCCBB))
    grid.put(Voxel(0, 0, 5, 0xDDCCBB))

    grid.put(Voxel(-10, 0, 0, 0xCCBBAA))
    grid.put(Voxel(10, 0, 0, 0xCCBBAA))
    grid.put(Voxel(0, 0, -10, 0xCCBBAA))
    grid.put(Voxel(0, 0, 10, 0xCCBBAA))
    
    grid.put(Voxel(-15, 0, 0, 0xBBAA99))
    grid.put(Voxel(15, 0, 0, 0xBBAA99))
    grid.put(Voxel(0, 0, -15, 0xBBAA99))
    grid.put(Voxel(0, 0, 15, 0xBBAA99))

    return grid
  }

  /** Grid stretching 30x30 with voxels every 10 along the edge, and 600 random voxels.  */
  fun randomGrid(): Grid {
    val grid: MutableGrid = SimpleGrid("random sparse 30x30")
    grid.put(Voxel(0, 0, 0, 0xFFEEDD))
    grid.put(Voxel(1, 0, 0, 0xEEDDFF))
    grid.put(Voxel(0, 1, 0, 0xDDFFEE))
    grid.put(Voxel(10, 0, 0, 0xDFFEED))
    grid.put(Voxel(0, 10, 0, 0xDFFEED))
    grid.put(Voxel(20, 0, 0, 0xFEEDDF))
    grid.put(Voxel(0, 20, 0, 0xEDDFFE))
    grid.put(Voxel(30, 0, 0, 0xFEDFED))
    grid.put(Voxel(0, 30, 0, 0xDEFDEF))
    val r = Random()
    for (i in 0..599) {
      grid.put(
          Voxel(
              r.nextInt(30), r.nextInt(30), r.nextInt(21) - 10, 0xAAAAAA + r
              .nextInt(0x555555)
          )
      )
    }
    return grid
  }

  /** Grid stretching 30x30 with voxels in simple patterns like stairsteps, etc  */
  fun steps(): Grid {
    val grid: MutableGrid = SimpleGrid("steps 30x30")

    // First set of steps: single color stairs to floor.
    var start = Position(5, 5, 0)
    drawPath(
        grid,
        HeightColorStairs(1, 0, start, Position(10, 5, 0), 0xFFEEDD)
    )
    drawPath(
        grid,
        HeightColorStairs(0, 1, start, Position(5, 10, 0), 0xFFEEDD)
    )
    drawPath(
        grid,
        HeightColorStairs(-1, 0, start, Position(0, 5, 0), 0xFFEEDD)
    )
    drawPath(
        grid,
        HeightColorStairs(0, -1, start, Position(5, 0, 0), 0xFFEEDD)
    )

    // Second set of steps: color varies by height
    start = Position(20, 20, 0)
    drawPath(grid, Stairs(1, 0, start, 6, 0xFFEEDD))
    drawPath(grid, Stairs(0, 1, start, 6, 0xFFEEDD))
    drawPath(grid, Stairs(-1, 0, start, 6, 0xFFEEDD))
    drawPath(grid, Stairs(0, -1, start, 6, 0xFFEEDD))

    // Third set of steps: color varies by height
    var stairColors =
      intArrayOf(0xDFFEED, 0xDEEEED, 0xDDDEED, 0xDCCEED, 0xDBBEED, 0xDAAEED)
    start = Position(20, 5, 0)
    drawPath(
        grid,
        HeightColorStairs(1, 0, start, Position(25, 5, 0), *stairColors)
    )
    drawPath(
        grid,
        HeightColorStairs(0, 1, start, Position(20, 10, 0), *stairColors)
    )
    drawPath(
        grid,
        HeightColorStairs(-1, 0, start, Position(15, 5, 0), *stairColors)
    )
    drawPath(
        grid,
        HeightColorStairs(0, -1, start, Position(20, 0, 0), *stairColors)
    )

    // Fourth set of steps: color varies by column
    stairColors = intArrayOf(0xFEEDDF, 0xEEEDDE, 0xDEEDDD, 0xCEEDDC, 0xBEEDDB, 0xAEEDDA)
    start = Position(5, 20, 0)
    drawPath(grid, ColumnColorStairs(1, 0, start, *stairColors))
    drawPath(grid, ColumnColorStairs(0, 1, start, *stairColors))
    drawPath(grid, ColumnColorStairs(-1, 0, start, *stairColors))
    drawPath(grid, ColumnColorStairs(0, -1, start, *stairColors))

    // Fifth: flat cross-hatch, with order-of-adds different on each line
    // TL to BR
    start = Position(30, 0, 0)
    var end = Position(40, 10, 0)
    line(grid, start, end, 0xFFEEDD)

    // TR to BL
    start = Position(40, 0, 0)
    end = Position(30, 10, 0)
    line(grid, start, end, 0xFFEEDD)

    // BR to TL
    start = Position(40, 8, 0)
    end = Position(32, 0, 0)
    line(grid, start, end, 0xFFEEDD)

    // BL to TR
    start = Position(30, 8, 0)
    end = Position(38, 0, 0)
    line(grid, start, end, 0xFFEEDD)
    return grid
  }

  /** Plot of an exaggerated sine. Note that for pleasing results, the origin is shifted.  */
  fun plotSin(mulitplyer: Int): Grid {
    // TODO at low multipliers the colors aren't distinct enough.
    val grid: MutableGrid = SimpleGrid("Sine plot x$mulitplyer 40x40")
    val plotter: ZPlotter = object : ZPlotter {
      override fun plot(
        x: Int,
        y: Int
      ): Voxel? {
        val distanceFromOrigin = hypot(x.toDouble(), y.toDouble())
        val z = (mulitplyer * sin(distanceFromOrigin)).toInt()
        return Voxel(x, y, z, 0x888888 + z * 10)
      }
    }
    val bounds = XYBounds()
    bounds.setFromWidthHeight(-20, -20, 40, 40)
    bounds.plot(grid, plotter)
    return grid
  }

  /** Plot of a flattened hyperbola. Note that for pleasing results, the origin is shifted.  */
  fun plotHyperbola(d: Double): Grid {
    val grid: MutableGrid = SimpleGrid("Hyperbola x$d 40x40")
    val plotter: ZPlotter = object : ZPlotter {
      override fun plot(
        x: Int,
        y: Int
      ): Voxel? {
        val distanceFromOrigin = hypot(x.toDouble(), y.toDouble())
        val z = (d * distanceFromOrigin * distanceFromOrigin).toInt()
        return Voxel(x, y, z, 0x888888 + z * 10)
      }
    }
    val bounds = XYBounds()
    bounds.setFromWidthHeight(-20, -20, 40, 40)
    bounds.plot(grid, plotter)
    return grid
  }

  fun hollowCube(
    size: Int,
    color: Int
  ): Grid {
    val grid: MutableGrid = SimpleGrid("cube of size $size")
    val bnw = Position(0, 0, 0)
    val bne = Position(size, 0, 0)
    val bsw = Position(0, size, 0)
    val bse = Position(size, size, 0)
    val tnw = Position(0, 0, size)
    val tne = Position(size, 0, size)
    val tsw = Position(0, size, size)
    val tse = Position(size, size, size)
    // Base:
    line(grid, bnw, bne, color)
    line(grid, bsw, bse, color)
    line(grid, bnw, bsw, color)
    line(grid, bne, bse, color)
    // Top:
    line(grid, tnw, tne, color)
    line(grid, tsw, tse, color)
    line(grid, tnw, tsw, color)
    line(grid, tne, tse, color)
    // Risers (these actually introduce duplicate voxels, should clean that up sometime):
    line(grid, bnw, tnw, color)
    line(grid, bne, tne, color)
    line(grid, bse, tse, color)
    line(grid, bsw, tsw, color)
    return grid
  }

  /** 30x30 grid with all 900 voxels specified.  */
  fun fullRandomGrid(): Grid {
    val grid: MutableGrid = SimpleGrid("random full 30x30")
    val r = Random()
    for (i in 0..899) {
      val x = i / 30
      val y = i % 30
      val hasHeight = x % 5 == 0 && y % 5 == 0
      val z = if (hasHeight) r.nextInt(21) - 10 else 0
      val color = 0x999999 + r.nextInt(0x666666)
      val vox = Voxel(x, y, z, color)
      grid.put(vox)

      // If not animated, and if positive z, make into a column.
      for (j in 0 until z) {
        grid.put(Voxel(x, y, j, color))
      }
    }
    return grid
  }

  /** Creates a grid with two hundred towers of up to 50 voxels in a 30x30 grid.  */
  fun towers(): Grid {
    val grid: MutableGrid = SimpleGrid("200 towers 30x30")
    val r = Random()
    for (i in 0..199) {
      val x = r.nextInt(30)
      val y = r.nextInt(30)
      val height = r.nextInt(50)
      for (z in 0 until height) {
        val color = 0x999999 + r.nextInt(0x666666)
        grid.put(Voxel(x, y, z, color))
      }
    }
    return grid
  }

  fun linkGrid(): Grid {
    val colorMap: MutableMap<Char, Int> = HashMap(3)
    colorMap['G'] = 0xadfc14 // Green
    colorMap['S'] = 0xff8f2b // Light brown
    colorMap['H'] = 0xdb450f // Dark brown
    val grid = SimpleGrid("link")
    fromTextMap(
        grid, Position(0, 0, 0), """
   .....GGGGGG....
   ....GGGGGGGG...
   ..S.GHHHHHHG.S.
   ..S.HHHHHHHH.S.
   ..SSHSGSSGSHSS.
   ..SSHSHSSHSHSS.
   ...SSSSSSSSSSH.
   ...GGSSHHSSGGH.
   .HHHHHSSSSGGHHH
   HHSHHHHGGGGGSHH
   HSSSHHSHHGGSSSH
   HHSHHHSGHHHHSSS
   HHSHHHSHHGGGGS.
   HHHHHHSGGGGG...
   .SSSSSH..HHH...
   ....HHH........
   
   """.trimIndent(),
        colorMap
    )
    return grid
  }

  fun morboCat(): Grid {
    val colorMap: MutableMap<Char, Int> = HashMap(3)
    colorMap['*'] = 0x000000 // Black
    colorMap['@'] = 0x444444 // Gray
    colorMap['@'] = 0xbbbbbb // Gray
    colorMap['y'] = 0xaaaa00 // Yellow
    colorMap['.'] = 0xffffff // White
    val frame = SimpleGrid("morbo")
    fromTextMap(
        frame, Position(0, 0, 0), """
   ...........................................................
   ......**...................................................
   .....**.............................*.......*..............
   ....**..............................**.....**..............
   ...**...............................*.*...***..............
   ..**................................*..***..*..............
   ..**...............................*.........*.............
   ..**..............................*..yy...yy..*............
   ..**..............................*..y*...*y..*............
   ..**..............................*...........*............
   ..**...***************.............***********.............
   ...******..........***************..........*..............
   ...*........................................*..............
   ...*........................................*..............
   ...*.......................................*...............
   ....*......................................*...............
   ......*....................................*...............
   .......****************************************............
   ......**...**................**...........******...........
   .....**.....**...............**................****........
   ....**......**...............**...................***......
   ....**.......**..............**............................
   ...**........**...............**...........................
   
   """.trimIndent(),
        colorMap
    )
    return frame
  }

  fun megaManRunning(): FrameAnimatedGrid {
    val colorMap: MutableMap<Char, Int> = HashMap(3)
    colorMap['*'] = 0x000000 // Black
    colorMap['B'] = 0x0b59f4 // Dark blue
    colorMap['t'] = 0x21ffff // Turquoise
    colorMap['O'] = 0xffffff // White
    colorMap['f'] = 0xfede9c // Face
    val frame1 = SimpleGrid("mmr1")
    val topLeft = Position(0, 0, 40)
    fromTextMap(
        frame1, topLeft, """
   .........................
   .........................
   .............***.........
   ...........***tt*........
   ..........*BBB*tt*.......
   .........*BBBBB****......
   .........*BBBBB*ttB*.....
   ......***tBBBBBB**B*.....
   ....**tt*tBBfOOOBBO*.....
   ...*B*tt*tBfOO**f*O*.....
   ..*BBB*tt*BfOO**f*O*.***.
   .*BBBB*tt*BffOOOfOf**BBB*
   .*BBB*.*tt*Bf****f*.*BBB*
   .*BBB*.*ttt*fffff*t**BBB*
   ..***..*Bttt*****tttBBB*.
   ....**.*BBBttttt*ttBBB*..
   ...*BB**BBBBBBt*.*tBB*...
   ..*BBBB**BBttt*...***....
   .*BBBBBt**Btttt*.........
   .*BB*BBttt**tttB*........
   ..****Btt*.*BBBB*........
   ......***..*BBB***.......
   ..........*BBBBBBB*......
   ..........*********......
   
   """.trimIndent(),
        colorMap
    )
    val frame2 = SimpleGrid("mmr2")
    fromTextMap(
        frame2, topLeft, """
   .............***.........
   ...........***tt*........
   ..........*BBB*tt*.......
   .........*BBBBB****......
   .........*BBBBB*ttB*.....
   ........*tBBBBBB**B*.....
   ........*tBBfOOOBBO*.....
   .......**tBfOO**f*O*.....
   ......*tt*BfOO**f*O*.....
   .....*ttt*BffOOOfOf*.....
   ....*ttttt*Bf****f*......
   ....*ttt*tt*fffff*.......
   .....*BBB*tt******.......
   .....*BBBB**tttt*B*......
   ......*BB*BB*tt*BB*......
   .......*BBBB*B****.......
   ........*BB*B*t*.........
   .........**tt*t*.........
   ........**ttt**..........
   .......*BBBBB*...........
   ......*BBBBB*............
   ......*BBB***............
   .......*BBBBB*...........
   ........******...........
   
   """.trimIndent(),
        colorMap
    )
    val frame3 = SimpleGrid("mmr3")
    fromTextMap(
        frame3, topLeft, """
   .........................
   .........................
   .............***.........
   ...........***tt*........
   ..........*BBB*tt*.......
   .........*BBBBB****......
   .........*BBBBB*ttB*.....
   ........*tBBBBBB**B*.....
   ........*tBBfOOOBBO*.....
   ......***tBfOO**f*O*.....
   ....**ttt*BfOO**f*O*.....
   ...*BBBttt*ffOOOfOf*.....
   ..*BBBBtt*ttf****f*......
   ..*BBB***ttttBfff***.....
   ..*BBB*.*ttttt***BBB*....
   ...***.*Bt*BttBBBBBB*....
   ..*BB***BBt*BtBBBBBB*....
   .*BBBB*tBBBB********.....
   *BBBBBtttBBBBttt*........
   *BB*BBttt***tttBB*.......
   .****BBt*...*BBBB*.......
   .....***....*BBB***......
   ...........*BBBBBBB*.....
   ...........*********.....
   
   """.trimIndent(),
        colorMap
    )
    val grid = FrameAnimatedGrid("megaman!")
    grid.addFrame(frame1)
    grid.addFrame(frame2)
    grid.addFrame(frame3)
    grid.addFrame(frame2)
    return grid
  }

  /** Builds a few different 10 x 10 shapes.  */
  fun neighborTest(): Grid {
    val grid = SimpleGrid("Neighbor test")
    val start = Translation(5, 5, 0)
    val end = Translation(5, 14, 0)
    // 10x10x10 cube
    for (z in 1..10) {
      start.z = z
      end.z = z
      for (x in 5..14) {
        start.x = x
        end.x = x
        line(grid, start.asPosition(), end.asPosition(), 0x2a2a2a * z / 2)
      }
    }

    // Draw a couple slightly-overlapping squares
    val r = Random()
    start.x = 20
    start.z = 4
    end.x = 29
    end.z = 4
    for (y in 5..14) {
      start.y = y
      end.y = y
      val color = 0x999999 + r.nextInt(0x666666)
      // was 0x2a2a2a * start.z / 2
      line(grid, start.asPosition(), end.asPosition(), color)
    }
    start.y = 13
    start.z = 5
    end.y = 22
    end.z = 5
    for (x in 28..37) {
      start.x = x
      end.x = x
      val color = 0x999999 + r.nextInt(0x666666)
      line(grid, start.asPosition(), end.asPosition(), color)
    }

    // Draw this one barely adjacent to the last, and in reverse order
    start.x = 29
    start.z = 5
    end.x = 20
    end.z = 5
    for (y in 32 downTo 23) {
      start.y = y
      end.y = y
      val color = 0x999999 + r.nextInt(0x666666)
      line(grid, start.asPosition(), end.asPosition(), color)
    }
    return grid
  }

  val GRIDS = listOf(
          originGrid(),
          originGridZ(),
          neighborTest(),
          steps(),
          HouseVignette().buildHouse(),
          plotSin(5),
          plotSin(2),
          plotHyperbola(.2),
          linkGrid(),
          towers(),
          fullRandomGrid(),
          randomGrid(),
          hollowCube(20, 0x21ffff),
          morboCat()
      )

  fun next(current: Grid): Grid {
    val nextIdx = GRIDS.indexOf(current) + 1
    return GRIDS[if (nextIdx < GRIDS.size) nextIdx else 0]
  }

  class Stairs(
    private val xIncrement: Int,
    private val yIncrement: Int,
    private val start: Position,
    private val count: Int,
    private val color: Int
  ) : Path {
    override fun start(): Position {
      return start
    }

    override fun drawAndMove(
      grid: MutableGrid,
      current: Translation
    ): Translation? {
      grid.put(Voxel(current.asPosition(), color))
      current.x += xIncrement
      current.y += yIncrement
      current.z++
      return if (current.z - count == 0) null else current
    }

  }

  class ColumnColorStairs(
    private val xIncrement: Int,
    private val yIncrement: Int,
    private val start: Position,
    private vararg val colors: Int
  ) : Path {
    override fun start(): Position {
      return start
    }

    override fun drawAndMove(
      grid: MutableGrid,
      current: Translation
    ): Translation? {
      val lineEnd = Position(current.x, current.y, 0)
      line(grid, current.asPosition(), lineEnd, getColor(current))
      current.x += xIncrement
      current.y += yIncrement
      current.z++
      return if (current.z == 6) null else current
    }

    private fun getColor(pos: Translation): Int {
      var offset = abs(pos.x - start.x)
      if (offset == 0) {
        offset = abs(pos.y - start.y)
      }
      if (offset >= colors.size) offset = colors.size - 1
      return colors[offset]
    }
  }

  /** Simple stairs on x or y axis that may vary in color by height.  */
  class HeightColorStairs(
    private val xIncrement: Int,
    private val yIncrement: Int,
    private val start: Position,
    private val endXY: Position,
    private vararg val colors: Int
  ) : Path {
    override fun start(): Position {
      return start
    }

    override fun drawAndMove(
      grid: MutableGrid,
      current: Translation
    ): Translation? {
      val lineEnd = Position(endXY.x, endXY.y, current.z)
      line(grid, current.asPosition(), lineEnd, getColor(current))
      current.x += xIncrement
      current.y += yIncrement
      current.z++
      return if (current.z == 6) null else current
    }

    private fun getColor(pos: Translation): Int {
      if (colors.isEmpty()) return 0x888888
      var offset = pos.z
      if (offset >= colors.size) offset = colors.size - 1
      return colors[offset]
    }
  }
}
