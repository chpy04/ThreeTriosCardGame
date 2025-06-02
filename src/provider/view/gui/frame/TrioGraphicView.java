package provider.view.gui.frame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import provider.model.ReadonlyTrioGame;
import provider.model.cards.ICard;
import provider.model.grid.ICoordinate;
import provider.model.player.PlayerColor;
import provider.view.PlayerAction;
import provider.view.gui.board.TrioBoardPanel;
import provider.view.gui.board.TrioBoardPanelImpl;
import provider.view.gui.hand.HandCardButton;
import provider.view.gui.hand.TrioHandPanel;
import provider.view.gui.hand.TrioHandPanelImpl;

/**
 * Graphic view of TrioGame.
 */
public class TrioGraphicView extends JFrame implements TrioFrame {
  private final ReadonlyTrioGame model;
  private final TrioBoardPanel boardPanel;
  private final TrioHandPanel redHand;
  private final TrioHandPanel blueHand;
  private ICard selectedCard;
  private int selectedCardIndex;
  private final List<PlayerAction> observerList;
  private boolean inputEnabled;

  /**
   * Constructor for graphic view of Trio Game.
   */
  public TrioGraphicView(ReadonlyTrioGame model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;

    observerList = new ArrayList<>();
    inputEnabled = false;

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Three Trios - " + model.getCurrPlayer() + "'s Turn");

    this.redHand = new TrioHandPanelImpl(model, PlayerColor.RED, this::handleCardSelection);
    this.blueHand = new TrioHandPanelImpl(model, PlayerColor.BLUE, this::handleCardSelection);
    this.boardPanel = new TrioBoardPanelImpl(model, this::handleBoardClick);

    this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    this.add((Component) redHand);
    this.add((Component) boardPanel);
    this.add((Component) blueHand);

    this.setSize(model.getWidth() * 100 + 300, model.getHeight() * 120 + 50);

    adjustComponentSizes();

    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        adjustComponentSizes();
      }
    });
  }

  private void adjustComponentSizes() {
    int totalWidth = getWidth();
    int totalHeight = getHeight();

    int handWidth = totalWidth / 6;
    int boardWidth = totalWidth - (2 * handWidth) - 40;

    Dimension handSize = new Dimension(handWidth, totalHeight - 50);
    Dimension boardSize = new Dimension(boardWidth, totalHeight - 50);

    redHand.setPreferredSize(handSize);
    blueHand.setPreferredSize(handSize);
    boardPanel.setPreferredSize(boardSize);

    revalidate();
  }

  @Override
  public void update() {
    this.setTitle("Three Trios - " + model.getCurrPlayer() + "'s Turn");
    redHand.update();
    blueHand.update();
    boardPanel.update();
    revalidate();
    repaint();
    if (model.isGameOver()) {
      PlayerColor winner = model.getWinner();
      if (winner == null) {
        setTitle("Game Over - It's a tie!");
      } else {
        setTitle("Game Over - " + winner + " wins!");
      }
    }
  }

  @Override
  public void display(boolean show) {
    this.setVisible(show);
  }

  private void handleCardSelection(HandCardButton button, int index, PlayerColor owner) {
    if (owner == model.getCurrPlayer() && inputEnabled) {
      if (selectedCard == button.getCard() && selectedCardIndex == index) {
        selectedCard = null;
        selectedCardIndex = -1;
        button.setSelected(false);
      } else {
        selectedCard = button.getCard();
        selectedCardIndex = index;
        if (owner == PlayerColor.RED) {
          redHand.clearSelections();
        } else {
          blueHand.clearSelections();
        }
        button.setSelected(true);
      }
      for (PlayerAction listener : observerList) {
        listener.onCardSelected(index, owner);
      }
      // DEBUGGING
      System.out.println("Selected card index " + index + " in " + owner.name() + "'s hand");
    }
  }

  private void handleBoardClick(ICoordinate coord) {
    System.out.println("cell clicked: " + coord);
    if (selectedCard != null && selectedCardIndex >= 0 && model.isLegalMove(selectedCard, coord)) {
      try {
        for (PlayerAction listener : observerList) {
          listener.onPositionSelected(coord);
        }
        selectedCard = null;
        selectedCardIndex = -1;
        redHand.clearSelections();
        blueHand.clearSelections();
        update();
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("Invalid move: " + e.getMessage());
      }
    }
  }

  @Override
  public void addPlayerActionListener(PlayerAction listener) {
    observerList.add(listener);

  }

  @Override
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void render() {
    update();
    display(true);
  }

  public void setViewPosition(Point point) {
    setLocation(point);
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
  }

  @Override
  public void setInputEnabled(boolean enabled) {
    inputEnabled = enabled;
  }
}

