// Copyright 2016 Patrick Forhan
package wannabe.grid;

import wannabe.Voxel;

/** References to neighboring voxels. */
public class Neighbors {
  public final Voxel voxel;
  public Voxel north;
  public Voxel south;
  public Voxel east;
  public Voxel west;
  public Voxel above;
  public Voxel below;

  public Neighbors(Voxel voxel) {
    this.voxel = voxel;
  }

  /** Returns {@code true} if the voxel could be visible. */
  public boolean isNotSurrounded() {
    return above == null || north == null || south == null || east == null || west == null;
  }
}
