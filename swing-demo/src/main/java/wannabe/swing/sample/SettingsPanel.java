package wannabe.swing.sample;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import wannabe.grid.Grid;
import wannabe.projection.Projection;
import wannabe.projection.Projections;
import wannabe.swing.renderer.Circle;
import wannabe.swing.renderer.FilledCircle;
import wannabe.swing.renderer.FilledRoundedSquare;
import wannabe.swing.renderer.FilledSquare;
import wannabe.swing.renderer.FilledSquareWithCabinetSides;
import wannabe.swing.renderer.FilledSquareWithCabinetWires;
import wannabe.swing.renderer.FilledThreeDSquare;
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetSides;
import wannabe.swing.renderer.FilledThreeDSquareWithCabinetWires;
import wannabe.swing.renderer.Pixel;
import wannabe.swing.renderer.RoundedSquare;
import wannabe.swing.renderer.SolidWireCube;
import wannabe.swing.renderer.Square;
import wannabe.swing.renderer.SquareWithWireSides;
import wannabe.swing.renderer.SwingRenderer;
import wannabe.swing.renderer.ThreeDSquare;

public class SettingsPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private final JList<Projections> projectionView;
  private final JList<SwingRenderer> renderer;
  private final JList<Grid> grid;
  private Listener listener;
  private boolean reacting;
  private final List<SwingRenderer> renderers;

  public SettingsPanel() {
    super(new GridLayout(4,1));
    setBackground(Color.WHITE);
    setBorder(new EmptyBorder(5, 5, 5, 5));
    renderers = createRenders();

    // Set up components:
    JTextArea help = new JTextArea("Arrow keys - move"
        + "\nSpace - toggle move camera / player"
        + "\nZ - lower camera"
        + "\nX - raise camera"
        + "\nB - change buffer impl"
        + "\nG - change Grid"
        + "\nA - toggle player grid"
        + "\n/ - Make grid rotatable"
        + "\nR - change renderer"
        + "\nP - change projectionView"
        + "\nE - toggle export hidden"
        + "\n1 - set pixel size to 1"
        + "\n2 - set pixel size to 2"
        + "\n` - set pixel size to default"
        );
    help.setFocusable(false);
    help.setEditable(false);
    help.setBorder(new TitledBorder("Help"));

    projectionView = new JList<>(createProjectionsModel());
    projectionView.setFocusable(false);
    projectionView.setBorder(new TitledBorder("Projection"));
    renderer = new JList<>(renderers.toArray(new SwingRenderer[0]));
    renderer.setFocusable(false);
    renderer.setBorder(new TitledBorder("Render Type"));
    grid = new JList<>(createGridModel());
    grid.setFocusable(false);
    grid.setBorder(new TitledBorder("Available Grids"));

    // Listen for list changes and fire events accordingly.
    projectionView.addListSelectionListener(ignored -> {
      if (!reacting && listener != null) {
        listener.onProjectionChanged(
            Projections.values()[projectionView.getSelectedIndex()].projection);
      }
    });
    renderer.addListSelectionListener(ignored -> {
      if (!reacting && listener != null) {
        listener.onRendererChanged(renderers.get(renderer.getSelectedIndex()));
      }
    });
    grid.addListSelectionListener(ignored -> {
      if (!reacting && listener != null) {
        listener.onGridChanged(SwingGrids.GRIDS.get(grid.getSelectedIndex()));
      }
    });

    // Add all components together:
    add(new JScrollPane(help));
    add(new JScrollPane(projectionView));
    add(new JScrollPane(renderer));
    add(new JScrollPane(grid));
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  private ListModel<Grid> createGridModel() {
    DefaultListModel<Grid> model = new DefaultListModel<>();
    for (Grid grid : SwingGrids.GRIDS) {
      model.addElement(grid);
    }
    return model;
  }

  private List<SwingRenderer> createRenders() {
    List<SwingRenderer> list = new ArrayList<>();
    list.add(new FilledThreeDSquareWithCabinetSides());
    list.add(new FilledThreeDSquareWithCabinetWires());
    list.add(new FilledSquareWithCabinetSides());
    list.add(new FilledSquareWithCabinetWires());
    list.add(new SquareWithWireSides());
    list.add(new SolidWireCube(Color.BLACK));
    list.add(new Square());
    list.add(new FilledSquare());
    list.add(new ThreeDSquare());
    list.add(new FilledThreeDSquare());
    list.add(new RoundedSquare());
    list.add(new FilledRoundedSquare());
    list.add(new Circle());
    list.add(new FilledCircle());
    list.add(new Pixel());
    return list;
  }

  private static ListModel<Projections> createProjectionsModel() {
    DefaultListModel<Projections> model = new DefaultListModel<>();
    Projections[] allProjections = Projections.values();
    for (Projections projections : allProjections) {
      model.addElement(projections);
    }
    return model;
  }

  public interface Listener {
    void onRendererChanged(SwingRenderer newType);
    void onProjectionChanged(Projection newProjection);
    void onGridChanged(Grid newGrid);
  }

  public void gridSelected(Grid grid) {
    reacting = true;
    this.grid.setSelectedValue(grid, true);
    reacting = false;
  }

  public void projectionsSelected(Projections projections) {
    reacting = true;
    this.projectionView.setSelectedValue(projections, true);
    reacting = false;
  }

  public void renderTypeSelected(SwingRenderer sidedRenderer) {
    reacting = true;
    this.renderer.setSelectedValue(sidedRenderer, true);
    reacting = false;
  }

  public SwingRenderer nextRenderer() {
    int index = renderer.getSelectedIndex();
    index++;
    if (index >= renderers.size()) {
      index = 0;
    }
    return renderers.get(index);
  }
}
