// Copyright 2013 Square, Inc.
package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a constant center point, but with an differing a size to represent
 * height.
 */
public class Flat implements Projection {
  private final Rendered rendered = new Rendered();

  @Override public Rendered render(Camera camera, Position position, int pixelSize) {
    position = camera.translate(position);
    // TODO tweak, not sure what a good value is here:
    int zOffset = position.z / 4;
    rendered.left = pixelSize * position.x - zOffset + camera.uiPosition.left;
    rendered.top = pixelSize * position.y - zOffset + camera.uiPosition.top;
    rendered.size = pixelSize + zOffset << 1;

    return rendered;
  }
}
