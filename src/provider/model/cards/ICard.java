package provider.model.cards;

/**
 * Interface for a Card.
 */
public interface ICard {
  /**
   * Returns the Attack value of this card for the given direction.
   *
   * @param direction Direction of the Attack wanted
   * @return the Attack value of given direction
   */
  Attack getAttackValue(Direction direction);

  /**
   * Returns the name of this card.
   *
   * @return this card's name
   */
  String getName();

  String toString();

  boolean equals(Object o);

  int hashCode();
}
