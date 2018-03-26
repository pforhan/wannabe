package wannabe.projection;

/** Converts a 3d Position to a 2d coordinate for rendering. */
public enum Projections {
  TOP_LEFT_5(new Cabinet(5, 5)),
  TOP_RIGHT_5(new Cabinet(-5, 5)),
  BOTTOM_LEFT_5(new Cabinet(5, -5)),
  BOTTOM_RIGHT_5(new Cabinet(-5, -5)),
  TOP_LEFT_1(new Cabinet(1, 1)),
  TOP_RIGHT_1(new Cabinet(-1, 1)),
  BOTTOM_LEFT_1(new Cabinet(1, -1)),
  BOTTOM_RIGHT_1(new Cabinet(-1, -1)),
  FLAT(new Flat()),
  PSEUDO_PERSPECTIVE(new PseudoPerspective()),
  ;

  public final Projection projection;

  Projections(Projection projection) {
    this.projection = projection;
  }

  public Projections next() {
    int nextIdx = ordinal() + 1;
    Projections[] all = values();
    return all[nextIdx < all.length ? nextIdx : 0];
  }

  public static Projections withProjection(Projection search) {
    Projections[] all = values();
    for (Projections projections : all) {
      if (projections.projection.equals(search)) {
        return projections;
      }
    }
    throw new IllegalArgumentException(
        "Did not find projection " + search + " here.");
  }
}
