package provider.view.gui.hand;

import java.awt.Dimension;

/**
 * Interface representation TriHandPanel, used to be see deck.
 */
public interface TrioHandPanel {
  /**
   * Sets the preferred size for this TrioHandPanel.
   * @param dim preferred size
   */
  void setPreferredSize(Dimension dim);

  /**
   * Repaints the hand panel.
   */
  void update();

  /**
   * Clears selected buttons from the hand panel.
   */
  void clearSelections();
}
