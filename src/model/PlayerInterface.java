package model;

/**
 * Represents a player (either AI or an actual person).
 */
public interface PlayerInterface {
  /**
   * Plays one card on the model.
   * @param model the model to make a move on
   */
  void playMove(Board model);

  /*
  Essentially, the only thing a player has to do to interact with the board is to play a move.
  Given the model as input, the player can determine in it's own implementation which move it
  should make. For an AI player, it could run the properties of the model through some algorithm to
  make the most "optimal" move. For an actual player, it would prompt the user for command line
  input and then make the corresponding move. The important thing is, our player only needs to
  know about the model to make an informed decision on what card and which place to play that card.
   */
}
