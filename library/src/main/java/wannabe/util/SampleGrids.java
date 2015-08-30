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
import wannabe.grid.FrameAnimatedGrid;
import wannabe.grid.Grid;
import wannabe.grid.MutableGrid;
import wannabe.grid.SimpleGrid;

public class SampleGrids {

  /**
   * TODO figure out if this is viable. Need some way of iteratively creating data.
   * Can I clean up? Can I make an Iterable?
   */
  interface Path {
    Position start();
    Position drawAndMove(MutableGrid grid, Position pos);
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

  public static final class ColumnColorStairs implements Path {
    private final int xIncrement;
    private final int yIncrement;
    private final Position start;
    private final int[] colors;

    public ColumnColorStairs(int xIncrement, int yIncrement, Position start, int... colors) {
      this.xIncrement = xIncrement;
      this.yIncrement = yIncrement;
      this.start = start;
      this.colors = colors;
    }

    @Override public Position start() {
      return start.clone();
    }

    @Override public Position drawAndMove(MutableGrid grid, Position pos) {
      Position lineEnd = new Position(pos.x, pos.y, 0);
      line(grid, pos, lineEnd, getColor(pos));
      pos.x += xIncrement;
      pos.y += yIncrement;
      pos.z++;
      return pos.z == 6 ? null : pos;
    }

    private int getColor(Position pos) {
      int offset = Math.abs(pos.x - start.x);
      if (offset == 0) {
        offset = Math.abs(pos.y - start.y);
      }
      if (offset >= colors.length) offset = colors.length - 1;
      return colors[offset];
    }
  }

  /** Simple stairs on x or y axis that may vary in color by height. */
  public static final class HeightColorStairs implements Path {
    private final int xIncrement;
    private final int yIncrement;
    private final int[] colors;
    private final Position start;
    private final Position endXY;

    public HeightColorStairs(int xIncrement, int yIncrement, Position start, Position endXY,
        int... colors) {
      this.start = start;
      this.endXY = endXY;
      this.xIncrement = xIncrement;
      this.yIncrement = yIncrement;
      this.colors = colors;
    }

    @Override public Position start() {
      return start.clone();
    }

    @Override public Position drawAndMove(MutableGrid grid, Position pos) {
      Position lineEnd = new Position(endXY.x, endXY.y, pos.z);
      line(grid, pos, lineEnd, getColor(pos));
      pos.x += xIncrement;
      pos.y += yIncrement;
      pos.z++;
      return pos.z == 6 ? null : pos;
    }

    private int getColor(Position pos) {
      if (colors.length == 0) return 0x888888;
      int offset = pos.z;
      if (offset >= colors.length) offset = colors.length - 1;
      return colors[offset];
    }
  }

  /** Grid stretching 30x30 with voxels every 10 along the edge, and 600 random voxels. */
  public static Grid randomGrid() {
    MutableGrid grid = new SimpleGrid("random sparse 30x30");
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

  /** Grid stretching 30x30 with voxels in simple patterns like stairsteps, etc */
  public static Grid testBed() {
    MutableGrid grid = new SimpleGrid("testbed 30x30");

    // First set of steps: single color stairs to floor.
    Position start = new Position(5, 5, 0);
    drawPath(grid,
        new HeightColorStairs(1, 0,  start, new Position(10, 5, 0), 0xFFEEDD));
    drawPath(grid,
        new HeightColorStairs(0, 1,  start, new Position(5, 10, 0), 0xFFEEDD));
    drawPath(grid,
        new HeightColorStairs(-1, 0, start, new Position(0, 5, 0), 0xFFEEDD));
    drawPath(grid,
        new HeightColorStairs(0, -1, start, new Position(5, 0, 0), 0xFFEEDD));

    // Second set of steps: color varies by height
    int[] stairColors = {0xDFFEED, 0xDEEEED, 0xDDDEED, 0xDCCEED, 0xDBBEED, 0xDAAEED};
    start = new Position(20, 5, 0);
    drawPath(grid,
        new HeightColorStairs(1, 0,  start, new Position(25, 5, 0), stairColors));
    drawPath(grid,
        new HeightColorStairs(0, 1,  start, new Position(20, 10, 0), stairColors));
    drawPath(grid,
        new HeightColorStairs(-1, 0, start, new Position(15, 5, 0), stairColors));
    drawPath(grid,
        new HeightColorStairs(0, -1, start, new Position(20, 0, 0), stairColors));

    // Third set of steps: color varies by column
    stairColors = new int[] {0xFEEDDF, 0xEEEDDE, 0xDEEDDD, 0xCEEDDC, 0xBEEDDB, 0xAEEDDA};
    start = new Position(5, 20, 0);
    drawPath(grid, new ColumnColorStairs(1, 0, start, stairColors));
    drawPath(grid, new ColumnColorStairs(0, 1, start, stairColors));
    drawPath(grid, new ColumnColorStairs(-1, 0, start, stairColors));
    drawPath(grid, new ColumnColorStairs(0, -1, start, stairColors));

    return grid;
  }

  private static void drawPath(MutableGrid grid, Path path) {
    Position pos = path.start();
    while (pos != null) {
      pos = path.drawAndMove(grid, pos);
    }
  }

  /** Plot of an exaggerated sine. Note that for pleasing results, the origin is shifted. */
  public static Grid plotSin(final int mulitplyer) {
    // TODO at low multiplyers the colors aren't distinct enough.
    MutableGrid grid = new SimpleGrid("Sine plot x" + mulitplyer + " 40x40");
    Plotter plotter = new Plotter() {
      @Override public int plot(int x, int y) {
        double distanceFromOrigin = Math.hypot(x, y);
        return (int) (mulitplyer * (Math.sin(distanceFromOrigin)));
      }
    };
    plot(grid, plotter);
    return grid;
  }

  /** Plot of a flattened hyperbola. Note that for pleasing results, the origin is shifted. */
  public static Grid plotHyperbola(final double d) {
    MutableGrid grid = new SimpleGrid("Hyperbola x" + d + " 40x40");
    Plotter plotter = new Plotter() {
      @Override public int plot(int x, int y) {
        double distanceFromOrigin = Math.hypot(x, y);
        return (int) (d * distanceFromOrigin * distanceFromOrigin);
      }
    };
    plot(grid, plotter);
    return grid;
  }

  private static void plot(MutableGrid grid, Plotter plotter) {
    for (int x = 0; x < 40; x++) {
      for (int y = 0; y < 40; y++) {
        int height = plotter.plot(x - 20, y - 20);
        grid.add(new Voxel(x,  y, height, 0x888888 + height * 10));
      }
    }
  }

  public static Grid cube(int size, int color) {
    MutableGrid grid = new SimpleGrid("cube of size " + size);

    // Base is four lines, top, bottom, left, right:
    Position tl = new Position(0, 0, 0);
    Position tr = new Position(size, 0, 0);
    Position bl = new Position(0, size, 0);
    Position br = new Position(size, size, 0);
    line(grid, tl, tr, color);
    line(grid, bl, br, color);
    line(grid, tl, bl, color);
    line(grid, tr, br, color);

    // Risers: TODO just draw with line() method
    for (int z = 1; z < size; z++) {
      tl.z = z;
      tr.z = z;
      bl.z = z;
      br.z = z;
      grid.add(new Voxel(tl.clone(), color));
      grid.add(new Voxel(tr.clone(), color));
      grid.add(new Voxel(bl.clone(), color));
      grid.add(new Voxel(br.clone(), color));
    }

    // TODO probably need to bump z once more...
    // Top:
    line(grid, tl, tr, color);
    line(grid, bl, br, color);
    // TODO this duplicates some voxels!
    line(grid, tl, bl, color);
    line(grid, tr, br, color);

    return grid;
  }

  private static void line(MutableGrid grid, Position p1, Position p2, int color) {
    // Adapted from: https://www.ict.griffith.edu.au/anthony/info/graphics/bresenham.procs

    int i, dx, dy, dz, l, m, n, x_inc, y_inc, z_inc, err_1, err_2, dx2, dy2, dz2;
    Position pixel = new Position(p1);

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
        grid.add(new Voxel(pixel.clone(), color));
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
        grid.add(new Voxel(pixel.clone(), color));
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
        grid.add(new Voxel(pixel.clone(), color));
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
    grid.add(new Voxel(pixel.clone(), color));
  }

  /** 30x30 grid with all 900 voxels specified. */
  public static Grid fullRandomGrid() {
    MutableGrid grid = new SimpleGrid("random full 30x30");

    Random r = new Random();
    // Grab one randomly to animate:
    int which = r.nextInt(900);
    final AtomicReference<Voxel> animated = new AtomicReference<>();
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
    MutableGrid grid = new SimpleGrid("200 towers 30x30");
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
    MutableGrid grid = heightMap("cloudy heightmap 256x256", false);
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
   * TODO handle other platforms
   */
  public static MutableGrid heightMap(String name, boolean deep) {
    // TODO pick by platform
    return Swing.heightMap(name, deep);
  }

  public static class Swing {
    public static MutableGrid heightMap(String name, boolean deep) {
      MutableGrid grid = new SimpleGrid(name);
      try {
        BufferedImage img = ImageIO.read(Swing.class.getResourceAsStream("/example-heightmap.png"));
        WritableRaster raster = img.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        byte[] bytes = data.getData();

        int width = img.getWidth();
        System.out.println(String.format("heightmap Width %s, height %s, w*h %s, len %s", width,
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
              color = 0x888800 + d * 4;
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
    colorMap.put('G', 0xadfc14); // Green
    colorMap.put('S', 0xff8f2b); // Light brown
    colorMap.put('H', 0xdb450f); // Dark brown
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
    MutableGrid grid = new SimpleGrid(name);
    Position workhorse = new Position(0, 0, 40);
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

  public static Grid morboCat() {
    Map<Character, Integer> colorMap = new HashMap<>(3);
    colorMap.put('*', 0x000000); // Black
    colorMap.put('@', 0x444444); // Gray
    colorMap.put('@', 0xbbbbbb); // Gray
    colorMap.put('y', 0xaaaa00); // Yellow
    Grid frame = fromTextMap("morbo", ""
        + "...........................................................\n"
        + "......**...................................................\n"
        + ".....**.............................*.......*..............\n"
        + "....**..............................**.....**..............\n"
        + "...**...............................*.*...***..............\n"
        + "..**................................*..***..*..............\n"
        + "..**...............................*.........*.............\n"
        + "..**..............................*..yy...yy..*............\n"
        + "..**..............................*..y*...*y..*............\n"
        + "..**..............................*...........*............\n"
        + "..**...***************.............***********.............\n"
        + "...******..........***************..........*..............\n"
        + "...*........................................*..............\n"
        + "...*........................................*..............\n"
        + "...*.......................................*...............\n"
        + "....*......................................*...............\n"
        + "......*....................................*...............\n"
        + ".......****************************************............\n"
        + "......**...**................**...........******...........\n"
        + ".....**.....**...............**................****........\n"
        + "....**......**...............**...................***......\n"
        + "....**.......**..............**............................\n"
        + "...**........**...............**...........................\n",
        colorMap);
    return frame;
  }

  public static FrameAnimatedGrid megaManRunning() {
    Map<Character, Integer> colorMap = new HashMap<>(3);
    colorMap.put('*', 0x000000); // Black
    colorMap.put('B', 0x0b59f4); // Dark blue
    colorMap.put('t', 0x21ffff); // Turquoise
    colorMap.put('O', 0xffffff); // White
    colorMap.put('f', 0xfede9c); // Face
    Grid frame1 = fromTextMap("mmr1", "" // Frame 2 is 2px taller, so 1 and 3 need spacer rows
        + ".........................\n"
        + ".........................\n"
        + ".............***.........\n"
        + "...........***tt*........\n"
        + "..........*BBB*tt*.......\n"
        + ".........*BBBBB****......\n"
        + ".........*BBBBB*ttB*.....\n"
        + "......***tBBBBBB**B*.....\n"
        + "....**tt*tBBfOOOBBO*.....\n"
        + "...*B*tt*tBfOO**f*O*.....\n"
        + "..*BBB*tt*BfOO**f*O*.***.\n"
        + ".*BBBB*tt*BffOOOfOf**BBB*\n"
        + ".*BBB*.*tt*Bf****f*.*BBB*\n"
        + ".*BBB*.*ttt*fffff*t**BBB*\n"
        + "..***..*Bttt*****tttBBB*.\n"
        + "....**.*BBBttttt*ttBBB*..\n"
        + "...*BB**BBBBBBt*.*tBB*...\n"
        + "..*BBBB**BBttt*...***....\n"
        + ".*BBBBBt**Btttt*.........\n"
        + ".*BB*BBttt**tttB*........\n"
        + "..****Btt*.*BBBB*........\n"
        + "......***..*BBB***.......\n"
        + "..........*BBBBBBB*......\n"
        + "..........*********......\n",
        colorMap);
    Grid frame2 = fromTextMap("mmr2", ""
        + ".............***.........\n"
        + "...........***tt*........\n"
        + "..........*BBB*tt*.......\n"
        + ".........*BBBBB****......\n"
        + ".........*BBBBB*ttB*.....\n"
        + "........*tBBBBBB**B*.....\n"
        + "........*tBBfOOOBBO*.....\n"
        + ".......**tBfOO**f*O*.....\n"
        + "......*tt*BfOO**f*O*.....\n"
        + ".....*ttt*BffOOOfOf*.....\n"
        + "....*ttttt*Bf****f*......\n"
        + "....*ttt*tt*fffff*.......\n"
        + ".....*BBB*tt******.......\n"
        + ".....*BBBB**tttt*B*......\n"
        + "......*BB*BB*tt*BB*......\n"
        + ".......*BBBB*B****.......\n"
        + "........*BB*B*t*.........\n"
        + ".........**tt*t*.........\n"
        + "........**ttt**..........\n"
        + ".......*BBBBB*...........\n"
        + "......*BBBBB*............\n"
        + "......*BBB***............\n"
        + ".......*BBBBB*...........\n"
        + "........******...........\n",
        colorMap);
    Grid frame3 = fromTextMap("mmr3", ""
        + ".........................\n"
        + ".........................\n"
        + ".............***.........\n"
        + "...........***tt*........\n"
        + "..........*BBB*tt*.......\n"
        + ".........*BBBBB****......\n"
        + ".........*BBBBB*ttB*.....\n"
        + "........*tBBBBBB**B*.....\n"
        + "........*tBBfOOOBBO*.....\n"
        + "......***tBfOO**f*O*.....\n"
        + "....**ttt*BfOO**f*O*.....\n"
        + "...*BBBttt*ffOOOfOf*.....\n"
        + "..*BBBBtt*ttf****f*......\n"
        + "..*BBB***ttttBfff***.....\n"
        + "..*BBB*.*ttttt***BBB*....\n"
        + "...***.*Bt*BttBBBBBB*....\n"
        + "..*BB***BBt*BtBBBBBB*....\n"
        + ".*BBBB*tBBBB********.....\n"
        + "*BBBBBtttBBBBttt*........\n"
        + "*BB*BBttt***tttBB*.......\n"
        + ".****BBt*...*BBBB*.......\n"
        + ".....***....*BBB***......\n"
        + "...........*BBBBBBB*.....\n"
        + "...........*********.....\n",
        colorMap);

    FrameAnimatedGrid grid = new FrameAnimatedGrid("megaman!");
    grid.addFrame(frame1);
    grid.addFrame(frame2);
    grid.addFrame(frame3);
    grid.addFrame(frame2);
    return grid;
  }

  public static final List<Grid> GRIDS = Collections.unmodifiableList(Arrays.asList(
    heightMap("heightMap 256x256", false),
    testBed(),
    plotSin(5),
    plotSin(2),
    plotHyperbola(.2),
    linkGrid(),
    heightMap("deep heightmap 256x256", true),
    cloudySky(),
    towers(),
    fullRandomGrid(),
    randomGrid(),
    cube(20, 0x21ffff)
  ));

  public static Grid next(Grid current) {
    int nextIdx = GRIDS.indexOf(current) + 1;
    return GRIDS.get(nextIdx < GRIDS.size() ? nextIdx : 0);
  }


}
