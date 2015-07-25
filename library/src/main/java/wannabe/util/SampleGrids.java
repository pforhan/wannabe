// Copyright 2013 Patrick Forhan.
package wannabe.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import wannabe.Position;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;

public class SampleGrids {
  /** Grid stretching 30x30 with pixels every 10 along the edge, and 600 random pixels. */
  public static Grid randomGrid() {
    Grid grid = new SimpleGrid("random sparse 30x30");
    grid.add(new Voxel(0, 0, 0, 0xFFEEDD));
    grid.add(new Voxel(1, 0, 0, 0xEEDDFF));
    grid.add(new Voxel(0, 1, 0, 0xDDFFEE));
    grid.add(new Voxel(10, 0, 0, 0xDFFEED));
    grid.add(new Voxel(0, 10, 0, 0xDFFEED));
    grid.add(new Voxel(20, 0, 0, 0xFEEDDF));
    grid.add(new Voxel(0, 20, 0, 0xEDDFFE));
    grid.add(new Voxel(30, 0, 0, 0xFEDFED));
    grid.add(new Voxel(0, 30, 0, 0xDEFDEF));

    Random r = new Random();
    for (int i = 0; i < 600; i++) {
      grid.add(new Voxel(r.nextInt(30), r.nextInt(30), r.nextInt(21) - 10, 0xAAAAAA + r
          .nextInt(0x555555)));
    }
    return grid;
  }

  /** 30x30 grid with all 900 voxels specified. */
  public static Grid fullRandomGrid() {
    Grid grid = new SimpleGrid("random full 30x30");

    Random r = new Random();
    // Grab one randomly to animate:
    int which = r.nextInt(900);
    final AtomicReference<Voxel> animated = new AtomicReference<Voxel>();
    for (int i = 0; i < 900; i++) {
      int x = i / 30;
      int y = i % 30;
      boolean hasHeight = x % 5 == 0 && y % 5 == 0;
      int z = hasHeight ? r.nextInt(21) - 10 : 0;
      int color = 0x999999 + r.nextInt(0x666666);
      Voxel vox = new Voxel(x, y, z, color);
      grid.add(vox);

      if (i == which) {
        animated.set(vox);
        vox.color = 0xFFFFFF;
      } else if (z > 0) {
        // If not animated, and if positive z, make into a cylinder.
        for (int j = 0; j < z; j++) {
          grid.add(new Voxel(x, y, j, color));
        }
      }
    }

    Thread t = new Thread(new Runnable() {
      final Voxel myAnimated = animated.get();

      @Override public void run() {
        while (true) {
          myAnimated.position.z++;
          if (myAnimated.position.z > 10) myAnimated.position.z = -10;
          try {
            Thread.sleep(200);
          } catch (InterruptedException ex) {
          }
        }
      }
    });
    t.start();

    return grid;
  }

  /** Creates a grid with two hundred towers of up to 50 voxels in a 30x30 grid. */
  public static Grid towers() {
    Grid grid = new SimpleGrid("200 towers 30x30");
    Random r = new Random();
    for (int i = 0; i < 200; i++) {
      int x = r.nextInt(30);
      int y = r.nextInt(30);
      int height = r.nextInt(50);
      for (int z = 0; z < height; z++) {
        int color = 0x999999 + r.nextInt(0x666666);
        grid.add(new Voxel(x, y, z, color));
      }
    }
    return grid;
  }

  /** Grid with the heightMap as a base, with "clouds" above and shadows below them. */
  public static Grid cloudySky() {
    Grid grid = heightMap("cloudy heightmap 256x256", false);
    Random r = new Random();
    for (int row = 20; row < 40; row++) {
      for (int col = 20; col < 40; col++) {
        // All clouds are about 50-60z in height. There are three for each x, y coordinate.
        // The higher a cloud, the lighter its color.
        int cloudHeight = r.nextInt(8); // 0-7
        int cloudColorComponent = (cloudHeight << 3) + 199; // 199 - 255
        int cloudColor = (cloudColorComponent << 16)
            + (cloudColorComponent << 8)
            + cloudColorComponent; // 0x888888 - 0xFFFFFF
        grid.add(new Voxel(col, row, 50 + cloudHeight, cloudColor));
        // TODO maybe randomize count and spacing of stacks, too.
        grid.add(new Voxel(col, row, 51 + cloudHeight, cloudColor));
        grid.add(new Voxel(col, row, 52 + cloudHeight, cloudColor));
      }
    }
    return grid;
  }

  public static Grid deepHeightMap() {
    return heightMap("deep heightmap 256x256", true);
  }
  /**
   * Loads the sample-heightmap image into a Grid, with lighter pixels given a greater height
   * and a bluer color.
   * @param name TODO
   */
  public static Grid heightMap(String name, boolean deep) {
    // TODO pick by platform
    return Swing.heightMap(name, deep);
  }

  public static class Swing {
    public static Grid heightMap(String name, boolean deep) {
      Grid grid = new SimpleGrid(name);
      try {
        BufferedImage img = ImageIO.read(Swing.class.getResourceAsStream("/example-heightmap.png"));
        WritableRaster raster = img.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        byte[] bytes = data.getData();

        int width = img.getWidth();
        System.out.println(String.format("Width %s, height %s, w*h %s, len %s", width,
            img.getHeight(), width * img.getHeight(), bytes.length));
        for (int i = 0; i < bytes.length; i++) {
          byte b = bytes[i];
          int x = i % width;
          int y = i / width;
          int z = (0xFF & b) / 4;
          int color = 0x888800 + b;
          grid.add(new Voxel(x, y, z, color));
          // Continue down to height 0 if deep:
          if (deep) {
            for (int d = 0; d < z; d++) {
              // TODO will it look nicer to vary the color along with the height?
              grid.add(new Voxel(x, y, d, color));
            }
          }
        }

      } catch (IOException ex) {
        ex.printStackTrace();
      }
      return grid;
    }
  }

  public static Grid linkGrid() {
    Map<Character, Integer> colorMap = new HashMap<>(3);
    colorMap.put('G', 0xadfc14);
    colorMap.put('S', 0xff8f2b);
    colorMap.put('H', 0xdb450f);
    return fromTextMap("link", ""
        + ".....GGGGGG....\n"
        + "....GGGGGGGG...\n"
        + "..S.GHHHHHHG.S.\n"
        + "..S.HHHHHHHH.S.\n"
        + "..SSHSGSSGSHSS.\n"
        + "..SSHSHSSHSHSS.\n"
        + "...SSSSSSSSSSH.\n"
        + "...GGSSHHSSGGH.\n"
        + ".HHHHHSSSSGGHHH\n"
        + "HHSHHHHGGGGGSHH\n"
        + "HSSSHHSHHGGSSSH\n"
        + "HHSHHHSGHHHHSSS\n"
        + "HHSHHHSHHGGGGS.\n"
        + "HHHHHHSGGGGG...\n"
        + ".SSSSSH..HHH...\n"
        + "....HHH........\n",
        colorMap);
  }

  /**
   * Parses a textmap to create a grid. newlines separate rows.  Any characters not in the color
   * map will act as spacers (leaves empty space in the final grid).
   *
   * @param name Name of the grid
   * @param textmap two-dimensional array of characters representing the desired output.
   * @param charToColor map of character to color describing what colors to use.
   */
  public static Grid fromTextMap(String name, String textmap,
      Map<Character, Integer> charToColor) {
    SimpleGrid grid = new SimpleGrid(name);
    Position workhorse = new Position(0, 0, 0);
    for (int i = 0; i < textmap.length(); i++) {
      char chr = textmap.charAt(i);
      if (chr == '\n') {
        // Found a new line, so increment y, reset x.
        workhorse.x = 0;
        workhorse.y++;
        continue;
      }
      Character boxed = chr;
      if (charToColor.containsKey(boxed)) {
        int color = charToColor.get(boxed);
        grid.add(new Voxel(workhorse.clone(), color));
      }
      workhorse.x++;
    }
    return grid;
  }

  public static final List<Grid> GRIDS = Collections.unmodifiableList(Arrays.asList(
    heightMap("heightMap 256x256", false),
    linkGrid(),
    heightMap("deep heightmap 256x256", true),
    cloudySky(),
    towers(),
    fullRandomGrid(),
    randomGrid()
  ));

  public static Grid next(Grid current) {
    int nextIdx = GRIDS.indexOf(current) + 1;
    return GRIDS.get(nextIdx < GRIDS.size() ? nextIdx : 0);
  }

}
