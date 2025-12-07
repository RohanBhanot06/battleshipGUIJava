/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: BetweenTurnsScreen
 * Description: Creating the BetweenTurnsScreen class for my Battleship Culminating Project.
 */

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BetweenTurnsScreen implements MouseListener {

	private JPanel window; // JPanel for the window
	private ImageIcon backgroundImageIcon; // ImageIcon for the background image
	private JLabel bkgImageContainer; // JLabel for displaying the background image
	private volatile boolean isImageVisible; // Boolean flag to check if the image is visible
	private Grid grid; // Grid object reference
	private SmallGrid small; // SmallGrid object reference

	/*
	 * Purpose: Constructor for initializing the BetweenTurnsScreen with a JPanel, Grid, and SmallGrid.
	 * PRE: JPanel theWindow, Grid grid, SmallGrid small
	 * POST: None
	 */
	public BetweenTurnsScreen(JPanel theWindow, Grid grid, SmallGrid small) {
		window = theWindow; // Assign theWindow to the window instance variable
		backgroundImageIcon = new ImageIcon("nextTurn.png"); // Load the background image icon
		Image bkgImage = backgroundImageIcon.getImage(); // Get the image from the icon
		Image scaledBkgImage = bkgImage.getScaledInstance(window.getWidth(), window.getHeight(),
				BufferedImage.SCALE_FAST); // Scale the image to fit the window size
		ImageIcon scaledBkgImageIcon = new ImageIcon(scaledBkgImage); // Create a new ImageIcon from the scaled image
		bkgImageContainer = new JLabel(scaledBkgImageIcon); // Create a JLabel with the scaled image icon
		bkgImageContainer.setSize(window.getWidth(), window.getHeight()); // Set the size of the JLabel to the window size
		bkgImageContainer.setLocation(0, 0); // Set the location of the JLabel to (0, 0)
		isImageVisible = true; // Set isImageVisible to true
		this.grid = grid; // Assign the grid parameter to the grid instance variable
		this.small = small; // Assign the small parameter to the small instance variable
	}

	/*
	 * Purpose: Loads and displays the turn screen with the background image.
	 * PRE: None
	 * POST: None
	 */
	public void loadTurnScreen() {
		window.add(bkgImageContainer); // Add the background image container to the window
		bkgImageContainer.addMouseListener(this); // Add a mouse listener to the background image container
		window.setVisible(true); // Make the window visible
		window.repaint(); // Repaint the window to update the display
	}

	/*
	 * Purpose: Checks if the background image is currently visible.
	 * PRE: None
	 * POST: boolean (true if the image is visible, false otherwise)
	 */
	public boolean isImageVisible() {
		return isImageVisible; // Return the value of isImageVisible
	}

	/*
	 * Purpose: Handles the mouseReleased event to transition to the next turn.
	 * PRE: MouseEvent arg0
	 * POST: None
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		window.remove(bkgImageContainer); // Remove the background image container from the window
		window.revalidate(); // Revalidate the window to update the layout
		window.repaint(); // Repaint the window to update the display
		grid.setTurn(true); // Set the turn to true in the grid
		grid.setVisible(true); // Make the grid visible
		small.setVisible(true); // Make the small grid visible
	}

	// Empty method for the mouseClicked event, required by the MouseListener interface
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	// Empty method for the mouseEntered event, required by the MouseListener interface
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	// Empty method for the mouseExited event, required by the MouseListener interface
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	// Empty method for the mousePressed event, required by the MouseListener interface
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
}
