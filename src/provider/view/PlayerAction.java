package provider.view;

import provider.model.grid.ICoordinate;
import provider.model.player.PlayerColor;

/**
 * Interface for playerActions, for the view to update listeners.
 */
public interface PlayerAction {
  /**
   * Called when a player selects a card from their hand.
   * @param cardIndex the index of the selected card in the player's hand
   * @param playedColor the player's color who selected the card;
   */
  void onCardSelected(int cardIndex, PlayerColor playedColor);

  /**
   * Called when a player chooses a position on the grid to play their card.
   * @param coord location of play
   */
  void onPositionSelected(ICoordinate coord);
}
