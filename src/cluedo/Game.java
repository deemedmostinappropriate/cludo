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
	/** A return from a key press event. **/
	private char keyMessage = 'p';
	/** Special messages only used in the case of an accusation or suggestion. **/
	private String charSuggestionMessage = null, roomSuggestionMessage = null, weaponSuggestionMessage = null;
	/** Lets game know that a button was pushed. Only to be used within awaitResponse.**/
	private boolean buttonRegistered = false;

	/**Scanner for use in any input scanning, including use by other objects. **/
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
	private int diceroll = 0;
	/** The index of the current player in the list of players. **/
	private int currentPlayerIndex = 0;


	public <T> Game(Application app) {
		this.app = app;
		this.gameState = "PLAYING";
		this.scan = new Scanner(System.in);
		this.players = new ArrayList<>(); // prevents null pointer exception, do not remove.
		this.board = new Board();// Set up board
		this.gui = new GUI("CLUEDO", this);
		this.listener = new Listener(gui, this); // set up after objects created
		this.gui.setListener(this.listener);	//passes the listener to the gui.
		// updates the gui's comboboxes with object lists and titles
		this.gui.setComboBoxItems((List<T>)this.board.getCharacters(), (List<T>)this.board.getRooms(), (List<T>)this.board.getWeapons(),
				"Characters", "Rooms", "Weapons");

		// Card drawing parameters defined.
		CARD_HEIGHT = this.gui.WINDOW_HEIGHT - this.board.BOARD_HEIGHT - this.gui.BUTTON_PANEL_HEIGHT - this.gui.MENU_HEIGHT - 10; // 20> x <40
		CARD_WIDTH = this.gui.WINDOW_WIDTH / 8; // 8 = 6 cards in a hand + room  for die
		CARD_X_ORIGIN = this.gui.WINDOW_WIDTH - this.gui.BORDER_OFFSET;
		CARD_Y = this.board.BOARD_HEIGHT;
		// Die drawing parameters defined.
		DIE_WIDTH = this.gui.WINDOW_WIDTH / 5;
		DIE_HEIGHT = this.gui.WINDOW_HEIGHT / 8;
		DIE_X = 5;
		DIE_Y = this.board.BOARD_HEIGHT + 5;

		this.numPlayers = setNumPlayers(); // Determines the number of players in the game.
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

			gui.textQuery("Please write your name");// Player chooses their
			// name
			awaitResponse("event");
			name = this.eventMessage; // Retrieves the player's name
			this.eventMessage = null; // Resets the event message for future comms from GUI

			// loops while waiting for character selection. Character cannot be null.
			while(character == null){
				gui.radioButtonSelection("Please choose your character.", charNames);// player chooses their character
				awaitResponse("event");
				choice = this.eventMessage; // converts the message into an integer
				character = nameToChar.get(choice);
				charNames.remove(choice); // removes the choice from the list.


				players.add(new Player(name, character)); // The player chooses which character to use from the list

				this.eventMessage = null; // Resets the event message for future comms from GUI

			}
			character = null;	//resets for another loop.

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
	 * Sets the most recent message from an event listener
	 * @param The  message
	 */
	public void setEventMessage(String message) {
		this.eventMessage = message;
	}

	/**
	 * Sets the value of teh mouse click event message, detailing where the mouse was clicked.
	 * @param message
	 */
	public void setMouseClickMessage(String message){
		this.mouseClickMessage = message;
	}

	/**
	 * Sets the key message character to the given argument.
	 * @param The character returned from a key press.
	 */
	public void setKeyMessage(char c) {
		this.keyMessage = c;
	}

	/**
	 * Sets the charSuggestionMessage field.
	 * @param The new message value.
	 */
	public void setCharacterSuggestionMesssage(String s){
		this.charSuggestionMessage = s;
	}

	/**
	 * Sets the weaponSuggestionMessage field.
	 * @param The new message value.
	 */
	public void setWeaponSuggestionMesssage(String s){
		this.weaponSuggestionMessage = s;
	}

	/**
	 * Sets the roomSuggestionMessage field.
	 * @param The new message value.
	 */
	public void setRoomSuggestionMesssage(String s){
		this.roomSuggestionMessage = s;
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
			this.gui.draw(); 				// draws the board so that player's hand is displayed
			boolean roomEntered = false; 	// Don't ask about leaving room if they have just entered:

			this.mouseClickMessage = null; 	//resets the mouse click event message.
			this.listener.changeLabel("\t\t" + this.currentPlayer.PLAYER_NAME + ", roll the die to start your turn!"); // requests  user roll the die.
			this.diceroll = diceRoll(); 	// Roll dice and display result
			this.gui.draw(); 				// draws the game so that dice is drawn

			System.out.println("Player " + currentPlayer.PLAYER_NAME + "'s turn (" + currentPlayer.getCharacter().ABBREV + "): ");
			currentPlayer.printKnownCards();// print out list of cards player knows about

			// Display and process move options if on a traversable board square:
			if (currentPlayer.getCharacterLocation() == null) {
				roomEntered = doMovementTurn();

				// handles player losing game
				if(this.currentPlayer == null){
					updateCurrentPlayer();
					continue; //player's turn has ended.
				}
				if (!roomEntered) {
					List<Object> list = new ArrayList<>();
					list.add("End turn");
					list.add("Accusation");
					// tells player that they can make an accusation before ending their turn.
					this.gui.radioButtonSelection("Would you like to end your turn or make an accusation? Caution accusations may make you lose the game.", list);
					awaitResponse("event");	//await player response
					if(this.eventMessage.equals("Accusation")){
						this.eventMessage = null;
						accusation();	//player makes an accusation
					}
					this.eventMessage = null; //resets the event message
				}
				else{
					// player entered a room
					doRoomEntry();
					// handles player losing game
					if(this.currentPlayer == null){
						updateCurrentPlayer();
						continue; //player's turn has ended.
					}
				}
			}
			else{
				//player started turn in a room
				Room currentRoom = currentPlayer.getCharacterLocation();
				doRoomTurn(currentRoom, roomEntered);
				if(this.players.size() == 1)
					break;
				doMovementTurn();
			}
			updateCurrentPlayer();
		}
		if (this.gameState.equals("NewGAME"))
			Application.newGame(this.app); // starts a new game.

		// Display the winner and close game elements
		System.out.printf("Congratulations Player %s, you have won the game!\n", this.players.get(0).PLAYER_NAME);
	}

	/**
	 * Processes basic movement interactions via the console.
	 * @param <T>
	 * @param The player's dice roll
	 * @return True if the player's character is in a room.
	 */
	private <T> boolean doMovementTurn() {
		boolean actionMade = false;

		while (this.diceroll > 0) {
			Character character = currentPlayer.getCharacter(); // the character piece being moved.
			this.listener.changeLabel("\t" + this.currentPlayer.PLAYER_NAME + "("+ character.ABBREV +"), move with WASD. You have "+this.diceroll+"moves left.");

			// loops until the player has made a move or an accusation
			while(!actionMade){
				awaitResponse("movementOrAccusation");		//awaits a key press from the player. Can also result in an accusation
				if(this.keyMessage != 'p'){
					doStep();
					actionMade = true;
				}
				else{
					// accusation path
					this.eventMessage = null;		//resets event message after accusation button selection.
					// asks the player if they mean to make an accusation, and processes it if they do.
					if(preAccusation())
						break;
				}
			}
			if(currentPlayer == null)
				return false;
			if(currentPlayer.getCharacterLocation() != null) { // If the move resulted in player entering a room:
				return true;
			}
			this.keyMessage = 'p';		//resets the key press message.
			this.eventMessage = null;	//resets the event message to null. Useful when an accusation is made.
			actionMade = false;			//allows for choice between movement and accusation.
		}
		this.keyMessage = 'p';		//resets the key press message.
		this.eventMessage = null;	//resets the event message to null. Useful when an accusation is made.

		this.diceroll = 0; //keeps die image at zero until next player rolls
		this.listener.changeLabel(this.currentPlayer.PLAYER_NAME + " your turn is now over. ");// Notify the player they have completed their turn. They have not reached a room.
		return false;
	}

	/**
	 * Moves the player's character one step if able.
	 */
	private void doStep(){
		// Move this players character based on the input char:
		if (currentPlayer.move(this.keyMessage, board)){
			--this.diceroll; // Take away from remaining moves:
			this.gui.draw(); // draws the board with the character moved to new location.
		}else{
			this.listener.changeLabel("        You cannot walk in that direction");//notify player that they cannot walk in the specified direction
		}
	}

	/**
	 * Asks the player if they mean to make an accusation, and processes it if they do.
	 * @return Returns true if the player made an accusation
	 */
	private boolean preAccusation(){
		List<Object> list = new ArrayList<>();
		list.add("Yes");
		list.add("No");
		//Gives player option not to accuse.
		this.gui.radioButtonSelection("An accusation can make you win or lose the game. Do you want to continue?", list);
		awaitResponse("event");
		if(this.eventMessage.equals("Yes")){
			this.eventMessage = null;
			//accusation dialog is made
			this.accusation();
			return true;
		}
		this.eventMessage = null;		//resets event message.  Is needed here, do not remove.
		return false;
	}

	/**
	 * Processes a turn which starts in a room.
	 * @param The current room of the character piece.
	 * @param True
	 */
	private void doRoomTurn(Room currentRoom, boolean roomEntered) {
		Character character = currentPlayer.getCharacter();
		// Only ask the player if they want to leave when they haven't entered
		// in the same turn:
		if (!roomEntered) {
			Door exit = null; // Pull coordinates from the door player is
			// leaving from.

			System.out.println("Do you want to leave the current room? (" + currentRoom.NAME + ") y/n: ");
			char input = scan.next().charAt(0);
			if (input != 'y' && input != 'Y') {
				return;
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
				return;
			}

			this.gui.draw(); // draws the board

			board.changeCharacterRoom(character, null); // Set players room to
			// null
			--this.diceroll;
			return ; // continue with turn from the beginning
		}
	}

	/**
	 * Processes the part of the turn where a player enters a room.
	 */
	private void doRoomEntry() {
		List<Object> list = new ArrayList<>();
		list.add("Suggestion");
		list.add("Accusation");
		//requests choice from user
		this.gui.radioButtonSelection("Would you like to make a suggestion or an accusation? Caution: Accusation will make you win or lose the game", list);
		awaitResponse("event");//awaits choice from user

		if(this.eventMessage.equals("Suggestion")){
			this.eventMessage = null;	//resets event message.
			suggestion(currentPlayer);
		}
		else{
			this.eventMessage = null;	//resets event message.
			accusation();		//accusation path
		}
		this.eventMessage = null;
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

		RoomCard roomCard = null;
		CharacterCard characterCard = null;
		WeaponCard weaponCard = null;
		Character character = null;
		Weapon weapon = null;
		List<Weapon> weapons = this.board.getWeapons();
		List<Character> chars = this.board.getCharacters();
		String refutingPlayerName = "noone.", refute = null;

		this.gui.suggestionSelection("Make your suggestion from the choices below.");		//requests choices from player
		awaitResponse("event");		//awaits the player's choice

		//Gets the room which the player is in, and finds the relative card.
		Room room = this.currentPlayer.getCharacterLocation();
		int roomIndex = Card.ROOM.valueOf(p.getCharacterLocation().NAME).ordinal();	//uses ordering of Card.ROOM enum to find the current roo
		roomCard = this.board.getRoomCards().get(roomIndex);

		//gets the character from the suggestion using its name. Character card found using resulting index.
		for(int i = 0; i < chars.size(); i++){
			if(chars.get(i).NAME.equals(this.charSuggestionMessage)){
				character = chars.get(i);
				characterCard = this.board.getCharacterCards().get(i);
			}
		}

		//gets the weapon from the suggestion using its name. Weapon card found using resulting index.
		for(int i = 0; i < weapons.size(); i++){
			if(weapons.get(i).toString().equals(this.weaponSuggestionMessage)){
				weapon = weapons.get(i);

				weaponCard = this.board.getWeaponCards().get(i);
			}
		}
		// moves the pieces to the new room
		board.changeCharacterRoom(character, room);
		board.changeWeaponRoom(weapon, room);

		this.gui.draw(); // draws the board to show character and weapon in new room
		String s = null;
		// Check for a player to refute this suggestion.
		outside:
			for (int i = 0; i < this.players.size(); i++) {
				Player otherPlayer = this.players.get(i);
				if (!otherPlayer.equals(p)) {
					for (Card c : otherPlayer.getHand()) {
						if(c == null)
							continue;	//prevents null pointer exception
						if (c.equals(roomCard)) {
							refutingPlayerName = otherPlayer.PLAYER_NAME;
							refute = otherPlayer.PLAYER_NAME + "who has the"+c.toString()+" card";
							p.learn(c);
							break outside;
						} else if (c.equals(characterCard)) {
							refutingPlayerName = otherPlayer.PLAYER_NAME;
							refute = otherPlayer.PLAYER_NAME + "who has the"+c.toString()+" card";
							p.learn(c);
							break outside;
						} else if (c.equals(weaponCard)){
							refutingPlayerName = otherPlayer.PLAYER_NAME;
							refute = otherPlayer.PLAYER_NAME + "who has the"+c.toString()+" card";
							p.learn(c);
							break outside;
						}
					}
				}
			}

		this.gui.basicAlert("Suggestion Results!",
				"",
				this.currentPlayer.PLAYER_NAME + "suggests:",
				"", "",
				"It was "+ this.charSuggestionMessage,
				"",
				"in the "+ room.toString(),
				"",
				"with the "+this.weaponSuggestionMessage+".",
				"",
				"This has been refuted by "+ refutingPlayerName,
				refute);
	}

	/**
	 * Called when a player chooses to make an accusation about the murder.
	 * If they guess correctly they win the game, otherwise they lose and the game
	 * continues without them.
	 *
	 * @return True if the player's accusation was correct
	 */
	public boolean accusation() {
		if (this.currentPlayer == null)
			throw new IllegalArgumentException("Null argument received.");

		boolean gameWon = false;

		this.gui.accusationSelection("Make your accusation from the choices below.");		//requests choices from player
		awaitResponse("event");

		if (!this.charSuggestionMessage.equals(this.murderer.toString())
				|| !this.roomSuggestionMessage.equals(this.murderRoom.toString())
				|| !this.weaponSuggestionMessage.equals(this.murderWeapon.toString())) {

			this.players.remove(this.currentPlayer); // removes the current player from the game.


			System.out.printf("Player %s has guessed incorrectly, and is out of the game!\n", this.currentPlayer.PLAYER_NAME);
			awaitResponse("event");
			// displays results to player
			this.gui.basicAlert("Accusation Results!",
					"",
					this.currentPlayer.PLAYER_NAME +" has guessed:",
					"",
					"It was",
					this.charSuggestionMessage,
					"",
					"in the",
					this.roomSuggestionMessage,
					"",
					"with the ",
					this.weaponSuggestionMessage + ".",
					"","","",
					this.currentPlayer.PLAYER_NAME,
					"You have guessed incorrectly",
					"and are out of the game.");	//display choices and result in jdialog
			gameWon = false;
			this.currentPlayer = null;				//sets the current player to null, to remove all reference to this losing player
		} else{
			// removes all players except for the current player from the game.
			for(Player other : this.players){
				if(!other.equals(this.currentPlayer))
					this.players.remove(this.currentPlayer);
			}
			//display choices and result in jdialog
			this.gui.basicAlert("Accusation Results!",
					"",
					this.currentPlayer.PLAYER_NAME +" has guessed:",
					"",
					"It was",
					this.charSuggestionMessage,
					"",
					"in the",
					this.roomSuggestionMessage,
					"",
					"with the ",
					this.weaponSuggestionMessage + ".",
					"","","",
					this.currentPlayer.PLAYER_NAME,
					"You have guessed correctly!",
					"Congratulations! You have won the game!");	//display choices and result in jdialog
			gameWon = true;
		}
		//resets variables for later date.
		this.charSuggestionMessage = null;
		this.weaponSuggestionMessage = null;
		this.roomSuggestionMessage = null;
		this.eventMessage = null;
		return gameWon;
	}

	/**
	 * Pauses gameplay while waiting for a player response.
	 */
	public void awaitResponse(String type) {
		if(type.equals("event")){
			while (this.eventMessage == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(buttonRegistered)
					return;
			}
		}
		else if(type.equals("movementOrAccusation")){
			while(this.keyMessage == 'p' && this.eventMessage == null) {
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
		List<Card> allCards = new ArrayList<>(); // a list for all cards to distribute
		// Copies lists of cards.
		List<Card> chars = new ArrayList<>();
		chars.addAll(this.board.getCharacterCards());
		List<Card> rooms = new ArrayList<>();
		rooms.addAll(this.board.getRoomCards());
		List<Card> weapons = new ArrayList<>();
		weapons.addAll(this.board.getWeaponCards());
		// Chooses character, weapon, and room for murderer, murder weapon and  murder room.
		// Remaining cards are added to allCards in method.
		this.murderer = assignMurderCard(chars, allCards);
		this.murderRoom = assignMurderCard(rooms, allCards);
		this.murderWeapon = assignMurderCard(weapons, allCards);
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

				System.out.println(master.get(mIndex));

				hands[index][handIndex] = master.get(mIndex); // adds a random card to hand i.
				master.remove(mIndex); // prevents the same card being dealt twice
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
	 * @param A list of cards to select from
	 * @param A master list of cards
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
		// Has there been a turn end without the last player losing the game?
		if(currentPlayer != null){
			// Check if the next player is the beginning of the list of players:
			if (currentPlayerIndex + 1 >= players.size()) {
				currentPlayerIndex = 0;
				currentPlayer = players.get(currentPlayerIndex);
			} else { // Otherwise increment the index and find the next player with
				// it
				currentPlayer = players.get(++currentPlayerIndex);
			}
		}
		else{
			//The last player lost the game
			if(this.players.get(currentPlayerIndex)!= null)
				this.currentPlayer = this.players.get(currentPlayerIndex);
			else
				this.currentPlayer = this.players.get(0);
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
	 * /** Displays a message over the selected square.
	 * @param The position of mouse click
	 * @param The y position of the mouse click.
	 */
	public void doToolTip(int x, int y) {
		//TODO
	}

}
