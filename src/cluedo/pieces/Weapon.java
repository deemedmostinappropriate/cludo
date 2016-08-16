package cluedo.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cluedo.locations.Board;

/**
 * A weapon piece on the board.
 * @author Daniel Anastasi
 *
 */
public class Weapon implements Piece{

	/**
	 * The names of the potential murder weapons.
	 * @author Daniel Anastasi
	 */
	public enum Name{
		ROPE, DAGGER, CANDLESTICK, REVOLVER, LEADPIPE, SPANNER;
	}

	/** The image for the board. **/
	private BufferedImage image = null;
	/** The x and y position of this Weapon in relation to squares on the board.**/
	private int x = 0, y = 0;
	/** X and Y for drawing the piece. **/
	private int realX = 0, realY = 0;
	/** The name of the weapon**/
	public final Name name;


	public Weapon(Name name, BufferedImage image){
		this.name = name;
		this.image = image;
	}




	/**
	 * Returns the x coordinate of this piece on the board.
	 * @return The x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of this piece on the board.
	 * @return The y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the x coordinate of this piece on the board.
	 * Also sets the real value for drawing.
	 * @param The x value.
	 */
	public void setX(int x) {
		this.x = x;
		this.realX =  x * Board.SQ_WIDTH + (Board.PIECE_OFFSET * (x + 1));
	}


	/**
	 * Sets the y coordinate of this piece on the board.
	 * Also sets the real values for drawing.
	 * @param The y value.
	 */
	public void setY(int y) {
		this.y = y;
		this.realY = Board.BOARD_HEIGHT - ((y + 1) * Board.SQ_HEIGHT + (Board.PIECE_OFFSET * (y +1)));
	}

	/**
	 * Sets the image for this object to draw.
	 * @param The weapon's image
	 */
	public void setImage(BufferedImage image){
		if(this.image != null)
			this.image = image;
	}

	/**
	 * Draws this weapon to the canvas.
	 */
	public void draw(Graphics g){
		g.drawImage(this.image, realX, realY, Board.SQ_WIDTH, Board.SQ_HEIGHT, null);
	}

	/**
	 * Returns the name of the weapon.
	 * @return The name of the weapon
	 */
	public String ToString(){
		return this.name.toString();
	}


}