package provider.model.cards;

/**
 * Represents the possible Attack values for
 * cards in the game ThreeTrios.
 */
public enum Attack {
  ONE(1, "1"),
  TWO(2, "2"),
  THREE(3, "3"),
  FOUR(4, "4"),
  FIVE(5, "5"),
  SIX(6, "6"),
  SEVEN(7, "7"),
  EIGHT(8, "8"),
  NINE(9, "9"),
  TEN(10, "A");

  private final int value;
  private final String str;

  Attack(int value, String str) {
    this.value = value;
    this.str = str;
  }

  /**
   * Returns the corresponding value to this Attack.
   * @return the value of this Attack.
   */
  public int getValue() {
    return value;
  }

  /**
   * Returns the String representation of this Attack.
   * @return String Representation of this Attack
   */
  public String getStr() {
    return str;
  }
}
