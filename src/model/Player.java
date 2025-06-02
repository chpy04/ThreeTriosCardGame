package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents players in the ThreeTriosBoard game.
 */
public enum Player {
  A {
    public String toString() {
      return "RED";
    }

    public Color playerColor() {
      return Color.RED;
    }
  },

  B {
    public String toString() {
      return "BLUE";
    }

    public Color playerColor() {
      return Color.BLUE;
    }
  },

  NONE {
    public String toString() {
      return "";
    }

    public Color playerColor() {
      return Color.GRAY;
    }
  };

  /**
   * Returns the next player after this one.
   * @param curPlayer the current player
   * @return the next player
   */
  public static Player nextPlayer(Player curPlayer) {
    if (curPlayer == null) {
      throw new IllegalArgumentException("player cannot be null");
    }

    if (curPlayer == Player.A) {
      return Player.B;
    } else {
      return Player.A;
    }

  }

  /**
   * Returns the first player of the game.
   * @return the first player in the game
   */
  public static Player firstPlayer() {
    return Player.A;
  }

  abstract public Color playerColor();

  /**
   * Splits the cards evenly between all of the players, with the first player getting the first
   * segment of cards, the second the second.
   * @param cards the cards (already shuffled) to split
   * @return a map from players to their hands as lists of cards
   */
  public static Map<Player, List<Card>> splitCardsBetweenPlayers(List<Card> cards) {
    if (cards == null) {
      throw new IllegalArgumentException("cards cannot be null");
    }

    for (Card card : cards) {
      if (card == null) {
        throw new IllegalArgumentException("all cards within cards cannot be null");
      }
    }

    Map<Player, List<Card>> result = new HashMap<>();
    List<Card> bCards = new ArrayList<>(cards.subList(0, cards.size() / 2));
    for (Card card : bCards) {
      card.switchPlayer(Player.B);
    }

    List<Card> aCards = new ArrayList<>(cards.subList(cards.size() / 2, cards.size()));
    for (Card card: aCards) {
      card.switchPlayer(Player.A);
    }
    result.put(Player.A, aCards);
    result.put(Player.B, bCards);
    return result;
  }
}
