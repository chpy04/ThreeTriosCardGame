package provider.view.gui.board;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import adapters.model.CoordinateAdapter;
import provider.model.ReadonlyTrioGame;
import provider.model.grid.cell.Cell;
import provider.model.grid.ICoordinate;

/**
 * TrioBoardPanelImpl class that puts the boards together as a set of panels.
 */
public class TrioBoardPanelImpl extends JPanel implements TrioBoardPanel {
  private final ReadonlyTrioGame model;
  private final CellPanel[][] cellPanels;
  private final List<CellClickListener> listeners;
  private static final int CELL_MIN_SIZE = 50;
  private static final int CELL_PADDING = 2;

  /**
   * Contructor for TrioBoardPanelImpl that ensures model is not null.
   */
  public TrioBoardPanelImpl(ReadonlyTrioGame model, ClickCallBack clickCallback) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.listeners = new ArrayList<>();
    this.cellPanels = new CellPanel[model.getHeight()][model.getWidth()];

    setupLayout();
    setupResizeListener();
    initializeCells(clickCallback);
  }

  private void setupLayout() {
    setLayout(new GridLayout(model.getHeight(), model.getWidth(), CELL_PADDING, CELL_PADDING));
    setBorder(BorderFactory.createEmptyBorder(CELL_PADDING,
            CELL_PADDING, CELL_PADDING, CELL_PADDING));
  }

  private void setupResizeListener() {
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        int width = getWidth() - (CELL_PADDING * (model.getWidth() + 1));
        int height = getHeight() - (CELL_PADDING * (model.getHeight() + 1));

        int cellWidth = Math.max(CELL_MIN_SIZE, width / model.getWidth());
        int cellHeight = Math.max(CELL_MIN_SIZE, height / model.getHeight());

        for (CellPanel[] row : cellPanels) {
          for (CellPanel cell : row) {
            cell.setPreferredSize(new Dimension(cellWidth, cellHeight));
          }
        }
        revalidate();
      }
    });
  }

  private void initializeCells(ClickCallBack clickCallback) {
    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        ICoordinate coord = new CoordinateAdapter(row, col);
        Cell cell = model.getCell(coord);
        CellPanel panel = new CellPanel(cell);

        final int finalRow = row;
        final int finalCol = col;
        panel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
            ICoordinate clickedCoord = new CoordinateAdapter(finalRow, finalCol);
            if (clickCallback != null) {
              clickCallback.cellClicked(clickedCoord);
            }
            notifyCellClicked(clickedCoord);
          }
        });

        cellPanels[row][col] = panel;
        add(panel);
      }
    }
  }

  @Override
  public void setBoardMove(ICoordinate coord) {
    if (coord != null) {
      notifyCellClicked(coord);
    }
  }

  @Override
  public void update() {
    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        ICoordinate coord = new CoordinateAdapter(row, col);
        cellPanels[row][col].setCell(model.getCell(coord));
      }
    }
    revalidate();
    repaint();
  }

  @Override
  public void addCellClickListener(CellClickListener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  private void notifyCellClicked(ICoordinate coord) {
    for (CellClickListener listener : listeners) {
      listener.onCellClicked(coord);
    }
  }
}
