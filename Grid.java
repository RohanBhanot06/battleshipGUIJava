/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: Grid
 * Description: Creating the Grid class for my Battleship Culminating Project.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Grid extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L; // Serial version UID for serialization
	private BufferedImage gridImage; // BufferedImage to store the grid image
	private Object[][] array; // 2D array to represent the grid
	public static final int X_ORIGIN = 54; // X coordinate of the top left corner of the grid
	public static final int Y_ORIGIN = 56; // Y coordinate of the top left corner of the grid
	public static final int TILE_SIZE = 47; // Size of each tile in the grid
	public static final int BORDER_SIZE = 5; // Size of the border between tiles
	private volatile boolean isTurn; // Boolean to track if it's the player's turn
	private boolean state; // Boolean to track the state of the game

	/*
	 * Purpose: Default constructor. Uses an empty array.
	 * PRE: None
	 * POST: None
	 */
	public Grid() {
		this(new Object[10][10], "gridLabels.png"); // Calling another constructor with default values
	}

	/*
	 * Purpose: Constructor that takes an array.
	 * PRE: Object[][] arr
	 * POST: None
	 */
	public Grid(Object[][] arr) {
		this(arr, "gridLabels.png"); // Calling another constructor with a provided array
	}

	/*
	 * Purpose: Constructor that takes an array and a file path.
	 * PRE: Object[][] arr, String path
	 * POST: None
	 */
	public Grid(Object[][] arr, String path) {
		array = arr; // Initializing the grid array
		isTurn = true; // Initializing the turn to true
		state = false; // Initializing the state to false
		setBackground(Color.WHITE); // Setting the background color to white
		setPreferredSize(new Dimension((X_ORIGIN + arr.length + 1 + ((TILE_SIZE + BORDER_SIZE) * array.length)),
				Y_ORIGIN + arr.length + 1 + ((TILE_SIZE + BORDER_SIZE) * array.length))); // Setting preferred size of the grid
		setSize(getPreferredSize()); // Setting the size of the grid
		setLocation(0, 0); // Setting the location of the grid

		try {
			gridImage = ImageIO.read(new File(path)); // Reading the grid image from the file
		} catch (IOException e) {
			System.out.println("Failed to load image"); // Handling exception if image loading fails
		}
	}

	/*
	 * Purpose: Overridden method to paint components on the grid.
	 * PRE: Graphics g
	 * POST: None
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Calling the superclass method
		Graphics2D g2 = (Graphics2D) g; // Casting to Graphics2D for advanced drawing
		g2.drawImage(gridImage, 0, 0, this); // Drawing the grid image

		// Loop through all spots in the grid
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				// Check if there is a 1 or a ShipPiece that has not been destroyed
				if (array[i][j].equals((Object) 1) || ((array[i][j]).getClass().getName().equals("ShipPiece")
						&& !((ShipPiece) array[i][j]).isDestroy())) {
					g2.setColor(Color.GRAY); // Set color to gray
					g2.fillRect(X_ORIGIN + i + 1 + ((TILE_SIZE + BORDER_SIZE) * i),
							Y_ORIGIN + j + 1 + ((TILE_SIZE + BORDER_SIZE) * j), TILE_SIZE + (BORDER_SIZE / 2) - 1,
							TILE_SIZE + (BORDER_SIZE / 2) - 1); // Fill the tile with gray
				} else if ((array[i][j]).getClass().getName().equals("ShipPiece")) {
					g2.drawImage(((ShipPiece) array[i][j]).getShipImage(),
							X_ORIGIN + i + ((TILE_SIZE + BORDER_SIZE) * i) + BORDER_SIZE / 2,
							Y_ORIGIN + j + ((TILE_SIZE + BORDER_SIZE) * j) + BORDER_SIZE / 2, this); // Draw the ship piece image
				}
			}
		}
	}

	/*
	 * Purpose: Handles mouse released events.
	 * PRE: MouseEvent e
	 * POST: None
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// Left click
		if (isTurn && e.getButton() == MouseEvent.BUTTON1) {
			int value = e.getX(); // Get X coordinate of the mouse
			int counter1 = 0; // Initialize counter1
			while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter1) + BORDER_SIZE < value) {
				counter1++; // Increment counter1 to find the grid position
			}
			counter1--;

			int value2 = e.getY() - (TILE_SIZE / 2); // Get Y coordinate of the mouse
			int counter2 = 0; // Initialize counter2
			while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter2) + BORDER_SIZE < value2) {
				counter2++; // Increment counter2 to find the grid position
			}
			counter2--;

			// If (counter1, counter2) is a valid position in the array
			if (counter1 < array.length && counter1 >= 0) {
				if (counter2 < array[0].length && counter2 >= 0) {
					// If the object at the coordinate is 1
					if (array[counter1][counter2].equals((Object) 1)) {
						array[counter1][counter2] = 0; // Set the object at the coordinate to 0 (an empty space)
						repaint(); // Repaint the grid
						isTurn = false; // End the turn
					} else if ((array[counter1][counter2]).getClass().getName().equals("ShipPiece")
							&& !((ShipPiece) array[counter1][counter2]).isDestroy()) {
						((ShipPiece) array[counter1][counter2]).destroy(); // Destroy the ship piece
						repaint(); // Repaint the grid
						isTurn = false; // End the turn
					}
					state = false; // Update the state
				}
			}
		} else if (!isTurn && e.getButton() == MouseEvent.BUTTON1) {
			state = true; // Update the state
		}
	}

	/*
	 * Purpose: Empty method for mouse clicked event.
	 * PRE: MouseEvent e
	 * POST: None
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * Purpose: Empty method for mouse entered event.
	 * PRE: MouseEvent e
	 * POST: None
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * Purpose: Empty method for mouse exited event.
	 * PRE: MouseEvent e
	 * POST: None
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * Purpose: Empty method for mouse pressed event.
	 * PRE: MouseEvent e
	 * POST: None
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * Purpose: Returns if it's the player's turn.
	 * PRE: None
	 * POST: boolean (isTurn)
	 */
	public boolean isTurn() {
		return isTurn; // Return the turn state
	}

	/*
	 * Purpose: Sets the turn to the given parameter.
	 * PRE: boolean t
	 * POST: None
	 */
	public void setTurn(boolean t) {
		isTurn = t; // Set the turn state
	}

	/*
	 * Purpose: Returns the grid array.
	 * PRE: None
	 * POST: array
	 */
	public Object[][] getArray() {
		return array; // Return the grid array
	}

	/*
	 * Purpose: Sets the grid array to the given parameter.
	 * PRE: Object[][] arr
	 * POST: None
	 */
	public void setArray(Object[][] arr) {
		array = arr; // Set the grid array
	}

	/*
	 * Purpose: Returns the state of the game.
	 * PRE: None
	 * POST: boolean (state)
	 */
	public boolean getState() {
		return state; // Return the state
	}

	/*
	 * Purpose: Sets the state of the game to the given parameter.
	 * PRE: boolean s
	 * POST: None
	 */
	public void setState(boolean s) {
		state = s; // Set the state
	}
}
