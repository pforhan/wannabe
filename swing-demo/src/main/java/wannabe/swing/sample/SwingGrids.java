package wannabe.swing.sample;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import wannabe.Voxel;
import wannabe.grid.Grid;
import wannabe.grid.MutableGrid;
import wannabe.grid.SimpleGrid;
import wannabe.util.SampleGrids;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class SwingGrids {

  /** Grid with the heightMap as a base, with "clouds" above and shadows below them. */
  public static Grid cloudySky() {
    MutableGrid grid = heightMap("cloudy heightmap 256x256", false);
    Random r = new Random();
    for (int row = 20; row < 40; row++) {
      for (int col = 20; col < 40; col++) {
        // All clouds are about 50-60z in height. There are three for each x, y coordinate.
        // The higher a cloud, the lighter its color.
        int cloudHeight = r.nextInt(4) + 1; // 1-4
        int cloudColorComponent = (cloudHeight << 3) + 199; // 199 - 255
        int cloudColor = (0x7A << 24)
            + (cloudColorComponent << 16)
            + (cloudColorComponent << 8)
            + cloudColorComponent; // 0x888888 - 0xFFFFFF
        grid.put(new Voxel(col, row, 50 + cloudHeight, cloudColor));
        grid.put(new Voxel(col, row, 51 + cloudHeight, cloudColor));
        grid.put(new Voxel(col, row, 52 + cloudHeight, cloudColor));
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
   */
  public static MutableGrid heightMap(String name, boolean deep) {
    MutableGrid grid = new SimpleGrid(name);
    try {
      BufferedImage img = ImageIO.read(SwingGrids.class.getResourceAsStream("/example-heightmap.png"));
      WritableRaster raster = img.getRaster();
      DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
      byte[] bytes = data.getData();

      int width = img.getWidth();
      for (int i = 0; i < bytes.length; i++) {
        byte b = bytes[i];
        int x = i % width;
        int y = i / width;
        int z = (0xFF & b) / 4;
        int color = 0x888800 + b;
        grid.put(new Voxel(x, y, z, color));
        // Continue down to height 0 if deep:
        if (deep) {
          for (int d = 0; d < z; d++) {
            color = 0x888800 + d * 4;
            grid.put(new Voxel(x, y, d, color));
          }
        }
      }

    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return grid;
  }

  public static final List<Grid> GRIDS = join(SampleGrids.GRIDS, asList(
    heightMap("heightMap 256x256", false),
    heightMap("deep heightmap 256x256", true),
    cloudySky()
  ));

  public static Grid next(Grid current) {
    int nextIdx = GRIDS.indexOf(current) + 1;
    return GRIDS.get(nextIdx < GRIDS.size() ? nextIdx : 0);
  }

  private static List<Grid> join(List<Grid> grids1, List<Grid> grids2) {
    List<Grid> all = new ArrayList<>(grids1.size() + grids2.size());
    all.addAll(grids1);
    all.addAll(grids2);
    return unmodifiableList(all);
  }


}
