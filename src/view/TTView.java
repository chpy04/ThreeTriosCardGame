package view;

import controller.ViewFeatures;

/**
 * represents a view for the ThreeTrios game.
 */
public interface TTView {

  /**
   * Set up the controller to handle click events in this view.
   *
   * @param listener the controller
   */
  void addListener(ViewFeatures listener);

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh(int selectedCard);

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  void showMessage(String message);
}
