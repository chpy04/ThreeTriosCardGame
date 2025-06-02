package provider.model;

import java.io.IOException;

import provider.model.grid.ICoordinate;

/**
 * Represents a Game of Three Trios, where
 * players take turns place cards on a grid,
 * attempting to overtake the other player's cards.
 * The game is over when all card slots have been filled.
 * The player with the most cards of their
 * color when the game ends is the winner.
 */
public interface TrioGame extends ReadonlyTrioGame {

  /**
   * Places a card at coord on the grid.
   * Starts placing + battle phases.
   * @param idx the card to be placed
   * @param coord the location where the card should be placed
   * @throws IllegalStateException if game is over
   * @throws IllegalArgumentException if index is not valid
   * @throws IllegalArgumentException if Coordinate is null
   * @throws IllegalArgumentException if Coordinate is not on the grid
   */
  void playCard(int idx, ICoordinate coord);

  /**
   * Adds a listener for game state changes.
   * @param listener the features listener to add
   */
  void addModelStateListener(ModelState listener);

  /**
   * Starts the game. Should be called after all listeners are registered.
   */
  void startGame() throws IOException;
}
