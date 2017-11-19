package wannabe;

import wannabe.grid.AllNeighbors;
import wannabe.grid.Neighbors;

import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

// TODO make an XYZ bounds too, of course.
public interface Bounds {
  public boolean contains(Position position);
  public boolean contains(Translation position);
  /** Indicates whether all specified neighbors are within bounds. */
  public boolean containsAll(Neighbors neighbors);
  public boolean containsAll(Position pos, AllNeighbors neighbors);

  public class XYBounds implements Bounds {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public void setFromWidthHeight(int x, int y, int width, int height) {
      left = x;
      top = y;
      right = x + width;
      bottom = y + height;
    }

    public void setFromAbsolute(int left, int top, int right, int bottom) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
    }

    @Override public boolean contains(Position pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
    }

    @Override public boolean contains(Translation pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
    }

    /** Ignores above and below. */
    @Override public boolean containsAll(Neighbors neighbors) {
      return containsOrNull(neighbors.north)
          && containsOrNull(neighbors.south)
          && containsOrNull(neighbors.east)
          && containsOrNull(neighbors.west);
    }

    /** This XY bounds ignores above and below. */
    @Override public boolean containsAll(Position pos, AllNeighbors neighbors) {
      // Must contain the N,S,E,W neighbors, if they exist.
      return contains(pos)
          && (!neighbors.same.get(WEST)  || pos.x - 1 >= left)
          && (!neighbors.same.get(EAST)  || pos.x + 1 <  right)
          && (!neighbors.same.get(NORTH) || pos.y - 1 >= top)
          && (!neighbors.same.get(SOUTH) || pos.y + 1 <  bottom);
    }

    private boolean containsOrNull(Neighbors neighbors) {
      return neighbors == null || contains(neighbors.voxel.position);
    }
  }
}
