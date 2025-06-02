package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import controller.mocks.LoggingModelFeaturesMock;
import controller.mocks.LoggingPlayToBoardMock;
import controller.mocks.LoggingPlayerActionsMock;
import controller.mocks.LoggingViewMock;
import controller.mocks.ModelFeaturesMock;
import model.Board;
import model.Card;
import model.Player;
import model.Slot;
import model.ThreeTriosBoard;
import model.stategy.MostFlipped;

import static org.junit.Assert.assertEquals;

/**
 * A testing class for game players.
 */
public class GamePlayerTest {
  GamePlayer human;
  GamePlayer ai;
  ConfigParser parser = new ConfigParser();
  Board model;
  StringBuilder featuresLog;
  Board nBoardModel;
  Board playerActionsModel;
  StringBuilder playerActionsLog;
  PlayerActions playerActionsController;
  Board viewFeaturesModel;
  StringBuilder viewFeaturesLog;
  ViewFeatures viewFeatures;

  @Before
  public void setup() {
    this.model = new ThreeTriosBoard(getBoard("separatedBoard.config"),
            getCards("17cards.config"), new Random(4), false);
    this.human = new HumanPlayer(model);
    this.ai = new StrategyPlayer(model, new MostFlipped());
    featuresLog = new StringBuilder();
    nBoardModel = new ThreeTriosBoard(getBoard("nboard.config"),
            getCards("17cards.config"), new Random(4), false);
    playerActionsLog = new StringBuilder();
    playerActionsModel = new ThreeTriosBoard(getBoard("nboard.config"),
            getCards("17cards.config"), new Random(4), false);
    playerActionsController = new Controller(playerActionsModel,
     new HumanPlayer(playerActionsModel),
                new LoggingViewMock(playerActionsLog));
    viewFeaturesLog = new StringBuilder();
    viewFeaturesModel = new ThreeTriosBoard(getBoard("nboard.config"),
            getCards("17cards.config"), new Random(4), false);
    viewFeatures = new Controller(viewFeaturesModel, new HumanPlayer(viewFeaturesModel),
            new LoggingViewMock(viewFeaturesLog));
  }

  /**
   * Given a name of a file in the docs folder, produces the 2d array of slots.
   * @param name name of the file
   * @return the array of slots as defined by the file
   */
  public Slot[][] getBoard(String name) {
    String cardsPath = "docs" + File.separator + name;
    Scanner boardReader;
    try {
      boardReader = new Scanner(new File(cardsPath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file name does not exist");
    }

    return parser.parseBoard(boardReader);
  }

  /**
   * Generates the cards based on the name of the config file passed in.
   * @param name the name of the card config file
   * @return the cards from the config file
   */
  public List<Card> getCards(String name) {
    String cardsPath = "docs" + File.separator + name;
    Scanner cardReader;
    try {
      cardReader = new Scanner(new File(cardsPath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file name does not exist");
    }

    return parser.parseCards(cardReader);
  }

  @Test(expected = NullPointerException.class)
  public void testHumanPlayerImproperObjectConstruction() {
    human = new HumanPlayer(null);
  }

  @Test(expected = NullPointerException.class)
  public void testStrategyPlayerImproperObjectConstructionModelNull() {
    ai = new StrategyPlayer(null, new MostFlipped());
  }

  @Test(expected = NullPointerException.class)
  public void testStrategyPlayerImproperObjectConstructionStrategyNull() {
    ai = new StrategyPlayer(new LoggingPlayToBoardMock(new StringBuilder()), null);
  }

  @Test
  public void testMakeMoveHumanPlayer() {
    StringBuilder logs = new StringBuilder();
    this.human.addListener(new LoggingPlayerActionsMock(logs));
    this.human.makeMove(Player.A);
    this.human.makeMove(Player.B);
    this.human.makeMove(Player.A);
    assertEquals(logs.toString(), "");
  }

  @Test
  public void testMakeMoveStrategyPlayer() {
    StringBuilder logs = new StringBuilder();
    this.ai.addListener(new LoggingPlayerActionsMock(logs));
    ai.makeMove(Player.A);
    ai.makeMove(Player.B);
    ai.makeMove(Player.A);

    assertEquals(logs.toString(), "cardIdx=0 row=0 col=1\n" +
            "cardIdx=0 row=0 col=1\n" +
            "cardIdx=0 row=0 col=1\n");

    model.startGame();
    model.playToBoard(0, 1, 0);
    ai.makeMove(Player.A);
    ai.makeMove(Player.B);
    assertEquals(logs.toString(), "cardIdx=0 row=0 col=1\n" +
            "cardIdx=0 row=0 col=1\n" +
            "cardIdx=0 row=0 col=1\n" +
            "cardIdx=0 row=0 col=2\n" +
            "cardIdx=0 row=0 col=2\n");
  }

  @Test
  public void testModelCallsFeaturesInStartGame() {
    StringBuilder featuresLogsA = new StringBuilder();
    StringBuilder featuresLogsB = new StringBuilder();
    ModelFeatures featuresA = new LoggingModelFeaturesMock(featuresLogsA);
    ModelFeatures featuresB = new LoggingModelFeaturesMock(featuresLogsB);
    nBoardModel.addListener(featuresA);
    nBoardModel.addListener(featuresB);
    nBoardModel.startGame();
    assertEquals(featuresLogsA.toString(), "Tried to change turn to RED\n");
    assertEquals(featuresLogsB.toString(), "");


  }

  @Test
  public void testModelCallsFeaturesDuringPlayToBoard() {
    StringBuilder featuresLogsA = new StringBuilder();
    StringBuilder featuresLogsB = new StringBuilder();
    ModelFeatures featuresA = new LoggingModelFeaturesMock(featuresLogsA);
    ModelFeatures featuresB = new LoggingModelFeaturesMock(featuresLogsB);
    nBoardModel.addListener(featuresA);
    nBoardModel.addListener(featuresB);
    nBoardModel.startGame();
    nBoardModel.playToBoard(0, 0, 0);
    nBoardModel.playToBoard(1, 0, 2);
    nBoardModel.playToBoard(1, 1, 0);
    assertEquals(featuresLogsA.toString(), "Tried to change turn to RED\n" +
            "Tried to change turn to RED\n");
    assertEquals(featuresLogsB.toString(), "Tried to change turn to BLUE\n" +
            "Tried to change turn to BLUE\n");

  }


  @Test
  public void testModelCallsFeaturesWhenGameOver() {
    nBoardModel = new ThreeTriosBoard(getBoard("separatedboard.config"),
            getCards("17cards.config"), new Random(4), false);

    StringBuilder featuresLogsA = new StringBuilder();
    StringBuilder featuresLogsB = new StringBuilder();
    ModelFeatures featuresA = new LoggingModelFeaturesMock(featuresLogsA);
    ModelFeatures featuresB = new LoggingModelFeaturesMock(featuresLogsB);
    nBoardModel.addListener(featuresA);
    nBoardModel.addListener(featuresB);
    nBoardModel.startGame();
    nBoardModel.playToBoard(0, 0, 3);
    nBoardModel.playToBoard(0, 1, 3);
    nBoardModel.playToBoard(0, 2, 3);
    nBoardModel.playToBoard(0, 3, 3);
    nBoardModel.playToBoard(0, 1, 0);
    nBoardModel.playToBoard(0, 2, 0);
    nBoardModel.playToBoard(0, 3, 0);

    assertEquals(featuresLogsA.toString(), "Tried to change turn to RED\n" +
            "Tried to change turn to RED\n" +
            "Tried to change turn to RED\n" +
            "Tried to change turn to RED\n" +
            "Tried to signal the game was over\n");

    assertEquals(featuresLogsB.toString(), "Tried to change turn to BLUE\n" +
            "Tried to change turn to BLUE\n" +
            "Tried to change turn to BLUE\n" +
            "Tried to signal the game was over\n");
  }

  @Test
  public void testPlayCardNotTheirTurn() {

    playerActionsController.playCard(0, 0, 0);
    assertEquals(playerActionsLog.toString(), "");
  }

  @Test
  public void testPlayCardTheirTurnInvalidSpotViewSendsMessage() {

    playerActionsModel.addListener(new ModelFeaturesMock());
    playerActionsModel.startGame();

    //get it to be waiting for input
    ((Controller) playerActionsController).changeTurn(Player.A);


    playerActionsController.playCard(0, 0, 3);

    assertEquals(playerActionsLog.toString(),
            "Tried to Refresh with selected card at index -1\n"
            + "Tried to Refresh with selected card at index -1\n"
            + "Tried to send message: Cannot Play Card there\n");

  }

  @Test
  public void testPlayCardPassesCorrectValuesToModel() {
    playerActionsModel = new LoggingPlayToBoardMock(playerActionsLog);
    playerActionsController = new Controller(playerActionsModel,
    new HumanPlayer(playerActionsModel),
            new LoggingViewMock(new StringBuilder()));

    //get it to be waiting for input
    ((Controller) playerActionsController).changeTurn(Player.A);
    playerActionsController.playCard(3, 0, 0);

    ((Controller) playerActionsController).changeTurn(Player.A);
    playerActionsController.playCard(3, 0, 1);

    ((Controller) playerActionsController).changeTurn(Player.A);
    playerActionsController.playCard(3, 1, 0);

    ((Controller) playerActionsController).changeTurn(Player.A);
    playerActionsController.playCard(3, 2, 0);

    assertEquals(playerActionsLog.toString(), "Tried to play to (0, 0) with handIndex=3\n" +
            "Tried to play to (1, 0) with handIndex=3\n" +
            "Tried to play to (0, 1) with handIndex=3\n" +
            "Tried to play to (0, 2) with handIndex=3\n");
    System.out.println(playerActionsLog.toString());
  }

  @Test
  public void testPlayCardPassesCorrectValuesToModelEvenIfInvalid() {
    playerActionsModel = new LoggingPlayToBoardMock(playerActionsLog);
    playerActionsController = new Controller(playerActionsModel,
    new HumanPlayer(playerActionsModel),
            new LoggingViewMock(new StringBuilder()));

    //get it to be waiting for input
    ((Controller) playerActionsController).changeTurn(Player.A);

    playerActionsController.playCard(-1, 450, 12);
    assertEquals(playerActionsLog.toString(), "Tried to play to (12, 450) with handIndex=-1\n");
  }

  @Test
  public void testHandleGridPlaySendsCorrectValueToModel() {
    StringBuilder mockLogs = new StringBuilder();
    viewFeatures = new Controller(new LoggingPlayToBoardMock(mockLogs),
    new HumanPlayer(viewFeaturesModel),
            new LoggingViewMock(viewFeaturesLog));

    //allows it be waiting for input
    ((Controller) viewFeatures).changeTurn(Player.A);



    //makes sure there is a card to select
    viewFeatures.handleSelectCard(Player.A, 0);
    viewFeatures.handleGridPlay(0, 0);


    assertEquals(mockLogs.toString(), "Tried to play to (0, 0) with handIndex=0\n");

  }

  @Test
  public void testHandleGridPlayNoCardSelectedYet() {
    StringBuilder mockLogs = new StringBuilder();
    viewFeatures = new Controller(new LoggingPlayToBoardMock(mockLogs),
    new HumanPlayer(viewFeaturesModel),
            new LoggingViewMock(viewFeaturesLog));

    //allows it be waiting for input
    ((Controller) viewFeatures).changeTurn(Player.A);
    viewFeatures.handleGridPlay(0, 0);
    System.out.println(viewFeaturesLog.toString());

    assertEquals(viewFeaturesLog.toString(),
            "Tried to Refresh with selected card at index -1\n"
            + "Tried to send message: Please Select a card first\n");

  }
}
