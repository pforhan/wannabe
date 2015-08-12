// Copyright 2013 Patrick Forhan.
package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a constant size, but with an offset to represent height.
 * Larger heights are placed up and to the left, lower values are down and to the right.
 */
public class Cabinet implements Projection {
  private final Rendered rendered = new Rendered();

  @Override public Rendered render(Camera camera, Position position, int pixelSize) {
    position = camera.translate(position);
    rendered.left = pixelSize * position.x - position.z + camera.uiPosition.left;
    rendered.top = pixelSize * position.y - position.z + camera.uiPosition.top;
    rendered.size = pixelSize;

    return rendered;
  }
}
