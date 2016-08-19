package cluedo.test.suite;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

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
	
	@Test
	public void testNullCardLearnt(){
		try{
			player.learn(null);
			fail();
		}catch(IllegalArgumentException e){}
	}
}
