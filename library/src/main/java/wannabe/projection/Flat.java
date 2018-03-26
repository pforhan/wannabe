package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Projected;
import wannabe.Translation;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a constant center point, but with an differing a size to represent
 * height.
 */
public class Flat implements Projection {
  private final Projected projected = new Projected();

  @Override public Projected project(Camera camera, Position position, int pixelSize) {
    Translation translation = camera.translate(position);
    int zOffset = position.z >> 2;
    projected.left = pixelSize * translation.x - zOffset + camera.uiPosition.left;
    projected.top = pixelSize * translation.y - zOffset + camera.uiPosition.top;
    projected.size = pixelSize + zOffset + zOffset;

    return projected;
  }

  @Override public String toString() {
    return getClass().getSimpleName();
  }
}
