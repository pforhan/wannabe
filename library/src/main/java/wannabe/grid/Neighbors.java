package wannabe.grid;

import wannabe.Voxel;

/** References to neighboring voxels. */
public class Neighbors {
  public Voxel north;
  public Voxel south;
  public Voxel east;
  public Voxel west;
  public Voxel above;
  public Voxel below;

  /** Returns {@code true} if the voxel could be visible. */
  public boolean isNotSurrounded() {
    return above == null || north == null || south == null || east == null || west == null;
  }
}
