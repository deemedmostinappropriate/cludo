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
	/** The name of the room. **/
	public final String NAME;
	/** A list of weapons in this room. **/
	private List<Weapon> weapons = null;
	/** The doors leading into this room. **/
	private List<Door> doors = null;
	/** The character pieces in this room. **/
	private List<Character> characters = null;
	/** Arrays for x and y positions for weapon placement in this room. **/
	private int[] weaponPositionsX,  weaponPositionsY;
	/** Arrays for x and y positions for character placement in this room.  **/
	private int[] charPositionsX, charPositionsY;
	/** The stairs in this room, if any. **/
	private Room stairs = null;

	/**
	 * @param The name of the room.
	 * @param X coordinates for weapon placements in this room.
	 * @param Y coordinates for weapon placements in this room.
	 * @param X coordinates for character placements in this room.
	 * @param Y coordinates for character placements in this room.
	 */
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

	/**
	 * Returns a list of the weapons in this room.
	 * @return A list of the weapons in this room.
	 */
	public List<Weapon> getWeapons(){
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
	 * Returns the room associated with the stairs in this room, if there are any.
	 * @return The room from the stairs.
	 */
	public Room getStairs(){
		return this.stairs;
	}

	/**
	 * Sets the stairs for this room.
	 * @param stairs
	 */
	public void setStairs(Room stairs){
		if(this.stairs != null)
			return;
		this.stairs = stairs;
	}

	/**
	 * Returns the list of this room's characters.
	 * @return A list of this room's characters.
	 */
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
			throw new IllegalArgumentException("Argument is null.");
		this.doors.add(door);
	}

	/**
	 * Adds a weapon to the room.
	 * @param The weapon to add to the room.
	 */
	public void addWeapon(Weapon weapon){
		if(weapon == null)
			throw new IllegalArgumentException("Argument is null.");

		this.weapons.add(weapon);
	}

	/**
	 * Adds a Character to the room.
	 * @param The character to add to the room.
	 */
	public void addCharacter(Character character){
		if(character == null)
			throw new IllegalArgumentException("Argument is null.");


		this.characters.add(character);
	}


	/**
	 * Removes a weapon from the room.
	 * @param The weapon to remove from the room.
	 */
	public void removeWeapon(Weapon weapon){
		if(weapon == null)
			throw new IllegalArgumentException("Argument is null.");

		if(!this.weapons.contains(weapon)){
			throw new IllegalArgumentException("Argument is not contained in this Room.");
		}
		this.weapons.remove(weapon);
	}

	/**
	 * Removes a character from the room.
	 * @param The character to remove from the room.
	 */
	public void removeCharacter(Character character){
		if(character == null)
			throw new IllegalArgumentException("Argument is null.");

		if(!this.characters.contains(character)){
			throw new IllegalArgumentException("Room does not contain character.");
		}
		this.characters.remove(character);
	}
}
