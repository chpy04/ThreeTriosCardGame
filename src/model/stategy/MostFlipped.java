package model.stategy;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Player;
import model.ReadOnlyBoard;

/**
 * A strategy that evaluates moves based on which move flips the most enemy cards.
 */
public class MostFlipped extends InfallibleTTStrategy {
  @Override
  public List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player) {
    List<IMove> ret = new ArrayList<>();
    List<Card> hand = model.getHand(player);

    int flippedCards = 0;
    for (IMove move : options) {
      if (model.isMoveLegal(move.xCord(), move.yCord())) {
        int flipped = model.possibleCardsFlipped(hand.get(move.handIdx()),
                move.xCord(), move.yCord());
        if (flipped == flippedCards) {
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(), flipped));
        }
        if (flipped > flippedCards) {
          flippedCards = flipped;
          ret = new ArrayList<>();
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(), flipped));
        }
      }
    }
    return ret;
  }
}
