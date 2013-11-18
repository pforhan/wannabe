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
  private int pixelSize;

  @Override public Rendered render(Camera camera, Position position) {
    // Determine distance from camera:
    int xDiff = camera.position.x - position.x;
    int yDiff = camera.position.y - position.y;

    // Get the rough location to draw
    Position onScreen = camera.translate(position);
    rendered.left = pixelSize * position.x - position.z;
    rendered.top = pixelSize * position.y - position.z;
    rendered.size = pixelSize;

    return rendered;
  }

  @Override public void setPixelSize(int size) {
    this.pixelSize = size;
  }
}
