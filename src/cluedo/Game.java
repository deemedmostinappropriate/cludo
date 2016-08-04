package cluedo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import cluedo.Card.CHARACTER;
import cluedo.Card.ROOM;
import cluedo.Card.WEAPON;
import cluedo.locations.Board;
import cluedo.locations.Door;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

//package assignment1.cluedo;

public class Game {
	/** Scanner for use in any input scanning, including use by other objects. */
	public static Scanner scan;

	/** The amount of players in the current game. */
	private int numPlayers;

	/** The board to be interacted with. */
	private Board board;

	/** A reference to the player whose turn it is. */
	private Player currentPlayer = null;

	/** Holds references to all players currently in a game. */
	private static List<Player> players;

	/** For a the reult from a player's roll of the dice.**/
	private int diceroll = 0;

	/** The index of the current player in the list of players. **/
	private int currentPlayerIndex = 0;

	/* The game is over when all of these are correctly guessed by the player: */
	private Card.CHARACTER murderer;
	private Card.ROOM murderRoom;
	private Card.WEAPON murderWeapon;

	public Game(boolean run){
		int numPlayers = 0, rand = 0;
		scan = new Scanner(System.in);
		System.out.println("Welcome to Cluedo");
		System.out.println("How many people are playing? (enter a number between 3 and 6):");
		// Makes sure the number of players is in the range of 3-6.
		while(numPlayers < 3 || numPlayers > 6){
			numPlayers = scan.nextInt();
			if(numPlayers < 3 || numPlayers > 6){
				System.out.println("Please enter a number between 3 and 6:");
			}
		}
		this.numPlayers = numPlayers;
		this.board = new Board();//Set up board
		this.players = new ArrayList<Player>();

		List<Character> freeCharacters = new ArrayList<Character>();
		freeCharacters.addAll(this.board.getCharacters());		//adds all characters to the list.

		// Set up players
		for(int i = 0; i < numPlayers; i++)
			players.add(new Player(i+1, freeCharacters, scan));	//The player chooses which character to use from the list.
		int startingPlayer = assignCards();	//Assigns all cards in the game.

		List<Weapon> weaponPieces = new ArrayList<>(Arrays.asList(Weapon.values()));
		// Distribute weapons between rooms
		for(Room r : this.board.getRooms()){
			if(weaponPieces.isEmpty())
				break;
			rand = (int)(Math.random()*weaponPieces.size());	//index of the weapon
			this.getBoard().changeWeaponRoom(weaponPieces.get(rand), r); //adds a weapon to the room
			weaponPieces.remove(rand);						//removes the weapon from the list of weapon pieces.
		}
		if(run)	run(startingPlayer);		//to turn debug on and off
		scan.close();				// closes the scanner after running the game.
	}

	/**
	 * Returns the game board
	 * @return
	 */
	public Board getBoard(){
		return this.board; 
	}

	/**
	 * Returns the list of players.
	 * @return The list of players
	 */
	public List<Player> getPlayers() {
		return this.players;
	}


	/**
	 * Returns the player whose turn it is.
	 * @return The Player object.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Returns the character card from the solution.
	 * @return The character card.
	 */
	public Card.CHARACTER getMurderer(){
		return murderer;
	}

	/**
	 * Returns the room card from the solution.
	 * @return The room card.
	 */
	public Card.ROOM getMurderRoom() {
		return murderRoom;
	}

	/**
	 * Returns the weapon card from the solution.
	 * @return The weapon card.
	 */
	public Card.WEAPON getMurderWeapon() {
		return murderWeapon;
	}

	/**
	 * Runs the game loop.
	 * @param The index of the starting player in the list of players.
	 */
	public void run(int startingPlayer){
		currentPlayer = players.get(startingPlayer);

		//The game loop.
		while(players.size() > 1){
			boolean roomEntered = false;		// Don't ask about leaving room if they have just entered:
			this.diceroll = diceRoll();		// Roll dice and display result
			this.board.drawBoard(); //draws the board
			System.out.println("Player " +  currentPlayer.PLAYER_NUM + "'s turn ("+ currentPlayer.getCharacter().ABBREV +"): ");
			currentPlayer.printKnownCards();//print out list of cards player knows about

			// Display and process move options if on a traversable board square:
			if(currentPlayer.characterLocation() == null){
				System.out.printf("Your Dice Roll is:  %d\n ", diceroll);
				System.out.println("Would you like to move or make an accusation?Choose (1)move, or (0)accusation");
				System.out.println("Caution: an accusation may end the game!");
				int choice = 99;
				do{
					choice = scan.nextInt();
				}while(choice != 0 && choice != 1);
				if(choice == 1){
					roomEntered = doMovement();	
					System.out.println("Would you like to move or make an accusation?Choose (1)no, or (0)yes");
					choice = 99;
					do{
						choice = scan.nextInt();
					}while(choice != 0 && choice != 1);
					if(choice == 0){
						accusation(currentPlayer);
						break;
					}
				}
				else{
					accusation(currentPlayer);
					break;
				}



			}


			// Check if in a room, show leave options or 
			if(currentPlayer.characterLocation() != null){
				Room currentRoom = currentPlayer.characterLocation();
				if(doRoomTurn(currentRoom, roomEntered))
					break;
				else
					doMovement();	
			}
			updateCurrentPlayer();

			// Prevents players from viewing eachother's hands
			String securityGap = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
			System.out.printf("\nDon't look at another player's hand!!!%s", securityGap);
		}

		// Display the winner and close game elements
		System.out.printf("Congratulations Player %s, you have won the game!\n", this.players.get(0).PLAYER_NUM);
	}

	/**
	 * Processes basic movement interactions via the console.
	 * @param The player's dice roll
	 * @return True if the player's character is in a room.
	 */
	private boolean doMovement(){
		char dir;			// Players direction choice goes here (w a s d).

		System.out.printf("Your Dice Roll is:  %d\n ", diceroll);
		System.out.println("Use W A S D to move, press enter to apply: ");

		while(this.diceroll > 0){
			dir = scan.next().charAt(0);
			Character character = currentPlayer.getCharacter();		//the character piece being moved.

			// Move this players character based on the input char:
			try {

				if(currentPlayer.move(dir, board)){
					-- this.diceroll;			// Take away from remaining moves:
					// Print success and the new location:
					System.out.printf("%s  is now at (%d, %d) on the board.\n", character.NAME, character.getX(), character.getY());
				} else{
					// If the place player's character is moving to is not traversable:
					this.board.drawBoard();
					System.out.println("There is no square to move to in that direction, please try again.");
					continue;		// Start loop again to receive new input.
				}
			}catch (IOException e) {
				// Catch an exception if the input char from player is not w a s d
				System.out.println(e.getMessage() + "Please use W A S D.");
				continue;		// Start loop again to receive new input.
			}

			if(this.diceroll != 0 && currentPlayer.characterLocation() == null){
				this.board.drawBoard(); //Draws the map with the character in a room.
				currentPlayer.printKnownCards();
				System.out.println("    Moves remaining: " + this.diceroll);		// Show moves remaining, only after a successful move:

			}
			else if(currentPlayer.characterLocation() != null){		// If the move resulted in player entering a room:
				this.board.drawBoard(); //Draws the map with the character in a room.
				currentPlayer.printKnownCards();
				return true;
			}
			else{ // Notify the player they have completed their turn if they did not reach a room:
				this.board.drawBoard(); //Draws the map with the character in a room.
				System.out.println("Move Turn Complete for Player " + currentPlayer.PLAYER_NUM);
			}
		}
		return false;
	}


	/**
	 * Processes a turn which starts in a room.
	 * @param The current room of the character piece.
	 * @param True if the player entered the room during this turn.
	 * @return True if the game is over.
	 */
	private boolean doRoomTurn(Room currentRoom, boolean roomEntered){
		Character character = currentPlayer.getCharacter();
		// Only ask the player if they want to leave when they haven't entered in the same turn:
		if(!roomEntered){
			Door exit = null;			// Pull coordinates from the door player is leaving from.

			System.out.println("Do you want to leave the current room? (" + currentRoom.NAME + ") y/n: ");
			char input = scan.next().charAt(0);
			if(input != 'y' && input != 'Y'){
				return false;
			}
			// Print the choices of door when there's more than one, and the staircase's room:
			System.out.println("Type the number of the door or staircase you want to leave from: ");
			int i, r = 999;
			while(exit == null){
				if(currentRoom.getDoors().size() == 1 && currentRoom.getStairs() == null)	
					exit = 	currentRoom.getDoors().get(0);	//only one exit available.
				for(i = 0; i< currentRoom.getDoors().size(); ++i)
					System.out.printf("%d: %s\t", i, reverseDir(currentRoom.getDoors().get(i).ROOM_DIRECTION));

				if(currentRoom.getStairs() != null)
					System.out.printf("%d: Stairs to %s\n", i, currentRoom.getStairs().NAME);
				System.out.println();	
				// Catch an integer from players input:
				try{
					r = scan.nextInt();

					if(r != currentRoom.getDoors().size())		//if a door is selected, and definitely not stairs
						exit = currentRoom.getDoors().get(r);
					else
						break;
				}catch(Exception e){
					System.out.println("Input Error: Please pick a number from the list of doors:");
					continue;
				}
			}

			if(exit != null){
				board.changeCharacterRoom(character, null);
				character.setPosition(exit.getX(), exit.getY());		// Move the players character to the coordinates of the chosen door:
			}
			else{
				board.changeCharacterRoom(character, currentRoom.getStairs());	//Moves the character to the room at the other end of the stairs.
				this.diceroll = 0;
				return doRoomEntry();		//asks usual room entry questions.
			}
			this.board.drawBoard();
			board.changeCharacterRoom(character, null);	// Set players room to null
			--this.diceroll;
			return false;		// continue with turn from the beginning
		} 
		// If just entered into the room, give player options for suggestion, accusation
		// if room has them. All make diceroll 0;
		else if(roomEntered && doRoomEntry())
			return true;
		return false;
	}

	/**
	 * Processes the part of the turn where a player enters a room.
	 * @return Returns true if the game is over
	 */
	private boolean doRoomEntry(){
		boolean gameOver = false;
		String choice = "";
		try{	
			do{
				System.out.println("Would you like to make a (s)uggestion or an (a)ccusation?");
				System.out.println("Caution: an accusation may end the game!");
				choice = scan.next();		//takes player's choice
			} while(!choice.equals("a") && !choice.equals("s"));
		}catch(Exception e){throw new Error(e);}

		if(choice.equals("s"))
			suggestion(currentPlayer);		//processes player suggestion
		else
			gameOver = accusation(currentPlayer); //calls accusation method

		return gameOver || players.size() == 1;			// Game is over because the accusation was correct, or theres only one player left,
	}

	/**
	 * A suggestion made by the player to learn more about the murder.
	 * @param The player who made the suggestion
	 */
	public void suggestion(Player p)throws IllegalArgumentException{
		if(p == null)
			throw new IllegalArgumentException("Null argument received.");
		if(!p.equals(currentPlayer))
			throw new RuntimeException("Only the current player can make suggestions.");
		
		//give player options for suggestion, accusation  
		int characterChoice = 0, weaponChoice = 0;
		Room location = p.characterLocation();		// Suggest from the current room only:
		int roomChoice = Card.ROOM.valueOf(location.NAME).ordinal();

		try{
			do{
				System.out.printf("Which character do you wish to suggest was the murderer?\n");
				//gives choices for character
				for(int c = 0; c < Card.CHARACTER.values().length; c++ )
					System.out.printf("  (%d) %s\n", c,Card.CHARACTER.values()[c]);
				characterChoice = scan.nextInt(); 	//takes user choice

				System.out.printf("Which weapon do you suggest was the murder weapon?\n");
				//gives choices for weapon
				for(int c = 0; c < Card.WEAPON.values().length; c++ )
					System.out.printf("  (%d) %s\n", c,Card.WEAPON.values()[c]);
				weaponChoice = scan.nextInt(); 	//takes user choice

				if(characterChoice < 0 || characterChoice >=  Card.CHARACTER.values().length)
					System.out.println("Character could not be found, please try again.");
				else if(weaponChoice < 0 || characterChoice >=  Card.WEAPON.values().length)
					System.out.println("Weapon could not be found, please try again.");
				else
					break;
			}while(true);

			System.out.printf("You suggest it was %s in the %s with the %s!\n",
					Card.CHARACTER.values()[characterChoice],
					Card.ROOM.values()[roomChoice],
					Card.WEAPON.values()[weaponChoice]);
		}catch(Exception e){throw new Error(e);}



		Character character = this.board.getCharacters().get(characterChoice);
		Room room = this.board.getRooms().get(roomChoice);
		Weapon weapon = Weapon.values()[weaponChoice];

		board.changeCharacterRoom(character, room);
		board.changeWeaponRoom(weapon, room);

		this.board.drawBoard(); //draws the map

		Card roomCard = Card.ROOM.values()[roomChoice];
		Card characterCard = Card.CHARACTER.values()[characterChoice];
		Card weaponCard = Card.WEAPON.values()[weaponChoice];

		p.learn(roomCard);					//Adds the Room to the player's set of known Rooms
		p.learn(characterCard);			//Adds the Character to the player's set of known Characters
		p.learn(weaponCard);				//Adds the Weapon to the player's set of known Weapons

		//Check for a player to refute this suggestion.
		for(int i = 0; i < this.players.size(); i++){
			Player otherPlayer = this.players.get(i);
			if(otherPlayer.equals(p))
				for(Card c: otherPlayer.getHand()){
					if(c == roomCard || c == characterCard || c == weaponCard){
						System.out.printf("Player %d refutes this suggestion!", i);
						return;
					}
				}
		}
		System.out.printf("No player refuted Player %d's suggestion.", currentPlayer.PLAYER_NUM);
		pauseForResponse();
	}

	/**
	 * Called when a player chooses to make an accusation about the murder.
	 * If they guess correctly they win the game, otherwise they lose and the game continues without them.
	 * @param The player making the accusation
	 * @return A string message telling the player the result of their accusation.
	 */
	public boolean accusation(Player p){
		if(p == null)
			throw new IllegalArgumentException("Null argument received.");
		if(!p.equals(currentPlayer))
			throw new RuntimeException("Only the current player can make suggestions.");
		
		int characterChoice = 0, roomChoice = 0, weaponChoice = 0;

		try{	
			do{
				System.out.printf("Which character do you want to accuse of murder?\n");
				//gives choices for character
				for(int c = 0; c < Card.CHARACTER.values().length; c++ )
					System.out.printf("  (%d) %s\n", c,Card.CHARACTER.values()[c]);
				characterChoice = scan.nextInt(); 	//takes user choice

				// Ask from room only if player is making an accusation
				System.out.println("Which room do you say that the murder took place?");
				for(int c = 0; c < Card.ROOM.values().length; c++ )
					System.out.printf("  (%d) %s\n", c,Card.ROOM.values()[c]);
				roomChoice = scan.nextInt();

				System.out.printf("Which weapon do you accuse the muderer of using?\n");
				//gives choices for weapon
				for(int c = 0; c < Card.WEAPON.values().length; c++ )
					System.out.printf("  (%d) %s\n", c,Card.WEAPON.values()[c]);
				weaponChoice = scan.nextInt(); 	//takes user choice

				if(characterChoice < 0 || characterChoice >=  Card.CHARACTER.values().length)
					System.out.println("Character could not be found, please try again.");
				else if(weaponChoice < 0 || characterChoice >=  Card.WEAPON.values().length)
					System.out.println("Weapon could not be found, please try again.");
				else
					break;
			}while(true);

		}catch(Exception e){throw new Error(e);}
		System.out.printf("Player %d states: It was %s in the %s with the %s!\n",
				currentPlayer.PLAYER_NUM,
				Card.CHARACTER.values()[characterChoice],
				Card.ROOM.values()[roomChoice],
				Card.WEAPON.values()[weaponChoice]);

		Card.CHARACTER character = Card.CHARACTER.values()[characterChoice];
		Card.ROOM room = Card.ROOM.values()[roomChoice];
		Card.WEAPON weapon = Card.WEAPON.values()[weaponChoice];

		if(character != this.murderer 
				|| room != this.murderRoom
				|| weapon != this.murderWeapon){
			this.players.remove(p);	//removes the current player from the game.
			System.out.printf("Player %d has guessed incorrectly, and is out of the game!\n", p.PLAYER_NUM);
			pauseForResponse();
			return false;
		}
		else return true;
	}

	/**
	 * Pauses gameplay while waiting for a player response.
	 */
	public void pauseForResponse(){
		try{
			System.out.println("Press c key to continue.");
			while(scan.next().charAt(0) != 'c'){
				System.out.println("Press c key  to continue.");
			}
		}catch(Exception e){throw new Error(e);}
	}

	/**
	 * Assigns the solution cards, and assigns all other cards to player's hands.
	 * @return The index of the player to start the game.
	 */
	private int assignCards(){
		List<Card> allCards = new ArrayList<Card>();	// a list for all cards to distribute
		List<Card> characters =  new ArrayList<>(Arrays.asList((Card.CHARACTER.values()))); 	// a list for all character cards to distribute.
		List<Card> weapons =  new ArrayList<>(Arrays.asList((Card.WEAPON.values())));	// a list for all weapon cards to distribute.
		List<Card> rooms =  new ArrayList<>(Arrays.asList((Card.ROOM.values())));	// a list for all room cards to distribute.

		//Chooses character, weapon, and room for murderer, murder weapon and murder room.
		// Remaining cards are added to  allCards in method.
		this.murderer = (Card.CHARACTER) assignMurderCard(characters, allCards);
		this.murderRoom = (Card.ROOM) assignMurderCard(rooms, allCards);
		this.murderWeapon = (Card.WEAPON) assignMurderCard(weapons, allCards);
		if(murderer == null || murderRoom == null|| murderWeapon == null)
			throw new Error("Card could not be assigned for solution");
		Card[][] hands = new Card[this.numPlayers][18 / this.numPlayers +1];	//player hands

		int startingPlayer = fillHands(hands, allCards);
		// Passes the hands to their respective players.
		for(int i = 0; i < this.numPlayers; i++){
			this.players.get(i).setHand(hands[i]);
		}
		return startingPlayer;
	}

	/**
	 * Distributes cards from the master list across all hands.
	 * @param 2D array of Cards, the hands to fill.
	 * @param The list of cards to distribute.
	 * @return Index of the player to start the game.
	 */
	private int fillHands(Card[][] hands, List<Card> master){
		int handIndex = 0, index = 0, mIndex = 0;
		//Iterates over each hand, dealing a card until all cards are dealt
		while(master.size() > 0){

			for(index = 0; index < hands.length; index++){
				mIndex = (int)(Math.random()*master.size());
				hands[index][handIndex] = master.get(mIndex);	//adds a random card to hand i.
				master.remove(mIndex);						// prevents the same card being dealt twice
			}
			handIndex++;
		}
		// Finds the starting player based on who was last dealt to.
		return (index >= hands.length - 1 ? 0 : index +1);
	}

	/**
	 * Chooses a card at random from the selection list. 
	 * All other cards in the list are added to the master list.
	 * @param <T>
	 * @param A list of cards to select from
	 * @param A master list of cards
	 * @return The chosen card
	 */
	private Card assignMurderCard(List<Card> selection, List<Card> master){
		Card chosen = null;
		int index = 0;
		index = (int)(Math.random() * selection.size());
		chosen = selection.get(index);			// The chosen card
		selection.remove(index);				// Removes the chosen  card from the list.
		master.addAll(selection); 				// Puts the cards in the master list
		return chosen;
	}

	/**
	 * Returns a random number between 1 and 6 for use as a dice roll.
	 * @return
	 */
	private static int diceRoll(){
		return (int) (Math.random() * 6 + 1);
	}

	/**
	 * Returns a String of the direction corresponding to the reverse of the given 
	 * direction string. Used with the direction held in Door objects.
	 * @param dir
	 * @return
	 */
	private static String reverseDir(String dir){
		switch(dir){
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
	public void updateCurrentPlayer(){
		// Check if the next player is the beginning of the list of players:
		if(currentPlayerIndex + 1 == players.size()){
			currentPlayerIndex = 0;
			currentPlayer = players.get(currentPlayerIndex);
		} else{ // Otherwise increment the index and find the next player with it
			currentPlayer = players.get(++currentPlayerIndex);
		}
	}

	public static void main(String[] args){
		new Game(true);
	}




}

