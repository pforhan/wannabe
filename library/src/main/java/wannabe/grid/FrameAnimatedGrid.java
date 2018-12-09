package wannabe.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wannabe.Bounds;
import wannabe.Translation;
import wannabe.Voxel;

/** A Grid that implements animation by selecting one of its child grids at a time. */
public class FrameAnimatedGrid implements Grid {
  private final List<Grid> frames = new ArrayList<>();
  private final String name;
  private int current;
  private boolean dirty;

  public FrameAnimatedGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    dirty = false;
    return frames.get(current).iterator();
  }

  @Override public boolean isDirty() {
    return dirty || frames.get(current).isDirty();
  }

  @Override public int size() {
    return frames.get(current).size();
  }

  public void nextFrame() {
    dirty = true;
    current++;
    if (current >= frames.size()) {
      current = 0;
    }
  }

  public void addFrame(Grid frame) {
    if (frame == this) throw new IllegalArgumentException("Cannot have itself as a frame.");
    frames.add(frame);
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    return frames.get(current).neighbors(voxel);
  }

  @Override public String toString() {
    return name + "; frame " + current + " of " + frames.size();
  }
}
