package model.stategy;

import java.util.List;

import model.Player;
import model.ReadOnlyBoard;

/**
 * Represents an infallible strategy that composes 2 other infallible strategies. The second
 * strategy is used to break any ties between the moves produced by the first strategy.
 */
public class CombinedStrategy extends InfallibleTTStrategy {
  InfallibleStategy strat1;
  InfallibleStategy strat2;

  /**
   * Creates a combined strategy from two infallible strategies.
   * @param strat1 the strategy that will be used initially
   * @param strat2 the strategy that will be used to break ties from strategy 1
   */
  public CombinedStrategy(InfallibleStategy strat1, InfallibleStategy strat2) {
    this.strat1 = strat1;
    this.strat2 = strat2;
  }

  @Override
  public List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player) {
    return strat2.chooseFromOptions(
            strat1.chooseFromOptions(options, model, player), model, player);
  }
}
