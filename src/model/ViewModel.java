package model;

import java.util.List;

/**
 * A ReadOnlyBoard that cannot be casted into a Board.
 */
public class ViewModel implements ReadOnlyBoard {

  ReadOnlyBoard delegate;

  /**
   * Creates this board that can't be casted with the given delegate.
   * @param delegate the board that will be used as the delegate
   */
  public ViewModel(ReadOnlyBoard delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean isGameOver() {
    return delegate.isGameOver();
  }

  @Override
  public Player gameWinner() {
    return delegate.gameWinner();
  }

  @Override
  public Slot[][] getGrid() {
    return delegate.getGrid();
  }

  @Override
  public int gameWidth() {
    return delegate.gameWidth();
  }

  @Override
  public int gameHeight() {
    return delegate.gameHeight();
  }

  @Override
  public Slot getCoord(int x, int y) {
    return delegate.getCoord(x, y);
  }

  @Override
  public Player getCellOwner(int x, int y) {
    return delegate.getCellOwner(x, y);
  }

  @Override
  public boolean isMoveLegal(int x, int y) {
    return delegate.isMoveLegal(x, y);
  }

  @Override
  public int score(Player player) {
    return delegate.score(player);
  }

  @Override
  public int possibleCardsFlipped(Card card, int x, int y) {
    return delegate.possibleCardsFlipped(card, x, y);
  }

  @Override
  public Player curPlayer() {
    return delegate.curPlayer();
  }

  @Override
  public List<Card> getHand(Player player) {
    return delegate.getHand(player);
  }
}
