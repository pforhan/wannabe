package wannabe.projection;

import wannabe.Camera;
import wannabe.Position;
import wannabe.Rendered;
import wannabe.Translation;
import wannabe.Voxel;

/**
 * Renders {@link Voxel}s with a constant size, but with an offset to represent height.
 */
public class Cabinet implements Projection {
  private final Rendered rendered = new Rendered();
  /** Horizontal offset to use for each unit of Z. */
  final int hOffsetPerZ;
  /** Vertical offset to use for each unit of Z. */
  final int vOffsetPerZ;

  /** Default constructor just has increasing Z values go up and left. */
  public Cabinet() {
    this(-1, -1);
  }

  public Cabinet(int hOffsetPerZ, int vOffsetPerZ) {
    this.hOffsetPerZ = hOffsetPerZ;
    this.vOffsetPerZ = vOffsetPerZ;
  }

  @Override public Rendered render(Camera camera, Position position, int pixelSize) {
    Translation address = camera.translate(position);
    int horizOffset = hOffsetPerZ * address.z + pixelSize * address.x;
    int vertOffset = vOffsetPerZ * address.z + pixelSize * address.y;
    rendered.left = camera.uiPosition.left + horizOffset;
    rendered.top = camera.uiPosition.top + vertOffset;
    rendered.size = pixelSize;
    rendered.hDepth = -hOffsetPerZ;
    rendered.vDepth = -vOffsetPerZ;

    return rendered;
  }

  @Override public String toString() {
    return getClass().getSimpleName() + " " + hOffsetPerZ + ", " + vOffsetPerZ;
  }
}
