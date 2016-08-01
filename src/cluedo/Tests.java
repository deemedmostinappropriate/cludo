package cluedo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import cluedo.locations.Board;
import cluedo.locations.Room;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

public class Tests {
	private List<Character>characters;
	private Board board;
	Player player;
	private List<Room> rooms;
	Character character;
	Game game;

	@Test
	public void setup(){
		this.game = new Game();
		this.board = game.board;
		this.player = game.getPlayers().get(0);
		this.character = player.getCharacter();
		// Solution implemented properly
		assertTrue(Game.murderer != null);
		assertTrue(Game.murderRoom != null);
		assertTrue(Game.murderWeapon != null);
		// Hands distributed
		int cardsInHands = 0;
		for(Player p : game.getPlayers()){
			assertTrue(p.getHand() != null && !p.getHand().isEmpty());		//no players have empty hands
			cardsInHands += p.getHand();
		}
		assertTrue(cardsInHands == 18);//all hands total 18 (all cards - solution)

		assert(true);
	}


	//safe movement in any direction from (5,7)

	@Test
	public void legalMoves(){
		character.setPosition(5,7);			//sets to safe square
		//move up ends up in correct square (x,y+1)
		assert(player.move(1, 'w', board));
		//ToDo:process move		
		assert(character.getX() == 5 && character.getY() == 8);

		//right ends up in correct square (x+1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move(1, 'd', board));
		//ToDo:process move
		assert(character.getX() == 6 && character.getY() == 7);

		//down ends up in correct square (x,y-1)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move(1, 's', board));
		//ToDo:process move
		assert(character.getX() == 5 && character.getY() == 6);

		//left ends up in correct square (x-1,y)
		character.setPosition(5,7);		//resets to safe square
		assert(player.move(1, 'a', board));
		//ToDo:process move
		assert(character.getX() == 4 && character.getY() == 7);  


	}

	@Test
	public void illegalMoves(){
		character.setPosition(0,7);		//resets to unsafe square(walls above, below and to left)
		assertFalse(player.move(1, 'w', board));		//illegal move up into non-room wall
		assertFalse(player.move(1, 's', board));		//illegal move down into non-room wall
		assertFalse(player.move(1, 'a', board));		//illegal move into non-room wall

		character.setPosition(17, 12);		//resets to unsafe square(wall to right)
		assertFalse(player.move(1, 'd', board));		//illegal move into room wall on right

		assertFalse(player.move(1, 'f', board)); //illegal instruction, letter not a direction.
		assertFalse(player.move(0, 'a', board));	//illegal move with diceroll of 0.

		assertFalse(player.move(99, 'd', board)); 	//dice roll too high 
		//board null
		try{
			player.move(2, 'd', null);
			fail();
		}catch(IllegalArgumentException e){}
	}


	//movement into room:
	//player is on door square
	//if player direction is door's direction
	//then room contains player
	// and player dice roll == 0        
	// room contains player


	@Test
	public void legalRoomTests(){
		character.setRoom(null);
		character.setPosition(6,6);	//door to lounge
		player.move(5, 's', board);	//processes player request to walk down
		//ToDo: process movement
		assertTrue(character.getRoom() != null);		//is in a room.
		assertTrue(character.getRoom().equals(rooms.get(0))); // is in correct room.
		assertTrue(rooms.get(0).getCharacters().contains(character)); //room holds character
		assertTrue(board.getRoomFromCharacter(character.NAME) != null); //roomFromCharacter map updated
		assertTrue(board.getRoomFromCharacter(character.NAME).equals(
				rooms.get(0)));	// room mapped to character name.


		//test moving out of room.

	}

	@Test
	public void illegalRoomTests(){
		character.setRoom(null);
		character.setPosition(6,6);	//door to lounge
		player.move(1, 'a', board);	//processes player request to walk left
		//ToDo: process movement
		assertTrue(character.getRoom() == null); //cannot move into lounge by walking left




	}


	//movement from rooms
	//on stepping out of a room, the player's position is updated to the square

	//movement via staircase
	//old room does not contain character
	//new room does contain character
	//

	@Test
	public void legalSuggestionTests(){
		assertNull(Game.suggestion(player, 0, 0, 0));		//should process properly
		Weapon weapon = Weapon.values()[0];
		Room room = board.getRooms().get(0);
		Character character = board.getCharacters().get(0);
		assertTrue(room.contains(weapon));	//rope should be held in lounge
		assertTrue(board.getRoomFromWeapon(weapon).equals(room));//lounge should be mapped to weapon
		assertTrue(player.getKnownCharacters().contains(character));
		assertTrue(player.getKnownRooms().contains(room));
		assertTrue(player.getKnownWeapons().contains(weapon));
	}

	@Test
	public void illegalSuggestionTests(){
		//character selection too low
		assertTrue(Game.suggestion(player, -1, 0,0).equals("Character could not be found, please try again."));
		//character selection too high
		assertTrue(Game.suggestion(player, 10, 0,0).equals("Character could not be found, please try again."));
		//room selection too low
		assertTrue(Game.suggestion(player, 0, -1,0).equals("Room could not be found, please try again."));
		//room selection too high
		assertTrue(Game.suggestion(player, 0, 10,0).equals("Room could not be found, please try again."));
		//weapon selection too low
		assertTrue(Game.suggestion(player, 0, 0,-1).equals("Weapon could not be found, please try again."));
		//weapon selection too high
		assertTrue(Game.suggestion(player, 0, 10,10).equals("Weapon could not be found, please try again."));

		try{
			Game.suggestion(null, 0, 0, 0);	// null player
			fail();
		}catch(RuntimeException e){}
	}

	@Test
	public void illegalAccusationTests(){
		//character selection too low
		assertTrue(Game.accusation(player, -1, 0,0).equals("Character could not be found, please try again."));
		//character selection too high
		assertTrue(Game.accusation(player, 10, 0,0).equals("Character could not be found, please try again."));
		//room selection too low
		assertTrue(Game.accusation(player, 0, -1,0).equals("Room could not be found, please try again."));
		//room selection too high
		assertTrue(Game.accusation(player, 0, 10,0).equals("Room could not be found, please try again."));
		//weapon selection too low
		assertTrue(Game.accusation(player, 0, 0,-1).equals("Weapon could not be found, please try again."));
		//weapon selection too high
		assertTrue(Game.accusation(player, 0, 10,10).equals("Weapon could not be found, please try again."));

		try{
			Game.suggestion(null, 0, 0, 0);	// null player
			fail();
		}catch(RuntimeException e){}
	}

	@Test
	public void gameChangeCharacterRoomTests(){
		try{
			Game.changeCharacterRoom(null, board.getRooms().get(0));	//should fail for null parameter 1
			fail();
		}catch(Exception e){}


		try{
			Game.changeCharacterRoom(character, null);	
		}catch(Exception e){fail();}	//should not throw an exception.
	}

	@Test
	public void playerTests(){
		Card[] hand = player.getHand();

		//attempt to illegally modify hand.
		player.setHand(null);
		assertTrue(player.getHand().equals(hand));

		//Attempt to learn null.
		try{
			player.learn(null);
			fail();
		}catch(IllegalArgumentException e){}

	}

	@Test
	public void characterTests(){
		//construction
		try{
			new Character(-1, 5, "");		// x position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(25, 5, "");		// x position too high
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, -1, "");		// y position too low
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, 25, "");		// y position too high
			fail();
		}catch(IllegalArgumentException e){}

		try{
			new Character(5, 25, null);		// name is null
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
		//in range
		assertTrue(board.inRange(0, 0));
		assertTrue(board.inRange(24, 24));
		assertFalse(board.inRange(-1, 24));
		assertFalse(board.inRange(5, -1));
		assertFalse(board.inRange(99, 5));
		assertFalse(board.inRange(5, 99));

		//getRoomFromWeapon
		try{
			board.getRoomFromWeapon(null);		// weapon null
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

		try{
			board.getRoomFromCharacter("Scarlett");		// contains part of name only
			fail();
		}catch(IllegalArgumentException e){}

		//setRoomFromCharacter
		Room room = board.getRooms().get(0);
		try{
			board.setRoomFromCharacter(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromCharacter("sdfsdf", room);		// name wrong
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromCharacter("sdfsdf", room);		// name wrong
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromCharacter(character.NAME, null);
		}catch(IllegalArgumentException e){fail();}  //should not throw exception

		//setRoomFromWeapon
		try{
			board.setRoomFromWeapon("sdfsdf", room);		// name wrong
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromWeapon(null, room);		// name null
			fail();
		}catch(IllegalArgumentException e){}

		try{
			board.setRoomFromWeapon(Weapon.CANDLESTICK, null);		// name null		
		}catch(IllegalArgumentException e){fail();} //should not throw exception
	}

	@Test
	public void roomTests(){
		Room room = board.getRooms().get(0);
		//addWeapon
		try{
			room.addWeapon(null); 		// null weapon
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addWeapon(null); 	//weapon already contained in room
		}catch(IllegalArgumentException e){fail();} // should be ok.

		//addDoor
		try{
			room.addDoor(null); 		// null door
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addDoor(null); 	//door already contained in room
		}catch(IllegalArgumentException e){fail();} // should be ok.

		//addCharacter
		try{
			room.addCharacter(null); 		// null character
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.addCharacter(null); 	//character already contained in room
		}catch(IllegalArgumentException e){fail();} // should be ok.

		// removeWeapon
		try{
			room.removeWeapon(null); 		// null weapon
			fail();
		}catch(IllegalArgumentException e){}

		try{
			room.removeWeapon(Weapon.CANDLESTICK); 	// loop is prep for next one.
		}catch(IllegalArgumentException e){}	//may throw
		try{
			room.removeWeapon(Weapon.CANDLESTICK); 	
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


