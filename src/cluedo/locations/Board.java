package cluedo.locations;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cluedo.Card;
import cluedo.Card.WEAPON;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

public class Board {
	/** This holds a value of 1 at any space where the character can move to. */
	private int[][] board;
	
	/** The visual representation of the game board.. */
	private char[][] visualBoard;
	
	/** The list of all rooms in the game. */
	private List<Room> rooms;
	
	/** Holds the x,y locations of all doors. */
	private Door[][] doors;
	
	/** Lists the characters on the board, used to access their locations. */
	private List<Character> characters;
	
	/** A map of weapons and the room they are in. */
	private Map<Weapon, Room> roomsFromWeapons;
	
	/** A map of Characters and the room they are currently in. Matches to Character.NAME. */
	private Map<String, Room> roomFromCharacter;
	
	/** The size of the game board in total */
	private static final int SIZE = 25;
	
	public Board(){
		this.board = new int[SIZE][SIZE];
		this.visualBoard = new char [SIZE][SIZE*2];
		this.rooms = new ArrayList<Room>();
		this.doors = new Door[SIZE][SIZE];
		this.characters = new ArrayList<>();
		this.roomsFromWeapons = new HashMap<>();
		this.roomFromCharacter = new HashMap<>();
		//Adds rooms to the board.
		this.rooms.add(new Room("LOUNGE", new int[]{0,1,2,3,4,5}, new int[]{0,0,0,0,0,0}, new int[]{0,1,2,3,4,5},  new int[]{4,4,4,4,4,4}));
		this.rooms.add(new Room("DINING_ROOM", new int[]{0,1,2,3,4,5}, new int[]{9,9,9,9,9,9}, new int[]{0,1,2,3,4,0}, new int[]{15,15,15,15,15,14}));
		this.rooms.add(new Room("KITCHEN", new int[]{1,2,3,1,2,3}, new int[]{18,18,18,19,19,19}, new int[]{0,1,2,3,4,0}, new int[]{23,23,23,23,23,22}));
		this.rooms.add(new Room("BALL_ROOM", new int[]{13,14,15,16,13,14}, new int[]{20,20,20,20,21,21}, new int[]{13,14,15,16,13,14}, new int[]{17,17,17,17,18,18}));
		this.rooms.add(new Room("CONSERVATORY", new int[]{18,18,18,19,19,19}, new int[]{21,22,23,21,22,23}, new int[]{21,21,21,22,22,22}, new int[]{21,22,23,21,22,23}));
		this.rooms.add(new Room("BILLIARD_ROOM", new int[]{18,18,18,19,19,19}, new int[]{12,13,14,12,13,14}, new int[]{21,21,21,22,22,22}, new int[]{13,14,15,13,14,15}));
		this.rooms.add(new Room("LIBRARY", new int[]{18,18,18,19,19,19}, new int[]{7,8,9,7,8,9}, new int[]{21,21,21,22,22,22}, new int[]{7,8,9,7,8,9}));
		this.rooms.add(new Room("STUDY", new int[]{17,18,19,17,18,19}, new int[]{2,2,2,1,1,1}, new int[]{20,20,20,21,21,21}, new int[]{2,2,2,1,1,1}));
		this.rooms.add(new Room("HALL", new int[]{10,11,12,13,10,11}, new int[]{7,7,7,7,6,6}, new int[]{10,11,12,13,10,11}, new int[]{2,2,2,2,1,1}));
		
		parseSquareFile();	//Adds squares to the board.
		parseDoorFile();	//Adds doors to the board and rooms.
		parseVisualMap();
		//Sets up characters
		this.characters.add(new Character(7,0,"Miss Scarlett", "MS"));
		this.characters.add(new Character(0,7,"Col Mustard", "CM"));
		this.characters.add(new Character(9,24,"Mrs White", "MW"));
		this.characters.add(new Character(14,24,"Mr Green", "MG"));
		this.characters.add(new Character(23,18,"Mrs Peacock", "MP"));
		this.characters.add(new Character(23,5,"Prof Plum", "PP"));
		
	}

	/**
	 * Returns the integer array representing the board.
	 * @return The board array.
	 */
	public int[][] getBoard(){
		return this.board;
	}
	
	/**
	 * Returns a door associated with the given x,y or null if it doesn't exist.
	 * @param x
	 * @param y
	 * @return
	 */
	public Door getDoor(int x, int y){
		if(!this.inRange(x, y)) return null;
		return doors[x][y];
	}
	/**
	 * Returns the set of all character pieces.
	 * @return
	 */
	public List<Character> getCharacters(){
		return this.characters;
	}
	
	
	/**
	 * Returns a boolean of whether or not a given x,y is in range of
	 * the board's x,y.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inRange(int x, int y){
		return x >= 0 && y >= 0
				&& x <= SIZE && y <= SIZE;
	}
	
	/**
	 * Return the list of rooms.
	 * @return A list of rooms.
	 */
	public List<Room> getRooms(){
		return this.rooms;
	}
	
	/**
	 * Returns the room which a particular weapon piece resides.
	 * @param The weapon
	 * @return The room.
	 */
	public Room getRoomFromWeapon(Weapon weapon){
		return this.roomsFromWeapons.get(weapon);
	}
	
	/**
	 * Returns the room in which a particualr character piece resides.
	 * @param The character's name.
	 * @return The room.
	 */
	public Room getRoomFromCharacter(String characterName){
		return this.roomFromCharacter.get(characterName);
	}
	
	/**
	 * Maps a character name to the room, in which they reside.
	 * @param The character's name.
	 * @param The room.
	 */
	public void setRoomFromCharacter(String characterName, Room room){
		this.roomFromCharacter.put(characterName, room);
	}
	
	/**
	 * Changes this.roomsFromWeapons to point from the weapon to the new room
	 * @param The weapon
	 * @param The room
	 */
	public void setRoomFromWeapon(Weapon weapon, Room room){
		this.roomsFromWeapons.put(weapon, room);
	}

	/**
	 * Draws the board to the Console.
	 */
	public void drawBoard(){
		// Copies board to new array
		char[][] map = new char[SIZE][SIZE*2];		
		for(int i = 0; i < 25; ++i){
			for(int j = 0; j < 50; ++j){
				map[i][j] = visualBoard[i][j];
			}
		}
		
		//Adds characters
		for(Character c : this.characters){
			map[c.getY()][c.getX()*2] = c.ABBREV.charAt(0);
			map[c.getY()][c.getX()*2+1] = c.ABBREV.charAt(1);
			System.out.println(c.getX() + " " + c.getY() + c.NAME);
		}
		//Adds weapons
		for(Weapon w : this.roomsFromWeapons.keySet()){
			map[w.getY()][w.getX()*2] = w.toString().charAt(0);
			map[w.getY()][w.getX()*2+1] = w.toString().charAt(1);
		}

		//Prints the array
		for(int i = map.length-1; i >= 0; --i){
			for(int j = 0; j < map[i].length; ++j){
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

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
		String line = null;
		Scanner scan = null;
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(new File("doors.txt")));
			while((line = reader.readLine()) != null){
				scan = new Scanner(line);	// The file scanner.
				scan.useDelimiter(",");
				parseDoor(scan);			// Parses the line to create a door.
			}
			reader.close();
		} catch(IOException e){
			throw new Error(e);
		} finally{
			if (scan != null) scan.close();
		}

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
			door = new Door("UP");		//room is above
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
		door.setX(x);
		door.setY(y);
		this.doors[x][y] = door;			//adds the door to the board

		// adds the door to the room
		for(Room room : this.rooms){
			if(room.NAME.equals(roomName)){
				room.addDoor(door);			// gives the door to the room.
				door.setRoom(room);			// gives the room to the door.
			}
		}
	}
	
	private void parseVisualMap(){
		BufferedReader reader = null;
		String line = null;
		try{
			reader = new BufferedReader(new FileReader(new File("visual-map.txt")));
			// Reads each line while there is one
			for(int row = 0; (line = reader.readLine()) != null; row ++){	
				// Increments over 2 chars due to spaces.
				for(int col = 0; col < line.length(); col ++){
					if(line.charAt(col) != ' ')
						this.visualBoard[row][col] = line.charAt(col);
				}
			}

			reader.close();		//won't accept being put in finally block.
		}catch(IOException e){throw new Error(e);}
	}


}
