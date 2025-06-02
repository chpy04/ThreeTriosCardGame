package controller.mocks;

import controller.PlayerActions;

/**
 * Represents a player actions that logs when a card is played.
 */
public class LoggingPlayerActionsMock implements PlayerActions {
  StringBuilder sb;

  /**
   * Creates this mock with a string builder to append the log too.
   * @param sb what the logs are appended to
   */
  public LoggingPlayerActionsMock(StringBuilder sb) {
    this.sb = sb;
  }

  @Override
  public void playCard(int cardIdx, int row, int col) {
    sb.append("cardIdx=" + cardIdx + " row=" + row + " col=" + col).append("\n");
  }
}
