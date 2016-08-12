package cluedo.locations;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import cluedo.Card;
import cluedo.Card.WEAPON;
import cluedo.Game;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

/**
 * The game board: Holds the character and weapon pieces.
 * @author Daniel Anastasi
 *
 */
public class Board {

	/** Board width and height. **/
	public static final int BOARD_WIDTH = 651, BOARD_HEIGHT = 703;   	//earlier 700,600
	/** Dimensions of our squares, not including black borders. Based on board dimension 645, 713**/
	public static final int SQ_WIDTH= 24, SQ_HEIGHT = 25;
	/** Offset to compensate for board borders**/
	public static final int PIECE_OFFSET = 3;
	/** The size of the game board in squares */
	private static final int SIZE = 25;

	/** Weapon image width and height for template image cropping. **/
	public final int WEAPON_WIDTH = 170, WEAPON_HEIGHT = 220;

	/** The board image. **/
	private BufferedImage boardImage;

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
	/** A list of the game's weapon pieces**/
	private List<Weapon> weapons;

	public Board(){
		// Items, characters and their locations if/when in rooms:
		this.rooms = new ArrayList<Room>();
		this.characters = new ArrayList<>();
		this.roomsFromWeapons = new HashMap<>();
		this.roomFromCharacter = new HashMap<>();

		// Locations relative to the game board itself:
		this.board = new int[SIZE][SIZE];
		this.visualBoard = new char [SIZE][SIZE*2];
		this.doors = new Door[SIZE][SIZE];
		this.boardImage = loadImage("./Images/cluedoBoard.jpeg");		//loads board image

		//Adds rooms to the board.
		Room lounge = new Room("LOUNGE", new int[]{0,1,2,3,4,5}, new int[]{0,0,0,0,0,0}, new int[]{0,1,2,3,4,5},  new int[]{4,4,4,4,4,4});
		this.rooms.add(lounge);
		this.rooms.add(new Room("DINING_ROOM", new int[]{0,1,2,3,4,5}, new int[]{10,10,10,10,10,10}, new int[]{0,1,2,3,4,0}, new int[]{14,14,14,14,14,14}));
		Room kitchen = new Room("KITCHEN", new int[]{1,2,3,1,2,3}, new int[]{19,19,19,20,20,20}, new int[]{0,1,2,3,4,0}, new int[]{23,23,23,23,23,22});
		this.rooms.add(kitchen);
		this.rooms.add(new Room("BALL_ROOM", new int[]{13,14,15,16,13,14}, new int[]{20,20,20,20,21,21}, new int[]{13,14,15,16,13,14}, new int[]{18,18,18,19,19,19}));
		Room conservatory = new Room("CONSERVATORY", new int[]{19,19,19,20,20,20}, new int[]{21,22,23,21,22,23}, new int[]{21,21,21,22,22,22}, new int[]{21,22,23,21,22,23});
		this.rooms.add(conservatory);
		this.rooms.add(new Room("BILLIARD_ROOM", new int[]{19,19,19,20,20,20}, new int[]{13,14,15,13,14,15}, new int[]{21,21,21,22,22,22}, new int[]{13,14,15,13,14,15}));
		this.rooms.add(new Room("LIBRARY", new int[]{18,18,18,19,19,19}, new int[]{7,8,9,7,8,9}, new int[]{21,21,21,22,22,22}, new int[]{7,8,9,7,8,9}));
		Room study = new Room("STUDY", new int[]{18,19,20,21,22,23}, new int[]{2,2,2,2,2,2}, new int[]{18,19,20,21,22,23}, new int[]{3,3,3,3,3,3});
		this.rooms.add(study);
		this.rooms.add(new Room("HALL", new int[]{10,11,12,13,10,11}, new int[]{6,6,6,6,5,5}, new int[]{10,11,12,13,10,11}, new int[]{2,2,2,2,1,1}));
		//Adds stairs to the corner rooms
		lounge.setStairs(conservatory);
		conservatory.setStairs(lounge);
		kitchen.setStairs(study);
		study.setStairs(kitchen);

		parseSquareFile();	//Adds squares to the board.
		parseDoorFile();	//Adds doors to the board and rooms.
		parseVisualMap();

		//Sets up characters
		this.characters.add(new Character(7,0,"Miss Scarlett", "MS", loadImage("./Images/Miss Scarlett.png")));
		this.characters.add(new Character(0,7,"Col Mustard", "CM", loadImage("./Images/Col Mustard.png")));
		this.characters.add(new Character(9,24,"Mrs White", "MW", loadImage("./Images/Mrs White.png")));
		this.characters.add(new Character(14,24,"Mr Green", "MG", loadImage("./Images/Mr Green.png")));
		this.characters.add(new Character(23,18,"Mrs Peacock", "MP", loadImage("./Images/Mrs Peacock.png")));
		this.characters.add(new Character(23,5,"Prof Plum", "PP", loadImage("./Images/Prof Plum.png")));


		int rand = 0, weaponIndex = 0, vertOffset = 0, horiOffset = 0;
		List<Weapon.Name> weaponNames = new ArrayList<>(Arrays.asList(Weapon.Name.values()));
		this.weapons = new ArrayList<>();
		Weapon w = null;
		// Creates and distribute weapons between rooms
		for(Weapon.Name name : weaponNames){
			rand = (int)(Math.random()*this.rooms.size());	//index of the room chosen at random
			w = new Weapon(name, loadImage("./Images/"+name.toString()+".jpg"));
			this.weapons.add(w);	//adds the weapon to the list.
			changeWeaponRoom(w, this.rooms.get(rand)); //adds a weapon to the room
		}

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
				&& x < SIZE && y < SIZE;
	}

	/**
	 * Return the list of rooms.
	 * @return A list of rooms.
	 */
	public List<Room> getRooms(){
		return this.rooms;
	}

	/**
	 * Returns a list of the weapon pieces/
	 * @return A list of Weapons
	 */
	public List<Weapon> getWeapons(){
		return this.weapons;
	}

	/**
	 * Returns the room which a particular weapon piece resides.
	 * @param The weapon's name
	 * @return The room.
	 */
	public Room getRoomFromWeaponName(Weapon.Name weapon){
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
	 * Draws the board to the canvas
	 */
	public void draw(Graphics g){
		g.drawImage(this.boardImage, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);	//fits image to size

		//draws the weapons
		for(Weapon w : this.weapons){
			w.draw(g);
		}

		for(Character c : this.characters)
			c.draw(g);


		/*
		//Prints the array
		for(int i = map.length-1; i >= 0; --i){
			for(int j = 0; j < map[i].length; ++j){
				System.out.printf("%s",map[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		 */
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
		y = scan.nextInt();
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
					this.visualBoard[row][col] = line.charAt(col);
				}
			}

			reader.close();		//won't accept being put in finally block.
		}catch(IOException e){throw new Error(e);}
	}

	/**
	 * Changes the Character.room field to the new room.
	 * Removes the character from any room they were in.
	 * Adds the player to the new room.
	 * If the room is null, the player will be removed from their room, and have its field changed.
	 * In this instance it will not be added to a new room.
	 * @param c TODO
	 * @param r TODO
	 * @param The character.
	 * @param The new room to move the character to.
	 */
	public void changeCharacterRoom(Character c, Room r){
		boolean contains = false;
		if(c.getRoom() != null)
			c.getRoom().removeCharacter(c);		//Removes character from old room
		c.setRoom(r);								//Changes characters record of room.
		setRoomFromCharacter(c.NAME, null);
		if(r == null)
			return;
		r.addCharacter(c);						//Moves character to the new room.

		//gets X to set to
		if(r.getCharacters().isEmpty()){
			c.setX(r.getCharPositionsX()[0]);
			c.setY(r.getCharPositionsY()[0]);
		}
		else{
			// Finds the first character position which is not currently occupied.
			for(int x = 0; x < r.getCharPositionsX().length; x++){
				contains = false;
				for(int piece = 0; piece < r.getCharacters().size(); piece++){
					if(r.getCharacters().get(piece).getX() == r.getCharPositionsX()[x]){
						contains = true;
						break;
					}
				}
				if(!contains){
					c.setX(r.getCharPositionsX()[x]);
					c.setY(r.getCharPositionsY()[x]);
					return;
				}
			}
		}

	}

	/**
	 * Moves the weapon piece to the room
	 * @param The weapon
	 * @param The room
	 * @param game TODO
	 * @param w TODO
	 * @param r TODO
	 */
	public void changeWeaponRoom(Weapon w, Room r){
		boolean contains = false;
		Room oldRoom = getRoomFromWeaponName(w.name);
		if(oldRoom != null)
			oldRoom.removeWeapon(w);						// Removes the weapon from the old room
		setRoomFromWeapon(w, r);					// Changes mapping of weapon -> room in board.
		if(r == null) return;
		r.addWeapon(w);										// Moves the weapon to the new room.
		if(r.getWeapons().isEmpty()){
			w.setX(r.getWeaponPositionsX()[0]);
			w.setY(r.getWeaponPositionsY()[0]);
		}
		else{
			// Finds the first weapon position which is not currently occupied.
			for(int x = 0; x < r.getWeaponPositionsX().length; x++){
				contains = false;
				for(int piece = 0; piece < r.getWeapons().size(); piece++){
					if(r.getWeapons().get(piece).getX() == r.getWeaponPositionsX()[x]){
						contains = true;
						break;
					}
				}
				if(!contains){
					w.setX(r.getWeaponPositionsX()[x]);
					w.setY(r.getWeaponPositionsY()[x]);
					return;
				}
			}
		}
	}

	/**
	 * Returns the weapon with the given name.
	 * This method assumes that the weapon is in the map roomsFromWeapons
	 * @param The name of the weapon
	 * @return The weapon
	 */
	public Weapon findWeaponFromName(Weapon.Name name){
		for(Weapon w : this.roomsFromWeapons.keySet()){
			if(w.name == name)
				return w;
		}
		return null;
	}

	/**
	 * Loads an image from a filepath
	 * @param The filepath as a string
	 * @return The buffered image
	 */
	public BufferedImage loadImage(String filepath){
		BufferedImage b = null;
		try {
			b =  ImageIO.read(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}
