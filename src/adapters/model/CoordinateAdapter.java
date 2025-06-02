package adapters.model;

import model.stategy.IMove;
import model.stategy.Move;
import provider.model.grid.ICoordinate;

/**
 * An adapter that converts a given IMove into an ICoordinate that can be used in the
 * providers code.
 */
public class CoordinateAdapter implements ICoordinate {

  private final IMove delegate;

  /**
   * Creates this ICoordinate with the given row and col indexes.
   * @param row the row index
   * @param col the column index
   */
  public CoordinateAdapter(int row, int col) {
    this.delegate = new Move(0, row, col, 0);
  }

  @Override
  public int getRow() {
    return delegate.xCord();
  }

  @Override
  public int getCol() {
    return delegate.yCord();
  }
}
