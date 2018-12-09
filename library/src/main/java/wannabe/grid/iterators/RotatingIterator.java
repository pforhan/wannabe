package wannabe.grid.iterators;

import java.util.Iterator;
import wannabe.Position;
import wannabe.Voxel;

public class RotatingIterator implements Iterator<Voxel> {
  public static class RotationDegrees implements Cloneable {
    public int x;
    public int y;
    public int z;

    boolean isZero() {
      return x == 0 && y == 0 && z == 0;
    }

    // TODO probably push more logic into this class directly, sort of like Translation

    @Override public String toString() {
      return "RotationDegrees{" +
          "x=" + x +
          ", y=" + y +
          ", z=" + z +
          '}';
    }

    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override public RotationDegrees clone() {
      try {
        return (RotationDegrees) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private final Iterator<Voxel> realIterator;
  private final double xRad;
  private final double yRad;
  private final boolean noRotate;

  public RotatingIterator(Iterator<Voxel> realIterator, RotationDegrees rotation) {
    this.realIterator = realIterator;
    noRotate = rotation.isZero();
    xRad = Math.toRadians(rotation.x);
    yRad = Math.toRadians(rotation.y);
    //zRad = Math.toRadians(rotation.z); // not used, probably the 0 term
  }

  @Override public boolean hasNext() {
    return realIterator.hasNext();
  }

  @Override public Voxel next() {
    Voxel real = realIterator.next();

    // Special case, if we have nothing to do just use the real voxel.
    if (noRotate) {
      return real;
    }

    Position old = real.position;

    // from https://www.opengl.org/discussion_boards/showthread.php/139444-Easiest-way-to-rotate-point-in-3d-using-trig
    // only rotates about the x and y axes
    // TODO let a matrix lib handle this
    int newX = (int) (Math.cos(yRad) * old.x
        + Math.sin(yRad) * Math.sin(xRad) * old.y
        - Math.sin(yRad) * Math.cos(xRad) * old.z);

    int newY = (int) (0 + Math.cos(xRad) * old.y + Math.sin(xRad) * old.z);

    int newZ = (int) (Math.sin(yRad) * old.x
        + Math.cos(yRad) * -Math.sin(xRad) * old.y
        + Math.cos(yRad) * Math.cos(xRad) * old.z);

    return new Voxel(newX, newY, newZ, real.value);
  }

  @Override public void remove() {
    // TODO should I support remove?
    realIterator.remove();
  }
}
