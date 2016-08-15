package cluedo;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.junit.experimental.theories.Theories;

import cluedo.locations.Board;
import cluedo.locations.Door;
import cluedo.locations.Room;
import cluedo.pieces.Card;
import cluedo.pieces.Character;
import cluedo.pieces.CharacterCard;
import cluedo.pieces.RoomCard;
import cluedo.pieces.Weapon;
import cluedo.pieces.WeaponCard;

/**
 * The application class for a Cluedo game. For 3-6 players.
 *
 * @author Daniel Anastasi
 *
 */
public class Game {
	/** The greater application. **/
	private Application app = null;
	/** The graphic user interface for this game. **/
	private GUI gui;
	/** A controller for event listening. **/
	private Listener listener;
	/** The game state. **/
	private String gameState = null;
	/** A return from an event listener **/
	private String eventMessage = null;
	/** A return from a mouse listener **/
	private String mouseClickMessage = null;
	/** A return from a mouse motion event listener **/
	private String mousePosMessage = null;
	/**
	 * Scanner for use in any input scanning, including use by other objects.
	 */
	public static Scanner scan;

	/** Card height, width for drawing. **/
	public final int CARD_HEIGHT, CARD_WIDTH;
	/** Bottom right corner of the GUI canvas. **/
	public final int CARD_X_ORIGIN;
	/** Y coordinate forr drawing cards. **/
	public final int CARD_Y;
	/** The width and height of the game die. **/
	public final int DIE_WIDTH, DIE_HEIGHT;
	/** The x and y position of the game die on the gui. **/
	public final int DIE_X, DIE_Y;

	/** The board to be interacted with. */
	private Board board;
	/** Holds references to all players currently in a game. */
	private List<Player> players;
	/** The character card in the solution. **/
	private Card murderer;
	/** The room card in the solution. **/
	private Card murderRoom;
	/** The weapon card in the solution. **/
	private Card murderWeapon;
	/** The amount of players in the current game. */
	private int numPlayers;
	/** A reference to the player whose turn it is. */
	private Player currentPlayer = null;
	/** For a the result from a player's roll of the dice. **/
	private int diceroll = 1;
	/** The index of the current player in the list of players. **/
	private int currentPlayerIndex = 0;

	public Game(Application app) {
		this.app = app;
		this.gameState = "PLAYING";
		this.scan = new Scanner(System.in);
		this.players = new ArrayList<>(); // prevents null pointer exception, do
											// not remove.

		this.board = new Board();// Set up board
		this.gui = new GUI("CLUEDO", this);
		this.listener = new Listener(gui, this); // set up after objects created
		this.gui.setListener(this.listener);

		// Card drawing parameters defined.
		CARD_HEIGHT = this.gui.WINDOW_HEIGHT - this.board.BOARD_HEIGHT - this.gui.BUTTON_PANEL_HEIGHT
				- this.gui.MENU_HEIGHT - 10; // 20> x <40
		CARD_WIDTH = this.gui.WINDOW_WIDTH / 8; // 8 = 6 cards in a hand + room
												// for die
		CARD_X_ORIGIN = this.gui.WINDOW_WIDTH - this.gui.BORDER_OFFSET;
		CARD_Y = this.board.BOARD_HEIGHT;
		// Die drawing parameters defined.
		DIE_WIDTH = this.gui.WINDOW_WIDTH / 5;
		DIE_HEIGHT = this.gui.WINDOW_HEIGHT / 8;
		DIE_X = 5;
		DIE_Y = this.board.BOARD_HEIGHT + 5;

		this.numPlayers = setNumPlayers(); // Determines the number of players
											// in the game.
		this.players = setupPlayers(); // Player enters their name
		assignCards(); // Assigns all cards in the game.

		int startingPlayer = 0; // generate random number if there is time.

		run(startingPlayer);
		scan.close(); // closes the scanner after running the game.
	}

	/**
	 * Creates all player objects.
	 *
	 * @return A list of player objects.
	 */
	private List<Player> setupPlayers() {
		List<Player> players = new ArrayList<Player>();
		Map<String, Character> nameToChar = new HashMap<>(); // maps names to
																// characters
		List<Object> charNames = new ArrayList<>();
		// adds all character's names to the list.
		for (Character c : this.board.getCharacters()) {
			nameToChar.put(c.NAME, c);
			charNames.add(c.NAME);
		}

		Character character = null;
		String name = null;
		String choice = null;
		// Each player chooses their name and character.
		for (int i = 0; i < this.numPlayers; i++) {
			gui.popUpTextQuery("Please write your name");// Player chooses their
															// name
			awaitResponse("event");
			name = this.eventMessage; // Retrieves the player's name
			this.eventMessage = null; // Resets the event message for future comms from GUI
			gui.radioButtonSelection("Please choose your character.", charNames);// player chooses their character

			awaitResponse("event");
			choice = this.eventMessage; // converts the message into an integer
			character = nameToChar.get(choice);
			charNames.remove(choice); // removes the choice from the list.
			this.eventMessage = null; // Resets the event message for future
										// comms from GUI

			players.add(new Player(name, character)); // The player chooses which character to use from the list

		}
		return players;
	}

	/**
	 * Returns the game board
	 *
	 * @return
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Returns the list of players.
	 *
	 * @return The list of players
	 */
	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Returns the player whose turn it is.
	 *
	 * @return The Player object.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Returns the character card from the solution.
	 *
	 * @return The character card.
	 */
	public Card getMurderer() {
		return murderer;
	}

	/**
	 * Returns the room card from the solution.
	 *
	 * @return The room card.
	 */
	public Card getMurderRoom() {
		return murderRoom;
	}

	/**
	 * Returns the weapon card from the solution.
	 *
	 * @return The weapon card.
	 */
	public Card getMurderWeapon() {
		return murderWeapon;
	}


	/**
	 * Runs the game loop.
	 *
	 * @param The
	 *            index of the starting player in the list of players.
	 */
	public void run(int startingPlayer) {
		currentPlayer = players.get(startingPlayer);

		// The game loop.
		while (players.size() > 1 && this.gameState.equals("PLAYING")) {
			boolean roomEntered = false; // Don't ask about leaving room if they have just entered:

			this.mouseClickMessage = null; //resets the mouse click event message.
			this.listener.changeLabel("\t\t" + this.currentPlayer.PLAYER_NAME + ", roll the die to start your turn!"); // requests  user roll the die.
			this.diceroll = diceRoll(); // Roll dice and display result
			this.gui.draw(); // draws the board

			System.out.println("Player " + currentPlayer.PLAYER_NAME + "'s turn (" + currentPlayer.getCharacter().ABBREV + "): ");
			currentPlayer.printKnownCards();// print out list of cards player knows about
			// Display and process move options if on a traversable board square:
			if (currentPlayer.getCharacterLocation() == null) {
				System.out.printf("Your Dice Roll is:  %d\n ", diceroll);
				System.out.println("Would you like to move or make an accusation?Choose (1)move, or (0)accusation");
				System.out.println("Caution: an accusation may end the game!");
				int choice = 99;

				while (choice != 0 && choice != 1) {
					try {
						choice = scan.nextInt();
					} catch (Exception e) {
						System.out.println("Your choice was invalid. Please try again.");
						scan.next();
						continue;
					}
				}

				if (choice == 1) {
					roomEntered = doMovement();
					if (!roomEntered) {
						System.out.println("Would you like to make an accusation?Choose (1)no, or (0)yes");
						choice = 99;
						while (choice != 0 && choice != 1) {
							try {
								choice = scan.nextInt();
							} catch (Exception e) {
								System.out.println("Your choice was invalid. Please try again.");
								scan.next();
								continue;
							}
						}
						if (choice == 0) {
							accusation(currentPlayer);
							break;
						}
					}
				} else {
					accusation(currentPlayer);
					break;
				}
			}

			// Check if in a room, show leave options or
			if (currentPlayer.getCharacterLocation() != null) {
				Room currentRoom = currentPlayer.getCharacterLocation();
				if (doRoomTurn(currentRoom, roomEntered))
					break;
				else
					doMovement();
			}
			updateCurrentPlayer();

			// Prevents players from viewing eachother's hands
			String securityGap = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
			System.out.printf("\nDon't look at another player's hand!!!%s", securityGap);
		}
		if (this.gameState.equals("NewGAME"))
			Application.newGame(this.app); // starts a new game.

		// Display the winner and close game elements
		System.out.printf("Congratulations Player %s, you have won the game!\n", this.players.get(0).PLAYER_NAME);
	}

	/**
	 * Processes basic movement interactions via the console.
	 *
	 * @param The
	 *            player's dice roll
	 * @return True if the player's character is in a room.
	 */
	private boolean doMovement() {
		char dir; // Players direction choice goes here (w a s d).

		System.out.printf("Your Dice Roll is:  %d\n ", diceroll);
		System.out.println("Use W A S D to move, press enter to apply: ");

		while (this.diceroll > 0) {
			dir = scan.next().charAt(0);
			Character character = currentPlayer.getCharacter(); // the character piece being moved.
			// Move this players character based on the input char:
			try {

				if (currentPlayer.move(dir, board)) {
					--this.diceroll; // Take away from remaining moves:
					System.out.printf("%s  is now at (%d, %d) on the board.\n", character.NAME, character.getX(),
							character.getY());
				} else {
					// If the place player's character is moving to is not
					// traversable:

					this.gui.draw(); // draws the board

					System.out.println("There is no square to move to in that direction, please try again.");
					continue; // Start loop again to receive new input.
				}
			} catch (IOException e) {
				// Catch an exception if the input char from player is not w a s
				// d
				System.out.println(e.getMessage() + "Please use W A S D.");
				scan.next();
				continue; // Start loop again to receive new input.
			}

			if (this.diceroll != 0 && currentPlayer.getCharacterLocation() == null) {

				this.gui.draw(); // draws the board

				currentPlayer.printKnownCards();
				System.out.println("    Moves remaining: " + this.diceroll); // Show moves remaining only after a successful move.


			} else if (currentPlayer.getCharacterLocation() != null) { // If the move resulted in player entering a room:

				this.gui.draw(); // Draws the board with the character in a room.
				currentPlayer.printKnownCards();
				return true;
			} else { // Notify the player they have completed their turn if they did not reach a room:
				this.gui.draw(); // Draws the board with the character in a room.
				System.out.println("Move Turn Complete for Player " + currentPlayer.PLAYER_NAME);

			}
		}
		return false;
	}

	/**
	 * Processes a turn which starts in a room.
	 *
	 * @param The
	 *            current room of the character piece.
	 * @param True
	 *            if the player entered the room during this turn.
	 * @return True if the game is over.
	 */
	private boolean doRoomTurn(Room currentRoom, boolean roomEntered) {
		Character character = currentPlayer.getCharacter();
		// Only ask the player if they want to leave when they haven't entered
		// in the same turn:
		if (!roomEntered) {
			Door exit = null; // Pull coordinates from the door player is
								// leaving from.

			System.out.println("Do you want to leave the current room? (" + currentRoom.NAME + ") y/n: ");
			char input = scan.next().charAt(0);
			if (input != 'y' && input != 'Y') {
				return false;
			}
			// Print the choices of door when there's more than one, and the
			// staircase's room:
			System.out.println("Type the number of the door or staircase you want to leave from: ");
			int i, r = 999;
			while (exit == null) {
				if (currentRoom.getDoors().size() == 1 && currentRoom.getStairs() == null)
					exit = currentRoom.getDoors().get(0); // only one exit
															// available.
				for (i = 0; i < currentRoom.getDoors().size(); ++i)
					System.out.printf("%d: %s\t", i, reverseDir(currentRoom.getDoors().get(i).ROOM_DIRECTION));

				if (currentRoom.getStairs() != null)
					System.out.printf("%d: Stairs to %s\n", i, currentRoom.getStairs().NAME);
				System.out.println();
				// Catch an integer from players input:
				try {
					r = scan.nextInt();

					if (r != currentRoom.getDoors().size()) // if a door is selected, and definitely not stairs.
						exit = currentRoom.getDoors().get(r);
					else
						break;
				} catch (Exception e) {
					System.out.println("Input Error: Please pick a number from the list of doors:");
					continue;
				}
			}

			if (exit != null) {
				board.changeCharacterRoom(character, null);
				character.setPosition(exit.getX(), exit.getY()); // Move the lpayer character to the coordinates of the chosen door:
			} else {
				board.changeCharacterRoom(character, currentRoom.getStairs()); // Moves the character to the room at the other end of the stairs.
				this.diceroll = 0;
				return doRoomEntry(); // asks usual room entry questions.
			}

			this.gui.draw(); // draws the board

			board.changeCharacterRoom(character, null); // Set players room to
														// null
			--this.diceroll;
			return false; // continue with turn from the beginning
		}
		// If just entered into the room, give player options for suggestion,
		// accusation
		// if room has them. All make diceroll 0;
		else if (roomEntered && doRoomEntry())
			return true;
		return false;
	}

	/**
	 * Processes the part of the turn where a player enters a room.
	 *
	 * @return Returns true if the game is over
	 */
	private boolean doRoomEntry() {
		boolean gameOver = false;
		String choice = "";
		this.diceroll = 0;
		try {
			do {
				System.out.println("Would you like to make a (s)uggestion or an (a)ccusation?");
				System.out.println("Caution: an accusation may end the game!");
				choice = scan.next(); // takes player's choice
			} while (!choice.equals("a") && !choice.equals("s"));
		} catch (Exception e) {
			throw new Error(e);
		}

		if (choice.equals("s"))
			suggestion(currentPlayer); // processes player suggestion
		else
			gameOver = accusation(currentPlayer); // calls accusation method

		return gameOver || players.size() == 1; // Game is over because the
												// accusation was correct, or
												// theres only one player left,
	}

	/**
	 * A suggestion made by the player to learn more about the murder.
	 *
	 * @param The
	 *            player who made the suggestion
	 */
	public void suggestion(Player p) throws IllegalArgumentException {
		if (p == null)
			throw new IllegalArgumentException("Null argument received.");
		if (!p.equals(currentPlayer))
			throw new RuntimeException("Only the current player can make suggestions.");

		// give player options for suggestion, accusation
		int characterChoice = 0, weaponChoice = 0;
		Room location = p.getCharacterLocation(); // Suggest from the current
													// room only:
		int roomChoice = Card.ROOM.valueOf(location.NAME).ordinal();

		try {
			do {
				System.out.printf("Which character do you wish to suggest was the murderer?\n");
				// gives choices for character
				for (int c = 0; c < Card.CHARACTER.values().length; c++)
					System.out.printf("  (%d) %s\n", c, Card.CHARACTER.values()[c]);
				characterChoice = scan.nextInt(); // takes user choice

				System.out.printf("Which weapon do you suggest was the murder weapon?\n");
				// gives choices for weapon
				for (int c = 0; c < Card.WEAPON.values().length; c++)
					System.out.printf("  (%d) %s\n", c, Card.WEAPON.values()[c]);
				weaponChoice = scan.nextInt(); // takes user choice

				if (characterChoice < 0 || characterChoice >= Card.CHARACTER.values().length)
					System.out.println("Character could not be found, please try again.");
				else if (weaponChoice < 0 || characterChoice >= Card.WEAPON.values().length)
					System.out.println("Weapon could not be found, please try again.");
				else
					break;
			} while (true);

			System.out.printf("You suggest it was %s in the %s with the %s!\n",
					Card.CHARACTER.values()[characterChoice], Card.ROOM.values()[roomChoice],
					Card.WEAPON.values()[weaponChoice]);
		} catch (Exception e) {
			throw new Error(e);
		}

		Character character = this.board.getCharacters().get(characterChoice);
		Room room = this.board.getRooms().get(roomChoice);
		Weapon weapon = this.board.findWeaponFromName(Weapon.Name.values()[weaponChoice]);

		board.changeCharacterRoom(character, room);
		board.changeWeaponRoom(weapon, room);

		this.gui.draw(); // draws the board

		RoomCard roomCard = this.board.getRoomCards().get(roomChoice);
		CharacterCard characterCard = this.board.getCharacterCards().get(characterChoice);
		WeaponCard weaponCard = this.board.getWeaponCards().get(weaponChoice);

		// Check for a player to refute this suggestion.
		outside: for (int i = 0; i < this.players.size(); i++) {
			Player otherPlayer = this.players.get(i);
			if (!otherPlayer.equals(p)) {
				for (Card c : otherPlayer.getHand()) {
					if (c == roomCard) {
						System.out.printf("Player %d refutes this suggestion with %s\n", i, c.toString());
						p.learn(c);
						break outside;
					} else if (c == characterCard) {
						System.out.printf("Player %d refutes this suggestion with %s\n", i, c.toString());
						p.learn(c);
						break outside;
					} else if (c == weaponCard) {
						System.out.printf("Player %d refutes this suggestion with %s\n", i, c.toString());
						p.learn(c);
						break outside;
					}
				}
			}
		}
		awaitResponse("event");
	}

	/**
	 * Called when a player chooses to make an accusation about the murder. If
	 * they guess correctly they win the game, otherwise they lose and the game
	 * continues without them.
	 *
	 * @param The
	 *            player making the accusation
	 * @return A string message telling the player the result of their
	 *         accusation.
	 */
	public boolean accusation(Player p) {
		if (p == null)
			throw new IllegalArgumentException("Null argument received.");
		if (!p.equals(currentPlayer))
			throw new RuntimeException("Only the current player can make suggestions.");

		int characterChoice = 0, roomChoice = 0, weaponChoice = 0;

		try {
			do {
				System.out.printf("Which character do you want to accuse of murder?\n");
				// gives choices for character
				for (int c = 0; c < Card.CHARACTER.values().length; c++)
					System.out.printf("  (%d) %s\n", c, Card.CHARACTER.values()[c]);
				characterChoice = scan.nextInt(); // takes user choice

				// Ask from room only if player is making an accusation
				System.out.println("Which room do you say that the murder took place?");
				for (int c = 0; c < Card.ROOM.values().length; c++)
					System.out.printf("  (%d) %s\n", c, Card.ROOM.values()[c]);
				roomChoice = scan.nextInt();

				System.out.printf("Which weapon do you accuse the muderer of using?\n");
				// gives choices for weapon
				for (int c = 0; c < Card.WEAPON.values().length; c++)
					System.out.printf("  (%d) %s\n", c, Card.WEAPON.values()[c]);
				weaponChoice = scan.nextInt(); // takes user choice

				if (characterChoice < 0 || characterChoice >= Card.CHARACTER.values().length)
					System.out.println("Character could not be found, please try again.");
				else if (weaponChoice < 0 || characterChoice >= Card.WEAPON.values().length)
					System.out.println("Weapon could not be found, please try again.");
				else
					break;
			} while (true);

		} catch (Exception e) {
			throw new Error(e);
		}
		System.out.printf("Player %d states: It was %s in the %s with the %s!\n", currentPlayer.PLAYER_NAME,
				Card.CHARACTER.values()[characterChoice], Card.ROOM.values()[roomChoice],
				Card.WEAPON.values()[weaponChoice]);

		Card.CHARACTER character = Card.CHARACTER.values()[characterChoice];
		Card.ROOM room = Card.ROOM.values()[roomChoice];
		Card.WEAPON weapon = Card.WEAPON.values()[weaponChoice];

		if (character.equals(this.murderer) || !room.equals(this.murderRoom) || !weapon.equals(this.murderWeapon)) {
			this.players.remove(p); // removes the current player from the game.
			System.out.printf("Player %d has guessed incorrectly, and is out of the game!\n", p.PLAYER_NAME);
			awaitResponse("event");
			return false;
		} else
			return true;
	}

	/**
	 * Pauses gameplay while waiting for a player response.
	 */
	public void awaitResponse(String type) {
;
		if(type.equals("event")){
			while (this.eventMessage == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else if(type.equals("click")){
			while (this.mouseClickMessage == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Determines the number of players in the game
	 *
	 * @return The number of players in the game.
	 */
	private int setNumPlayers() {
		// Sets up player number choosing.
		List<Object> playerNumSelection = new ArrayList<>();
		playerNumSelection.add((Integer) 3);
		playerNumSelection.add((Integer) 4);
		playerNumSelection.add((Integer) 5);
		playerNumSelection.add((Integer) 6);
		this.gui.radioButtonSelection("How many characters are playing?", playerNumSelection); // User(s) choose number of players
		awaitResponse("event");
		Integer i = Integer.valueOf(this.eventMessage);
		System.out.println(eventMessage);
		System.out.println(""+i);
		this.eventMessage = null; // resets the event message for future
									// communications from GUI.
		return i;
	}

	/**
	 * Assigns the solution cards, and assigns all other cards to player's
	 * hands.
	 *
	 * @return The index of the player to start the game.
	 */
	private int assignCards() {
		List<Card> allCards = new ArrayList<>(); // a list for all cards to
													// distribute
		// Chooses character, weapon, and room for murderer, murder weapon and  murder room.
		// Remaining cards are added to allCards in method.
		this.murderer = assignMurderCard(this.board.getCharacterCards(), allCards);
		this.murderRoom = assignMurderCard(this.board.getRoomCards(), allCards);
		this.murderWeapon = assignMurderCard(this.board.getWeaponCards(), allCards);
		if (murderer == null || murderRoom == null || murderWeapon == null)
			throw new Error("Card could not be assigned for solution");
		Card[][] hands = new Card[this.numPlayers][18 / this.numPlayers + 1]; // player
																				// hands

		int startingPlayer = fillHands(hands, allCards);
		// Passes the hands to their respective players.
		for (int i = 0; i < this.numPlayers; i++) {
			this.players.get(i).setHand(hands[i]);
		}
		return startingPlayer;
	}

	/**
	 * Distributes cards from the master list across all hands.
	 *
	 * @param 2D
	 *            array of Cards, the hands to fill.
	 * @param The
	 *            list of cards to distribute.
	 * @return Index of the player to start the game.
	 */
	private int fillHands(Card[][] hands, List<Card> master) {
		int handIndex = 0, index = 0, mIndex = 0;
		// Iterates over each hand, dealing a card until all cards are dealt
		while (master.size() > 0) {

			for (index = 0; index < hands.length; index++) {
				mIndex = (int) (Math.random() * master.size());
				hands[index][handIndex] = master.get(mIndex); // adds a random
																// card to hand
																// i.
				master.remove(mIndex); // prevents the same card being dealt
										// twice
			}
			handIndex++;
		}
		// Finds the starting player based on who was last dealt to.
		return (index >= hands.length - 1 ? 0 : index + 1);
	}

	/**
	 * Chooses a card at random from the selection list. All other cards in the
	 * list are added to the master list.
	 *
	 * @param <T>
	 * @param A
	 *            list of cards to select from
	 * @param A
	 *            master list of cards
	 * @return The chosen card
	 */
	private Card assignMurderCard(List<? extends Card> selection, List<Card> master) {
		Card chosen = null;
		int index = 0;
		index = (int) (Math.random() * selection.size());
		chosen = selection.get(index); // The chosen card
		selection.remove(index); // Removes the chosen card from the list.
		master.addAll(selection); // Puts the cards in the master list
		return chosen;
	}

	/**
	 * Returns a random number between 1 and 6 for use as a dice roll. Requires
	 * user input.
	 *
	 * @return An random integer between 1 and 6.
	 */
	private int diceRoll() {
		awaitResponse("click");
		this.mouseClickMessage = null; // resets the event message.
		return (int) (Math.random() * 6 + 1);
	}

	/**
	 * Returns a String of the direction corresponding to the reverse of the
	 * given direction string. Used with the direction held in Door objects.
	 *
	 * @param dir
	 * @return
	 */
	private static String reverseDir(String dir) {
		switch (dir) {
		case "UP":
			return "Bottom";
		case "RIGHT":
			return "Left";
		case "DOWN":
			return "Top";
		case "LEFT":
			return "Right";
		default:
			throw new IllegalArgumentException("Direction is not recognised");
		}
	}

	/**
	 * Iterates over the list of players to find the player whose turn is next.
	 */
	public void updateCurrentPlayer() {
		// Check if the next player is the beginning of the list of players:
		if (currentPlayerIndex + 1 == players.size()) {
			currentPlayerIndex = 0;
			currentPlayer = players.get(currentPlayerIndex);
		} else { // Otherwise increment the index and find the next player with
					// it
			currentPlayer = players.get(++currentPlayerIndex);
		}
	}

	public void draw(Graphics g) {
		this.board.draw(g);

		int x = 0;
		Card[] hand = null;
		// draws the players'cards
		for (Player p : this.players) {
			hand = p.getHand(); // player hand
			for (int i = 0; i < hand.length; i++) {
				if (hand[i] != null) {
					x = CARD_X_ORIGIN - ((i + 1) * CARD_WIDTH);
					hand[i].draw(g, x, CARD_Y, CARD_WIDTH, CARD_HEIGHT);
				}
			}
		}
		// draws the die
		g.drawImage(this.board.getDieImage(this.diceroll), this.DIE_X, this.DIE_Y, this.DIE_WIDTH, this.DIE_HEIGHT, null);
	}

	/**
	 * Sets the most recent message from an event listener
	 * @param The  message
	 */
	public void setEventMessage(String message) {
		this.eventMessage = message;
	}

	/**
	 * /** Displays a message over the selected square.
	 * @param The position of mouse click
	 * @param The y position of the mouse click.
	 */
	public void doToolTip(int x, int y) {
		//TODO
	}

	/**
	 * Sets the value of teh mouse click event message, detailing where the mouse was clicked.
	 * @param message
	 */
	public void setMouseClickMessage(String message){
		this.mouseClickMessage = message;
	}

}
