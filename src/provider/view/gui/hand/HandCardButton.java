package provider.view.gui.hand;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import provider.model.cards.ICard;
import provider.model.cards.Direction;
import provider.model.player.PlayerColor;

/**
 * Class that allows for the selection of a card in your deck.
 */
public class HandCardButton extends JPanel {
  private final ICard card;
  private final PlayerColor color;
  private boolean selected;
  private Dimension prefSize;

  /**
   * Constructor for HandCardButton.
   */
  public HandCardButton(ICard card, PlayerColor color) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    this.card = card;
    this.color = color;
    this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    this.setPreferredSize(new Dimension(100, 100));
    this.selected = false;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    if (selected) {
      g2d.setColor(Color.YELLOW);
      g2d.setStroke(new BasicStroke(3));
      g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
    }

    g2d.setColor(color == PlayerColor.RED ? new Color(226, 76, 78) : new Color(76, 167, 226));
    g2d.fillRect(5, 5, getWidth()  - 10, getHeight() - 10);

    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 30));

    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;

    g2d.drawString(card.getAttackValue(Direction.NORTH).getStr(), centerX - 10, 35);
    g2d.drawString(card.getAttackValue(Direction.SOUTH).getStr(), centerX - 10, getHeight() - 15);
    g2d.drawString(card.getAttackValue(Direction.WEST).getStr(), 25, centerY + 10);
    g2d.drawString(card.getAttackValue(Direction.EAST).getStr(), getWidth() - 35, centerY + 10);

    g2d.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    return prefSize;
  }

  @Override
  public void setPreferredSize(Dimension d) {
    prefSize = d;
  }

  public boolean getSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
    repaint();
  }

  public ICard getCard() {
    return card;
  }
}