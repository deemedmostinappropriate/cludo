import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private int[][] board;		//filled with 1's where a traversable square is.
	private List<Room> rooms;
<<<<<<< HEAD
	private Door[][] doors;		// Door locations.
	
	private static final int SIZE = 25;
=======
	private Door[][] doors;				// Door locations.
	private Set<Character> characters;	//A set of character pieces on the board.
	private Map<Room.WEAPON, Room> roomsFromWeapons; //So that we can quickly find where the weapon is.
>>>>>>> origin/master

	public Board(){
		this.board = new int[SIZE][SIZE];
		this.rooms = new ArrayList<Room>();
<<<<<<< HEAD
		this.doors = new Door[SIZE][SIZE];
=======
		this.doors = new Door[25][25];
		this.characters = new HashSet<>();
		this.roomsFromWeapons = new HashMap<>();
>>>>>>> origin/master
		//Adds rooms to the board.
		this.rooms.add(new Room("LOUNGE"));
		this.rooms.add(new Room("DINING_ROOM"));
		this.rooms.add(new Room("KITCHEN"));
		this.rooms.add(new Room("BALL_ROOM"));
		this.rooms.add(new Room("CONSERVATORY"));
		this.rooms.add(new Room("BILLIARD_ROOM"));
		this.rooms.add(new Room("LIBRARY"));
		this.rooms.add(new Room("STUDY"));
		this.rooms.add(new Room("HALL"));			
		parseSquareFile();	//Adds squares to the board.

		parseDoorFile();	//Adds doors to the board and rooms.
	}

	/**
	 * Returns the integer array representing the board.
	 * @return The board array.
	 */
	public int[][] getBoard(){
		return this.board;
	}
	
	/**
<<<<<<< HEAD
	 * Returns a boolean indicating whether or not the given x,y coordinates
	 * are on the board.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInRange(int x, int y){
		return x >= 0 && y >= 0
				&& x <= SIZE && y <= SIZE;
	}
	
=======
	 * Returns the room which a particular weapon resides.
	 * @param The weapon
	 * @return The room.
	 */
	public Room getRoomFromWeapon(Room.WEAPON weapon){
		return this.roomsFromWeapons.get(weapon);
	}
	
	/**
	 * Changes this.roomsFromWeapons to point from the weapon to the new room
	 * @param The weapon
	 * @param The room
	 */
	public void setRoomFromWeapon(Room.WEAPON weapon, Room room){
		this.roomsFromWeapons.put(weapon, room);
	}
	
	/**
	 * Finds the room with a name that matches the argument.
	 * @param The name to match;
	 * @return The room which matches the name;
	 */
	public Room findRoom(String name){
		for(Room room : this.rooms){
			if(room.NAME.equals(name))
				return room;
		}
		return null;
	}
	
	/**
	 * Finds the room with a name that matches the argument.
	 * @param The name to match;
	 * @return The character which matches the name;
	 */
	public Character findCharacter(String name){
		for(Character character : this.characters){
			if(character.NAME.equals(name))
				return character;
		}
		return null;
	}


>>>>>>> origin/master
	/**
	 * Reads the file ascii-map.txt to fill in the board array.
	 */
	private void parseSquareFile(){
		BufferedReader reader = null;
		String line = null;
		try{
			reader = new BufferedReader(new FileReader(new File("ascii-map.txt")));
			// Reads each line while there is one
			for(int row = 0; (line = reader.readLine()) != null; row ++){
				// Increments over 2 chars due to spaces.
				for(int col = 0; col < line.length(); col += 2){
					if(line.charAt(col) == '1')
						this.board[row][col/2] = 1;
				}
			}

			reader.close();		//won't accept being put in finally block.
		}catch(IOException e){throw new Error(e);}

	}

	/**
	 * Fills the board array with Square object. 
	 * Loads the square data from a file.
	 */
	private void parseDoorFile(){
		int x = 0, y = 0, key = 0;
		String room = null, line = null;
		Scanner scan = null;
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(new File("doors.txt")));
			while((line = reader.readLine()) != null){
				scan = new Scanner(line);	// The file scanner.
				scan.useDelimiter(",");
				parseDoor(scan);		//parses the line to create a door.
			}
			
		}catch(IOException e){throw new Error(e);}
		finally{scan.close();}

	}

	/**
	 * Processes a Door object from an integer triple and inserts it into the board.
	 * @param X coordinate integer
	 * @param Y coordinate integer
	 * @param Key integer representing the walls around the square.
	 * @throws IOException 
	 */
	private void parseDoor(Scanner scan) throws IOException{

		int x = 0, y = 0, key = 0;
		String roomName = null;
		Door door = null;

		x = scan.nextInt();
		y =  scan.nextInt();
		key = scan.nextInt();
		roomName = scan.next();

		switch(key){
		case 8:
			door = new Door("UP");	//room is above
			break;
		case 6:
			door = new Door("RIGHT");	//room is to the right
			break;
		case 2:
			door = new Door("DOWN");	//room is below
			break;
		case 4:
			door = new Door("LEFT");	//room is to the left
			break;
		default:
			throw new IOException("Door could not be parsed.");
		}

		this.doors[x][y] = door;			//adds the door to the board
		// adds the door to the room
		for(Room room : this.rooms){
			if(room.NAME.equals(roomName)){
				room.addDoor(door);			// gives the door to the room.
				door.setRoom(room);			// gives the room to the door.
			}
		}
	}


}
