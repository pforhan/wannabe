package wannabe.swing.renderer;

import java.awt.Graphics;
import wannabe.Projected;

/** Displays a voxel. */
public abstract class SwingRenderer {
  abstract public void draw(Graphics g, Projected r);

  @Override public String toString() {
    return getClass().getSimpleName();
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    return getClass().equals(obj.getClass());
  }

  @Override public int hashCode() {
    return getClass().hashCode();
  }
}
