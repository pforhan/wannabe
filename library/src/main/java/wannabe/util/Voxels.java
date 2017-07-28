package wannabe.util;

import wannabe.Position;
import wannabe.Translation;
import wannabe.Voxel;
import wannabe.grid.MutableGrid;

/** Voxel and Grid utilities. */
public class Voxels {

  /**
   * Simple mechanism to iteratively add a set of voxels. See {@link #drawPath}.
   * TODO Can I make an Iterable? Would that gain anything?
   */
  interface Path {
    /** Set up the path and return its starting Position. */
    Position start();

    /**
     * Adds voxel(s) at the specified location, and returns the next location, or {@code null} if
     * complete.
     */
    Translation drawAndMove(MutableGrid grid, Translation pos);
  }

  /**
   * TODO does it make sense to restrict to x, y inputs? And when I make it generic this way,
   * I bet there's better function interfaces out there...
   * It may be worth letting the plotter plot voxels directly, could make more accurate color
   * decisions... or could plot multiple points.  This reduces capability to a single scalar.
   */
  interface Plotter {
    int plot(int x, int y);
  }

  /**
   * Draw a Bresenham line, uses all three dimensions.
   * Adapted from: https://www.ict.griffith.edu.au/anthony/info/graphics/bresenham.procs
   */
  public static void line(MutableGrid grid, Position p1, Position p2, int color) {
    int i, dx, dy, dz, l, m, n, x_inc, y_inc, z_inc, err_1, err_2, dx2, dy2, dz2;
    Translation pixel = new Translation(p1);

    dx = p2.x - p1.x;
    dy = p2.y - p1.y;
    dz = p2.z - p1.z;
    x_inc = (dx < 0) ? -1 : 1;
    l = Math.abs(dx);
    y_inc = (dy < 0) ? -1 : 1;
    m = Math.abs(dy);
    z_inc = (dz < 0) ? -1 : 1;
    n = Math.abs(dz);
    dx2 = l << 1;
    dy2 = m << 1;
    dz2 = n << 1;

    if ((l >= m) && (l >= n)) {
      err_1 = dy2 - l;
      err_2 = dz2 - l;
      for (i = 0; i < l; i++) {
        grid.add(new Voxel(pixel.asPosition(), color));
        if (err_1 > 0) {
          pixel.y += y_inc;
          err_1 -= dx2;
        }
        if (err_2 > 0) {
          pixel.z += z_inc;
          err_2 -= dx2;
        }
        err_1 += dy2;
        err_2 += dz2;
        pixel.x += x_inc;
      }
    } else if ((m >= l) && (m >= n)) {
      err_1 = dx2 - m;
      err_2 = dz2 - m;
      for (i = 0; i < m; i++) {
        grid.add(new Voxel(pixel.asPosition(), color));
        if (err_1 > 0) {
          pixel.x += x_inc;
          err_1 -= dy2;
        }
        if (err_2 > 0) {
          pixel.z += z_inc;
          err_2 -= dy2;
        }
        err_1 += dx2;
        err_2 += dz2;
        pixel.y += y_inc;
      }
    } else {
      err_1 = dy2 - n;
      err_2 = dx2 - n;
      for (i = 0; i < n; i++) {
        grid.add(new Voxel(pixel.asPosition(), color));
        if (err_1 > 0) {
          pixel.y += y_inc;
          err_1 -= dz2;
        }
        if (err_2 > 0) {
          pixel.x += x_inc;
          err_2 -= dz2;
        }
        err_1 += dy2;
        err_2 += dx2;
        pixel.z += z_inc;
      }
    }
    grid.add(new Voxel(pixel.asPosition(), color));
  }

  static void drawPath(MutableGrid grid, Path path) {
    Translation pos = new Translation(path.start());
    while (pos != null) {
      pos = path.drawAndMove(grid, pos);
    }
  }

  static void plot(MutableGrid grid, Plotter plotter) {
    for (int x = 0; x < 40; x++) {
      for (int y = 0; y < 40; y++) {
        int height = plotter.plot(x - 20, y - 20);
        grid.add(new Voxel(x,  y, height, 0x888888 + height * 10));
      }
    }
  }

}
