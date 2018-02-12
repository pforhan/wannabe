package wannabe;

import wannabe.grid.AllNeighbors;
import wannabe.grid.MutableGrid;
import wannabe.grid.Neighbors;
import wannabe.util.Voxels.ZPlotter;

import static wannabe.grid.AllNeighbors.RelativePosition.EAST;
import static wannabe.grid.AllNeighbors.RelativePosition.NORTH;
import static wannabe.grid.AllNeighbors.RelativePosition.SOUTH;
import static wannabe.grid.AllNeighbors.RelativePosition.WEST;

public interface Bounds {
  /** A bounds that accepts all voxels. */
  Bounds UNBOUNDED = new Bounds() {
    @Override public boolean contains(Position position) {
      return true;
    }

    @Override public boolean contains(Translation position) {
      return true;
    }

    @Override public boolean containsAll(Neighbors neighbors) {
      return true;
    }

    @Override public boolean containsAll(Position pos, AllNeighbors neighbors) {
      return true;
    }
  };

  public boolean contains(Position position);
  public boolean contains(Translation position);
  /** Indicates whether all specified neighbors are within bounds. */
  public boolean containsAll(Neighbors neighbors);
  public boolean containsAll(Position pos, AllNeighbors neighbors);

  public class XYBounds implements Bounds {
    private int left;
    private int right;
    private int top;
    private int bottom;

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

    public void plot(MutableGrid grid, ZPlotter plotter) {
      for (int row = top; row < bottom; row++) {
        for (int col = left; col < right; col++) {
          grid.put(plotter.plot(col, row));
        }
      }
    }
  }
}
