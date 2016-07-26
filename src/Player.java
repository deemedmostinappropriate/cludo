import java.util.HashSet;
import java.util.Set;

public class Player {

	private Character character;
	private Set<Room> knownRooms;
	private Set<Character> knownCharacters;
	private Set<Room.WEAPON> knownWeapons;
	
	public Player(Character ch){
		this.character = ch;
		this.knownRooms = new HashSet<>();
		this.knownCharacters = new HashSet<>();
		this.knownWeapons = new HashSet<>();
	}
	
	/**
	 * Adds a new item to the sets of what the player knows regarding the murder.
	 * @param An object that must be of type Room, Character, or Room.WEAPON.
	 */
	public void learn(Object o){
		if(o instanceof Room){
			this.knownRooms.add((Room)o);
		}
		else if(o instanceof Character){
			this.knownCharacters.add((Character)o);
		}
		else if(o instanceof Room.WEAPON){
			this.knownWeapons.add((Room.WEAPON)o);
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
	public boolean move(int diceroll, int x, int y, Board board){
		 //////MAJOR BREAkTHROUGH, move the player one step at a time. bypass most checks :D
		//Bad cases:
		//decides legality of move.
		if(!board.isInRange(x, y)){
			return false;
		}
		// x = current x and y = current y.  new position is same as old.
		if(x == character.getX() && y == character.getY()){
			return false;
		}
		////Blatant distance issue
		// x - diceroll > current x
		// x + diceroll < current x
		// y - diceroll > current y
		// y + diceroll < current y
		
		//total distance moved is less than diceroll, and new position is not in a room
		
		
		//if endpoint < diceroll somehow, and it is not in a room.
		
		
		return true;
	}
}
