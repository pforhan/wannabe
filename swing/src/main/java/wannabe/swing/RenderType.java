// Copyright 2015 Patrick Forhan
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
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
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
      g.fillRect(r.left, r.top, r.size, r.size);
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
    @Override void draw(Graphics g, Rendered r) {
      // First erase what we're going to write on:
      Rendered dark = r.clone();
      dark.color = Color.BLACK;
      dark.darkerColor = Color.BLACK;
      // Populate manually so that we only calculate it once.
      populatePolygon(r);
      fillSides(g, dark, polygon);
      g.fillRect(r.left, r.top, r.size, r.size);
      // Now draw the cube:
      wireSides(g, r, polygon);
      g.drawRect(r.left, r.top, r.size, r.size);
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
    populatePolygon(r);
    fillSides(g, r, polygon);
  }

  void fillSides(Graphics g, Rendered r, Polygon polygon) {
    g.setColor(r.darkerColor);
    g.fillPolygon(polygon);
    g.setColor(r.color);
    // Draw a line representing the edge of the side
    // Kinda cool: if I connect other points it adds texture to the sides.
    g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
  }

  void wireSides(Graphics g, Rendered r) {
    populatePolygon(r);
    wireSides(g, r, polygon);
  }

  void wireSides(Graphics g, Rendered r, Polygon polygon) {
    g.setColor(r.darkerColor);
    g.drawPolygon(polygon);
    g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
    g.setColor(r.color);
  }

  /** Populates a polygon representing the sides of a cube. Does no drawing. */
  void populatePolygon(Rendered r) {
    // The sides of the cube are described by a filled six-sided polygon with points
    // 1, 2, and 3 at the edge of the square and 4, 5, 6 offset by hDepth, vDepth
    // The first three points are based on which direction we indicate height.
    int bottom = r.top + r.size;
    int right = r.left + r.size;
    polygon.reset();
    // Pick the first three points based on direction.
    // TODO this really feels refactorable... but maybe not.
    if (r.hDepth < 0) {
      // Height indicated by drawing to the left of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-left the face.
        polygon.addPoint(right, r.top);
        polygon.addPoint(r.left, r.top);
        polygon.addPoint(r.left, bottom);
        int outTop = r.top + r.vDepth;
        int outLeft = r.left+ r.hDepth;
        polygon.addPoint(outLeft, bottom + r.vDepth);
        polygon.addPoint(outLeft, outTop);
        polygon.addPoint(right + r.hDepth, outTop);
      } else {
        // Height indicated by drawing below-left the face.
        polygon.addPoint(r.left, r.top);
        polygon.addPoint(r.left, bottom);
        polygon.addPoint(right, bottom);
        int outBottom = bottom + r.vDepth;
        int outLeft = r.left+ r.hDepth;
        polygon.addPoint(right + r.hDepth, outBottom);
        polygon.addPoint(outLeft, outBottom);
        polygon.addPoint(outLeft, r.top + r.vDepth);
      }
    } else {
      // Height indicated by drawing to the right of the face.
      if (r.vDepth < 0) {
        // Height indicated by drawing above-right the face.
        polygon.addPoint(right, bottom);
        polygon.addPoint(right, r.top);
        polygon.addPoint(r.left, r.top);
        int outRight = right + r.hDepth;
        int outTop = r.top + r.vDepth;
        polygon.addPoint(r.left + r.hDepth, outTop);
        polygon.addPoint(outRight, outTop);
        polygon.addPoint(outRight, bottom + r.vDepth);
      } else {
        // Height indicated by drawing below-right the face.
        polygon.addPoint(r.left, bottom);
        polygon.addPoint(right, bottom);
        polygon.addPoint(right, r.top);
        int outRight = right + r.hDepth;
        int outBottom = bottom + r.vDepth;
        polygon.addPoint(outRight, r.top + r.vDepth);
        polygon.addPoint(outRight, outBottom);
        polygon.addPoint(r.left + r.hDepth, outBottom);
      }
    }
  }

  abstract void draw(Graphics g, Rendered r);

  public RenderType next() {
    RenderType[] values = values();
    int next = ordinal() + 1;
    return next != values.length ? values[next] : values[0];
  }
}
