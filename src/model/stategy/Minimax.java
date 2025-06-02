package model.stategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Board;
import model.Card;
import model.Player;
import model.ReadOnlyBoard;
import model.Slot;
import model.ThreeTriosBoard;

/**
 * An infallible strategy that takes in a strategy that it assumes the enemy is using, and
 * evaluates moves based on which move leaves the enemy strategy's best move with the lowest score.
 */
public class Minimax extends InfallibleTTStrategy {

  private final InfallibleStategy enemyStrategy;

  /**
   * Produces a strategy that minimizes the enemy's best move with the given strategy.
   * @param enemyStrategy the strategy that this strategy will minimize
   */
  public Minimax(InfallibleStategy enemyStrategy) {
    this.enemyStrategy = enemyStrategy;
  }

  @Override
  public List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player) {
    List<IMove> ret = new ArrayList<>();
    List<Card> curHand = model.getHand(player);
    List<Card> nextPlayerHand = model.getHand(Player.nextPlayer(player));
    int lowestMoveScore = Integer.MAX_VALUE;

    for (IMove move : options) {
      List<Card> combinedHands = new ArrayList<>();
      Slot[][] grid = model.getGrid();
      combinedHands.addAll(curHand);
      combinedHands.addAll(nextPlayerHand);
      Board simGame = new ThreeTriosBoard(grid, combinedHands, new Random(), false);
      simGame.startGame();
      if (simGame.isMoveLegal(move.xCord(), move.yCord())) {
        simGame.playToBoard(move.handIdx(), move.xCord(), move.yCord());
        IMove sampleMove = enemyStrategy.chooseMove(simGame, Player.nextPlayer(player));
        if (sampleMove.score() == lowestMoveScore) {
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(), lowestMoveScore));
        }
        if (sampleMove.score() < lowestMoveScore) {
          lowestMoveScore = sampleMove.score();
          ret.clear();
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(), lowestMoveScore));
        }
      }
    }

    return ret;
  }
}
