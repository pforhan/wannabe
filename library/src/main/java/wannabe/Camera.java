package wannabe;

/** Movable view of the playfield. */
public class Camera {
  /** The position of this camera in the voxel space. */
  public final Translation position;
  /** This position of this camera in the UI (pixel) space. The size value is ignored. */
  public final Rendered uiPosition = new Rendered();
  private final Translation translated = new Translation(0, 0, 0);
  public boolean isDirty;

  public Camera(int x, int y, int z) {
    position = new Translation(x, y, z);
  }

  /**
   * Translate a position based on the camera's position. Note: the returned reference is reused on
   * every call to translate().
   */
  public Translation translate(Position toTranslate) {
    translated.x = toTranslate.x - position.x;
    translated.y = toTranslate.y - position.y;
    translated.z = toTranslate.z - position.z;
    return translated;
  }
}
