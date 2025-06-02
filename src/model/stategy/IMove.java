package model.stategy;

/**
 * Represents a given move that a player can make in a TT game.
 */
public interface IMove {

  /**
   * The index of the card that a player would play from their hand.
   * @return the index
   */
  int handIdx();

  /**
   * The x coordinate of the cell that the player would play to.
   * @return the x coordinate, 0-indexed, starting from the left
   */
  int xCord();

  /**
   * The y coordinate of the cell that the player would play to.
   * @return the y coordinate, 0-indexed, starting from the top
   */
  int yCord();

  /**
   * The score of this move representing how good this move is.
   * @return an integer representing how good the strategy that made this move thinks this move
   *         is, higher is better
   */
  int score();
}
