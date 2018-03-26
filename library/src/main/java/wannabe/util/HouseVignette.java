package wannabe.util;

import wannabe.Bounds.XYBounds;
import wannabe.Position;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;
import wannabe.projection.Projection;

import static wannabe.util.Voxels.line;

/**
 * An attempt to make a scene with realistic elements. Should be "real" positioning as much as
 * possible without fakes or facades.
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
  public Projection preferredProjection() {
    return null; //Projections.PROJECTIONS.
  }

  public Grid buildHouse() {
    SimpleGrid scene = new SimpleGrid("House");

    // House
    house(scene);
    roof(scene);
    door(scene);
    windows(scene);

    // Yard
    yard(scene);
    path(scene);

    // Background
    hills(scene);
    clouds(scene);
    rain(scene);

    return scene;
  }

  private void house(SimpleGrid scene) {
    // Flood-fill a 20x20x15 base house from -8,10,-5 to 12,30,10
    XYBounds bounds = new XYBounds();
    bounds.setFromWidthHeight(-8, 10, 20, 20);
    final int[] ugh = { 0 };
    for (int z = -5; z < 10; z++) {
      ugh[0] = z;
      bounds.plot(scene, (x, y) -> new Voxel(x, y, ugh[0], 0xFFAA9988));
    }
  }

  private void roof(SimpleGrid scene) {
    // Add a roof. Extends 2 over the sides of the house. Spine runs along the x axis.
    // In practice, a triangular prism, just made out of lines stacked together.
    // TODO: eaves should be hollow, ie just the edges hang over
    // TODO ...could do that via some lines at the end
    // First level: full depth
    int y = 9;
    for (int z = -7; z < 12; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, 0xFFAA4444);
    }
    // Next level: -2 depth on each side
    y = 8;
    for (int z = -5; z < 10; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, 0xFFAA4444);
    }
    // Next level: -4 depth on each side
    y = 7;
    for (int z = -3; z < 8; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, 0xFFAA4444);
    }
    // Next level: -6 depth on each side
    y = 6;
    for (int z = -1; z < 6; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, 0xFFAA4444);
    }
    // Next level: -8 depth on each side
    y = 5;
    for (int z = 1; z < 4; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, 0xFFAA4444);
    }
  }

  private void door(SimpleGrid scene) {
    Position topLeft  = new Position(11, 13, 7);
    Position topRight = new Position(11, 13, 2);
    Position botLeft  = new Position(11, 29, 7);
    Position botRight = new Position(11, 29, 2);
    line(scene, topLeft,  topRight, 0xFFFFFFFF);
    line(scene, topLeft,  botLeft,  0xFFFFFFFF);
    line(scene, topRight, botRight, 0xFFFFFFFF);

    // Doorknob
    scene.put(new Voxel(12, 23, 6, 0xFFFDCD50));
  }

  private void windows(SimpleGrid scene) {

  }

  private void yard(SimpleGrid scene) {
    // TODO Auto-generated method stub

  }

  private void path(SimpleGrid scene) {
    // TODO Auto-generated method stub

  }

  private void hills(SimpleGrid scene) {
    // TODO Auto-generated method stub

  }

  private void clouds(SimpleGrid scene) {
    // TODO Auto-generated method stub

  }

  private void rain(SimpleGrid scene) {
    // TODO Auto-generated method stub

  }
}
