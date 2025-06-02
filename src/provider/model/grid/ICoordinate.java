package provider.model.grid;

/**
 * Interface for Coordinate.
 */
public interface ICoordinate {
  /**
   * Returns the row of this Coordinate point.
   *
   * @return the row
   */
  int getRow();

  /**
   * Returns the column of this Coordinate point.
   *
   * @return the column
   */
  int getCol();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
