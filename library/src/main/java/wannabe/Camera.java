// Copyright 2013 Square, Inc.
package wannabe;

/** Movable view of the playfield. */
public class Camera {
  public final Position position;
  private final Position translated = new Position();

  public Camera(int x, int y, int z) {
    position = new Position(x, y, z);
  }

  /**
   * Translate a position based on the camera's position.  Note: the returned reference is
   * reused on every call to translate().
   */
  public Position translate(Position toTranslate) {
    translated.x = toTranslate.x - position.x;
    translated.y = toTranslate.y - position.y;
    translated.z = toTranslate.z - position.z;
    return translated;
  }
}
