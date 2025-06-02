package provider.view.gui.frame;

import provider.view.TrioView;

/**
 * Interface to represent TrioFrame, used to make the whole game screen.
 */
public interface TrioFrame extends TrioView {
  /**
   * Updates display to the boolean value.
   * @param show true if should show.
   */
  void display(boolean show);

  /**
   * Updates the view.
   */
  void update();
}
