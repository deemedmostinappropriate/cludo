package cluedo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

/**
 * The interface between the game model and GUI
 * @author anastadani
 *
 */

public class Listener implements ActionListener, MouseMotionListener{
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
				p = (JPanel)comps[0];
				comps = p.getComponents();
			}
			else if(comps.length > 1 && comps[1] instanceof JPanel){
				p = (JPanel)comps[1];
				comps = p.getComponents();
			}
			Component chosen = null;
			String event = null;

			for(Component b : comps){
				if(b instanceof JButton)
					continue;
				else if(b instanceof JRadioButtonMenuItem){
					JRadioButtonMenuItem i = (JRadioButtonMenuItem) b;
					if(i.isSelected()){
						chosen = i;		// this is the chosen button.
						event = i.getText();	// the text from the button.
						break;
					}
				}
				else if(b instanceof JTextField){
					chosen = b;	//pacifies return condition
					event = ((JTextField)b).getText();
				}
			}
			if(chosen == null)
				return;						// prevents exit without selection
			this.gui.getDialog().dispose();		// we can close the window now that it has no use.
			this.game.setEventMessage(event);		//passes a message to the game.
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



}
