// Copyright 2013 Patrick Forhan.
package wannabe;

import java.awt.Color;
import wannabe.grid.Neighbors;

/** Describes real pixel color, location, and size for a voxel. */
// TODO do we want java.awt.Color here?
public class Rendered implements Cloneable {
  public int left;
  public int top;
  public int size;
  /** Horizontal space to use to indicate height. */
  public int hDepth;
  /** Vertical space to use to indicate height. */
  public int vDepth;
  public Color color;
  public Color darkerColor;

  // Hints about whether there are neighboring voxels nearby.

  /** A voxel is present at x, y-1, z */
  public boolean neighborNorth;
  /** A voxel is present at x, y+1, z */
  public boolean neighborSouth;
  /** A voxel is present at x-1, y, z */
  public boolean neighborWest;
  /** A voxel is present at x+1, y, z */
  public boolean neighborEast;
  /** A voxel is present at x, y, z+1 */
  public boolean neighborAbove;

  // TODO do I care about below? Seems like it'd be nice to know if only as a reflexive of above.
  /** A voxel is present at x, y, z-1 */
  public boolean neighborBelow;

  /** Copies all values except color fields into this object. */
  public void duplicateWithoutColor(Rendered other) {
    left = other.left;
    top = other.top;
    size = other.size;
    hDepth = other.hDepth;
    vDepth = other.vDepth;
    neighborNorth = other.neighborNorth;
    neighborSouth = other.neighborSouth;
    neighborWest = other.neighborWest;
    neighborEast = other.neighborEast;
    neighborAbove = other.neighborAbove;
    neighborBelow = other.neighborBelow;
  }

  /** Returns {@code true} if this voxel has neighbor on north, south, east, west, and above. */
  public boolean isSurrounded() {
    return neighborNorth && neighborSouth && neighborWest && neighborEast && neighborAbove;
  }

  public void neighborsFrom(Neighbors neighbors) {
    neighborNorth = neighbors.north != null;
    neighborSouth = neighbors.south != null;
    neighborWest  = neighbors.west  != null;
    neighborEast  = neighbors.east  != null;
    neighborAbove = neighbors.above != null;
    neighborBelow = neighbors.below != null;
  }
}