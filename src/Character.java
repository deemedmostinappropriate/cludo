/**
 * A character piece on the board.
 * @author Daniel Anastasi
 *
 */
public class Character {

	private int x, y;
	public final String NAME;
	
	public Character(int x, int y, String name){
		this.x = x;
		this.y = y;
		this.NAME = name;
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
	
}
