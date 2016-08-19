package cluedo.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

public class BoardTests {
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
}
