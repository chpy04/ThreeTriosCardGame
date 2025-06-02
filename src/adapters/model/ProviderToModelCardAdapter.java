package adapters.model;

import java.util.HashMap;
import java.util.Map;

import model.AttackValue;
import model.Card;
import model.Direction;
import model.Player;
import provider.model.cards.ICard;

/**
 * An adapter that converts a given ICard and its owner into a Card that can be used in out code.
 */
public class ProviderToModelCardAdapter extends Card {

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate the ICard that will handle functions
   * @param player the player that owns the given ICard
   */
  public ProviderToModelCardAdapter(ICard delegate, Player player) {
    super(delegate.getName(), player, mapCreator(delegate));
  }

  private static Map<Direction, AttackValue> mapCreator(ICard initialCard) {
    Map<Direction, AttackValue> ret = new HashMap<>();
    ret.put(Direction.UP, new AttackAdapter()
            .providerToModel(initialCard
                    .getAttackValue(provider.model.cards.Direction.NORTH)));
    ret.put(Direction.DOWN, new AttackAdapter()
            .providerToModel(initialCard
                    .getAttackValue(provider.model.cards.Direction.SOUTH)));
    ret.put(Direction.RIGHT, new AttackAdapter()
            .providerToModel(initialCard
                    .getAttackValue(provider.model.cards.Direction.EAST)));
    ret.put(Direction.LEFT, new AttackAdapter()
            .providerToModel(initialCard
                    .getAttackValue(provider.model.cards.Direction.WEST)));
    return ret;
  }
}
