/**
 * Represents cards in the player's hands, and the cards in the solution.
 * @author Daniel Anastasi
 *
 */
public interface Card{
	/**
	 * The six weapon cards.
	 * @author Daniel Anastasi
	 *
	 */
	public enum WEAPON implements Card{
		ROPE, DAGGER, CANDLESTICK, REVOLVER, LEADPIPE, SPANNER;
	}
	
	/**
	 * The nine room cards.
	 * @author Daniel Anastasi
	 *
	 */
	public enum ROOM implements Card{
		LOUNGE, DINING, KITCHEN, BALL, CONSERVATORY, BILLIARD, LIBRARY, STUDY, HALL;		
	}
	
	/**
	 * The six character cards.
	 * @author Daniel Anastasi
	 *
	 */
	public enum CHARACTER implements Card{
		MISS_SCARLET, COL_MUSTARD, MRS_WHITE, MR_GREEN, MRS_PEACOCK, PROF_PLUM;
	}
	
	
	
}
