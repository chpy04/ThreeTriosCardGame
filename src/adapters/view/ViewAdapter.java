package adapters.view;

import adapters.model.FeaturesAdapter;
import controller.ViewFeatures;
import provider.view.gui.frame.TrioFrame;
import view.TTView;

/**
 * An adapter that converts a given TrioGrame view into a TTView that can be used with our
 * controller.
 */
public class ViewAdapter implements TTView {

  private final TrioFrame delegate;

  /**
   * Creates this adapter that will offload functionality to a delegate.
   * @param delegate a TrioFrame that will handle equivalent calls
   */
  public ViewAdapter(TrioFrame delegate) {
    this.delegate = delegate;
    delegate.setInputEnabled(true);
  }

  @Override
  public void addListener(ViewFeatures listener) {
    delegate.addPlayerActionListener(new FeaturesAdapter(listener));
  }

  @Override
  public void refresh(int selectedCard) {
    delegate.update();
  }

  @Override
  public void makeVisible() {
    delegate.setVisible(true);
  }

  @Override
  public void showMessage(String message) {
    delegate.showError(message);
  }
}
