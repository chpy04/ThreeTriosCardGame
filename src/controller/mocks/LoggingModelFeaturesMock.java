package controller.mocks;

import controller.ModelFeatures;
import model.Player;

/**
 * Represents a model features mock logs calls to the functions.
 */
public class LoggingModelFeaturesMock implements ModelFeatures {

  StringBuilder sb;

  /**
   * Creates this mock with a sb to append logs too.
   * @param sb the string builder to append logs to
   */
  public LoggingModelFeaturesMock(StringBuilder sb) {
    this.sb = sb;
  }

  @Override
  public void changeTurn(Player playerTurn) {
    sb.append("Tried to change turn to ").append(playerTurn).append("\n");
  }

  @Override
  public void gameOver() {
    sb.append("Tried to signal the game was over").append("\n");
  }
}
