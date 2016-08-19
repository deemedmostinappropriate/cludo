package cluedo.test.suite;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

/**
 * Tests the Board of the cluedo game.
 * @author aidandoak
 *
 */
public class BoardTests {
	Board board = new Board();
	Character character = board.getCharacters().get(0);
	
	@Test
	public void isInRange_1(){
		assertTrue(board.inRange(0, 0));
	}
	
	@Test
	public void isInRange_2(){
		assertTrue(board.inRange(24, 24));
	}
	
	@Test
	public void isOutOfRange_1(){
		assertFalse(board.inRange(-1, 24));
	}
	
	@Test
	public void isOutOfRange_2(){
		assertFalse(board.inRange(5, -1));
	}
	
	@Test
	public void isOutOfRange_3(){
		assertFalse(board.inRange(99, 5));
	}
	
	@Test
	public void isOutOfRange_4(){
		assertFalse(board.inRange(5, 99));
	}
	
	@Test
	public void testGetRoomFromWeapon(){
		try{
			board.getRoomFromWeaponName(null);		// weapon null
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testGetRoomFromCharacter(){
		try{
			board.getRoomFromCharacter(null);		// character null
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testGetNonCharacterFromRoom(){
		try{
			board.getRoomFromCharacter("sdfs");		// no character found
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test public void testSetRoomFromCharacter(){
		Room room = board.getRooms().get(0);
		
		try{
			board.setRoomFromCharacter(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromCharacter(character.NAME, null);
		}catch(IllegalArgumentException e){fail();}  //should not throw exception

	}
	
	@Test 
	public void testSetRoomFromWeapon(){
		Room room = board.getRooms().get(0);

		try{
			board.setRoomFromWeapon(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		Weapon w = new Weapon(null, null);
		try{
			board.setRoomFromWeapon(w, null);		// room name null
		}catch(IllegalArgumentException e){fail();} //should not throw exception

	}
	
	
}
