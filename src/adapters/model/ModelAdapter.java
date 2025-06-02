package adapters.model;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Card;
import model.Player;
import model.Slot;
import provider.model.cards.ICard;
import provider.model.grid.ICoordinate;
import provider.model.grid.IGrid;
import provider.model.ReadonlyTrioGame;
import provider.model.grid.cell.Cell;
import provider.model.player.PlayerColor;

/**
 * An adapter that converts a given Board into a ReadonlyTrioGame, and an IGrid that can be used
 * in the provider's code.
 */
public class ModelAdapter implements ReadonlyTrioGame, IGrid {

  private final Board delegate;

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate a Board that will handle equivalent calls
   */
  public ModelAdapter(Board delegate) {
    this.delegate = delegate;
  }

  @Override
  public IGrid getGrid() {
    return this;
  }

  @Override
  public int getWidth() {
    return this.delegate.gameWidth();
  }

  @Override
  public int getHeight() {
    return this.delegate.gameHeight();
  }

  @Override
  public PlayerColor getCurrPlayer() {
    return new PlayerAdapter().modelToProvider(delegate.curPlayer());
  }

  @Override
  public List<ICard> getHand(PlayerColor color) {
    List<ICard> ret = new ArrayList<>();
    List<Card> modelCards =  delegate.getHand(new PlayerAdapter().providerToModel(color));
    for (Card card : modelCards) {
      ret.add(new ModelToProviderCardAdapter(card));
    }
    return ret;
  }

  @Override
  public PlayerColor getWinner() {
    return new PlayerAdapter().modelToProvider(delegate.gameWinner());
  }

  @Override
  public int getScore(PlayerColor player) {
    return delegate.score(new PlayerAdapter().providerToModel(player));
  }

  @Override
  public boolean isLegalMove(ICard card, ICoordinate coord) {
    return delegate.isMoveLegal(coord.getCol(), coord.getRow());
  }

  @Override
  public boolean isGameOver() {
    return delegate.isGameOver();
  }

  @Override
  public boolean isGameStarted() {
    return true;
  }

  @Override
  public int getRows() {
    return delegate.gameHeight();
  }

  @Override
  public int getCols() {
    return delegate.gameWidth();
  }

  @Override
  public void setCell(ICoordinate coord, Cell cell) {
    List<Card> hand = delegate.getHand(delegate.curPlayer());
    String cardName = cell.getCard().getName();
    for (int i = 0; i < hand.size(); i++) {
      if (hand.get(i).toString().substring(0, hand.get(i).toString().indexOf(" "))
              .equals(cardName)) {
        delegate.playToBoard(i, coord.getCol(), coord.getRow());
        return;
      }
    }
    throw new IllegalArgumentException("card is not in the current players hand");
  }

  @Override
  public Cell getCell(ICoordinate coord) {
    return new SlotToCellAdapter(delegate.getGrid()[coord.getRow()][coord.getCol()], coord);
  }

  @Override
  public Cell getUnclonedCell(ICoordinate coord) {
    return new SlotToCellAdapter(delegate.getGrid()[coord.getRow()][coord.getCol()], coord);
  }

  @Override
  public List<ICoordinate> getAdjCoord(ICoordinate coord) {
    List<ICoordinate> ret = new ArrayList<>();
    int x = coord.getCol();
    int y = coord.getRow();

    if (x > 0) {
      ret.add(new CoordinateAdapter(x - 1, y));
    }

    if (y > 0) {
      ret.add(new CoordinateAdapter(x, y - 1));
    }

    if (x < delegate.gameWidth() - 1) {
      ret.add(new CoordinateAdapter(x + 1, y));
    }

    if (y < delegate.gameHeight() - 1) {
      ret.add(new CoordinateAdapter(x, y + 1));
    }

    return ret;
  }

  @Override
  public int getEmpty() {
    Slot[][] grid = delegate.getGrid();
    int ret = 0;
    for (Slot[] row : grid) {
      for (Slot cell : row) {
        if (cell.canPlayCard()) {
          ret++;
        }
      }
    }
    return ret;
  }

  @Override
  public IGrid copy() {
    return this;
  }

  @Override
  public PlayerColor getCellOwner(ICoordinate coord) {
    return new PlayerAdapter()
            .modelToProvider(delegate.getCellOwner(coord.getCol(), coord.getRow()));
  }

  @Override
  public int howManyFlip(ICard card, ICoordinate coord) {
    Player curPlayer = delegate.getCellOwner(coord.getCol(), coord.getRow());
    return delegate.possibleCardsFlipped(new ProviderToModelCardAdapter(card, curPlayer),
            coord.getCol(), coord.getRow());
  }
}
