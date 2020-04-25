package wannabe.swing

import android.util.SparseArray
import wannabe.Camera
import wannabe.Translation
import wannabe.UI
import wannabe.Voxel
import wannabe.XYBounds
import wannabe.grid.Grid
import wannabe.projection.Cabinet
import wannabe.projection.Projection
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetSides
import wannabe.swing.renderer.SwingRenderer
import wannabe.util.UIs.pxToCells
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JPanel
import kotlin.math.min

/**
 * Swing painting code to display a Wannabe [Grid]. Treats [Voxel.getValue] as an ARGB
 * value.  If the alpha component is 0x00, it is treated as 0xFF (100% opaque).
 * Camera is fixed to the center of the widget.
 */
class WannabePanel(
  override var camera: Camera
) : JPanel(), UI {
  var realPixelSize = DEFAULT_PIXEL_SIZE

  // Our dimensions:
  var widthPx = 0
  var heightPx = 0
  var widthCells = 0
  var heightCells = 0

  // Center cell:
  var halfWidthCells = 0
  var halfHeightCells = 0
  private val colorCache = SparseArray<Color>(256)
  private val darkerCache = SparseArray<Color>(256)

  // Playfield paraphanellia:
  private lateinit var grid: Grid
  private val bounds = XYBounds()

  private var projection: Projection = Cabinet(-1, -1)
  var renderer: SwingRenderer = FilledThreeDSquareWithCabinetSides()
  private var stats = false
  private var exportHidden = false
  private var dirty = false
  private val lastCameraTranslation = Translation(0, 0, 0)
  private val swing = SwingProjected()

  init {
    isOpaque = true
    isFocusable = true
    addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        widthPx = width
        heightPx = height
        camera.uiPosition.left = widthPx shr 1
        camera.uiPosition.top = heightPx shr 1
        val shortest = min(widthPx, heightPx)
        val scale =
          shortest.toFloat() / PREFERRED_SIZE.height.toFloat()
        realPixelSize = (DEFAULT_PIXEL_SIZE * scale).toInt()
        if (realPixelSize < 1) realPixelSize = 1
        widthCells = pxToCells(widthPx, realPixelSize)
        heightCells = pxToCells(heightPx, realPixelSize)
        halfWidthCells = widthCells shr 1
        halfHeightCells = heightCells shr 1
      }
    })
  }

  override fun setGrid(grid: Grid) {
    dirty = true
    this.grid = grid
  }

  override fun setProjection(projection: Projection) {
    this.projection = projection
  }

  fun getProjection(): Projection {
    return projection
  }

  /** Shows advanced stats on the display.  */
  fun showStats() {
    stats = true
  }

  /** Updates timer, requests refresh.  */
  override fun render() {
    repaint()
  }

  override fun getPreferredSize(): Dimension {
    return PREFERRED_SIZE
  }

  fun exportHidden(exportHidden: Boolean) {
    dirty = true
    this.exportHidden = exportHidden
  }

  override fun paintBorder(g: Graphics) {}
  override fun paintChildren(g: Graphics) {}
  public override fun paintComponent(g: Graphics) {
    val start = System.currentTimeMillis()
    bounds.setFromWidthHeight(
        camera.position.x() - halfWidthCells,  //
        camera.position.y() - halfHeightCells,  //
        widthCells, heightCells
    )
    if (lastCameraTranslation != camera.position) {
      dirty = true
    }
    val afterGrid = System.currentTimeMillis()

    // Background:
    g.color = Color.BLACK
    g.fillRect(0, 0, widthPx, heightPx)
    for (voxel in grid) {
      val p = projection.project(camera, voxel.position, realPixelSize)
      // If it's going to be fully off-screen, don't bother drawing.
      if (p.left < -realPixelSize || p.left > widthPx //
          || p.top < -realPixelSize || p.top > heightPx
      ) {
        continue
      }
      // If it's too small don't draw:
      // TODO probably should put a specific bounds on when PsPerspective used, but anyway...
      // TODO and of course this affects pixel-sized rendering if we want to try that
      if (p.size <= 1) continue
      swing.copyCoreFrom(p)
      swing.neighborsFrom(grid.neighbors(voxel))
      swing.color = getSwingColor(voxel.value)
      swing.darkerColor = getDarkerColor(voxel.value)

      // TODO it seems a bit weird that that this method controls the context value
      g.color = swing.color
      renderer.draw(g, swing)
    }

    // Timing info:
    val total = System.currentTimeMillis() - start
    if (total > 100) {
      val gridTime = afterGrid - start
      println(
          String.format(
              "render took %s; %s was grid, %s was render", total,
              gridTime, total - gridTime
          )
      )
    }
    if (stats) {
      val gridTime = afterGrid - start
      val statistics = ("voxels on screen: " + grid.size + "; time: grid "
          + gridTime + " render " + (total - gridTime))
      // Make a small shadow to help stand out:
      g.color = Color.BLACK
      g.drawString(statistics, 20, 20)
      g.color = Color.WHITE
      g.drawString(statistics, 19, 19)
    }
    lastCameraTranslation.set(camera.position)
  }

  private fun getSwingColor(argb: Int): Color {
    var color = colorCache[argb]
    if (color == null) {
      val hasAlpha = argb shr 24 > 0
      color = Color(argb, hasAlpha)
      colorCache.put(argb, color)
    }
    return color
  }

  private fun getDarkerColor(argb: Int): Color? {
    var color = darkerCache[argb]
    if (color == null) {
      color = getSwingColor(argb).darker()
      darkerCache.put(argb, color)
    }
    return color
  }

  companion object {
    const val DEFAULT_PIXEL_SIZE = 20
    private const val MIN_PLAYFIELD_HEIGHT = 50
    private const val MIN_PLAYFIELD_WIDTH = 50
    private val PREFERRED_SIZE = Dimension(
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT
    )
  }
}
