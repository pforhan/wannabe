package wannabe.grid;

import java.util.BitSet;

import static wannabe.grid.AllNeighbors.RelativePosition.CENTER;
import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

/** References to neighboring voxels. */
public class AllNeighbors {
  /** Positions in a 3x3 grid. Not an enum to save us having to call the ordinal method. */
  public interface RelativePosition {
    static final int NORTH = 0;
    static final int NORTHEAST = 1;
    static final int EAST = 2;
    static final int SOUTHEAST = 3;
    static final int SOUTH = 4;
    static final int SOUTHWEST = 5;
    static final int WEST = 6;
    static final int NORTHWEST = 7;
    static final int CENTER = 8;
  }

  /** Whether there's a neighbor at z+1, indexed by RelativePosition.ordinal. */
  public final BitSet above = new BitSet(9);
  /** Whether there's a neighbor at z-1, indexed by RelativePosition.ordinal. */
  public final BitSet below = new BitSet(9);
  /** Whether there's a neighbor at the same z, indexed by RelativePosition.ordinal. */
  public final BitSet same = new BitSet(9);

  /** Resets all indicators to no neighbors. */
  public void clear() {
    above.clear();
    below.clear();
    same.clear();
  }

  /** Returns {@code true} if the voxel can't be visible. Ignores "below" in this calculation. */
  public boolean isSurrounded() {
    return above.get(CENTER)
        && same.get(NORTH)
        && same.get(EAST)
        && same.get(SOUTH)
        && same.get(WEST);
  }
}
