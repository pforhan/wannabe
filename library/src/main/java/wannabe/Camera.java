// Copyright 2013 Patrick Forhan.
package wannabe;

/** Movable view of the playfield. */
public class Camera {
  /** The position of this camera in the voxel space. */
  public final Position position;
  /** This position of this camera in the UI (pixel) space. The size value is ignored. */
  public final Rendered uiPosition = new Rendered();
  private final Position translated = new Position(0, 0, 0);

  public Camera(int x, int y, int z) {
    position = new Position(x, y, z);
  }

  /**
   * Translate a position based on the camera's position. Note: the returned reference is reused on
   * every call to translate().
   */
  public Position translate(Position toTranslate) {
    translated.x = toTranslate.x - position.x;
    translated.y = toTranslate.y - position.y;
    translated.z = toTranslate.z - position.z;
    return translated;
  }
}
