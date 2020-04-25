package wannabe.swing.sample

import wannabe.grid.Grid
import wannabe.projection.Projection
import wannabe.projection.Projections
import wannabe.swing.renderer.Circle
import wannabe.swing.renderer.FilledCircle
import wannabe.swing.renderer.FilledRoundedSquare
import wannabe.swing.renderer.FilledSquare
import wannabe.swing.renderer.FilledSquareWithCabinetSides
import wannabe.swing.renderer.FilledSquareWithCabinetWires
import wannabe.swing.renderer.FilledThreeDSquare
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetSides
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetWires
import wannabe.swing.renderer.Pixel
import wannabe.swing.renderer.RoundedSquare
import wannabe.swing.renderer.SolidWireCube
import wannabe.swing.renderer.Square
import wannabe.swing.renderer.SquareWithWireSides
import wannabe.swing.renderer.SwingRenderer
import wannabe.swing.renderer.ThreeDSquare
import java.awt.Color
import java.awt.GridLayout
import java.util.ArrayList
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.ListModel
import javax.swing.border.EmptyBorder
import javax.swing.border.TitledBorder
import javax.swing.event.ListSelectionEvent

internal class SettingsPanel : JPanel(GridLayout(4, 1)) {
  private val projectionView: JList<Projections>
  private val renderer: JList<SwingRenderer>
  private val grid: JList<Grid>
  private var listener: Listener? = null
  private var reacting = false
  private val renderers: List<SwingRenderer>
  fun setListener(listener: Listener?) {
    this.listener = listener
  }

  private fun createGridModel(): ListModel<Grid> {
    val model = DefaultListModel<Grid>()
    for (grid in SwingGrids.GRIDS) {
      model.addElement(grid)
    }
    return model
  }

  private fun createRenders(): List<SwingRenderer> {
    val list: MutableList<SwingRenderer> = ArrayList()
    list.add(FilledThreeDSquareWithCabinetSides())
    list.add(FilledThreeDSquareWithCabinetWires())
    list.add(FilledSquareWithCabinetSides())
    list.add(FilledSquareWithCabinetWires())
    list.add(SquareWithWireSides())
    list.add(SolidWireCube(Color.BLACK))
    list.add(Square())
    list.add(FilledSquare())
    list.add(ThreeDSquare())
    list.add(FilledThreeDSquare())
    list.add(RoundedSquare())
    list.add(FilledRoundedSquare())
    list.add(Circle())
    list.add(FilledCircle())
    list.add(Pixel())
    return list
  }

  interface Listener {
    fun onRendererChanged(newType: SwingRenderer?)
    fun onProjectionChanged(newProjection: Projection?)
    fun onGridChanged(newGrid: Grid?)
  }

  fun gridSelected(grid: Grid?) {
    reacting = true
    this.grid.setSelectedValue(grid, true)
    reacting = false
  }

  fun projectionsSelected(projections: Projections?) {
    reacting = true
    projectionView.setSelectedValue(projections, true)
    reacting = false
  }

  fun renderTypeSelected(sidedRenderer: SwingRenderer?) {
    reacting = true
    renderer.setSelectedValue(sidedRenderer, true)
    reacting = false
  }

  fun nextRenderer(): SwingRenderer {
    var index = renderer.selectedIndex
    index++
    if (index >= renderers.size) {
      index = 0
    }
    return renderers[index]
  }

  companion object {
    private const val serialVersionUID = 1L
    private fun createProjectionsModel(): ListModel<Projections> {
      val model = DefaultListModel<Projections>()
      val allProjections = Projections.values()
      for (projections in allProjections) {
        model.addElement(projections)
      }
      return model
    }
  }

  init {
    background = Color.WHITE
    border = EmptyBorder(5, 5, 5, 5)
    renderers = createRenders()

    // Set up components:
    val help = JTextArea(
        """
          Arrow keys - move
          Space - toggle move camera / player
          Z - lower camera
          X - raise camera
          B - change buffer impl
          G - change Grid
          A - toggle player grid
          / - reset rotation to 0
          R - change renderer
          P - change projectionView
          E - toggle export hidden
          1 - set pixel size to 1
          2 - set pixel size to 2
          ` - set pixel size to default
          """.trimIndent()
    )
    help.isFocusable = false
    help.isEditable = false
    help.border = TitledBorder("Help")
    projectionView = JList(createProjectionsModel())
    projectionView.isFocusable = false
    projectionView.border = TitledBorder("Projection")
    renderer = JList(renderers.toTypedArray())
    renderer.isFocusable = false
    renderer.border = TitledBorder("Render Type")
    grid = JList(createGridModel())
    grid.isFocusable = false
    grid.border = TitledBorder("Available Grids")

    // Listen for list changes and fire events accordingly.
    projectionView.addListSelectionListener { ignored: ListSelectionEvent? ->
      if (!reacting && listener != null) {
        listener!!.onProjectionChanged(
            Projections.values()[projectionView.selectedIndex].projection
        )
      }
    }
    renderer.addListSelectionListener { ignored: ListSelectionEvent? ->
      if (!reacting && listener != null) {
        listener!!.onRendererChanged(renderers[renderer.selectedIndex])
      }
    }
    grid.addListSelectionListener { ignored: ListSelectionEvent? ->
      if (!reacting && listener != null) {
        listener!!.onGridChanged(SwingGrids.GRIDS[grid.selectedIndex])
      }
    }

    // Add all components together:
    add(JScrollPane(help))
    add(JScrollPane(projectionView))
    add(JScrollPane(renderer))
    add(JScrollPane(grid))
  }
}
