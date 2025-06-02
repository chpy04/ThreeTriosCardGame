package model.stategy;

import java.util.List;

import model.Player;
import model.ReadOnlyBoard;

/**
 * Represents a strategy in a TT game that will always produce a move (if one exists).
 */
public interface InfallibleStategy {

  /**
   * Chooses a move based on this strategy. If this strategy evaluates two moves as being
   * equally good, it will choose the one furthest up, furthest left, and with the lowest
   * hand index, in that order.
   * @param model the model that is used to decide on moves
   * @param player the players whose turn it is
   * @return the move this strategy evaluated as being the best
   * @throws IllegalStateException if there are no valid moves
   */
  IMove chooseMove(ReadOnlyBoard model, Player player) throws IllegalStateException;

  /**
   * Produces a list of all the moves with the best score when evaluated by this model.
   * @param model the model that is used to decide on moves
   * @param player the players whose turn it is
   * @return a list of all the best moves
   */
  List<IMove> allBestMoveCandidates(ReadOnlyBoard model, Player player);

  /**
   * Behaves like allBestMoveCandidates, but only evaluates a given subset of moves.
   * @param options the subset of moves that will be evaluated
   * @param model the model that is used to decide on moves
   * @param player the players whose turn it is
   * @return a list of all the best moves of the given moves
   */
  List<IMove> chooseFromOptions(List<IMove> options, ReadOnlyBoard model, Player player);
}
