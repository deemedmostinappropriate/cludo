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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import cluedo.pieces.Card;

/**
 * A graphical user interface.
 * This example uses JMenuBar, JPanel, JButton, JTextField, JRadioButton, and JDialog components.
 * Also uses a mouse listener.
 *
 * @author Daniel Anastasi
 *
 */

public class GUI extends JFrame{
	/** X and Y coordinates for pop up menus.**/
	public final int POPUP_X = 600, POPUP_Y = 300;
	public final int POPUP_WIDTH = 400, POPUP_HEIGHT = 400;

	/** An event listener**/
	private Listener listener = null;

	/** A popup menu**/
	private JDialog dialog = null;
	/** Combo boxes for dialogs.**/
	public JComboBox box1, box2, box3;
	/** Offset for JFrame border **/
	public final int BORDER_OFFSET = 4;
	/** Window width and height. **/
	public final int WINDOW_WIDTH = 651 + BORDER_OFFSET, WINDOW_HEIGHT = 920;	//once 715
	/** Height of the menu. **/
	public final int MENU_HEIGHT = 25;
	/** Height of the button panel. **/
	public final int BUTTON_PANEL_HEIGHT = 40;
	/** The Canvas for the game**/
	public final Canvas canvas;
	/** The menu **/
	public final JMenuBar menu;
	/** File button for the menu**/
	public final JMenuItem fileItem;
	/** Game button for the menu**/
	public final JMenuItem gameItem;
	/** Button to switch to next turn.**/
	public final JButton nextTurn;
	/** Button to roll dice.**/
	public final JButton accusation;
	/** A button to close a jdialog. **/
	public final JButton continueButton;
	/** A label to give instructions to the player.**/
	public final JLabel instruction;
	/** A panel to organise the buttons**/
	public final JPanel buttonPanel;

	/** The size of text drawn.**/
	private final float TEXT_SIZE = 20;

	public GUI(String appName, Game game){
		super(appName);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		//Sets up menu
		this.menu = new JMenuBar();
		this.menu.setLayout(new FlowLayout(FlowLayout.LEFT));	//aligns the items to the left
		this.fileItem = new JMenuItem("File");
		this.fileItem.addMouseListener(this.listener);

		this.gameItem = new JMenuItem("Game");
		this.gameItem.addMouseListener(this.listener);

		this.menu.add(this.fileItem);
		this.menu.add(this.gameItem);
		//Jbuttons
		this.nextTurn = new JButton("Next Turn");
		this.accusation = new JButton("Make Accusation");
		this.instruction = new JLabel("Loading Game");
		this.buttonPanel = new JPanel();			//holds our buttons
		this.buttonPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, BUTTON_PANEL_HEIGHT));
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.buttonPanel.add(this.accusation);
		this.buttonPanel.add(this.nextTurn);
		this.buttonPanel.add(this.instruction);

		this.canvas = new Canvas(game);
		this.canvas.setBackground(Color.BLACK);	//provides a black background
		this.canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.menu.setPreferredSize(new Dimension(WINDOW_WIDTH, MENU_HEIGHT));
		add(this.menu, BorderLayout.PAGE_START);
		add(this.canvas, BorderLayout.CENTER);
		add(this.buttonPanel, BorderLayout.PAGE_END);

		//additional components for dialogs
		this.continueButton = new JButton("Continue");
		this.box1 = new JComboBox();
		this.box2 = new JComboBox();
		this.box3 = new JComboBox();

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);				//Display the window.
	}

	/**
	 * Returns the dialog component.
	 * @return A JDialog component.
	 */
	public JDialog getDialog(){
		return this.dialog;
	}

	/**
	 * Sets the event listener.
	 * @param An event listener.
	 */
	public void setListener(Listener l){
		if(this.listener != null)
			return;
		this.listener = l;
		this.canvas.addMouseListener(l);	//adds the listener to the canvas
		this.canvas.setFocusable(true);		//adds focus to canvas
		this.canvas.addKeyListener(l);
		this.nextTurn.addMouseListener(this.listener); 	//adds a mouse listener to the button
		this.accusation.addMouseListener(this.listener);	//adds a mouse listener to the button
		this.continueButton.addActionListener(listener);
	}

	/**
	 * Adds arrays of values to the GUI's three JComboBoxes, and sets titles for the boxes.
	 * @param An array of T objects for box1
	 * @param An array of T objects for box2
	 * @param An array of T objects for box3
	 * @param A String title for box1
	 * @param A String title for box2
	 * @param A String title for box3
	 * @param <T>
	 */
	public <T> void setComboBoxItems(List<T> list1, List<T> list2, List<T> list3, String title1, String title2, String title3){
		prepBox(this.box1, list1, title1);
		prepBox(this.box2, list2, title2);
		prepBox(this.box3, list3, title3);
	}

	/**
	 * Adds all values in the list to the box, and sets the boxes title.
	 * @param The JComoBox.
	 * @param The list of items to add to the box.
	 * @param The String title for the box.
	 */
	private <T> void prepBox(JComboBox box, List<T> list, String title){
		//adds all items in the list
		for(T t : list)
			box.addItem(t);
		//sets additional parameters for box.
		box.setRenderer(new ComboBoxRenderer(title));	//allows box to have title
		box.setSelectedIndex(-1);						//makes box point to title
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
		this.dialog = basicDialog(label);
		ButtonGroup bg = new ButtonGroup();	//organises the buttons so that no two can be selected at the same time.
		JPanel panel = new JPanel();		//holds the buttons
		panel.setLayout(new GridLayout(4,1));
		this.dialog.add(panel, BorderLayout.WEST);

		// Adds buttons to the panel and to the ButtonGroup
		for(Object o : elements){
			JRadioButtonMenuItem but = new JRadioButtonMenuItem(o.toString());
			but.addActionListener(listener);
			bg.add(but);		//adds button to the group.
			panel.add(but);		//adds button to the panel.
		}
		this.dialog.add(this.continueButton, BorderLayout.SOUTH);	//adds the continue button at the bottom of the dialog.
		this.dialog.pack();
	}

	/**
	 * Displays a dialog with a number of comboboxes equal to the number of lists passed in. Labels are also added to mark the boxes.
	 * @param The title of the dialog.
	 * @param A varying number of lists of objects.
	 */
	public <T> void comboBoxSelection(String label ){
		//this.popupMenu.setLabel(label);		//Sets a new label for the menu
		this.dialog = basicDialog(label);
		this.dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));	//panel to hold labels and combo boxes.
		// adds boxes to panel.
		panel.add(this.box1);
		panel.add(this.box2);
		panel.add(this.box3);

		this.dialog.add(panel, BorderLayout.CENTER);
		this.dialog.add(this.continueButton, BorderLayout.SOUTH);	//adds the continue button at the bottom of the dialog.
		this.dialog.pack();
	}

	/**
	 * Creates a JDialog window with a text field.
	 * @param string
	 */
	public void popUpTextQuery(String string) {
		this.dialog = basicDialog(string);			// dialog window for text field
		JTextField tf = new JTextField();			// text field
		this.dialog.add(tf, BorderLayout.NORTH);	//adds the text field to the window
		this.dialog.add(this.continueButton, BorderLayout.SOUTH);	//adds the continue button to the window
		this.dialog.pack();
	}

	/**
	 * Returns a JDialog window.
	 * @param The title for the window.
	 * @return The JDialog
	 */
	public JDialog basicDialog(String title){
		JDialog d = new JDialog();
		d.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);	//prevents closing with close button
		d.setLocation(this.POPUP_X, this.POPUP_Y);
		d.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
		d.setVisible(true);					// window can be seen
		d.setLayout(new BorderLayout());	// A default layout
		d.setTitle(title);				//The title in the border of the window
		return d;
	}

	/**
	 * Used to repaint JPanel component.
	 */
	public void draw(){
		this.canvas.repaint();
	}

	/**
	 * Allows JComboBoxes to have a default title.
	 * @author anastadani
	 *
	 */
	private class ComboBoxRenderer extends JLabel implements ListCellRenderer{

		private String title;

        public ComboBoxRenderer(String title)
        {
            this.title = title;
        }

        /**
         * Sets the component to
         */
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			 if (index == -1 && value == null)
				 setText(title);
	         else
	        	 setText(value.toString());
	            return this;
		}

	}

}
