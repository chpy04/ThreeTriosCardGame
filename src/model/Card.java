package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a card that can battle other cards in the ThreeTrios game.
 */
public class Card implements Slot {

  private final String name;
  private final Map<Direction, AttackValue> values;
  private Player cardOwner;
  private Comparator<AttackValue> rule;
  private List<Function<AttackValue, AttackValue>> valueFilters;

  /**
   * Constructs a Card object.
   * @param name the name of the card
   * @param cardOwner the owner of the card
   * @param directionValues the values for each direction of the card
   */
  protected Card(String name, Player cardOwner,
              Map<Direction, AttackValue> directionValues) {
    this.name = Objects.requireNonNull(name);
    this.cardOwner = Objects.requireNonNull(cardOwner);
    this.values = directionValues;
    class DefaultCardComp implements Comparator<AttackValue> {
      @Override
      public int compare(AttackValue o1, AttackValue o2) {
        return o1.value - o2.value;
      }
    }

    this.rule = new DefaultCardComp();
    this.valueFilters = new ArrayList<>();
  }

  /**
   * represents a builder class for a card.
   */
  public static class CardBuilder {
    String name;
    Map<Direction, AttackValue> values = new HashMap<>();
    Player cardOwner;

    /**
     * Returns the card with all of the values initialized.
     * @return a fully built Card
     * @throws IllegalStateException if name, values, or cardOwner is null, or if values
     *         doesn't have all the directions in its keys
     */
    public Card build() {
      if (name == null) {
        throw new IllegalArgumentException("name cannot be null");
      }

      if (cardOwner == null) {
        throw new IllegalArgumentException("cardOwner cannot be null");
      }

      if (values == null) {
        throw new IllegalArgumentException("values cannot be null");
      }

      if (!(values.containsKey(Direction.DOWN) && values.containsKey(Direction.UP) &&
              values.containsKey(Direction.LEFT) && values.containsKey(Direction.RIGHT))) {
        throw new IllegalArgumentException("values must have all directions of keys");
      }

      return new Card(name, cardOwner, values);
    }

    /**
     * Sets the name for the unbuilt card.
     * @param name name for the card
     * @return this builder
     * @throws IllegalArgumentException if name is null
     */
    public CardBuilder addName(String name) {
      if (name == null) {
        throw new IllegalArgumentException("name cannot be null");
      }
      this.name = name;
      return this;
    }

    /**
     * Sets the value for the corresponding direction in the unbuilt card.
     * @param dir direction to set
     * @param value value to assign to direction
     * @return this builder
     * @throws IllegalArgumentException if dir or value are null
     */
    public CardBuilder addValue(Direction dir, AttackValue value) {
      if (dir == null) {
        throw new IllegalArgumentException("dir cannot be null");
      }

      if (value == null) {
        throw new IllegalArgumentException("value cannot be null");
      }
      this.values.put(dir, value);
      return this;
    }

    /**
     * Sets the player for the unbuilt card.
     * @param player the player to own the card
     * @return this builder
     * @throws IllegalArgumentException if player is null
     */
    public CardBuilder addPlayer(Player player) {
      if (player == null) {
        throw new IllegalArgumentException("player cannot be null");
      }
      this.cardOwner = player;
      return this;
    }
  }

  @Override
  public boolean canPlayCard() {
    return false;
  }

  @Override
  public void switchPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("player cannot be null");
    }

    if (player != this.cardOwner) {
      this.cardOwner = player;
    }
  }

  @Override
  public boolean battle(Slot other, Direction direction) {
    if (other == null) {
      throw new IllegalArgumentException("other cannot be null");
    }

    if (direction == null) {
      throw new IllegalArgumentException("direction cannot be null");
    }

    return other.compareAttackValueTo(direction, this.values.get(direction)) < 0;
  }

  @Override
  public int compareAttackValueTo(Direction dirAttackComingFrom, AttackValue other) {
    if (dirAttackComingFrom == null) {
      throw new IllegalArgumentException("dirAttackComingFrom cannot be null");
    }

    if (other == null) {
      throw new IllegalArgumentException("other cannot be null");
    }
    AttackValue a1 = this.values.get(dirAttackComingFrom.getOppositeDirection());
    AttackValue a2 = other;
    for (Function<AttackValue, AttackValue> transformer : this.valueFilters) {
      a1 = transformer.apply(a1);
      a2 = transformer.apply(a2);
    }
    return rule.compare(a1, a2);
  }

  @Override
  public void addToPlayerCount(Map<Player, Integer> curCount) {

    if (curCount == null) {
      throw new IllegalArgumentException("curCount cannot be null");
    }

    curCount.put(this.cardOwner, curCount.getOrDefault(this.cardOwner, 0) + 1);
  }

  @Override
  public String boardPrint() {
    return this.cardOwner.toString().substring(0, 1);
  }

  @Override
  public Slot copySlot() {
    return this.copyCard();
  }

  @Override
  public Player getSlotOwner() {
    return this.cardOwner;
  }

  @Override
  public Color getSlotColor() {
    return this.cardOwner.playerColor();
  }

  @Override
  public Map<Direction, AttackValue> getDirectionalValues() {
    return new HashMap<>(this.values);
  }

  @Override
  public void addRule(Comparator<AttackValue> rule) {
    this.rule = rule;
  }

  @Override
  public void addCardTransformer(Function<AttackValue, AttackValue> func) {
    this.valueFilters.add(func);
  }

  /**
   * Returns a copy of this card.
   * @return a card that is identical to this but whose mutations do not effect this
   */
  public Card copyCard() {
    Card newCard = new Card(this.name, this.cardOwner,
            new HashMap<Direction, AttackValue>(this.values));
    for (Function<AttackValue, AttackValue> func : this.valueFilters) {
      newCard.addCardTransformer(func);
    }
    newCard.addRule(this.rule);
    return newCard;
  }


  @Override
  public String toString() {
    return this.name + " " + this.values.get(Direction.UP)
            + " " + this.values.get(Direction.DOWN)
            + " " + this.values.get(Direction.RIGHT)
            + " " + this.values.get(Direction.LEFT);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Card) {
      return this.name.equals(((Card)o).name)
              && this.cardOwner.equals(((Card)o).cardOwner)
              && this.values.equals(((Card)o).values);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.name.length();
  }
}
