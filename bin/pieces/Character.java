package cluedo.pieces;
import cluedo.locations.Room;

/**
 * A character piece on the board.
 * @author Daniel Anastasi
 *
 */
public class Character implements Piece{
	/** X and Y coordinates of this piece. **/
	private int x, y;
	/** Name of the character. **/
	public final String NAME;
	/** The room which this character is in, if any.**/
	private Room room = null;
	/** An abbreviation of the name of this character. **/
	public final String ABBREV;

	/**
	 * @param X coordinate
	 * @param Y coordinate
	 * @param Name of the piece
	 * @param Abbreviation of the piece's name.
	 */
	public Character(int x, int y, String name, String abbreviation){
		if(x < 0 || x > 24 ) throw new IllegalArgumentException("X position out of bounds");
		if(y < 0 || y > 24 ) throw new IllegalArgumentException("Y position out of bounds");
		if(name == null)throw new IllegalArgumentException();
		if(abbreviation == null)throw new IllegalArgumentException();
		this.x = x;
		this.y = y;
		this.NAME = name;
		ABBREV = abbreviation;
	}

	/**
	 * Returns the room which the character is in. This can be null.
	 * @return The room in which the player is standing.
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Returns the x coordinate that this piece is at.
	 * @return X coordinate as an integer.
	 */
	public int getX(){
		return this.x;
	}

	/**
	 * Returns the y coordinate that this piece is at.
	 * @return Y coordinate as an integer.
	 */
	public int getY(){
		return this.y;
	}

	/**
	 * Sets new integer values for the x and y coordinates.
	 * @param New x coordinate.
	 * @param New y coordinate.
	 */
	public void setPosition(int x, int y){
		if(x < 0 || x > 24 ) throw new IllegalArgumentException("X position out of bounds");
		if(y < 0 || y > 24 ) throw new IllegalArgumentException("Y position out of bounds");
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the character's room to the room provided.
	 * @param room
	 */
	public void setRoom(Room room){
		this.room = room;
	}

}
