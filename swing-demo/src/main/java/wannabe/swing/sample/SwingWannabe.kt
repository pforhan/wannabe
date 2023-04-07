package wannabe.swing.sample

import wannabe.Camera
import wannabe.Translation
import wannabe.Voxel
import wannabe.grid.CachingGrid
import wannabe.grid.Grid
import wannabe.grid.GroupGrid
import wannabe.grid.RotateGrid
import wannabe.grid.SingleGrid
import wannabe.grid.TranslateGrid
import wannabe.grid.VisibilityGrid
import wannabe.projection.Projection
import wannabe.projection.Projections
import wannabe.swing.WannabePanel
import wannabe.swing.renderer.SwingRenderer
import wannabe.swing.sample.SettingsPanel.Listener
import wannabe.swing.sample.SwingGrids.next
import wannabe.util.SampleGrids.megaManRunning
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Toolkit
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager

/** All the glue to make a sample swing Wannabe application.  */
object SwingWannabe {
  private val TRANSLATE_UP = Translation(0, -1, 0)
  private val TRANSLATE_DOWN = Translation(0, 1, 0)
  private val TRANSLATE_LEFT = Translation(-1, 0, 0)
  private val TRANSLATE_RIGHT = Translation(1, 0, 0)
  private val TRANSLATE_HIGHER = Translation(0, 0, 1)
  private val TRANSLATE_LOWER = Translation(0, 0, -1)
  private val allGrids = GroupGrid("Demo Group")

  /** Maps a grid to its visibility grid  */
  private val gridToVis: MutableMap<Grid, VisibilityGrid> = mutableMapOf()

  /** Maps a grid to its rotation grid  */
  private val gridToRot: MutableMap<Grid, RotateGrid> = mutableMapOf()

  private var currentGrid: Grid? = null
  private var exportHidden = false

  // Player grid stuff
  private val playerGridFrames = megaManRunning()
  private val playerGridTranslate =
    TranslateGrid("Translate Player", playerGridFrames)
  private val playerGridVisibility =
    VisibilityGrid("Player Visibility", playerGridTranslate)
  private var movingPlayer = false
  
  // Cursor pick stuff
  private val pickGrid = SingleGrid("Cursor")

  // Rotating stuff
  private var rotateGrid: RotateGrid? = null
  private var startDragX = 0
  private var startDragY = 0

  @Throws(InterruptedException::class) @JvmStatic
  fun main(args: Array<String>) {
    pickFonts()
    val frame = JFrame("SwingWannabe")
    frame.setLocation(20, 30)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val mainLayout = JPanel(BorderLayout())
    frame.contentPane = mainLayout
    val camera = Camera(18, 18, 0)
    val panel = WannabePanel(camera)
    panel.showStats()
    val settings = SettingsPanel()
    // TODO rather than have this class be the go-between of settings and WannabePanel, just
    // have settings change it directly. That way we shouldn't have to set things three times.
    // *Maybe* I could even move the key listener code to there, too, so i don't have to edit
    // two classes when bindings change
    settings.setListener(object : Listener {
      override fun onRendererChanged(newType: SwingRenderer) {
        panel.renderer = newType
      }

      override fun onProjectionChanged(newProjection: Projection) {
        panel.setProjection(newProjection)
      }

      override fun onGridChanged(newGrid: Grid) {
        moveToGrid(newGrid)
      }
    })
    mainLayout.add(panel, BorderLayout.CENTER)
    mainLayout.add(settings, BorderLayout.EAST)

    // Wrap each demo grid in a visibility and rotate control. Hide each.
    for (grid in SwingGrids.GRIDS) {
      val rot = RotateGrid("Rotate", grid)
      val vis = VisibilityGrid(
          "vis for $grid",
          CachingGrid("rot cache for $grid", rot)
      )
      vis.hide()
      allGrids.add(vis)
      gridToVis[grid] = vis
      gridToRot[grid] = rot
    }
    allGrids.add(playerGridVisibility)
    allGrids.add(pickGrid)
    playerGridVisibility.hide()
    // Show the first grid:
    currentGrid = SwingGrids.GRIDS[0]
    moveToGrid(currentGrid)
    panel.setGrid(allGrids)
    settings.gridSelected(currentGrid)
    panel.setProjection(Projections.values()[0].projection)
    settings.projectionsSelected(Projections.values()[0])
    settings.renderTypeSelected(panel.renderer)
    frame.size = mainLayout.preferredSize
    frame.isVisible = true
    panel.addKeyListener(object : KeyAdapter() {
      override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
          KeyEvent.VK_UP -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_UP)
          } else {
            camera.position.y--
          }
          KeyEvent.VK_DOWN -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_DOWN)
          } else {
            camera.position.y++
          }
          KeyEvent.VK_LEFT -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_LEFT)
          } else {
            camera.position.x--
          }
          KeyEvent.VK_RIGHT -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_RIGHT)
          } else {
            camera.position.x++
          }
          KeyEvent.VK_Z -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_LOWER)
          } else {
            camera.position.z--
          }
          KeyEvent.VK_X -> if (movingPlayer) {
            playerGridTranslate.translate(TRANSLATE_HIGHER)
          } else {
            camera.position.z++
          }
          KeyEvent.VK_SPACE -> movingPlayer = !movingPlayer
          KeyEvent.VK_A -> {
            playerGridVisibility.toggle()
            println("player state: $playerGridVisibility")
          }
          KeyEvent.VK_B -> {
          }
          KeyEvent.VK_G -> {
            moveToGrid(next(currentGrid!!))
            settings.gridSelected(currentGrid)
          }
          KeyEvent.VK_R -> {
            val nextRenderType = settings.nextRenderer()
            panel.renderer = nextRenderType
            settings.renderTypeSelected(nextRenderType)
          }
          KeyEvent.VK_P -> {
            val next = Projections.withProjection(panel.getProjection())
                .next()
            panel.setProjection(next.projection)
            settings.projectionsSelected(next)
          }
          KeyEvent.VK_E -> {
            exportHidden = !exportHidden
            panel.exportHidden(exportHidden)
          }
          KeyEvent.VK_1 -> panel.realPixelSize = 1
          KeyEvent.VK_2 -> panel.realPixelSize = 2
          KeyEvent.VK_BACK_QUOTE -> panel.realPixelSize = WannabePanel.DEFAULT_PIXEL_SIZE
          KeyEvent.VK_SLASH ->
            currentGrid?.let {
              gridToRot[it]!!.setRotate(0, 0, 0)
            }
        }
      }
    })
    panel.addMouseListener(object : MouseAdapter() {
      override fun mousePressed(e: MouseEvent) {
        startDragX = e.x
        startDragY = e.y
      }
    })
    panel.addMouseMotionListener(object : MouseMotionAdapter() {
      override fun mouseMoved(e: MouseEvent) {
        val pos = panel.positionAtPixel(e.x, e.y)
        // We effectively capture the picked voxel here, and then use it
        // below for the rotation offset.
        pickGrid.voxel = Voxel(pos, 0xFFFFFF)
      }
      override fun mouseDragged(e: MouseEvent) {
        // Odd ordering here is just because it looks best, since most content is down-right of origin
        rotateGrid!!.setRotate(
            e.y - startDragY, startDragX - e.x, 0, pickGrid.voxel.position
        )
      }
    })

    // noinspection InfiniteLoopStatement
    while (true) {
      playerGridFrames.nextFrame()
      panel.getProjection().tick()
      panel.render()
      Thread.sleep(100)
    }
  }

  private fun moveToGrid(newGrid: Grid?) {
    // TODO make this support multiselect
    hideGrid(currentGrid)
    currentGrid = newGrid
    rotateGrid = gridToRot[currentGrid]
    showGrid(currentGrid)
  }

  private fun showGrid(grid: Grid?) {
    gridToVis[grid]!!.show()
  }

  private fun hideGrid(grid: Grid?) {
    gridToVis[grid]!!.hide()
  }
}

fun pickFonts() {
  // Bump font size when the display manager isn't honoring UI scale.
  if (!System.getProperty("os.version", "").contains(other = "wsl", ignoreCase = true)) return

  val font = Font("Dialog", Font.PLAIN, 18)
  for (key in UIManager.getLookAndFeelDefaults().keys()) {
      if (key.toString().endsWith(".font")) {
          UIManager.put(key, font)
      }
  }
}
