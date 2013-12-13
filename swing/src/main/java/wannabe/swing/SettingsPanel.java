// Copyright 2013 Patrick Forhan.
package wannabe.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import wannabe.grid.Grid;
import wannabe.projection.Projection;
import wannabe.projection.Projections;
import wannabe.swing.WannabePanel.RenderType;
import wannabe.util.SampleGrids;

public class SettingsPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JList projection;
  private JList renderType;
  private JList grid;
  private Listener listener;
  private boolean reacting;

  public SettingsPanel() {
    super(new GridLayout(4,1));
    setBackground(Color.WHITE);
    setBorder(new EmptyBorder(5, 5, 5, 5));

    // Set up components:
    JTextArea help = new JTextArea("Arrow keys - move camera\n"
        + "Z - zoom in (move camera Z)\n"
        + "X - zoom out (move camera Z)\n"
        + "G - change SimpleGrid\n"
        + "R - change renderer\n"
        + "P - change projection\n"
        );
    help.setFocusable(false);
    help.setEditable(false);
    help.setBorder(new TitledBorder("Help"));

    projection = new JList(createProjectionModel());
    projection.setFocusable(false);
    projection.setCellRenderer(new ClassNameRenderer());
    projection.setBorder(new TitledBorder("Projection"));
    renderType = new JList(createRenderTypeModel());
    renderType.setFocusable(false);
    renderType.setBorder(new TitledBorder("Render Type"));
    grid = new JList(createGridModel());
    grid.setFocusable(false);
    grid.setBorder(new TitledBorder("Available Grids"));

    // Listen for list changes and fire events accordingly.
    projection.addListSelectionListener(new ListSelectionListener() {
      @Override public void valueChanged(ListSelectionEvent e) {
        if (!reacting && listener != null) {
          listener.onProjectionChanged(Projections.PROJECTIONS.get(projection.getSelectedIndex()));
        }
      }
    });
    renderType.addListSelectionListener(new ListSelectionListener() {
      @Override public void valueChanged(ListSelectionEvent e) {
        if (!reacting && listener != null) {
          listener.onRenderTypeChanged(RenderType.values()[renderType.getSelectedIndex()]);
        }
      }
    });
    grid.addListSelectionListener(new ListSelectionListener() {
      @Override public void valueChanged(ListSelectionEvent e) {
        if (!reacting && listener != null) {
          listener.onGridChanged(SampleGrids.GRIDS.get(grid.getSelectedIndex()));
        }
      }
    });

    // Add all components together:
    add(help);
    add(projection);
    add(renderType);
    add(grid);
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  private ListModel createGridModel() {
    DefaultListModel model = new DefaultListModel();
    for (Grid grid : SampleGrids.GRIDS) {
      model.addElement(grid);
    }
    return model;
  }

  private ListModel createRenderTypeModel() {
    DefaultListModel model = new DefaultListModel();
    for (RenderType render : RenderType.values()) {
      model.addElement(render);
    }
    return model;
  }

  protected ListModel createProjectionModel() {
    DefaultListModel model = new DefaultListModel();
    for (Projection projection : Projections.PROJECTIONS) {
      model.addElement(projection);
    }
    return model;
  }

  public interface Listener {
    void onRenderTypeChanged(RenderType newType);
    void onProjectionChanged(Projection newProjection);
    void onGridChanged(Grid newGrid);
  }

  private static class ClassNameRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      return super.getListCellRendererComponent(list, value.getClass().getSimpleName(), index,
          isSelected, cellHasFocus);
    }
  }

  public void gridSelected(Grid grid) {
    reacting = true;
    this.grid.setSelectedValue(grid, true);
    reacting = false;
  }

  public void projectionSelected(Projection projection) {
    reacting = true;
    this.projection.setSelectedValue(projection, true);
    reacting = false;
  }

  public void renderTypeSelected(RenderType renderType) {
    reacting = true;
    this.renderType.setSelectedValue(renderType, true);
    reacting = false;
  }
}
