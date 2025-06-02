package provider.model;

import java.util.List;

import provider.model.cards.ICard;
import provider.model.grid.ICoordinate;
import provider.model.grid.IGrid;
import provider.model.grid.cell.Cell;
import provider.model.player.PlayerColor;

/**
 * Read only version of Trio Game. Contain all the observation methods the model.
 */
public interface ReadonlyTrioGame {
  /**
   * Returns the Grid.
   * The grid is 0 indexed, and begins in the upper left corner.
   * Modifying this grid has no effect on the game
   * @return the grid
   */
  IGrid getGrid();

  /**
   * Returns the width of the grid.
   * @return width of game grid
   */
  int getWidth();

  /**
   * Returns the width of the grid.
   * @return width of game grid
   */
  int getHeight();

  /**
   * Returns the color of the current player whose turn it is.
   * @return current player's color
   */
  PlayerColor getCurrPlayer();

  /**
   * Gets the cards in the given players hand.
   * Modifying this list has no effect on the game.
   * @return a new list of cards in the given players hand.
   */
  List<ICard> getHand(PlayerColor color);


  /**
   * Returns the PlayerColor of the winning player.
   * @return winning players color
   * @throws IllegalStateException if game is not over
   */
  PlayerColor getWinner();

  /**
   * Returns the given PlayerColor's score, which is
   * calculated based on number of cards on the board
   * that are owned by player and the cards in their hand.
   * @return the players score.
   */
  int getScore(PlayerColor player);

  /**
   * Checks legality of a move.
   * @return true if move is legal, false otherwise
   */
  boolean isLegalMove(ICard card, ICoordinate coord);

  /**
   * Checks if the game is over.
   * @return true if game over, false otherwise
   */
  boolean isGameOver();

  /**
   * Checks if the game has been started.
   * @return true if game has started, false otherwise
   */
  boolean isGameStarted();

  /**
   * Gets the cell at a coordinate.
   * @param coord cell location
   * @return cell
   * @throws IllegalArgumentException if coord is not valid
   */
  Cell getCell(ICoordinate coord);

  /**
   * Gets owner of cell at coordinate.
   * @param coord cell location
   * @return owner of cell, null if no owner
   * @throws IllegalArgumentException if coord is not legal
   */
  PlayerColor getCellOwner(ICoordinate coord);

  /**
   * Returns how many cards will flip by the current player playing given
   * card to coordinate.
   * @param card to play
   * @param coord location of play
   * @return how many cards the play flipped in players direction
   * @throws IllegalArgumentException if play is not legal move
   */
  int howManyFlip(ICard card, ICoordinate coord);

}
