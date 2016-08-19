package cluedo.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

public class RoomTests {
	/*	Movement into room:
		Player is on door square:
		if player direction is door's direction,
		then room contains player. */

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
	
	
}
