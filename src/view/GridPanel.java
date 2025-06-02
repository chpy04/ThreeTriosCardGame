package view;

/**
 * A Panel that displays the grid fo a three trios game.
 */
public interface GridPanel extends Panel {

  /**
   * Refreshes this view.
   */
  void refresh(int selectedCardIdx);

  JCell getCell(int x, int y);

  void revalidate();

}
