import java.util.List;
import java.util.ArrayList;
/**
 * Rooms on the Cluedo board.
 * Each room has at least one door.
 * @author Daniel Anastasi
 *
 */
public class Room {

	/**
	 * Represents weapons placed in rooms, to distinguish from Game.WEAPON_CARD.
	 * @author Daniel Anastasi
	 */
	public enum WEAPON{
		CANDLESTICK, DAGGER, LEAD_PIPE, REVOLVER, ROPE, SPANNER;
	}
	
	public final String NAME;
	private List<Room.WEAPON> weapons = null;
	private List<Door> doors = null;
	

	public Room(String name){
		this.NAME = name;
		this.weapons = new ArrayList<>();
		this.doors = new ArrayList<Door>();
	}
	
	/**
	 * Returns a list of the weapons in this room.
	 * @return A list of the weapons in this room.
	 */
	public List<Room.WEAPON> getWeapons(){
		return this.weapons;
	}
	
	/**
	 * Returns a lists of this room's doors.
	 * @return A list of this room's doors.
	 */
	public List<Door> getDoors(){
		return this.doors;
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
	public void addWeapon(Room.WEAPON weapon){
		this.weapons.add(weapon);
	}
	
	/**
	 * Removes a weapon from the room.
	 * @param The weapon to remove from the room.
	 */
	public void removeWeapon(Room.WEAPON weapon){
		if(!this.weapons.contains(weapon)){
			throw new IllegalArgumentException("Room does not contain weapon.");
		}
		this.weapons.remove(weapon);
	}
}
