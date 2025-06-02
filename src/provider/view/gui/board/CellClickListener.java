package provider.view.gui.board;

import provider.model.grid.ICoordinate;

/**
 * Interface for receiving cell click events from the board.
 */
interface CellClickListener {
  /**
   * Called when a cell on the board is clicked.
   */
  void onCellClicked(ICoordinate coord);
}