package cluedo.pieces;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cluedo.locations.Board;
import cluedo.locations.Room;

/**
 * A character piece on the board.
 * @author Daniel Anastasi
 *
 */
public class Character implements Piece{
	/** The image of this piece. **/
	private BufferedImage image;
	/** The xy coordinates and the real positions on the board. **/
	private int x = 0, y = 0, realX = 0, realY = 0;
	/** The character's name. **/
	public final String NAME;
	/** The room which this piece is in.**/
	private Room room = null;
	/** An abbreviation for the character's name. **/
	public final String ABBREV;

	public Character(int x, int y, String name, String abbreviation, BufferedImage image){
		this.x = x;
		this.y = y;
		this.NAME = name;
		ABBREV = abbreviation;
		this.image = image;
		this.realX = x * Board.SQ_WIDTH + (Board.PIECE_OFFSET * (x + 1));
		this.realY = Board.BOARD_HEIGHT - ((y + 1) * Board.SQ_HEIGHT + (Board.PIECE_OFFSET * (y +1)));
	}

	/**
	 * Returns the room which the character is in. This can be null.
	 * @return The room in which the player is standing.
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Returns the x coordinate that this piece is at.
	 * @return X coordinate as an integer.
	 */
	public int getX(){
		return this.x;
	}

	/**
	 * Returns the y coordinate that this piece is at.
	 * @return Y coordinate as an integer.
	 */
	public int getY(){
		return this.y;
	}

	/**
	 * Sets the x coordinate of this character
	 * @param The new x coordinate
	 */
	public void setX(int x) {
		this.x = x;
		this.realX =  x * Board.SQ_WIDTH + (Board.PIECE_OFFSET * (x + 1));
	}

	/**
	 * Sets the y coordinate of this character
	 * @param The new y coordinate
	 */
	public void setY(int y) {
		this.y = y;
		this.realY = Board.BOARD_HEIGHT - ((y + 1) * Board.SQ_HEIGHT + (Board.PIECE_OFFSET * (y +1)));
	}

	/**
	 * Sets new integer values for the x and y coordinates.
	 * @param New x coordinate.
	 * @param New y coordinate.
	 */
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the character's room to the room provided.
	 * @param room
	 */
	public void setRoom(Room room){
		this.room = room;
	}

	/**
	 * Draws the character piece.
	 * @param The graphics object
	 */
	public void draw(Graphics g){
		g.drawImage(this.image, realX, realY, Board.SQ_WIDTH, Board.SQ_HEIGHT, null);
	}

	/**
	 * Returns the name of the character.
	 * @return The name of the character
	 */
	public String toString(){
		return NAME;
	}
}
