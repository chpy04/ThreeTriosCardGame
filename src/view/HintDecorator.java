package view;

import java.awt.TextField;

import controller.ViewFeatures;
import model.Card;
import model.ReadOnlyBoard;

/**
 * Represents a decorator for a grid panel that adds hints to empty cells showing how many
 * cards the selected card would flip if played there.
 */
public class HintDecorator implements GridPanel {
  private final GridPanel delegate;
  private final ReadOnlyBoard model;

  /**
   * Creates this decorator with the given delegate to overlay its hints onto.
   * @param delegate the delegate that hints will be overlayed onto
   * @param model the model that will be used to determine the value of the hints
   */
  public HintDecorator(GridPanel delegate, ReadOnlyBoard model) {
    this.delegate = delegate;
    this.model = model;
  }

  @Override
  public void refresh(int selectedCardIdx) {

    this.delegate.refresh(selectedCardIdx);
    if (selectedCardIdx == -1) {
      return;
    }

    for (int yPos = 0; yPos < model.gameHeight(); yPos++) {
      for (int xPos = 0; xPos < model.gameWidth(); xPos++) {
        if (model.isMoveLegal(xPos, yPos)) {
          Card selectedCard = model.getHand(model.curPlayer()).get(selectedCardIdx);
          int hintVal = model.possibleCardsFlipped(selectedCard, xPos, yPos);
          getCell(xPos, yPos).add(new TextField(hintVal + ""));
        }
      }
    }
    this.delegate.revalidate();
  }

  @Override
  public JCell getCell(int x, int y) {
    return this.delegate.getCell(x, y);
  }

  @Override
  public void revalidate() {
    this.delegate.revalidate();
  }

  @Override
  public void addListener(ViewFeatures listener) {
    this.delegate.addListener(listener);
  }
}
