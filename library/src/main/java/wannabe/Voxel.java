// Copyright 2013 Square, Inc.
package wannabe;

public class Voxel {
  public final Position position;
  public int color;

  public Voxel(int x, int y, int z, int color) {
    position = new Position(x, y, z);
    this.color = color;
  }
}
