package wannabe;

import java.awt.Color;
import wannabe.grid.AllNeighbors;

import static wannabe.grid.AllNeighbors.RelativePosition.CENTER;
import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTHEAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTHWEST;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTHEAST;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTHWEST;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

/** Describes real pixel color, location, and size for a voxel. */
// TODO do we want java.awt.Color here?
// TODO should I just put the Voxel in here?
public class Projected {
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

  public void neighborsFrom(AllNeighbors neighbors) {
    neighborNorth     = neighbors.same.get(NORTH);
    neighborNorthEast = neighbors.same.get(NORTHEAST);
    neighborEast      = neighbors.same.get(EAST);
    neighborSouthEast = neighbors.same.get(SOUTHEAST);
    neighborSouth     = neighbors.same.get(SOUTH);
    neighborSouthWest = neighbors.same.get(SOUTHWEST);
    neighborWest      = neighbors.same.get(WEST);
    neighborNorthWest = neighbors.same.get(NORTHWEST);
    neighborAbove = neighbors.above.get(CENTER);
    neighborBelow = neighbors.below.get(CENTER);
  }
}