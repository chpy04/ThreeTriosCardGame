package provider.view.gui.board;

import provider.model.grid.ICoordinate;

/**
 * Interface representing the cell clicked's coordinates, used to deal with issues of incorrect
 * start states.
 */
public interface ClickCallBack {
  void cellClicked(ICoordinate coord);
}
