package cluedo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

/**
 * A graphical user interface. 
 * This example uses JMenuBar, JPanel, JButton, JTextField, JRadioButton, and JDialog components.
 * Also uses a mouse listener.
 * 
 * @author Daniel Anastasi
 *
 */

public class GUI extends JFrame{
	/** Window width and height. **/
	public final int WINDOW_WIDTH = 715, WINDOW_HEIGHT = 900;
	/** Height of the menu. **/
	public final int MENU_HEIGHT = 25;
	/** Height of the button panel. **/
	public final int BUTTON_PANEL_HEIGHT = 30;
	/** The Canvas for the game**/
	private Canvas canvas;
	/** The menu **/
	private JMenuBar menu;
	/** File button for the menu**/
	private JMenuItem fileItem;
	/** Game button for the menu**/
	private JMenuItem gameItem;
	/** Button to switch to next turn.**/
	private JButton nextTurn;
	/** Button to roll dice.**/
	private JButton rollDice;
	/** A panel to organis the buttons**/
	private JPanel buttonPanel;
	
	
	/** The size of text drawn.**/
	private final float TEXT_SIZE = 20;
	
	public GUI(String appName){
		super(appName);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLayout(new BorderLayout());
		//Sets up menu
		this.menu = new JMenuBar();
		this.menu.setLayout(new FlowLayout(FlowLayout.LEFT));	//aligns the items to the left
		this.fileItem = new JMenuItem("File");	
		this.gameItem = new JMenuItem("Game");
		this.menu.add(this.fileItem);
		this.menu.add(this.gameItem);
		//Jbuttons
		this.nextTurn = new JButton("Next Turn");
		this.rollDice = new JButton("Roll Dice");
		this.buttonPanel = new JPanel();			//holds our buttons
		this.buttonPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, BUTTON_PANEL_HEIGHT));
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.buttonPanel.add(this.rollDice);
		this.buttonPanel.add(this.nextTurn);
		
		//getContentPane().
		this.canvas = new Canvas();
		this.canvas.setBackground(Color.BLACK);	//provides a black background
		this.canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.menu.setPreferredSize(new Dimension(WINDOW_WIDTH, MENU_HEIGHT));
		add(this.menu, BorderLayout.PAGE_START);
		add(this.canvas, BorderLayout.CENTER);
		add(this.buttonPanel, BorderLayout.PAGE_END);
		
		

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);				//Display the window.
	}



	/**
	 * Provides the Canvas with a Game object for drawing.
	 * @param The game object.
	 */
	public void setGame(Game game){
		if(this.canvas != null)
			this.canvas.setGame(game);
	}


}
