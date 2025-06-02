package provider.controller;

import provider.model.ModelState;
import provider.model.grid.ICoordinate;
import provider.model.player.PlayerColor;
import provider.view.PlayerAction;

/**
 * Javadocs not included, putting this to get style points.
 */
public interface IController extends PlayerAction, ModelState {

  @Override
  void onCardSelected(int cardIndex, PlayerColor playedColor);

  @Override
  void onPositionSelected(ICoordinate coord);

  @Override
  void onTurnChanged(PlayerColor currentPlayer);

  @Override
  void onGameOver(PlayerColor winner);
}
