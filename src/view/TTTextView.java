package view;

import java.util.List;
import controller.ViewFeatures;
import model.Card;
import model.ReadOnlyBoard;
import model.Slot;

/**
 * A view for a Three Trios game that displays a textual output of the game.
 */
public class TTTextView implements TTView {

  ReadOnlyBoard model;
  Appendable appendable;

  /**
   * Creates a view of this three trios game.
   * @param model the model that will determine the state of the game
   * @param appendable the appendable that this view will write to
   */
  public TTTextView(ReadOnlyBoard model, Appendable appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("model or appendable cannot be null");
    }
    this.appendable = appendable;
    this.model = model;
  }

  /**
   * Helper constructor that writes to System.out.
   * @param model the model that will determine the state of the game
   */
  public TTTextView(ReadOnlyBoard model) {
    this(model, System.out);
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder("Player: " + model.curPlayer().toString() + "\n");
    Slot[][] grid = model.getGrid();
    for (Slot[] row : grid) {
      for (Slot itemInRow : row) {
        ret.append(itemInRow.boardPrint());
      }
      ret.append("\n");
    }
    ret.append("Hand:").append("\n");
    List<Card> hand = model.getHand(model.curPlayer());
    for (Slot card : hand) {
      ret.append(card.toString()).append("\n");
    }
    return ret.toString();
  }




  @Override
  public void addListener(ViewFeatures listener) {
    //don't need this
  }

  @Override
  public void refresh(int handIdx) {
    //don't need this
  }

  @Override
  public void makeVisible() {
    //don't need this
  }

  @Override
  public void showMessage(String message) {
    //don't need this
  }
}
