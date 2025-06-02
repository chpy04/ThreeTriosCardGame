package model;

import java.util.Comparator;
import java.util.function.Function;

import controller.ModelFeatures;

/**
 * Represents a playable board which is a read only board with any state changing methods.
 */
public interface Board extends ReadOnlyBoard {

  /**
   * Plays a card to a given board.
   *
   * @param handIndex the card in the current player's hand to place on the grid
   * @param x         the x position of the card to be placed (0-indexed, starting at the top-left)
   * @param y         the y position of the card to be placed (0-indexed, starting at the top-left);
   * @throws IllegalArgumentException if x or y are out of range of the grid, hand is out of
   *                                  range for the current player's hand, or if you cannot
   *                                  play a card to that cell in the grid
   */
  void playToBoard(int handIndex, int x, int y);

  /**
   * Starts the game and allows the game to be mutated.
   * @throws IllegalStateException if the game has already started, if the game is over, or if
   *                               only one listener was added to this board
   */
  void startGame();

  /**
   * Adds a listener to correspond to specific events happening in the game.
   * @param features the listener to wait for specific events
   * @throws IllegalStateException if the game has already started, if the game is over, or if all
   *                               players in the game have a listener assigned
   *
   */
  void addListener(ModelFeatures features);

  /**
   * Adds a given rule to compare cards to all the cards in this model.
   * @param rule the rule to compare attack values with
   */
  void addRuleToCards(Comparator<AttackValue> rule);

  /**
   * Adds a given attack value transformer to all the cards for before they battle.
   * @param func the function to put attackvalues through
   */
  void addCardTransformerToCards(Function<AttackValue, AttackValue> func);
}
