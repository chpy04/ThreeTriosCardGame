package model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

import controller.ConfigParser;
import view.TTTextView;
import view.TTView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * represents tests for the Board.
 */
public class ThreeTriosBoardTest {

  private Board model;

  private TTView view;
  private Slot[][] grid;
  private List<Card> cards;
  private ConfigParser parser = new ConfigParser();
  private Card a224;
  private Card two899;
  private Card seven253;
  private Card five27A;
  private Card two7A6;
  private Card four599;
  private Card four623;
  private Card four426;
  private Card one234;
  private Card three99A;
  private Slot empty = new Empty();
  private Slot hole = new Hole();
  private Slot card;
  private Slot card2;

  /**
   * Generates an array of slots from the file name passed in.
   * @param name the name of the board config file.
   * @return the array of slots
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

  @Before
  public void setUp() {

    String cardsPath = "docs" + File.separator + "testCards.config";
    Scanner cardReader;
    try {
      cardReader = new Scanner(new File(cardsPath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file name does not exist");
    }

    cards = parser.parseCards(cardReader);

    a224 = cards.get(0);
    two899 = cards.get(1);
    seven253 = cards.get(2);
    five27A = cards.get(3);
    two7A6 = cards.get(4);
    four599 = cards.get(5);
    four623 = cards.get(6);
    four426 = cards.get(7);
    one234 = cards.get(8);
    three99A = cards.get(9);


    a224.switchPlayer(Player.A);
    seven253.switchPlayer(Player.B);

    cards.remove(a224);
    cards.remove(seven253);


    grid = new Slot[][]{{empty, empty, hole},{a224, empty, seven253},{hole, empty, empty}};

    model = new ThreeTriosBoard(grid, cards, new Random(1), true);
    model.startGame();

    view = new TTTextView(model);

    card = new Card.CardBuilder().addName("A").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.ONE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.A)
            .build();
    card2 = new Card.CardBuilder().addName("B").addPlayer(Player.B)
            .addValue(Direction.UP, AttackValue.TWO)
            .addValue(Direction.DOWN, AttackValue.SIX)
            .addValue(Direction.LEFT, AttackValue.A)
            .addValue(Direction.RIGHT, AttackValue.FOUR)
            .build();


  }

  @Test
  public void AttackValueTransformer() {
    class Reverse implements Function<AttackValue, AttackValue> {
      @Override
      public AttackValue apply(AttackValue attackValue) {
        return AttackValue.valueFromInt(11 - attackValue.value);
      }
    }

    class FallenAce implements Function<AttackValue, AttackValue> {
      @Override
      public AttackValue apply(AttackValue attackValue) {
        if (attackValue.value == 10) {
          return AttackValue.valueFromInt(1);
        } else {
          return AttackValue.valueFromInt(attackValue.value + 1);
        }
      }
    }

    a224.addCardTransformer(new FallenAce());
    four599.addCardTransformer(new FallenAce());
    four623.addCardTransformer(new FallenAce());
    four426.addCardTransformer(new FallenAce());

    assertFalse(a224.battle(four426, Direction.RIGHT));
    assertTrue(four599.battle(four426, Direction.LEFT));
    assertFalse(four623.battle(four426, Direction.UP));
    assertTrue(four426.battle(a224, Direction.DOWN));
    assertTrue(four426.battle(four426, Direction.LEFT));

    a224.addCardTransformer(new Reverse());
    four599.addCardTransformer(new Reverse());
    four623.addCardTransformer(new Reverse());
    four426.addCardTransformer(new Reverse());

    assertTrue(a224.battle(four426, Direction.RIGHT));
    assertFalse(four599.battle(four426, Direction.LEFT));
    assertFalse(four623.battle(four426, Direction.UP));
    assertFalse(four426.battle(a224, Direction.DOWN));
    assertFalse(four426.battle(four426, Direction.LEFT));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionNullGrid() {
    model = new ThreeTriosBoard(null, List.of(seven253), new Random(1), true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionNullCards() {
    Slot[][] slots = new Slot[][]{{}};
    model = new ThreeTriosBoard(slots, null, new Random(1), true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionNullRandom() {
    Slot[][] slots = new Slot[][]{{}};
    model = new ThreeTriosBoard(slots, List.of(), null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionNotEnoughCards() {
    model = new ThreeTriosBoard(grid, List.of(), new Random(1), true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionGridHasNull() {
    grid = new Slot[][]{{null, empty, hole}, {a224, empty, seven253}, {hole, empty, empty}};
    model = new ThreeTriosBoard(grid, cards, new Random(1), true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionCardsHasNull() {
    List<Card> cards = new ArrayList<>();
    cards.add(null);
    model = new ThreeTriosBoard(grid, cards, new Random(1), true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImproperThreeTriosConstructionGridNotSquare() {
    grid = new Slot[][]{{empty, empty, hole}, {a224, empty}, {hole, empty, empty}};
    model = new ThreeTriosBoard(grid, cards, new Random(1), true);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardXOutOfRange() {
    model.playToBoard(0, 8, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardYOutOfRange() {
    model.playToBoard(0, 1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardHandIdxOutOfRange() {
    model.playToBoard(-3, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardImproperPlayToCard() {
    model.playToBoard(0, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardImproperPlayToHole() {
    model.playToBoard(0, 0, 2);
  }

  @Test
  public void playToBoardBasicFunctionality() {

    model.playToBoard(0, 1, 0);
    assertEquals(model.getGrid()[0][1], four599);
    assertEquals(model.getHand(Player.A).size(), 3);

    model.playToBoard(1, 0, 0);
    assertEquals(model.getGrid()[0][0], one234);
    assertEquals(model.getHand(Player.B).size(), 3);



    model.playToBoard(0, 1, 1);
    assertEquals(model.getGrid()[1][1], five27A);
    assertEquals(model.getHand(Player.A).size(), 2);


    model.playToBoard(0, 1, 2);
    assertEquals(model.getGrid()[2][1], two7A6);
    assertEquals(model.getHand(Player.B).size(), 2);


    model.playToBoard(0, 2, 2);
    assertEquals(model.getGrid()[2][2], four623);
    assertEquals(model.getHand(Player.A).size(), 1);

    assertTrue(model.isGameOver());
    assertEquals(model.gameWinner(), Player.A);
  }

  @Test
  public void testPlayToBoardCantFlipOwnCardsOrReBattleUsingCardsAlreadyOwned() {
    System.out.println(view.toString());
    model.playToBoard(3, 1, 1);
    assertEquals(model.getGrid()[1][2].getSlotOwner(), Player.B);
    model.playToBoard(2, 2, 2);
    assertEquals(model.getGrid()[1][2].getSlotOwner(), Player.B);
    assertEquals(model.getGrid()[1][1].getSlotOwner(), Player.A);
    assertEquals(model.getGrid()[1][0].getSlotOwner(), Player.A);
  }

  @Test
  public void testPlayToBoardFlipsCard() {
    assertEquals(model.getGrid()[1][2].getSlotOwner(), Player.B);
    model.playToBoard(0, 1, 1);
    assertEquals(model.getGrid()[1][2].getSlotOwner(), Player.A);
  }

  @Test
  public void testPlayToBoardFlipsMultipleCards() {
    model.playToBoard(0, 0, 0);
    model.playToBoard(2, 2, 2);
    model.playToBoard(0, 1, 2);
    model.playToBoard(2, 1, 1);

    assertEquals(model.getGrid()[2][1].getSlotOwner(), Player.B);
    assertEquals(model.getGrid()[1][0].getSlotOwner(), Player.B);
    assertEquals(model.getGrid()[0][0].getSlotOwner(), Player.B);
  }


  @Test
  public void testIsGameOverJustStarted() {
    assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverGameEnds() {
    model.playToBoard(0, 0, 0);
    assertFalse(model.isGameOver());
    model.playToBoard(0, 1, 0);
    model.playToBoard(0, 1, 1);
    model.playToBoard(0, 1, 2);
    model.playToBoard(0, 2, 2);
    assertTrue(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testGameWinnerGameNotOver() {
    model.gameWinner();
  }

  @Test
  public void gameWinnerGameOverAWins() {
    model.playToBoard(0, 0, 0);
    assertFalse(model.isGameOver());
    model.playToBoard(0, 1, 0);
    model.playToBoard(0, 1, 1);
    model.playToBoard(0, 1, 2);
    model.playToBoard(0, 2, 2);
    System.out.println(view.toString());
    System.out.println(model.isGameOver());
    assertEquals(model.gameWinner(), Player.A);
  }

  @Test
  public void gameWinnerGameOverBWins() {
    System.out.println(view.toString());
    model.playToBoard(0, 0, 0);
    System.out.println(view.toString());
    assertFalse(model.isGameOver());
    model.playToBoard(0, 1, 1);
    System.out.println(view.toString());
    model.playToBoard(0, 1, 2);
    System.out.println(view.toString());
    model.playToBoard(0, 1, 0);
    System.out.println(view.toString());
    model.playToBoard(0, 2, 2);
    System.out.println(view.toString());
    assertEquals(model.gameWinner(), Player.B);
  }

  @Test
  public void testGetGrid() {
    Slot[][] result = model.getGrid();

    assertEquals(result.length, grid.length);
    assertEquals(result[0].length, grid[0].length);
    assertEquals(result[0][0], empty);
    assertEquals(result[0][2], hole);
    assertEquals(result[1][1], empty);
    assertEquals(result[1][2], seven253);
    assertEquals(result[2][0], hole);
    assertEquals(result[1][0], a224);
  }

  @Test
  public void testGetGridNoMutation() {
    Slot[][] result = model.getGrid();
    result[1][1] = null;
    assertEquals(model.getGrid()[1][1], empty);

    assertEquals(model.getGrid()[1][2].boardPrint(), "B");
    result[1][2].switchPlayer(Player.A);
    assertEquals(model.getGrid()[1][2].boardPrint(), "B");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetHandPlayerNull() {
    model.getHand(null);
  }

  @Test
  public void testGetHandBothHands() {


    assertEquals(model.getHand(Player.A), List.of(four599, five27A, four623, four426));
    model.playToBoard(0,0,0);
    System.out.println(view.toString());
    assertEquals(model.getHand(Player.A), List.of(five27A, four623, four426));

    assertEquals(model.getHand(Player.B), List.of(two7A6, one234, three99A, two899));
    model.playToBoard(2, 1, 0);
    assertEquals(model.getHand(Player.B), List.of(two7A6, one234, two899));
  }

  @Test
  public void testGetHandNoMutation() {
    assertEquals(model.getHand(Player.A).size(), 4);
    List<Card> playerAHand = model.getHand(Player.A);
    List<Card> playerAHandCopy = new ArrayList<>(playerAHand);
    Card card = playerAHand.remove(0);
    assertEquals(card.boardPrint(), "R");
    card.switchPlayer(Player.B);

    assertEquals(card.boardPrint(), "B");
    assertEquals(model.getHand(Player.A).size(), 4);
    assertEquals(model.getHand(Player.A).get(0).boardPrint(), "R");
  }

  @Test
  public void testCurPlayer() {
    assertEquals(model.curPlayer(), Player.A);
    model.playToBoard(0, 0, 0);
    assertEquals(model.curPlayer(), Player.B);
    model.playToBoard(0, 1, 0);
    assertEquals(model.curPlayer(), Player.A);
    model.playToBoard(0, 1, 1);
    assertEquals(model.curPlayer(), Player.B);
  }

  //Game Width

  @Test
  public void testGameWidth() {
    Slot[][] separatedBoard = getBoard("separatedboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(separatedBoard, cards17,new Random(5), true );
    assertEquals(model.gameWidth(), 4);

    Slot[][] accessibleBoard = getBoard("accessibleboard.config");
    model = new ThreeTriosBoard(accessibleBoard, cards17, new Random(), true);
    assertEquals(model.gameWidth(), 5);

  }

  @Test
  public void testGameHeight() {
    Slot[][] separatedBoard = getBoard("separatedboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(separatedBoard, cards17,new Random(5), true );
    assertEquals(model.gameHeight(), 4);

    Slot[][] accessibleBoard = getBoard("accessibleboard.config");
    model = new ThreeTriosBoard(accessibleBoard, cards17, new Random(), true);
    assertEquals(model.gameHeight(), 5);


    Slot[][] nBoard = getBoard("nboard.config");
    model = new ThreeTriosBoard(nBoard, cards17, new Random(), true);
    assertEquals(model.gameHeight(), 5);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCoordInvalidX() {
    model.getCoord(-1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCoordInvalidY() {
    model.getCoord(0, 10);
  }

  @Test
  public void testGetCoordEmpty() {
    assertEquals(model.getCoord(0, 0), empty);
  }

  @Test
  public void testGetCoordHole() {
    assertEquals(model.getCoord(2, 0), hole);
  }

  @Test
  public void testGetCoordCard() {
    assertEquals(model.getCoord(0, 1).getSlotOwner(), Player.A);
  }

  @Test
  public void testGetCoordChangesWithPlace() {
    assertEquals(model.getCoord(0, 0), empty);
    model.playToBoard(0, 0, 0);
    assertEquals(model.getCoord(0, 0).getSlotOwner(), Player.A);
  }

  @Test
  public void testGetCoordChangesWithFlip() {
    assertEquals(model.getCoord(2, 1).getSlotOwner(), Player.B);
    model.playToBoard(0, 1, 1);
    assertEquals(model.getCoord(2, 1).getSlotOwner(), Player.A);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellOwnerInvalidX() {
    model.getCellOwner(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellOwnerInvalidY() {
    model.getCellOwner(0, -1);
  }

  @Test
  public void testGetCellOwnerEmpty() {
    assertEquals(model.getCellOwner(0, 0), Player.NONE);
  }

  @Test
  public void testGetCellOwnerHole() {
    assertEquals(model.getCellOwner(0, 0), Player.NONE);
  }

  @Test
  public void testGetCellOwnerCard() {
    assertEquals(model.getCellOwner(0, 1), Player.A);
    assertEquals(model.getCellOwner(2, 1), Player.B);
  }

  @Test
  public void testGetCellOwnerPlaceCard() {
    assertEquals(model.getCellOwner(1, 1), Player.NONE);
    model.playToBoard(0, 1, 1);
    assertEquals(model.getCellOwner(1, 1), Player.A);
  }

  @Test
  public void testGetCellOwnerPlacedCardFlips() {
    assertEquals(model.getCellOwner(0, 1), Player.A);
    assertEquals(model.getCellOwner(2, 1), Player.B);
    assertEquals(model.getCellOwner(1, 1), Player.NONE);
    model.playToBoard(0, 1, 1);
    assertEquals(model.getCellOwner(1, 1), Player.A);
    assertEquals(model.getCellOwner(0, 1), Player.A);
    assertEquals(model.getCellOwner(2, 1), Player.A);
  }

  @Test
  public void testisMoveLegalInvalidX() {
    assertFalse(model.isMoveLegal(-1, 0));
  }

  @Test
  public void testIsMoveLegalInvalidY() {
    assertFalse(model.isMoveLegal(0, -1));
  }

  @Test
  public void testIsMoveLegalToEmpty() {
    assertTrue(model.isMoveLegal(0, 0));
  }

  @Test
  public void testIsMoveLegalHole() {
    assertFalse(model.isMoveLegal(2, 0));
  }

  @Test
  public void testIsMoveLegalCard() {
    assertFalse(model.isMoveLegal(0, 1));
  }

  @Test
  public void testIsMoveLegalAfterPlace() {
    assertTrue(model.isMoveLegal(0, 0));
    model.playToBoard(0, 0, 0);
    assertFalse(model.isMoveLegal(0, 0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testScoreNull() {
    model.score(null);
  }

  @Test
  public void testScoreEmptyBoard() {
    Slot[][] grid = getBoard("emptyboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(grid, cards17, new Random(4), true);
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);
  }

  @Test
  public void testScoreNonePlayer() {
    Slot[][] grid = getBoard("emptyboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(grid, cards17, new Random(4), true);
    assertEquals(model.score(Player.NONE), 0);
  }

  @Test
  public void testScoreAfterPlacingNoFlip() {
    Slot[][] grid = getBoard("emptyboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(grid, cards17, new Random(4), true);

    model.startGame();
    model.playToBoard(0, 2, 2);
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);
    model.playToBoard(0, 0, 0);
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);
    model.playToBoard(0, 3, 0);
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);

  }

  @Test
  public void testScoreAfterPlacingWithFlip() {
    Slot[][] grid = getBoard("emptyboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(grid, cards17, new Random(5), true);

    model.startGame();
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);
    model.playToBoard(0, 0, 0);
    assertEquals(model.score(Player.A), 9);
    assertEquals(model.score(Player.B), 8);
    model.playToBoard(0, 0, 1);
    assertEquals(model.score(Player.A), 8);
    assertEquals(model.score(Player.B), 9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPossibleCardsFlippedCardNull() {
    model.possibleCardsFlipped(null, 0, 0);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testPossibleCardsFlippedInvalidX() {
    model.possibleCardsFlipped(one234, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPossibleCardsFlippedInvalidY() {
    model.possibleCardsFlipped(one234, 0, -12);
  }

  @Test
  public void testPossibleCardsFlippedBoardEmpty() {
    Slot[][] grid = getBoard("emptyboard.config");
    List<Card> cards17 = getCards("17cards.config");
    model = new ThreeTriosBoard(grid, cards17, new Random(2), true);
    model.startGame();
    assertEquals(model.possibleCardsFlipped(one234, 0, 0), 0);

  }

  @Test
  public void testPossibleCardsFlippedCardNotHighEnoughToFlipAny() {
    assertEquals(model.possibleCardsFlipped(one234, 0, 0), 0);
    assertEquals(model.getCellOwner(0, 1), Player.A);
    model.playToBoard(0, 0, 0);
    assertEquals(model.getCellOwner(0, 1), Player.A);
  }

  @Test
  public void testPossibleCardsFlippedOneCard() {
    System.out.println(view.toString());
    assertEquals(model.possibleCardsFlipped(four599, 1, 1), 1);
    assertEquals(model.getCellOwner(2, 1), Player.B);
    model.playToBoard(0, 1, 1);
    assertEquals(model.getCellOwner(2, 1), Player.A);
  }

  @Test
  public void testPossibleCardsFlippedMultipleCardsWithCombo() {
    assertEquals(model.possibleCardsFlipped(model.getHand(Player.A).get(0), 0, 0), 0);
    model.playToBoard(0, 0, 0);
    assertEquals(model.possibleCardsFlipped(model.getHand(Player.B).get(2), 2, 2), 0);
    model.playToBoard(2, 2, 2);
    assertEquals(model.possibleCardsFlipped(model.getHand(Player.A).get(0), 1, 2), 0);
    model.playToBoard(0, 1, 2);
    assertNotEquals(model.getCellOwner(2, 2), Player.A);
    assertEquals(model.possibleCardsFlipped(model.getHand(Player.B).get(2), 1, 1), 3);


  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameAlreadyStarted() {
    model.startGame();
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameAlreadyOver() {
    model.playToBoard(0, 1, 0);
    model.playToBoard(1, 0, 0);
    model.playToBoard(0, 1, 1);
    model.playToBoard(0, 1, 2);
    model.playToBoard(0, 2, 2);

    assertTrue(model.isGameOver());
    model.startGame();
  }

  //  @Test(expected = IllegalStateException.class)
  //  public void testStartGameOnlyOneListener() {
  //    Slot[][] separatedBoard = getBoard("separatedboard.config");
  //    List<Card> cards17 = getCards("17cards.config");
  //    model = new ThreeTriosBoard(separatedBoard, cards17,new Random(5), true );
  //    model.addListener(new ModelFeaturesMock());
  //    model.startGame();
  //  }
  //
  //  @Test(expected = IllegalStateException.class)
  //  public void testAddListenerTooMany() {
  //    Slot[][] separatedBoard = getBoard("separatedboard.config");
  //    List<Card> cards17 = getCards("17cards.config");
  //    model = new ThreeTriosBoard(separatedBoard, cards17,new Random(5), true );
  //    model.addListener(new ModelFeaturesMock());
  //    model.addListener(new ModelFeaturesMock());
  //    model.addListener(new ModelFeaturesMock());
  //  }

  @Test
  public void testBoardParser() {
    Scanner workingString = new Scanner("2 3\n" +
            "XCC\n" +
            "CXC");
    Slot[][] result = parser.parseBoard(workingString);
    assertEquals(result[0][0], new Hole());
    assertEquals(result[0][1], new Empty());
    assertEquals(result[0][2], new Empty());
    assertEquals(result[1][0], new Empty());
    assertEquals(result[1][1], new Hole());
    assertEquals(result[1][2], new Empty());

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("A 3\n" + "XCC\n" + "CXC")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("2 A\n" + "XCC\n" + "CXC")));
    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("2 A\n" + "XCC\n" + "CXC")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("2 3\n" + "XCC\n" + "CXCX")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("2 3\n" + "XCC\n" + "CXC\n" + "CCC")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseBoard(new Scanner("2 3\n" + "XCC\n" + "CX")));

    Assert.assertThrows(IllegalArgumentException.class,() -> parser
            .parseBoard(new Scanner("2 3\n" + "XCC")));

  }

  @Test
  public void testCardParser() {
    Scanner cardString = new Scanner("A244 A 2 4 4\n" +
            "2899 2 8 9 9\n" +
            "7253 7 2 5 3\n" +
            "527A 5 2 7 A");

    Card card1 = new Card.CardBuilder().addName("A244").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.A)
            .addValue(Direction.DOWN, AttackValue.TWO)
            .addValue(Direction.LEFT, AttackValue.FOUR)
            .addValue(Direction.RIGHT, AttackValue.FOUR)
            .build();

    Card card2 = new Card.CardBuilder().addName("2899").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.TWO)
            .addValue(Direction.DOWN, AttackValue.EIGHT)
            .addValue(Direction.LEFT, AttackValue.NINE)
            .addValue(Direction.RIGHT, AttackValue.NINE)
            .build();

    Card card3 = new Card.CardBuilder().addName("7253").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.TWO)
            .addValue(Direction.LEFT, AttackValue.THREE)
            .addValue(Direction.RIGHT, AttackValue.FIVE)
            .build();

    Card card4 = new Card.CardBuilder().addName("527A").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.FIVE)
            .addValue(Direction.DOWN, AttackValue.TWO)
            .addValue(Direction.LEFT, AttackValue.A)
            .addValue(Direction.RIGHT, AttackValue.SEVEN)
            .build();

    List<Card> result = parser.parseCards(cardString);
    assertEquals(result.size(), 4);
    assertEquals(result.get(0), card1);
    assertEquals(result.get(1), card2);
    assertEquals(result.get(2), card3);
    assertEquals(result.get(3), card4);

    assertEquals(parser.parseCards(new Scanner("")).size(), 0);

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseCards(new Scanner("CARD_1 1 2 3 4\n" +
                    "CARD_2 5 G 7 8\n" + "CARD_3 A 9 3 2\n" + "CARD_4 5 9 2 A")));

    Assert.assertThrows(IllegalArgumentException.class, () -> parser
            .parseCards(new Scanner("CARD_1 1 2 3 4\n" + "CARD_2 5_8 7 8\n" +
                    "CARD_3 A 9 3 2\n" +
                    "CARD_4 5 9 2 A")));

    Assert.assertThrows(IllegalArgumentException.class,() -> parser
            .parseCards(new Scanner("CARD_1 1 2 3 4\n" +
                    "CARD_2 5_8 7 8\n" +
                    "CARD_3 A 9 3 2\n" +
                    "5 9 2 A")));

  }

  @Test
  public void testFirstPlayer() {
    assertEquals(Player.firstPlayer(), Player.A);
  }

  @Test
  public void testSplitCards() {
    Card card1 = new Card.CardBuilder().addName("A").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.ONE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.A)
            .build();
    Card card2 = new Card.CardBuilder().addName("B").addPlayer(Player.B)
            .addValue(Direction.UP, AttackValue.TWO)
            .addValue(Direction.DOWN, AttackValue.SIX)
            .addValue(Direction.LEFT, AttackValue.A)
            .addValue(Direction.RIGHT, AttackValue.FOUR)
            .build();
    Card card3 = new Card.CardBuilder().addName("C").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.ONE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.A)
            .build();
    List<Card> inputCards = List.of(card1, card2, card3);
    Map<Player, List<Card>> result = Player.splitCardsBetweenPlayers(inputCards);
    assertEquals(result.get(Player.A).size(), 2);
    assertEquals(result.get(Player.B).size(), 1);
    assertEquals(result.get(Player.B).get(0), inputCards.get(0));
    assertEquals(result.get(Player.A).get(0), inputCards.get(1));
    assertEquals(result.get(Player.A).get(1), inputCards.get(2));

    assertThrows(IllegalArgumentException.class,() -> Player.splitCardsBetweenPlayers(null));

    List<Card> nullList = new ArrayList<>();
    nullList.add(card1);
    nullList.add(null);
    nullList.add(card3);

    assertThrows(IllegalArgumentException.class, () -> Player.splitCardsBetweenPlayers(nullList));
  }

  @Test
  public void testNextPlayer() {
    assertThrows(IllegalArgumentException.class, () -> Player.nextPlayer(null));
    assertEquals(Player.nextPlayer(Player.A), Player.B);
    assertEquals(Player.nextPlayer(Player.B), Player.A);
  }

  @Test
  public void testPlayerToString() {
    assertEquals(Player.A.toString(), "RED");
    assertEquals(Player.B.toString(), "BLUE");
  }

  @Test
  public void testGetOppositeDirection() {
    assertEquals(Direction.UP.getOppositeDirection(), Direction.DOWN);
    assertEquals(Direction.DOWN.getOppositeDirection(), Direction.UP);
    assertEquals(Direction.LEFT.getOppositeDirection(), Direction.RIGHT);
    assertEquals(Direction.RIGHT.getOppositeDirection(), Direction.LEFT);
  }

  @Test
  public void testAttackValueToString() {
    assertEquals(AttackValue.A.toString(), "A");
    assertEquals(AttackValue.ONE.toString(), "1");
    assertEquals(AttackValue.THREE.toString(), "3");
    assertEquals(AttackValue.FOUR.toString(), "4");
    assertEquals(AttackValue.EIGHT.toString(), "8");
    assertEquals(AttackValue.NINE.toString(), "9");
  }

  @Test
  public void testCompareToValue() {
    assertThrows(IllegalArgumentException.class,() -> AttackValue.THREE.compareToValue(null));
    assertEquals(AttackValue.TWO.compareToValue(AttackValue.EIGHT), -6);
    assertEquals(AttackValue.A.compareToValue(AttackValue.THREE), 7);
    assertEquals(AttackValue.FIVE.compareToValue(AttackValue.A), -5);
    assertEquals(AttackValue.NINE.compareToValue(AttackValue.EIGHT), 1);
    assertEquals(AttackValue.FOUR.compareToValue(AttackValue.SEVEN), -3);
    assertEquals(AttackValue.ONE.compareToValue(AttackValue.ONE), 0);
    assertEquals(AttackValue.A.compareToValue(AttackValue.A), 0);
  }

  @Test
  public void testCanPlayCard() {
    assertTrue(empty.canPlayCard());
    assertFalse(hole.canPlayCard());
    assertFalse(card.canPlayCard());
  }

  @Test
  public void testCardConstructorErrors() {

    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName(null).addPlayer(Player.A)
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.DOWN, AttackValue.ONE)
              .addValue(Direction.LEFT, AttackValue.FIVE)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());
    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addPlayer(Player.A)
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.DOWN, AttackValue.ONE)
              .addValue(Direction.LEFT, AttackValue.FIVE)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());

    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName("A").addPlayer(null)
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.DOWN, AttackValue.ONE)
              .addValue(Direction.LEFT, AttackValue.FIVE)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());
    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName("A")
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.DOWN, AttackValue.ONE)
              .addValue(Direction.LEFT, AttackValue.FIVE)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());

    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName("A").addPlayer(Player.A)
              .build());

    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName("A").addPlayer(Player.A)
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.DOWN, AttackValue.ONE)
              .addValue(Direction.LEFT, null)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());
    assertThrows(IllegalArgumentException.class,
        () -> new Card.CardBuilder().addName("A").addPlayer(Player.A)
              .addValue(Direction.UP, AttackValue.SEVEN)
              .addValue(Direction.LEFT, AttackValue.FIVE)
              .addValue(Direction.RIGHT, AttackValue.A)
              .build());
  }


  /**
   * testing method.
   * @param slot slot to use
   */
  public void testGenericSlotCompareAttackValueBehavior(Slot slot) {
    assertThrows(IllegalArgumentException.class,
        () -> slot.compareAttackValueTo(null, AttackValue.A));
    assertThrows(IllegalArgumentException.class,
        () -> slot.compareAttackValueTo(Direction.RIGHT, null));
  }

  @Test
  public void testCompareAttackValueToWithCard() {
    testGenericSlotCompareAttackValueBehavior(card);
    assertEquals(card.compareAttackValueTo(Direction.LEFT, AttackValue.FIVE), 5);
    assertEquals(card.compareAttackValueTo(Direction.LEFT, AttackValue.A), 0);

    assertEquals(card.compareAttackValueTo(Direction.RIGHT, AttackValue.SIX), -1);
    assertEquals(card.compareAttackValueTo(Direction.RIGHT, AttackValue.FIVE), 0);
    assertEquals(card.compareAttackValueTo(Direction.RIGHT, AttackValue.ONE), 4);

    assertEquals(card.compareAttackValueTo(Direction.DOWN, AttackValue.SIX), 1);
    assertEquals(card.compareAttackValueTo(Direction.DOWN, AttackValue.A), -3);
    assertEquals(card.compareAttackValueTo(Direction.DOWN, AttackValue.ONE), 6);

    assertEquals(card.compareAttackValueTo(Direction.UP, AttackValue.SIX), -5);
    assertEquals(card.compareAttackValueTo(Direction.UP, AttackValue.A), -9);
    assertEquals(card.compareAttackValueTo(Direction.UP, AttackValue.ONE), 0);
  }

  @Test
  public void testCompareAttackValueToWithHole() {
    testGenericSlotCompareAttackValueBehavior(new Hole());
    assertEquals(hole.compareAttackValueTo(Direction.LEFT, AttackValue.A), 1);
    assertEquals(hole.compareAttackValueTo(Direction.LEFT, AttackValue.EIGHT), 1);
    assertEquals(hole.compareAttackValueTo(Direction.LEFT, AttackValue.FIVE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.LEFT, AttackValue.ONE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.RIGHT, AttackValue.A), 1);
    assertEquals(hole.compareAttackValueTo(Direction.RIGHT, AttackValue.EIGHT), 1);
    assertEquals(hole.compareAttackValueTo(Direction.RIGHT, AttackValue.FIVE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.RIGHT, AttackValue.ONE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.UP, AttackValue.A), 1);
    assertEquals(hole.compareAttackValueTo(Direction.UP, AttackValue.EIGHT), 1);
    assertEquals(hole.compareAttackValueTo(Direction.UP, AttackValue.FIVE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.UP, AttackValue.ONE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.DOWN, AttackValue.A), 1);
    assertEquals(hole.compareAttackValueTo(Direction.DOWN, AttackValue.EIGHT), 1);
    assertEquals(hole.compareAttackValueTo(Direction.DOWN, AttackValue.FIVE), 1);
    assertEquals(hole.compareAttackValueTo(Direction.DOWN, AttackValue.ONE), 1);
  }

  @Test
  public void testCompareAttackValueToWithEmptySpace() {
    testGenericSlotCompareAttackValueBehavior(new Empty());
    assertEquals(empty.compareAttackValueTo(Direction.LEFT, AttackValue.A), 1);
    assertEquals(empty.compareAttackValueTo(Direction.LEFT, AttackValue.EIGHT), 1);
    assertEquals(empty.compareAttackValueTo(Direction.LEFT, AttackValue.FIVE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.LEFT, AttackValue.ONE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.RIGHT, AttackValue.A), 1);
    assertEquals(empty.compareAttackValueTo(Direction.RIGHT, AttackValue.EIGHT), 1);
    assertEquals(empty.compareAttackValueTo(Direction.RIGHT, AttackValue.FIVE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.RIGHT, AttackValue.ONE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.UP, AttackValue.A), 1);
    assertEquals(empty.compareAttackValueTo(Direction.UP, AttackValue.EIGHT), 1);
    assertEquals(empty.compareAttackValueTo(Direction.UP, AttackValue.FIVE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.UP, AttackValue.ONE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.DOWN, AttackValue.A), 1);
    assertEquals(empty.compareAttackValueTo(Direction.DOWN, AttackValue.EIGHT), 1);
    assertEquals(empty.compareAttackValueTo(Direction.DOWN, AttackValue.FIVE), 1);
    assertEquals(empty.compareAttackValueTo(Direction.DOWN, AttackValue.ONE), 1);
  }

  /**
   * Testing method.
   * @param slot the slot
   */
  public void testGenericSlotBattleBehavior(Slot slot) {
    assertThrows(IllegalArgumentException.class,
        () -> slot.battle(null, Direction.RIGHT));
    assertThrows(IllegalArgumentException.class,
        () -> slot.battle(new Empty(), null));
  }

  @Test
  public void testBattleWithCard() {
    testGenericSlotBattleBehavior(card);
    assertTrue(card.battle(card2, Direction.UP)); //card wins
    assertTrue(card.battle(card2, Direction.LEFT)); //card wins
    assertFalse(card.battle(card2, Direction.DOWN)); //respective value in card2 greater
    assertFalse(card.battle(card2, Direction.RIGHT)); //equal values, so card doesn't win
  }

  @Test
  public void testBattleWithNonCard() {
    testGenericSlotBattleBehavior(new Hole());
    testGenericSlotBattleBehavior(new Empty());
    assertFalse(new Hole().battle(card, Direction.RIGHT));
    assertFalse(new Hole().battle(new Hole(), Direction.LEFT));
    assertFalse(new Hole().battle(new Empty(), Direction.UP));
    assertFalse(new Empty().battle(card2, Direction.DOWN));
    assertFalse(new Empty().battle(new Hole(), Direction.RIGHT));
    assertFalse(new Empty().battle(new Empty(), Direction.LEFT));
  }

  @Test
  public void testAddToPlayerCountCard() {
    Map<Player, Integer> counts = new HashMap<>();
    card.addToPlayerCount(counts);
    int count = counts.getOrDefault(Player.A, 0);
    assertEquals(count, 1);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 0);

    card2.addToPlayerCount(counts);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 1);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 1);
  }

  @Test
  public void testAddToPlayerCountEmpty() {
    Map<Player, Integer> counts = new HashMap<>();
    empty.addToPlayerCount(counts);
    int count = counts.getOrDefault(Player.A, 0);
    assertEquals(count, 0);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 0);

    empty.switchPlayer(Player.A);
    count = counts.getOrDefault(Player.A, 0);
    assertEquals(count, 0);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 0);

  }

  @Test
  public void testAddToPlayerCountHole() {
    Map<Player, Integer> counts = new HashMap<>();
    hole.addToPlayerCount(counts);
    int count = counts.getOrDefault(Player.A, 0);
    assertEquals(count, 0);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 0);

    hole.switchPlayer(Player.A);
    count = counts.getOrDefault(Player.A, 0);
    assertEquals(count, 0);
    count = counts.getOrDefault(Player.B, 0);
    assertEquals(count, 0);
  }

  @Test
  public void testSwitchPlayerCard() {
    card.switchPlayer(Player.A);
    assertEquals(card.boardPrint(), "R");
    card.switchPlayer(Player.B);
    assertEquals(card.boardPrint(), "B");
    card.switchPlayer(Player.B);
    assertEquals(card.boardPrint(), "B");

    card2.switchPlayer(Player.A);
    assertEquals(card2.boardPrint(), "R");
    card2.switchPlayer(Player.B);
    assertEquals(card2.boardPrint(), "B");
  }

  @Test
  public void testSwitchPlayerEmpty() {
    assertEquals(empty.boardPrint(), "_");
    empty.switchPlayer(Player.A);
    assertEquals(empty.boardPrint(), "_");
    empty.switchPlayer(Player.B);
    assertEquals(empty.boardPrint(), "_");

  }

  @Test
  public void testSwitchPlayerHole() {
    assertEquals(hole.boardPrint(), " ");
    hole.switchPlayer(Player.A);
    assertEquals(hole.boardPrint(), " ");
    hole.switchPlayer(Player.B);
    assertEquals(hole.boardPrint(), " ");

  }

  @Test
  public void testToStringCard() {
    assertEquals(card.toString(), "A 7 1 A 5");
    assertEquals(card2.toString(), "B 2 6 4 A");
  }

  @Test
  public void testToStringHole() {
    assertEquals(hole.toString(), " ");
  }

  @Test
  public void testToStringEmpty() {
    assertEquals(empty.toString(), "_");
  }

  @Test
  public void testCopyEmpty() {
    assertEquals(empty.copySlot(), new Empty());
  }

  @Test
  public void testCopyHole() {
    assertEquals(hole.copySlot(), new Hole());
  }

  @Test
  public void testCopyCard() {
    Card copiedCard = new Card.CardBuilder().addName("A").addPlayer(Player.A)
            .addValue(Direction.UP, AttackValue.SEVEN)
            .addValue(Direction.DOWN, AttackValue.ONE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.A)
            .build();

    assertEquals(card, copiedCard);
    Slot copyResult = card.copySlot();
    assertEquals(copyResult, copiedCard);
    copyResult.switchPlayer(Player.B);
    assertEquals(card, copiedCard);
  }

  @Test
  public void testGetSlotOwnerEmpty() {
    Slot empty = new Empty();
    assertEquals(empty.getSlotOwner(), Player.NONE);
    empty.switchPlayer(Player.B);
    assertEquals(empty.getSlotOwner(), Player.NONE);
  }

  @Test
  public void testGetSlotOwnerHole() {
    Slot hole = new Hole();
    assertEquals(hole.getSlotOwner(), Player.NONE);
    hole.switchPlayer(Player.B);
    assertEquals(hole.getSlotOwner(), Player.NONE);
  }

  @Test
  public void testGetSlotOwnerCard() {
    assertEquals(card.getSlotOwner(), Player.A);
    card.switchPlayer(Player.B);
    assertEquals(card.getSlotOwner(), Player.B);
  }

  @Test
  public void testGetSlotColorCard() {
    assertEquals(card.getSlotColor(), Color.RED);
    card.switchPlayer(Player.B);
    assertEquals(card.getSlotColor(), Color.BLUE);
  }

  @Test
  public void testGetSlotColorEmpty() {
    assertEquals(empty.getSlotColor(), Color.YELLOW);
  }

  @Test
  public void testGetSlotColorHole() {
    assertEquals(hole.getSlotColor(), Color.GRAY);
  }

  @Test
  public void testGetDirectionalAttackValuesEmpty() {
    assertEquals(empty.getDirectionalValues(), new HashMap<>());
  }

  @Test
  public void testGetDirectionalAttackValuesHole() {
    assertEquals(hole.getDirectionalValues(), new HashMap<>());
  }

  @Test
  public void testGetDirectionalAttackValuesCard() {
    Map<Direction, AttackValue> ans = new HashMap<>();
    ans.put(Direction.UP, AttackValue.SEVEN);
    ans.put(Direction.DOWN, AttackValue.ONE);
    ans.put(Direction.LEFT, AttackValue.FIVE);
    ans.put(Direction.RIGHT, AttackValue.A);
    assertEquals(card.getDirectionalValues(), ans);
  }

  @Test
  public void testGetDirectionalValuesNoMutation() {
    Map<Direction, AttackValue> ret = card.getDirectionalValues();
    Map<Direction, AttackValue> noMutation = card.getDirectionalValues();
    ret.put(Direction.UP, AttackValue.A);
    assertEquals(card.getDirectionalValues(), noMutation);
  }


}