package wannabe.grid;

import java.util.Iterator;
import wannabe.Bounds;
import wannabe.Position;
import wannabe.Translation;
import wannabe.Voxel;

/** A Grid that applies a rotation function to a source grid and caches the results. */
public class RotateGrid implements Grid {
  private final String name;
  private final Grid source;
  private final SimpleGrid dest = new SimpleGrid("Rotated");
  private final Translation translation = new Translation(0, 0, 0);

  private boolean dirty = true;
  private int xDegrees;
  private int yDegrees;
  private int zDegrees;

  public RotateGrid(String name, Grid source) {
    this.name = name;
    this.source = source;
  }

  @Override public Iterator<Voxel> iterator() {
    maybeClearAndFillDest();
    dirty = false;

    Iterator realIterator = dest.iterator();
    return translation.isZero() ? realIterator : new TranslatingIterator(realIterator, translation);
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds, boolean exportHidden) {
    maybeClearAndFillDest();
    dirty = false;

    if (translation.isZero()) {
      // We can skip cloning and translation.
      dest.exportTo(grid, bounds, exportHidden);
      return;
    }

    // Otherwise, we have to translate all the things.
    dest.translate(translation);
    dest.exportTo(grid, bounds, exportHidden);
    dest.clearTranslation();
    // Clear the dirty flag. TODO there's bound to be a better way.
    dest.iterator();
  }

  @Override public boolean isDirty() {
    maybeClearAndFillDest();
    return rawIsDirty();
  }

  @Override public int size() {
    maybeClearAndFillDest();
    return dest.size();
  }

  @Override public void translate(Translation offset) {
    dirty = true;
    translation.add(offset);
  }

  @Override public void clearTranslation() {
    dirty = true;
    translation.zero();
  }

  public void setRotate(int xDegrees, int yDegrees, int zDegrees) {
    this.xDegrees = xDegrees;
    this.yDegrees = yDegrees;
    this.zDegrees = zDegrees;
    dirty = true;
  }

  private void maybeClearAndFillDest() {
    if (rawIsDirty()) {
      dest.clear();
      for (Voxel voxel : source) {
        Position old = voxel.position;

        // from https://www.opengl.org/discussion_boards/showthread.php/139444-Easiest-way-to-rotate-point-in-3d-using-trig
        // only rotates about the x and y axes
        // TODO let a matrix lib handle this
        double xRad = Math.toRadians(xDegrees);
        double yRad = Math.toRadians(yDegrees);
        double zRad = Math.toRadians(zDegrees); // not used, probably the 0 term
        int newX = (int) (Math.cos(yRad) * old.x
                        + Math.sin(yRad) * Math.sin(xRad) * old.y
                        - Math.sin(yRad) * Math.cos(xRad) * old.z);

        int newY = (int) (0 + Math.cos(xRad) * old.y + Math.sin(xRad) * old.z);

        int newZ = (int) (Math.sin(yRad) * old.x
                        + Math.cos(yRad) * -Math.sin(xRad) * old.y
                        + Math.cos(yRad) * Math.cos(xRad) * old.z);

        dest.put(new Voxel(newX, newY, newZ, voxel.value));
      }
    }
  }

  private boolean rawIsDirty() {
    return dirty || dest.isDirty();
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    maybeClearAndFillDest();
    return dest.neighbors(voxel);
  }

  @Override public String toString() {
    return name + "; rotx " + xDegrees + " roty " + yDegrees + " rotz " + zDegrees + "(size: " + size() + ")";
  }
}
