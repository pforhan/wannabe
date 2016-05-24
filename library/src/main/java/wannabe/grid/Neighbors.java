package wannabe.grid;

import wannabe.Voxel;

/** References to neighboring voxels. */
public class Neighbors {
  public final Voxel voxel;

  public Neighbors north;
  public Neighbors south;
  public Neighbors east;
  public Neighbors west;
  public Neighbors above;
  public Neighbors below;

  public Neighbors(Voxel v) {
    voxel = v;
  }

  /** Returns {@code true} if the voxel could be visible. Ignores "below" in this calculation. */
  public boolean isNotSurrounded() {
    return above == null || north == null || south == null || east == null || west == null;
  }

  public void neighborAbove(Neighbors other) {
    above = other;
    if (other != null) {
      other.below = this;
    }
  }

  public void neighborBelow(Neighbors other) {
    below = other;
    if (other != null) {
      other.above = this;
    }
  }

  public void neighborNorth(Neighbors other) {
    north = other;
    if (other != null) {
      other.south = this;
    }
  }

  public void neighborSouth(Neighbors other) {
    south = other;
    if (other != null) {
      other.north = this;
    }
  }

  public void neighborEast(Neighbors other) {
    east = other;
    if (other != null) {
      other.west = this;
    }
  }

  public void neighborWest(Neighbors other) {
    west = other;
    if (other != null) {
      other.east = this;
    }
  }
}
