package ca.mcgill.ecse223.quoridor.controller;

import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;

public class Controller {

	// ------------------------------------------------------------------------------
	/**
	 * <p>
	 * Start New Game
	 * <p>
	 * <p>
	 * Initialize a new Game object.
	 * 
	 * @return the created initialized Game object
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 * @throws Throwable
	 */
	public static Game StartNewGame() {
		return null;
	}

	/**
	 * <p>
	 * Provide or Select User Name
	 * <p>
	 * <p>
	 * Lets the user select an existing user name that was used in a previous game
	 * or enter a new one.
	 * 
	 * @param user
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void provideOrSelectUserName(User user) {
	}

	/**
	 * <p>
	 * Set Total Thinking Time
	 * <p>
	 * <p>
	 * Set the total thinking time for player.
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void setTotalThinkingTime() {
	}

	/**
	 * <p>
	 * Start the Clock
	 * <p>
	 * <p>
	 * Initiates the game timer which starts when the game is running.
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void startClock() {
	}

	/**
	 * <p>
	 * Select an Existing Username
	 * <p>
	 * <p>
	 * The user selects an existing user name that was previously used in a game
	 * 
	 * @param username is a String that is the existing user name
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void selectExistingUsername(String username) {
	}

	/**
	 * <p>
	 * Provide a New Username
	 * <p>
	 * <p>
	 * The user selects/enters a new user name that was not previously used in a
	 * game
	 * 
	 * @param username is a String that is the new user name
	 * 
	 * @author Ali Tapan
	 * @verison 1.0
	 */
	public static void provideNewUsername(String username) {
	}

	/**
	 * @author Sam Perreault Sets the starting thinking time for the players. Time
	 *         is accepted as minutes and seconds, and is converted to milliseconds.
	 * @param minute The number of minutes allowed to each player
	 * @param second The number of seconds allowed to each player
	 */
	public static void setPlayerThinkingTime(int minute, int second) {
	}

	/**
	 * @author Sam Perreault Generates the board, and sets the starting position and
	 *         walls of each player. In addition, sets white/player 1 as the player
	 *         to move, and starts counting down the white player's thinking time.
	 */
	public static void initializeBoard() {
	}

	/**
	 * @author Luke Barber Grabs a given wall and holds it so that it is ready for
	 *         use.
	 * @param wall The wall that will be grabbed
	 */
	public static void grabWall(Wall wall) {
	}
	// TODO Auto-generated method stub

	/**
	 * @author Luke Barber Rotates a given wall that is on the board.
	 * @param wall The wall that will be rotated
	 */
	public static void rotateWall(Wall wall) {
	}

	/**
	 * Part of Feature 7: Move Wall
	 * 
	 * moveWall method that allows a player to move the wall that they have in their
	 * hand over the board. It allows the user to move to a certain side and updates
	 * target tiles based off it. We will increment the movement of walls by either
	 * column or row and 1 unit at a time. Returns false when errors arise.
	 * 
	 * @param side
	 * @return
	 */
	public static boolean moveWall(String side) throws UnsupportedOperationException {
		// Global variables to make life easier

		// Gets the Current game
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		// Gets the current wall move candidate in play
		WallMove currentWallMoveCandidate = currentGame.getWallMoveCandidate();

		GamePosition currentPosition = currentGame.getCurrentPosition();
		// Gets current player to move
		Player currentPlayer = currentPosition.getPlayerToMove();
		// Gets Current board
		Board currentBoard = QuoridorApplication.getQuoridor().getBoard();
		// Gets move number
		int currentMoveNumber = currentGame.numberOfMoves();
		// Gets round number
		int currentRoundNumber = (currentGame.numberOfMoves() + 1) / 2; // Ex: move 5 and 6 are round 3
		// Fetch initial Wall Move Candidate
		WallMove wallMove = currentWallMoveCandidate;

		// Store position of target tile
		int row = wallMove.getTargetTile().getRow();
		int col = wallMove.getTargetTile().getColumn();

		// Get index of current tile
		Tile currentTile = currentBoard.getTile((row - 1) * 9 + col - 1);

		// Initialize new targets as old ones for now
		int targetRow = row;
		int targetCol = col;

		// Move wall <side> and update parameters of target tile
		switch (side) {
		case "left": {
			targetCol = col - 1; // Left from the perspective of the white player going towards black player
			break;
		}
		case "right": {
			targetCol = col + 1; // Right from the perspective of the white player going towards black player
			break;
		}
		case "up": {
			targetRow = row - 1; // Up means towards black players side
			break;
		}
		case "down": {
			targetRow = row + 1; // Down means away from black players side
			break;
		}
		}

		// Give error if wall is not on board
		if (targetRow < 1 || targetRow > 8 || targetCol < 1 || targetCol > 8) { // Row, col cannot be bigger than 8
																				// since reference point is NW
			// tile

			// Keep Same Target tile
			currentWallMoveCandidate.setTargetTile(currentTile);

			boolean wallNotMoved = false;
			return wallNotMoved; // Not a valid wall placement

		} else {
			// Make a new updated target tile with new parameters
			Tile updatedTile = currentBoard.getTile((targetRow - 1) * 9 + targetCol - 1);

			// Else, update wall move candidate with new target tile

			currentWallMoveCandidate.setTargetTile(updatedTile);

			/*
			 * WallMove updatedWallMoveCandidate = new WallMove(currentMoveNumber,
			 * currentRoundNumber, currentPlayer, newTargetTile, currentGame,
			 * currentWallDirection, currentWallPlaced); // Update the Wall Move Candidate
			 * with new Target Positions currentWallMoveCandidate =
			 * updatedWallMoveCandidate;
			 */

			// Return wallMoved if works
			boolean wallMoved = true;
			return wallMoved;
		}
	}

	/**
	 * <p>
	 * 8. Drop Wall
	 * <p>
	 * <p>
	 * dropWall method that allows player to place the wall after navigating to the
	 * designated (and valid) area in order to register the wall placement as a move
	 * for the player.
	 * <p>
	 * 
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 */
	public static Wall dropWall(WallMove wallMoveCandidate) {

		// Initial Parameters of game
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		Player player = currentGamePosition.getPlayerToMove();

		
		// -------- Validate Wall Position-------------
		// Todo CHECK WITH WILLIAM TO SEE IF BELOW PARTS CAN BE REPLACED BY HIS METHODS
		// Get a list of all walls on board
		List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
		List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();

		// Check black walls on board
		for (Wall wall : blackWallsOnBoard) { // For each wall placed by black player on the board
			if (isWallAlreadyPresent(wallMoveCandidate, wall.getMove())) { // If wall is already present at target
				System.out.println("wall present black");
													// location
				cancelWallMove();
				return null; // Return wall not dropped
			}
		}

		// Check white walls on board
		for (Wall wall : whiteWallsOnBoard) { // For each wall placed by white player on the board
			if (isWallAlreadyPresent(wallMoveCandidate, wall.getMove())) { // If wall is already present at target
				System.out.println("wall present white");
													// location
				cancelWallMove();

				return null;// Return wall not dropped
			}
		}
		
		// ----------- Now drop the wall -------
		
		// Update parameters of game:
		currentGame.addMove(wallMoveCandidate); // Stores move	
		
	
		// Update player info
		if (player.equals(currentGame.getWhitePlayer())) {
			currentGamePosition.addWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());// Also increments number of																		// walls on board
			currentGamePosition.setPlayerToMove(currentGame.getBlackPlayer());// Update player to black player)
			currentGame.setWallMoveCandidate(null);// Refreshes wall move candidate
			return wallMoveCandidate.getWallPlaced();

		} else if (player.equals(currentGame.getBlackPlayer())) {
			currentGamePosition.addBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());
			currentGamePosition.setPlayerToMove(currentGame.getWhitePlayer()); // Update player to white player
			currentGame.setWallMoveCandidate(null);// Refreshes wall move candidate
			return wallMoveCandidate.getWallPlaced();
		} else {
			return null;
		}
	}

	/**
	 * @author arneetkalra isWallAlreadyPresent checks to see if a wall is already
	 *         placed where a new wall is wanting to be placed. Returns true if wall
	 *         is already present.
	 * 
	 * @param WallMove wallOnBoard
	 * @param WallMove wallCandidate
	 * @return Boolean
	 */
	private static Boolean isWallAlreadyPresent(WallMove wallOnBoard, WallMove wallCandidate) {
		// Get tiles for onBoard and Candidate
		Tile tileOnBoard = wallOnBoard.getTargetTile();
		Tile tileCandidate = wallCandidate.getTargetTile();

		// Verify overlap status:
		Boolean isSameColumn = (tileOnBoard.getColumn() == tileCandidate.getColumn());
		Boolean isSameRow = (tileOnBoard.getRow() == tileCandidate.getRow());
		
		// Check if directions are both vertical
		if (wallOnBoard.getWallDirection() == Direction.Vertical
				&& wallCandidate.getWallDirection() == Direction.Vertical) {
			// Then verify if column and row are identical and return boolean
			return (isSameColumn
					&& Math.abs(tileOnBoard.getRow() - tileCandidate.getRow()) <= 1); // Checks if rows are off by more
																						// than 1
		}
		// Check if directions are both horizontal
		else if (wallOnBoard.getWallDirection() == Direction.Horizontal
				&& wallCandidate.getWallDirection() == Direction.Horizontal) {
			// Then verify if column and row are identical and return boolean
			return (isSameRow
					&& Math.abs(tileOnBoard.getColumn() - tileCandidate.getColumn()) <= 1);// Checks if columns are off
																							// by more than 1
		}
		// Case when Directions are opposite
		else {
			// Only overlaps if NorthWest tile is the same for both walls
			return (isSameColumn && isSameRow);
		}
	}

	/**
	 * Part of Feature 8: Drop Wall
	 * 
	 * cancelWallMove runs when there is an invalid wallMoveCandidate.
	 * 
	 * @author arneetkalra
	 * @return boolean true if wall move was successfully cancelled
	 */
	public static boolean cancelWallMove() {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		currentGame.getCurrentPosition().getPlayerToMove();
		QuoridorApplication.getQuoridor().getBoard();

		// Get current Wall Move and it's player
		WallMove wallMove = currentGame.getWallMoveCandidate();
		Player player = currentGamePosition.getPlayerToMove();

		// Check if there is a move
		if (wallMove == null) {
			return false;
		}

		// If it is white players move
		if (player.equals(currentGame.getWhitePlayer())) {
			currentGamePosition.addWhiteWallsInStock(wallMove.getWallPlaced()); // Puts wall attempted to be placed back
																				// in
			// their stock
		}
		// Black player move
		else if (player.equals(currentGame.getBlackPlayer())) {
			currentGamePosition.addBlackWallsInStock(wallMove.getWallPlaced()); // Puts wall attempted to be placed back
																				// in
			// their stock
		} else {
			return false; // Some unexpected error
		}
		// currentGame.setWallMoveCandidate(null);
		return true;
	}

	/**
	 * 
	 * Load the game from the game file. load the correct player position and wall
	 * position
	 * 
	 * @author Yin
	 * @param quoridor This is the quoridor you want to load the game into
	 * @param fileName This is the name of the file which stores the game
	 * 
	 */
	public static Quoridor loadPosition(Quoridor quoridor, String fileName) {
		return null;
	}

	/**
	 * Save the game into a game file
	 * 
	 * @author Yin
	 * @param fileName
	 */
	public static void savePosition(String fileName, GamePosition gamePosition) {
	}

	/**
	 * @author Yin Zhang 260726999 The user confirm whether to overwrite the
	 *         existing file
	 */
	public static void confirmsToOverWrite() {
	}

	/**
	 * <p>
	 * 11 Validate Position
	 * <p>
	 * <p>
	 * validate if the player positions and wall positions are valid e.g.
	 * overlapping walls or outof-track pawn or wall positions.
	 * <p>
	 * 
	 * @author William Wang
	 * @param position the currentPosition object of the game
	 * @return the validation result, true for pass, false for error
	 */
	public static boolean validatePosition(GamePosition position) {
		return false;
	}

	/**
	 * <p>
	 * 12. Switch player (aka. Update board)
	 * <p>
	 * <p>
	 * Switch current player and update clock
	 * <p>
	 * 
	 * @author William Wang
	 * @param game the current quoridor game
	 */
	public static void switchCurrentPlayer(Game game) {
	}
}
