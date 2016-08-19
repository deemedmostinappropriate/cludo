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

	public Room getRoom() {
		return room;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
		this.realX =  x * Board.SQ_WIDTH + (Board.PIECE_OFFSET * (x + 1));
	}

	public void setY(int y) {
		this.y = y;
		this.realY = Board.BOARD_HEIGHT - ((y + 1) * Board.SQ_HEIGHT + (Board.PIECE_OFFSET * (y +1)));
	}

	public void setPosition(int x, int y){
		if(!Board.inRange(x,y))
			throw new IllegalArgumentException("Position out of range of the board.");
		this.x = x;
		this.y = y;
	}

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
