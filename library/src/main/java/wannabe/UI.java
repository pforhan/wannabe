// Copyright 2013 Patrick Forhan.
package wannabe;

import wannabe.grid.Grid;
import wannabe.projection.Projection;

/** Implementors know how to render a {@link Grid} to a device. */
public interface UI {
  void setSize(int width, int height);
  void addGrid(Grid grid);
  void removeGrid(Grid grid);
  void setCamera(Camera camera);
  void setProjection(Projection projection);
  /** Called when the clock advances and the UI should be rendered. */
  void render();
}
