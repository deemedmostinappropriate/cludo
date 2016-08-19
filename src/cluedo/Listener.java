package cluedo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import cluedo.locations.Board;

/**
 * The interface between the game model and GUI. Implements multiple listeners.
 * @author anastadani
 *
 */

public class Listener implements ActionListener, MouseMotionListener, MouseListener, KeyListener, WindowListener {
	/** The cluedo game**/
	private Game game;
	/** The application interface**/
	private GUI gui;

	public Listener(GUI gui, Game game){
		this.game = game;
		this.gui = gui;
	}

	/**
	 * Detects actions performed on components.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel p = null;
		// If continue button on dialog pressed
		if(e.getActionCommand().equals("Continue")){
			Component[] comps = this.gui.getDialog().getContentPane().getComponents();
			// gets the components that may have been used
			if(comps[0] instanceof JPanel){
				comps = ((JPanel)comps[0]).getComponents();
				// for combo box dialog
				if(comps[0] instanceof JComboBox){
					//sends messages to game.
					this.game.setCharacterSuggestionMesssage(gui.box1.getSelectedItem().toString());
					//not used in game.suggestion
					if(gui.box2.getSelectedItem()!= null)
						this.game.setRoomSuggestionMesssage(gui.box2.getSelectedItem().toString());
					this.game.setWeaponSuggestionMesssage(gui.box3.getSelectedItem().toString());
					//resets boxes' selected items
					gui.box1.setSelectedIndex(-1);
					gui.box2.setSelectedIndex(-1);
					gui.box3.setSelectedIndex(-1);
					this.game.setEventMessage("selectionDone");
					// we can close the window now that it has no use.
					this.gui.getDialog().dispose();
					return;
				}
			}
			else if(comps[0] instanceof JTextField){
				//do nothing. this is Character name input.
			}
			else if(comps.length > 1 && comps[1] instanceof JPanel){
				comps = ((JPanel)comps[1]).getComponents();
			}
			else
				return;	//for unnecessary action in the game.
			Component chosen = null;
			String event = null;

			for(int index = 0; index < comps.length; index ++){
				if(comps[index] instanceof JButton)
					continue;
				else if(comps[index] instanceof JRadioButtonMenuItem){
					//For radio button selection
					JRadioButtonMenuItem i = (JRadioButtonMenuItem) comps[index];
					if(i.isSelected()){
						chosen = i;		// this is the chosen button.
						event = i.getText();	// the text from the button.
						break;
					}
				}
				else if(comps[index] instanceof JTextField){
					//For text field event.
					chosen = comps[index];	//pacifies return condition
					event = ((JTextField)comps[index]).getText();
				}
			}
			// jlabels are the first component when working with dialogs without choices
			if(chosen == null && !(comps[0] instanceof JLabel))
				return;		// prevents closing of window until option has been chosen by user.
			// prevents exit without selection
			this.gui.getDialog().dispose();		// we can close the window now that it has no use.

			this.game.setEventMessage(event);		//passes a message to the game.
		}
		else if(e.getSource().equals(gui.exitItem)){
			//close application enquiry
			closeApplication();
		}
		else if(e.getActionCommand().equals("Make Accusation")){
			this.game.setEventMessage(((JButton)e.getSource()).getText());
			return;
		}


	}


	/**
	 * Dummy method for MouseMotionListener interface.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

	}


	@Override
	public void mouseMoved(MouseEvent e) {
		this.game.doToolTip(e.getX(),e.getY());
		
		if(e.getX() < game.CARD_X_ORIGIN && e.getY() < game.CARD_Y
				&& e.getX() > gui.WINDOW_WIDTH && e.getY() > gui.WINDOW_HEIGHT){
			System.err.println("here");
			game.drawHiddenHand(gui.getGraphics());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if(e.getSource() instanceof JButton){
			this.game.setMouseClickMessage(((JButton)e.getSource()).getText());
		}
		else{
				int x = e.getX(), y = e.getY();
				// Checks that the click is in the bounds of the die image.
				if(x > game.DIE_X && x < game.DIE_X + game.DIE_WIDTH
						&& y > game.DIE_Y && y < game.DIE_Y + game.DIE_HEIGHT){
					game.setMouseClickMessage("DIE");
					return;
				}
				else{
					//if in the range of the board itself
					if(x >= 0 && x < Board.BOARD_WIDTH && y >= 0 && y < Board.BOARD_HEIGHT){
						this.game.setEvent(e);
					}
				}
				return;
		}

		if(c.equals(this.gui.gameMenu)){
			((JMenuItem)c).setEnabled(true);	//makes the menu item be pressed.
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Changes the text in a label in the gui.
	 * @param The new text.
	 */
	public void changeLabel(String i){
		this.gui.instruction.setText(i);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("key typed");
		char c = e.getKeyChar();
		//
		switch(c){
		case 'w':
		case 'a':
		case 's':
		case 'd':
			System.out.printf("key message %c\n", c);
			this.game.setKeyMessage(c);	//if key is a movement key, send to game.
			return;
		default:
			return;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key pressed");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Closes the application.
	 */
	public void closeApplication(){
		int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", null, JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		closeApplication();

	}

	@Override
	public void windowClosed(WindowEvent e) {


	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
