
/**
 * Insaf Ozge Berktas - 150118072 & Ahmet Onat Ozalan - 150118054
 * This program creates a puzzle-like game. You have to move the ball
 * from starting tile to ending tile by making the correct path for the
 * ball. You can drag tiles to move them, only brown tiles can be moved
 * and they can be moved only there is an empty free tile next to them.
 * Also tiles can move only vertically and horizontally, they can't move
 * diagonally.
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/** This class displays the game board and allows the user to play the game. */
public class BoardPane extends Pane {
	// This variable keeps the types and properties of tiles in order.
	private String[] levelInfo = new String[16];
	
	// This variable indicates the current level.
	private int level = 1;
	
	// This variable indicates the number of moves.
	private int moveCount;
	
	// This variable is used for displaying the move count.
	private Text moveCountText;
	
	// This variable is used for displaying a message if the user selects
	// an invalid source tile or target tile.
	private Text text;

	/**
	 * This method creates an imageView for each tile according to the information
	 * stored in levelInfo, initialize the move count text, and adds them to the pane 
	 * for displaying them.
	 */
	public void loadLevel() {
		readLevelInfo();

		for (int i = 0; i < 16; i++) {
			ImageView tile = new ImageView("images/" + levelInfo[i]);

			// Each tile becomes 75x75 square after the two following statements invoked.
			tile.setFitWidth(75);
			tile.setFitHeight(75);

			// The two following statements place the tiles in the pane as if the pane is a
			// 4x4 grid. The pane will become 300x300 square after that statements because
			// the tiles are 75x75 square.
			tile.setX((i % 4) * 75);
			tile.setY((i / 4) * 75);

			getChildren().add(tile);
			configureEventHandler(tile);
		}
		
		// These statements add the move count as a text below the tiles.
		moveCountText = new Text("Move: " + moveCount);
		moveCountText.setX(5);
		moveCountText.setY(315);
		getChildren().add(moveCountText);
	}

	/**
	 * This method reads the level information from the input file for that
	 * level and writes those informations to levelInfo string array.
	 */
	private void readLevelInfo() {
		try {
			File file = new File("input files/level" + level + ".txt");
			Scanner reader = new Scanner(file);

			for (int i = 0; i < 16; i++) {
				levelInfo[i] = reader.nextLine().toLowerCase();

				// parts array is for storing the id, type and property values
				// of a tile in different memory locations. This makes the things
				// easier when loadLevel method creates an imageView for each tile.
				String[] parts = new String[3];
				parts = levelInfo[i].split(",");

				levelInfo[i] = parts[1] + "_" + parts[2] + ".jpg";
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}	
	}

	/**
	 * This method allows the user to drag tiles. It calls the isSourceValid and isTargetValid
	 * method to check whether the source tile and target tile is suitable for moving operation.
	 */
	private void configureEventHandler(ImageView tile) {
		tile.setOnMouseReleased(e -> {
			// This variable is used when displaying a message after the user completes the 
			// level and finishes the game.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Congrulations");
			alert.setHeaderText(null);
			
			// Delete the previous error text (if it exits) displayed when the user selected
			// an invalid source tile or target tile.
			if (getChildren().size() == 18) {
				getChildren().remove(17);
			}
			
			// This if - else block checks whether the source tile is suitable for moving operation.
			if (isSourceValid(tile)) {
				// ((int)e.getX() / 75) * 75 and (int)e.getY() / 75) * 75 values find the
				// coordinates of the tile which the mouse is released on it.
				ImageView targetTile = getTile(((int)e.getX() / 75) * 75, ((int)e.getY() / 75) * 75);

				// This if else block checks whether the target tile is suitable for moving
				// operation.
				if (isTargetValid(tile, targetTile)) {
					// These statements replaces the coordinates of the source tile and target tile.
					int tempX = (int)tile.getX();
					int tempY = (int)tile.getY();
					tile.setX(targetTile.getX());
					tile.setY(targetTile.getY());
					targetTile.setX(tempX);
					targetTile.setY(tempY);
					
					// Increase the move count by 1 and modify the text.
					moveCount++;
					moveCountText.setText("Move: " + moveCount);
					
					// This if block checks whether the level is completed.
					if (isLevelCompleted()) {
						if (level < 5) {
							// These statements configure the text will be displayed after
							// the user completes the level.
							alert.setContentText("You have completed the level " + level + "!");
							alert.showAndWait();
							
							// Reset the move count.
							moveCount = 0;
							
							// These statements allows the program to move to the next level.
							// getChildren().clear() method is necessary to avoid some errors.
							level++;
							getChildren().clear();
							loadLevel();
						}
						else {
							// The statements configure the text will be displayed after
							// the user finishes the game.
							alert.setContentText("You have finished the game!");
							alert.showAndWait();
							
							// Terminate the program after the game finishes.
							System.exit(0);
						}
					}
				}
				else {
					// These statements configure the text will be displayed if the user
					// selects an invalid target tile.
					text = new Text("Select a valid target tile.");
					text.setX(5);
					text.setY(330);
					getChildren().add(text);
				}
			}
			else {
				// These statements configure the text will be displayed if the user
				// selects an invalid source tile. 
				text = new Text("Select a valid source tile.");
				text.setX(5);
				text.setY(330);
				getChildren().add(text);
			}
		});
	}

	/**
	 * This method checks whether the source tile is suitable for moving operation. 
	 * If the source tile is a brown colored pipe or empty-free tile, it is suitable.
	 */
	private boolean isSourceValid(ImageView sourceTile) {
		if (sourceTile.getImage().getUrl().endsWith("pipe_horizontal.jpg") ||
			sourceTile.getImage().getUrl().endsWith("pipe_vertical.jpg") ||
			sourceTile.getImage().getUrl().endsWith("pipe_00.jpg") ||
			sourceTile.getImage().getUrl().endsWith("pipe_01.jpg") ||
			sourceTile.getImage().getUrl().endsWith("pipe_10.jpg") ||
			sourceTile.getImage().getUrl().endsWith("pipe_11.jpg") ||
			sourceTile.getImage().getUrl().endsWith("empty_none.jpg")) {
			return true;
		}

		return false;
	}
	
	/**
	 * This method takes x and y coordinates as parameters and finds the tile which
	 * has the same coordinates with given parameters.
	 */
	private ImageView getTile(int coordinateX, int coordinateY) {
		// For loop checks every object in the pane to find the matching tile.
		for (int i = 0; i < 16; i++) {
			if ((int)(((ImageView) getChildren().get(i)).getX()) == coordinateX &&
				(int)(((ImageView) getChildren().get(i)).getY()) == coordinateY) {
				return (ImageView) (getChildren().get(i));
			}
		}
		
		return null;
	}

	/** This method checks whether the target tile is suitable for moving operation. */
	private boolean isTargetValid(ImageView sourceTile, ImageView targetTile) {
		// This if statements checks whether the target tile is empty-free.
		// Target tile can be only empty-free.
		if (!(targetTile.getImage().getUrl().endsWith("empty_free.jpg"))) {
			return false;
		}

		// To move a movable tile, the user must drag it on the empty-free tile
		// and empty-free tile must be next to the movable tile. Previous if block
		// checked the first situation. This if block checks the second situation.
		// If movable tile and empty-free tile are next to each other, the sum of
		// the absolute value of the difference of x-coordinates and the absolute 
		// value of the difference of y-coordinates must be equal to 75. This if
		// block checks whether that sum is equal to 75.
		int sourceX = (int)sourceTile.getX();
		int sourceY = (int)sourceTile.getY();
		int targetX = (int)targetTile.getX();
		int targetY = (int)targetTile.getY();
		int coordinateDifference = Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);

		if (coordinateDifference != 75) {
			return false;
		}

		return true;
	}
	
	/**
	 * This method defines the solution for each level. It indicates that some tiles
	 * must be vertical, horizontal, etc.
	 * The board pane can be thought as a 4x4 grid. In this method, ImageView variables
	 * are named according to that perspective. For example, tile2_1 means the tile in 2nd row
	 * and 1st column.
	 */
	private boolean isLevelCompleted() {
		if (level == 1 || level == 2 || level == 3) {
			ImageView tile2_1 = getTile(0, 75);
			ImageView tile3_1 = getTile(0, 150);
			ImageView tile4_1 = getTile(0, 225);
			ImageView tile4_2 = getTile(75, 225);

			if (tile2_1.getImage().getUrl().endsWith("pipe_vertical.jpg") &&
				tile3_1.getImage().getUrl().endsWith("pipe_vertical.jpg") &&
				tile4_1.getImage().getUrl().endsWith("pipe_01.jpg") &&
				tile4_2.getImage().getUrl().endsWith("pipe_horizontal.jpg")) {
				return true;
			}
		}
		else if (level == 4) {
			ImageView tile3_1 = getTile(0, 150);
			ImageView tile3_2 = getTile(75, 150);
			ImageView tile3_3 = getTile(150, 150);
			ImageView tile3_4 = getTile(225, 150);

			if (tile3_1.getImage().getUrl().endsWith("pipe_01.jpg") &&
				tile3_2.getImage().getUrl().endsWith("pipe_horizontal.jpg") &&
				tile3_3.getImage().getUrl().endsWith("pipe_horizontal.jpg") &&
				tile3_4.getImage().getUrl().endsWith("pipe_00.jpg")) {
				return true;
			}
		}
		else {
			ImageView tile2_1 = getTile(0, 75);
			ImageView tile3_2 = getTile(75, 150);
			ImageView tile3_3 = getTile(150, 150);
			ImageView tile3_4 = getTile(225, 150);

			if (tile2_1.getImage().getUrl().endsWith("pipe_vertical.jpg") &&
				tile3_2.getImage().getUrl().endsWith("pipe_horizontal.jpg") &&
				tile3_3.getImage().getUrl().endsWith("pipe_horizontal.jpg") &&
				tile3_4.getImage().getUrl().endsWith("pipe_00.jpg")) {
				return true;
			}
		}
		
		return false;
	}
}