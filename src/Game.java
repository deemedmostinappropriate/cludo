import java.io.IOException;
import java.util.Scanner;

//package assignment1.cluedo;

public class Game {
	
	static class Card{
		public enum WEAPON{
			ROPE, DAGGER, CANDLESTICK, REVOLVER, LEADPIPE, SPANNER;
		}
		public enum ROOM{
			HALL, DINING, BILLIARD, CONSERVATORY, BALL, LIBRARY, STUDY, LOUNGE, KITCHEN;
		}
		public enum CHAR{
			SCARLET, MUSTARD, WHITE, PLUM, GREEN, PEACOCK;
		}
	}

	private int numPlayers;
	private Board board;

	public Game(int numPlayers){
		this.numPlayers = numPlayers;
		//Set up players
		this.board = new Board();//Set up board
		run();
	}

	
	/**
	 * Runs the game loop.
	 */
	public void run(){
		// Array input test
		int[][] b = this.board.getBoard();
		for(int i = 0; i < b.length; i++){
			for(int j = 0; j < b[0].length; j++){
				System.out.print(b[i][j]);
			}
			System.out.println();
		}
	}
	

	public static void main(String[] args){
		int players = 0;
		Scanner s = new Scanner(System.in);;
		System.out.println("Welcome to Cluedo");
		System.out.println("How many people are playing? (enter a number between 3 and 6):");
		// Makes sure the number of players is in the range of 3-6.
		while(players < 3 || players > 6){
			players = s.nextInt();
			if(players < 3 || players > 6){
				players = 0;
				System.out.println("Please enter a number between 3 and 6:");
			}
		}

		s.close();				// closes the scanner.
		new Game(players);
	}


}

