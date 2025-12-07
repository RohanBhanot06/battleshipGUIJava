/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: Ship
 * Description: Creating the Ship class for my Battleship Culminating Project.
 */

import java.awt.Point;
import java.util.ArrayList;

public class Ship {

	private ShipPiece[] pieces; // Array of ShipPiece objects representing the pieces of the ship
	private Point startingPosition; // Point representing the starting position of the ship off the grid

	/*
	 * Purpose: Constructor. Initializes the Ship with an array of ShipPiece objects.
	 * PRE: ShipPiece[] list
	 * POST: None
	 */
	Ship(ShipPiece[] list) {
		pieces = list; // Assign the array of ShipPiece objects to pieces
		startingPosition = new Point(0, 0); // Initialize startingPosition to (0, 0)
	}

	/*
	 * Purpose: Constructor. Initializes the Ship with an ArrayList of ShipPiece objects.
	 * PRE: ArrayList<ShipPiece> list
	 * POST: None
	 */
	Ship(ArrayList<ShipPiece> list) {
		pieces = list.toArray(new ShipPiece[0]); // Convert ArrayList to array and assign to pieces
		startingPosition = new Point(0, 0); // Initialize startingPosition to (0, 0)
	}

	/*
	 * Purpose: Checks if all ship pieces are destroyed, indicating the ship is dead.
	 * PRE: None
	 * POST: boolean (true if the ship is dead, false otherwise)
	 */
	public boolean checkIfDead() {
		boolean isDead = true; // Initialize isDead to true
		for (int i = 0; i < pieces.length; i++) { // Iterate through all pieces of the ship
			if (!pieces[i].isDestroy()) { // If any piece is not destroyed
				isDead = false; // Set isDead to false
			}
		}
		return isDead; // Return the status of the ship
	}

	/*
	 * Purpose: Returns the array of ShipPiece objects.
	 * PRE: None
	 * POST: pieces
	 */
	public ShipPiece[] getShipPieces() {
		return pieces; // Return the array of ShipPiece objects
	}

	/*
	 * Purpose: Sets the starting off-grid position of the ship.
	 * PRE: Point sp
	 * POST: None
	 */
	public void setStartingOffGridPosition(Point sp) {
		startingPosition = sp; // Assign the provided point to startingPosition
	}

	/*
	 * Purpose: Returns the starting off-grid position of the ship.
	 * PRE: None
	 * POST: startingPosition
	 */
	public Point getStartingOffGridPosition() {
		return startingPosition; // Return the startingPosition
	}
}
