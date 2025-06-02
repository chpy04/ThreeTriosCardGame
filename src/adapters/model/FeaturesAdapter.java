package adapters.model;

import controller.ViewFeatures;
import provider.model.grid.ICoordinate;
import provider.model.player.PlayerColor;
import provider.view.PlayerAction;

/**
 * Adapter that converts a given ViewFeatures object into a PlayerAction object that can
 * be used in the providers code.
 */
public class FeaturesAdapter implements PlayerAction {

  private final ViewFeatures delegate;

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate a ViewFeatures that will handle equivalent calls
   */
  public FeaturesAdapter(ViewFeatures delegate) {
    this.delegate = delegate;
  }

  @Override
  public void onCardSelected(int cardIndex, PlayerColor playedColor) {
    delegate.handleSelectCard(new PlayerAdapter().providerToModel(playedColor), cardIndex);
  }

  @Override
  public void onPositionSelected(ICoordinate coord) {
    delegate.handleGridPlay(coord.getRow(), coord.getCol());
  }
}
