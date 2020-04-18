package wannabe;

/**
 * Effectively the same as a {@link Position} but mutable. Produces the same hashcode and equals
 * results as a Position for the same values of x, y, and z.
 */
public class Translation implements Pos {
  public int x;
  public int y;
  public int z;

  public Translation(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Translation(Translation position) {
    set(position);
  }

  public Translation(Position position) {
    set(position);
  }

  public Translation add(Translation offset) {
    x += offset.x;
    y += offset.y;
    z += offset.z;
    return this;
  }

  public Position asPosition() {
    return new Position(x, y, z);
  }

  public Translation set(Translation position) {
    x = position.x;
    y = position.y;
    z = position.z;
    return this;
  }

  public Translation set(Position position) {
    x = position.x;
    y = position.y;
    z = position.z;
    return this;
  }

  public Translation zero() {
    x = 0;
    y = 0;
    z = 0;
    return this;
  }

  @Override public int x() {
    return x;
  }

  @Override public int y() {
    return y;
  }

  @Override public int z() {
    return z;
  }

  @Override public boolean isZero() {
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
    if (obj instanceof Translation) {
      Translation other = (Translation) obj;
      return x == other.x && y == other.y && z == other.z;
    } else if (obj instanceof Position) {
      Position other = (Position) obj;
      return x == other.x && y == other.y && z == other.z;
    }
    return false;
  }

  @Override public String toString() {
    return "translation[x=" + x + ", y=" + y + ", z=" + z + "]";
  }

}
