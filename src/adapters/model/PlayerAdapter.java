package adapters.model;

import model.Player;
import provider.model.player.PlayerColor;

/**
 * An adapter between PlayerColors and Players which represent the same thing in two
 * different implementations.
 */
public class PlayerAdapter implements EnumAdapter<PlayerColor, Player> {
  @Override
  public Player providerToModel(PlayerColor input) {
    if (input.equals(PlayerColor.RED)) {
      return Player.A;
    } else {
      return Player.B;
    }
  }

  @Override
  public PlayerColor modelToProvider(Player input) {
    if (input.equals(Player.A)) {
      return PlayerColor.RED;
    } else {
      return PlayerColor.BLUE;
    }
  }
}
