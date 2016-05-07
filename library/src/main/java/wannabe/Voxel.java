package wannabe;

import wannabe.grid.Neighbors;

public class Voxel {
  public final Position position;
  public final Neighbors neighbors = new Neighbors();
  public int color;

  public Voxel(int x, int y, int z, int color) {
    this(new Position(x, y, z), color);
  }

  public Voxel(Position position, int color) {
    this.position = position;
    this.color = color;
  }

  // TODO I like the concept, but should these be in this class?
  public void neighborAbove(Voxel other) {
    neighbors.above = other;
    if (other != null) {
      other.neighbors.below = this;
    }
  }

  public void neighborBelow(Voxel other) {
    neighbors.below = other;
    if (other != null) {
      other.neighbors.above = this;
    }
  }

  public void neighborNorth(Voxel other) {
    neighbors.north = other;
    if (other != null) {
      other.neighbors.south = this;
    }
  }

  public void neighborSouth(Voxel other) {
    neighbors.south = other;
    if (other != null) {
      other.neighbors.north = this;
    }
  }

  public void neighborEast(Voxel other) {
    neighbors.east = other;
    if (other != null) {
      other.neighbors.west = this;
    }
  }

  public void neighborWest(Voxel other) {
    neighbors.west = other;
    if (other != null) {
      other.neighbors.east = this;
    }
  }
}
