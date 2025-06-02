package model.stategy;

/**
 * A move that keeps track of the hand index, x position, and y position of a move that a player
 * could make in a TT game, as well as the score indicating how good the move is as decided
 * by the strategy that created this move.
 */
public class Move implements IMove {
  private final int handIdx;
  private final int xCord;
  private final int yCord;
  private final int score;

  /**
   * Creates an immutable move with the given parameters.
   * @param handIdx the index of the hand (0-indexed)
   * @param xCord the x coordinate (0-indexed, from the left)
   * @param yCord the y coordinate (0-indexed, from the top)
   * @param score the score of this move as decided by the strategy that created this move
   */
  public Move(int handIdx, int xCord, int yCord, int score) {
    this.handIdx = handIdx;
    this.xCord = xCord;
    this.yCord = yCord;
    this.score = score;
  }

  @Override
  public int handIdx() {
    return handIdx;
  }

  @Override
  public int xCord() {
    return xCord;
  }

  @Override
  public int yCord() {
    return yCord;
  }

  @Override
  public int score() {
    return score;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IMove)) {
      return false;
    }
    return this.handIdx == ((IMove) o).handIdx() &&
            this.xCord == ((IMove) o).xCord() &&
            this.yCord == ((IMove) o).yCord() &&
            this.score == ((IMove) o).score();
  }

  @Override
  public int hashCode() {
    return this.handIdx + this.xCord + this.yCord + this.score;
  }

  @Override
  public String toString() {
    return String.format("new Move(%d, %d, %d, %d)", handIdx, xCord, yCord, score);
  }
}
