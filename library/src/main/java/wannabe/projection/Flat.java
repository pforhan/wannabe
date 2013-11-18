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
  private int pixelSize;

  @Override public Rendered render(Camera camera, Position position) {
    position = camera.translate(position);
    rendered.left = pixelSize * position.x - position.z;
    rendered.top = pixelSize * position.y - position.z;
    rendered.size = pixelSize + position.z + position.z;

    return rendered;
  }

  @Override public void setPixelSize(int size) {
    this.pixelSize = size;
  }
}
