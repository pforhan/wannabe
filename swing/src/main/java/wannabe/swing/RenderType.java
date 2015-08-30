// Copyright 2015 Patrick Forhan
package wannabe.swing;

import java.awt.Graphics;
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

      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  filledSquareWithCabinetSides {
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

  abstract void draw(Graphics g, Rendered r);

  public RenderType next() {
    RenderType[] values = values();
    int next = ordinal() + 1;
    return next != values.length ? values[next] : values[0];
  }
}
