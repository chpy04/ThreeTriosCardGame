package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.ViewFeatures;
import model.Player;
import model.ReadOnlyBoard;

/**
 * A frame that contains all the content for a TT game, including
 * two player panels and a grid panel.
 */
public class JFrameView extends JFrame implements TTView {

  private final TTGridPanel gridPanel;
  private final TTPlayerPanel player1Panel;
  private final TTPlayerPanel player2Panel;
  private final ReadOnlyBoard model;
  private static final int MINIMUM_CELL_SIZE = 50;
  private boolean displayHints;
  private int selectedCard;

  /**
   * Creates this frame with the given model.
   * @param model the model that will be displayed
   */
  public JFrameView(ReadOnlyBoard model) {
    super();
    this.selectedCard = -1;
    this.addKeyListener(new Keyboard());
    this.model = Objects.requireNonNull(model);
    this.gridPanel = new TTGridPanel(model);
    this.gridPanel.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
    this.player1Panel = new TTPlayerPanel(Player.A, model);
    this.player2Panel = new TTPlayerPanel(Player.B, model);

    this.setSize(600, 600);
    this.setMinimumSize(new Dimension((model.gameWidth() + 2) * MINIMUM_CELL_SIZE,
            model.gameHeight() * MINIMUM_CELL_SIZE));
    this.setTitle("Three Trios");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    this.player1Panel.setPreferredSize(new Dimension(this.getWidth() / 8, this.getHeight()));
    this.player1Panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 2));
    this.player2Panel.setPreferredSize(new Dimension(this.getWidth() / 8, this.getHeight()));
    this.player2Panel.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 4));
    this.gridPanel.setPreferredSize(new Dimension(this.getWidth() * 6 / 8, this.getHeight()));

    this.add(player1Panel, BorderLayout.WEST);
    this.add(gridPanel, BorderLayout.CENTER);
    this.add(player2Panel, BorderLayout.EAST);

    this.player1Panel.refresh(-1);
    this.player2Panel.refresh(-1);
  }



  @Override
  public void addListener(ViewFeatures listener) {
    this.player1Panel.addListener(listener);
    this.player2Panel.addListener(listener);
    this.gridPanel.addListener(listener);
  }

  @Override
  public void refresh(int selectedCard) {
    this.selectedCard = selectedCard;
    if (model.curPlayer().equals(Player.A)) {
      this.player1Panel.refresh(selectedCard);
      this.player2Panel.refresh(-1);
    } else if (model.curPlayer().equals(Player.B)) {
      this.player1Panel.refresh(-1);
      this.player2Panel.refresh(selectedCard);
    }
    if (displayHints) {
      new HintDecorator(this.gridPanel, model).refresh(selectedCard);
    } else {
      this.gridPanel.refresh(selectedCard);
    }
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.PLAIN_MESSAGE);
  }

  private class Keyboard implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == 'h') {
        displayHints = !displayHints;
        refresh(selectedCard);
      }
    }

    @Override
    public void keyPressed(KeyEvent e) {
      //nothing needed here
    }

    @Override
    public void keyReleased(KeyEvent e) {
      //nothing needed here
    }
  }
}
