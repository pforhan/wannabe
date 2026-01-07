package wannabe.grid.iterators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;

public class IteratingIteratorTest {
  @Test public void testBasicOperation() {
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
