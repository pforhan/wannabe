package wannabe.grid;

import java.util.Iterator;
import wannabe.Voxel;
import wannabe.grid.iterators.OperationIterator;
import wannabe.grid.operations.GroupOperation;
import wannabe.grid.operations.Operation;

/**
 * A Grid that caches the output of the specified operation(s) applied to the source Grid's data.
 */
public class OperationGrid implements Grid {
  private final Grid source;
  private final Operation op;

  public OperationGrid(Grid source, Operation operation) {
    this.source = source;
    op = operation;
  }

  public OperationGrid(Grid source, Operation... operations) {
    this.source = source;
    op = new GroupOperation(operations);
  }

  @Override public Iterator<Voxel> iterator() {
    return new OperationIterator(source.iterator(), op);
  }

  @Override public boolean isDirty() {
    return source.isDirty() && op.isDirty();
  }

  @Override public int size() {
    throw new IllegalStateException("Unable to determine size.");
  }

  @Override public AllNeighbors neighbors(Voxel voxel) {
    throw new IllegalStateException("Unable to determine neighbors.");
  }

  @Override public String toString() {
    return "op grid"; // TODO include op names or something
  }
}
