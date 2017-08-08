package wannabe.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import wannabe.Position;
import wannabe.Translation;
import wannabe.Voxel;
import wannabe.grid.FrameAnimatedGrid;
import wannabe.grid.Grid;
import wannabe.grid.MutableGrid;
import wannabe.grid.SimpleGrid;
import wannabe.util.Voxels.Path;
import wannabe.util.Voxels.Plotter;

import static wannabe.util.Voxels.drawPath;
import static wannabe.util.Voxels.fromTextMap;
import static wannabe.util.Voxels.line;

public class SampleGrids {

  public static final class Stairs implements Path {
    private final int xIncrement;
    private final int yIncrement;
    private final int color;
    private final Position start;
    private final int count;

    public Stairs(int xIncrement, int yIncrement, Position start, int count, int color) {
      this.start = start;
      this.xIncrement = xIncrement;
      this.yIncrement = yIncrement;
      this.count = count;
      this.color = color;
    }

    @Override public Position start() {
      return start;
    }

    @Override public Translation drawAndMove(MutableGrid grid, Translation pos) {
      grid.add(new Voxel(pos.asPosition(), color));

      pos.x += xIncrement;
      pos.y += yIncrement;
      pos.z++;
      return pos.z - count == 0 ? null : pos;
    }
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
      return start;
    }

    @Override public Translation drawAndMove(MutableGrid grid, Translation pos) {
      Position lineEnd = new Position(pos.x, pos.y, 0);
      line(grid, pos.asPosition(), lineEnd, getColor(pos));
      pos.x += xIncrement;
      pos.y += yIncrement;
      pos.z++;
      return pos.z == 6 ? null : pos;
    }

    private int getColor(Translation pos) {
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
      return start;
    }

    @Override public Translation drawAndMove(MutableGrid grid, Translation pos) {
      Position lineEnd = new Position(endXY.x, endXY.y, pos.z);
      line(grid, pos.asPosition(), lineEnd, getColor(pos));
      pos.x += xIncrement;
      pos.y += yIncrement;
      pos.z++;
      return pos.z == 6 ? null : pos;
    }

    private int getColor(Translation pos) {
      if (colors.length == 0) return 0x888888;
      int offset = pos.z;
      if (offset >= colors.length) offset = colors.length - 1;
      return colors[offset];
    }
  }

  /** Grid stretching 30x30 with voxels every 10 along the edge, and 600 random voxels. */
  public static Grid randomGrid() {
    MutableGrid grid = new SimpleGrid("random sparse 30x30", true);
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
    MutableGrid grid = new SimpleGrid("testbed 30x30", true);

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
    start = new Position(20, 20, 0);
    drawPath(grid, new Stairs(1, 0,  start, 6, 0xFFEEDD));
    drawPath(grid, new Stairs(0, 1,  start, 6, 0xFFEEDD));
    drawPath(grid, new Stairs(-1, 0, start, 6, 0xFFEEDD));
    drawPath(grid, new Stairs(0, -1, start, 6, 0xFFEEDD));

    // Third set of steps: color varies by height
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

    // Fourth set of steps: color varies by column
    stairColors = new int[] {0xFEEDDF, 0xEEEDDE, 0xDEEDDD, 0xCEEDDC, 0xBEEDDB, 0xAEEDDA};
    start = new Position(5, 20, 0);
    drawPath(grid, new ColumnColorStairs(1, 0, start, stairColors));
    drawPath(grid, new ColumnColorStairs(0, 1, start, stairColors));
    drawPath(grid, new ColumnColorStairs(-1, 0, start, stairColors));
    drawPath(grid, new ColumnColorStairs(0, -1, start, stairColors));

    // Fifth: flat cross-hatch, with order-of-adds different on each line
    // TL to BR
    start = new Position(30, 0, 0);
    Position end = new Position(40, 10, 0);
    line(grid, start, end, 0xFFEEDD);

    // TR to BL
    start = new Position(40, 0, 0);
    end = new Position(30, 10, 0);
    line(grid, start, end, 0xFFEEDD);

    // BR to TL
    start = new Position(40, 8, 0);
    end = new Position(32, 0, 0);
    line(grid, start, end, 0xFFEEDD);

    // BL to TR
    start = new Position(30, 8, 0);
    end = new Position(38, 0, 0);
    line(grid, start, end, 0xFFEEDD);

    return grid;
  }

  /** Plot of an exaggerated sine. Note that for pleasing results, the origin is shifted. */
  public static Grid plotSin(final int mulitplyer) {
    // TODO at low multipliers the colors aren't distinct enough.
    MutableGrid grid = new SimpleGrid("Sine plot x" + mulitplyer + " 40x40");
    Plotter plotter = new Plotter() {
      @Override public int plot(int x, int y) {
        double distanceFromOrigin = Math.hypot(x, y);
        return (int) (mulitplyer * (Math.sin(distanceFromOrigin)));
      }
    };
    Voxels.plot(grid, plotter);
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
    Voxels.plot(grid, plotter);
    return grid;
  }

  public static Grid cube(int size, int color) {
    MutableGrid grid = new SimpleGrid("cube of size " + size, true);

    Position bnw = new Position(0, 0, 0);
    Position bne = new Position(size, 0, 0);
    Position bsw = new Position(0, size, 0);
    Position bse = new Position(size, size, 0);
    Position tnw = new Position(0, 0, size);
    Position tne = new Position(size, 0, size);
    Position tsw = new Position(0, size, size);
    Position tse = new Position(size, size, size);
    // Base:
    line(grid, bnw, bne, color);
    line(grid, bsw, bse, color);
    line(grid, bnw, bsw, color);
    line(grid, bne, bse, color);
    // Top:
    line(grid, tnw, tne, color);
    line(grid, tsw, tse, color);
    line(grid, tnw, tsw, color);
    line(grid, tne, tse, color);
    // Risers (these actually introduce duplicate voxels, should clean that up sometime):
    line(grid, bnw, tnw, color);
    line(grid, bne, tne, color);
    line(grid, bse, tse, color);
    line(grid, bsw, tsw, color);

    return grid;
  }

  /** 30x30 grid with all 900 voxels specified. */
  public static Grid fullRandomGrid() {
    MutableGrid grid = new SimpleGrid("random full 30x30");

    Random r = new Random();
    for (int i = 0; i < 900; i++) {
      int x = i / 30;
      int y = i % 30;
      boolean hasHeight = x % 5 == 0 && y % 5 == 0;
      int z = hasHeight ? r.nextInt(21) - 10 : 0;
      int color = 0x999999 + r.nextInt(0x666666);
      Voxel vox = new Voxel(x, y, z, color);
      grid.add(vox);

      // If not animated, and if positive z, make into a column.
      for (int j = 0; j < z; j++) {
        grid.add(new Voxel(x, y, j, color));
      }
    }

    return grid;
  }

  /** Creates a grid with two hundred towers of up to 50 voxels in a 30x30 grid. */
  public static Grid towers() {
    MutableGrid grid = new SimpleGrid("200 towers 30x30", true);
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

  public static Grid linkGrid() {
    Map<Character, Integer> colorMap = new HashMap<>(3);
    colorMap.put('G', 0xadfc14); // Green
    colorMap.put('S', 0xff8f2b); // Light brown
    colorMap.put('H', 0xdb450f); // Dark brown
    SimpleGrid grid = new SimpleGrid("link");
    fromTextMap(grid, new Position(0, 0, 40), ""
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
    return grid;
  }

  public static Grid morboCat() {
    Map<Character, Integer> colorMap = new HashMap<>(3);
    colorMap.put('*', 0x000000); // Black
    colorMap.put('@', 0x444444); // Gray
    colorMap.put('@', 0xbbbbbb); // Gray
    colorMap.put('y', 0xaaaa00); // Yellow
    SimpleGrid frame = new SimpleGrid("morbo");
    fromTextMap(frame, new Position(0, 0, 40), ""
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
    SimpleGrid frame1 = new SimpleGrid("mmr1");

    Position topLeft = new Position(0, 0, 40);
    fromTextMap(frame1, topLeft, "" // Frame 2 is 2px taller, so 1 and 3 need spacer rows
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
    SimpleGrid frame2 = new SimpleGrid("mmr2");
    fromTextMap(frame2, topLeft, ""
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
    SimpleGrid frame3 = new SimpleGrid("mmr3");
    fromTextMap(frame3, topLeft, ""
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

  /** Builds a few different 10 x 10 shapes. */
  public static Grid neighborTest() {
    SimpleGrid grid = new SimpleGrid("Neighbor test");
    Translation start = new Translation(5, 5, 0);
    Translation end = new Translation(5, 14, 0);
    // 10x10x10 cube
    for (int z = 1; z < 11; z++) {
      start.z = z;
      end.z = z;
      for (int x = 5; x < 15; x++) {
        start.x = x;
        end.x = x;
        line(grid, start.asPosition(), end.asPosition(), 0x2a2a2a * z / 2);
      }
    }

    // Draw a couple slightly-overlapping squares
    Random r = new Random();
    start.x = 20;
    start.z = 4;
    end.x = 29;
    end.z = 4;

    for (int y = 5; y < 15; y++) {
      start.y = y;
      end.y = y;
      int color = 0x999999 + r.nextInt(0x666666);
      // was 0x2a2a2a * start.z / 2
      line(grid, start.asPosition(), end.asPosition(), color);
    }

    start.y = 13;
    start.z = 5;
    end.y = 22;
    end.z = 5;
    for (int x = 28; x < 38; x++) {
      start.x = x;
      end.x = x;
      int color = 0x999999 + r.nextInt(0x666666);
      line(grid, start.asPosition(), end.asPosition(), color);
    }

    // Draw this one barely adjacent to the last, and in reverse order
    start.x = 29;
    start.z = 5;
    end.x = 20;
    end.z = 5;
    for (int y = 32; y > 22; y--) {
      start.y = y;
      end.y = y;
      int color = 0x999999 + r.nextInt(0x666666);
      line(grid, start.asPosition(), end.asPosition(), color);
    }

    return grid;
  }

  public static final List<Grid> GRIDS = Collections.unmodifiableList(Arrays.asList(
    neighborTest(),
    testBed(),
    plotSin(5),
    plotSin(2),
    plotHyperbola(.2),
    linkGrid(),
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
