package controller.mocks;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import controller.ModelFeatures;
import model.AttackValue;
import model.Board;
import model.Card;
import model.Player;
import model.Slot;

/**
 * A mock board that logs when playToBoard is called.
 */
public class LoggingPlayToBoardMock implements Board {
  StringBuilder sb;

  /**
   * Creates this mock with a string builder to append the log to.
   * @param sb what the log is appended to
   */
  public LoggingPlayToBoardMock(StringBuilder sb) {
    this.sb = sb;
  }


  @Override
  public void playToBoard(int handIndex, int x, int y) {
    sb.append("Tried to play to (" + x + ", " + y + ") with handIndex=" + handIndex).append("\n");
  }

  @Override
  public void startGame() {
    //does nothing here for mock
  }

  @Override
  public void addListener(ModelFeatures features) {
    //does nothing here for mock
  }

  @Override
  public void addRuleToCards(Comparator<AttackValue> rule) {
    //nothing to do here
  }

  @Override
  public void addCardTransformerToCards(Function<AttackValue, AttackValue> func) {
    //nothing to do here
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Player gameWinner() {
    return null;
  }

  @Override
  public Slot[][] getGrid() {
    return new Slot[0][];
  }

  @Override
  public int gameWidth() {
    return 0;
  }

  @Override
  public int gameHeight() {
    return 0;
  }

  @Override
  public Slot getCoord(int x, int y) {
    return null;
  }

  @Override
  public Player getCellOwner(int x, int y) {
    return null;
  }

  @Override
  public boolean isMoveLegal(int x, int y) {
    return false;
  }

  @Override
  public int score(Player player) {
    return 0;
  }

  @Override
  public int possibleCardsFlipped(Card card, int x, int y) {
    return 0;
  }

  @Override
  public Player curPlayer() {
    return Player.A;
  }

  @Override
  public List<Card> getHand(Player player) {
    return null;
  }
}
