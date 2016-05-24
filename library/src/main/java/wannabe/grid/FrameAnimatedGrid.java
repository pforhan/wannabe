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
  private final Translation translation = new Translation(0, 0, 0);
  private final String name;
  private int current;

  public FrameAnimatedGrid(String name) {
    this.name = name;
  }

  @Override public Iterator<Voxel> iterator() {
    Iterator<Voxel> realIterator = frames.get(current).iterator();
    System.out.println("iterating FrameAnimGrid; translation zero? " + translation.isZero());
    return translation.isZero() ? realIterator : new TranslatingIterator(realIterator, translation);
  }

  @Override public void exportTo(MutableGrid grid, Bounds bounds, boolean exportHidden) {
    Grid currentGrid = frames.get(current);
    if (translation.isZero()) {
      // We can skip cloning and translation.
      currentGrid.exportTo(grid, bounds, exportHidden);
      return;
    }

    // Otherwise, we have to translate all the things.
    // TODO this is a bit of a dumb hack... maybe add an optional translation in exportTo?
    currentGrid.translate(translation);
    currentGrid.exportTo(grid, bounds, exportHidden);
    currentGrid.clearTranslation();
  }

  @Override public int size() {
    return frames.get(current).size();
  }

  @Override public void translate(Translation offset) {
    translation.add(offset);
  }

  @Override public void clearTranslation() {
    translation.zero();
  }

  public void nextFrame() {
    current++;
    if (current >= frames.size()) {
      current = 0;
    }
  }

  public void addFrame(Grid frame) {
    if (frame == this) throw new IllegalArgumentException("Cannot have itself as a frame.");
    frames.add(frame);
  }

  @Override public Neighbors neighbors(Voxel voxel) {
    return frames.get(current).neighbors(voxel);
  }

  @Override public String toString() {
    return name + "; frame " + current + " of " + frames.size();
  }
}
