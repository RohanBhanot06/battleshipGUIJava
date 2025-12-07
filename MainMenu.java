/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: MainMenu
 * Description: Creating the MainMenu class for my Battleship Culminating Project.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainMenu {

	private JFrame window; // Window frame to hold all components
	private ImageIcon backgroundImageIcon; // Background image for the main menu
	private JLabel bkgImageContainer; // Label to hold and display the background image
	private JButton gridSizeBtn; // Button to adjust the grid size
	private JButton startGame; // Button to start the game
	private JButton instructionsBtn; // Button to show the instructions
	private JButton backBtn; // Button to go back from instructions screen
	private JButton battleshipSize, cruiserSize, destroyerSize, submarineSize; // Buttons to set sizes of different ships
	private JButton battleshipCount, cruiserCount, destroyerCount, submarineCount; // Buttons to set counts of different ships
	private JLabel errorMessage; // Label to display error messages
	private JLabel instructionsImage; // Label to display instructions image
	private volatile boolean isImageVisible; // Flag to check if the image is visible
	private static final int MAX_SHIP_SIZE = 8; // Constant for maximum ship size
	private static final int MAX_SHIP_COUNT = 5; // Constant for maximum ship count

	/*
	 * Purpose: Constructor to initialize the main menu with a given window.
	 * PRE: JFrame theWindow
	 * POST: None
	 */
	public MainMenu(JFrame theWindow) {
		window = theWindow; // Assign the given window to the window variable
		backgroundImageIcon = new ImageIcon("mainMenu.png"); // Load the background image
		bkgImageContainer = new JLabel(backgroundImageIcon); // Create a label to hold the background image
		isImageVisible = true; // Set the image visibility flag to true
	}

	/*
	 * Purpose: Checks if the selected ships can fit on the board.
	 * PRE: None
	 * POST: boolean (true/false)
	 */
	public boolean canShipsFitOnBoard() {
		// Calculate total ship size based on counts and sizes of different ships
		int totalShipSize = (GameLogic.battleshipCount * GameLogic.battleshipSize)
				+ (GameLogic.cruiserCount * GameLogic.cruiserSize)
				+ (GameLogic.destroyerCount * GameLogic.destroyerSize)
				+ (GameLogic.submarineCount * GameLogic.submarineSize);
		
		// Check if total ship size exceeds board area
		if (totalShipSize > GameLogic.boardSize * GameLogic.boardSize) {
			return false; // Ships cannot fit
		}
		// Check if any individual ship size exceeds board size
		if (GameLogic.battleshipSize > GameLogic.boardSize) {
			return false; // Battleship cannot fit
		}
		if (GameLogic.cruiserSize > GameLogic.boardSize) {
			return false; // Cruiser cannot fit
		}
		if (GameLogic.destroyerSize > GameLogic.boardSize) {
			return false; // Destroyer cannot fit
		}
		if (GameLogic.submarineSize > GameLogic.boardSize) {
			return false; // Submarine cannot fit
		}
		return true; // All ships can fit
	}

	/*
	 * Purpose: Loads the main title screen with all components.
	 * PRE: None
	 * POST: None
	 */
	public void loadTitleScreen() {
		// Set size and location of background image
		bkgImageContainer.setSize(window.getContentPane().getWidth(), window.getContentPane().getHeight() / 2);
		bkgImageContainer.setLocation(0, 0);
		bkgImageContainer.setVisible(true); // Make the background image visible

		// Initialize and configure error message label
		errorMessage = new JLabel("Error: Grid is too small to fit the selected ships.");
		errorMessage.setForeground(Color.RED); // Set text color to red
		errorMessage.setFont(new Font("Courier New", Font.BOLD, 15)); // Set font style and size
		errorMessage.setSize(460, 40); // Set size of the error message label
		errorMessage.setLocation(window.getWidth() / 2 - errorMessage.getWidth() / 2,
				window.getHeight() - errorMessage.getHeight() - 30); // Set location of the error message
		errorMessage.setVisible(false); // Initially hide the error message

		// Initialize and configure start game button
		startGame = new JButton("Start Game");
		startGame.setFont(new Font("Courier New", Font.BOLD, 20)); // Set font style and size
		startGame.setSize(200, 75); // Set size of the start game button
		startGame.setLocation(115, bkgImageContainer.getHeight() + 20); // Set location of the start game button
		startGame.addActionListener(new ActionListener() { // Add action listener to handle button click
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove all components from the window on start game button click
				window.getContentPane().remove(startGame);
				window.getContentPane().remove(bkgImageContainer);
				window.getContentPane().remove(gridSizeBtn);
				window.getContentPane().remove(battleshipSize);
				window.getContentPane().remove(cruiserSize);
				window.getContentPane().remove(destroyerSize);
				window.getContentPane().remove(submarineSize);
				window.getContentPane().remove(battleshipCount);
				window.getContentPane().remove(cruiserCount);
				window.getContentPane().remove(destroyerCount);
				window.getContentPane().remove(submarineCount);
				window.getContentPane().remove(instructionsBtn);
				window.getContentPane().revalidate();
				window.getContentPane().repaint();
				window.getContentPane().setBackground(Color.WHITE);
				isImageVisible = false;
			}
		});

		// Initialize and configure instructions button
		instructionsBtn = new JButton("Instructions");
		instructionsBtn.setFont(new Font("Courier New", Font.BOLD, 20)); // Set font style and size
		instructionsBtn.setSize(200, 75); // Set size of the instructions button
		instructionsBtn.setLocation(115, bkgImageContainer.getHeight() + startGame.getHeight() + 25); // Set location of the instructions button
		instructionsBtn.addActionListener(new ActionListener() { // Add action listener to handle button click
			@Override
			public void actionPerformed(ActionEvent e) {
				loadInstructionsScreen(); // Load instructions screen on button click
			}
		});

		// Initialize and configure grid size button
		gridSizeBtn = new JButton("Grid Width: " + GameLogic.boardSize);
		gridSizeBtn.setFont(new Font("Courier New", Font.BOLD, 20)); // Set font style and size
		gridSizeBtn.setSize(200, 75); // Set size of the grid size button
		gridSizeBtn.setLocation(115, bkgImageContainer.getHeight() + startGame.getHeight() + 105); // Set location of the grid size button
		gridSizeBtn.addActionListener(new ActionListener() { // Add action listener to handle button click
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increase grid size or reset if it exceeds maximum limit
				if (GameLogic.boardSize < 18) {
					GameLogic.boardSize += 2;
				} else {
					GameLogic.boardSize = 6;
				}
				gridSizeBtn.setText("Grid Width: " + GameLogic.boardSize); // Update button text with new grid size
				boolean shipsFit = canShipsFitOnBoard(); // Check if ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable start game button based on ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide error message based on ships fit check
			}
		});

		battleshipSize = new JButton("Length of Battleship: " + GameLogic.battleshipSize); // Initialize the battleship size button with the current battleship size
		battleshipSize.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the battleship size button
		battleshipSize.setSize(225, 60); // Set the size of the battleship size button
		battleshipSize.setLocation(window.getContentPane().getWidth() - battleshipSize.getWidth() - 350, // Set the location of the battleship size button
				bkgImageContainer.getHeight() + 20);
		battleshipSize.addActionListener(new ActionListener() { // Add an action listener to the battleship size button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increment the battleship size or reset if it exceeds the maximum size
				if (GameLogic.battleshipSize < MAX_SHIP_SIZE) {
					GameLogic.battleshipSize += 1;
				} else {
					GameLogic.battleshipSize = 1;
				}
				battleshipSize.setText("Length of Battleship: " + GameLogic.battleshipSize); // Update the button text with the new battleship size
				boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
			}
		});

		battleshipCount = new JButton("Number of Battleships: " + GameLogic.battleshipCount); // Initialize the battleship count button with the current battleship count
		battleshipCount.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the battleship count button
		battleshipCount.setSize(225, 60); // Set the size of the battleship count button
		battleshipCount.setLocation(window.getContentPane().getWidth() - battleshipCount.getWidth() - 115, // Set the location of the battleship count button
				bkgImageContainer.getHeight() + 20);
		battleshipCount.addActionListener(new ActionListener() { // Add an action listener to the battleship count button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increment the battleship count or reset if it exceeds the maximum count
				if (GameLogic.battleshipCount < MAX_SHIP_COUNT) {
					GameLogic.battleshipCount += 1;
				} else {
					GameLogic.battleshipCount = 1;
				}
				battleshipCount.setText("Number of Battleships: " + GameLogic.battleshipCount); // Update the button text with the new battleship count
				boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
			}
		});

		cruiserSize = new JButton("Length of Cruiser: " + GameLogic.cruiserSize); // Initialize the cruiser size button with the current cruiser size
		cruiserSize.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the cruiser size button
		cruiserSize.setSize(225, 60); // Set the size of the cruiser size button
		cruiserSize.setLocation(window.getContentPane().getWidth() - cruiserSize.getWidth() - 350, // Set the location of the cruiser size button
				bkgImageContainer.getHeight() + battleshipSize.getHeight() + 17);
		cruiserSize.addActionListener(new ActionListener() { // Add an action listener to the cruiser size button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increment the cruiser size or reset if it exceeds the maximum size
				if (GameLogic.cruiserSize < MAX_SHIP_SIZE) {
					GameLogic.cruiserSize += 1;
				} else {
					GameLogic.cruiserSize = 1;
				}
				cruiserSize.setText("Length of Cruiser: " + GameLogic.cruiserSize); // Update the button text with the new cruiser size
				boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
			}
		});

		cruiserCount = new JButton("Number of Cruisers: " + GameLogic.cruiserCount); // Initialize the cruiser count button with the current cruiser count
		cruiserCount.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the cruiser count button
		cruiserCount.setSize(225, 60); // Set the size of the cruiser count button
		cruiserCount.setLocation(window.getContentPane().getWidth() - cruiserCount.getWidth() - 115, // Set the location of the cruiser count button
				bkgImageContainer.getHeight() + battleshipCount.getHeight() + 17);
		cruiserCount.addActionListener(new ActionListener() { // Add an action listener to the cruiser count button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increment the cruiser count or reset if it exceeds the maximum count
				if (GameLogic.cruiserCount < MAX_SHIP_COUNT) {
					GameLogic.cruiserCount += 1;
				} else {
					GameLogic.cruiserCount = 1;
				}
				cruiserCount.setText("Number of Cruisers: " + GameLogic.cruiserCount); // Update the button text with the new cruiser count
				boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
			}
		});

		destroyerSize = new JButton("Length of Destroyer: " + GameLogic.destroyerSize); // Initialize the destroyer size button with the current destroyer size
		destroyerSize.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the destroyer size button
		destroyerSize.setSize(225, 60); // Set the size of the destroyer size button
		destroyerSize.setLocation(window.getContentPane().getWidth() - battleshipSize.getWidth() - 350, // Set the location of the destroyer size button
				bkgImageContainer.getHeight() + battleshipSize.getHeight() + cruiserSize.getHeight() + 17);
		destroyerSize.addActionListener(new ActionListener() { // Add an action listener to the destroyer size button
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Increment the destroyer size or reset if it exceeds the maximum size
				if (GameLogic.destroyerSize < MAX_SHIP_SIZE) {
					GameLogic.destroyerSize += 1;
				} else {
					GameLogic.destroyerSize = 1;
				}
				destroyerSize.setText("Length of Destroyer: " + GameLogic.destroyerSize); // Update the button text with the new destroyer size
				boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
				startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
				errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
			}
		});

		destroyerCount = new JButton("Number of Destroyers: " + GameLogic.destroyerCount); // Initialize the destroyer count button with the current destroyer count
		destroyerCount.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the destroyer count button
		destroyerCount.setSize(225, 60); // Set the size of the destroyer count button
		destroyerCount.setLocation(window.getContentPane().getWidth() - destroyerCount.getWidth() - 115, // Set the location of the destroyer count button
		        bkgImageContainer.getHeight() + battleshipCount.getHeight() + cruiserCount.getHeight() + 17);
		destroyerCount.addActionListener(new ActionListener() { // Add an action listener to the destroyer count button
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        // Increment the destroyer count or reset if it exceeds the maximum count
		        if (GameLogic.destroyerCount < MAX_SHIP_COUNT) {
		            GameLogic.destroyerCount += 1;
		        } else {
		            GameLogic.destroyerCount = 1;
		        }
		        destroyerCount.setText("Number of Destroyers: " + GameLogic.destroyerCount); // Update the button text with the new destroyer count
		        boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
		        startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
		        errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
		    }
		});

		submarineSize = new JButton("Length of Submarine: " + GameLogic.submarineSize); // Initialize the submarine size button with the current submarine size
		submarineSize.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the submarine size button
		submarineSize.setSize(225, 60); // Set the size of the submarine size button
		submarineSize.setLocation(window.getContentPane().getWidth() - submarineSize.getWidth() - 350, // Set the location of the submarine size button
		        bkgImageContainer.getHeight() + battleshipSize.getHeight() + cruiserSize.getHeight()
		                + destroyerSize.getHeight() + 15);
		submarineSize.addActionListener(new ActionListener() { // Add an action listener to the submarine size button
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        // Increment the submarine size or reset if it exceeds the maximum size
		        if (GameLogic.submarineSize < MAX_SHIP_SIZE) {
		            GameLogic.submarineSize += 1;
		        } else {
		            GameLogic.submarineSize = 1;
		        }
		        submarineSize.setText("Length of Submarine: " + GameLogic.submarineSize); // Update the button text with the new submarine size
		        boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
		        startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
		        errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
		    }
		});

		submarineCount = new JButton("Number of Submarines: " + GameLogic.submarineCount); // Initialize the submarine count button with the current submarine count
		submarineCount.setFont(new Font("Courier New", Font.BOLD, 14)); // Set the font of the submarine count button
		submarineCount.setSize(225, 60); // Set the size of the submarine count button
		submarineCount.setLocation(window.getContentPane().getWidth() - submarineCount.getWidth() - 115, // Set the location of the submarine count button
		        bkgImageContainer.getHeight() + battleshipCount.getHeight() + cruiserCount.getHeight()
		                + destroyerCount.getHeight() + 15);
		submarineCount.addActionListener(new ActionListener() { // Add an action listener to the submarine count button
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        // Increment the submarine count or reset if it exceeds the maximum count
		        if (GameLogic.submarineCount < MAX_SHIP_COUNT) {
		            GameLogic.submarineCount += 1;
		        } else {
		            GameLogic.submarineCount = 1;
		        }
		        submarineCount.setText("Number of Submarines: " + GameLogic.submarineCount); // Update the button text with the new submarine count
		        boolean shipsFit = canShipsFitOnBoard(); // Check if the ships can fit on the board
		        startGame.setEnabled(shipsFit); // Enable or disable the start game button based on the ships fit check
		        errorMessage.setVisible(!shipsFit); // Show or hide the error message based on the ships fit check
		    }
		});

		// Set visibility of various components to true
		startGame.setVisible(true); // Make the start game button visible
		gridSizeBtn.setVisible(true); // Make the grid size button visible
		instructionsBtn.setVisible(true); // Make the instructions button visible
		battleshipSize.setVisible(true); // Make the battleship size button visible
		cruiserSize.setVisible(true); // Make the cruiser size button visible
		destroyerSize.setVisible(true); // Make the destroyer size button visible
		submarineSize.setVisible(true); // Make the submarine size button visible
		battleshipCount.setVisible(true); // Make the battleship count button visible
		cruiserCount.setVisible(true); // Make the cruiser count button visible
		destroyerCount.setVisible(true); // Make the destroyer count button visible
		submarineCount.setVisible(true); // Make the submarine count button visible

		// Add various components to the window's content pane
		window.getContentPane().add(errorMessage); // Add the error message to the window
		window.getContentPane().add(startGame); // Add the start game button to the window
		window.getContentPane().add(instructionsBtn); // Add the instructions button to the window
		window.getContentPane().add(bkgImageContainer); // Add the background image container to the window
		window.getContentPane().add(gridSizeBtn); // Add the grid size button to the window
		window.getContentPane().add(battleshipSize); // Add the battleship size button to the window
		window.getContentPane().add(cruiserSize); // Add the cruiser size button to the window
		window.getContentPane().add(destroyerSize); // Add the destroyer size button to the window
		window.getContentPane().add(submarineSize); // Add the submarine size button to the window
		window.getContentPane().add(battleshipCount); // Add the battleship count button to the window
		window.getContentPane().add(cruiserCount); // Add the cruiser count button to the window
		window.getContentPane().add(destroyerCount); // Add the destroyer count button to the window
		window.getContentPane().add(submarineCount); // Add the submarine count button to the window

		window.getContentPane().setBackground(Color.BLACK); // Set the background color of the window to black
		window.setVisible(true); // Make the window visible
		window.getContentPane().revalidate(); // Revalidate the window's content pane to ensure all components are laid out properly
		window.getContentPane().repaint(); // Repaint the window's content pane to update the display
	}

	/*
	 * Purpose: Loads the instructions screen, displaying the instructions image and a back button to return to the main menu.
	 * PRE: None
	 * POST: None
	 */
	public void loadInstructionsScreen() {
		// Remove all components from the content pane of the window
		window.getContentPane().removeAll();
		
		// Create a new JLabel with an image icon of instructions.png
		instructionsImage = new JLabel(new ImageIcon("instructions.png"));
		// Set the size of the instructions image to the width of the content pane and the height minus 60 pixels
		instructionsImage.setSize(window.getContentPane().getWidth(), window.getContentPane().getHeight() - 60);
		// Set the location of the instructions image to the top-left corner (0, 0)
		instructionsImage.setLocation(0, 0);

		// Create a new button for returning to the main menu
		backBtn = new JButton("Back to Main Menu");
		// Set the font of the back button to Courier New, bold, size 20
		backBtn.setFont(new Font("Courier New", Font.BOLD, 20));
		// Set the size of the back button to 250x50 pixels
		backBtn.setSize(250, 50);
		// Set the location of the back button to be centered horizontally and 35 pixels from the bottom of the window
		backBtn.setLocation(window.getWidth() / 2 - backBtn.getWidth() / 2, window.getHeight() - backBtn.getHeight() - 35);
		// Add an action listener to the back button to handle click events
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Load the title screen when the back button is clicked
				loadTitleScreen();
				// Remove the instructions image and back button from the content pane
				window.getContentPane().remove(instructionsImage);
				window.getContentPane().remove(backBtn);
			}
		});

		// Add the instructions image and back button to the content pane of the window
		window.getContentPane().add(instructionsImage);
		window.getContentPane().add(backBtn);

		// Revalidate the content pane to apply the changes
		window.getContentPane().revalidate();
		// Repaint the content pane to refresh the display
		window.getContentPane().repaint();
	}

	/*
	 * Purpose: Checks if the image is currently visible.
	 * PRE: None
	 * POST: boolean (isImageVisible)
	 */
	public boolean isImageVisible() {
		// Return the value of the isImageVisible variable
		return isImageVisible;
	}
}
