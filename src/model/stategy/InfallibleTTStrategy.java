package model.stategy;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Player;
import model.ReadOnlyBoard;

/**
 * An infallible strategy that chooses the best move by picking the first move of all the best
 * moves, and that calculates the best moves by calling chooseFromOptions with every possible move.
 */
public abstract class InfallibleTTStrategy implements InfallibleStategy {
  @Override
  public IMove chooseMove(ReadOnlyBoard model, Player player) throws IllegalStateException {
    List<IMove> moveCandidates = this.allBestMoveCandidates(model, player);

    if (moveCandidates.isEmpty()) {
      throw new IllegalStateException("no valid moves");
    }

    return moveCandidates.get(0);
  }

  @Override
  public List<IMove> allBestMoveCandidates(ReadOnlyBoard model, Player player) {
    List<IMove> allMoves = new ArrayList<>();
    List<Card> hand = model.getHand(player);

    for (int row = 0; row < model.gameHeight(); row++) {
      for (int col = 0; col < model.gameWidth(); col++) {
        if (model.isMoveLegal(col, row)) {
          for (int handIdx = 0; handIdx < hand.size(); handIdx++) {
            allMoves.add(new Move(handIdx, col, row, 0));
          }
        }
      }
    }

    return this.chooseFromOptions(allMoves, model, player);
  }
}
