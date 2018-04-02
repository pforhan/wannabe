package wannabe.util;

import java.util.Random;
import wannabe.Bounds.XYBounds;
import wannabe.Bounds.XZBounds;
import wannabe.Position;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;
import wannabe.util.Voxels.EdgesZPlotter;
import wannabe.util.Voxels.FloodFillZPlotter;
import wannabe.util.Voxels.YPlotter;

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
 * <li>Dark, cloudy sky
 * <li>Animated rain coming in at an angle toward the house.
 * </ul>
 */
public class HouseVignette {
  private static final int FRAMING_COLOR = 0xFFFFFFFF;
  private static final int DOOR_KNOB_COLOR = 0xFFFDCD50;
  private static final int ROOF_COLOR = 0xFFAA4444;
  private static final int HOUSE_COLOR = 0xFFAA9988;
  private static final int GLASS_COLOR = 0x60888888;

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
    // Make the house hollow
    XYBounds bounds = new XYBounds();
    bounds.setFromWidthHeight(-8, 10, 20, 20);
    for (int z = -5; z < 10; z++) {
      bounds.plot(scene, new EdgesZPlotter(bounds, z, HOUSE_COLOR));
    }

    // Left and right sides:
    bounds.plot(scene, new FloodFillZPlotter(-5, HOUSE_COLOR));
    bounds.plot(scene, new FloodFillZPlotter(9, HOUSE_COLOR));
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
      line(scene, left, right, ROOF_COLOR);
    }
    // Next level: -2 depth on each side
    y = 8;
    for (int z = -5; z < 10; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, ROOF_COLOR);
    }
    // Next level: -4 depth on each side
    y = 7;
    for (int z = -3; z < 8; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, ROOF_COLOR);
    }
    // Next level: -6 depth on each side
    y = 6;
    for (int z = -1; z < 6; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, ROOF_COLOR);
    }
    // Next level: -8 depth on each side
    y = 5;
    for (int z = 1; z < 4; z++) {
      Position left  = new Position(-9, y, z);
      Position right = new Position(12, y, z);
      line(scene, left, right, ROOF_COLOR);
    }
  }

  private void door(SimpleGrid scene) {
    // Frame
    Position topLeft  = new Position(11, 13, 7);
    Position topRight = new Position(11, 13, 2);
    Position botLeft  = new Position(11, 29, 7);
    Position botRight = new Position(11, 29, 2);
    line(scene, topLeft,  topRight, FRAMING_COLOR);
    line(scene, topLeft,  botLeft,  FRAMING_COLOR);
    line(scene, topRight, botRight, FRAMING_COLOR);

    // Doorknob
    scene.put(new Voxel(12, 23, 6, DOOR_KNOB_COLOR));

    // Door window (T, B, L, R from the perspective of facing the door from the outside)
    topLeft = new Position(11, 15, 5);
    botLeft = new Position(11, 20, 5);
    topRight = new Position(11, 15, 4);
    botRight = new Position(11, 20, 4);
    line(scene, topLeft, botLeft, GLASS_COLOR);
    line(scene, topRight, botRight, GLASS_COLOR);
  }

  private void windows(SimpleGrid scene) {
    // "Front" window, beside the door (T, B, L, R from the perspective of facing the window outside)
    final int windowTop = 14;
    final int windowBot = 25;
    Position topLeft  = new Position(11, windowTop, 0);
    Position topRight = new Position(11, windowTop, -4);
    Position botLeft  = new Position(11, windowBot, 0);
    Position botRight = new Position(11, windowBot, -4);

    // - Frame
    line(scene, topLeft,  topRight, FRAMING_COLOR);
    line(scene, topLeft,  botLeft,  FRAMING_COLOR);
    line(scene, topRight, botRight, FRAMING_COLOR);
    line(scene, botLeft, botRight, FRAMING_COLOR);
    // - Glass
    for (int z = -3; z < 0; z++) {
      line(scene, new Position(11, windowTop + 1, z), new Position(11, windowBot - 1, z),
          GLASS_COLOR);
    }

    // Left side rear window (facing from the outside)
    xyWindow(scene, -6, 9);

    // Left side fore window
    xyWindow(scene, 4, 9);

    // Right side rear window (unlike others, as facing window from inside)
    xyWindow(scene, -6, -5);

    // Right side fore window
    xyWindow(scene, 4, -5);
  }

  /** Draws a window on the x-y plane at the specified left and z values. */
  private void xyWindow(SimpleGrid scene, int left, int z) {
    final int windowTop = 14;
    final int windowBot = 25;

    Position topLeft = new Position(left, windowTop, z);
    Position topRight = new Position(left + 5, windowTop, z);
    Position botLeft = new Position(left, windowBot, z);
    Position botRight = new Position(left + 5, windowBot, z);
    // - Frame
    line(scene, topLeft,  topRight, FRAMING_COLOR);
    line(scene, topLeft,  botLeft,  FRAMING_COLOR);
    line(scene, topRight, botRight, FRAMING_COLOR);
    line(scene, botLeft, botRight, FRAMING_COLOR);
    // - Glass
    for (int x = left + 1; x < left + 5; x++) {
      line(scene, new Position(x, windowTop + 1, z), new Position(x, windowBot - 1, z),
          GLASS_COLOR);
    }
  }

  private void yard(SimpleGrid scene) {
    // Yard is just a flat bit of greens beneath the house, stretches 5 beyond the walls on
    // the sides and back, 25 out the front.
    Random r = new Random();
    XZBounds bounds = new XZBounds();
    bounds.setFromWidthHeight(-13, -10, 50, 30);
    bounds.plot(scene, new YPlotter() {
      @Override public Voxel plot(int x, int z) {
        return new Voxel(x, 30, z, 0xFF00A000 + (r.nextInt(128) << 16) + r.nextInt(128));
      }
    });
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
