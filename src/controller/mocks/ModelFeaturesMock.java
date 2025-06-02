package controller.mocks;

import controller.ModelFeatures;
import model.Player;

/**
 * represents a ModelFeatures object that does nothing for testing purposes.
 */
public class ModelFeaturesMock implements ModelFeatures {

  @Override
  public void changeTurn(Player playerTurn) {
    //does nothing here for mock
  }

  @Override
  public void gameOver() {
    //does nothing here for mock
  }
}
