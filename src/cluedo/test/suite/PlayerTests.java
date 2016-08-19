package cluedo.test.suite;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;

/**
 * Tests for the PLayer Class of the cluedo Game and associated .
 * @author aidandoak
 *
 */
public class PlayerTests {
	Board board = new Board();
	Character character = board.getCharacters().get(0);
	Player player = new Player(null , character);

	@Test
	public void testIllegalHandModification(){
		try{
			player.setHand(null);		//null hand
			fail();
		}catch(Exception e){}
	}
}
