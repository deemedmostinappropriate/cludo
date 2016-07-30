import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Player {

	private Character character = null;
	private Set<Room> knownRooms;
	private Set<Character> knownCharacters;
	private Set<WEAPON> knownWeapons;
	public final int PLAYER_NUM;
	
	public Player(int playerNumber, List<Character> freeCharacters){
		PLAYER_NUM = playerNumber;
		this.knownRooms = new HashSet<>();
		this.knownCharacters = new HashSet<>();
		this.knownWeapons = new HashSet<>();
		
		char c = '\0';
		Scanner scan = new Scanner(System.in);
		//Requests player to choose character from list of free characters.
		while(this.character == null){
			System.out.printf("Player %d, please choose your character:\n", PLAYER_NUM);
			for(int i = 0; i < freeCharacters.size(); i++){
				System.out.printf("(%c) %s\n",'a'-i, freeCharacters.get(i).NAME); 	//e.g (a)Colonel Mustard
			}
			c = (char) scan.nextInt();
			if('a'-c < freeCharacters.size())
				this.character = freeCharacters.get('a'-c);
			else
				System.out.println("Sorry, your choice is not valid. Please try again.");
		}
		if(this.character != null)
			System.out.println(this.character.NAME);
		
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
		else if(o instanceof WEAPON){
			this.knownWeapons.add((WEAPON)o);
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
		//Bad/false cases:
		// x,y are outside the board range
		if(!board.inRange(x, y)){
			return false;
		}
		// x = current x and y = current y.  new position is same as old.
		if(x == character.getX() && y == character.getY()){
			return false;
		}
		
		// false if taking more than one step:
		if(x != character.getX() - 1 || x != character.getX() + 1
				|| y != character.getY() - 1 || x != character.getY() + 1){
			return false;
		}
		//total distance moved is less than diceroll, and new position is not in a room
		
		
		//if endpoint < diceroll somehow, and it is not in a room.
		
		// When all false move conditions are exhausted, return success:
		return true;
	}
}
