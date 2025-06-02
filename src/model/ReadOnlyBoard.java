package model;

import java.util.List;

/**
 * Represents a board in the ThreeTrios game that only allows read operations.
 */
public interface ReadOnlyBoard {
  /**
   * Determines if the game is over.
   * @return whether the game is over
   */
  boolean isGameOver();

  /**
   * Calculates the winner of the game.
   * @return the winner
   * @throws IllegalStateException if the game is not over
   */
  Player gameWinner();

  /**
   * Gets the grid from the game going on.
   * @return the grid
   */
  Slot[][] getGrid();

  /**
   * Gives the width of this game in number of slots.
   * @return the width of the game
   */
  int gameWidth();

  /**
   * Gives the height of this game in number of slots.
   * @return the height of the game
   */
  int gameHeight();

  /**
   * Gets the slot at the given x y coordinate in this game, starting in the top left.
   * @param x the x coordinate, 0-indexed
   * @param y the y coordinate, 0-indexed
   * @return a copy of the slot at the given coordinate
   * @throws IllegalArgumentException if x or y are out of range of the board
   */
  Slot getCoord(int x, int y);

  /**
   * Gets the owner of the slot at the given x y coordinate in this game, starting in the top left.
   * @param x the x coordinate, 0-indexed
   * @param y the y coordinate, 0-indexed
   * @return the player that owns the cell, if the cell is of a type that can be owned
   * @throws IllegalArgumentException if the x or y are out of range of the board
   */
  Player getCellOwner(int x, int y);

  /**
   * Can the current player play a card at the given x y coordinates, starting in the top left.
   * @param x the x coordinate, 0-indexed
   * @param y the y coordinate, 0-indexed
   * @return true if a card can be played there, false if not
   */
  boolean isMoveLegal(int x, int y);

  /**
   * What is a given players current score in the game, including cards in their hand.
   * @param player the player whose score is being calculated
   * @return the number of cards that that player owns in the board or in their hand
   * @throws IllegalArgumentException if player is null
   */
  int score(Player player);

  /**
   * If a given card is played at the given coordinates, how many cards will be flipped.
   * @param card the card that could be played at those coordinates
   * @param x the x coordinate, 0-indexed, starting from the left
   * @param y the y coordinate, 0-indexed, starting from the top
   * @return the number of cards that would be flipped if this move was played
   * @throws IllegalArgumentException if card is null or x or y are invalid coordinates
   */
  int possibleCardsFlipped(Card card, int x, int y);

  /**
   * Gets the player that is currently making a turn.
   * @return the current player
   */
  Player curPlayer();

  /**
   * Gets the hand for a specific player.
   * @param player the player's whos hand we should get
   * @return the player's hand
   * @throws IllegalArgumentException if player is null
   */
  List<Card> getHand(Player player);
}
