package wannabe.grid.operations;

import wannabe.Voxel;

/** A unit of work to perform on a Voxel. May optionally perform filtering as well. */
public abstract class Operation {
  /** Whether this operation has changed since the last time it was used. */
  public boolean isDirty() {
    return false;
  }

  /** Whether the given Voxel should be included. */
  public boolean includes(Voxel voxel) {
    return true;
  }

  /** Performs an operation on a Voxel, returning a new Voxel instance. */
  public abstract Voxel apply(Voxel voxel);
}
