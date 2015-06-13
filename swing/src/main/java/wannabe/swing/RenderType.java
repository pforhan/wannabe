// Copyright 2015 Patrick Forhan
package wannabe.swing;

import java.awt.Graphics2D;
import wannabe.Rendered;

public enum RenderType {
  filledCircle {
    @Override void draw(Graphics2D g, Rendered r) {
      g.fillOval(r.left, r.top, r.size, r.size);
    }
  },
  filledRoundedSquare {
    @Override void draw(Graphics2D g, Rendered r) {
      int arc = r.size / 3;
      g.fillRoundRect(r.left, r.top, r.size, r.size, arc, arc);
    }
  },
  filledSquare {
    @Override void draw(Graphics2D g, Rendered r) {
      g.fillRect(r.left, r.top, r.size, r.size);
    }
  },
  filledThreeDSquare {
    @Override void draw(Graphics2D g, Rendered r) {
      g.fill3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  pixel {
    @Override void draw(Graphics2D g, Rendered r) {
      g.drawLine(r.left, r.top, r.left, r.top);
    }
  },
  circle {
    @Override void draw(Graphics2D g, Rendered r) {
      g.drawOval(r.left, r.top, r.size, r.size);
    }
  },
  square {
    @Override void draw(Graphics2D g, Rendered r) {
      g.drawRect(r.left, r.top, r.size, r.size);
    }
  },
  roundedSquare {
    @Override void draw(Graphics2D g, Rendered r) {
      int arc = r.size / 3;
      g.drawRoundRect(r.left, r.top, r.size, r.size, arc, arc);
    }
  },
  threeDSquare {
    @Override void draw(Graphics2D g, Rendered r) {
      g.draw3DRect(r.left, r.top, r.size, r.size, true);
    }
  },
  ;

  abstract void draw(Graphics2D g, Rendered r);

  public RenderType next() {
    RenderType[] values = values();
    int next = ordinal() + 1;
    return next != values.length ? values[next] : values[0];
  }
}
