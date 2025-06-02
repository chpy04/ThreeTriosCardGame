package view;

import controller.ViewFeatures;

/**
 * Represents a panel that can take in a features listener.
 */
public interface Panel {

  /**
   * Takes in a listener and adds it to this panel.
   * @param listener the features that will listen to calls from this class
   */
  void addListener(ViewFeatures listener);
}
