package wannabe.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import wannabe.Rendered;

public enum RenderType {
  filledCircle {
    @Override void draw(Graphics g, Rendered r) {
      g.fillOval(r.left, r.top, r.size, r.size);
    }
  },
  filledRoundedSquare {
    @Override void draw(Graphics g, Rendered r) {
      int arc = r.size / 3;
      g.fillRoundRect(r.left, r.top, r.size, r.size, arc, arc);
    }
  },
  filledSquare {
    @Override void draw(Graphics g, Rendered r) {
      g.fillRect(r.left, r.top, r.size, r.size);
    }
  },
  filledThreeDSquare {
    @Override void draw(Graphics g, Rendered r) {
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledThreeDSquareWithCabinetSides {
    @Override void draw(Graphics g, Rendered r) {
      fillSides(g, r);
      if (!r.neighborAbove) g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledThreeDSquareWithCabinetWires {
    @Override void draw(Graphics g, Rendered r) {
      wireSides(g, r);
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledSquareWithCabinetSides {
    @Override void draw(Graphics g, Rendered r) {
      fillSides(g, r);
      if (!r.neighborAbove) g.fillRect(r.left, r.top, r.size, r.size);
    }
  },
  filledSquareWithCabinetWires {
    @Override void draw(Graphics g, Rendered r) {
      wireSides(g, r);
      g.fillRect(r.left, r.top, r.size, r.size);
    }
  },
  pixel {
    @Override void draw(Graphics g, Rendered r) {
      g.drawLine(r.left, r.top, r.left, r.top);
    }
  },
  circle {
    @Override void draw(Graphics g, Rendered r) {
      g.drawOval(r.left, r.top, r.size, r.size);
    }
  },
  square {
    @Override void draw(Graphics g, Rendered r) {
      g.drawRect(r.left, r.top, r.size, r.size);
    }
  },
  squareWithWireSides {
    @Override void draw(Graphics g, Rendered r) {
      wireSides(g, r);
      g.drawRect(r.left, r.top, r.size, r.size);
    }
  },
  solidWireSquare {
    final Rendered dark = new Rendered();
    {
      // Set up "background" color.
      // TODO this should probably be configurable.
      dark.color = Color.BLACK;
      dark.darkerColor = Color.BLACK;
    }

    @Override void draw(Graphics g, Rendered r) {
      // First erase what we're going to write on:
      // TODO we probably don't need this method, we could just pass colors to the populate methods.
      dark.duplicateWithoutColor(r);
      // Populate manually so that we only calculate it once.
      populateCubeSidesPolygon(r);
      fillSides(g, polygon, r.darkerColor, r.color);
      if (!r.neighborAbove) g.fillRect(r.left, r.top, r.size, r.size);
      // Now draw the cube:
      wireSides(g, polygon, r.darkerColor, r.color);
      if (!r.neighborAbove) g.drawRect(r.left, r.top, r.size, r.size);
    }
  },
  roundedSquare {
    @Override void draw(Graphics g, Rendered r) {
      int arc = r.size / 3;
      g.drawRoundRect(r.left, r.top, r.size, r.size, arc, arc);
    }
  },
  threeDSquare {
    @Override void draw(Graphics g, Rendered r) {
      g.draw3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  ;

  final Polygon polygon = new Polygon();

  void fillSides(Graphics g, Rendered r) {
    populateCubeSidesPolygon(r);
    fillSides(g, polygon, r.darkerColor, r.color);
  }

  void fillSides(Graphics g, Polygon polygon, Color darkerColor, Color color) {
    if (polygon.npoints == 0) return;
    g.setColor(darkerColor);
    g.fillPolygon(polygon);
    g.setColor(color);
    // Draw a line representing the edge of the side
    // TODO restore.  Now the number of points can be 0-8, so we need to be smarter.
    // Kinda cool: if I connect other points it adds texture to the sides.
//    g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
  }

  void wireSides(Graphics g, Rendered r) {
    populateCubeSidesPolygon(r);
    wireSides(g, polygon, r.darkerColor, r.color);
  }

  void wireSides(Graphics g, Polygon polygon, Color darkerColor, Color color) {
    if (polygon.npoints == 0) return;
    g.setColor(darkerColor);
    g.drawPolygon(polygon);
    // TODO restore.  Now the number of points can be 0-8, so we need to be smarter.
//    g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
    g.setColor(color);
  }

  /**
   * Computes a polygon representing the sides of a cube. Does no drawing.
   * For a lone cube with no neighbors, this would normally be shaped as follows.
   * The sides of the cube are described by a filled six-sided polygon with points
   * 1, 2, and 3 at the edge of the square and 4, 5, 6 offset by hDepth, vDepth
   * The first three points are based on which direction we indicate height.
   *
   * See the various populate methods for more details:
   * <ul>
   * <li> {@link #populateAboveLeft(Rendered)}
   * <li> {@link #populateAboveRight(Rendered)}
   * <li> {@link #populateBelowLeft(Rendered)}
   * <li> {@link #populateBelowRight(Rendered)}
   * </ul>
   */
  void populateCubeSidesPolygon(Rendered r) {
    polygon.reset();
    // Pick the first three points based on direction.
    if (r.hDepth < 0) {
      // Height indicated by drawing to the left of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-left the face.
        populateAboveLeft(r);
      } else {
        // Height indicated by drawing below-left the face.
        populateBelowLeft(r);
      }
    } else {
      // Height indicated by drawing to the right of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-right the face.
        populateAboveRight(r);
      } else {
        // Height indicated by drawing below-right the face.
        populateBelowRight(r);
      }
    }
  }

  /**
   * The presence or absence of three neighbors dictate which of eight cases are used to draw:
   * <p>
   * Case 1. No blocking neighbor case; 1, 2, and 3 all share points with the face:
   * <pre>
   * (5) *--------* (6)
   *     |         \
   *     |  *-------* (1)
   * (4) *  | (2)
   *      \ |
   *       \|
   *        * (3)
   * </pre>
   *
   * Cases 2 and 3. With a single neighbor blocking, the polygon would instead share only one
   * point (1) with the face. 2, 3, and 4 are determined by hdepth and vdepth.
   * <pre>
   * (3) *--------* (4)    (3) *--* (4)
   *     |         \           |  |
   * (2) *----------* (1)      |  |
   *                       (2) *  |
   *                            \ |
   *                             \|
   *                              * (1)
   * </pre>
   *
   * Case 4. With two adjacent blocking neighbors, the polygon remaining would be a small rectangle
   * sharing only * one corner (1) with the face.
   * <pre>
   * (3) *--* (4)
   *     |  |
   * (2) *--* (1)
   * </pre>
   *
   * Case 5. With three blocking neighbors (including the diagonal), draw nothing.
   * <p>
   * Cases 6 and 7. With an adjacent and a diagonal neighbor blocking, the polygon would share
   * 1 and 2 with the face. 3 and 4 are determined by hdepth and vdepth.  The small rectangle
   * shown (the only thing drawn in case 4) is elided.
   * <pre>
   *    (3)
   *  *--*-----* (4)        *--*
   *  |  |      \           |  |
   *  *--*-------* (1)  (4) *--* (1)
   *    (2)             (3) *  |
   *                         \ |
   *                          \|
   *                           * (2)
   * </pre>
   *
   * Case 8. Only diagonal blocking neighbor case; 1, 2, 3, and 6 (same as 2) all share points
   * with the face:
   * <pre>
   *    (7) *-----* (8)
   *        |      \
   * (5) *--*-------* (1)
   * (4) *  | (2, 6)
   *      \ |
   *       \|
   *        * (3)
   * </pre>
   *
   * Note that there are only ever 8 different points in all these cases.
   */
  public void populateAboveLeft(Rendered r) {
    int bottom = r.top + r.size;
    int right = r.left + r.size;
    int outTop = r.top + r.vDepth;
    int outLeft = r.left + r.hDepth;
    if (r.neighborNorth) {
      if (r.neighborWest) {
        if (!r.neighborNorthWest) {
          // Case 4. Just a small square here.
          polygon.addPoint(r.left, r.top);
          polygon.addPoint(outLeft, r.top);
          polygon.addPoint(outLeft, outTop);
          polygon.addPoint(r.left, outTop);
        } else {
          // Case 5, fully blocked.  Do nothing.
          // TODO ^ This isn't great if depths > pixelSize though.
        }

      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(r.left, bottom);
        polygon.addPoint(outLeft, bottom + r.vDepth);
        if (r.neighborNorthWest) {
          // Case 7. Corner rectangle is blocked, shorten this. Point sequence per diagram
          // is 2, 3, 4, 1 in order to use the first two common points.
          polygon.addPoint(outLeft, r.top);
          polygon.addPoint(r.left, r.top);
        } else {
          // Case 3.
          polygon.addPoint(outLeft, outTop);
          polygon.addPoint(r.left, outTop);
        }
      }

    } else if (r.neighborWest) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(right, r.top);
      if (r.neighborNorthWest) {
        // Case 6.
        polygon.addPoint(r.left, r.top);
        polygon.addPoint(r.left, outTop);
      } else {
        // Case 2.
        polygon.addPoint(outLeft, r.top);
        polygon.addPoint(outLeft, outTop);
      }
      polygon.addPoint(right + r.hDepth, outTop);

    } else {
      // Draw both sides.
      polygon.addPoint(right, r.top);
      polygon.addPoint(r.left, r.top);
      polygon.addPoint(r.left, bottom);
      polygon.addPoint(outLeft, bottom + r.vDepth);

      if (r.neighborNorthWest) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(outLeft, r.top);
        polygon.addPoint(r.left, r.top);
        polygon.addPoint(r.left, outTop);
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outLeft, outTop);
      }
      polygon.addPoint(right + r.hDepth, outTop);
    }
  }

  /** See javadoc on {@link #populateAboveLeft(Rendered)} for details. */
  public void populateBelowRight(Rendered r) {
    int bottom = r.top + r.size;
    int right = r.left + r.size;
    int outRight = right + r.hDepth;
    int outBottom = bottom + r.vDepth;

    if (r.neighborSouth) {
      if (r.neighborEast) {
        if (!r.neighborSouthEast) {
          // Case 4. Just a small square here.
          polygon.addPoint(right, bottom);
          polygon.addPoint(outRight, bottom);
          polygon.addPoint(outRight, outBottom);
          polygon.addPoint(right, outBottom);
        } else {
          // Case 5, fully blocked.  Do nothing.
          // TODO ^ This isn't great if depths > pixelSize though.
        }

      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(right, r.top);
        polygon.addPoint(outRight, r.top + r.vDepth);
        if (r.neighborSouthEast) {
          // Case 7. Corner rectangle is blocked, shorten this. Point sequence per diagram
          // is 2, 3, 4, 1 in order to use the first two common points.
          polygon.addPoint(outRight, bottom);
          polygon.addPoint(right, bottom);
        } else {
          // Case 3.
          polygon.addPoint(outRight, outBottom);
          polygon.addPoint(right, outBottom);
        }
      }

    } else if (r.neighborEast) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(r.left, bottom);
      if (r.neighborSouthEast) {
        // Case 6.
        polygon.addPoint(right, bottom);
        polygon.addPoint(right, outBottom);
      } else {
        // Case 2.
        polygon.addPoint(outRight, bottom);
        polygon.addPoint(outRight, outBottom);
      }
      polygon.addPoint(r.left + r.hDepth, outBottom);

    } else {
      // Draw both sides.


      polygon.addPoint(r.left, bottom);
      polygon.addPoint(right, bottom);
      polygon.addPoint(right, r.top);
      polygon.addPoint(outRight, r.top + r.vDepth);

      if (r.neighborSouthEast) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(outRight, bottom);
        polygon.addPoint(right, bottom);
        polygon.addPoint(right, outBottom);
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outRight, outBottom);
      }
      polygon.addPoint(r.left + r.hDepth, outBottom);
    }
  }

  /** See javadoc on {@link #populateAboveLeft(Rendered)} for details. */
  public void populateAboveRight(Rendered r) {
    int bottom = r.top + r.size;
    int right = r.left + r.size;
    polygon.addPoint(right, bottom);
    polygon.addPoint(right, r.top);
    polygon.addPoint(r.left, r.top);
    int outRight = right + r.hDepth;
    int outTop = r.top + r.vDepth;
    polygon.addPoint(r.left + r.hDepth, outTop);
    polygon.addPoint(outRight, outTop);
    polygon.addPoint(outRight, bottom + r.vDepth);
  }

  /** See javadoc on {@link #populateAboveLeft(Rendered)} for details. */
  public void populateBelowLeft(Rendered r) {
    int bottom = r.top + r.size;
    int right = r.left + r.size;
    polygon.addPoint(r.left, r.top);
    polygon.addPoint(r.left, bottom);
    polygon.addPoint(right, bottom);
    int outBottom = bottom + r.vDepth;
    int outLeft = r.left+ r.hDepth;
    polygon.addPoint(right + r.hDepth, outBottom);
    polygon.addPoint(outLeft, outBottom);
    polygon.addPoint(outLeft, r.top + r.vDepth);
  }

  abstract void draw(Graphics g, Rendered r);

  public RenderType next() {
    RenderType[] values = values();
    int next = ordinal() + 1;
    return next != values.length ? values[next] : values[0];
  }
}
