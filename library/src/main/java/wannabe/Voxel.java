package wannabe;

public class Voxel {
  public final Position position;
  public int color;

  public Voxel(int x, int y, int z, int color) {
    this(new Position(x, y, z), color);
  }

  public Voxel(Position position, int color) {
    this.position = position;
    this.color = color;
  }
}
