package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import controller.ViewFeatures;
import model.Card;
import model.Player;
import model.ReadOnlyBoard;

/**
 * A panel that displays a players hand in a TT game.
 */
public class TTPlayerPanel extends JPanel implements PlayerPanel {

  private final Player handOwner;
  private ViewFeatures features;
  private final ReadOnlyBoard model;

  /**
   * Creates a panel to display the given players hand in the given game.
   * @param handOwner the player that owns the hand
   * @param model the game
   */
  public TTPlayerPanel(Player handOwner, ReadOnlyBoard model) {
    this.model = model;
    this.handOwner = handOwner;
    ClickListener listener = new ClickListener();
    this.addMouseListener(listener);
  }

  @Override
  public void refresh(int selectedCard) {
    List<Card> hand = model.getHand(this.handOwner);
    this.removeAll();
    this.setLayout(new GridLayout(hand.size(), 1));
    for (int cardIdx = 0; cardIdx < hand.size(); cardIdx++) {
      JCell cell = new JCell(hand.get(cardIdx), selectedCard == cardIdx);
      cell.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / hand.size()));
      this.add(cell);
    }
    this.validate();
  }

  @Override
  public void addListener(ViewFeatures listener) {
    this.features = Objects.requireNonNull(listener);
  }

  private AffineTransform transformModelToPhysical() {
    int handSize = model.getHand(this.handOwner).size();
    AffineTransform ret = new AffineTransform();
    ret.scale(getWidth(), (double)getHeight() / handSize);
    return ret;
  }

  private AffineTransform transformPhysicalToModel() {
    int handSize = model.getHand(this.handOwner).size();
    AffineTransform ret = new AffineTransform();
    ret.scale((double) 1 / getWidth(), (double)handSize / getHeight());
    return ret;
  }


  /**
   * A click listener that listens to different events on this panel.
   */
  private class ClickListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

      Point physicalPoint = e.getPoint();

      Point2D logical = transformPhysicalToModel().transform(physicalPoint, null);
      features.handleSelectCard(handOwner, (int)logical.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
      //don't need this
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      //don't need this
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      //don't need this
    }

    @Override
    public void mouseExited(MouseEvent e) {
      //don't need this
    }
  }
}
