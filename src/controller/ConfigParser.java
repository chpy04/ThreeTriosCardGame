package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.AttackValue;
import model.Board;
import model.Card;
import model.Direction;
import model.Empty;
import model.Hole;
import model.Player;
import model.Slot;
import model.ThreeTriosBoard;

/**
 * A class to parse scanners from files into arrays of slots or cards.
 */
public class ConfigParser {

  /**
   * Given the names of two files, parses them and produces a board with their contents.
   * @param boardFile the name of the file with the board contents
   * @param cardFile the name of the file with the card contents
   * @param random the random object used to start initiate the model with
   * @return a board initialized with the
   * @throws IllegalArgumentException if the files do not exist or are incorrectly formatted
   */
  public Board createModelFromFiles(String boardFile, String cardFile, Random random) {
    Scanner boardReader;
    Scanner cardReader;
    try {
      boardReader = new Scanner(new File(boardFile));
      cardReader = new Scanner(new File(cardFile));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file name does not exist");
    }

    return new ThreeTriosBoard(this.parseBoard(boardReader), this.parseCards(cardReader),
            random, true);
  }

  /**
   * Takes in a scanner and produces a 2D array of Slots based on the contents as described
   * by the assignment.
   * @param fileContents the scanner that will be used to build the array
   * @return an array of slots representing the string
   * @throws IllegalArgumentException if the file is in the incorrect form
   */
  public Slot[][] parseBoard(Scanner fileContents) {

    Slot[][] boardSlots;
    int numRows = 0;
    int numCols = 0;
    int curRow = 0;
    if (fileContents.hasNextLine()) {
      String firstLine = fileContents.nextLine();
      try {
        numRows = Integer.parseInt(String.valueOf(firstLine.charAt(0)));
        numCols = Integer.parseInt(String.valueOf(firstLine.charAt(2)));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("non-integer values included in first line");
      }
    } else {
      throw new IllegalArgumentException("empty input");
    }

    boardSlots = new Slot[numRows][numCols];

    try {
      while (fileContents.hasNextLine()) {
        String nextLine = fileContents.nextLine();
        for (int charIdx = 0; charIdx < nextLine.length(); charIdx += 1) {
          if (nextLine.charAt(charIdx) == 'C') {
            boardSlots[curRow][charIdx] = new Empty();
          } else if (nextLine.charAt(charIdx) == 'X') {
            boardSlots[curRow][charIdx] = new Hole();
          }
        }
        curRow += 1;
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("board is bigger than size values");
    }

    fileContents.close();

    for (Slot[] row : boardSlots) {
      for (Slot index : row) {
        if (index == null) {
          throw new IllegalArgumentException("board is not as big as size values");
        }
      }
    }

    return boardSlots;
  }

  /**
   * Takes in a scanner and produces a list of cards based on the contents as described
   * by the assignment.
   * @param fileContents the contents of the scanner that will be used to produce the list.
   * @return a list of cards with designated values, all owned by player A to begin with
   * @throws IllegalArgumentException if the scanner does not fit the proper format
   */
  public List<Card> parseCards(Scanner fileContents) {

    List<Card> result = new ArrayList<>();

    while (fileContents.hasNextLine()) {
      String nextLine = fileContents.nextLine();
      String[] split = nextLine.split(" ");
      try {
        Card card = new Card.CardBuilder()
                .addName(split[0])
                .addValue(Direction.UP, stringToAttackVal(split[1]))
                .addValue(Direction.DOWN, stringToAttackVal(split[2]))
                .addValue(Direction.RIGHT, stringToAttackVal(split[3]))
                .addValue(Direction.LEFT, stringToAttackVal(split[4]))
                .addPlayer(Player.A)
                .build();
        result.add(card);
      } catch (IndexOutOfBoundsException | NumberFormatException e) {
        throw new IllegalArgumentException("Invalid number of card arguments");
      }
    }

    return result;
  }

  /**
   * Attempts to produce an attack value from a given string.
   * @param str the string which represents the attack value (1-9 or A)
   * @return an attack value
   * @throws IllegalArgumentException if the string cannot be parsed into an int
   */
  private AttackValue stringToAttackVal(String str) {
    if (str.equals("A")) {
      return AttackValue.A;
    }
    int value = Integer.parseInt(str);
    switch (value) {
      case 1:
        return AttackValue.ONE;
      case 2:
        return AttackValue.TWO;
      case 3:
        return AttackValue.THREE;
      case 4:
        return AttackValue.FOUR;
      case 5:
        return AttackValue.FIVE;
      case 6:
        return AttackValue.SIX;
      case 7:
        return AttackValue.SEVEN;
      case 8:
        return AttackValue.EIGHT;
      case 9:
        return AttackValue.NINE;
      default:
        //do nothing, we will throw an exception.
    }
    throw new IllegalArgumentException("invalid attack value");
  }
}
