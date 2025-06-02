package provider.model.grid;

import java.util.List;

import provider.model.grid.cell.Cell;

/**
 * Interface for Grid.
 */
public interface IGrid {
  /**
   * Returns the number of rows in this grid.
   *
   * @return the number of rows in this grid
   */
  int getRows();

  /**
   * Returns the number of columns in this grid.
   *
   * @return the number of columns in this grid
   */
  int getCols();

  /**
   * Sets the cell at the given coordinate to the given cell.
   *
   * @param coord the position of the cell to be changed
   * @param cell  the cell to be changed to
   * @throws IllegalArgumentException if coordinate is null
   *                                  or does not exist in this grid.
   */
  void setCell(ICoordinate coord, Cell cell);

  /**
   * Returns the cell at the given coordinate position on this grid.
   *
   * @param coord the position of the cell to be returned
   * @return the cell at the given coordinate position.
   * @throws IllegalArgumentException if coordinate is null
   *                                  or does not exist in this grid
   */
  Cell getCell(ICoordinate coord);

  // issue w flipping cards
  Cell getUnclonedCell(ICoordinate coord);

  /**
   * Returns a list of all adjacent coordinates of the given coordinate in the
   * order: North, South, East, West.
   * Diagonal coordinates are not adjacent.
   *
   * @param coord the position of the coordinate to calculate the
   *              adjacent coordinates for
   * @return a list of Coordinate that are adjacent to the given coordinate
   */
  List<ICoordinate> getAdjCoord(ICoordinate coord);

  /**
   * Returns the number of empty card cells in the grid.
   *
   * @return empty card cells
   */
  int getEmpty();

  /**
   * Creates a copy of the grid to avoid direct mutation to the grid.
   */
  IGrid copy();
}
