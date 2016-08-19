package cluedo.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import cluedo.Player;
import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

public class MoveTests {
	@Test
	public void legalMoves() throws IOException{
		Board board = new Board();
		Character character = board.getCharacters().get(0);
		Player player = new Player(null , character);
		character.setPosition(5,7);			//sets to safe square

		//move up ends up in correct square (x,y+1)
		assert(player.move('w', board));
		assert(character.getX() == 5 && character.getY() == 8);

		//right ends up in correct square (x+1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('d', board));
		assert(character.getX() == 6 && character.getY() == 7);

		//down ends up in correct square (x,y-1)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('s', board));
		assert(character.getX() == 5 && character.getY() == 6);

		//left ends up in correct square (x-1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move('a', board));
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
}
