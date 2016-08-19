package cluedo.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

/**
 * The test suite for the Cluedo application.
 * @author Daniel Anastasi
 *
 */
public class Tests {


	//safe movement in any direction from (5,7)

	@Test
	public void legalMoves() throws IOException{
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);
		character.setPosition(5,7);			//sets to safe square

		//move up ends up in correct square (x,y+1)
		assert(player.move('w', board));
		//ToDo:process move
		assert(character.getX() == 5 && character.getY() == 8);

		//right ends up in correct square (x+1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('d', board));
		//ToDo:process move
		assert(character.getX() == 6 && character.getY() == 7);

		//down ends up in correct square (x,y-1)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('s', board));
		//ToDo:process move
		assert(character.getX() == 5 && character.getY() == 6);

		//left ends up in correct square (x-1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('a', board));
		//ToDo:process move
		assert(character.getX() == 4 && character.getY() == 7);


	}

	@Test
	public void illegalMoves() throws IOException{
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);

		character.setPosition(0,7);		//resets to unsafe square(walls above, below and to left)
		assertFalse(player.move('w', board));		//illegal move up into non-room wall
		assertFalse(player.move('s', board));		//illegal move down into non-room wall
		assertFalse(player.move('a', board));		//illegal move into non-room wall

		character.setPosition(17, 12);		//resets to unsafe square(wall to right)
		assertFalse(player.move('d', board));		//illegal move into room wall on right

		assertFalse(player.move('f', board)); //illegal instruction, letter not a direction.
		//board null
		try{
			player.move('d', null);
			fail();
		}catch(IllegalArgumentException e){}
	}


	//movement into room:
	//player is on door square
	//if player direction is door's direction
	//then room contains player
	// room contains player


	@Test
	public void legalRoomTests() throws IOException{
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);

		character.setRoom(null);
		character.setPosition(6,6);	//door to lounge
		player.move('s', board);	//processes player request to walk down
		//ToDo: process movement
		assertTrue(character.getRoom() != null);		//is in a room.
		assertTrue(character.getRoom().equals(board.getRooms().get(0))); // is in correct room.
		assertTrue(board.getRooms().get(0).getCharacters().contains(character)); //room holds character

	}

	@Test
	public void illegalRoomTests()throws IOException {
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);

		character.setRoom(null);
		character.setPosition(6,6);	//door to lounge
		player.move('a', board);	//processes player request to walk left
		//ToDo: process movement
		assertTrue(character.getRoom() == null); //cannot move into lounge by walking left
	}


	@Test
	public void gameChangeCharacterRoomTests(){
		Board board = new Board();
		Character character = board.getCharacters().get(0);

		try{
			board.changeCharacterRoom(null, board.getRooms().get(0));	//should fail for null parameter 1
			fail();
		}catch(Exception e){}

		try{
			board.changeCharacterRoom(character, null);
		}catch(Exception e){fail();}	//should not throw an exception.
	}

	@Test
	public void playerTests(){
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);

		//attempt to illegally modify hand.
		try{
			player.setHand(null);		//null hand
			fail();
		}catch(Exception e){}

		//Attempt to learn null.
		try{
			player.learn(null);
			fail();
		}catch(IllegalArgumentException e){}

	}

	@Test
	public void characterTests(){
		Board board = new Board();
		Character character = board.getCharacters().get(0);

		//construction
		try{
			new Character(-1, 5, "", "",null);		// x position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(25, 5, "", "",null);		// x position too high
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, -1, "", "",null);		// y position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, 25, "", "",null);		// y position too high
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, 25, null, "",null);		// name is null
			fail();
		}catch(IllegalArgumentException e){}

		// setPosition
		try{
			character.setPosition(-1, 5);		// x position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			character.setPosition(25, 5);		// x position too high
			fail();
		}catch(IllegalArgumentException e){}

		try{
			character.setPosition(5, -1);		// y position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			character.setPosition(5, 25);		// y position too high
			fail();
		}catch(IllegalArgumentException e){}

	}

	@Test
	public void boardTests(){
		Board board = new Board();
		Character character = board.getCharacters().get(0);

		//in range
		assertTrue(board.inRange(0, 0));
		assertTrue(board.inRange(24, 24));
		assertFalse(board.inRange(-1, 24));
		assertFalse(board.inRange(5, -1));
		assertFalse(board.inRange(99, 5));
		assertFalse(board.inRange(5, 99));

		//getRoomFromWeapon
		try{
			board.getRoomFromWeaponName(null);		// weapon null
			fail();
		}catch(IllegalArgumentException e){}

		//getRoomFromCharacter
		try{
			board.getRoomFromCharacter(null);		// character null
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.getRoomFromCharacter("sdfs");		// no character found
			fail();
		}catch(IllegalArgumentException e){}


		//setRoomFromCharacter
		Room room = board.getRooms().get(0);
		try{
			board.setRoomFromCharacter(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromCharacter(character.NAME, null);
		}catch(IllegalArgumentException e){fail();}  //should not throw exception

		//setRoomFromWeapon
		try{
			board.setRoomFromWeapon(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		Weapon w = new Weapon(null, null);
		try{
			board.setRoomFromWeapon(w, null);		// room name null
		}catch(IllegalArgumentException e){fail();} //should not throw exception
	}

	@Test
	public void roomTests(){
		Board board = new Board();

		Room room = board.getRooms().get(0);
		//addWeapon
		try{
			room.addWeapon(null); 		// null weapon
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addWeapon(null); 	//weapon already contained in room
			fail();
		}catch(IllegalArgumentException e){}

		//addDoor
		try{
			room.addDoor(null); 		// null door
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addDoor(null); 	//door already contained in room
			fail();
		}catch(IllegalArgumentException e){}

		//addCharacter
		try{
			room.addCharacter(null); 		// null character
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addCharacter(null); 	//character already contained in room
			fail();
		}catch(IllegalArgumentException e){} // should be ok.

		// removeWeapon
		try{
			room.removeWeapon(null); 		// null weapon
			fail();
		}catch(IllegalArgumentException e){}

		Weapon w = new Weapon(null, null);
		try{
			room.removeWeapon(w); 	// loop is prep for next one.
		}catch(IllegalArgumentException e){}	//may throw
		try{
			room.removeWeapon(w);
			fail();
		}catch(IllegalArgumentException e){}	//should throw

		// removeCharacter
		try{
			room.removeCharacter(null); 		// null character
			fail();
		}catch(IllegalArgumentException e){}

		Character c = board.getCharacters().get(0);
		try{
			room.removeCharacter(c);// loop is prep for next one.
		}catch(IllegalArgumentException e){}	//may throw
		try{
			room.removeCharacter(c);
			fail();
		}catch(IllegalArgumentException e){}	//should throw
	}



	//Room.doors needs to be a set
}


