// Copyright 2015 Patrick Forhan
package wannabe;

public interface Bounds {
  public boolean contains(Position position);
  // TODO make an XYZ bounds too, of course.

  public class XYBounds implements Bounds {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public void setWithWidthHeight(int x, int y, int width, int height) {
      left = x;
      top = y;
      right = x + width;
      bottom = y + height;
    }

    public void setWithAbsolute(int left, int top, int right, int bottom) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
    }

    @Override public boolean contains(Position pos) {
      return pos.x >= left && pos.x < right
          && pos.y >= top && pos.y < bottom;
    }
  }
}
