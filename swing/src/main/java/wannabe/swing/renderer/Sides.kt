package wannabe.swing.renderer

import wannabe.Projected
import wannabe.swing.SwingProjected
import java.awt.Color
import java.awt.Graphics
import java.awt.Polygon

/** * Utility class for drawing sides of cubes.  */
internal class Sides {
  private val polygon = Polygon()
  fun fillSides(
    g: Graphics,
    r: SwingProjected
  ) {
    populateCubeSidesPolygon(r)
    fillSides(g, r.darkerColor, r.color)
  }

  fun fillSides(
    g: Graphics,
    darkerColor: Color?,
    color: Color?
  ) {
    if (polygon.npoints == 0) return
    g.color = darkerColor
    g.fillPolygon(polygon)
    g.color = color
    // Draw a line representing the edge of the side
    if (polygon.npoints == 6) {
      // We only care about drawing this edge in case 1, which is the only case with 6 points.
      // Connect point 2 to point 5.
      g.drawLine(
          polygon.xpoints[1], polygon.ypoints[1],  //
          polygon.xpoints[4], polygon.ypoints[4]
      )
    }
    // Kinda cool: if I connect other points it adds texture to the sides.
  }

  fun wireSides(
    g: Graphics,
    r: SwingProjected
  ) {
    populateCubeSidesPolygon(r)
    wireSides(g, r.darkerColor, r.color)
  }

  fun wireSides(
    g: Graphics,
    darkerColor: Color?,
    color: Color?
  ) {
    if (polygon.npoints == 0) return
    g.color = darkerColor
    g.drawPolygon(polygon)
    // Draw a line representing the edge of the side
    if (polygon.npoints == 6) {
      // We only care about drawing this edge in case 1, which is the only case with 6 points.
      // Connect point 2 to point 5.
      g.drawLine(
          polygon.xpoints[1], polygon.ypoints[1],  //
          polygon.xpoints[4], polygon.ypoints[4]
      )
    }
    g.color = color
  }

  /**
   * Computes a polygon representing the sides of a cube. Does no drawing.
   * For a lone cube with no neighbors, this would normally be shaped as follows.
   * The sides of the cube are described by a filled six-sided polygon with points
   * 1, 2, and 3 at the edge of the square and 4, 5, 6 offset by hDepth, vDepth
   * The first three points are based on which direction we indicate height.
   *
   * See the various populate methods for more details:
   *
   *  *  [.populateAboveLeft]
   *  *  [.populateAboveRight]
   *  *  [.populateBelowLeft]
   *  *  [.populateBelowRight]
   *
   */
  fun populateCubeSidesPolygon(r: Projected) {
    polygon.reset()
    // Pick the first three points based on direction.
    if (r.hDepth < 0) {
      // Height indicated by drawing to the left of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-left the face.
        populateAboveLeft(r)
      } else {
        // Height indicated by drawing below-left the face.
        populateBelowLeft(r)
      }
    } else {
      // Height indicated by drawing to the right of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-right the face.
        populateAboveRight(r)
      } else {
        // Height indicated by drawing below-right the face.
        populateBelowRight(r)
      }
    }
  }

  /**
   * The presence or absence of three neighbors dictate which of eight cases are used to draw:
   *
   *  1. No neighbors
   *  1. Neighbor to the left
   *  1. Neighbor above
   *  1. Neighbor left and above
   *  1. Neighbor left, above, and above-left
   *  1. Neighbor left and above-left
   *  1. Neighbor above and above-left
   *  1. Neighbor above-left
   *
   * ```
   *  1 22 3  4 55 6  77 8
   *       3 44 55 66  7  8
   * ```
   *
   * TODO might be nice to enumerate all possible points too, and use the same point #s in
   * each case.
   *
   *
   *
   * Case 1. No blocking neighbor case; 1, 2, and 3 all share points with the face:
   * ```
   * (5) *--------* (6)
   *     |         \
   *     |  *-------* (1)
   * (4) *  | (2)
   *      \ |
   *       \|
   *        * (3)
   * ```
   *
   * Cases 2 and 3. With a single neighbor blocking, the polygon would instead share two
   * points (1, 2) with the face. 3, and 4 are determined by hdepth and vdepth.
   * Note that the areas marked with x are omitted because the blocking neighbor will
   * draw there.
   * ```
   * (3) *--------* (4)    (4) *\ x
   *     x\        \           | \
   *   (2) *--------* (1)      |  * (1)
   *                       (3) *  |
   *                            \ |
   *                             \|
   *                              * (2)
   * ```
   *
   * Cases 4 and 5. With two adjacent blocking neighbors, each neighbor will draw its sides, leaving
   * no space for this one. If there's a diagonal here, its face will cover the area anyway.
   *
   *
   *
   * Cases 6 and 7. With an adjacent and a diagonal neighbor blocking, the polygon would share
   * 1 and 2 with the face. 3 and 4 are determined by hdepth and vdepth.  The small rectangle
   * shown with xx is elided.
   * ```
   *    (3)
   *  *--*-----* (4)        *--*
   *  |xx|      \           |xx|
   *  *--*-------* (1)  (4) *--* (1)
   *    (2)             (3) *  |
   *                         \ |
   *                          \|
   *                           * (2)
   * ```
   *
   * Case 8. Only diagonal blocking neighbor case; 1, 2, 3, and 6 (same as 2) all share points
   * with the face:
   * TODO can this simplify to a 6-point polygon (1, 5, 4, 3, 7, 8, 1)?
   * ```
   *    (7) *-----* (8)
   *        |      \
   * (5) *--*-------* (1)
   * (4) *  | (2, 6)
   *      \ |
   *       \|
   *        * (3)
   * ```
   *
   * Note that there are at most 8 points in any cases.
   */
  fun populateAboveLeft(r: Projected) {
    val bottom = r.top + r.size
    val right = r.left + r.size
    val outTop = r.top + r.vDepth
    val outLeft = r.left + r.hDepth
    if (r.neighborNorth) {
      if (r.neighborWest) {
        // Cases 4 & 5, blocked. Do nothing.
      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(r.left, r.top)
        polygon.addPoint(r.left, bottom)
        polygon.addPoint(outLeft, bottom + r.vDepth)
        if (r.neighborNorthWest) {
          // Case 7. Corner rectangle is blocked, shorten this.
          polygon.addPoint(outLeft, r.top)
        } else {
          // Case 3.
          polygon.addPoint(outLeft, outTop)
        }
      }
    } else if (r.neighborWest) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(right, r.top)
      polygon.addPoint(r.left, r.top)
      if (r.neighborNorthWest) {
        // Case 6.
        polygon.addPoint(r.left, outTop)
      } else {
        // Case 2.
        polygon.addPoint(outLeft, outTop)
      }
      polygon.addPoint(right + r.hDepth, outTop)
    } else {
      // Draw both sides.
      polygon.addPoint(right, r.top)
      polygon.addPoint(r.left, r.top)
      polygon.addPoint(r.left, bottom)
      polygon.addPoint(outLeft, bottom + r.vDepth)
      if (r.neighborNorthWest) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(outLeft, r.top)
        polygon.addPoint(r.left, r.top)
        polygon.addPoint(r.left, outTop)
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outLeft, outTop)
      }
      polygon.addPoint(right + r.hDepth, outTop)
    }
  }

  /** See javadoc on [.populateAboveLeft] for details.  */
  fun populateBelowRight(r: Projected) {
    val bottom = r.top + r.size
    val right = r.left + r.size
    val outRight = right + r.hDepth
    val outBottom = bottom + r.vDepth
    if (r.neighborSouth) {
      if (r.neighborEast) {
        // Case 4 & 5, blocked. Do nothing.
      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(right, bottom)
        polygon.addPoint(right, r.top)
        polygon.addPoint(outRight, r.top + r.vDepth)
        if (r.neighborSouthEast) {
          // Case 7. Corner rectangle is blocked, shorten this.
          polygon.addPoint(outRight, bottom)
        } else {
          // Case 3.
          polygon.addPoint(outRight, outBottom)
        }
      }
    } else if (r.neighborEast) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(r.left, bottom)
      polygon.addPoint(right, bottom)
      if (r.neighborSouthEast) {
        // Case 6.
        polygon.addPoint(right, outBottom)
      } else {
        // Case 2.
        polygon.addPoint(outRight, outBottom)
      }
      polygon.addPoint(r.left + r.hDepth, outBottom)
    } else {
      // Draw both sides.
      polygon.addPoint(r.left, bottom)
      polygon.addPoint(right, bottom)
      polygon.addPoint(right, r.top)
      polygon.addPoint(outRight, r.top + r.vDepth)
      if (r.neighborSouthEast) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(outRight, bottom)
        polygon.addPoint(right, bottom)
        polygon.addPoint(right, outBottom)
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outRight, outBottom)
      }
      polygon.addPoint(r.left + r.hDepth, outBottom)
    }
  }

  /** See javadoc on [.populateAboveLeft] for details.  */
  fun populateAboveRight(r: Projected) {
    val bottom = r.top + r.size
    val right = r.left + r.size
    val outRight = right + r.hDepth
    val outTop = r.top + r.vDepth
    if (r.neighborNorth) {
      if (r.neighborEast) {
        // Case 4 & 5, blocked. Do nothing.
      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(right, bottom)
        polygon.addPoint(right, r.top)
        if (r.neighborNorthEast) {
          // Case 7. Corner rectangle is blocked, shorten this.
          polygon.addPoint(outRight, r.top)
        } else {
          // Case 3.
          polygon.addPoint(outRight, outTop)
        }
        polygon.addPoint(outRight, bottom + r.vDepth)
      }
    } else if (r.neighborEast) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(r.left, r.top)
      polygon.addPoint(right, r.top)
      if (r.neighborNorthEast) {
        // Case 6.
        polygon.addPoint(right, outTop)
      } else {
        // Case 2.
        polygon.addPoint(outRight, outTop)
      }
      polygon.addPoint(r.left + r.hDepth, outTop)
    } else {
      // Draw both sides.
      polygon.addPoint(right, bottom)
      polygon.addPoint(right, r.top)
      polygon.addPoint(r.left, r.top)
      polygon.addPoint(r.left + r.hDepth, outTop)
      if (r.neighborNorthEast) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(right, outTop)
        polygon.addPoint(right, r.top)
        polygon.addPoint(outRight, r.top)
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outRight, outTop)
      }
      polygon.addPoint(outRight, bottom + r.vDepth)
    }
  }

  /** See javadoc on [.populateAboveLeft] for details.  */
  fun populateBelowLeft(r: Projected) {
    // TODO fix this up like the others
    val bottom = r.top + r.size
    val right = r.left + r.size
    val outBottom = bottom + r.vDepth
    val outLeft = r.left + r.hDepth
    if (r.neighborSouth) {
      if (r.neighborWest) {
        // Case 4 & 5, blocked. Do nothing.
      } else {
        // Blocked only vertically, draw horizontal part only.
        polygon.addPoint(r.left, bottom)
        polygon.addPoint(r.left, r.top)
        polygon.addPoint(outLeft, r.top + r.vDepth)
        if (r.neighborSouthWest) {
          // Case 7. Corner rectangle is blocked, shorten this.
          polygon.addPoint(outLeft, bottom)
        } else {
          // Case 3.
          polygon.addPoint(outLeft, outBottom)
        }
      }
    } else if (r.neighborWest) {
      // Blocked only horizontally, draw vertical part only.
      polygon.addPoint(right, bottom)
      polygon.addPoint(r.left, bottom)
      if (r.neighborSouthWest) {
        // Case 6.
        polygon.addPoint(r.left, outBottom)
      } else {
        // Case 2.
        polygon.addPoint(outLeft, outBottom)
      }
      polygon.addPoint(right + r.hDepth, outBottom)
    } else {
      // Draw both sides.
      polygon.addPoint(r.left, r.top)
      polygon.addPoint(r.left, bottom)
      polygon.addPoint(right, bottom)
      polygon.addPoint(right + r.hDepth, outBottom)
      if (r.neighborSouthWest) {
        // Case 8. Cut out the obscured rectangle.
        polygon.addPoint(r.left, outBottom)
        polygon.addPoint(r.left, bottom)
        polygon.addPoint(outLeft, bottom)
      } else {
        // Case 1. The most normal of the cases, sigh.
        polygon.addPoint(outLeft, outBottom)
      }
      polygon.addPoint(outLeft, r.top + r.vDepth)
    }
  }
}
