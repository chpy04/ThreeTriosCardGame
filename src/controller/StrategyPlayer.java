package controller;

import java.util.Objects;

import model.Board;
import model.Player;
import model.stategy.IMove;
import model.stategy.InfallibleStategy;

/**
 * Represents a player in the game that plays using a given strategy.
 */
public class StrategyPlayer implements GamePlayer {

  private final InfallibleStategy strategy;
  private final Board model;
  private PlayerActions listener;

  /**
   * constructs a StrategyPlayer object.
   * @param model the model to refer to.
   * @param strategy the strategy to use when picking a move.
   */
  public StrategyPlayer(Board model, InfallibleStategy strategy) {
    this.strategy = Objects.requireNonNull(strategy);
    this.model = Objects.requireNonNull(model);
  }


  @Override
  public void makeMove(Player player) {
    IMove chosenMove = strategy.chooseMove(model, player);
    this.listener.playCard(chosenMove.handIdx(), chosenMove.yCord(), chosenMove.xCord());

  }

  @Override
  public void addListener(PlayerActions listener) {
    this.listener = listener;
  }
}
