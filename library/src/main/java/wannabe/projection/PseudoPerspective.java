package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Projected;
import wannabe.Translation;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a simple perspective shift for height. The farther from the camera,
 * and the higher the Z value, the greater the shift. Higher Z values also get a larger size. A
 * Voxel at the same x and y position as the camera would only get a shift due to its height.
 *
 * This class doesn't calculate a real perspective offset. Instead, it adds a simple x and/or y
 * pixel offset for increasing distance from the camera. All voxels at the same Z height have the
 * same size.
 * TODO change that last bit; as x or y offset increase, z should decrease a bit.
 * TODO xDiff and yDiff are too drastic.
 */
public class PseudoPerspective implements Projection {
  private final Projected projected = new Projected();

  @Override public Projected project(Camera camera, Position position, int pixelSize) {
    // Determine distance from camera:
    int xDiff = camera.position.x - position.x;
    int yDiff = camera.position.y - position.y;
    int zDiff = camera.position.z - position.z;

    // Get the rough location to draw from:
    Translation onScreen = camera.translate(position);
    projected.size = pixelSize - zDiff;
    int halfSize = projected.size >> 1;
    projected.left = pixelSize * onScreen.x - onScreen.z * xDiff + camera.uiPosition.left - halfSize;
    projected.top = pixelSize * onScreen.y - onScreen.z * yDiff + camera.uiPosition.top - halfSize;
    // TODO come up with better depth calcs... though this works decently
    projected.hDepth = xDiff;
    projected.vDepth = yDiff;

    return projected;
  }

  @Override public String toString() {
    return getClass().getSimpleName();
  }
}
