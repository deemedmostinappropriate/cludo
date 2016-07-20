
public class Player {

	Character character;
	
	public Player(Character ch){
		this.character = ch;
	}
	
	/**
	 * Moves the player's piece on the board.
	 * @param New x coordinate
	 * @param New y coordinate
	 * @param The game board.
	 * @return Whether or not the player is able to make the move.
	 */
	public boolean move(int diceroll, int x, int y, Board board){
		boolean result = false;
		 //////MAJOR BREAkTHROUGH, move the player one step at a time. bypass most checks :D
		
		
		//decides legality of move.
		//Bad cases
		// x = current x and y = current y.  new position is same as old.
		////Blatant distance issue
		// x - diceroll > current x
		// x + diceroll < current x
		// y - diceroll > current y
		// y + diceroll < current y
		
		//total distance moved is less than diceroll, and new position is not in a room
		
		
		//if endpoint < diceroll somehow, and it is not in a room.
		
		
		return result;
	}
}
