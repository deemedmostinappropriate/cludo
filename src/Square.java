/**
 * A square on the cluedo board.
 * Records where there are walls adjacent to itself.
 * @author Daniel Anastasi
 *
 */
public class Square {
	
	private boolean vacant;
	public final boolean WALL_ABOVE, WALL_RIGHT, WALL_BELOW, WALL_LEFT; 
	
	public Square(boolean top, boolean right, boolean bottom, boolean left){
		this.vacant = true;
		WALL_ABOVE = top;
		WALL_RIGHT = right;
		WALL_BELOW = bottom;
		WALL_LEFT = left;
	}
	
	/**
	 * Returns whether the square is vacant or occupied by a player
	 * @return True if the square is vacant, false if occupied.
	 */
	public boolean getVacant(){
		return this.vacant;
	}
	
	/**
	 * Sets whether the square is vacant or not.
	 * @param The proposed vacancy of the square.
	 */
	public void setVacant(boolean vacancy){
		this.vacant = vacancy;
	}
}
