package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Objects;


import javax.swing.JPanel;

import controller.ViewFeatures;
import model.ReadOnlyBoard;
import model.Slot;

/**
 * A panel to display the grid in a three trios game.
 */
public class TTGridPanel extends JPanel implements GridPanel {

  private ViewFeatures features;
  private ReadOnlyBoard model;

  /**
   * Creates this panel with the model that will be displayed.
   * @param model the model whose grid will be displayed
   */
  public TTGridPanel(ReadOnlyBoard model) {
    this.model = model;
    TTGridPanel.ClickListener listener = new TTGridPanel.ClickListener();
    this.addMouseListener(listener);
    refresh(-1);
  }

  @Override
  public void refresh(int selectedCardIdx) {
    this.removeAll();
    Slot[][] grid = model.getGrid();
    this.setLayout(new GridLayout(model.gameHeight(), model.gameWidth()));

    for (int ypos = 0; ypos < model.gameHeight(); ypos++) {
      for (int xpos = 0; xpos < model.gameWidth(); xpos++) {
        JPanel cellBox = new JCell(model.getCoord(xpos, ypos), false);
        cellBox.setPreferredSize(new Dimension(this.getWidth() / grid[0].length,
                this.getHeight() / grid.length));
        this.add(cellBox);
      }
    }
    this.validate();
  }

  @Override
  public JCell getCell(int x, int y) {
    return (JCell)this.getComponent(y * model.gameWidth() + x);
  }

  @Override
  public void addListener(ViewFeatures listener) {
    this.features = Objects.requireNonNull(listener);
  }

  private AffineTransform transformModelToPhysical() {
    AffineTransform ret = new AffineTransform();
    ret.scale((double)getWidth() / model.gameWidth(), (double)getHeight() / model.gameHeight());
    return ret;
  }

  private AffineTransform transformPhysicalToModel() {
    AffineTransform ret = new AffineTransform();
    ret.scale((double)model.gameWidth() / getWidth(), (double)model.gameHeight() / getHeight());
    return ret;
  }

  /**
   * A click listener to listen to mouse actions on this panel.
   */
  private class ClickListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
      Point physicalPoint = e.getPoint();
      Point2D logical = transformPhysicalToModel().transform(physicalPoint, null);
      features.handleGridPlay((int)logical.getY(), (int)logical.getX());
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      // nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      // nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
      // nothing
    }
  }
}
