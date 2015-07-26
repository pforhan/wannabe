// Copyright 2013 Patrick Forhan.
package wannabe;

public class Position implements Cloneable {
  public int x;
  public int y;
  public int z;

  public Position(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void add(Position offset) {
    x += offset.x;
    y += offset.y;
    z += offset.z;
  }

  public void set(Position position) {
    x = position.x;
    y = position.y;
    z = position.z;
  }

  public void zero() {
    x = 0;
    y = 0;
    z = 0;
  }

  public boolean isZero() {
    return x == 0 && y == 0 && z == 0;
  }

  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    result = prime * result + z;
    return result;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Position other = (Position) obj;
    return x == other.x && y == other.y && z == other.z;
  }

  @Override public String toString() {
    return "[x=" + x + ", y=" + y + ", z=" + z + "]";
  }

  @Override public Position clone() {
    try {
      return (Position) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new IllegalStateException("Could not clone cloneable object");
    }
  }

}
