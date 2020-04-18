package wannabe.grid.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import wannabe.Voxel;

/** Applies specified operations serially, applying only those that are included by all. */
public class GroupOperation extends Operation {
  private List<Operation> ops = new ArrayList<>();

  public GroupOperation(Operation[] operations) {
    ops.addAll(Arrays.asList(operations));
  }

  @Override public boolean isDirty() {
    for (Operation op : ops) {
      if (op.isDirty()) {
        return true;
      }
    }
    return false;
  }

  /** Includes a voxel if all operations include it. */
  @Override public boolean includes(Voxel voxel) {
    for (Operation op : ops) {
      if (!op.includes(voxel)) {
        return false;
      }
    }
    return true;
  }

  @Override public Voxel apply(Voxel voxel) {
    Voxel out = voxel;
    for (Operation op : ops) {
      out = op.apply(out);
    }
    return out;
  }
}
