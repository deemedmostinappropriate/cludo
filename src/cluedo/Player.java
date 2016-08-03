package cluedo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cluedo.Card.CHARACTER;
import cluedo.Card.ROOM;
import cluedo.Card.WEAPON;
import cluedo.locations.Board;
import cluedo.locations.Door;
import cluedo.locations.Room;
import cluedo.pieces.Character;

/**
 * Stores all player data, including the cards they know about, their character, and their player number.
 * @author Daniel Anastasi
 *
 */
public class Player {
	/** The character piece associated with this Player */
	private Character character = null;

	/** Represents the cards in hand. Can be assumed to not contain double-ups
	 * across all Player objects. */
	private Card[] hand = null;

	/* These hold the cards known by an individual player to be in other players hands.
	 * May have double-ups with other Player objects but not within them. */
	private List<Card.ROOM> knownRooms;
	private List<Card.CHARACTER> knownCharacters;
	private List<Card.WEAPON> knownWeapons;

	/** The designated turn position  of this player. */
	public final int PLAYER_NUM;

	public Player(int playerNumber, List<Character> freeCharacters, Scanner scan){
		PLAYER_NUM = playerNumber;
		this.knownRooms = new ArrayList<>();
		this.knownCharacters = new ArrayList<>();
		this.knownWeapons = new ArrayList<>();
		int choice = 0;
		String str = null;
		char c = '\0';

		//Requests player to choose character from list of free characters.
		while(this.character == null){
			System.out.printf("Player %d, please choose your character:\n", PLAYER_NUM);
			for(int i = 0; i < freeCharacters.size(); i++){
				System.out.printf("(%c) %s\n",'a'+i, freeCharacters.get(i).NAME); 	//e.g (a)Colonel Mustard
			}
			str = scan.next();
			c = str.charAt(0);	
			if(c >= 97 && c <= 102){


				choice = c - 'a';		//the index of the player's choice in the list.
				if(choice < freeCharacters.size()){
					this.character = freeCharacters.get(choice);	//Sets the character
					freeCharacters.remove(choice);					//Removes the character from the list, so that it cannot be chosen again.
				}
				else
					System.out.println("Sorry, your choice is not valid. Please try again.");
			}
			else
				System.out.println("Sorry, your choice is not valid. Please try again.");
		}
		if(this.character != null)
			System.out.printf("Player %d chose %s\n", PLAYER_NUM, this.character.NAME);

	}


	/**
	 * Returns the list of known room cards
	 * @return The list of room cards
	 */
	public List<Card.ROOM> getKnownRooms() {
		return knownRooms;
	}

	/**
	 * Returns the list of known character cards
	 * @return The list of character cards
	 */
	public List<Card.CHARACTER> getKnownCharacters() {
		return knownCharacters;
	}

	/**
	 * Returns the list of known weapon cards
	 * @return The list of weapon cards
	 */
	public List<Card.WEAPON> getKnownWeapons() {
		return knownWeapons;
	}

	/**
	 * Gives the player their hand of cards.
	 * @param The hand, an array of Cards
	 */
	public void setHand(Card[] hand){
		if(this.hand != null)
			return;
		this.hand = hand;
		for(Card card: hand)
			learn(card);	//adds the card to the list of the player's known cards.
	}

	/**
	 * Adds a new item to the sets of what the player knows regarding the murder.
	 * @param An object that must be of type Room, Character, or Room.WEAPON.
	 */
	public void learn(Card card){
		if(card instanceof Card.ROOM){
			this.knownRooms.add((Card.ROOM)card);
		}
		else if(card instanceof Card.CHARACTER){
			this.knownCharacters.add((Card.CHARACTER)card);
		}
		else if(card instanceof Card.WEAPON){
			this.knownWeapons.add((Card.WEAPON)card);
		}
		else{
			throw new IllegalArgumentException("Argument cannot be learnt by Player: Type incorrect");
		}
	}


	/**
	 * Moves the player's piece on the board.
	 * @param New x coordinate
	 * @param New y coordinate
	 * @param The game board.
	 * @return Whether or not the player is able to make the move.
	 * @throws IOException 
	 */
	public boolean move(char direction, Board board) throws IOException{
		// Get current x,y of the players Character:
		int newX = character.getX();
		int newY = character.getY();

		String dir;
		int[][] traversable = board.getBoard();

		switch(direction){
		case 'W':
		case 'w':
			++newY;		// Reversed for drawing
			dir = "UP";
			break;
		case 'D':
		case 'd':
			++newX;
			dir = "RIGHT";
			break;
		case 'S':
		case 's':
			--newY;		// Reversed for drawing
			dir = "DOWN";
			break;
		case 'A':
		case 'a':
			--newX;
			dir = "LEFT";
			break;
		default:
			throw new IOException("Input was incorrect.");
		}
		
		// If the player is trying to go into a room while at a door, set
		// the character to be in that room and return successful move.
		if(board.getDoor(character.getX(), character.getY()) != null){
			Door d = board.getDoor(character.getX(), character.getY());
			
			// Complete move into room if players input matches the entrance
			// direction of the door:
			if(d.ROOM_DIRECTION.equals(dir)){
				Game.changeCharacterRoom(this.getCharacter(), d.getRoom());
				return true;
			}
		}
		// False if the square is non-traversable:
		if(!board.inRange(newX, newY) || traversable[newY][newX] == 0){
			return false;
		}

		// Otherwise, players character is allowed to move:
		character.setPosition(newX, newY);
		return true;
	}

	/**
	 * Returns the Room location of this players character, or null if
	 * not in a Room currently.
	 * @return
	 */
	public Room characterLocation(){
		return character.getRoom();
	}

	/**
	 * Returns the character object associated with this player.
	 * @return
	 */
	public Character getCharacter(){
		return this.character;
	}

	/**
	 * Returns the player's hand
	 * @return The array of cards which make the hand.
	 */
	public Card[] getHand() {
		return hand;
	}



}
