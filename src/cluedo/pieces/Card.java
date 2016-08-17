package cluedo.pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents cards in the player's hands, and the cards in the solution.
 * @author Daniel Anastasi
 *
 */
public class Card{
	/**
	 * The six weapon cards.
	 * @author Daniel Anastasi
	 *
	 */
	private BufferedImage image;
	private String name;

	public Card(String name, BufferedImage image){
		this.image = image;
		this.name = name;
	}

	/**
	 * Returns this card's image.
	 * @return
	 */
	public BufferedImage getImage(){
		return this.image;
	}

	/**
	 * Draws the card on the GUI.
	 * @param The Graphics object
	 * @param X coordinate
	 * @param Y coordinate
	 * @param Width of card
	 * @param Height of card
	 */
	public void draw(Graphics g, int x, int y, int width, int height){
		g.drawImage(this.image, x, y, width, height, null);
	}

	/** Returns the name of the card. **/
	public String toString(){
		return this.name;
	}

	public enum WEAPON{
		ROPE, KNIFE, CANDLESTICK, REVOLVER, LEADPIPE, WRENCH;
	}

	/**
	 * The nine room cards.
	 * @author Daniel Anastasi
	 *
	 */
	public enum ROOM{
		LOUNGE, DINING_ROOM, KITCHEN, BALL_ROOM, CONSERVATORY, BILLIARD_ROOM, LIBRARY, STUDY, HALL;
	}

	/**
	 * The six character cards.
	 * @author Daniel Anastasi
	 *
	 */
	public enum CHARACTER{
		MISS_SCARLETT, COL_MUSTARD, MRS_WHITE, MR_GREEN, MRS_PEACOCK, PROF_PLUM;
	}

}
