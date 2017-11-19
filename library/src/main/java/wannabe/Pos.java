package wannabe;

/** Parent interface that allows easy comparison of {@link Position} and {@link Translation}. */
public interface Pos {
  int x();
  int y();
  int z();
  boolean isZero();
}
