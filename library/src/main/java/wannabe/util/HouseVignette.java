package wannabe.util;

import wannabe.Bounds.XYBounds;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;
import wannabe.util.Voxels.ZPlotter;

/**
 * An attempt to make a scene with realistic elements.  Should be "real" positioning as much
 * as possible without fakes.
 * <br>
 * Scene:
 * <ul>
 * <li>House on the left with door, windows, roof.
 * <li>Fenced yard with pathway and gate
 * <li>Distant hills
 * <li>Cloudy sky
 * <li>Animated rain coming in at an angle toward the house.
 * </ul>
 */
public class HouseVignette {
  /** 30x30x30 base cube */
  public Grid buildHouse() {
    SimpleGrid house = new SimpleGrid("House");

    // Flood-fill a cube:
    XYBounds bounds = new XYBounds();
    bounds.setFromWidthHeight(-20, 0, 30, 30);
    final int[] ugh = {0};
    for (int z = -10; z < 20; z++) {
      ugh[0] = z;
      bounds.plot(house, new ZPlotter() {
        @Override public Voxel plot(int x, int y) {
          return new Voxel(x, y, ugh[0], 0xFFAA9988);
        }
      });
    }
    return house;
  }
}
