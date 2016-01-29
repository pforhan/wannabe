// Copyright 2013 Patrick Forhan.
package wannabe;

import java.awt.Color;

/** Describes real pixel color, location, and size for a voxel. */
// TODO do we want java.awt.Color here?
public class Rendered {
  public int left;
  public int top;
  public int size;
  /** Horizontal space to use to indicate height. */
  public int hDepth;
  /** Vertical space to use to indicate height. */
  public int vDepth;
  public Color color;
  public Color darkerColor;
}