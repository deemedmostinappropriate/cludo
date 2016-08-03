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
	private static Board board;

	/** A reference to the player whose turn it is. */
	private static Player currentPlayer = null;

	/** Holds references to all players currently in a game. */
	private static List<Player> players;

	/* The game is over when all of these are correctly guessed by the player: */
	private Card.CHARACTER murderer;
	private Card.ROOM murderRoom;
	private Card.WEAPON murderWeapon;

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

		List<Character> freeCharacters = new ArrayList<Character>();
		freeCharacters.addAll(this.board.getCharacters());		//adds all characters to the list.

		// Set up players
		for(int i = 0; i < numPlayers; i++)
			players.add(new Player(i+1, freeCharacters, scan));	//The player chooses which character to use from the list.
		int startingPlayer = assignCards();	//Assigns all cards in the game.

		
		List<Weapon> weaponPieces =  new ArrayList<>(Arrays.asList(Weapon.values()));
		// Distribute weapons between rooms
		for(Room r : this.board.getRooms()){
			if(weaponPieces.isEmpty())
				break;
			rand = (int)(Math.random()*weaponPieces.size());	//index of the weapon
			this.changeWeaponRoom(weaponPieces.get(rand), r); //adds a weapon to the room
			weaponPieces.remove(rand);						//removes the weapon from the list of weapon pieces.
		}

		this.board.drawBoard(); //draws the map
		run();
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
	public static Player getCurrentPlayer() {
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
	 */
	public void run(){
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

		// Scan for input while the game is running:
		Scanner s = new Scanner(System.in);

		playerturn: while(players.size() > 1){
			// Roll dice and display result
			diceroll = diceRoll();
			// Don't ask about leaving room if they have just entered:
			boolean roomEntered = false;

			System.out.println("Player " +  currentPlayer.PLAYER_NUM + "'s turn ("+ currentPlayer.getCharacter().ABBREV +"): ");

			// Display and process move options if on a traversable board square:
			if(currentPlayer.characterLocation() == null){
				System.out.println("    Your Dice Roll is: " + diceroll);

				char dir;			// Players direction choice goes here (w a s d).

				System.out.println("Use W A S D to move, press enter to apply: ");

				moveturn: while(diceroll > 0){
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
							continue moveturn;		// Start loop again to receive new input.
						}
					} catch (IOException e) {
						// Catch an exception if the input char from player is not w a s d
						System.out.println(e.getMessage() + "Please use W A S D.");
						continue moveturn;			// Start loop again to receive new input.
					}
					if(diceroll != 0 && currentPlayer.characterLocation() == null){
						// Show moves remaining, only after a successful move:
						System.out.println("    Moves remaining: " + diceroll);
					}
					if(currentPlayer.characterLocation() != null){
						// If the move resulted in player entering a room:
						// Break out of loop immediately, will automatically go to next conditional.
						roomEntered = true;
						this.board.drawBoard(); //Draws the map with the character in a room.

						break moveturn;
					}
					else{ // Notify the player they have completed their turn if they did not reach a room:
						this.board.drawBoard(); //draws the map.
						System.out.println("Move Turn Complete for Player " + currentPlayer.PLAYER_NUM);
					}
				}				
			}
			// Check if in a room, show leave options or 
			roomturn: if(currentPlayer.characterLocation() != null){
				Room currentRoom = currentPlayer.characterLocation();
				
				// Only ask the player if they want to leave when they haven't entered in the same turn:
				if(!roomEntered){
					System.out.println("Do you want to leave the current room? (" + currentRoom.NAME + ") y/n: ");

					Door exit = null;			// Pull coordinates from the door player is leaving from.
					char input = s.next().charAt(0);

					// Print the choices of door when there's more than one:
					if(currentRoom.getDoors().size() > 1 && (input == 'y' || input == 'Y')){
						
						System.out.println("Type the number of the door you want to leave from: ");
						
						while(exit == null){
							for(int i = 0; i< currentRoom.getDoors().size(); ++i){
								System.out.print(i + ": " + reverseDir(currentRoom.getDoors().get(i).ROOM_DIRECTION + "    "));
								if(i == currentRoom.getDoors().size() - 1) System.out.print('\n');
							}
							int r;
							// Catch an integer from players input:
							try{
								r = s.nextInt();
								exit = currentRoom.getDoors().get(r);
							} catch(Exception e){
								System.out.println("Input Error: Please pick a number from the list of doors:");
								continue;
							}
						}
						// Move the players character to the coordinates of the chosen door:
						currentPlayer.getCharacter().setPosition(exit.getX(), exit.getY());
						this.board.drawBoard();
					}
					
					else if(currentRoom.getDoors().size() == 1 && (input == 'y' || input == 'Y')){
						// Get the only door in the room, player choice not needed:
						exit = currentRoom.getDoors().get(0);
						// Move the players character to the coordinates of the chosen door:
						currentPlayer.getCharacter().setPosition(exit.getX(), exit.getY());
						
						this.board.drawBoard();
					} else{
						// Break out of the room turn when player chooses no:
						break roomturn;
					}

					// Move to the coordinates of that door on board
					// Set players room to null

					// continue with turn from the beginning
					--diceroll;
					continue playerturn;
				} 
				// If just entered into the room, give player options for suggestion, accusation or stairs
				// if room has them. All make diceroll 0;
				else if(roomEntered){
					break roomturn;
				}
			}
			
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
		s.close();
	}

	/**
	 * A suggestion made by the player to learn more about the murder.
	 * @param The player who made the suggestion
	 * @param An integer representing the character provided by the player.
	 * @param An integer representing the room provided by the player.
	 * @param TAn integer representing the weapon provided by the player.
	 * @return A string detailing why the method was unsuccessful. Is null if there was no issue.
	 */
	public String suggestion(Player p, int characterChoice, int roomChoice, int weaponChoice){
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
		changeWeaponRoom(weapon, room);

		p.learn(Card.ROOM.values()[roomChoice]);					//Adds the Room to the player's set of known Rooms
		p.learn(Card.CHARACTER.values()[characterChoice]);			//Adds the Character to the player's set of known Characters
		p.learn(Card.WEAPON.values()[weaponChoice]);				//Adds the Weapon to the player's set of known Weapons

		this.board.drawBoard(); //draws the map

		return null;
	}

	/**
	 * Moves the weapon piece to the room
	 * @param The weapon
	 * @param The room
	 */
	public void changeWeaponRoom(Weapon w, Room r){
		boolean contains = false;
		Room oldRoom = this.board.getRoomFromWeapon(w);
		if(oldRoom != null)
			oldRoom.removeWeapon(w);	//Removes the weapon from the old room
		this.board.setRoomFromWeapon(w, r);					//Changes mapping of weapon -> room in board.
		r.addWeapon(w);										//Moves the weapon to the new room.
		if(r.getWeapons().isEmpty()){
			w.setX(r.getWeaponPositionsX()[0]);
			w.setY(r.getWeaponPositionsY()[0]);
		}
		else{
			// Finds the first weapon position which is not currently occupied.
			for(int x = 0; x < r.getWeaponPositionsX().length; x++){
				contains = false;
				for(int piece = 0; piece < r.getWeapons().size(); piece++){
					if(r.getWeapons().get(piece).getX() == r.getWeaponPositionsX()[x]){
						contains = true;
						break;
					}
				}
				if(!contains){
					w.setX(r.getWeaponPositionsX()[x]);
					w.setY(r.getWeaponPositionsY()[x]);
					return;
				}
			}
		}

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
	public void changeCharacterRoom(Character c, Room r){
		boolean contains = false;
		if(c.getRoom() != null)
			c.getRoom().removeCharacter(c);		//Removes character from old room
		c.setRoom(r);								//Changes characters record of room.
		r.addCharacter(c);						//Moves character to the new room.

		//gets X to set to
		if(r.getCharacters().isEmpty()){
			c.setX(r.getCharPositionsX()[0]);
			c.setY(r.getCharPositionsY()[0]);
		}
		else{
			// Finds the first character position which is not currently occupied.
			for(int x = 0; x < r.getCharPositionsX().length; x++){
				contains = false;
				for(int piece = 0; piece < r.getCharacters().size(); piece++){
					if(r.getCharacters().get(piece).getX() == r.getCharPositionsX()[x]){
						contains = true;
						break;
					}
				}
				if(!contains){
					c.setX(r.getCharPositionsX()[x]);
					c.setY(r.getCharPositionsY()[x]);
					return;
				}
			}
		}

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
	public boolean accusation(Player p, int characterChoice, int roomChoice, int weaponChoice){
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
		Card[][] hands = new Card[this.numPlayers][18 / this.numPlayers];	//player hands
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
				mIndex = (int)(Math.random()*master.size());
				hands[index][handIndex] = master.get(mIndex);	//adds a random card to hand i.
				master.remove(mIndex);						// prevents the same card being dealt twice
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
		default:
			return "Right";
		}
	}
	public static void main(String[] args){
		new Game();
	}




}

