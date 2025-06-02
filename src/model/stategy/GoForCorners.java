package model.stategy;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Direction;
import model.Player;
import model.ReadOnlyBoard;

/**
 * An infallible strategy that attempts to go for open corners on the board. Of the open corners,
 * it prioritizes moves that exposes the highest combined attack values to the two adjacent
 * cells on the board (regardless of whether they can be played too). If no corners can be played,
 * every other move is considered the same by this strategy.
 */
public class GoForCorners extends InfallibleTTStrategy {

  @Override
  public List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player) {
    List<IMove> ret = new ArrayList<>();
    int bestScore = 0;
    for (IMove move : options) {
      if (model.isMoveLegal(move.xCord(), move.yCord())) {
        bestScore = evaluateMove(move, 0, 0,
                Direction.RIGHT, Direction.DOWN, model, player, ret, bestScore);

        bestScore = evaluateMove(move, model.gameWidth() - 1, 0,
                Direction.LEFT, Direction.DOWN, model, player, ret, bestScore);

        bestScore = evaluateMove(move, 0, model.gameHeight() - 1,
                Direction.RIGHT, Direction.UP, model, player, ret, bestScore);

        bestScore = evaluateMove(move, model.gameWidth() - 1, model.gameHeight() - 1,
                Direction.LEFT, Direction.UP, model, player, ret, bestScore);
        if (bestScore == 0) {
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(), 0));
        }
      }
    }

    return ret;
  }

  private int evaluateMove(IMove move, int xPos, int yPos, Direction open1, Direction open2,
                            ReadOnlyBoard model, Player player,
                           List<IMove> bestMoves, int bestScore) {
    if (move.xCord() == xPos && move.yCord() == yPos) {
      int value = sumValue(model.getHand(player).get(move.handIdx()), open1, open2);
      if (value == bestScore) {
        bestMoves.add(new Move(move.handIdx(), move.xCord(), move.yCord(), value));
      }
      if (value > bestScore) {
        bestMoves.clear();
        bestMoves.add(new Move(move.handIdx(), move.xCord(), move.yCord(), value));
        bestScore = value;
      }
    }
    return bestScore;
  }

  private int sumValue(Card card, Direction direction1, Direction direction2) {
    return card.getDirectionalValues().get(direction1).value +
            card.getDirectionalValues().get(direction2).value;
  }
}
