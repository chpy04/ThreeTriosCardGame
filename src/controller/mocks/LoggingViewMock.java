package controller.mocks;

import controller.ViewFeatures;
import view.TTView;

/**
 * A mock view that logs when refresh or showmessage is called.
 */
public class LoggingViewMock implements TTView {
  StringBuilder sb;

  /**
   * Creates this mock with a string builder to append the log to.
   * @param sb what the log is appended to
   */
  public LoggingViewMock(StringBuilder sb) {
    this.sb = sb;
  }

  @Override
  public void addListener(ViewFeatures listener) {
    //does nothing here for mock
  }

  @Override
  public void refresh(int selectedCard) {
    sb.append("Tried to Refresh with selected card at index " + selectedCard).append("\n");
  }

  @Override
  public void makeVisible() {
    //does nothing here for mock
  }

  @Override
  public void showMessage(String message) {
    sb.append("Tried to send message: " + message).append("\n");
  }
}
