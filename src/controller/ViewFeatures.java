package controller;

import model.Player;

/**
 * represents callbacks that the view uses after certain events occur.
 */
public interface ViewFeatures {

  /**
   * indicates that there was an attempt to play to the grid at a specic location.
   * @param row the row that the user is trying to place a card on
   * @param col the column that the user to place a card on
   */
  void handleGridPlay(int row, int col);

  /**
   * indiciates that there was an attempt to select a different card.
   * @param player the player who's deck it comes form
   * @param cardIdx the index of the card in the hand
   * @throws IllegalArgumentException if player is null or Player.None;
   */
  void handleSelectCard(Player player, int cardIdx);
}

