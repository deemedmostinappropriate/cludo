
/**
 * Used to identify the square outside a Room. The door has a direction relating it to the position of the room.
 * @author Daniel Anastasi
 *
 */
public class Door{

	private Room room;					//Room must be set after object construction: Cannot be final.
	public final String ROOM_DIRECTION;		//The direction of Room relative to this Door.
	
	public Door(String direction){
		ROOM_DIRECTION = direction;
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
