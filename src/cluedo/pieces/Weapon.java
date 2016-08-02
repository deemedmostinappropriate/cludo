package cluedo.pieces;
/**
 * A weapon piece on the board.
 * @author Daniel Anastasi
 *
 */
public enum Weapon implements Piece{
		CANDLESTICK,
		DAGGER,
		LEAD_PIPE,
		REVOLVER,
		ROPE,
		SPANNER;
	
	private int x = 0, y = 0;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

}