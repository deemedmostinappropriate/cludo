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
	private static Board board;
	
	/** A reference to the player whose turn it is. */
	private static Player currentPlayer = null;
	
	/** Holds references to all players currently in a game. */
	private static List<Player> players;
	
	/* The game is over when all of these are correctly guessed by the player: */
	private Card.CHARACTER murderer = null;
	private Card.ROOM murderRoom = null;
	private Card.WEAPON murderWeapon = null;

	public Game(){
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

		// Compiles list of free characters
		List<Character> freeCharacters = new ArrayList<Character>();
		freeCharacters.addAll(this.board.getCharacters());	//adds all characters to the list.

		// Set up players
		for(int i = 0; i < numPlayers; i++){
			players.add(new Player(i+1, freeCharacters, scan));	//The player chooses which character to use from the list.
		}
		int startingPlayer = assignCards();	//Assigns all cards in the game.
		
		List<Weapon> weaponPieces = Arrays.asList(Weapon.values());
		// Distribute weapons between rooms
		for(Room r : this.board.getRooms()){
			rand = (int)Math.random()*weaponPieces.size();	//index of the weapon
			r.addWeapon(weaponPieces.get(rand));			//adds a weapon to the room
			weaponPieces.remove(rand);						//removes the weapon from the list of weapon pieces.
		}

		run();
		scan.close();				// closes the scanner after running the game.
	}

	
	/**
	 * Runs the game loop.
	 */
	public static void run(){
		/*
		// Array input test
		int[][] b = this.board.getBoard();
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[0].length; j++){
				System.out.print(b[i][j]);
			}
			System.out.println();
		}
		 */

		int diceroll = 0;
		int currentPlayerIndex = 0;
		
		currentPlayer = players.get(currentPlayerIndex);
		
		while(players.size() > 1){
			diceroll = diceRoll();
			// Player x's turn, display roll
			System.out.println("Player " +  currentPlayer.PLAYER_NUM + "'s turn: ");
			
			// Display option if on square: move options
			if(currentPlayer.characterLocation() == null){
				System.out.println("    Your Dice Roll is: " + diceroll);
				
				char dir;
				Scanner s = new Scanner(System.in);
				
				System.out.println("Use W A S D to move, press enter to apply: ");
				while(diceroll > 0){
					dir = s.next().charAt(0);
					// Move this players character based on the input char:
					try {
						if(currentPlayer.move(dir, board)){
							// Take away from remaining moves:
							--diceroll;
							// Print success and the new location:
							System.out.println("Character " + currentPlayer.getCharacter().NAME
									+ " is now at (" + currentPlayer.getCharacter().getX() + ","
									+ currentPlayer.getCharacter().getY() + ") on the board.");
						} else{
							// If the place player's character is moving to is not traversable:
							System.out.println("There is no square to move to in that direction, please try again.");
							continue;		// Start loop again to receive new input.
						}
					} catch (IOException e) {
						// Catch an exception if the input char from player is not w a s d
						System.out.println(e.getMessage() + "Please use W A S D.");
						continue;			// Start loop again to receive new input.
					}
					if(diceroll != 0){ // Show moves remaining, only after a successful move:
						System.out.println("    Moves remaining: " + diceroll);
					} else{ // Notify the player they have completed their turn if they did not reach a room:
						System.out.println("Move Turn Complete for Player " + currentPlayer.PLAYER_NUM);
					}
				}
				
				s.close();
			}
			// if in room: display doors(coordinates ?), 
			// if there are stairs in room: stairs(room)
			// ------------------------------
			// if not in room: process move request
			// if in room:
				//     door: move to door (subtract from diceroll)
			// 	    Game.drawBoard // map, pieces.
			// 	      character.move
			// 	      Game.changeCharacterRoom(null)
			//      process moves
			// 	  if then in room:
					// 	    ask if they want to make a suggestion or accusation
			// 	      process
			//    stairs: move to room (diceroll to zero)
			//        ask if they want to make a suggestion or accusation
			
			// Check if the next player is the beginning of the list of players:
			if(currentPlayerIndex + 1 == players.size()){
				currentPlayerIndex = 0;
				currentPlayer = players.get(currentPlayerIndex);
			} else{ // Otherwise increment the index and find the next player with it
				currentPlayer = players.get(++currentPlayerIndex);
			}
		}

	}

	/**
	 * Processes the given players input to translate to their character moving around
	 * the board. 
	 * @param player
	 * @param direction
	 * @return boolean of whether the move was successful
	 */
	private boolean processMove(Player player, char direction){
		
		return false;
	}

	/**
	 * A suggestion made by the player to learn more about the murder.
	 * @param The player who made the suggestion
	 * @param An integer representing the character provided by the player.
	 * @param An integer representing the room provided by the player.
	 * @param TAn integer representing the weapon provided by the player.
	 * @return A string detailing why the method was unsuccessful. Is null if there was no issue.
	 */
	private String suggestion(Player p, int characterChoice, int roomChoice, int weaponChoice){
		if(characterChoice < 0 || characterChoice >=  Card.CHARACTER.values().length)
			return "Character could not be found, please try again.";
		if(roomChoice < 0 || characterChoice >=  Card.ROOM.values().length)
			return "Room could not be found, please try again.";
		if(weaponChoice < 0 || characterChoice >=  Card.WEAPON.values().length)
			return "Weapon could not be found, please try again.";
		
		Character character = this.board.getCharacters().get(characterChoice);
		Room room = this.board.getRooms().get(roomChoice);
		Weapon weapon = Weapon.values()[weaponChoice];
		
		changeCharacterRoom(character, room);

		this.board.getRoomFromWeapon(weapon).removeWeapon(weapon);	//Removes the weapon from the old room
		room.addWeapon(weapon);										//Moves the weapon to the new room.
		this.board.setRoomFromWeapon(weapon, room);					//Changes mapping of weapon -> room in board.

		p.learn(Card.ROOM.values()[roomChoice]);					//Adds the Room to the player's set of known Rooms
		p.learn(Card.CHARACTER.values()[characterChoice]);			//Adds the Character to the player's set of known Characters
		p.learn(Card.WEAPON.values()[weaponChoice]);				//Adds the Weapon to the player's set of known Weapons

		return null;
	}

	/**
	 * Changes the Character.room field to the new room.
	 * Removes the character from any room they were in.
	 * Adds the player to the new room.
	 * If the room is null, the player will be removed from their room, and have its field changed.
	 * In this instance it will not be added to a new room.
	 * @param The character.
	 * @param The new room to move the character to.
	 */
	public void changeCharacterRoom(Character character, Room newRoom){
		if(character.getRoom() != null)
			character.getRoom().removeCharacter(character);		//Removes character from old room
		character.setRoom(newRoom);								//Changes characters record of room.
		newRoom.addCharacter(character);						//Moves character to the new room.
	}

	/**
	 * Called when a player chooses to make an accusation about the murder.
	 * If they guess correctly they win the game, otherwise they lose and the game continues without them.
	 * @param The player making the accusation
	 * @param The character accused of the crime.
	 * @param The room where the murder is said to have taken place.
	 * @param The proposed murder weapon.
	 * @return A string message telling the player the result of their accusation.
	 */
	private boolean accusation(Player p, int characterChoice, int roomChoice, int weaponChoice){
		Card.CHARACTER character = Card.CHARACTER.values()[characterChoice];
		Card.ROOM room = Card.ROOM.values()[roomChoice];
		Card.WEAPON weapon = Card.WEAPON.values()[weaponChoice];
		
		if(character != this.murderer 
				|| room != this.murderRoom
				|| weapon != this.murderWeapon){
			this.players.remove(p);	//removes the current player from the game.
			return false;
		}
		else
			return true;
	}

	
	
	/**
	 * Assigns the solution cards, and assigns all other cards to player's hands.
	 * @return The index of the player to start the game.
	 */
	private int assignCards(){
		List<Card> allCards = new ArrayList<Card>();	// a list for all cards to distribute
		List<Card.CHARACTER> characters =  Arrays.asList((Card.CHARACTER.values())); 	// a list for all character cards to distribute.
		List<Card> weapons =  Arrays.asList((Card.WEAPON.values()));	// a list for all weapon cards to distribute.
		List<Card> rooms =  Arrays.asList((Card.ROOM.values()));	// a list for all room cards to distribute.

		//Chooses character, weapon, and room for murderer, murder weapon and murder room.

		this.murderer = (Card.CHARACTER) assignMurderCard(characters, allCards);
		this.murderRoom = (Card.ROOM) assignMurderCard(rooms, allCards);
		this.murderWeapon = (Card.WEAPON) assignMurderCard(weapons, allCards);
		if(murderer == null || murderRoom == null|| murderWeapon == null)
			throw new Error("Card could not be assigned for solution");
		Card[][] hands = new Card[this.numPlayers][this.numPlayers / 18];	//player hands
		int startingPlayer = fillHand(hands, allCards);
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
	private int fillHand(Card[][] hands, List<Card> master){
		int start = 0, handIndex = 0, index = 0, mIndex = 0;
		
		while(master.size() > 0){
			for(index = 0; index < hands.length; index++){
				mIndex = (int)Math.random()*master.size();
				hands[index][handIndex] = master.get(mIndex);	//adds a random card to hand i.
			}
			handIndex++;
		}
		// Finds the starting player based on who was last dealt to.
		if(index == hands.length - 1){
			start = 0;
		}
		else{
			start = index +1;
		}
		return start;
	}
	
	/**
	 * Chooses a card at random from the selection list. 
	 * All other cards in the list are added to the master list.
	 * @param <T>
	 * @param A list of cards to select from
	 * @param A master list of cards
	 * @return The chosen card
	 */
	private Card assignMurderCard(List<? extends Card> selection, List<Card> master){
		Card chosen = null;
		int index = 0;
		index = (int)Math.random() * selection.size();
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
		return (int) (Math.random() * 6);
	}

	public static void main(String[] args){
		new Game();
	}


}

