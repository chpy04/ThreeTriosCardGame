package model;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a slot where a card could potentially go.
 */
public interface Slot {
  /**
   * Determines a card can be played to this slot.
   * @return whether a card can be played to this slot
   */
  boolean canPlayCard();

  /**
   * Switches the player who owns this slot if the slot can be owned.
   * @param player the player who should own the slot after execution of this method
   * @throws IllegalArgumentException if player is null
   */
  void switchPlayer(Player player);


  /**
   * Determines if this slot wins in a battle against the other slot, given the direction
   * this slot is attacking from.
   * @param other the slot to compare
   * @param direction the direction this slot is attacking from
   * @return whether this slot wins
   * @throws IllegalArgumentException if other or direction is null
   */
  boolean battle(Slot other, Direction direction);

  /**
   * Adds one to the count of this slot's owner player (if it has one).
   * @param curCount map of counts to each player in the game
   * @throws IllegalArgumentException if curCount is null
   */
  void addToPlayerCount(Map<Player, Integer> curCount);

  /**
   * Compares the values of this slot and another slot given the direction that the attack
   * is coming from and the value of the attacker (in the appropriate direction).
   * @param dirAttackComingFrom the direction that the attack is coming from
   * @param other the attackValue of the card attacking (in the appropriate direction)
   * @return positive if this value is higher, 0 if the same, negative if other is higher
   * @throws IllegalArgumentException if dirAttackComingFrom or other are null
   */
  int compareAttackValueTo(Direction dirAttackComingFrom, AttackValue other);

  /**
   * Generates a string that represents this slot on the board.
   * @return a string that represents this slot on the board
   */
  String boardPrint();

  /**
   * Return a copy of this slot which has no effect on this slot when mutated.
   * @return the copy
   */
  Slot copySlot();

  /**
   * Gets the owner of this slot.
   * @return the player that owns this slot, if the slot is of a type that can be owned
   */
  Player getSlotOwner();

  /**
   * Gets the color of this slot as it is displayed graphically.
   * @return the color of this slot
   */
  Color getSlotColor();

  /**
   * Gets a copy of the directional values for this slot.
   * @return a copy of the directional values for this slot
   */
  Map<Direction, AttackValue> getDirectionalValues();

  /**
   * Sets this slot to utilize a new given rule for comparing attack values.
   * @param rule the new ruleset
   */
  void addRule(Comparator<AttackValue> rule);

  /**
   * Transforms attackvalues being compared using this function before applying them to the rule.
   * @param func the function that transforms the attackvalues
   */
  void addCardTransformer(Function<AttackValue, AttackValue> func);
}
