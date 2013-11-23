// Copyright 2013 Square, Inc.
package wannabe;

import wannabe.grid.Grid;

import wannabe.projection.Projection;

public interface UI {
  void setSize(int width, int height);
  void setGrid(Grid grid);
  void setCamera(Camera camera);
  Camera getCamera();
  void setProjection(Projection projection);
  /** Called when the clock advances and the UI should be rendered. */
  void render();
}
