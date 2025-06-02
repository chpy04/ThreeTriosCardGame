package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Map;

import javax.swing.JPanel;

import model.AttackValue;
import model.Direction;
import model.Slot;

/**
 * A cell that displays a slot from a TT game.
 */
public class JCell extends JPanel {
  Slot cell;
  boolean selected;

  /**
   * Constructs this cell with a given slot, and a boolean for whether it is selected.
   * @param cell the cell that will be displayed
   * @param selected if this cell is selected
   */
  public JCell(Slot cell, boolean selected) {
    this.cell = cell;
    this.selected = selected;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
    g2d.setColor(this.cell.getSlotColor());
    if (this.selected) {
      g2d.fillRect(5, 5, this.getWidth() - 10, this.getHeight() - 10);
    } else {
      g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    g2d.setColor(Color.BLACK);

    Map<Direction, AttackValue> values = this.cell.getDirectionalValues();

    Font font = new Font("Arial", Font.PLAIN, 12);

    g2d.setFont(font);
    FontMetrics metrics = g2d.getFontMetrics(font);
    int textHeight = metrics.getHeight();

    for (Direction key : values.keySet()) {
      int xQuarter = 0;
      int yQuarter = 0;
      switch (key) {
        case RIGHT:
          xQuarter = 3;
          yQuarter = 2;
          break;
        case LEFT:
          xQuarter = 1;
          yQuarter = 2;
          break;
        case UP:
          xQuarter = 2;
          yQuarter = 1;
          break;
        case DOWN:
          xQuarter = 2;
          yQuarter = 3;
          break;
        default:
          //no need for this, will get one of the cases
          break;

      }
      int textWidth = metrics.stringWidth(values.get(key).toString());
      int xCenter = (int) (this.getWidth() * xQuarter / 4);
      int yCenter = (int) (this.getHeight() * yQuarter / 4);

      g2d.drawString(values.get(key).toString(),
              xCenter - (textWidth / 2), yCenter + (textHeight / 2));

    }
  }
}
