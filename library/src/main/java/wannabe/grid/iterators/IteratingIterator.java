package wannabe.grid.iterators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Depth-first iterates over a group of {@link Iterable}s, iterating each in turn, advancing
 * to the next one as each is exhausted.
 */
public class IteratingIterator<T> implements Iterator<T> {
  private final Iterator<? extends Iterable<T>> iterableIterator;
  private Iterator<T> current = new EmptyIterator<>();

  public IteratingIterator(Iterator<? extends Iterable<T>> iterableIterator) {
    this.iterableIterator = iterableIterator;
  }

  @Override public boolean hasNext() {
    maybeAdvance();
    return current.hasNext();
  }

  @Override public T next() {
    maybeAdvance();
    return current.next();
  }

  @Override public void remove() {
    throw new UnsupportedOperationException("Remove not supported");
  }

  private void maybeAdvance() {
    // If the current one is used up but there is another grid, advance:
    while (!current.hasNext() && iterableIterator.hasNext()) {
      // Grab the next grid and its voxel iterator:\
      current = iterableIterator.next().iterator();
    }
  }

  public static void main(String[] args) {
    Set<String> first = new HashSet<>(Arrays.asList("first one", "first two", "first three"));
    Set<String> second = new HashSet<>(Arrays.asList("sec one", "sec two"));
    Set<String> third = new HashSet<>(Arrays.asList("th one", "th two", "th three"));

    IteratingIterator<String> test =
        new IteratingIterator<>(Arrays.asList(first, second, third).iterator());

    while (test.hasNext()) {
      System.out.println("test " + test.next());
    }
  }
}

