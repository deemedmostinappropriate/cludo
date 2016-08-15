package cluedo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class Canvas extends JPanel{
	/** The image for double buffering **/
	private Image dbImage = null;
	/** Graphics object for double buffering **/
	private Graphics dbg = null;
	/** The Game object. **/
	private Game game = null;

	public Canvas(Game game){
		super();
		this.game = game;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.game.draw(g);	// all other drawing follows from this call.
	}		// Adds all cards to the list.

	public void paint (Graphics g){
		dbImage = createImage(getWidth(), getHeight());	// our screen image
		dbg = dbImage.getGraphics();
		paintComponent(dbg);		//force call to paintComponent.
		g.drawImage(dbImage, 0, 0, this);
	}

}
