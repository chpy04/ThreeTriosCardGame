package view;

/**
 * A panel to display a player's hand.
 */
public interface PlayerPanel extends Panel {

  /**
   * Refreshes this panel.
   * @param selectedCard the index of the selected card in this panel
   */
  void refresh(int selectedCard);

}
