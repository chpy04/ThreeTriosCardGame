package view;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Board;
import model.Card;
import controller.ConfigParser;
import model.Empty;
import model.Hole;
import model.Player;
import model.Slot;
import model.ThreeTriosBoard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Testing class for the text view.
 */
public class TTTextViewTest {

  private Appendable out;
  private Board model;
  private TTView view;
  private ConfigParser parser = new ConfigParser();

  private Slot empty = new Empty();
  private Slot hole = new Hole();


  @Before
  public void setup() {
    String cardsPath = "docs" + File.separator + "testCards.config";
    Scanner cardReader;
    try {
      cardReader = new Scanner(new File(cardsPath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file name does not exist");
    }

    List<Card> cards = parser.parseCards(cardReader);

    Card a224 = cards.get(0);
    Card tWO899 = cards.get(1);
    Card sEVEN253 = cards.get(2);
    Card fIVE27A = cards.get(3);
    Card tWO7A6 = cards.get(4);
    Card fOUR599 = cards.get(5);
    Card fOUR623 = cards.get(6);
    Card fOUR426 = cards.get(7);
    Card oNE234 = cards.get(8);
    Card tHREE99A = cards.get(9);


    a224.switchPlayer(Player.A);
    sEVEN253.switchPlayer(Player.B);

    cards.remove(a224);
    cards.remove(sEVEN253);

    Slot[][] grid = new Slot[][]{{empty, empty, hole},{a224, empty, sEVEN253},{hole, empty, empty}};

    out = new StringBuilder();
    model = new ThreeTriosBoard(grid, cards, new Random(2), false);
    model.startGame();
    view = new TTTextView(model, out);
  }

  @Test
  public void testViewConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new TTTextView(null, out));
    assertThrows(IllegalArgumentException.class, () -> new TTTextView(model, null));
  }

  @Test
  public void testToString() {
    assertEquals(view.toString(), "Player: RED\n" +
            "__ \n" +
            "R_B\n" +
            " __\n" +
            "Hand:\n" +
            "4623 4 6 2 3\n" +
            "4426 4 4 3 6\n" +
            "1234 1 2 3 4\n" +
            "399A 3 9 9 A\n");
    model.playToBoard(1, 1, 1);
    assertEquals(view.toString(), "Player: BLUE\n" +
            "__ \n" +
            "RRB\n" +
            " __\n" +
            "Hand:\n" +
            "2899 2 8 9 9\n" +
            "527A 5 2 7 A\n" +
            "27A6 2 7 A 6\n" +
            "4599 4 5 9 9\n");
  }

}