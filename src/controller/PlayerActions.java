package controller;



/**
 * Represents actions that a player can take in the game via the controller.
 */
public interface PlayerActions {

  /**
   * Plays a card to the three trios game.
   * @param cardIdx the index of the card to play from the current player's hand
   * @param row the row to play the card at in the grid
   * @param col the column to play the card at in the grid
   */
  void playCard(int cardIdx, int row, int col);


}
