package cluedo;


import java.awt.BorderLayout;

/**
 * This GUI will contain a single JPanel.
 * @author Daniel Anastasi
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

public class GUI extends JFrame{
	/** The Canvas for the game**/
	private JPanel canvas;
	/** Width and height of the frame. **/
	public final int WINDOW_WIDTH, WINDOW_HEIGHT, MENU_HEIGHT;
	/** The size of text drawn.**/
	private final float TEXT_SIZE = 20;
	/** The image for double buffering **/
	private Image dbImage = null;
	/** Graphics object for double buffering **/
	private Graphics dbg = null;
	/** The menu **/
	private JMenuBar menu;
	/** File button for the menu**/
	private JMenuItem fileItem;
	/** Game button for the menu**/
	private JMenuItem gameItem;
	/** The game object**/
	private Game game;

	public GUI(String appName){
		super(appName);
		WINDOW_WIDTH = 1080;
		WINDOW_HEIGHT = 720;
		MENU_HEIGHT = 25;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLayout(new BorderLayout());
		//Sets up menu
		this.menu = new JMenuBar();
		this.menu.setLayout(new FlowLayout(FlowLayout.LEFT));	//aligns the items to the left
		this.fileItem = new JMenuItem("File");	
		this.gameItem = new JMenuItem("Game");
		this.menu.add(this.fileItem);
		this.menu.add(this.gameItem);
		
		//getContentPane().
		this.canvas = new JPanel(){

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				game.draw(g);	// all other drawing follows from this call.
			}

			public void paint (Graphics g){
				dbImage = createImage(getWidth(), getHeight());	// our screen image
				dbg = dbImage.getGraphics();
				paintComponent(dbg);		//force call to paintComponent.
				g.drawImage(dbImage, 0, 0, this);
			}

		};
		this.canvas.setBackground(Color.BLACK);	//provides a black background
		this.canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.menu.setPreferredSize(new Dimension(WINDOW_WIDTH, MENU_HEIGHT));
		add(menu, BorderLayout.PAGE_START);
		add(canvas, BorderLayout.CENTER);
		//third component uses BorderLayout.PAGE_END
		
		

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);				//Display the window.
	}



	/**
	 * Provides the gui the only reference it should need to draw the game.
	 * @param The game object.
	 */
	public void setGame(Game game){
		this.game = game;
	}


}
