/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: ShipPiece
 * Description: Creating the ShipPiece class for my Battleship Culminating Project.
 */

import java.awt.*;
import javax.swing.ImageIcon;

public class ShipPiece {
	private Image shipPieceAlive; // Image representing the ship piece when it is alive
	private boolean shipIsDead; // Boolean indicating if the ship piece is destroyed
	boolean isPlayer1; // Boolean indicating which player the ship piece belongs to

	/*
	 * Purpose: Constructor that initializes the ShipPiece with the player information and sets the image.
	 * PRE: boolean isPlayer1
	 * POST: None
	 */
	public ShipPiece(boolean isPlayer1) {
		this.isPlayer1 = isPlayer1; // Initialize the player information
		// Set the image based on which player the piece belongs to
		if (isPlayer1)
			shipPieceAlive = new ImageIcon("player1.png").getImage(); // Image for player 1
		else
			shipPieceAlive = new ImageIcon("player2.png").getImage(); // Image for player 2
		shipIsDead = false; // Initialize the ship piece as not destroyed
	}

	/*
	 * Purpose: Sets the image of the ship piece based on the provided file name.
	 * PRE: String file
	 * POST: None
	 */
	public void setShipImage(String file) {
		shipPieceAlive = new ImageIcon(file).getImage(); // Set the image based on the file name
	}

	/*
	 * Purpose: Returns the image of the ship piece.
	 * PRE: None
	 * POST: shipPieceAlive
	 */
	public Image getShipImage() {
		return shipPieceAlive; // Return the image of the ship piece
	}

	/*
	 * Purpose: Destroys the ship piece by setting it to dead and changing the image to the damaged image.
	 * PRE: None
	 * POST: None
	 */
	public void destroy() {
		shipIsDead = true; // Set the ship piece as destroyed
		// Change the image to the damaged image based on the player
		if (isPlayer1) {
			setShipImage("player1Hit.png"); // Damaged image for player 1
		} else {
			setShipImage("player2Hit.png"); // Damaged image for player 2
		}
	}

	/*
	 * Purpose: Returns whether the ship piece is destroyed.
	 * PRE: None
	 * POST: boolean (true if the ship piece is destroyed, false otherwise)
	 */
	public boolean isDestroy() {
		return shipIsDead; // Return if the ship piece is destroyed
	}
}
