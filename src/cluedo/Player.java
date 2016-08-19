package cluedo;
import java.io.IOException;

import cluedo.cards.Card;
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

	/** The player's name. */
	public final String PLAYER_NAME;

	/**
	 * The Player constructor.
	 * @param The number associated with the player
	 * @param The player's character
	 */
	public Player(String name, Character character){
		PLAYER_NAME = name;
		this.character = character;
	}

	/**
	 * Returns the Room location of this players character, or null if
	 * not in a Room currently.
	 * @return The room which the character is in. Can be null.
	 */
	public Room getCharacterLocation(){
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


	/**
	 * Gives the player their hand of cards.
	 * @param The hand, an array of Cards
	 */
	public void setHand(Card[] hand){
		if(this.hand != null)
			return;
		if(hand == null)
			throw new IllegalArgumentException("Null hand passed in");
		this.hand = hand;
	}

	/**
	 * Moves the player's piece on the board.
	 * @param New x coordinate
	 * @param New y coordinate
	 * @param The game board.
	 * @return Whether or not the player is able to make the move.
	 * @throws IOException
	 */
	public boolean move(char direction, Board board){
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
			return false;
		}

		// If the player is trying to go into a room while at a door, set
		// the character to be in that room and return successful move.
		if(board.getDoor(character.getX(), character.getY()) != null){
			Door d = board.getDoor(character.getX(), character.getY());

			// Complete move into room if players input matches the entrance
			// direction of the door:
			if(d.ROOM_DIRECTION.equals(dir)){
				board.changeCharacterRoom(this.getCharacter(), d.getRoom());
				return true;
			}
		}

		if(board.blockedByCharacter(newX, newY))
			return false;

		// False if the square is non-traversable:
		if(!board.inRange(newX, newY)
				|| traversable[newY][newX] != 1){
			return false;
		}

		// Otherwise, players character is allowed to move:
		character.setX(newX);
		character.setY(newY);
		return true;
	}

}
