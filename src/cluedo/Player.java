package cluedo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cluedo.Card.CHARACTER;
import cluedo.Card.ROOM;
import cluedo.Card.WEAPON;
import cluedo.locations.Board;
import cluedo.pieces.Character;

/**
 * Stores all player data, including the cards they know about, their character, and their player number.
 * @author Daniel Anastasi
 *
 */
public class Player {

	private Character character = null;
	private Card[] hand = null;
	private List<Card.ROOM> knownRooms;
	private List<Card.CHARACTER> knownCharacters;
	private List<Card.WEAPON> knownWeapons;
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
			choice = c - 'a';		//the index of the player's choice in the list.
			if(choice < freeCharacters.size()){
				this.character = freeCharacters.get(choice);	//Sets the character
				freeCharacters.remove(choice);					//Removes the character from the list, so that it cannot be chosen again.
			}
			else
				System.out.println("Sorry, your choice is not valid. Please try again.");

		}
		if(this.character != null)
			System.out.printf("Player %d chose %s\n", PLAYER_NUM, this.character.NAME);

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
	 */
	public boolean move(char direction, Board board){
		// Get current x,y of the players Character:
		int newX = character.getX();
		int newY = character.getY();
		
		switch(direction){
		case 'N':
		case 'n':
			++newX;
			break;
		case 'E':
		case 'e':
			++newY;
			break;
		case 'S':
		case 's':
			--newX;
			break;
		case 'W':
		case 'w':
			--newY;
			break;
		}
		// False if the square is non-traversable:
		if(board.getBoard()[newX][newY] == 0){
			return false;
		}
		return true;
	}
}
