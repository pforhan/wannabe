// Copyright 2013 Patrick Forhan.
package wannabe.swing;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class HelpPanel extends JTextArea {
  private static final long serialVersionUID = 1L;

  public HelpPanel() {
    setText("Arrow keys - move camera\n"
        + "Z - zoom in (move camera Z)\n"
        + "X - zoom out (move camera Z)\n"
        + "G - change SimpleGrid\n"
        + "R - change renderer\n"
        + "P - change projection\n"
        );
    setBorder(new EmptyBorder(5, 5, 5, 5));
  }
}
