package model;

import java.awt.Color;

/**
 * Represents a place on the board where a card cannot be placed.
 */
public class Hole extends NonCardSlot {

  /**
   * Constructs a hole object.
   */
  public Hole() {
    super(false);
  }

  @Override
  public String toString() {
    return " ";
  }

  @Override
  public String boardPrint() {
    return " ";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Hole;
  }

  @Override
  public int hashCode() {
    return Boolean.hashCode(this.canPlayTo);
  }

  @Override
  public Color getSlotColor() {
    return Color.GRAY;
  }
}
