package model;

import java.awt.Color;

/**
 * Represents an empty place where a card can be placed.
 */
public class Empty extends NonCardSlot {

  /**
   * Creates an empty object.
   */
  public Empty() {
    super(true);
  }

  @Override
  public String toString() {
    return "_";
  }

  @Override
  public String boardPrint() {
    return "_";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Empty;
  }

  @Override
  public int hashCode() {
    return Boolean.hashCode(this.canPlayTo);
  }

  @Override
  public Color getSlotColor() {
    return Color.YELLOW;
  }


}
