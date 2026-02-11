package wannabe.util

object UIs {
  fun pxToCells(
    lengthPx: Int,
    cellSizePx: Int
  ): Int {
    return lengthPx / cellSizePx
  }
}
