// Copyright 2013 Square, Inc.
package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a simple perspective shift for height. The farther from the camera,
 * and the higher the Z value, the greater the shift. Higher Z values also get a larger size. A
 * Voxel at the same x and y position as the camera would only get a shift due to its height.
 *
 * This class doesn't calculate a real perspective offset. Instead, it adds a simple x and/or y
 * pixel offset for increasing distance from the camera. All voxels at the same Z height have the
 * same size.
 */
public class PseudoPerspective implements Projection {
  private final Rendered rendered = new Rendered();

  @Override public Rendered render(Camera camera, Position position, int pixelSize) {
    // Determine distance from camera: TODO this is why camera may need to start in the middle of the screen
    int xDiff = camera.position.x - position.x;
    int yDiff = camera.position.y - position.y;
    int zDiff = camera.position.z - position.z;

    // Get the rough location to draw from:
    Position onScreen = camera.translate(position);
    rendered.size = pixelSize - zDiff;
    int halfSize = rendered.size >> 1; // TODO it's a shame I have to calculate this on every voxel
    rendered.left = pixelSize * onScreen.x - onScreen.z * xDiff + camera.uiPosition.left - halfSize;
    rendered.top = pixelSize * onScreen.y - onScreen.z * yDiff + camera.uiPosition.top - halfSize;

    return rendered;
  }
}
