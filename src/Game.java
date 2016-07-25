import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//package assignment1.cluedo;

public class Game {
	
	static class Card{
		public enum WEAPON{
			ROPE, DAGGER, CANDLESTICK, REVOLVER, LEADPIPE, SPANNER;
		}
		public enum ROOM{
			HALL, DINING, BILLIARD, CONSERVATORY, BALL, LIBRARY, STUDY, LOUNGE, KITCHEN;
		}
		public enum CHAR{
			SCARLET, MUSTARD, WHITE, PLUM, GREEN, PEACOCK;
		}
	}

	private int numPlayers;
	private Board board;
	private Player currentPlayer = null;
	private Character murderer = null;
	private Room murderRoom = null;
	private Room.WEAPON murderWeapon = null;
	private List<Player> players;
	
	
	public Game(int numPlayers){
		this.numPlayers = numPlayers;
		this.players = new ArrayList<Player>();
		
		//Set up players
		//Teach players about what was not involved in the murder: Rooms, characters, weapons.
		//Chooses character, weapon, and room for murderer, murder weapon and murder room.
		//
		//Distributes weapons around rooms.
		
		this.board = new Board();//Set up board
		run();
	}

	
	/**
	 * Runs the game loop.
	 */
	public void run(){
		// Array input test
		int[][] b = this.board.getBoard();
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[0].length; j++){
				System.out.print(b[i][j]);
			}
			System.out.println();
		}
		
		
		while(true){
			
			
			//exit loop if player has won via correct accusation.
			//exit loop if only one player remains.
			
			//Changes currentPlayer for next round.
		}
		
	}

	
	/**
	 * A suggestion made by the player to learn more about the murder.
	 * @param The player who made the suggestion
	 * @param The name of the character provided by the player.
	 * @param The name of the room provided by the player.
	 * @param The name of the weapon provided by the player.
	 * @return A string detailing why the method was unsuccessful. Is null if there was no issue.
	 */
	private String suggestion(Player p, String characterName, String roomName, String weaponName){
		Character character = this.board.findCharacter(characterName);
		Room room = this.board.findRoom(roomName);
		Room.WEAPON weapon = Room.WEAPON.valueOf(weaponName);
		
		if(character == null)return "Character could not be found, please try again.";
		else if(room == null)return "Room could not be found, please try again.";
		else if(weapon == null)return "Weapon could not be found, please try again.";
		
		changeCharacterRoom(character, room);
	
		this.board.getRoomFromWeapon(weapon).removeWeapon(weapon);	//Removes the weapon from the old room
		room.addWeapon(weapon);										//Moves the weapon to the new room.
		this.board.setRoomFromWeapon(weapon, room);					//Changes mapping of weapon -> room in board.
		
		p.learn(room);				//Adds the Room to the player's set of known Rooms
		p.learn(character);			//Adds the Character to the player's set of known Characters
		p.learn(weapon);			//Adds the Weapon to the player's set of known Weapons
		
		return null;
	}
	
	/**
	 * Changes the Character.room field to the new room.
	 * Removes the character from any room they were in.
	 * Adds the player to the new room.
	 * If the room is null, the player will be removed from their room, and have its field changed.
	 * In this instance it will not be added to a new room.
	 * @param The character.
	 * @param The new room to move the character to.
	 */
	public void changeCharacterRoom(Character character, Room newRoom){
		if(character.getRoom() != null)
			character.getRoom().removeCharacter(character);		//Removes character from old room
		character.setRoom(newRoom);								//Changes characters record of room.
		newRoom.addCharacter(character);						//Moves character to the new room.
	}
	
	/**
	 * Called when a player chooses to make an accusation about the murder.
	 * If they guess correctly they win the game, otherwise they lose and the game continues without them.
	 * @param The player making the accusation
	 * @param The character accused of the crime.
	 * @param The room where the murder is said to have taken place.
	 * @param The proposed murder weapon.
	 * @return A string message telling the player the result of their accusation.
	 */
	private String accusation(Player p, String characterName, String roomName, String weaponName){
		String result = null;
		Character character = this.board.findCharacter(characterName);
		Room room = this.board.findRoom(roomName);
		Room.WEAPON weapon = Room.WEAPON.valueOf(weaponName);
		if(character != this.murderer 
				|| room != this.murderRoom
				|| weapon != this.murderWeapon){
			result = "Sorry, you have guessed incorrectly. You are out of the game. :(";
			this.players.remove(this.currentPlayer);	//removes the current player from the game.
		}
		else
			result = "CONGRATULATIONS :D  YOU WIN!!!!!";
		return result;
	}
	

	public static void main(String[] args){
		int players = 0;
		Scanner s = new Scanner(System.in);;
		System.out.println("Welcome to Cluedo");
		System.out.println("How many people are playing? (enter a number between 3 and 6):");
		// Makes sure the number of players is in the range of 3-6.
		while(players < 3 || players > 6){
			players = s.nextInt();
			if(players < 3 || players > 6){
				players = 0;
				System.out.println("Please enter a number between 3 and 6:");
			}
		}

		s.close();				// closes the scanner.
		new Game(players);
	}


}

