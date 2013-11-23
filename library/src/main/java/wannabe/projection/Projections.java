// Copyright 2013 Patrick Forhan.
package wannabe.projection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Converts a 3d Position to a 2d coordinate for rendering. */
public class Projections {

  private static final Projection[] PROJECTION_ARRAY = new Projection[] {
      new Isometric(),
      new Flat(),
      new PseudoPerspective(),
  };
  public static final List<Projection> PROJECTIONS =
      Collections.unmodifiableList(Arrays.asList(PROJECTION_ARRAY));

  public static Projection next(Projection current) {
    int nextIdx = PROJECTIONS.indexOf(current) + 1;
    return PROJECTIONS.get(nextIdx < PROJECTIONS.size() ? nextIdx : 0);
  }
}