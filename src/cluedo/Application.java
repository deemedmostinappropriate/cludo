package cluedo;
/**
 * Where the application starts.
 * @author anastadani
 *
 */
public class Application {

	public static void main(String[] args){
		newGame( new Application());
	}

	/**
	 * Creates a new game.
	 */
	public static void newGame(Application app){
		new Game(app);
	}
}
