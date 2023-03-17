package wannabe.swing

import wannabe.Camera
import wannabe.Position
import wannabe.Translation
import wannabe.UI
import wannabe.Voxel
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
  private val colorCache = mutableMapOf<Int, Color>()
  private val darkerCache = mutableMapOf<Int, Color>()

  // Playfield paraphanellia:
  private lateinit var grid: Grid

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

  /** 
   * Reverse lookup a Position from a pixel location. This location should be
   * relative to this widget.
   */
  // TODO this probably needs to consult the projection, and take a z value
  // so that it can work with any height.
  // For now, assume 0 z dimension so that realPixelSize matches up.
  fun positionAtPixel(x:Int, y: Int) = camera.reverseTranslate(Position(
      x = x.toVoxels(),
      y = y.toVoxels(),
      z = 0
    )
  ).asPosition()

  private fun Int.toVoxels(): Int = pxToCells(this, realPixelSize)

  override fun paintBorder(g: Graphics) {}
  override fun paintChildren(g: Graphics) {}
  public override fun paintComponent(g: Graphics) {
    val start = System.currentTimeMillis()
    if (lastCameraTranslation != camera.position) {
      dirty = true
    }

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
      println("render took $total")
    }
    if (stats) {
      val statistics = "voxels on screen: ${grid.size}; time: $total"
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
      colorCache[argb] = color
    }
    return color
  }

  private fun getDarkerColor(argb: Int): Color? {
    var color = darkerCache[argb]
    if (color == null) {
      color = getSwingColor(argb).darker()
      darkerCache[argb] = color
    }
    return color
  }

  companion object {
    const val DEFAULT_PIXEL_SIZE = 30
    private const val MIN_PLAYFIELD_HEIGHT = 50
    private const val MIN_PLAYFIELD_WIDTH = 50
    private val PREFERRED_SIZE = Dimension(
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT
    )
  }
}
