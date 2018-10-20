package wannabe;

import wannabe.grid.AllNeighbors;
import wannabe.grid.MutableGrid;
import wannabe.util.Voxels.YPlotter;
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

    @Override public boolean containsAll(Position pos, AllNeighbors neighbors) {
      return true;
    }
  };

  public boolean contains(Position position);
  public boolean contains(Translation position);
  /** Indicates whether all specified neighbors are within bounds. */
  public boolean containsAll(Position pos, AllNeighbors neighbors);

  /** A 2d {@link Bounds} on the XY plane. */
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

    public boolean isEdge(int x, int y) {
      return x == left || x == right - 1 || y == top || y == bottom - 1;
    }

    @Override public boolean contains(Position pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
    }

    @Override public boolean contains(Translation pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
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

    public void plot(MutableGrid grid, ZPlotter plotter) {
      for (int row = top; row < bottom; row++) {
        for (int col = left; col < right; col++) {
          final Voxel plotted = plotter.plot(col, row);
          if (plotted == null) continue;
          grid.put(plotted);
        }
      }
    }
  }

  /** A 2d {@link Bounds} on the XZ plane. */
  // TODO can these be merged without being confusing?
  public class XZBounds implements Bounds {
    private int left;
    private int right;
    private int top;
    private int bottom;

    public void setFromWidthHeight(int x, int z, int width, int height) {
      left = x;
      top = z;
      right = x + width;
      bottom = z + height;
    }

    public void setFromAbsolute(int left, int top, int right, int bottom) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
    }

    public boolean isEdge(int x, int z) {
      return x == left || x == right - 1 || z == top || z == bottom - 1;
    }

    @Override public boolean contains(Position pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
    }

    @Override public boolean contains(Translation pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
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

    public void plot(MutableGrid grid, YPlotter plotter) {
      for (int row = top; row < bottom; row++) {
        for (int col = left; col < right; col++) {
          final Voxel plotted = plotter.plot(col, row);
          if (plotted == null) continue;
          grid.put(plotted);
        }
      }
    }
  }
}
