package wannabe;

public class Position implements Placed {
  public static final Position ZERO = new Position(0, 0, 0);

  public final int x;
  public final int y;
  public final int z;

  public Position(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override public Position asPosition() {
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
    return "pos[x=" + x + ", y=" + y + ", z=" + z + "]";
  }

}
