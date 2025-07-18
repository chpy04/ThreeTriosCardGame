package model.mocks;

import java.io.IOException;
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
 * A mock that records checked positions and those that have been flipped.
 */
public class RecordCheckedPositionsAndFlippedMock implements Board {

  private final Appendable out;
  private final int width;
  private final int height;

  /**
   * constructs the mock object.
   * @param out the appendable to append output to
   * @param width the width of the board
   * @param height the height of the board
   */
  public RecordCheckedPositionsAndFlippedMock(Appendable out, int width, int height) {
    this.out = out;
    this.width = width;
    this.height = height;
  }

  @Override
  public void playToBoard(int handIndex, int x, int y) {
    //empty becasue we don't need any info here
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
    try {
      this.out.append(String.format("Checked: %d, %d\n", x, y));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  @Override
  public int score(Player player) {
    return 0;
  }

  @Override
  public int possibleCardsFlipped(Card card, int x, int y) {
    try {
      this.out.append(String.format("Looking for cards flipped by %s at (%d, %d)\n",
              card.toString(), x, y));
    } catch (IOException e) {
      throw new RuntimeException(e);
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
