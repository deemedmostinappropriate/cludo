package cluedo.locations;
import java.util.List;

import cluedo.pieces.Character;
import cluedo.pieces.Weapon;

import java.util.ArrayList;
/**
 * Rooms on the Cluedo board.
 * Each room has at least one door.
 * @author Daniel Anastasi
 *
 */
public class Room {

	public final String NAME;
	private List<Weapon> weapons = null;
	private List<Door> doors = null;
	private List<Character> characters = null;
	private int[] weaponPositionsX,  weaponPositionsY; //for mapping placement of weapons in rooms.
	private int[] charPositionsX, charPositionsY; //for mapping placement of characters in rooms.
	private Room stairs = null;


	public Room(String name, int[] weaponX, int[] weaponY, int[] charX, int[] charY){
		this.NAME = name;
		this.weaponPositionsX = weaponX;
		this.weaponPositionsY = weaponY;
		this.charPositionsX = charX;
		this.charPositionsY = charY;
		this.weapons = new ArrayList<>();
		this.doors = new ArrayList<Door>();
		this.characters = new ArrayList<>();
	}

	public List<Weapon> getWeapons(){
		return this.weapons;
	}

	public List<Door> getDoors(){
		return this.doors;
	}

	public Room getStairs(){
		return this.stairs;
	}

	public void setStairs(Room stairs){
		if(this.stairs != null)
			return;
		this.stairs = stairs;
	}

	public List<Character> getCharacters(){
		return this.characters;
	}

	/**
	 * Returns the list of x coordinates for weapon placements.
	 * @return list of x coordinates for weapon placements.
	 */
	public int[] getWeaponPositionsX() {
		return weaponPositionsX;
	}

	/**
	 * Returns the list of y coordinates for weapon placements.
	 * @return list of y coordinates for weapon placements.
	 */
	public int[] getWeaponPositionsY() {
		return weaponPositionsY;
	}

	/**
	 * Returns the list of x coordinates for character placements.
	 * @return list of x coordinates for character placements.
	 */
	public int[] getCharPositionsX() {
		return charPositionsX;
	}

	/**
	 * Returns the list of y coordinates for character placements.
	 * @return list of y coordinates for character placements.
	 */
	public int[] getCharPositionsY() {
		return charPositionsY;
	}

	/**
	 * Adds a door to the list of doors into this room.
	 * @param A door into this room.
	 */
	public void addDoor(Door door){
		if(door == null)
			  throw new IllegalArgumentException("Parameter null.");
		this.doors.add(door);
	}

	/**
	 * Adds a weapon to the room.
	 * @param The weapon to add to the room.
	 */
	public void addWeapon(Weapon weapon){
		if(weapon == null)
			  throw new IllegalArgumentException("Parameter null.");
		this.weapons.add(weapon);
	}

	/**
	 * Adds a Character to the room.
	 * @param The character to add to the room.
	 */
	public void addCharacter(Character character){
		if(character == null)
			  throw new IllegalArgumentException("Parameter null.");


		this.characters.add(character);
	}

	/**
	 * Removes a weapon from the room.
	 * @param The weapon to remove from the room.
	 */
	public void removeWeapon(Weapon weapon){
		if(weapon == null)
			  throw new IllegalArgumentException("Parameter null.");

		if(!this.weapons.contains(weapon)){
			return;	//needs to be ok to not contain
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

	/**
	 * Returns the name of the room.
	 * @return The name of the room
	 */
	public String toString(){
		return NAME;
	}
}
