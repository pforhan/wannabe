// Copyright 2015 Patrick Forhan
package wannabe.swing;

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
      // The sides of the cube are described by a filled six-sided polygon with points
      // 1, 2, and 3 at the edge of the square and 4, 5, 6 offset by hDepth, vDepth
      // The first three points are based on which direction we indicate height.

      // TODO alter for different directions -- maybe just pick the first three points and base the others on these and it'll always work?
      g.setColor(r.darkerColor);
      int top = r.top;
      int left = r.left;
      int bottom = r.top + r.size;
      int right = r.left + r.size;
      int outRight = right + r.hDepth;
      int outBottom = bottom + r.vDepth;
      polygon.reset();
      polygon.addPoint(left, bottom);
      polygon.addPoint(right, bottom);
      polygon.addPoint(right, top);
      polygon.addPoint(outRight, top + r.vDepth);
      polygon.addPoint(outRight, outBottom);
      polygon.addPoint(left + r.hDepth, outBottom);
      g.fillPolygon(polygon);
      // Draw the top:
      g.setColor(r.color);
      g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
      // Kinda cool: if I connect other points it adds texture to the sides.
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledThreeDSquareWithCabinetWires {
    @Override void draw(Graphics g, Rendered r) {
      // TODO I'm doubling lines now because of the +1s, apparently
      // TODO I probably should be altering the +1s based on direction...
      int top = r.top + 1;
      int left = r.left + 1;
      int bottom = r.top + r.size;
      int right = r.left + r.size;
      g.setColor(r.darkerColor);
      if (r.hDepth < 0) {
        // Height indicated by drawing to the left of the face.
        if (r.vDepth < 0) {
          // Height indicated by drawing above-left the face. TODO finish
        } else {
          // Height indicated by drawing below-left the face. TODO finish
        }
      } else {
        // Height indicated by drawing to the right of the face.
        if (r.vDepth < 0) {
          // Height indicated by drawing above-right the face. TODO finish
        } else {
          // Height indicated by drawing below-right the face.
          // From bottom-left:
          g.drawLine(left, bottom, left + r.hDepth, bottom + r.vDepth);
          // From bottom-right:
          g.drawLine(right, bottom, right + r.hDepth, bottom + r.vDepth);// TODO do I need some -1s here?
          // From top-right
          g.drawLine(right, top, right + r.hDepth, top + r.vDepth);
        }
      }

      g.setColor(r.color);
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledSquareWithCabinetSides {
    @Override void draw(Graphics g, Rendered r) {
      // The sides of the cube are described by a filled six-sided polygon with points
      // 1, 2, and 3 at the edge of the square and 4, 5, 6 offset by hDepth, vDepth
      // The first three points are based on which direction we indicate height.

      // TODO alter for different directions -- maybe just pick the first three points and base the others on these and it'll always work?
      g.setColor(r.darkerColor);
      int top = r.top;
      int left = r.left;
      int bottom = r.top + r.size;
      int right = r.left + r.size;
      int outRight = right + r.hDepth;
      int outBottom = bottom + r.vDepth;
      polygon.reset();
      polygon.addPoint(left, bottom);
      polygon.addPoint(right, bottom);
      polygon.addPoint(right, top);
      polygon.addPoint(outRight, top + r.vDepth);
      polygon.addPoint(outRight, outBottom);
      polygon.addPoint(left + r.hDepth, outBottom);
      g.fillPolygon(polygon);
      // Draw the top:
      g.setColor(r.color);
      g.drawLine(polygon.xpoints[1], polygon.ypoints[1], polygon.xpoints[4] - 1, polygon.ypoints[4] - 1);
      // Kinda cool: if I connect other points it adds texture to the sides.
      g.fillRect(r.left, r.top, r.size, r.size);
    }
  },
  filledSquareWithCabinetWires {
    @Override void draw(Graphics g, Rendered r) {
      int bottom = r.top + r.size;
      int right = r.left + r.size;
      if (r.hDepth < 0) {
        // Height indicated by drawing to the left of the face.
        if (r.vDepth < 0) {
          // Height indicated by drawing above-left the face. TODO finish
        } else {
          // Height indicated by drawing below-left the face. TODO finish
        }
      } else {
        // Height indicated by drawing to the right of the face.
        if (r.vDepth < 0) {
          // Height indicated by drawing above-right the face. TODO finish
        } else {
          // Height indicated by drawing below-right the face.
          // From bottom-left:
          g.drawLine(r.left, bottom, r.left + r.hDepth, bottom + r.vDepth);
          // From bottom-right:
          g.drawLine(right , bottom, right + r.hDepth, bottom + r.vDepth);
          // From top-right
          g.drawLine(right, r.top, right + r.hDepth, r.top + r.vDepth);
        }
      }

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

  static final Polygon polygon = new Polygon();

  abstract void draw(Graphics g, Rendered r);

  public RenderType next() {
    RenderType[] values = values();
    int next = ordinal() + 1;
    return next != values.length ? values[next] : values[0];
  }
}
