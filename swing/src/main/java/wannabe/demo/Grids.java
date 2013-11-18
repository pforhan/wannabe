// Copyright 2013 Square, Inc.
package wannabe.demo;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import wannabe.Grid;
import wannabe.Voxel;

public class Grids {

  public static Grid randomGrid() {
    Grid grid = new Grid();
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
      grid.add(new Voxel(r.nextInt(30), r.nextInt(30), r.nextInt(21) - 10,
          0xAAAAAA + r.nextInt(0x555555)));
    }
    return grid;
  }

  public static Grid fullRandomGrid() {
    Grid grid = new Grid();

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

  public static Grid heightMap() {
    // TODO pick by platform
    return Swing.heightMap();
  }

  public static class Swing {
    public static Grid heightMap() {
      Grid grid = new Grid();
      try {
        BufferedImage img = ImageIO.read(Swing.class.getResourceAsStream("/example-heightmap.png"));
        WritableRaster raster = img.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        byte[] bytes = data.getData();

        int width = img.getWidth();
        System.out.println(
            String.format("Width %s, height %s, w*h %s, len %s",
                width, img.getHeight(), width * img.getHeight(), bytes.length));
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
}
