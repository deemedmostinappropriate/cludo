package cluedo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cluedo.locations.Board;
import cluedo.locations.Door;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

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

	/** Room cards which the player has seen. **/
	private List<Card.ROOM> knownRooms;

	/** Character cards which the player has seen. **/
	private List<Card.CHARACTER> knownCharacters;

	/** Weapon cards which the player has seen. **/
	private List<Card.WEAPON> knownWeapons;

	/** The player's name. */
	public final String PLAYER_NAME;

	/**
	 * The Player constructor.
	 * @param The number associated with the player
	 * @param The player's character
	 */
	public Player(String name, Character character){
		PLAYER_NAME = name;
		this.knownRooms = new ArrayList<>();
		this.knownCharacters = new ArrayList<>();
		this.knownWeapons = new ArrayList<>();
		this.character = character;
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
		for(Card card: hand){
			if(card != null)
				learn(card);	//adds the card to the list of the player's known cards.
		}
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
		if(board == null)
			throw new IllegalArgumentException("Argument is null.");
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
		// False if the square is non-traversable:
		if(!board.inRange(newX, newY) || traversable[newY][newX] == 0){
			return false;
		}

		// Otherwise, players character is allowed to move:
		character.setPosition(newX, newY);
		return true;
	}


	/**
	 * Prints the cards that are known by the player.
	 */
	public void printKnownCards(){
		System.out.printf("Character cards you have seen:\t");
		for(Card c : this.knownCharacters)
			System.out.printf("%s\t",c.toString());
		System.out.println();
		System.out.printf("Room cards you have seen:\t");
		for(Card c : this.knownRooms)
			System.out.printf("%s\t",c.toString());
		System.out.println();
		System.out.printf("Weapon cards you have seen:\t");
		for(Card c : this.knownWeapons)
			System.out.printf("%s\t", c.toString());
		System.out.printf("\n\n");
	}
	

}
