
/**
 * Identical to superclass, aside from room field.
 * Used to identify the entry of a Room.
 * @author Daniel Anastasi
 *
 */
public class Door extends Square{

	private Room room = null;
	
	public Door(boolean top, boolean right, boolean bottom, boolean left){
		super(top, right, bottom, left);
	}
	
	/**
	 * Returns this Door's Room.
	 * @return This Door's Room.
	 */
	public Room getRoom(){
		return this.room;
	}
	
	/**
	 * Sets the room which this door provides access to.
	 * @param The Room for the Door.
	 */
	public void setRoom(Room room){
		if(this.room != null)	//imitates final field.
			return;
		else
			this.room = room;
	}
	
}
