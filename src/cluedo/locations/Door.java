package cluedo.locations;

/**
 * Used to identify the square outside a Room. The door has a direction relating it to the position of the room.
 * @author Daniel Anastasi
 *
 */
public class Door{

	private Room room;						// Room must be set after object construction: Cannot be final.
	public final String ROOM_DIRECTION;		// The direction of Room relative to this Door.
	private int x = 0, y = 0;
	
	public Door(String direction){
		ROOM_DIRECTION = direction;
	}
	
	
	/**
	 * Get the x coordinate of this door
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the y coordinate of this door.
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	
	/**
	 * Set the x coordinate of this door
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 *  Set the y coordinate of this door
	 */
	public void setY(int y) {
		this.y = y;
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
