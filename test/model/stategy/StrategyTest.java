package model.stategy;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Random;

import model.Board;
import controller.ConfigParser;
import model.mocks.FlippedCardsGreatestAtCoordMock;
import model.mocks.NoOpenCornersMock;
import model.mocks.NoValidMovesMock;
import model.mocks.OnlyOneValidMoveMock;
import model.Player;
import model.mocks.RecordCheckedPositionsAndFlippedMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Class to test each of the 5 different strategies.
 */
public class StrategyTest {
  private Board mtBoard;
  private Board halfBoard;
  private Board fullBoard;
  private List<IMove> selectiveMoveList;
  private List<IMove> openMoveList;
  private InfallibleStategy leastChance;
  private InfallibleStategy corners;
  private InfallibleStategy mostFlipped;
  private InfallibleStategy minimaxCorners;
  private InfallibleStategy minimaxFlipped;
  private InfallibleStategy minimaxLeastFlipped;
  private InfallibleStategy combinedFlipCorner;
  private InfallibleStategy combinedLeastCorner;
  private InfallibleStategy combinedMiniFlip;
  IMove move1;
  IMove move2;
  IMove move3;

  @Before
  public void setUp() {
    String boardPath = "docs" + File.separator + "strategyTest.config";
    String cardsPath = "docs" + File.separator + "strategyCards.config";

    selectiveMoveList = List.of(new Move(1, 0, 1, 0),
            new Move(0, 2, 0, 0 ),
            new Move(1, 2, 2, 0),
            new Move(50, 50, 50, 50));

    openMoveList = List.of(new Move(1, 0, 1, 0),
            new Move(0, 1, 0, 0 ),
            new Move(1, 3, 2, 0),
            new Move(1, 3, 1, 0),
            new Move(0, 2, 1, 0),
            new Move(1, 0, 2, 0));

    mtBoard = new ConfigParser().createModelFromFiles(boardPath, cardsPath,
            new Random(1));
    halfBoard = new ConfigParser().createModelFromFiles(boardPath, cardsPath,
            new Random(1));
    fullBoard = new ConfigParser().createModelFromFiles(boardPath, cardsPath,
            new Random(1));


    mtBoard.startGame();


    halfBoard.startGame();


    fullBoard.startGame();

    halfBoard.playToBoard(3, 3, 0);
    fullBoard.playToBoard(3, 3, 0);

    halfBoard.playToBoard(4, 0, 2);
    fullBoard.playToBoard(4, 0, 2);

    halfBoard.playToBoard(3, 1, 1);
    fullBoard.playToBoard(3, 1, 1);

    halfBoard.playToBoard(0, 2, 1);
    fullBoard.playToBoard(0, 2, 1);

    fullBoard.playToBoard(3, 2, 2);
    fullBoard.playToBoard(1, 3, 1);
    fullBoard.playToBoard(0, 0, 0);

    leastChance = new LeastChanceOfBeingFlipped();
    corners = new GoForCorners();
    mostFlipped = new MostFlipped();

    minimaxLeastFlipped = new Minimax(new LeastChanceOfBeingFlipped());
    minimaxFlipped = new Minimax(new MostFlipped());
    minimaxCorners = new Minimax(new GoForCorners());

    combinedFlipCorner = new CombinedStrategy(mostFlipped, corners);
    combinedLeastCorner = new CombinedStrategy(leastChance, corners);
    combinedMiniFlip = new CombinedStrategy(new Minimax(mostFlipped), combinedFlipCorner);
    this.move1 = new Move(0, 3, 5, 72);
    this.move2 = new Move(0, 3, 5, 72);
    this.move3 = new Move(-90234, 500000, -1, -8);
  }

  /**
   * Tests multiple strategies against a board with only one open square.
   * @param strat the strategy to test.
   */
  public void onlyOneOpenSquare(InfallibleStategy strat) {
    Board oneOpen = new OnlyOneValidMoveMock(2, 3, 5, 5);
    IMove chosenMove = strat.chooseMove(oneOpen, Player.A);
    assertEquals(chosenMove.xCord(), 2);
    assertEquals(chosenMove.yCord(), 3);
  }

  /**
   * Tests multiple strategies against a board with no valid moves.
   * @param strat the strategy to test
   */
  public void noValidMovesThrows(InfallibleStategy strat) {
    Board noOpen = new NoValidMovesMock();
    assertThrows(IllegalStateException.class, () -> strat.chooseMove(noOpen, Player.A));
  }

  @Test
  public void chooseMoveMostFlipped() {
    onlyOneOpenSquare(mostFlipped);
    noValidMovesThrows(mostFlipped);
    assertEquals(mostFlipped.chooseMove(mtBoard, Player.A),
            new Move(0, 0, 0, 0));

    assertEquals(mostFlipped.chooseMove(halfBoard, Player.A),
            new Move(1, 2, 2, 2));
    assertEquals(mostFlipped.chooseMove(halfBoard, Player.B),
            new Move(0, 0, 0, 0));

    assertEquals(mostFlipped.chooseMove(fullBoard, Player.A),
            new Move(0, 1, 0, 0));
    assertEquals(mostFlipped.chooseMove(fullBoard, Player.B),
            new Move(0, 1, 0, 1));
  }

  @Test
  public void chooseFromOptionsMostFlipped() {
    assertEquals(mostFlipped.chooseFromOptions(selectiveMoveList, mtBoard, Player.A),
            List.of(new Move(1, 0, 1, 0),
                    new Move(1, 2, 2, 0)));
    assertEquals(mostFlipped.chooseFromOptions(selectiveMoveList, fullBoard, Player.B),
            List.of(new Move(1, 0, 1, 0)));
    assertEquals(mostFlipped.chooseFromOptions(openMoveList, halfBoard, Player.A),
            List.of(new Move(1, 0, 1, 0),
                    new Move(0, 1, 0, 0),
                    new Move(1, 3, 2, 0),
                    new Move(1, 3, 1, 0)));
  }

  @Test
  public void allBestMoveCandidatesMostFlipped() {
    assertEquals(mostFlipped.allBestMoveCandidates(halfBoard, Player.B),
            List.of(new Move(0, 0, 0, 0),
                    new Move(1, 0, 0, 0),
                    new Move(2, 0, 0, 0),
                    new Move(0, 1, 0, 0),
                    new Move(1, 1, 0, 0),
                    new Move(2, 1, 0, 0),
                    new Move(0, 0, 1, 0),
                    new Move(1, 0, 1, 0),
                    new Move(2, 0, 1, 0),
                    new Move(0, 3, 1, 0),
                    new Move(1, 3, 1, 0),
                    new Move(2, 3, 1, 0),
                    new Move(0, 2, 2, 0),
                    new Move(1, 2, 2, 0),
                    new Move(2, 2, 2, 0),
                    new Move(0, 3, 2, 0),
                    new Move(1, 3, 2, 0),
                    new Move(2, 3, 2, 0)));
    assertEquals(mostFlipped.allBestMoveCandidates(halfBoard, Player.A),
            List.of(new Move(1, 2, 2, 2),
                    new Move(3, 2, 2, 2)));
    assertEquals(mostFlipped.allBestMoveCandidates(fullBoard, Player.B),
            List.of(new Move(0, 1, 0, 1),
                    new Move(0, 3, 2, 1)));

    Board flippedCardsAtCord = new FlippedCardsGreatestAtCoordMock(1, 2, 3, 4);
    assertEquals(mostFlipped.allBestMoveCandidates(flippedCardsAtCord, Player.A),
            List.of(new Move(0, 1, 2, 1)));
  }

  @Test
  public void observesAllCorners() {
    Appendable out = new StringBuilder();
    Board recordChecks = new RecordCheckedPositionsAndFlippedMock(out, 4, 3);
    corners.chooseMove(recordChecks, Player.A);
    assertTrue(out.toString().contains("Checked: 0, 0"));
    assertTrue(out.toString().contains("Checked: 3, 0"));
    assertTrue(out.toString().contains("Checked: 0, 2"));
    assertTrue(out.toString().contains("Checked: 3, 2"));
  }

  @Test
  public void chooseMoveGoForCorners() {
    onlyOneOpenSquare(corners);
    noValidMovesThrows(corners);
    assertEquals(corners.chooseMove(mtBoard, Player.A),
            new Move(3, 3, 0, 18));
    assertEquals(corners.chooseMove(mtBoard, Player.B),
            new Move(4, 0, 2, 19));

    assertEquals(corners.chooseMove(halfBoard, Player.A),
            new Move(3, 3, 2, 13));
    assertEquals(corners.chooseMove(halfBoard, Player.B),
            new Move(1, 0, 0, 17));

    assertEquals(corners.chooseMove(fullBoard, Player.A),
            new Move(0, 3, 2, 11));
    assertEquals(corners.chooseMove(fullBoard, Player.B),
            new Move(0, 3, 2, 12));
  }

  @Test
  public void chooseFromOptionsGoForCorners() {
    assertEquals(corners.chooseFromOptions(selectiveMoveList, mtBoard, Player.A),
            List.of(new Move(1, 0, 1, 0),
                    new Move(1, 2, 2, 0)));
    assertEquals(corners.chooseFromOptions(selectiveMoveList, fullBoard, Player.B),
            List.of(new Move(1, 0, 1, 0)));
    assertEquals(corners.chooseFromOptions(openMoveList, halfBoard, Player.A),
            List.of(new Move(1, 3, 2, 11)));
  }

  @Test
  public void allBestMoveCandidatesGoForCorners() {
    assertEquals(corners.allBestMoveCandidates(mtBoard, Player.A),
            List.of(new Move(3, 3, 0, 18)));
    assertEquals(corners.allBestMoveCandidates(halfBoard, Player.B),
            List.of(new Move(1, 0, 0, 17)));
    assertEquals(corners.allBestMoveCandidates(fullBoard, Player.B),
            List.of(new Move(0, 3, 2, 12)));

    Board noCorners = new NoOpenCornersMock(3, 4);
    //all moves are the same, starts at top left and goes left and then down
    assertEquals(corners.allBestMoveCandidates(noCorners, Player.A),
            List.of(new Move(0, 1, 0, 0),
                    new Move(0, 0, 1, 0),
                    new Move(0, 1, 1, 0),
                    new Move(0, 2, 1, 0),
                    new Move(0, 0, 2, 0),
                    new Move(0, 1, 2, 0),
                    new Move(0, 2, 2, 0),
                    new Move(0, 1, 3, 0)));
  }

  @Test
  public void chooseMoveLeastChanceOfBeingFlipped() {
    onlyOneOpenSquare(leastChance);
    noValidMovesThrows(leastChance);
    assertEquals(leastChance.chooseMove(mtBoard, Player.A),
            new Move(0, 0, 0, 21));

    assertEquals(leastChance.chooseMove(halfBoard, Player.A),
            new Move(0, 0, 0, 13));
    assertEquals(leastChance.chooseMove(halfBoard, Player.B),
            new Move(0, 0, 0, 17));

    assertEquals(leastChance.chooseMove(fullBoard, Player.A),
            new Move(0, 1, 0, 9));
    assertEquals(leastChance.chooseMove(fullBoard, Player.B),
            new Move(0, 1, 0, 9));
  }

  @Test
  public void chooseFromOptionsLeastChanceOfBeingFlipped() {
    assertEquals(leastChance.chooseFromOptions(selectiveMoveList, mtBoard, Player.A),
            List.of(new Move(1, 0, 1, 17)));
    assertEquals(leastChance.chooseFromOptions(selectiveMoveList, fullBoard, Player.B),
            List.of(new Move(1, 0, 1, 9)));
    assertEquals(leastChance.chooseFromOptions(openMoveList, halfBoard, Player.A),
            List.of(new Move(1, 0, 1, 13),
                    new Move(0, 1, 0, 13),
                    new Move(1, 3, 1, 13)));
  }

  @Test
  public void allBestMoveCandidatesLeastChanceOfBeingFlipped() {
    assertEquals(leastChance.allBestMoveCandidates(halfBoard, Player.B),
            List.of(new Move(0, 0, 0, 17),
                    new Move(1, 0, 0, 17),
                    new Move(2, 0, 0, 17),
                    new Move(0, 1, 0, 17),
                    new Move(1, 1, 0, 17),
                    new Move(2, 1, 0, 17),
                    new Move(0, 0, 1, 17),
                    new Move(1, 0, 1, 17),
                    new Move(2, 0, 1, 17),
                    new Move(1, 3, 1, 17),
                    new Move(2, 3, 1, 17),
                    new Move(0, 2, 2, 17),
                    new Move(1, 2, 2, 17)));
    assertEquals(leastChance.allBestMoveCandidates(halfBoard, Player.A),
            List.of(new Move(0, 0, 0, 13),
                    new Move(1, 0, 0, 13),
                    new Move(2, 0, 0, 13),
                    new Move(3, 0, 0, 13),
                    new Move(0, 1, 0, 13),
                    new Move(1, 1, 0, 13),
                    new Move(2, 1, 0, 13),
                    new Move(3, 1, 0, 13),
                    new Move(0, 0, 1, 13),
                    new Move(1, 0, 1, 13),
                    new Move(2, 0, 1, 13),
                    new Move(3, 0, 1, 13),
                    new Move(1, 3, 1, 13)));
    assertEquals(leastChance.allBestMoveCandidates(fullBoard, Player.B),
            List.of(new Move(0, 1, 0, 9),
                    new Move(1, 1, 0, 9),
                    new Move(0, 0, 1, 9),
                    new Move(1, 0, 1, 9),
                    new Move(0, 3, 2, 9),
                    new Move(1, 3, 2, 9)));
  }

  @Test
  public void chooseFromOptionsMiniMax() {
    assertEquals(minimaxFlipped.chooseFromOptions(selectiveMoveList, mtBoard, Player.A),
            List.of(new Move(1, 0, 1, 1),
                    new Move(1, 2, 2, 1)));
    assertEquals(minimaxLeastFlipped.chooseFromOptions(selectiveMoveList, fullBoard, Player.B),
            List.of(new Move(1, 0, 1, 9)));
    assertEquals(minimaxCorners.chooseFromOptions(openMoveList, halfBoard, Player.A),
            List.of(new Move(1, 0, 1, 12),
                    new Move(0, 1, 0, 12),
                    new Move(1, 3, 2, 12),
                    new Move(1, 3, 1, 12)));
  }

  @Test
  public void allBestMoveCandidatesMiniMax() {
    assertEquals(minimaxFlipped.allBestMoveCandidates(halfBoard, Player.B),
            List.of(new Move(0, 2, 2, 0),
                    new Move(1, 2, 2, 0),
                    new Move(2, 2, 2, 0)));
    assertEquals(minimaxFlipped.allBestMoveCandidates(halfBoard, Player.A),
            List.of(new Move(2, 0, 0, 0),
                    new Move(1, 1, 0, 0),
                    new Move(2, 1, 0, 0),
                    new Move(0, 0, 1, 0),
                    new Move(3, 3, 1, 0),
                    new Move(0, 2, 2, 0),
                    new Move(1, 2, 2, 0),
                    new Move(2, 2, 2, 0)));
    assertEquals(minimaxLeastFlipped.allBestMoveCandidates(fullBoard, Player.B),
            List.of(new Move(0, 1, 0, 9),
                    new Move(1, 1, 0, 9),
                    new Move(0, 0, 1, 9),
                    new Move(1, 0, 1, 9),
                    new Move(0, 3, 2, 9),
                    new Move(1, 3, 2, 9)));
  }

  @Test
  public void chooseMoveCombinedStrategy() {
    onlyOneOpenSquare(combinedFlipCorner);
    noValidMovesThrows(combinedFlipCorner);
    onlyOneOpenSquare(combinedLeastCorner);
    noValidMovesThrows(combinedLeastCorner);
    assertEquals(combinedFlipCorner.chooseMove(mtBoard, Player.A),
            new Move(3, 3, 0, 18));

    assertEquals(combinedMiniFlip.chooseMove(halfBoard, Player.A),
            new Move(1, 2, 2, 0));
    assertEquals(combinedLeastCorner.chooseMove(halfBoard, Player.B),
            new Move(1, 0, 0, 17));

    assertEquals(combinedLeastCorner.chooseMove(fullBoard, Player.A),
            new Move(0, 3, 2, 11));
    assertEquals(combinedFlipCorner.chooseMove(fullBoard, Player.B),
            new Move(0, 3, 2, 12));
  }

  @Test
  public void allBestMoveCandidatesCombinedStrategy() {
    assertEquals(combinedLeastCorner.chooseFromOptions(selectiveMoveList, mtBoard, Player.A),
            List.of(new Move(1, 0, 1, 0)));
    assertEquals(combinedMiniFlip.chooseFromOptions(selectiveMoveList, fullBoard, Player.B),
            List.of(new Move(1, 0, 1, 0)));
    assertEquals(combinedFlipCorner.chooseFromOptions(openMoveList, halfBoard, Player.A),
            List.of(new Move(1, 3, 2, 11)));
  }

  @Test
  public void chooseFromOptionsCombinedStrategy() {
    assertEquals(combinedFlipCorner.allBestMoveCandidates(halfBoard, Player.B),
            List.of(new Move(1, 0, 0, 17)));
    assertEquals(combinedMiniFlip.allBestMoveCandidates(halfBoard, Player.A),
            List.of(new Move(1, 2, 2, 0)));
    assertEquals(combinedLeastCorner.allBestMoveCandidates(fullBoard, Player.B),
            List.of(new Move(0, 3, 2, 12)));
  }

  @Test
  public void handIdx() {
    assertEquals(move1.handIdx(), 0);
    assertEquals(move3.handIdx(), -90234);
  }

  @Test
  public void xCord() {
    assertEquals(move1.xCord(), 3);
    assertEquals(move3.xCord(), 500000);
  }

  @Test
  public void yCord() {
    assertEquals(move1.yCord(), 5);
    assertEquals(move3.yCord(), -1);
  }

  @Test
  public void score() {
    assertEquals(move1.score(), 72);
    assertEquals(move3.score(), -8);
  }

  @Test
  public void testEquals() {
    assertEquals(move1, move2);
    assertNotEquals(move1, move3);
    assertNotEquals(move1, new Object());
  }

  @Test
  public void testHashCode() {
    assertEquals(move1.hashCode(), 80);
    assertEquals(move3.hashCode(), 409757);
  }
}