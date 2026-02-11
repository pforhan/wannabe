package wannabe

/** Movable view of the playfield.  */
class Camera(
  x: Int = 0,
  y: Int = 0,
  z: Int = 0
) {
  /** The position of this camera in the voxel space.  */
  val position = Translation(x, y, z)

  /** This position of this camera in the UI (pixel) space. The size value is ignored.  */
  val uiPosition = Projected()

  private val translated = Translation(0, 0, 0)
  var isDirty = false

  /**
   * Translate a position based on the camera's position. Note: the returned reference is reused on
   * every call to translate().
   */
  fun translate(toTranslate: Position): Translation {
    translated.x = toTranslate.x - position.x
    translated.y = toTranslate.y - position.y
    translated.z = toTranslate.z - position.z
    return translated
  }

  // TODO wrong in so many ways. Where's the 25 come from?
  // size of the demo app playfield maybe?
  fun reverseTranslate(toTranslate: Position): Translation {
    translated.x = toTranslate.x + position.x - 25
    translated.y = toTranslate.y + position.y - 25
    translated.z = toTranslate.z + position.z
    return translated
  }
}
