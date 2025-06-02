package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import controller.ModelFeatures;

/**
 * Represents a playable board in the ThreeTriosGame.
 * INVARIANT: there are always more cards in the hand than open slots in the board
 * INVARIANT: the grid and hands contain no null values
 * INVARIANT: the grid is always at least 1 by 1 and square
 */
public class ThreeTriosBoard implements Board {

  private final Slot[][] grid; //0-indexed, row-column major
  private final Map<Player, List<Card>> unPlayedCards; //0-indexed
  private Player curPlayer;
  private boolean gameStarted;
  private final Map<Player, ModelFeatures> playerListeners;

  /**
   * Constructs a ThreeTriosBoard object.
   *
   * @param grid   the grid to use
   * @param cards  the cards you can use
   * @param random random variable to use when shuffling
   */
  public ThreeTriosBoard(Slot[][] grid, List<Card> cards, Random random, boolean shuffle) {
    this.grid = grid;
    this.gameStarted = false;
    this.playerListeners = new HashMap<>();

    //constructor checks
    if (grid == null || cards == null || random == null) {
      throw new IllegalArgumentException("Arguments for board must be non-null");
    }

    for (Card card : cards) {
      if (card == null) {
        throw new IllegalArgumentException("cards cannot contain null elements");
      }
    }

    if (grid.length == 0 || grid[0].length == 0) {
      throw new IllegalArgumentException("grid must be at least 1 by 1 in size");
    }

    int rowLength = 0;
    int numOfOpenSlots = 0;

    for (Slot[] row : grid) {
      for (Slot slot : row) {
        if (slot == null) {
          throw new IllegalArgumentException("grid cannot have null slots");
        }
        rowLength += 1;
        if (slot.canPlayCard()) {
          numOfOpenSlots += 1;
        }
      }
      if (rowLength != grid[0].length) {
        throw new IllegalArgumentException("grid must be square");
      }
      rowLength = 0;
    }

    if (numOfOpenSlots >= cards.size()) {
      throw new IllegalArgumentException("there must be more cards than empty slots");
    }


    ArrayList<Card> shuffledCardsCopy = new ArrayList<>(Objects.requireNonNull(cards));
    if (shuffle) {
      Collections.shuffle(shuffledCardsCopy, random);
    }
    this.unPlayedCards = Player.splitCardsBetweenPlayers(shuffledCardsCopy);
    this.curPlayer = Player.firstPlayer();
  }

  /**
   * Constructs a ThreeTriosBoard with a random Random variable.
   *
   * @param grid  the grid to use
   * @param cards the cards to use
   */
  public ThreeTriosBoard(Slot[][] grid, List<Card> cards) {
    this(grid, cards, new Random(), true);
  }

  private void changePlayerForListener(Player player) {
    if (this.playerListeners.get(player) != null) {
      this.playerListeners.get(player).changeTurn(player);
    }
  }



  @Override
  public void startGame() {
    if (this.gameStarted) {
      throw new IllegalStateException("game has already started");
    }
    if (this.isGameOver()) {
      throw new IllegalStateException("game is over");
    }
    if (!(this.playerListeners.keySet().size() == 2 || this.playerListeners.keySet().size() == 0)) {
      throw new IllegalStateException("Game needs either both of neither player listeners");
    }
    this.gameStarted = true;
    this.changePlayerForListener(Player.A);
  }

  @Override
  public void addListener(ModelFeatures listener) {
    if (this.gameStarted) {
      throw new IllegalArgumentException("Game already started");
    }
    if (this.isGameOver()) {
      throw new IllegalArgumentException("Game already over");
    }
    if (!this.playerListeners.containsKey(Player.A)) {
      this.playerListeners.put(Player.A, listener);
      return;
    }
    if (!this.playerListeners.containsKey(Player.B)) {
      this.playerListeners.put(Player.B, listener);
      return;
    }
    throw new IllegalStateException("both players have already been added to this game");
  }

  @Override
  public void addRuleToCards(Comparator<AttackValue> rule) {
    if (this.gameStarted) {
      throw new IllegalStateException("cannot change game rules mid game");
    }

    for (Player player : this.unPlayedCards.keySet()) {
      for (Card card : this.unPlayedCards.get(player)) {
        card.addRule(rule);
      }
    }
  }

  @Override
  public void addCardTransformerToCards(Function<AttackValue, AttackValue> func) {
    if (this.gameStarted) {
      throw new IllegalStateException("cannot change game rules mid game");
    }

    for (Player player : this.unPlayedCards.keySet()) {
      for (Card card : this.unPlayedCards.get(player)) {
        card.addCardTransformer(func);
      }
    }
  }

  @Override
  public void playToBoard(int handIndex, int x, int y) {
    if (!this.gameStarted || this.isGameOver()) {
      throw new IllegalStateException("cannot play when game is finished or hasn't started");
    }

    if ((y < 0) || (y > grid.length) || (x < 0) || (x > grid[0].length)) {
      throw new IllegalArgumentException("invalid coordinates");
    }
    if (!grid[y][x].canPlayCard()) {
      throw new IllegalArgumentException("cannot play card there");
    }
    if (handIndex < 0 || handIndex >= this.unPlayedCards.get(curPlayer).size()) {
      throw new IllegalArgumentException("Hand Index out of range");
    }

    Card cardBeingAdded = this.unPlayedCards.get(this.curPlayer).remove(handIndex);
    Set<Slot> alreadySeen = new HashSet<>();
    this.grid[y][x] = cardBeingAdded;
    this.battle(x, y, alreadySeen, this.grid);

    this.curPlayer = Player.nextPlayer(this.curPlayer);

    if (this.isGameOver()) {
      for (Player player : this.playerListeners.keySet()) {
        this.playerListeners.get(player).gameOver();
      }
    } else {
      this.changePlayerForListener(curPlayer);
    }


  }

  /**
   * battles all of the cards around the card at the given coordinate, and recursively calls the
   * method on it's neighbors that it wins against.
   *
   * @param x           the x coordinate of the card
   * @param y           the y coordinate of the card
   * @param alreadySeen the cards that we have already battled their neighbors with
   */
  private int battle(int x, int y, Set<Slot> alreadySeen, Slot[][] gridToActOn) {
    int result = 0;
    alreadySeen.add(gridToActOn[y][x]);
    if (y > 0) {
      Slot placeToCheck = gridToActOn[y - 1][x];
      if (!alreadySeen.contains(placeToCheck) &&
              gridToActOn[y][x].battle(placeToCheck, Direction.UP) &&
              !placeToCheck.getSlotOwner().equals(gridToActOn[y][x].getSlotOwner())) {
        placeToCheck.switchPlayer(curPlayer);
        result += this.battle(x, y - 1, alreadySeen, gridToActOn) + 1;
      }
    }

    if (y < gridToActOn.length - 1) {
      Slot placeToCheck = gridToActOn[y + 1][x];
      if (!alreadySeen.contains(placeToCheck) &&
              gridToActOn[y][x].battle(placeToCheck, Direction.DOWN) &&
              !placeToCheck.getSlotOwner().equals(gridToActOn[y][x].getSlotOwner())) {
        placeToCheck.switchPlayer(curPlayer);
        result += this.battle(x, y + 1, alreadySeen, gridToActOn) + 1;
      }
    }

    if (x > 0) {
      Slot placeToCheck = gridToActOn[y][x - 1];
      if (!alreadySeen.contains(placeToCheck) &&
              gridToActOn[y][x].battle(placeToCheck, Direction.LEFT) &&
              !placeToCheck.getSlotOwner().equals(gridToActOn[y][x].getSlotOwner())) {
        placeToCheck.switchPlayer(curPlayer);
        result += this.battle(x - 1, y, alreadySeen, gridToActOn) + 1;
      }
    }

    if (x < gridToActOn[0].length - 1) {
      Slot placeToCheck = gridToActOn[y][x + 1];
      if (!alreadySeen.contains(placeToCheck) &&
              gridToActOn[y][x].battle(placeToCheck, Direction.RIGHT) &&
              !placeToCheck.getSlotOwner().equals(gridToActOn[y][x].getSlotOwner())) {
        placeToCheck.switchPlayer(curPlayer);
        result += this.battle(x + 1, y, alreadySeen, gridToActOn) + 1;
      }
    }

    return result;
  }

  @Override
  public boolean isGameOver() {
    for (Slot[] row : grid) {
      for (Slot slot : row) {
        if (slot.canPlayCard()) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * gets the number of cards for each player on the board.
   *
   * @return a map of all the number of cards each player owns on the board
   */
  private Map<Player, Integer> getCounts() {
    Map<Player, Integer> numCards = new HashMap<Player, Integer>();

    for (Slot[] row : grid) {
      for (Slot slot : row) {
        if (!(slot.equals(new Empty()) | slot.equals(new Hole()))) {
          slot.addToPlayerCount(numCards);
        }
      }
    }
    return numCards;
  }


  @Override
  public Player gameWinner() {
    if (!this.isGameOver()) {
      throw new IllegalStateException("game is not over");
    }

    Map<Player, Integer> numCards = getCounts();


    if (numCards.get(Player.A) > numCards.get(Player.B)) {
      return Player.A;
    } else if (numCards.get(Player.B) > numCards.get(Player.A)) {
      return Player.B;
    } else {
      return Player.NONE;
    }

  }

  @Override
  public Slot[][] getGrid() {
    Slot[][] gridCopy = new Slot[this.grid.length][this.grid[0].length];
    for (int row = 0; row < this.grid.length; row++) {
      for (int col = 0; col < this.grid[row].length; col++) {
        gridCopy[row][col] = this.grid[row][col].copySlot();
      }
    }
    return gridCopy;
  }

  @Override
  public int gameWidth() {
    return this.grid[0].length;
  }

  @Override
  public int gameHeight() {
    return this.grid.length;
  }

  @Override
  public Slot getCoord(int x, int y) {
    validateCoordinates(x, y);
    return this.grid[y][x].copySlot();
  }

  @Override
  public Player getCellOwner(int x, int y) {
    validateCoordinates(x, y);
    return this.grid[y][x].getSlotOwner();
  }

  @Override
  public boolean isMoveLegal(int x, int y) {
    try {
      validateCoordinates(x, y);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return this.grid[y][x].canPlayCard();
  }

  @Override
  public int score(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("player cannot be null");
    }

    Map<Player, Integer> numCards = getCounts();

    return numCards.getOrDefault(player, 0)
            + this.unPlayedCards.getOrDefault(player, List.of()).size();
  }

  @Override
  public int possibleCardsFlipped(Card card, int x, int y) {
    if (card == null) {
      throw new IllegalArgumentException("card cannot be null");
    }
    if (!this.isMoveLegal(x, y)) {
      throw new IllegalArgumentException("move is not legal");
    }
    Slot[][] gridCopy = this.getGrid();
    gridCopy[y][x] = card.copyCard();
    return this.battle(x, y, new HashSet<>(), gridCopy);
  }

  @Override
  public List<Card> getHand(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("player cannot be null");
    }

    List<Card> ret = new ArrayList<>();
    for (Card card : this.unPlayedCards.get(player)) {
      ret.add(card.copyCard());
    }

    return ret;
  }

  @Override
  public Player curPlayer() {
    return this.curPlayer;
  }

  /**
   * checks whether x and y are valid coordinates in the grid.
   * @param x the x value of the coordinate
   * @param y the y value of the coordinate
   */
  private void validateCoordinates(int x, int y) {
    if (x < 0 || y < 0 || x >= this.grid[0].length || y >= this.grid.length) {
      throw new IllegalArgumentException("indexes are out of bounds for this grid");
    }
  }
}
