package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Projected;

/** Converts a 3d Position to a 2d coordinate for rendering. */
public interface Projection {
  /**
   * Resolves a {@link Position} to a two-dimensional location relative to the specified
   * {@link Camera} with a height-0 size of {@code pixelSize}.  Projections should honor
   * {@link Camera#uiPosition} to allow the UI to render voxels appropriately.
   * Implementations may reuse the returned instance for performance.
   */
  Projected project(Camera camera, Position position, int pixelSize);
}
