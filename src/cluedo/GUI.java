package cluedo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * A graphical user interface.
 * This example uses JMenuBar, JPanel, JButton, JTextField, JRadioButton, and JDialog components.
 * Also uses a mouse listener.
 *
 * @author Daniel Anastasi
 *
 */

public class GUI extends JFrame implements ActionListener, PopupMenuListener{
	/** X and Y coordinates for pop up menus.**/
	public final int POPUP_X = 200, POPUP_Y = 200;
	public final int POPUP_WIDTH = 400, POPUP_HEIGHT = 400;
	/** A popup menu**/
	private JDialog dialog;

	/** Window width and height. **/
	public final int WINDOW_WIDTH = 715, WINDOW_HEIGHT = 900;
	/** Height of the menu. **/
	public final int MENU_HEIGHT = 25;
	/** Height of the button panel. **/
	public final int BUTTON_PANEL_HEIGHT = 40;
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



	/** The Game object. **/
	private Game game = null;



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

		this.canvas = new Canvas();
		this.canvas.setBackground(Color.BLACK);	//provides a black background
		this.canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.menu.setPreferredSize(new Dimension(WINDOW_WIDTH, MENU_HEIGHT));
		add(this.menu, BorderLayout.PAGE_START);
		add(this.canvas, BorderLayout.CENTER);
		add(this.buttonPanel, BorderLayout.PAGE_END);

		//The popup menu
		this.dialog = new JDialog();
		this.dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);				//Display the window.
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
		this.game = game;
	}


	/**
	 * Displays a dialog with radio buttons for the user to make a choice based on the contents of the elements parameter.
	 * The dialog contains a panel arrange its contents.
	 * The buttons are grouped with a ButtonGroup.
	 * @param A string label for the menu
	 * @param A list of objects to choose from.
	 */
	public void radioButtonSelection(String label, List<Object> elements){
		//this.popupMenu.setLabel(label);		//Sets a new label for the menu
		this.dialog.add(new JLabel(label));
		this.dialog.setLocation(this.POPUP_X, this.POPUP_Y);
		this.dialog.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
		this.dialog.setLayout(new BorderLayout());
		this.dialog.setTitle(label);
		ButtonGroup bg = new ButtonGroup();	//organises the buttons so that no two can be selected at the same time.
		JPanel panel = new JPanel();		//holds the buttons
		panel.setLayout(new GridLayout(4,1));
		this.dialog.add(panel, BorderLayout.WEST);

		// Adds buttons to the panel and to the ButtonGroup
		for(Object o : elements){
			JRadioButtonMenuItem but = new JRadioButtonMenuItem(o.toString());
			but.addActionListener(this);
			bg.add(but);		//adds button to the group.
			panel.add(but);		//adds button to the panel.
		}
		JButton ok = new JButton("Continue");
		ok.addActionListener(this);

		this.dialog.add(ok, BorderLayout.SOUTH);	//adds the continue button at the bottom of the dialog.
		this.dialog.pack();
		this.dialog.setVisible(true);
	}

	/**
	 * Detects actions performed on components.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel p = null;
		// If continue button on dialog pressed
		if(e.getActionCommand().equals("Continue")){
			Component[] comps = this.dialog.getContentPane().getComponents();
			p = (JPanel)comps[1];
			Component chosen = null;
			String event = null;

			for(Component b : p.getComponents()){
				if(b instanceof JButton)
					continue;
				JRadioButtonMenuItem i = (JRadioButtonMenuItem) b;
				if(i.isSelected()){
					chosen = i;		// this is the chosen button.
					event = i.getText();	// the text from the button.
					break;
				}
			}
			if(chosen == null)
				return;						// prevents exit without selection
			this.dialog.dispose();		// we can close the window now that it has no use.
			this.game.setEventMessage(event);		//passes a message to the game.
		}
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		// TODO Auto-generated method stub

	}

}
