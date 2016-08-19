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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

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
