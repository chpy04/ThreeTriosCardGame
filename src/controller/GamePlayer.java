package controller;

import model.Player;

/**
 * represents a player in the three trios game that can interact with the board.
 */
public interface GamePlayer {

  /**
   * Makes a move for a player in the threetrios game.
   * @param player the currentplayer's turn
   */
  void makeMove(Player player);

  /**
   * Adds a listener for callbacks after certain events.
   * @param listener the listener to add to this player
   */
  void addListener(PlayerActions listener);
}
