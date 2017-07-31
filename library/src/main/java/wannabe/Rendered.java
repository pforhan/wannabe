package wannabe;

import java.awt.Color;
import wannabe.grid.Neighbors;

/** Describes real pixel color, location, and size for a voxel. */
// TODO do we want java.awt.Color here?
// TODO should I just put the Voxel in here?
public class Rendered {
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
  /** A voxel is present at x+1, y-1, z */
  public boolean neighborNorthEast;
  /** A voxel is present at x-1, y-1, z */
  public boolean neighborNorthWest;
  /** A voxel is present at x, y+1, z */
  public boolean neighborSouth;
  /** A voxel is present at x+1, y+1, z */
  public boolean neighborSouthEast;
  /** A voxel is present at x-1, y+1, z */
  public boolean neighborSouthWest;
  /** A voxel is present at x-1, y, z */
  public boolean neighborWest;
  /** A voxel is present at x+1, y, z */
  public boolean neighborEast;
  /** A voxel is present at x, y, z+1 */
  public boolean neighborAbove;

  // TODO do I care about below? Seems like it'd be nice to know if only as a reflexive of above.
  /** A voxel is present at x, y, z-1 */
  public boolean neighborBelow;

  /** Returns {@code true} if this voxel has neighbor on north, south, east, west, and above. */
  public boolean isSurrounded() {
    return neighborNorth && neighborSouth && neighborWest && neighborEast && neighborAbove;
  }

  public void neighborsFrom(Neighbors neighbors) {
    // Since they are calculated only when a nondiagonal neighbor is present, we must
    // clear out all the diagonals:
    // TODO Turns out, we need this data! pure diagonal neighbors should honor each other.
    neighborNorthEast = false;
    neighborNorthWest = false;
    neighborSouthEast = false;
    neighborSouthWest = false;

    // Boy, this sort of seems awful.
    neighborNorth = neighbors.north != null;
    if (neighborNorth) {
      neighborNorthEast = neighbors.north.east != null;
      neighborNorthWest = neighbors.north.west != null;
    }

    neighborSouth = neighbors.south != null;
    if (neighborSouth) {
      neighborSouthEast = neighbors.south.east != null;
      neighborSouthWest = neighbors.south.west != null;
    }

    // Not sure if there's any way to avoid this duplication...
    neighborWest  = neighbors.west  != null;
    if (neighborWest) {
      neighborNorthWest = neighbors.west.north != null;
      neighborSouthWest = neighbors.west.south != null;
    }

    neighborEast  = neighbors.east  != null;
    if (neighborEast) {
      neighborNorthEast = neighbors.east.north != null;
      neighborSouthEast = neighbors.east.south != null;
    }

    neighborAbove = neighbors.above != null;
    neighborBelow = neighbors.below != null;
  }
}