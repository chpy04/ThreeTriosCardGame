# Three Trios Card Game

The following project is trying to model an implementation of the ThreeTrios game. If you aren't familiar with the game, it's rules (for this iteration) are as follows: There are two players who are each dealt a hand of cards. Each Card has values on each of its sides (so north, south, east, and west). The objective of the game is to have the most cards owned by you when there are no more spots to place cards. When you place a card on the board, you enter the battle stage. In the battle stage, the card you just placed compares the numbers on each of it's sides to the adjacent sides (so it would compare it's value for the left side with the left card's right side). Intuitively, if there is no card on any of the adjacent sides, there is no battle. However, if that card wins a battle, it now belongs to the player who placed that card, and that card now begins trying to battle it's adjacent cards. So in theory, one card placement could lead to flipping all of the cards on the board. This placing then battling stage repeats until there are no places to place cards on the board.

Our model is comprised of the following interfaces: ReadOnlyBoard, Board, and Slot. At a high level, the board is responsible for managing a 2D array of Slots, representing either Cards, Empty Places, or Holes. The whole game relies on these slots for controlling placements and battles. Overall, the Board USES Slots to control the game. The Board is where all of the interaction is managed. The inheritance tree for Slots are as given:

```

                              ------------------Slot------------------
                              |                                       |
                            Card                            ----NonCardSlot----
                                                            |                  |
                                                          Empty               Hole
```

Here is now a explanation of all of the pieces of our project:

I will start with some "helper" data that makes it easy for us to model the game.

AttackValue:

- represents one of ten values that Card edges can hold
- flexible enough to be able to add more values if the attackvalues ever change
- able to compare two attack values

Player:

- represents the people taking turns playing the game
- like AttackValue, player is flexible in that we can add more players if the requirements for the game change to encorporate more than two players
- Can get the first player AND the next player

ConfigParser:

- a helper class to parse configuration text files for testing

Direction:

- represents the directions of the cards and on the board in general
- used to orient the board and cards
- able to get the opposite direction, given a current direction
- like the previous enums, we can add directions if, for example, we need to add corners

Now, the bulk of the design

ReadOnlyBoard

- represents the read-only representation of the board
- methods output useful functions for rendering or checking the state of the game (like getting the hand of a particular player)

Board

- represents a board that can do everything ReadOnlyBoard can do, but with the ability to play to certain positions
- this is the model that allows us to actually play the game

ThreeTriosBoard

- this is the actual implementation of the Board model.
- Within this board, we use a 2D array of Slots to represent the spaces on the grid, a map from Player to List<Card> for each player's hand, and the current Player who's turn it currently is

Slot

- represents one slot on the board
- can either be empty, a hole, or a card
- can battle other slots, compare attack values, and other things

Card

- represents the actual playable slot
- holds a map for each direction to it's attack value
  - this allows us to account for more directions if they are needed
- holds it's name and it's owner
- owner can change, so the method to change it's owner actually has an effect

NonCardSlot

- abstract class to represent a slot that is not a card
- because Holes and Emptys are highly related, it made sense to abstract their behavior
- holds the value of whether a card can be played to it

Empty

- represents the slots where you can play a card
- does not hold any other information besides the fact that you can play a card to it
- always wins in battle (to account for cards "playing" emptys)

Hole

- is almost identical to Empty, but you cannot play a card to it
- also does not hold any additional information
- always wins in battle (to account for cards "playing" emptys")

The above described everything in our model, which can be found in the model package under the src directory

The following is regarding the view

ThreeTriosView

- represents a view for the models we just described
- can render the model

TTTextView

- A text view for the ThreeTrios game
- it's "input" is the model it's given and it outputs it to an appendable
- also has an implemented toString to get the direct string representation of the model

To actually run our program, you can use the following code snippet
Side note, to run our model, Java 11 is recommended with IntelliJ set up.

Code Snippet:

```
String boardPath = "docs" + File.separator + "nboard.config";
String cardsPath = "docs" + File.separator + "17cards.config";
ConfigParser parser = new ConfigParser();
try {
boardReader = new Scanner(new File(boardPath));
cardReader = new Scanner(new File(cardsPath));
} catch (FileNotFoundException e) {
throw new IllegalArgumentException("file name does not exist");
}

model = new ThreeTriosBoard(parser.parseBoard(boardReader), parser.parseCards(cardReader), new Random(3));
view = new TTTextView(model);
System.out.print(view.toString());
```

This will print out a N-shaped board in it's initial state (RED, the first player, still yet to place their first card). Essentially, this snippet parses the text configuration files we had preset, populates the model accordingly, initializes the view, and displays it.

The following is regarding the GUI View.

GridPanel

- represents a panel on the view that holds the grid and handles user interaction

PlayerPanel

- represents the panel on the view that holds a user's hand and handles user interaction

JFrameView

- the JFrame that is the top-level component of our view. This component is what handles the
  end displaying
- interacts with TTGridPanel and TTPlayerPanels during click events
- outputs click events to the console
- keeps track of which card is selected in the current player's hand

TTGridPanel

- represents the JPanel that holds the actual grid
- handles user click events and translates these events to logical points on the model grid

TTPlayerPanel

- represents the JPanel that holds a player's hand
- handles user click events and translates these events to logical points on the model grid

JCell

- represents a helper JComponent that is used inside of TTGridPanel and TTPlayerPanel to render individual slots on the grid (card, empty, hole)

Strategies

IMove

- represents a move that can be made in a TT game
- keeps track of the hand index, x index, y index (all 0-indexed), and the score of the move

InfallibleStrategy

- represents a strategy that cannot fail unless there are no valid moves.
- is able too
  - produce a list of moves that are tied for being the best from a given list of possible moves
  - produce a list of moves that are tied for being the best from every possible move
  - produce the best move, deciding tie breakers by choosing the upper-most, left-most, least hand

GoForCorners

- a strategy that attempts to play cards to the corners, favoriing cards that show higher values

MostFlipped

- a strategy that attempts to flip as many of the opponents cards as possible

EXTRA CREDIT STRATEGIES:

LeastChanceOfBeingFlipped

- a strategy that minimizes the opponents chance of flipping the card played on this move

Minimax

- a strategy that minimizes the score of the opponents next move assuming a given strategy

CombinedStrategy

- a strategy that takes in two strategies, and uses the second to break ties from the first

Controller and Features

ModelFeatures

- represents features that that model uses to notify other objects of events occuring.
  Used for callbacks to the controller when moves are made or the game is over

PlayerActions

- represents actions that the player can do at any time. In this case, a player can only play to
  the board at a specific location with a specific card.

ViewFeatures

- represents features that the view uses to signal to the controller of a particular event, like
  the selected card changing or playing to the grid.

GamePlayer

- represents a player in the game
- can add listeners to itself so that it can notify others of events occurring

Controller

- implements ModelFeatures, PlayerActions, and ViewFeatures so that it can be added as a listener
  for each part of the model or view that requires it.

HumanPlayer

- represents a human playing the game
- essentially does nothing because the action from a human player will be coming from the view

StrategyPlayer

- represents an ai playing the game according to a certain strategy that it's been given
- instead of the view handeling moves, this is where operations for the computer occur.

ThreeTrios:

To run our game, use the main method within this file. Pass it two arguments in the command line
when you run it. The first argument corresponds to the first player and teh second argument
coresponds to the seconid player. The following values are acceptable:

"human" -> means a human will be playing it through the view
"strategy1" -> means a computer will be playing with the MostFlipped strategy
"strategy2" -> means a computer will be playing with the GoForCorners strategy
"strategy3" -> means a computer will be playing with the LeastChanceOfBeingFlipped strategy

<-------------------------------------------------------------------------------------------------->

Changes for part 2:

methods added:

- gameWidth
- gameHeight
- getCoord
- getCellOwner
- isMoveLegal
- score
- possibleCardsFlipped

The functionality that was missing in our model was:

- getting how big the grid was
- getting the contents of a cell at a specific position
- getting which player owns the card at a specific position
- checking whether a move is legal
- checking how many cards can be flipped by a given move
- calculating the player's score

For checking the size of the grid, we added two methods called gameWidth and gameHeight that returns the width and height of the game grid respectively, which are both calculated by the dimensions of our 2D array

For getting the contents of a cell, we made a copy of the Slot at that position in our array and returned it

For checking which player owns the card at a specific position, we made a method in slot that got the owner and made an extra value in our player enum called "NONE" which represents a hole or empty owner. So, we just called that method on the cell at that position.

For checking whether a move is legal, we used a method in our slot interface that allowed us to see whether a card could be played there (and whether the position was inbounds)

For checking how many cards can be flipped by a given move, we modified a helper method called "battle" in ThreeTriosBoard to accept a grid and return an integer representing how many cards were flipped. This allowed us to pass this method a copy of our grid and slots so that we could see how many cards were flipped without modifying the objects we were using for the actual game.

For calculating the player's score, we used a method in slot that allows us to count the number of cards a player owns on the board. We then added this value up with how many cards were in the player's deck.

We chose to add all of this functionality because it allows our view to observe the state of the game much more, which is important when communicating with the controller we will implement in the future(also because it was required by the assignment).

We also added these methods to Slots:

- getSlotOwner
- getSlotColor
- getDirectionalValues

These methods were added to support the new methods we needed to add to ReadOnlyBoard

Besides these new methods, our model implementation did not change. We already implemented the ReadOnlyBoard in the previous assignment, so we just needed to add the missing methods to our model

View Implementation

Our GUI view is split between two different types of Panels, PlayerPanel and GridPanel. PlayerPanels represent the panels holding users' cards in their deck, and GridPanel represents the view for the actual grid. We implement what is needed for these panels in TTPlayerPanel and TTGridPanel. Both of these classes extend JPanel so they can be used in our JFrame GUI view. In our TTGridPanel class, we use a component called JCell to render each individual cell.

Overall our JFrameView is composed of two TTPlayerPanels and one TTGridPanel. The JFrameView manages which card the user has selected, which the two TTPlayerPanels use in their rendering. Each TTPlayerPanel manages where a user clicks, and sends the hand index to the JFrameView for the JFrameView to handle the rest of the operations. The same thing for the TTGridPanel; when a user clicks on the grid, TTGridPanel translates the physical coordiantes to the logical coordinates of the grid and sends these to the JFrameView to be handled.

NOTE: We handle selecting cards based on who's turn it is. For example, if it is blue's turn, a user interacting with our JFrame would only be able to select a card in Blue's hand. That being said, you can still click on the other hand, it just won't do anything (and you can see this in the printing). Moreover, we implemented it such that when a card is selected and then the grid is clicked, the card is deselected.

Strategy Implementation

We chose to only implement infallible strategies, as fallible strategies would almost always be converted into infallible strategies using the upper-most, left-most moves. Every infallible strategy works with moves, which keep track of all the information necessary to make a move as well as a score which determines how good the move was. While a move does not know which strategy its score corresponds too, this is useful for comparing different moves within a strategy.

Strategies have the obvious functionality of being able to return one move they deem the best, but they are also able to produce a list of moves that they are unable to distinguish between as the best, as well as produce a list of the best moves from a list of given options. This allows us to combine strategies, by passing the tied moves from one strategy as options for a second tie breaking strategy.

For the minimax strategy, we made the decision to take in the assumed opponents strategy in the constructor. This is because we do not have a concrete way to compare different strategies, and this method allowed for the most flexibility as if a new strategy were created it could be minimized without changing any code

EXTRA CREDIT: strategy implementation can be found in the model.strategy package, all strategy tests can be found in the strategyTest file. We initially had test files for each strategy but had to condense files for handins.

For mocks, we made a number of mocks that accomplish different jobs for testing:

- FlippedCardsGreatestAtCoordMock: returns 1 for number of flipped cards at the given coord, 0 OW, this helps test strategies that rely on number of flipped cards
- NoOpenCornersMock: says that every corner is filled, helps with corners strategy
- NoValidMovesMock: says that there are no valid moves, helps with error testing
- OnlyOneValidMoveMock: says that there is only one valid move, every strat should return it
- RecordCheckedPositionsAndFlippedMock: records methods called by strategies

---

Changes for part 3

We added functionality for controllers across the game.

In our model, this meant we now had to add a method to add listeners for certain events.

In our view, this also meant adding a method to add listeners for certain events.

The ModelFeatures interface represents classes who depend on the certain actions of the model. For example, the you can call a function to notify the passed object of a change of turn, or that the game is over

The ViewFeatures interface represents classes who depend on the actions of the view, so clicking on certain parts of the screen essentially.

The PlayerActions interface represents actions that a player can do. In reality, the only action is playing a specific card to a specific square in the game. This is used for our GamePlayers when the player is not inputting through the view.

GamePlayer represents the entity actually playing the game. In practice, its either a human or a strategy. The human player does nothing here because the actual plays are coming from the view. The strategy players are the ones that actually make moves (and utilize the PlayerActions feature).

The controller for this assignment implemented ModelFeatures, ViewFeatures, and PlayerActions, as the controller acts as a listener for the view, model, and player, and these are the listener types for the corresponding components.
