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

  @Override public Position clone() {
    try {
      return (Position) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new IllegalStateException("Could not clone cloneable object");
    }
  }
}
