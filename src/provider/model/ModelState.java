package provider.model;

import provider.model.player.PlayerColor;

/**
 * Features interface for model notifications, where the model notifies all
 * subscribed listeners when these events happen.
 */
public interface ModelState {
  /**
   * Called when it becomes a different player's turn.
   * @param currentPlayerColor the ID of the player whose turn it is
   */
  void onTurnChanged(PlayerColor currentPlayerColor);

  /**
   * Called when the game ends.
   * @param winnerColor the Color of the winning player
   */
  void onGameOver(PlayerColor winnerColor);
}
