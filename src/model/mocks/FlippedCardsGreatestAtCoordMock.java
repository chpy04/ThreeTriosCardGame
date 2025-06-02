package model.mocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import controller.ModelFeatures;
import model.AttackValue;
import model.Board;
import model.Card;
import model.Direction;
import model.Empty;
import model.Player;
import model.Slot;

/**
 * represents a mock that checks if the flipped card is greatest at a coordinate.
 */
public class FlippedCardsGreatestAtCoordMock implements Board {
  private final int xPos;
  private final int yPos;
  private final int width;
  private final int height;

  /**
   * constructs the mock object.
   * @param xPos x position
   * @param yPos y position
   * @param width width of the board
   * @param height height of the board
   */
  public FlippedCardsGreatestAtCoordMock(int xPos, int yPos, int width, int height) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.width = width;
    this.height = height;
  }

  @Override
  public void playToBoard(int handIndex, int x, int y) {
    //empty because we don't need to track this;
  }

  @Override
  public void startGame() {
    //does nothing here for mock
  }

  @Override
  public void addListener(ModelFeatures gamePlayer) {
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
    return Player.A;
  }

  @Override
  public Slot[][] getGrid() {
    return new Slot[0][];
  }

  @Override
  public int gameWidth() {
    return this.width;
  }

  @Override
  public int gameHeight() {
    return this.height;
  }

  @Override
  public Slot getCoord(int x, int y) {
    return new Empty();
  }

  @Override
  public Player getCellOwner(int x, int y) {
    return Player.A;
  }

  @Override
  public boolean isMoveLegal(int x, int y) {
    return true;
  }

  @Override
  public int score(Player player) {
    return 0;
  }

  @Override
  public int possibleCardsFlipped(Card card, int x, int y) {
    if (x == xPos && y == yPos) {
      return 1;
    }
    return 0;
  }

  @Override
  public Player curPlayer() {
    return Player.A;
  }

  @Override
  public List<Card> getHand(Player player) {
    List<Card> ret = new ArrayList<>();
    Card c1 = new Card.CardBuilder().addName("A").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.ONE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.A)
            .build();
    ret.add(c1);
    return ret;
  }
}
