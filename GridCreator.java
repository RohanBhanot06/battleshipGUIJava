/* Name: Rohan Bhanot 
 * Starting Date: Friday, April 26, 2024
 * Ending Date: Wednesday, June 12, 2024
 * Teacher: Elena Kapustina
 * Course Code: ICS4U1-2
 * Program Name: GridCreator
 * Description: Creating the GridCreator class for my Battleship Culminating Project.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GridCreator extends JPanel {
    private static final long serialVersionUID = 1L; // Serialization ID for the class
    private BufferedImage gridImage = null; // Image for the grid
    private Object[][] gridArray; // 2D array to represent the grid
    private Ship[] shipArray; // Array of ships
    private JPanel[] panelArray; // Array of panels for ships
    private JButton endSetup, randomizeShipsBtn; // Buttons for ending setup and randomizing ships
    private JFrame window; // Main application window
    private volatile boolean setupOver = false; // Flag to indicate if setup is over
    public static final int X_ORIGIN = 54; // X-coordinate origin for the grid
    public static final int Y_ORIGIN = 56; // Y-coordinate origin for the grid
    public static final int TILE_SIZE = 47; // Size of each tile in the grid
    public static final int BORDER_SIZE = 5; // Size of the border around tiles
    public static boolean currentlyPlacingShip = false; // Flag to indicate if a ship is currently being placed

    /*
     * Purpose: Constructor to initialize GridCreator with a ship array and a JFrame.
     * PRE: Ship[] shipArray, JFrame app
     * POST: None
     */
    public GridCreator(Ship[] shipArray, JFrame app) {
        this(shipArray, 10, app); // Call another constructor with default grid size of 10
    }

    /*
     * Purpose: Constructor to initialize GridCreator with a ship array, grid size, and a JFrame.
     * PRE: Ship[] shipArray, int gridSize, JFrame app
     * POST: None
     */
    public GridCreator(Ship[] shipArray, int gridSize, JFrame app) {
        this(shipArray, gridSize, "gridLabels.png", app); // Call another constructor with default image path
    }

    /*
     * Purpose: Constructor to initialize GridCreator with a ship array, grid size, image path, and a JFrame.
     * PRE: Ship[] shipArray, int gridSize, String path, JFrame app
     * POST: None
     */
    public GridCreator(Ship[] shipArray, int gridSize, String path, JFrame app) {
        setLayout(null); // Set layout to null for absolute positioning
        setBackground(Color.WHITE); // Set background color to white
        setLocation(0, 0); // Set location of the panel
        window = app; // Assign the JFrame to the window variable

        // Initialize the grid array with the specified size and fill it with 1s
        Object[][] grid = new Object[gridSize][gridSize];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 1;
            }
        }

        gridArray = grid; // Assign the grid to the instance variable
        this.shipArray = shipArray; // Assign the ship array to the instance variable
        panelArray = new JPanel[shipArray.length]; // Initialize the panel array

        try {
            gridImage = ImageIO.read(new File(path)); // Load the grid image from the file
        } catch (IOException e) {
            System.out.println("Failed to load image"); // Print an error message if image loading fails
        }
    }

    /*
     * Purpose: Does all the work to setup the grid.
     * PRE: None
     * POST: None
     */
    public void setup() {
        int largestShipSize = 0; // Variable to keep track of the largest ship size
        // Find the size of the largest ship
        for (int i = 0; i < shipArray.length; i++) {
            int temp = shipArray[i].getShipPieces().length;
            if (temp > largestShipSize) {
                largestShipSize = temp;
            }
        }

        // Calculate the preferred window size based on the grid and ship sizes
        int windowWidth = X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * gridArray.length) + (2 * BORDER_SIZE) + 50
                + ((largestShipSize + 1) * TILE_SIZE);
        int windowHeight = Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * (gridArray.length + 1));
        if (windowHeight < 2 * TILE_SIZE + (shipArray.length * (TILE_SIZE + BORDER_SIZE + 2))) {
            windowHeight = 2 * TILE_SIZE + (shipArray.length * (TILE_SIZE + BORDER_SIZE + 2));
        }
        window.setPreferredSize(new Dimension(windowWidth, windowHeight)); // Set the preferred window size
        window.setMinimumSize(new Dimension(windowWidth, windowHeight)); // Set the minimum window size
        window.pack(); // Pack the window to fit the preferred size
        setSize(window.getContentPane().getSize()); // Set the size of the panel to match the content pane

        // Create a label with the grid image and add it to the screen
        JLabel gridLabel = new JLabel(new ImageIcon(gridImage));
        gridLabel.setSize(X_ORIGIN + gridArray.length + 1 + ((TILE_SIZE + BORDER_SIZE) * gridArray.length),
                Y_ORIGIN + gridArray.length + 1 + ((TILE_SIZE + BORDER_SIZE) * (gridArray.length)));
        gridLabel.setLocation(0, 0);
        gridLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gridLabel.setVerticalAlignment(SwingConstants.TOP);
        add(gridLabel);

        int buttonXPos = gridLabel.getWidth(); // Calculate the X position for buttons

        // Create a button for randomizing the grid
        randomizeShipsBtn = new JButton("Randomize Grid");
        randomizeShipsBtn.setFont(new Font("Courier New", Font.BOLD, 20));
        randomizeShipsBtn.setBounds(buttonXPos, 0, window.getWidth() - buttonXPos, TILE_SIZE - 5);
        randomizeShipsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                // Reset the position of each panel to its starting off-grid position
                for (int i = 0; i < panelArray.length; i++) {
                    panelArray[i].setLocation(shipArray[i].getStartingOffGridPosition());
                }
                // Randomly place each ship on the grid
                for (int i = 0; i < panelArray.length; i++) {
                    int timeout = 0;
                    while (timeout < 500
                            && shipArray[i].getStartingOffGridPosition().equals(panelArray[i].getLocation())) {
                        int x = rand.nextInt(gridArray.length);
                        int y = rand.nextInt(gridArray.length);
                        timeout++;
                        leftClick(i, x, y); // Simulate left click to place the ship
                        if (rand.nextInt(2) == 0
                                && !shipArray[i].getStartingOffGridPosition().equals(panelArray[i].getLocation())) {
                            rightClick(i, x, y); // Randomly decide whether to rotate the ship
                        }
                    }
                }
            }
        });
        add(randomizeShipsBtn); // Add the randomize button to the panel
        randomizeShipsBtn.setVisible(true); // Make the randomize button visible
        repaint(); // Repaint the panel to update the display

        // Create a button that ends setup when pressed
        endSetup = new JButton("Finish");
        endSetup.setFont(new Font("Courier New", Font.BOLD, 20));
        endSetup.setBounds(buttonXPos, TILE_SIZE - 5, window.getWidth() - buttonXPos, window.getHeight());
        endSetup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupOver = true; // Set the setupOver flag to true when the button is pressed
            }
        });
        add(endSetup); // Add the end setup button to the panel
        endSetup.setVisible(false); // Set the visibility of the button to false

        // Loops through all the ships
        for (int j = 0; j < shipArray.length; j++) {
            final int shipNum = j; // Final variable to use inside inner classes

            // Creates a panel with a BoxLayout (X Axis) for the ship
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE); // Set panel background color
            panel.setOpaque(false); // Set panel opacity
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); // Set panel layout to BoxLayout on X axis
            panel.add(Box.createRigidArea(new Dimension(0, 0))); // Add a rigid area (spacing) to the panel

            // Loops through the ship pieces in the ship
            for (int i = 0; i < shipArray[j].getShipPieces().length; i++) {
                // Adds labels containing each image to the panel
                ImageIcon icon = new ImageIcon(shipArray[j].getShipPieces()[i].getShipImage()); // Create an ImageIcon from the ship piece image
                JLabel label = new JLabel(icon); // Create a JLabel with the ImageIcon
                panel.add(label); // Add the label to the panel
                panel.add(Box.createRigidArea(new Dimension(BORDER_SIZE + 2, 0))); // Add spacing between labels
            }

            // Places the panel off to the side of the grid
            panel.setLocation(X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * gridArray.length) + (2 * BORDER_SIZE) + 50,
                              TILE_SIZE + BORDER_SIZE + 2 + (j * (TILE_SIZE + BORDER_SIZE + 2))); // Set panel location
            panel.setSize(shipArray[j].getShipPieces().length * (TILE_SIZE + BORDER_SIZE), TILE_SIZE); // Set panel size
            shipArray[shipNum].setStartingOffGridPosition(panel.getLocation()); // Store the panel's starting position
            add(panel); // Add the panel to the main panel
            panelArray[j] = panel; // Store the panel in the panel array
            setComponentZOrder(panel, 0); // Set the Z order of the panel

            // Add a mouse motion listener to the panel
            panel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // When the left mouse button is down, the panel is moved to the mouse location
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        JPanel component = (JPanel) e.getComponent().getParent().getParent(); // Get the parent component
                        Point pt = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component)); // Convert the point to the parent's coordinate space
                        panel.setLocation((int) pt.getX() - (TILE_SIZE / 2), (int) pt.getY() - (TILE_SIZE / 2)); // Set the panel location
                        currentlyPlacingShip = true; // Set flag indicating a ship is being placed
                    }
                }
            });

            // Add a mouse listener to the panel
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    // Gets the coordinates of the mouse in terms of the window
                    JPanel component = (JPanel) e.getComponent().getParent().getParent(); // Get the parent component
                    Point pt = new Point(SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), component)); // Convert the point to the parent's coordinate space
                    int counter1 = 0;
                    int counter2 = 0;

                    // Calculates the position in the grid array based on the mouse coordinates
                    int value = (int) pt.getX();
                    while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter1) < value) {
                        counter1++;
                    }
                    counter1--;

                    int value2 = (int) pt.getY();
                    while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter2) < value2) {
                        counter2++;
                    }
                    counter2--;

                    // If left button released
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        // Calls the left click method
                        currentlyPlacingShip = false; // Reset flag
                        leftClick(shipNum, counter1, counter2); // Call method to handle left click
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        // If right button released
                        rightClick(shipNum, counter1, counter2); // Call method to handle right click
                    }

                    endSetup.repaint(); // Repaint the end setup button
                }
            });
        }
        }

        /*
         * Purpose: Gets called when right click is pressed on a ship panel. Attempts to rotate the panel.
         * PRE: int shipNum, int x, int y
         * POST: None
         */
        private void rightClick(int shipNum, int x, int y) {
            // isVertical is set based on the layout of the panel (X or Y axis)
            boolean isVertical = false;
            if (((BoxLayout) panelArray[shipNum].getLayout()).getAxis() == BoxLayout.Y_AXIS) {
                isVertical = true; // Set isVertical to true if the layout is Y_AXIS
            }
            // Calls the remove method to remove the ship (not the panel)
            removeShipFromGridArray(shipArray[shipNum], isVertical); // Remove the ship from the grid array
            // Attempts to rotate the panel
            if (rotatePanel(panelArray[shipNum]) && !currentlyPlacingShip) {
                // If it works, call the add method to add the ship pieces in the new orientation
                addShipToGridArray(shipArray[shipNum], new Point(x, y), !isVertical); // Add the ship to the grid array
            } else if (!currentlyPlacingShip) {
                panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition()); // Reset panel location if rotation fails
                rotatePanel(panelArray[shipNum]); // Rotate the panel back to its original orientation
            }

            showFinishButton(); // Show the finish button
        }

        /*
        * Purpose: Handles the event when the left mouse button is pressed on a ship panel. It attempts to place the ship panel on the grid or resets it to its original position.
        * PRE: int shipNum, int x, int y
        * POST: None
        */
        private void leftClick(int shipNum, int x, int y) {
            // If the panel has a horizontal (X_AXIS) box layout
            if ((((BoxLayout) panelArray[shipNum].getLayout()).getAxis() == BoxLayout.X_AXIS)) {
                // Checks if the panel is within the horizontal bounds of the grid
                if (x < gridArray.length - panelArray[shipNum].getWidth() / TILE_SIZE + 1 && x >= 0) {
                    // Checks if the panel is within the vertical bounds of the grid
                    if (y < gridArray[0].length - panelArray[shipNum].getHeight() / TILE_SIZE + 1 && y >= 0) {
                        // Calls the method to place a ship panel on the specified location on the grid
                        placeShipPanelOnGrid(x, y, shipNum, false);
                    } else {
                        // Resets the panel to its starting position and removes it from the grid array
                        panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition());
                        removeShipFromGridArray(shipArray[shipNum], false);
                    }
                } else {
                    // Resets the panel to its starting position and removes it from the grid array
                    panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition());
                    removeShipFromGridArray(shipArray[shipNum], false);
                }
            } else { // If the panel has a vertical (Y_AXIS) box layout
                // Checks if the panel is within the horizontal bounds of the grid
                if (x < gridArray.length - panelArray[shipNum].getWidth() / TILE_SIZE + 1 && x >= 0) {
                    // Checks if the panel is within the vertical bounds of the grid
                    if (y < gridArray[0].length - panelArray[shipNum].getHeight() / TILE_SIZE + 1 && y >= 0) {
                        // Calls the method to place a ship panel on the specified location on the grid
                        placeShipPanelOnGrid(x, y, shipNum, true);
                    } else {
                        // Rotates the panel to horizontal, resets its position, and removes it from the grid array
                        rotatePanel(panelArray[shipNum]);
                        panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition());
                        removeShipFromGridArray(shipArray[shipNum], true);
                    }
                } else {
                    // Rotates the panel to horizontal, resets its position, and removes it from the grid array
                    rotatePanel(panelArray[shipNum]);
                    panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition());
                    removeShipFromGridArray(shipArray[shipNum], true);
                }
            }
            // Checks if the finish button should be shown
            showFinishButton();
        }

        /*
        * Purpose: Places a ship panel on the grid at the specified coordinates. Handles intersection checks and reverts if necessary.
        * PRE: int x, int y, int shipNum, boolean isVertical
        * POST: None
        */
        private void placeShipPanelOnGrid(int x, int y, int shipNum, boolean isVertical) {
            // Sets the panel's location on the grid based on the coordinates and offsets
            panelArray[shipNum].setLocation(X_ORIGIN + x + (((TILE_SIZE + BORDER_SIZE) * x) + BORDER_SIZE / 2),
                    Y_ORIGIN + y + ((TILE_SIZE + BORDER_SIZE) * y) + BORDER_SIZE / 2);
            // Checks for an intersection with another panel
            if (isIntersection(panelArray[shipNum])) {
                // If there is an intersection and the ship is vertical, rotate it to horizontal
                if (isVertical) {
                    rotatePanel(panelArray[shipNum]);
                }
                // Remove the ship from the grid array and reset its position
                removeShipFromGridArray(shipArray[shipNum], false);
                panelArray[shipNum].setLocation(shipArray[shipNum].getStartingOffGridPosition());
            } else {
                // If no intersection, update the grid array with the ship's new position
                removeShipFromGridArray(shipArray[shipNum], isVertical);
                addShipToGridArray(shipArray[shipNum], new Point(x, y), isVertical);
            }
        }

        /*
        * Purpose: Checks if the finish button should be displayed based on the placement of all ships.
        * PRE: None
        * POST: None
        */
        private void showFinishButton() {
            boolean showButton = true;
            // Check if the user is currently placing a ship
            if (!currentlyPlacingShip) {
                // Loop through all ships to check if any are still in their starting positions
                for (int i = 0; i < shipArray.length; i++) {
                    if (shipArray[i].getStartingOffGridPosition().equals(panelArray[i].getLocation())) {
                        showButton = false;
                    }
                }
                // Set the visibility of the end setup button based on the ship placements
                endSetup.setVisible(showButton);
            }
        }

        /*
        * Purpose: Checks if a panel intersects with any other panels on the grid.
        * PRE: JPanel p
        * POST: boolean (true if there is an intersection, false otherwise)
        */
        private boolean isIntersection(JPanel p) {
            // Loop through all panels in the array
            for (int i = 0; i < panelArray.length; i++) {
                // Check if panel p intersects with any other panel and is not itself
                if (p.getBounds().intersects(panelArray[i].getBounds()) && !p.equals(panelArray[i])) {
                    return true;
                }
            }
            return false;
        }

        /*
        * Purpose: Removes a ship from the grid array.
        * PRE: Ship ship, boolean isVertical
        * POST: None
        */
        private void removeShipFromGridArray(Ship ship, boolean isVertical) {
            // Loop through the grid array
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    for (int k = 0; k < ship.getShipPieces().length; k++) {
                        // Set the grid cell to 1 if it contains a piece of the ship to be removed
                        if (gridArray[j][i] == (ShipPiece) ship.getShipPieces()[k]) {
                            gridArray[j][i] = 1;
                        }
                    }
                }
            }
        }

        /*
        * Purpose: Adds a ship to the grid array at the specified location and orientation.
        * PRE: Ship ship, Point location, boolean isVertical
        * POST: None
        */
        private void addShipToGridArray(Ship ship, Point location, boolean isVertical) {
            // Check if the location is within the valid range of the grid array
            if (location.getX() < gridArray.length && location.getX() >= 0 && location.getY() < gridArray.length
                    && location.getY() >= 0) {
                // Loop through each piece of the ship
                for (int i = 0; i < ship.getShipPieces().length; i++) {
                    // If the ship is vertical
                    if (isVertical) {
                        // Add the ship piece to the grid at the specified location with i added to the y-coordinate
                        gridArray[(int) location.getX()][(int) location.getY() + i] = ship.getShipPieces()[i];
                    } else {
                        // Add the ship piece to the grid at the specified location with i added to the x-coordinate
                        gridArray[(int) location.getX() + i][(int) location.getY()] = ship.getShipPieces()[i];
                    }
                }
            }
        }

        /*
        * Purpose: Rotates a ship panel between horizontal and vertical orientations.
        * PRE: JPanel panel
        * POST: boolean (true if rotation was successful, false otherwise)
        */
        private boolean rotatePanel(JPanel panel) {
            // If the panel has a horizontal (X_AXIS) box layout
            if (((BoxLayout) panel.getLayout()).getAxis() == BoxLayout.X_AXIS) {
                // Check if the panel is within the valid range for rotation
                if (panel.getX() > X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * gridArray.length) && !currentlyPlacingShip) {
                    return false;
                }
                // Set the layout to vertical (Y_AXIS)
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                // Swap the width and height of the panel
                int temp = panel.getWidth();
                int temp2 = panel.getHeight();
                panel.setSize(temp2, temp);
                // Replace all horizontal (X_AXIS) padding with vertical (Y_AXIS) padding
                for (int i = 0; i < panel.getComponentCount(); i++) {
                    if (!panel.getComponent(i).getClass().toString().equals("JPanel")) {
                        panel.add(Box.createRigidArea(new Dimension(0, BORDER_SIZE + 2)), i);
                        panel.remove(++i);
                    }
                }
                // Add final padding to the panel and remove the last component
                panel.add(Box.createRigidArea(new Dimension(0, 0)), 0);
                panel.remove(1);
                // Revalidate the panel to force the layout to update
                panel.validate();
                // Set the panel's location to its current coordinates
                panel.setLocation(panel.getX(), panel.getY());

                // Calculate the length of the ship based on the Y-axis
                int counter = 0;
                while (Y_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter) < panel.getY() + panel.getWidth()) {
                    counter++;
                }
                counter--;

                // Check if the panel is intersecting another panel or is partially off the grid
                if (!(counter <= gridArray[0].length - panel.getHeight() / TILE_SIZE && counter >= 0)
                        || isIntersection(panel)) {
                    return false;
                }
            } else if (((BoxLayout) panel.getLayout()).getAxis() == BoxLayout.Y_AXIS) {
                // Set the layout to horizontal (X_AXIS)
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                // Swap the width and height of the panel
                int temp = panel.getWidth();
                int temp2 = panel.getHeight();
                panel.setSize(temp2, temp);
                // Replace all vertical (Y_AXIS) padding with horizontal (X_AXIS) padding
                for (int i = 0; i < panel.getComponentCount(); i++) {
                    if (!panel.getComponent(i).getClass().toString().equals("JPanel")) {
                        panel.add(Box.createRigidArea(new Dimension(BORDER_SIZE + 2, 0)), i);
                        panel.remove(++i);
                    }
                }
                // Add final padding to the panel and remove the last component
                panel.add(Box.createRigidArea(new Dimension(0, 0)), 0);
                panel.remove(1);
                // Revalidate the panel to force the layout to update
                panel.validate();
                // Set the panel's location to its current coordinates
                panel.setLocation(panel.getX(), panel.getY());

                // Calculate the length of the ship based on the X-axis
                int counter = 0;
                while (X_ORIGIN + ((TILE_SIZE + BORDER_SIZE) * counter) < panel.getX() + panel.getHeight()) {
                    counter++;
                }
                counter--;

                // Check if the panel is intersecting another panel or is partially off the grid
                if (!(counter <= gridArray.length - panel.getWidth() / TILE_SIZE && counter >= 0)
                        || isIntersection(panel)) {
                    return false;
                }
            }
            // Return true indicating the rotation was successful
            return true;
        }

        /*
        * Purpose: Returns the current grid array.
        * PRE: None
        * POST: gridArray
        */
        public Object[][] getGridArray() {
            return gridArray;
        }

        /*
        * Purpose: Returns whether the setup phase is over.
        * PRE: None
        * POST: boolean (true if setup is over, false otherwise)
        */
        public boolean isSetupOver() {
            return setupOver;
        }

        /*
        * Purpose: Returns the end setup button.
        * PRE: None
        * POST: endSetup
        */
        public JButton getButton() {
            return endSetup;
        }
}
