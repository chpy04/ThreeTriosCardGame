package provider.view;

import java.awt.Point;
import java.io.IOException;

/**
 * Represents a view for a game of Three Trios.
 */
public interface TrioView {
  /**
   * Renders the model.
   */
  void render() throws IOException;

  /**
   * Add Observer to be notified when events happen.
   */
  void addPlayerActionListener(PlayerAction listener);

  /**
   * Displays an error message to the user.
   * @param message the error message to display
   */
  void showError(String message);

  /**
   * Sets the position of the view window on the screen.
   * @param point the location to place the window
   */
  void setViewPosition(Point point);

  /**
   * Makes the view visible or invisible.
   * @param visible true to show the view, false to hide it
   */
  void setVisible(boolean visible);

  /**
   * Enables or disables user input based on whether it's this player's turn.
   * @param enabled true if it's this player's turn, false otherwise
   */
  void setInputEnabled(boolean enabled);

}
