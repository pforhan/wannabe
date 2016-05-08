package wannabe;

public class Position {
  public static final Position ZERO = new Position(0, 0, 0);

  public final int x;
  public final int y;
  public final int z;

  public Position(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
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
    if (obj instanceof Position) {
      Position other = (Position) obj;
      return x == other.x && y == other.y && z == other.z;
    } else if (obj instanceof Translation) {
      Translation other = (Translation) obj;
      return x == other.x && y == other.y && z == other.z;
    }
    return false;
  }

  @Override public String toString() {
    return "pos[x=" + x + ", y=" + y + ", z=" + z + "]";
  }

}
