package cluedo.locations;
import java.util.List;
import java.util.Set;

import cluedo.Card;
import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * Rooms on the Cluedo board.
 * Each room has at least one door.
 * @author Daniel Anastasi
 *
 */
public class Room {

	public final String NAME;
	private Set<Weapon> weapons = null;
	private List<Door> doors = null;
	private Set<Character> characters = null;
	

	public Room(String name){
		this.NAME = name;
		this.weapons = new HashSet<Weapon>();
		this.doors = new ArrayList<Door>();
		this.characters = new HashSet<Character>();
	}
	
	/**
	 * Returns a list of the weapons in this room.
	 * @return A list of the weapons in this room.
	 */
	public Set<Weapon> getWeapons(){
		return this.weapons;
	}
	
	/**
	 * Returns the list of this room's doors.
	 * @return A list of this room's doors.
	 */
	public List<Door> getDoors(){
		return this.doors;
	}
	
	/**
	 * Returns the set of this room's characters.
	 * @return A list of this room's characters.
	 */
	public Set<Character> getCharacters(){
		return this.characters;
	}

	/**
	 * Adds a door to the list of doors into this room.
	 * @param A door into this room.
	 */
	public void addDoor(Door door){
		this.doors.add(door);
	}
	
	/**
	 * Adds a weapon to the room.
	 * @param The weapon to add to the room.
	 */
	public void addWeapon(Weapon weapon){
		// needs some consideration
		
		this.weapons.add(weapon);
	}
	
	/**
	 * Adds a Character to the room.
	 * @param The character to add to the room.
	 */
	public void addCharacter(Character character){
		// needs some consideration
		
		
		this.characters.add(character);
	}
	
	
	/**
	 * Removes a weapon from the room.
	 * @param The weapon to remove from the room.
	 */
	public void removeWeapon(Weapon weapon){
		// needs some consideration
		
		if(!this.weapons.contains(weapon)){
			throw new IllegalArgumentException("Room does not contain weapon.");
		}
		this.weapons.remove(weapon);
	}
	
	/**
	 * Removes a character from the room.
	 * @param The character to remove from the room.
	 */
	public void removeCharacter(Character character){
		// needs some consideration
		
		if(!this.characters.contains(character)){
			throw new IllegalArgumentException("Room does not contain character.");
		}
		this.characters.remove(character);
	} 
}
