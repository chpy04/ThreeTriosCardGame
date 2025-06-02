import java.util.List;
import java.util.Random;
import java.util.function.Function;

import adapters.model.ModelAdapter;
import adapters.view.ViewAdapter;
import controller.Controller;
import controller.GamePlayer;
import controller.PlayerActions;
import controller.HumanPlayer;
import controller.StrategyPlayer;
import model.AttackValue;
import model.Board;
import model.Card;
import model.Direction;
import model.Empty;
import model.Hole;
import model.Player;
import model.ReadOnlyBoard;
import model.Slot;
import model.ThreeTriosBoard;
import model.ViewModel;
import model.stategy.GoForCorners;
import model.stategy.LeastChanceOfBeingFlipped;
import model.stategy.MostFlipped;
import provider.view.gui.frame.TrioGraphicView;
import view.JFrameView;
import view.TTView;

/**
 * represents a class to run the model.
 */
public final class ThreeTrios {

  /**
   * main method to run our board.
   * @param args command line arguments
   */
  public static void main(String[] args) {

    if (args.length < 2 ) {
      throw new IllegalArgumentException("Need two or more arguments for command line");
    }
    Card card1 = new Card.CardBuilder().addName("c1")
            .addValue(Direction.UP, AttackValue.THREE)
            .addValue(Direction.DOWN, AttackValue.FIVE)
            .addValue(Direction.LEFT, AttackValue.SEVEN)
            .addValue(Direction.RIGHT, AttackValue.A)
            .addPlayer(Player.NONE)
            .build();

    Card card2 = new Card.CardBuilder().addName("c2")
            .addValue(Direction.UP, AttackValue.TWO)
            .addValue(Direction.DOWN, AttackValue.NINE)
            .addValue(Direction.LEFT, AttackValue.A)
            .addValue(Direction.RIGHT, AttackValue.A)
            .addPlayer(Player.NONE)
            .build();

    Card card3 = new Card.CardBuilder().addName("c3")
            .addValue(Direction.UP, AttackValue.ONE)
            .addValue(Direction.DOWN, AttackValue.A)
            .addValue(Direction.LEFT, AttackValue.ONE)
            .addValue(Direction.RIGHT, AttackValue.NINE)
            .addPlayer(Player.NONE)
            .build();

    Card card4 = new Card.CardBuilder().addName("c4")
            .addValue(Direction.UP, AttackValue.FIVE)
            .addValue(Direction.DOWN, AttackValue.FIVE)
            .addValue(Direction.LEFT, AttackValue.FIVE)
            .addValue(Direction.RIGHT, AttackValue.FIVE)
            .addPlayer(Player.NONE)
            .build();

    Card card5 = new Card.CardBuilder().addName("c5")
            .addValue(Direction.UP, AttackValue.ONE)
            .addValue(Direction.DOWN, AttackValue.FOUR)
            .addValue(Direction.LEFT, AttackValue.SEVEN)
            .addValue(Direction.RIGHT, AttackValue.ONE)
            .addPlayer(Player.NONE)
            .build();
    Card card6 = new Card.CardBuilder().addName("c6")
            .addValue(Direction.UP, AttackValue.A)
            .addValue(Direction.DOWN, AttackValue.SIX)
            .addValue(Direction.LEFT, AttackValue.EIGHT)
            .addValue(Direction.RIGHT, AttackValue.FOUR)
            .addPlayer(Player.NONE)
            .build();
    Empty empty = new Empty();
    Hole hole = new Hole();
    Slot[][] grid = {{hole, empty, hole}, {empty, empty, hole}, {hole, empty, hole}};
    List<Card> cards = List.of(card1, card2, card3, card4, card5, card6);
    Board model = new ThreeTriosBoard(grid, cards, new Random(4), false);
    ReadOnlyBoard viewModel = new ViewModel(model);
    TTView viewPlayer1 = new JFrameView(viewModel);
//    TTView viewPlayer2 = new JFrameView(viewModel);
    TrioGraphicView viewPlayer2 = new TrioGraphicView(new ModelAdapter(model));
    GamePlayer player1;
    GamePlayer player2;

    switch (args[0]) {
      case "human":
        player1 = new HumanPlayer(model);
        break;

      case "strategy1":
        player1 = new StrategyPlayer(model, new MostFlipped());
        break;

      case "strategy2":
        player1 = new StrategyPlayer(model, new GoForCorners());
        break;

      case "strategy3":
        player1 = new StrategyPlayer(model, new LeastChanceOfBeingFlipped());
        break;
      default:
        throw new IllegalArgumentException("need a case!");
    }

    switch (args[1]) {
      case "human":
        player2 = new HumanPlayer(model);
        break;

      case "strategy1":
        player2 = new StrategyPlayer(model, new MostFlipped());
        break;

      case "strategy2":
        player2 = new StrategyPlayer(model, new GoForCorners());
        break;

      case "strategy3":
        player2 = new StrategyPlayer(model, new LeastChanceOfBeingFlipped());
        break;
      default:
        throw new IllegalArgumentException("need a case!");
    }

    class ReverseFunc implements Function<AttackValue, AttackValue> {
      @Override
      public AttackValue apply(AttackValue attackValue) {
        return AttackValue.valueFromInt(11 - attackValue.value);
      }
    }

    class FallenAce implements Function<AttackValue, AttackValue> {
      @Override
      public AttackValue apply(AttackValue attackValue) {
        if (attackValue.value == 10) {
          return AttackValue.valueFromInt(1);
        } else {
          return AttackValue.valueFromInt(attackValue.value + 1);
        }
      }
    }

    if (args.length > 2) {
      switch (args[2]) {
        case "fallenAce":
          model.addCardTransformerToCards(new FallenAce());
          break;

        case "reverse":
          model.addCardTransformerToCards(new ReverseFunc());
          break;
        default:
          throw new IllegalArgumentException("not a valid varient");
      }
    }

    if (args.length > 3) {
      switch (args[3]) {
        case "fallenAce":
          model.addCardTransformerToCards(new FallenAce());
          break;

        case "reverse":
          model.addCardTransformerToCards(new ReverseFunc());
          break;
        default:
          throw new IllegalArgumentException("not a valid varient");
      }
    }

    PlayerActions controller1 = new Controller(model, player1, viewPlayer1);
    PlayerActions controller2 = new Controller(model, player2, new ViewAdapter(viewPlayer2));
//    PlayerActions controller2 = new Controller(model, player2, viewPlayer2);
    model.startGame();
  }
}
