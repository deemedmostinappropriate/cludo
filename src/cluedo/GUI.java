package cluedo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
	public final int BUTTON_PANEL_HEIGHT = 40;
	/** X and Y coordinates for pop up menus.**/
	public final int POPUP_X = 200, POPUP_Y = 200;
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
	/** A popup menu**/
	private JPopupMenu popupMenu;
	
	
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
		//The popup menu
		this.popupMenu = new JPopupMenu(); 
		
		
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
	 * Displays a popup menu with radio buttons for the user to make a choice based on the contents of the elements parameter.
	 * @param A string label for the menu
	 * @param A list of objects to choose from.
	 * @return The index of the element chosen.
	 */
	public int radioButtonSelection(String label, List<Object> elements){
		int result = 0;
		this.popupMenu.setLabel(label);
		ActionListener al = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        result = e.getActionCommand().charAt(0);
		      }	
		    };
		
		
		for(Object o : elements){
			JRadioButtonMenuItem b = new JRadioButtonMenuItem(o.toString());	//Creates a radio button with the object as a string
			b.addActionListener(al);
			menu.add(b);	//adds the button to the menu.
		}
		this.popupMenu.show(this, POPUP_X, POPUP_Y);	//Displays the window
		
		return result;
	}

	/**
	 * Used to repaint JPanel component.
	 */
	public void draw(){
		this.canvas.repaint();
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
