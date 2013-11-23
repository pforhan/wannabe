// Copyright 2013 Patrick Forhan.
package wannabe.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.SimpleGrid;

public class SampleGrids {
  /** Grid stretching 30x30 with pixels every 10 along the edge, and 600 random pixels. */
  public static Grid randomGrid() {
    Grid grid = new SimpleGrid();
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
    Grid grid = new SimpleGrid();

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
  public static Grid perspectiveBox() {
    Grid grid = new SimpleGrid();
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
    // TODO finish
    return new SimpleGrid();
  }

  /**
   * Loads the sample-heightmap image into a Grid, with lighter pixels given a greater height
   * and a bluer color.
   */
  public static Grid heightMap() {
    // TODO pick by platform
    return Swing.heightMap();
  }

  public static class Swing {
    public static Grid heightMap() {
      Grid grid = new SimpleGrid();
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
        }

      } catch (IOException ex) {
        ex.printStackTrace();
      }
      return grid;
    }
  }

  private static final Grid[] GRID_ARRAY = new Grid[] {
    heightMap(),
    perspectiveBox(),
    cloudySky(),
    fullRandomGrid(),
    randomGrid(),
  };

  public static final List<Grid> GRIDS = Collections.unmodifiableList(Arrays.asList(GRID_ARRAY));

  public static Grid next(Grid current) {
    int nextIdx = GRIDS.indexOf(current) + 1;
    return GRIDS.get(nextIdx < GRIDS.size() ? nextIdx : 0);
  }

}
