package cludo.pieces;
import cluedo.locations.Room;

/**
 * A character piece on the board.
 * @author Daniel Anastasi
 *
 */
public class Character implements Piece{

	private int x, y;
	public final String NAME;
	private Room room = null;
	

	public Character(int x, int y, String name){
		this.x = x;
		this.y = y;
		this.NAME = name;
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
