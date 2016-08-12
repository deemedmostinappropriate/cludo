package cluedo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class Canvas extends JPanel implements ActionListener{
	/** X and Y coordinates for pop up menus.**/
	public final int POPUP_X = 200, POPUP_Y = 200;
	/** A popup menu**/
	private JPopupMenu popupMenu;


	/** The image for double buffering **/
	private Image dbImage = null;
	/** Graphics object for double buffering **/
	private Graphics dbg = null;
	/** The Game object. **/
	private Game game = null;

	public Canvas(){
		//The popup menu
		this.popupMenu = new JPopupMenu();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.game.draw(g);	// all other drawing follows from this call.
	}

	public void paint (Graphics g){
		dbImage = createImage(getWidth(), getHeight());	// our screen image
		dbg = dbImage.getGraphics();
		paintComponent(dbg);		//force call to paintComponent.
		g.drawImage(dbImage, 0, 0, this);
	}

	/**
	 * Provides the Panel the only reference it should need to draw the game.
	 * @param The game object.
	 */
	public void setGame(Game game){
		this.game = game;
	}

	/**
	 * Displays a popup menu with radio buttons for the user to make a choice based on the contents of the elements parameter.
	 * @param A string label for the menu
	 * @param A list of objects to choose from.
	 * @return The index of the element chosen.
	 */
	public int radioButtonSelection(String label, List<Object> elements){
		int result = 0;
		this.popupMenu.setLabel(label);		//Sets a new label for the menu

		for(Object o : elements){
			JRadioButtonMenuItem b = new JRadioButtonMenuItem(o.toString());	//Creates a radio button with the object as a string
			b.addActionListener(this);	//Gives all buttons an action listener.
			this.popupMenu.add(b);				//adds the button to the menu.
		}
		this.popupMenu.show(this, POPUP_X, POPUP_Y);	//Displays the window

		//trying jdialog
		JDialog j = new JDialog();
		//j.setVisible(true);
		j.setTitle(label);




		return result;
	}

	/**
	 * Detects actions performed on components.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.popupMenu))
			this.game.setEventMessage(Integer.getInteger(e.getActionCommand()));		//passes a message to the game.
	}

}
