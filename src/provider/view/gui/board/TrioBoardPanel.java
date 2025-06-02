package provider.view.gui.board;

import java.awt.Dimension;
import provider.model.grid.ICoordinate;

/**
 * Interface to represent TrioBoardPanel, used to update the game panels.
 */
public interface TrioBoardPanel {
  /**
   *   Sets the preferred size of this board panel.
   */
  void setPreferredSize(Dimension d);

  /**
   * Processes a move selection on the board at the coordinate.
   */
  void setBoardMove(ICoordinate coord);

  /**
   *   Updates the state of the board panel to reflect the  game state.
   */
  void update();

  /**
   * Adds a listener to be notified when a cell is clicked.
   *
   * @param listener the callback to notify of cell clicks
   */
  void addCellClickListener(CellClickListener listener);
}