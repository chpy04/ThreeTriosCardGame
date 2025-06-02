package provider.model.cards;

/**
 * Represents a direction on a card.
 * <ul><li>North is the top of the card
 * <li>South is the bottom of the card
 * <li>East is the right of the card
 * <li>West is the left of the card</ul>
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Returns the opposite facing direction of this Direction.
   *
   * @return the opposite Direction.
   */
  public Direction getOpposite() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      default:
        throw new IllegalStateException("Unknown direction");
    }
  }
}