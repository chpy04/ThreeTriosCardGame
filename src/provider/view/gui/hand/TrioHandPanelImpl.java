package provider.view.gui.hand;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import provider.model.ReadonlyTrioGame;
import provider.model.cards.ICard;
import provider.model.player.PlayerColor;

/**
 * Class used to see deck in game.
 */
public class TrioHandPanelImpl extends JPanel implements TrioHandPanel {
  private final ReadonlyTrioGame model;
  private Dimension prefSize;
  private final List<HandCardButton> handButtons;
  private final PlayerColor owner;
  private final TriCardClickCallback clickCallback;

  /**
   * Functional interface that represnts a callback for a card click.
   */
  @FunctionalInterface
  public interface TriCardClickCallback {
    void onCardClick(HandCardButton button, int index, PlayerColor owner);
  }

  /**
   * Constructor for TrioHandPanelImpl.
   */
  public TrioHandPanelImpl(ReadonlyTrioGame model,
                           PlayerColor player, TriCardClickCallback callback) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
    this.owner = player;
    this.clickCallback = callback;
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.prefSize = new Dimension(100, model.getHeight() * 120);
    this.handButtons = new ArrayList<>();

    initializeHandButtons();
  }

  private void initializeHandButtons() {
    List<ICard> modelHand = model.getHand(owner);
    for (int i = 0; i < modelHand.size(); i++) {
      HandCardButton cardButton = new HandCardButton(modelHand.get(i), owner);
      final int index = i;

      cardButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          if (clickCallback != null) {
            clickCallback.onCardClick(cardButton, index, owner);
          }
        }
      });

      handButtons.add(cardButton);
      this.add(cardButton);
    }
  }

  @Override
  public void setPreferredSize(Dimension d) {
    Objects.requireNonNull(d);
    this.prefSize = d;
    if (!handButtons.isEmpty()) {
      int buttonHeight = d.height / handButtons.size();
      for (HandCardButton button : handButtons) {
        button.setPreferredSize(new Dimension(d.width - 1, buttonHeight - 1));
      }
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return prefSize;
  }

  @Override
  public void clearSelections() {
    for (HandCardButton button : handButtons) {
      button.setSelected(false);
    }
    repaint();
  }

  @Override
  public void update() {
    removeAll();
    handButtons.clear();
    initializeHandButtons();
    revalidate();
    repaint();
  }
}