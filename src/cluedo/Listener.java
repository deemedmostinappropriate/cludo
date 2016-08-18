package cluedo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import cluedo.locations.Board;

/**
 * The interface between the game model and GUI
 * @author anastadani
 *
 */

public class Listener implements ActionListener, MouseMotionListener, MouseListener, KeyListener{
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
		else if(e.getActionCommand().equals("accusation")){
			System.out.println("acc button found");
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

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Component c = e.getComponent();
		if(c != null){
			if(c.equals(this.gui.accusation)){
				this.game.setEventMessage("accusation");
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
					Board b = this.game.getBoard();
					int xIndexBeforeDirection = (x)/(Board.SQ_WIDTH + 3);
					int yIndexBeforeDirection = 24 -(y - gui.canvas.getY())/(Board.SQ_HEIGHT + 3)-1;

					// checks that it is in the bounds of a door in the game's door array
					if(//if in the range of the board itself
							x >= 0 && x < Board.BOARD_WIDTH
							&& y >= 0 && y < Board.BOARD_HEIGHT)
						//is a door in the board array
						if(b.getBoard()[yIndexBeforeDirection][xIndexBeforeDirection] == 2){
							if(b.getDoor(xIndexBeforeDirection, yIndexBeforeDirection +1) != null){
								this.game.setMouseClickMessage("up");		//door is up relative to clicked square
							}
							else if(b.getDoor(xIndexBeforeDirection, yIndexBeforeDirection -1) != null){
								this.game.setMouseClickMessage("down");		//door is down relative to clicked square
							}
							else if(b.getDoor(xIndexBeforeDirection -1, yIndexBeforeDirection) != null){
								this.game.setMouseClickMessage("left");		//door is left relative to clicked square
							}
							else if(b.getDoor(xIndexBeforeDirection +1, yIndexBeforeDirection) != null){
								this.game.setMouseClickMessage("right");		//door is right relative to clicked square
							}
						}
						else if (b.getBoard()[yIndexBeforeDirection][xIndexBeforeDirection] == 3){
							this.game.setMouseClickMessage("stairs");		// stairs are relative to clicked square
						}
					System.out.println("got there");
				}
				return;
			}
		}
		if(c.equals(this.gui.fileItem)){
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

}
