package provider.view.gui.board;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Container;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import provider.model.grid.cell.Cell;
import provider.model.grid.cell.CellCardStatus;
import provider.model.cards.ICard;
import provider.model.cards.Direction;
import provider.model.player.PlayerColor;

/**
 * Class representing the panels of the game that are the cells on a board, colored by their state.
 */
public class CellPanel extends JPanel {
  private Cell cell;
  private static final Color RED_COLOR = new Color(255, 200, 200);
  private static final Color BLUE_COLOR = new Color(173, 216, 230);
  private static final Color EMPTY_COLOR = new Color(255, 255, 150);
  private static final Color HOLE_COLOR = Color.DARK_GRAY;

  /**
   * Constructor for CellPanel.
   */
  public CellPanel(Cell cell) {
    this.cell = cell;
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    this.setPreferredSize(new Dimension(100, 100));

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        Container board = CellPanel.this.getParent();
        if (board instanceof TrioBoardPanel) {
          ((TrioBoardPanel) board).setBoardMove(cell.getLoc());
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (cell.getCardStatus() == CellCardStatus.HOLE) {
      g2d.setColor(HOLE_COLOR);
    } else if (cell.getCardStatus() == CellCardStatus.FULL) {
      g2d.setColor(cell.getOwner() == PlayerColor.RED ? RED_COLOR : BLUE_COLOR);
    } else {
      paintEmpty(g2d);
    }
    g2d.fillRect(0, 0, getWidth(), getHeight());

    if (cell.getCardStatus() == CellCardStatus.FULL) {
      drawCard(g2d, cell.getCard());
    }

    g2d.dispose();
  }

  private void paintEmpty(Graphics2D g2d) {
    g2d.setColor(new Color(226, 216, 76));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    this.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public void setCell(Cell cur) {
    this.cell = cur;
  }

  private void drawCard(Graphics2D g2d, ICard card) {
    int w = getWidth();
    int h = getHeight();
    int centerX = w / 2;
    int centerY = h / 2;

    g2d.setFont(new Font("Arial", Font.BOLD, 24));
    g2d.setColor(Color.BLACK);

    g2d.drawString(card.getAttackValue(Direction.NORTH).getStr(), centerX - 10, h / 4);
    g2d.drawString(card.getAttackValue(Direction.SOUTH).getStr(), centerX - 10, h * 3 / 4);
    g2d.drawString(card.getAttackValue(Direction.WEST).getStr(), w / 4 - 10, centerY + 10);
    g2d.drawString(card.getAttackValue(Direction.EAST).getStr(), w * 3 / 4 - 10, centerY + 10);
  }
}
