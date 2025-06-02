package controller;

import model.Player;

/**
 * Represents events that occur in the model that the controller needs.
 */
public interface ModelFeatures {

  /**
   * indicates that the turn has changed.
   * @param playerTurn the player who's turn it is
   * @throws IllegalArgumentException if playerTurn is null
   */
  void changeTurn(Player playerTurn);

  /**
   * indicates that the game has ended.
   */
  void gameOver();
}
