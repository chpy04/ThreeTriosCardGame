package adapters.model;

import java.awt.Color;

import model.Card;
import model.Slot;
import provider.model.cards.ICard;
import provider.model.grid.ICoordinate;
import provider.model.grid.cell.Cell;
import provider.model.grid.cell.CellCardStatus;
import provider.model.player.PlayerColor;

/**
 * An adapter that converts a given Slot into a Cell that can be used in the provider's code.
 */
public class SlotToCellAdapter implements Cell {

  private final Slot delegate;
  private final ICoordinate position;

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate the Slot that will handle functionality
   * @param position the position of the slot on the board
   */
  public SlotToCellAdapter(Slot delegate, ICoordinate position) {
    this.delegate = delegate;
    this.position = position;
  }

  @Override
  public ICoordinate getLoc() {
    return this.position;
  }

  @Override
  public CellCardStatus getCardStatus() {
    if (!delegate.canPlayCard()) {
      if (delegate.getSlotColor().equals(Color.GRAY)) {
        return CellCardStatus.HOLE;
      } else {
        return CellCardStatus.FULL;
      }
    }
    return CellCardStatus.EMPTY;
  }

  @Override
  public PlayerColor getOwner() {
    return new PlayerAdapter().modelToProvider(delegate.getSlotOwner());
  }

  @Override
  public void setOwner(PlayerColor owner) {
    delegate.switchPlayer(new PlayerAdapter().providerToModel(owner));
  }

  @Override
  public boolean canHoldCard() {
    return delegate.canPlayCard();
  }

  @Override
  public ICard getCard() {
    if (delegate instanceof Card) {
      return new ModelToProviderCardAdapter((Card) delegate);
    }
    throw new IllegalStateException("Not a card");
  }

  @Override
  public void setCard(ICard card) {
    //Nothing can be done here
  }

  @Override
  public Cell cloneCell() {
    return this;
  }
}
