package model.stategy;

import java.util.ArrayList;
import java.util.List;

import model.AttackValue;
import model.Card;
import model.Direction;
import model.Player;
import model.ReadOnlyBoard;

/**
 * An infallible strategy that evaluates moves based on the number of opponent moves that could be
 * played on the next move that could flip whatever card is played.
 */
public class LeastChanceOfBeingFlipped extends InfallibleTTStrategy {

  @Override
  public List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player) {
    List<IMove> ret = new ArrayList<>();
    List<Card> playerHand = model.getHand(player);
    List<Card> nextPlayerHand = model.getHand(Player.nextPlayer(player));

    int numCardsFlip = nextPlayerHand.size() * 4 + 1;

    for (IMove move : options) {
      if (model.isMoveLegal(move.xCord(), move.yCord())) {
        int flipped = numOpponentMovesThatCanFlip(playerHand.get(move.handIdx()),
                move.xCord(), move.yCord(), model, nextPlayerHand);
        if (flipped == numCardsFlip) {
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(),
                  nextPlayerHand.size() * 4 + 1 - flipped));
        }
        if (flipped < numCardsFlip) {
          ret = new ArrayList<>();
          ret.add(new Move(move.handIdx(), move.xCord(), move.yCord(),
                  nextPlayerHand.size() * 4 + 1 - flipped));
          numCardsFlip = flipped;
        }
      }
    }

    return ret;
  }

  private int numOpponentMovesThatCanFlip(Card cardPlayed, int xPos, int yPos,
                                          ReadOnlyBoard model, List<Card> nextPlayerHand) {
    int ret = 0;

    if (validIGridIndex(xPos - 1, yPos, model.gameWidth(), model.gameHeight(), model)) {
      ret += compCardsInDir(Direction.LEFT, cardPlayed, nextPlayerHand);
    }

    if (validIGridIndex(xPos + 1, yPos, model.gameWidth(), model.gameHeight(), model)) {
      ret += compCardsInDir(Direction.RIGHT, cardPlayed, nextPlayerHand);
    }

    if (validIGridIndex(xPos, yPos + 1, model.gameWidth(), model.gameHeight(), model)) {
      ret += compCardsInDir(Direction.DOWN, cardPlayed, nextPlayerHand);
    }

    if (validIGridIndex(xPos, yPos - 1, model.gameWidth(), model.gameHeight(), model)) {
      ret += compCardsInDir(Direction.UP, cardPlayed, nextPlayerHand);
    }

    return ret;
  }

  private int compCardsInDir(Direction dir, Card playedCard, List<Card> nextPlayerHand) {
    int ret = 0;
    AttackValue compareVal = playedCard.getDirectionalValues().get(dir);

    for (Card card : nextPlayerHand) {
      if (card.getDirectionalValues().get(dir.getOppositeDirection())
              .compareToValue(compareVal) > 0) {
        ret++;
      }
    }
    return ret;
  }

  private boolean validIGridIndex(int xPos, int yPos, int width, int height, ReadOnlyBoard model) {
    return 0 <= xPos - 1 && xPos - 1 < width && 0 <= yPos - 1 && yPos - 1 < height
            && model.isMoveLegal(xPos, yPos);
  }
}
