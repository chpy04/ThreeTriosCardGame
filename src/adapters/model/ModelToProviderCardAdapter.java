package adapters.model;

import model.AttackValue;
import model.Card;
import provider.model.cards.Attack;
import provider.model.cards.Direction;
import provider.model.cards.ICard;

/**
 * An adapter that converts a given Card into an ICard that can be used in the provider's code.
 */
public class ModelToProviderCardAdapter implements ICard {

  private final Card delegate;

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate a Card that will handle equivalent calls
   */
  public ModelToProviderCardAdapter(Card delegate) {
    this.delegate = delegate;
  }

  @Override
  public Attack getAttackValue(Direction direction) {
    model.Direction modelDir = new DirectionAdapter().providerToModel(direction);
    AttackValue modelVal = delegate.getDirectionalValues().get(modelDir);
    return new AttackAdapter().modelToProvider(modelVal);
  }

  @Override
  public String getName() {
    int firstSpace = delegate.toString().indexOf(" ");
    return delegate.toString().substring(0, firstSpace);
  }
}
