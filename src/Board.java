import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Board {
	private Square[][] board;
	private List<Room> rooms;

	public Board(){
		addRooms();					//Adds rooms to the board.
		parseFile("/Board.txt");	//Adds squares to the board.
		parseFile("/Doors.txt");	//Adds doors to the board and rooms.
	}

	
	/**
	 * Fills the board array with Square object. 
	 * Loads the square data from a file.
	 */
	private void parseFile(String filepath){
		int x = 0, y = 0, key = 0;
		Scanner scan = null;
		String room = null;
		try{
			scan = new Scanner(new File(filepath));	// The file scanner.
			while(scan.hasNextLine()){
				scan.useDelimiter(",");
				x = scan.nextInt();
				y =  scan.nextInt();
				key = scan.nextInt();
				if(filepath.equals("/Board.txt"))
					parseSquare(x, y, key);	// Each square is parsed.
				else
					room = scan.next();
					parseDoor(x, y, key, room);
				scan.nextLine();		//drops to next line.
			}
		}catch(IOException e){throw new Error(e);}
		finally{scan.close();}

	}
	

	/**
	 * Processes a Square object from an integer triple and inserts it into the board.
	 * @param X coordinate integer
	 * @param Y coordinate integer
	 * @param Key integer representing the walls around the square.
	 * @throws IOException 
	 */
	private void parseSquare(int x, int y, int key) throws IOException{
		if(key > 12)
			throw new IOException("Square could not be parsed.");
		
		Square s = null;
		
		switch(key){
		case 0:
			s = new Square(true, false, false, false);	//top only
		case 1:
			s = new Square(false, true, false, false);	//right only
		case 2:
			s = new Square(false, false, true, false);	//bottom only
		case 3:
			s = new Square(false, false, false, true);	//left only
		case 4:
			s = new Square(true, false, true, false);	//top and bottom
		case 5:
			s = new Square(false, true, true, true);		//all but top	
		case 6:
			s = new Square(true, false, true, true);	//all but right	
		case 7:
			s = new Square(true, true, false, true);	//all but bottom
		case 8:
			s = new Square(true, true, true, false);	//all but left
		case 9:
			s = new Square(true, false, false, true);	//topleft	
		case 10:
			s = new Square(true, true, false, false);	//topright	
		case 11:
			s = new Square(false, true, true, false);	//bottomright
		case 12:
			s = new Square(false, false, true, true);	//bottom left	
		}
		this.board[x][y] = s;
	}
	
	/**
	 * Processes a Door object from an integer triple and inserts it into the board.
	 * @param X coordinate integer
	 * @param Y coordinate integer
	 * @param Key integer representing the walls around the square.
	 * @throws IOException 
	 */
	private void parseDoor(int x, int y, int key, String roomName) throws IOException{
		if(key > 3)
			throw new IOException("Door could not be parsed.");
		
		Door door = null;
		
		switch(key){
		case 0:
			door = new Door(false, false, false, false);	//No walls
		case 1:
			door = new Door(true, false, false, false);	//top only
		case 2:
			door = new Door(false, true, false, false);	//right only
		case 3:
			door = new Door(false, true, true, false);	//right and below
		
		}
		this.board[x][y] = door;			//adds the door to the board
		// adds the door to the room
		for(Room room : this.rooms){
			if(room.NAME.equals(roomName)){
				room.addDoor(door);			// gives the door to the room.
				door.setRoom(room);			// gives the room to the door.
			}
		}
	}
	
	/**
	 * Adds rooms to the list of rooms.
	 */
	private void addRooms(){
		 this.rooms.add(new Room("LOUNGE"));
		 this.rooms.add(new Room("DINING_ROOM"));
		 this.rooms.add(new Room("KITCHEN"));
		 this.rooms.add(new Room("BALL_ROOM"));
		 this.rooms.add(new Room("CONSERVATORY"));
		 this.rooms.add(new Room("BILLIARD_ROOM"));
		 this.rooms.add(new Room("LIBRARY"));
		 this.rooms.add(new Room("STUDY"));
		 this.rooms.add(new Room("HALL"));
	}
}
