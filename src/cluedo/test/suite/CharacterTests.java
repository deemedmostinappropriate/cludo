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
 * The Tests for Characters on the cluedo Board.
 * @author aidandoak
 *
 */
public class CharacterTests {

	Board board = new Board();
	Character character = board.getCharacters().get(0);
	
	@Test
	public void testOutOfBounds_1(){
		try{
			new Character(-1, 5, "", "",null);		// x position too low
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testOutOfBounds_2(){
		try{
			new Character(25, 5, "", "",null);		// x position too high
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testOutOfBounds_3(){
		try{
			new Character(5, -1, "", "",null);		// y position too low
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testOutOfBounds_4(){
		try{
			new Character(5, 25, "", "",null);		// y position too high
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testNullName(){
		try{
			new Character(5, 25, null, "",null);		// name is null
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testSetPosOutOfBounds_1(){
		try{
			character.setPosition(-1, 5);		// x position too low
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testSetPosOutOfBounds_2(){
		try{
			character.setPosition(25, 5);		// x position too high
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testSetPosOutOfBounds_3(){
		try{
			character.setPosition(5, -1);		// y position too low
			fail();
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testSetPosOutOfBounds_4(){
		try{
			character.setPosition(5, 25);		// y position too high
			fail();
		}catch(IllegalArgumentException e){}
	}
}
