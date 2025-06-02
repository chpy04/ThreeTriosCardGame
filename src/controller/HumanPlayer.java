package controller;

import java.util.Objects;

import model.Board;
import model.Player;

/**
 * Represents a human-controlled player in the three trios game.
 */
public class HumanPlayer implements GamePlayer {
  Board model;

  /**
   * Constructs a human player object.
   * @param model the model to refer to.
   */
  public HumanPlayer(Board model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public void makeMove(Player player) {
    //human player just waits for input
  }

  @Override
  public void addListener(PlayerActions listener) {
    //human player just waits for input
  }


}
