package wannabe.projection;

import java.util.Arrays;
import java.util.List;

/** Converts a 3d Position to a 2d coordinate for rendering. */
public class Projections {

  public static final List<Projection> PROJECTIONS =
      Arrays.asList(
          new Cabinet(5, 5),
          new Cabinet(-5, 5),
          new Cabinet(5, -5),
          new Cabinet(-5, -5),
          new Cabinet(1, 1),
          new Cabinet(-1, 1),
          new Cabinet(1, -1),
          new Cabinet(-1, -1),
          new Flat(),
          new PseudoPerspective());

  public static Projection next(Projection current) {
    int nextIdx = PROJECTIONS.indexOf(current) + 1;
    return PROJECTIONS.get(nextIdx < PROJECTIONS.size() ? nextIdx : 0);
  }
}
