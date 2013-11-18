// Copyright 2013 Square, Inc.
package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;

/** Converts a 3d Position to a 2d coordinate for rendering. */
public interface Projection {
  /** Sets the size of a pixel at height 0. */
  void setPixelSize(int size);

  /**
   * Resolves a {@link Position} to a two-dimensional location relative to the specified
   * {@link Camera}.
   * Implementations may reuse this instance for performance.
   */
  Rendered render(Camera camera, Position position);
}
