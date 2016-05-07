package wannabe;

/** Effectively the same as a {@link Position} but mutable. */
public class Translation implements Placed {
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

  public Translation(Placed placed) {
    set(placed);
  }

  public Translation add(Translation offset) {
    x += offset.x;
    y += offset.y;
    z += offset.z;
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

  @Override public Position asPosition() {
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

  public Translation set(Placed placed) {
    x = placed.x();
    y = placed.y();
    z = placed.z();
    return this;
  }

  public Translation zero() {
    x = 0;
    y = 0;
    z = 0;
    return this;
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
    Translation other = (Translation) obj;
    return x == other.x && y == other.y && z == other.z;
  }

  @Override public String toString() {
    return "addr[x=" + x + ", y=" + y + ", z=" + z + "]";
  }

}
