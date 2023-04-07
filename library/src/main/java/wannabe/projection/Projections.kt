package wannabe.projection

/** Converts a 3d Position to a 2d coordinate for rendering.  */
enum class Projections(val projection: Projection) {
  TOP_LEFT_5(Cabinet(5, 5)),
  TOP_RIGHT_5(Cabinet(-5, 5)),
  BOTTOM_LEFT_5(Cabinet(5, -5)),
  BOTTOM_RIGHT_5(Cabinet(-5, -5)),
  TOP_LEFT_1(Cabinet(1, 1)),
  TOP_RIGHT_1(Cabinet(-1, 1)),
  BOTTOM_LEFT_1(Cabinet(1, -1)),
  BOTTOM_RIGHT_1(Cabinet(-1, -1)),
  H_SLIDING_CABINET_1(Cabinet.horizontalSequence(1, -5, 5, true)),
  H_SLIDING_CABINET_5(Cabinet.horizontalSequence(5, -5, 5, true)),
  PSEUDO_PERSPECTIVE(PseudoPerspective()),
  FLAT(Flat());
  
  override fun toString() = projection.toString()

  operator fun next(): Projections {
    val nextIdx = ordinal + 1
    val all = values()
    return all[if (nextIdx < all.size) nextIdx else 0]
  }

  companion object {
    fun withProjection(search: Projection): Projections {
      val all = values()
      for (projections in all) {
        if (projections.projection == search) {
          return projections
        }
      }
      throw IllegalArgumentException(
          "Did not find projection $search here."
      )
    }
  }

}
