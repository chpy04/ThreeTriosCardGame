package provider.model.grid.cell;

import provider.model.cards.ICard;
import provider.model.grid.ICoordinate;
import provider.model.player.PlayerColor;


/**
 * Represents a single cell on a board of
 * Three Trios. A Cell can be a CardCell, which
 * can hold a card, or a HoleCell, representing
 * a space in the board that cannot hold a card.
 */
public interface Cell {
  /**
   * Returns whether this cell can hold a card,
   * ie, returns true if this cell is card cell,
   * and false otherwise.
   *
   * @return whether this cell can hold a card
   */
  boolean canHoldCard();

  /**
   * Returns the card that is in this cell.
   *
   * @return the card that is in this cell.
   * @throws IllegalStateException if this Cell is empty.
   */
  ICard getCard();

  /**
   * Sets this cell to the given card.
   *
   * @param card the card to be held in this cell.
   * @throws IllegalStateException if this cell already has a card
   */
  void setCard(ICard card);

  /**
   * Creates a copy of this cell.
   * @return copy of this cell.
   */
  Cell cloneCell();

  /**
   * Gets the Owner of the cell, in terms of their color.
   */
  PlayerColor getOwner();

  /**
   * Changes the owner of their cell, in terms of their color.
   */
  void setOwner(PlayerColor owner);

  /**
   * Gets the location of the coordinate.
   */
  ICoordinate getLoc();

  /**
   * Gets the card status of a cell, is it full, empty, or a hole in enum form.
   */
  CellCardStatus getCardStatus();

}
