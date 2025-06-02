package model;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a slot that is not a battle-able card.
 */
public abstract class NonCardSlot implements Slot {

  protected final boolean canPlayTo;

  /**
   * Constructs an Slot that is not a card.
   * @param canPlayTo whether you can play to this slot
   */
  public NonCardSlot(boolean canPlayTo) {
    this.canPlayTo = canPlayTo;
  }

  @Override
  public boolean canPlayCard() {
    return this.canPlayTo;
  }

  @Override
  public void switchPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("player cannot be null");
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

    return false;
  }

  @Override
  public void addToPlayerCount(Map<Player, Integer> curCount) {
    if (curCount == null) {
      throw new IllegalArgumentException("curCount cannot be null");
    }
  }

  @Override
  public int compareAttackValueTo(Direction dir, AttackValue other) {
    if (dir == null) {
      throw new IllegalArgumentException("dirAttackComingFrom cannot be null");
    }

    if (other == null) {
      throw new IllegalArgumentException("other cannot be null");
    }
    return 1;
  }

  @Override
  public Slot copySlot() {
    return this;
  }

  @Override
  public Player getSlotOwner() {
    return Player.NONE;
  }

  @Override
  public Map<Direction, AttackValue> getDirectionalValues() {
    return new HashMap<>();
  }

  @Override
  public void addRule(Comparator<AttackValue> rule) {
    //no rule changes needed for non cards.
  }

  @Override
  public void addCardTransformer(Function<AttackValue, AttackValue> func) {
    //no rule changes needed for non cards.
  }
}
