// Copyright 2013 Square, Inc.
package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a constant size, but with an offset to represent height.
 * Larger heights are placed up and to the left, lower values are down and to the right.
 */
public class Isometric implements Projection {
  private final Rendered rendered = new Rendered();
  private int pixelSize;


  @Override public Rendered render(Camera camera, Position position) {
    position = camera.translate(position);
    rendered.left = pixelSize * position.x - position.z;
    rendered.top = pixelSize * position.y - position.z;
    rendered.size = pixelSize;

    return rendered;
  }

  @Override public void setPixelSize(int size) {
    this.pixelSize = size;
  }
}
