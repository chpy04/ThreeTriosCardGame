package controller;

import java.util.Objects;

import model.Player;
import view.TTView;
import model.Board;

/**
 * A controller class representing a player playing a game that communicates with a given model
 * and view to play a three trios game.
 */
public class Controller implements PlayerActions, ModelFeatures, ViewFeatures {
  private final Board model;
  private final TTView view;
  private final GamePlayer player;
  private int selectedCard;
  private boolean waitingForInput;

  /**
   * Creates this controller, sets up callbacks to be able to play, and makes the view visable.
   * @param model the model used to play
   * @param gamePlayer the player represented by this controller
   * @param view the view used to display the game
   */
  public Controller(Board model, GamePlayer gamePlayer, TTView view) {
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.player = Objects.requireNonNull(gamePlayer);
    this.player.addListener(this);
    this.view.addListener(this);
    this.model.addListener(this);
    this.selectedCard = -1;
    this.waitingForInput = false;
    view.makeVisible();
  }


  @Override
  public void playCard(int cardIdx, int row, int col) {
    if (!this.waitingForInput) {
      return;
    }

    try {
      this.waitingForInput = false;
      this.model.playToBoard(cardIdx, col, row);
      selectedCard = -1;
      this.view.refresh(selectedCard);
    } catch (IllegalArgumentException ignored) {
      view.showMessage("Cannot Play Card there");
      this.waitingForInput = true;
    }

  }

  @Override
  public void handleGridPlay(int row, int col) {
    if (this.model.isGameOver()) {
      this.view.showMessage("Game Already Over");
      return;
    }
    if (!this.waitingForInput) {
      this.view.showMessage("Not your turn");
      return;
    }
    if (selectedCard == -1) {
      this.view.showMessage("Please Select a card first");
      return;
    }
    this.playCard(this.selectedCard, row, col);
  }

  @Override
  public void handleSelectCard(Player player, int handIdx) {
    if (this.model.isGameOver()) {
      this.view.showMessage("Game Already Over");
      return;
    }
    if (!this.waitingForInput) {
      this.view.showMessage("Not your turn");
      return;
    }
    if (!player.equals(model.curPlayer())) {
      this.view.showMessage("Cannot select cards from the other deck");
      return;
    }
    if (selectedCard == handIdx) {
      this.selectedCard = -1;
      this.view.refresh(-1);
    } else {
      this.selectedCard = handIdx;
      this.view.refresh(handIdx);
    }
  }

  @Override
  public void changeTurn(Player playerTurn) {
    this.waitingForInput = true;
    this.player.makeMove(playerTurn);
    this.view.refresh(-1);
  }

  @Override
  public void gameOver() {
    if (!model.isGameOver()) {
      throw new IllegalArgumentException("game isn't actually over");
    }
    this.view.refresh(-1);
    Player winner = model.gameWinner();
    if (winner == Player.NONE) {
      this.view.showMessage("Tie Game. You each had a score of " + model.score(Player.A));
    } else {
      this.view.showMessage(winner + " won with a score of " + model.score(winner));
    }
  }
}
