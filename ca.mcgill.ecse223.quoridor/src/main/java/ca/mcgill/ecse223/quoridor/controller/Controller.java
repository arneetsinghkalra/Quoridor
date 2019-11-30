package ca.mcgill.ecse223.quoridor.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorPersistence;
import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;

public class Controller {

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
	 * @version 2.0
	 */
	public static Game startNewGame() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);
		// quoridor.setCurrentGame(game);
		return game;
	}

	/**
	 * <p>
	 * Initialize White Player
	 * <p>
	 * <p>
	 * Initializes a white player and assigns it
	 * <p>
	 * 
	 * @param username
	 * @return Player object
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static Player initWhitePlayer(String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user = quoridor.addUser(username);
		Player p = new Player(Time.valueOf("00:01:00"), user, 1, Direction.Vertical);
		quoridor.getCurrentGame().setWhitePlayer(p);
		return p;
	}

	/**
	 * <p>
	 * Initialize Black Player
	 * <p>
	 * <p>
	 * Initialize a black player and assigns it
	 * <p>
	 * 
	 * @param username
	 * @return Player object
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static Player initBlackPlayer(String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user = quoridor.addUser(username);
		Player p = new Player(Time.valueOf("00:01:00"), user, 9, Direction.Vertical);
		quoridor.getCurrentGame().setBlackPlayer(p);
		return p;
	}

	/**
	 * <p>
	 * Set Total Thinking Time
	 * <p>
	 * <p>
	 * Set the total thinking time for player.
	 * 
	 * @author Ali Tapan
	 * @version 2.0
	 */
	public static void setTotalThinkingTime(String time) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		String error = "";
		Time remaining = null;

		try {
			remaining = Time.valueOf(time);
			remaining = new Time(remaining.getTime() - 5 * 1000 * 3600);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(remaining);
		quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(remaining);
		quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
	}

	/**
	 * <p>
	 * Start the Clock
	 * <p>
	 * <p>
	 * Initiates the game timer which starts when the game is running.
	 * 
	 * @author Ali Tapan
	 * @version 2.0
	 */
	public static void startClock() {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		quoridor.getCurrentGame().setGameStatus(GameStatus.Running);
	}

	/**
	 * <p>
	 * Select an Existing Username
	 * <p>
	 * <p>
	 * The user selects an existing user name that was previously used in a game
	 * 
	 * @param username is a String that is the existing user name
	 * @returns a Boolean, true if it successfully sets the username, false
	 *          otherwise
	 * @author Ali Tapan
	 * @version 2.0
	 */
	public static Boolean selectExistingUsername(String username, Player player) {

		if (username.equals(null)) {
			return false;
		}
		if (User.hasWithName(username) == false) {
			return false;
		}

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		QuoridorPersistence.load();
		// Iterate through the users to see if they match the user name provided
		for (int i = 0; i < quoridor.numberOfUsers(); i++) {
			User user = quoridor.getUser(i);
			if (user.getName().equals(username)) {
				player.setUser(user);
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * List of All Existing Usernames from the previous games
	 * <p>
	 * 
	 * @return Array of Usernames that were previously used
	 * 
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static String[] listExistingUsernames() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<String> list = new ArrayList<String>();
		QuoridorPersistence.load();
		for (User u : quoridor.getUsers()) {
			list.add(u.getName());
		}
		list.add(0, "or select existing username...");
		return list.toArray(new String[list.size()]);
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
	 * @return returns a Boolean, true if it successfully sets the username, false
	 *         otherwise
	 * @author Ali Tapan
	 * @version 2.0
	 */
	public static Boolean provideNewUsername(String username, Player player) {
		if (User.hasWithName(username) == true) {
			return false;
		}
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		try {
			player.getUser().setName(username);
			QuoridorPersistence.save(quoridor);
		} catch (Exception e) {
			e.getMessage();
		}
		// User user = quoridor.addUser(username);
		// player.setUser(user);

		return true;
	}

	/**
	 * @author Sam Perreault Creates the board object and its tiles
	 */
	public static void createBoard() {
		Quoridor q = QuoridorApplication.getQuoridor();
		Board board = new Board(q);
		Tile t;
		for (int i = 1; i < 10; i++)
			for (int j = 1; j < 10; j++)
				t = new Tile(i, j, board);
	}

	/**
	 * @author Sam Perreault Sets the starting position and walls of each player. In
	 *         addition, sets white/player 1 as the player to move, and starts
	 *         counting down the white player's thinking time.
	 */
	public static void initializeBoard() {
		Quoridor q = QuoridorApplication.getQuoridor();
		Board board = q.getBoard();
		PlayerPosition whitePlayerPosition = new PlayerPosition(q.getCurrentGame().getWhitePlayer(), board.getTile(76));
		PlayerPosition blackPlayerPosition = new PlayerPosition(q.getCurrentGame().getBlackPlayer(), board.getTile(4));
		GamePosition gp = new GamePosition(0, whitePlayerPosition, blackPlayerPosition,
				q.getCurrentGame().getWhitePlayer(), q.getCurrentGame());
		q.getCurrentGame().setCurrentPosition(gp);
		for (int i = 0; i < 10; i++) {
			Wall a, b;
			a = new Wall(i, q.getCurrentGame().getWhitePlayer());
			q.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(a);
			b = new Wall(i + 10, q.getCurrentGame().getBlackPlayer());
			q.getCurrentGame().getCurrentPosition().addBlackWallsInStock(b);
		}
		QuoridorWindow window = QuoridorApplication.quoridorWindow;
		window.setTimeRemaining((int) (q.getCurrentGame().getWhitePlayer().getRemainingTime().getTime()));
		window.createSecondTimer();
		window.setCurrentPlayer(window.whitePawn + " " + q.getCurrentGame().getWhitePlayer().getUser().getName() + " "
				+ window.whitePawn);
		window.setPlayerNames(q.getCurrentGame().getWhitePlayer().getUser().getName(),
				q.getCurrentGame().getBlackPlayer().getUser().getName());
		window.placePlayer(8, 4, 0, 4);
	}

	/**
	 * @author Sam Perreault Subtracts a second from the remaining time of the
	 *         current player. If the remaining time becomes zero, then the other
	 *         player wins
	 */
	public static void subtractSecond() {
		Quoridor q = QuoridorApplication.getQuoridor();

		Player curPlayer = q.getCurrentGame().getCurrentPosition().getPlayerToMove();
		long remaining = curPlayer.getRemainingTime().getTime();
		remaining -= 1000L;
		if (remaining == 0) {
			if (curPlayer.equals(q.getCurrentGame().getWhitePlayer()))
				q.getCurrentGame().setGameStatus(GameStatus.BlackWon);
			else
				q.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
			return;
		}
		curPlayer.setRemainingTime(new Time(remaining));

	}
	// Global variables to make life easier

	/**
	 * @author Luke Barber Returns a boolean on whether a wall is selected in the
	 *         stock
	 */
	public static boolean wallSelected() {
		QuoridorWindow quoridorWindow = QuoridorApplication.quoridorWindow;
		return quoridorWindow.wallSelected;
	}

	/**
	 * @author Luke Barber Checks the stock of the playerToMove and calls the notify
	 *         method in the view if the player's stock is 0
	 */
	public static void notifyNoMoreWalls() {
		List<Wall> stock = new ArrayList<Wall>();
		Quoridor quoridor = QuoridorApplication.getQuoridor();

		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
			stock = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock();
		} else if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
			stock = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock();
		}

		if (stock.size() == 0) {
			QuoridorWindow.warningNoMoreWalls();
		}
	}

	/**
	 * @author Luke Barber Grabs a given wall and holds it so that it is ready for
	 *         use.
	 * @param wall The wall that will be grabbed
	 */
	public static boolean grabWall(Wall wall) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		boolean grabbed = false;
		int initialRow = 1;
		int initialColumn = 1;
		Direction direction = Direction.Horizontal;
		Tile tile = quoridor.getBoard().getTile((initialRow - 1) * 9 + (initialColumn - 1));
		WallMove wallMoveCandidate = new WallMove(1, 1, currentPlayer, tile, quoridor.getCurrentGame(), direction,
				wall);
		quoridor.getCurrentGame().setWallMoveCandidate(wallMoveCandidate);
		wall.setMove(wallMoveCandidate);
		if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
			quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
		} else if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
			quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
		} else {
			return grabbed;
		}
		grabbed = true;
		return grabbed;
	}

	/**
	 * @author Luke Barber Rotates a given wall that is on the board.
	 * @param wall The wall to be rotated
	 */
	public static boolean rotateWall(Wall wall) {
		boolean rotated = false; // Boolean value signifying whether the wall has been rotated or not
		WallMove currentWallMove = wall.getMove(); // The wallMove associated with the given wall
		if (currentWallMove.getWallDirection().equals(Direction.Horizontal)) {
			currentWallMove.setWallDirection(Direction.Vertical);
		} else if (currentWallMove.getWallDirection().equals(Direction.Vertical)) {
			currentWallMove.setWallDirection(Direction.Horizontal);
		} else {
			return rotated;
		}
		wall.setMove(currentWallMove);
		rotated = true;
		return rotated;
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
		// Gets Current board
		Board currentBoard = QuoridorApplication.getQuoridor().getBoard();
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
	 * @author arneetkalra
	 * 
	 *         helper method for View to get the wall move direction
	 */
	public static Direction returnWallMoveDirection() {

		Direction direction = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
				.getWallDirection();
		return direction;
	}

	/**
	 * Helper method for view which returns the current wall move candidate
	 * 
	 * @author arneetkalra
	 * @return WallMove wallMoveCandidate
	 */
	public static WallMove returnWallMoveCandidate() {
		WallMove wallMoveCandidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		return wallMoveCandidate;
	}

	/**
	 * @author arneetkalra
	 * @param wallMoveCandidate
	 * @return
	 */
	public static boolean wallIsAlreadyPresent(WallMove wallMoveCandidate) {
		// Initial Parameters of game
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		Player player = currentGamePosition.getPlayerToMove();

		// Get a list of all walls on board
		List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
		List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();
		// Check black walls on board
		for (Wall wall : blackWallsOnBoard) { // For each wall placed by black player on the board
			if (isWallAlreadyPresent(wallMoveCandidate, wall.getMove())) { // If wall is already present at location
				return true; // Return wall not dropped
			}
		}

		// Check white walls on board
		for (Wall wall : whiteWallsOnBoard) { // For each wall placed by white player on the board
			if (isWallAlreadyPresent(wallMoveCandidate, wall.getMove())) { // If wall is already present at target
				return true;// Return wall not dropped
			}
		}
		return false;
	}

	public static boolean isAValidWallPosition() {

		return true;
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
	 * @return void method but drops wall which prompts end of player turn
	 */
	public static Wall dropWall(WallMove wallMoveCandidate) {

		// Initial Parameters of game
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		Player player = currentGamePosition.getPlayerToMove();

		// Get a list of all all the walls placed on the board
		List<Wall> allWallsOnBoard = getAllWallsOnBoard();

		// Check the validity of new wall based on the walls placed
		for (Wall wall : allWallsOnBoard) { // For each wall placed on the board
			if (isWallAlreadyPresent(wallMoveCandidate, wall.getMove())) { // If wall is already present at location
				cancelWallMove();
				return null; // Return wall not dropped
			}
		}


		// Check validity of the new wall based on path existence
        if(!checkIfPathExists(wallMoveCandidate.getTargetTile()))
            return null;
		// ----------- Now drop the wall -------

		// Update parameters of game:
		// currentGame.addMove(wallMoveCandidate); // Stores move

		// Update player info
		if (player.equals(currentGame.getWhitePlayer())) {
			currentGamePosition.addWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());// Also increments number of //
																						// walls on board
		int moveNumber = currentGame.numberOfMoves();
			System.out.println(moveNumber);

			currentGame.addMove(wallMoveCandidate);
			switchCurrentPlayer();
			currentGame.setWallMoveCandidate(null);// Refreshes wall move candidate
			return wallMoveCandidate.getWallPlaced();

		} else if (player.equals(currentGame.getBlackPlayer())) {
			currentGamePosition.addBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());
			int moveNumber = currentGame.numberOfMoves();
			System.out.println(moveNumber);
			currentGame.addMove(wallMoveCandidate);
			switchCurrentPlayer();
			currentGame.setWallMoveCandidate(null);// Refreshes wall move candidate

			return wallMoveCandidate.getWallPlaced();
		} else {
			return null;
		}
	}

	/**
	 * Helper method which returns a list of all walls on the board, both black and
	 * white.
	 *
	 * @author arneetkalra
	 * @return List<Wall>
	 */
	public static List<Wall> getAllWallsOnBoard() {
		// Initial Parameters of game
		GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();

		// Get a list of all walls on the board
		List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
		List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();

		// Make one list for all walls on the board:
		List<Wall> allWallsOnBoard = Stream.of(blackWallsOnBoard, whiteWallsOnBoard).flatMap(x -> x.stream())
				.collect(Collectors.toList());

		return allWallsOnBoard;
	}

	/**
	 * @author arneetkalra
	 * @param hoveredTile
	 * @param candidateDirection
	 * @return
	 */
	public static boolean hoveredWallIsValid(Tile hoveredTile, Direction candidateDirection) {

		List<Wall> allWallsOnBoard = getAllWallsOnBoard();

		// If there are walls on the board
		if (getAllWallsOnBoard().size() > 0) {
			for (Wall wall : allWallsOnBoard) {
				if (isWallAlreadyPresent(hoveredTile, candidateDirection, wall.getMove())|| !checkIfPathExists(hoveredTile)) {

					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @author arneetkalra
	 * @param hoveredTile
	 * @param candidateDirection
	 * @return
	 */
	public static int returnInvalidWallRow(Tile hoveredTile, Direction candidateDirection) {

		List<Wall> allWallsOnBoard = getAllWallsOnBoard();
		// If there are walls on the board
		if (getAllWallsOnBoard().size() > 0) {
			for (Wall wall : allWallsOnBoard) {
				if (isWallAlreadyPresent(hoveredTile, candidateDirection, wall.getMove())) {
					return wall.getMove().getTargetTile().getRow() - 1;
				}
			}
		}

		return 0;

	}

	/**
	 * @author arneetkalra
	 * @param hoveredTile
	 * @param candidateDirection
	 * @return
	 */
	public static int returnInvalidWallColumn(Tile hoveredTile, Direction candidateDirection) {

		List<Wall> allWallsOnBoard = getAllWallsOnBoard();

		// If there are walls on the board
		if (getAllWallsOnBoard().size() > 0) {
			for (Wall wall : allWallsOnBoard) {
				if (isWallAlreadyPresent(hoveredTile, candidateDirection, wall.getMove()) == true) {
					return wall.getMove().getTargetTile().getColumn() - 1;
				}
			}
		}

		return 0;
	}

	/**
	 * @author arneetkalra
	 * @param hoveredTile
	 * @param candidateDirection
	 * @return
	 */
	public static Direction returnInvalidWallDirection(Tile hoveredTile, Direction candidateDirection) {

		List<Wall> allWallsOnBoard = getAllWallsOnBoard();
		// If there are walls on the board
		if (!getAllWallsOnBoard().isEmpty()) {
			for (Wall wall : allWallsOnBoard) {
				if (isWallAlreadyPresent(hoveredTile, candidateDirection, wall.getMove())) {
					return wall.getMove().getWallDirection();
				}
			}
		}

		return null;
	}

	/**
	 * setDroppedWallTile is a helper method for the view to set
	 * 
	 * @author arneetkalra
	 */

	public static void setDroppedWallTileToCandidate(int row, int col) {
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row) * 9 + col);
		QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setTargetTile(targetTile);
	}

	/**
	 * @author arneetkalra
	 * @param row
	 * @param col
	 * @return
	 */
	public static Tile getDroppedWallTile(int row, int col) {
		Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row) * 9 + col);
		return targetTile;
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
		else { // (player.equals(currentGame.getBlackPlayer())) {
			currentGamePosition.addBlackWallsInStock(wallMove.getWallPlaced()); // Puts wall attempted to be placed back
																				// in
			// their stock
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
	 * @param fileName This is the name of the file which stores the game
	 * 
	 */
	public static Quoridor loadPosition(String fileName) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get("src/test/resources/savePosition/" + fileName),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// do something
			e.printStackTrace();
		}

		String firstLine = null;
		String secondLine = null;
		try {
			firstLine = lines.get(0);
			secondLine = lines.get(1);
		} catch (IndexOutOfBoundsException e) {
			throw new UnsupportedOperationException("Invalid position");
		}

		StringTokenizer first = new StringTokenizer(firstLine, ",");
		StringTokenizer second = new StringTokenizer(secondLine, ",");

		String nextPlayer = first.nextToken();
		String opponent = second.nextToken();

		int nextPlayerColumn = 0;
		int nextPlayerRow = 0;

		if (nextPlayer.toCharArray().length != 4 || opponent.toCharArray().length != 4) {
			throw new UnsupportedOperationException("Invalid position");
		}

		try {
			nextPlayerColumn = convertToInt(nextPlayer.substring(2, 3));
			nextPlayerRow = Integer.parseInt(nextPlayer.substring(3));
		} catch (IndexOutOfBoundsException e) {
			throw new UnsupportedOperationException("Invalid position");
		}

		if (nextPlayerRow > 9 || nextPlayerRow < 1 || nextPlayerColumn > 9 || nextPlayerColumn < 1) {
			throw new UnsupportedOperationException("Invalid position");
		}

		Tile nextPlayerTile = quoridor.getBoard().getTile((nextPlayerRow - 1) * 9 + nextPlayerColumn - 1);
		int opponentColumn = 0;
		int opponentRow = 0;
		try {
			opponentColumn = convertToInt(opponent.substring(2, 3));
			opponentRow = Integer.parseInt(opponent.substring(3));
		} catch (IndexOutOfBoundsException e) {
			throw new UnsupportedOperationException("Invalid position");
		}

		if (opponentRow > 9 || opponentRow < 1 || opponentColumn > 9 || opponentColumn < 1) {
			throw new UnsupportedOperationException("Invalid position");
		}

		Tile opponentTile = quoridor.getBoard().getTile((opponentRow - 1) * 9 + opponentColumn - 1);
		if (nextPlayer.substring(0, 1).equals("B")) {

			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
			quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(nextPlayerTile);
			quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(opponentTile);

			while (first.hasMoreTokens()) {
				int i = 0;
				Wall wall = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(i);
				String direction = null;
				int column = 0;
				int row = 0;
				String wallPosition = first.nextToken();
				if (wallPosition.toCharArray().length != 3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try {
					column = convertToInt(wallPosition.substring(0, 1));
					row = Integer.parseInt(wallPosition.substring(1, 2));
					direction = wallPosition.substring(2);
				} catch (IndexOutOfBoundsException e) {
					throw new UnsupportedOperationException("Invalid position");
				}
				if (row > 8 || row < 1 || column > 8 || column < 1) {
					throw new UnsupportedOperationException("Invalid position");
				}

				Tile tile = quoridor.getBoard().getTile((row - 1) * 9 + column - 1);
				WallMove move = new WallMove(0, 1, quoridor.getCurrentGame().getBlackPlayer(), tile,
						quoridor.getCurrentGame(), converToDir(direction), wall);
				wall.setMove(move);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				i++;
			}
			while (second.hasMoreTokens()) {
				int i = 0;
				Wall wall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(i + 1);
				int column = 0;
				int row = 0;
				String direction = null;
				String wallPosition = second.nextToken();
				if (wallPosition.toCharArray().length != 3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try {
					column = convertToInt(wallPosition.substring(0, 1));
					row = Integer.parseInt(wallPosition.substring(1, 2));
					direction = wallPosition.substring(2);
				} catch (IndexOutOfBoundsException e) {
					throw new UnsupportedOperationException("Invalid position");
				}
				if (row > 8 || row < 1 || column > 8 || column < 1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile((row - 1) * 9 + column - 1);
				WallMove move = new WallMove(0, 1, quoridor.getCurrentGame().getWhitePlayer(), tile,
						quoridor.getCurrentGame(), converToDir(direction), wall);
				wall.setMove(move);
				wall.setOwner(quoridor.getCurrentGame().getWhitePlayer());
				wall.getMove().setWallDirection(converToDir(direction));
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
				// dropWall(move);
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				i++;
			}

		} else if (nextPlayer.substring(0, 1).equals("W")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
			quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(nextPlayerTile);
			quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(opponentTile);
			while (first.hasMoreTokens()) {
				int i = 0;
				Wall wall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(i + 1);
				String direction = null;
				int column = 0;
				int row = 0;
				String wallPosition = first.nextToken();
				if (wallPosition.toCharArray().length != 3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try {
					column = convertToInt(wallPosition.substring(0, 1));
					row = Integer.parseInt(wallPosition.substring(1, 2));
					direction = wallPosition.substring(2);
				} catch (IndexOutOfBoundsException e) {
					throw new UnsupportedOperationException("Invalid position");
				}
				if (row > 8 || row < 1 || column > 8 || column < 1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile(row * column);
				WallMove move = new WallMove(0, 1, quoridor.getCurrentGame().getWhitePlayer(), tile,
						quoridor.getCurrentGame(), converToDir(direction), wall);
				wall.setMove(move);
				first.nextToken();
				wall.getMove().setWallDirection(converToDir(direction));
				wall.setOwner(quoridor.getCurrentGame().getBlackPlayer());
				quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard().add(wall);
				// dropWall(move);
				quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().remove(wall);
				i++;
			}
			while (second.hasMoreTokens()) {
				int i = 0;
				Wall wall = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(i);
				int column = 0;
				int row = 0;
				String direction = null;
				String wallPosition = second.nextToken();
				if (wallPosition.toCharArray().length != 3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try {
					column = convertToInt(wallPosition.substring(0));
					row = Integer.parseInt(wallPosition.substring(1));
					direction = wallPosition.substring(2);
				} catch (IndexOutOfBoundsException e) {
					throw new UnsupportedOperationException("Invalid position");
				}
				if (row > 8 || row < 1 || column > 8 || column < 1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile(row * column);
				WallMove move = new WallMove(0, 1, quoridor.getCurrentGame().getBlackPlayer(), tile,
						quoridor.getCurrentGame(), converToDir(direction), wall);
				second.nextToken();
				wall.setMove(move);
				quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard().add(wall);
				// Run drop wall
				// dropWall(move);
				quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().remove(wall);
				i++;
			}

		} else {
			throw new UnsupportedOperationException("Invalid position");
		}
		boolean sameRemainingWall = (quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock()
				.size() == quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
		System.out.print(validatePosition());
		System.out.print(sameRemainingWall);
		if (validatePosition() && sameRemainingWall) {
			int blackRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
			int blackColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			int whiteRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
			int whiteColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			QuoridorApplication.quoridorWindow.placePlayer(blackRow - 1, blackColumn - 1, whiteRow - 1,
					whiteColumn - 1);
			return quoridor;
		} else {
			throw new UnsupportedOperationException("Invalid position");
		}
	}

	/**
	 * Save the game into a game file
	 * 
	 * @author Yin
	 * @param fileName
	 */
	public static void savePosition(String fileName, GamePosition gamePosition, boolean confirms) throws IOException {
		String data = "";
		if (gamePosition.getPlayerToMove().getUser().getName()
				.equals(gamePosition.getBlackPosition().getPlayer().getUser().getName())) {
			data += blackPlayerData(gamePosition) + "\n";
			data += whitePlayerData(gamePosition);
		} else {
			data += whitePlayerData(gamePosition) + "\n";
			data += blackPlayerData(gamePosition);
		}
		Path path = Paths.get("src/test/resources/savePosition/" + fileName);
		if (Files.exists(path)) {
			if (confirms) {
				Files.delete(path);
				Files.createFile(path);
				Files.write(path, data.getBytes());

			}
		} else {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			Files.write(path, data.getBytes());
		}
	}
	public static void saveGame(String fileName, boolean confirms) throws IOException {
		String data = "";
		int result = -1;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(quoridor.getCurrentGame().getGameStatus().equals(Game.GameStatus.WhiteWon)) {
			result = 1;
		}
		if(quoridor.getCurrentGame().getGameStatus().equals(Game.GameStatus.BlackWon)) {
			result = 2;
		}
		if(quoridor.getCurrentGame().getGameStatus().equals(Game.GameStatus.Draw)) {
			result = 3;
		}
		if(quoridor.getCurrentGame().getGameStatus().equals(Game.GameStatus.Running)) {
			result = 0;
		}
		int num = quoridor.getCurrentGame().getMoves().size();
		int roundNum = (num+1)/2;
		int numIndex = 0;
		if(num%2==0) {
			for(int i =1; i<=roundNum; i++) {
				data += i+"."+ " " + moveData(quoridor.getCurrentGame().getMove(numIndex));
				numIndex++;
				data += " " + moveData(quoridor.getCurrentGame().getMove(numIndex))+"\n";
				numIndex++;
			}
		}else {
			for(int i =1; i<roundNum; i++) {
				data += i+"."+ " " + moveData(quoridor.getCurrentGame().getMove(numIndex));
				numIndex++;
				data += " " + moveData(quoridor.getCurrentGame().getMove(numIndex))+"\n";
				numIndex++;
			}
			data+= roundNum+"."+ " " + moveData(quoridor.getCurrentGame().getMove(numIndex))+"\n";
		}
		if(result == 1) {
			data += "1-0";
		}else if(result==2) {
			data += "0-1";
		}else if(result==3) {
			data += "0-0";
		}
		Path path = Paths.get("src/test/resources/saveGame/" + fileName);
		if (Files.exists(path)) {
			if (confirms) {
				Files.delete(path);
				Files.createFile(path);
				Files.write(path, data.getBytes());
			}
		} else {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			Files.write(path, data.getBytes());
		}
	}
	public static void loadGame(String fileName) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
		Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();
		Boolean finished = false;

		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get("src/test/resources/saveGame/" + fileName),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// do something
			e.printStackTrace();
		}
		int numRound = lines.size();
		StringTokenizer lastline = new StringTokenizer(lines.get(numRound-1));
		if(lastline.nextToken().toCharArray().length==3) {
			finished = true;
		}
		int whiteMoveColumn;
		int whiteMoveRow;
		int whiteWallMoveColumn;
		int whiteWallMoveRow;
		int blackMoveColumn;
		int blackMoveRow;
		int blackWallMoveColumn;
		int blackWallMoveRow;
		String whiteWallDirection;
		String blackWallDirection;
		if(!finished) {
			for(int i = 0; i<numRound;i++) {
				StringTokenizer moveToken = new StringTokenizer(lines.get(i));
				int rNumber = Integer.parseInt(moveToken.nextToken().substring(0,1));
				boolean two = (moveToken.countTokens()==2);
				boolean one = (moveToken.countTokens()==1);
				if(two) {
					String whiteMoveTile = moveToken.nextToken();
					String blackMoveTile = moveToken.nextToken();
					if(whiteMoveTile.toCharArray().length==2) {
						try {
							whiteMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteMoveRow = Integer.parseInt(whiteMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteMoveRow > 9 || whiteMoveRow < 1 || whiteMoveColumn > 9 || whiteMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteTile = quoridor.getBoard().getTile((whiteMoveRow - 1) * 9 + whiteMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,whitePlayer,whiteTile,quoridor.getCurrentGame());
						boolean returnvalue = loadMove(whiteTile.getColumn(),whiteTile.getRow(),whitePlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid Game");
						}
	
						//System.out.println("111111111111111111");
					}else if(whiteMoveTile.toCharArray().length==3) {
						try {
							whiteWallMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteWallMoveRow = Integer.parseInt(whiteMoveTile.substring(1,2));
							whiteWallDirection = whiteMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteWallMoveRow > 8 || whiteWallMoveRow < 1 || whiteWallMoveColumn > 8 || whiteWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteWallTile = quoridor.getBoard().getTile((whiteWallMoveRow - 1) * 9 + whiteWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,whitePlayer,whiteWallTile,quoridor.getCurrentGame(),converToDir(whiteWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						//System.out.println("1444444444444444441");
	
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
					
					if(blackMoveTile.toCharArray().length==2) {
						try {
							blackMoveColumn = convertToInt(blackMoveTile.substring(0,1));
							blackMoveRow = Integer.parseInt(blackMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (blackMoveRow > 9 || blackMoveRow < 1 || blackMoveColumn > 9 || blackMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile blackTile = quoridor.getBoard().getTile((blackMoveRow - 1) * 9 + blackMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,blackPlayer,blackTile,quoridor.getCurrentGame());
						boolean returnvalue = loadMove(blackTile.getColumn(),blackTile.getRow(),blackPlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						//System.out.println("222222222222222222222");
	
					}else if(blackMoveTile.toCharArray().length==3) {
						try {
							blackWallMoveColumn = convertToInt(blackMoveTile.substring(0,1));
							blackWallMoveRow = Integer.parseInt(blackMoveTile.substring(1,2));
							blackWallDirection = blackMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (blackWallMoveRow > 8 || blackWallMoveRow < 1 || blackWallMoveColumn > 8 || blackWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile blackWallTile = quoridor.getBoard().getTile((blackWallMoveRow - 1) * 9 + blackWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,blackPlayer,blackWallTile,quoridor.getCurrentGame(),converToDir(blackWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						//System.out.println("33333333333333");
	
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
				}else if(one) {
					String whiteMoveTile = moveToken.nextToken();
					if(whiteMoveTile.toCharArray().length==2) {
						try {
							whiteMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteMoveRow = Integer.parseInt(whiteMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteMoveRow > 9 || whiteMoveRow < 1 || whiteMoveColumn > 9 || whiteMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteTile = quoridor.getBoard().getTile((whiteMoveRow - 1) * 9 + whiteMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,whitePlayer,whiteTile,quoridor.getCurrentGame());
						boolean returnvalue = loadMove(whiteTile.getColumn(),whiteTile.getRow(),whitePlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid Game");
						}
					}else if(whiteMoveTile.toCharArray().length==3) {
						try {
							whiteWallMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteWallMoveRow = Integer.parseInt(whiteMoveTile.substring(1,2));
							whiteWallDirection = whiteMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteWallMoveRow > 8 || whiteWallMoveRow < 1 || whiteWallMoveColumn > 8 || whiteWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteWallTile = quoridor.getBoard().getTile((whiteWallMoveRow - 1) * 9 + whiteWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,whitePlayer,whiteWallTile,quoridor.getCurrentGame(),converToDir(whiteWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
				}else {
					throw new UnsupportedOperationException("Invalid Game");
				}
			}
	//		if(CheckIfGAMEWON) {
	//			REPORTFINAL;
	//			ENTERREPLAYMODE;
	//		}
			quoridor.getCurrentGame().setGameStatus(Game.GameStatus.ReadyToStart);
		}else {
			for(int i = 0; i<numRound-1;i++) {
				StringTokenizer moveToken = new StringTokenizer(lines.get(i));
				int rNumber = Integer.parseInt(moveToken.nextToken().substring(0,1));
				boolean two = (moveToken.countTokens()==2);
				boolean one = (moveToken.countTokens()==1);
				if(two) {
					String whiteMoveTile = moveToken.nextToken();
					String blackMoveTile = moveToken.nextToken();
					if(whiteMoveTile.toCharArray().length==2) {
						try {
							whiteMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteMoveRow = Integer.parseInt(whiteMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteMoveRow > 9 || whiteMoveRow < 1 || whiteMoveColumn > 9 || whiteMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid position");
						}
						Tile whiteTile = quoridor.getBoard().getTile((whiteMoveRow - 1) * 9 + whiteMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,whitePlayer,whiteTile,quoridor.getCurrentGame());
						boolean returnvalue =loadMove(whiteTile.getColumn(),whiteTile.getRow(),whitePlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid position");
						}
						//System.out.println("111111111111111111");
					}else if(whiteMoveTile.toCharArray().length==3) {
						try {
							whiteWallMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteWallMoveRow = Integer.parseInt(whiteMoveTile.substring(1,2));
							whiteWallDirection = whiteMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteWallMoveRow > 8 || whiteWallMoveRow < 1 || whiteWallMoveColumn > 8 || whiteWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteWallTile = quoridor.getBoard().getTile((whiteWallMoveRow - 1) * 9 + whiteWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,whitePlayer,whiteWallTile,quoridor.getCurrentGame(),converToDir(whiteWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						//System.out.println("1444444444444444441");
	
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
					
					if(blackMoveTile.toCharArray().length==2) {
						try {
							blackMoveColumn = convertToInt(blackMoveTile.substring(0,1));
							blackMoveRow = Integer.parseInt(blackMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (blackMoveRow > 9 || blackMoveRow < 1 || blackMoveColumn > 9 || blackMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile blackTile = quoridor.getBoard().getTile((blackMoveRow - 1) * 9 + blackMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,blackPlayer,blackTile,quoridor.getCurrentGame());
						boolean returnvalue = loadMove(blackTile.getColumn(),blackTile.getRow(),blackPlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid position");
						}
						//System.out.println("222222222222222222222");
	
					}else if(blackMoveTile.toCharArray().length==3) {
						try {
							blackWallMoveColumn = convertToInt(blackMoveTile.substring(0,1));
							blackWallMoveRow = Integer.parseInt(blackMoveTile.substring(1,2));
							blackWallDirection = blackMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (blackWallMoveRow > 8 || blackWallMoveRow < 1 || blackWallMoveColumn > 8 || blackWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile blackWallTile = quoridor.getBoard().getTile((blackWallMoveRow - 1) * 9 + blackWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,blackPlayer,blackWallTile,quoridor.getCurrentGame(),converToDir(blackWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						//System.out.println("33333333333333");
	
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
				}else if(one) {
					String whiteMoveTile = moveToken.nextToken();
					if(whiteMoveTile.toCharArray().length==2) {
						try {
							whiteMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteMoveRow = Integer.parseInt(whiteMoveTile.substring(1));
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteMoveRow > 9 || whiteMoveRow < 1 || whiteMoveColumn > 9 || whiteMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid position");
						}
						Tile whiteTile = quoridor.getBoard().getTile((whiteMoveRow - 1) * 9 + whiteMoveColumn - 1);
						//Move move = new StepMove(rNumber*2,rNumber,whitePlayer,whiteTile,quoridor.getCurrentGame());
						boolean returnvalue = loadMove(whiteTile.getColumn(),whiteTile.getRow(),whitePlayer);
						if(!returnvalue) {
							throw new UnsupportedOperationException("Invalid position");
						}
					}else if(whiteMoveTile.toCharArray().length==3) {
						try {
							whiteWallMoveColumn = convertToInt(whiteMoveTile.substring(0,1));
							whiteWallMoveRow = Integer.parseInt(whiteMoveTile.substring(1,2));
							whiteWallDirection = whiteMoveTile.substring(2);
						} catch(IndexOutOfBoundsException e) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						if (whiteWallMoveRow > 8 || whiteWallMoveRow < 1 || whiteWallMoveColumn > 8 || whiteWallMoveColumn < 1) {
							throw new UnsupportedOperationException("Invalid Game");
						}
						Tile whiteWallTile = quoridor.getBoard().getTile((whiteWallMoveRow - 1) * 9 + whiteWallMoveColumn - 1);
						Wall wallToBePlaced = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0);
						WallMove move = new WallMove(rNumber*2,rNumber,whitePlayer,whiteWallTile,quoridor.getCurrentGame(),converToDir(whiteWallDirection),wallToBePlaced);
						if(dropWall(move)==null) {
							throw new UnsupportedOperationException("Invalid Game");
						}else {
							quoridor.getCurrentGame().addMove(move);
							quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wallToBePlaced);
							quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wallToBePlaced);
							//quoridor.getCurrentGame().getPositions().add(quoridor.getCurrentGame().getCurrentPosition());
						}
						
					}else {
						throw new UnsupportedOperationException("Invalid Game");
					}
				}else {
					throw new UnsupportedOperationException("Invalid Game");
				}
			}
			quoridor.getCurrentGame().setGameStatus(Game.GameStatus.Replay);
		}
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
	 * overlapping walls or out-of-track pawn or wall positions.
	 * <p>
	 * 
	 * @author William Wang
	 * @return the validation result, true for pass, false for error
	 */
	public static boolean validatePosition() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		PlayerPosition whitePosition = gamePosition.getWhitePosition();
		PlayerPosition blackPosition = gamePosition.getBlackPosition();

		//// validate player position
		Tile whiteTile = whitePosition.getTile();
		Tile blackTile = blackPosition.getTile();

		// check out of bound
		if ((whiteTile.getRow() > 9) || (whiteTile.getColumn() < 1)) {
			return false;
		}
		if ((blackTile.getRow() > 9) || (whiteTile.getColumn() < 1)) {
			return false;
		}

		// check overlapping
		if ((whiteTile.getRow() == blackTile.getRow()) && (whiteTile.getColumn() == blackTile.getColumn())) {
			return false;
		}

		//// validate wall position
		List<Wall> whiteWallsOnBoard = gamePosition.getWhiteWallsOnBoard();
		List<Wall> blackWallsOnBoard = gamePosition.getBlackWallsOnBoard();
		// validate white wall on board
		for (int i = 0; i < whiteWallsOnBoard.size(); i++) {
			// check overlapping with white walls
			for (int j = i + 1; j < whiteWallsOnBoard.size(); j++) {
				if (!noOverlappingWalls(whiteWallsOnBoard.get(i).getMove(), whiteWallsOnBoard.get(j).getMove()))
					return false;
			}
			// check overlapping with black walls
			for (int j = 0; j < blackWallsOnBoard.size(); j++) {
				if (!noOverlappingWalls(whiteWallsOnBoard.get(i).getMove(), blackWallsOnBoard.get(j).getMove()))
					return false;
			}

			if ((whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow() < 1)
					|| (whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow() > 8))
				return false;
			if ((whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn() < 1)
					|| (whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn() > 8))
				return false;

		}
		// validate black wall on board
		for (int i = 0; i < blackWallsOnBoard.size(); i++) {
			// dont need check overlapping with white walls--checked while validating white
			// walls

			// check overlapping with black walls
			for (int j = i + 1; j < blackWallsOnBoard.size(); j++) {
				if (!noOverlappingWalls(blackWallsOnBoard.get(i).getMove(), blackWallsOnBoard.get(j).getMove()))
					return false;
			}
			if ((blackWallsOnBoard.get(i).getMove().getTargetTile().getRow() < 1)
					|| (blackWallsOnBoard.get(i).getMove().getTargetTile().getRow() > 8))
				return false;
			if ((blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn() < 1)
					|| (blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn() > 8))
				return false;
		}

		return true;
	}

	/**
	 * @author arneetkalra && William Wang isWallAlreadyPresent checks to see if a
	 *         wall is already placed where a new wall is wanting to be placed.
	 *         Returns true if wall is already present.
	 *
	 * @param wallOnBoard
	 * @param wallCandidate
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
			return (isSameColumn && Math.abs(tileOnBoard.getRow() - tileCandidate.getRow()) <= 1); // Checks if rows are
																									// off by more
																									// than 1
		}
		// Check if directions are both horizontal
		else if (wallOnBoard.getWallDirection() == Direction.Horizontal
				&& wallCandidate.getWallDirection() == Direction.Horizontal) {
			// Then verify if column and row are identical and return boolean
			return (isSameRow && Math.abs(tileOnBoard.getColumn() - tileCandidate.getColumn()) <= 1);// Checks if
																										// columns are
																										// off
																										// by more than
																										// 1
		}
		// Case when Directions are opposite
		else {
			// Only overlaps if NorthWest tile is the same for both walls
			return (isSameColumn && isSameRow);
		}
	}

	/**
	 * Another isWallAlreadyPresent method which takes different parameters, and is
	 * used to validate the position for a hovered wall.
	 *
	 * @author arneetkalra
	 * @param hoveredTile
	 * @param candidateDirection
	 * @param wallCandidate
	 * @return
	 */
	private static Boolean isWallAlreadyPresent(Tile hoveredTile, Direction candidateDirection,
			WallMove wallCandidate) {
		// Get tiles for onBoard and Candidate
		Tile tileCandidate = wallCandidate.getTargetTile();

		// Verify overlap status:
		Boolean isSameColumn = (hoveredTile.getColumn() == tileCandidate.getColumn());
		Boolean isSameRow = (hoveredTile.getRow() == tileCandidate.getRow());

		// Check if directions are both vertical
		if (candidateDirection == Direction.Vertical && wallCandidate.getWallDirection() == Direction.Vertical) {
			// Then verify if column and row are identical and return boolean
			return (isSameColumn && Math.abs(hoveredTile.getRow() - tileCandidate.getRow()) <= 1); // Checks if rows are
																									// off by more
																									// than 1
		}
		// Check if directions are both horizontal
		else if (candidateDirection == Direction.Horizontal
				&& wallCandidate.getWallDirection() == Direction.Horizontal) {
			// Then verify if column and row are identical and return boolean
			return (isSameRow && Math.abs(hoveredTile.getColumn() - tileCandidate.getColumn()) <= 1);// Checks if
																										// columns are
																										// off
																										// by more than
																										// 1
		}
		// Case when Directions are opposite
		else {
			// Only overlaps if wall center is the same for both walls
			return (isSameColumn && isSameRow);
		}
	}

	/*
	 * /** <p>Helper for validate move<p> <p>validate if two walls are
	 * overlapping<p>
	 *
	 * @author William Wang
	 * 
	 * @return the validation result, true for not overlapping, false for
	 * overlapping
	 */
	public static boolean noOverlappingWalls(WallMove imove, WallMove jmove) {
		System.out.print(imove.getTargetTile().getRow() + "," + imove.getTargetTile().getColumn());
		System.out.print(jmove.getTargetTile().getRow() + "," + jmove.getTargetTile().getColumn());
		if (imove.getWallDirection() == Direction.Horizontal) {
			//
			if (jmove.getWallDirection() == Direction.Horizontal) {
				if ((imove.getTargetTile().getRow() == jmove.getTargetTile().getRow())
						&& (Math.abs(imove.getTargetTile().getColumn() - jmove.getTargetTile().getColumn()) <= 1)) {
					return false;
				}
			} else {
				if ((imove.getTargetTile().getRow() == jmove.getTargetTile().getRow())
						&& (imove.getTargetTile().getColumn() == jmove.getTargetTile().getColumn())) {
					return false;
				}
			}
		} else {
			if (jmove.getWallDirection() == Direction.Horizontal) {
				if ((imove.getTargetTile().getRow() == jmove.getTargetTile().getRow())
						&& (imove.getTargetTile().getColumn() == jmove.getTargetTile().getColumn())) {
					return false;
				}
			} else {
				if ((Math.abs(imove.getTargetTile().getRow() - jmove.getTargetTile().getRow()) <= 1)
						&& (imove.getTargetTile().getColumn() == jmove.getTargetTile().getColumn())) {
					return false;
				}
			}
		}
		return true;
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
	 */
	public static void switchCurrentPlayer() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition currentPosition = quoridor.getCurrentGame().getCurrentPosition();
		GamePosition newPosition;
		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(),
				currentPosition.getWhitePosition().getTile());
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(),
				currentPosition.getBlackPosition().getTile());
		if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack() == null) {
			newPosition = new GamePosition(currentPosition.getId() + 1, player1Position, player2Position,
					game.getBlackPlayer(), game);

		} else {
			newPosition = new GamePosition(currentPosition.getId() + 1, player1Position, player2Position,
					game.getWhitePlayer(), game);
		}
		// Update Game position for the walls
		for (Wall w : currentPosition.getWhiteWallsInStock())
			newPosition.addWhiteWallsInStock(w);

		for (Wall w : currentPosition.getBlackWallsInStock())
			newPosition.addBlackWallsInStock(w);

		for (Wall w : currentPosition.getWhiteWallsOnBoard())
			newPosition.addWhiteWallsOnBoard(w);

		for (Wall w : currentPosition.getBlackWallsOnBoard())
			newPosition.addBlackWallsOnBoard(w);

		game.addPosition(currentPosition);
		game.setCurrentPosition(newPosition);
		QuoridorWindow window = QuoridorApplication.quoridorWindow;

		if (game.hasWallMoveCandidate()) {
			game.setWallMoveCandidate(null);
		}
		window.isGrabWall = true;

		window.placePlayer(quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow() - 1,
				quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn() - 1,
				quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow() - 1,
				quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn() - 1);

		if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove() == quoridor.getCurrentGame()
				.getBlackPlayer()) {
			window.setCurrentPlayer(window.blackPawn + " "
					+ quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getUser().getName() + " "
					+ window.blackPawn);
		} else {
			window.setCurrentPlayer(window.whitePawn + " "
					+ quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getUser().getName() + " "
					+ window.whitePawn);
		}

		window.setTimeRemaining(
				(int) (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime().getTime()));

	}

	/**
	 * <p>
	 * Step forward
	 * <p>
	 * @author William Wang
	 */
	public static void stepForward() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition currentPosition = game.getCurrentPosition();
		try {
			GamePosition nextPosition = game.getPosition(currentPosition.getId()+1);
			game.setCurrentPosition(nextPosition);
		}
		catch(IndexOutOfBoundsException e) {
			
		}
	}
	
	/**
	 * <p>
	 * Step backward
	 * <p>
	 * @author William Wang
	 */
	public static void stepBackward() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition currentPosition = game.getCurrentPosition();
		try {
			GamePosition nextPosition = game.getPosition(currentPosition.getId()+1);
			game.setCurrentPosition(nextPosition);
		}
		catch(IndexOutOfBoundsException e) {
			
		}
	}
	/*
	 * Runs a path finding algorithm based on the wall move candidate location
	 * to determine if both players are still able to reach their target destinations
	 * from their current position
     * @author Sam Perreault
	 * @param hoveredTile associated with the move, to be assigned to the wall
	 * @return true if the specified player can still reach their target destination
	 */
	public static boolean checkIfPathExists(Tile hoveredTile)
	{
	    Quoridor q = QuoridorApplication.getQuoridor();
	    if(q.getCurrentGame().getWallMoveCandidate()==null)
	        return false;
	    Player p = q.getCurrentGame().getCurrentPosition().getPlayerToMove();
	    WallMove wallMoveCandidate = q.getCurrentGame().getWallMoveCandidate();
	    wallMoveCandidate.setTargetTile(hoveredTile);
	    if(wallMoveCandidate.getPlayer().equals(q.getCurrentGame().getWhitePlayer()))
	        q.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());
	    else
	        q.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());

	    boolean success = Controller.pathFinder(q.getCurrentGame().getWhitePlayer(), q.getCurrentGame().getCurrentPosition().getWhitePosition().getTile()) &&
                 Controller.pathFinder(q.getCurrentGame().getBlackPlayer(), q.getCurrentGame().getCurrentPosition().getBlackPosition().getTile());

	    if(wallMoveCandidate.getPlayer().equals(q.getCurrentGame().getWhitePlayer()))
            q.getCurrentGame().getCurrentPosition().removeWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());
        else
            q.getCurrentGame().getCurrentPosition().removeBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());
        return success;
	}

	// Helper Methods ----------------------------

	private static int convertToInt(String letter) {
		int number = (int) letter.charAt(0) - 96;
		return number;

	}

	/** @author Luke Barber and Arneet Kalra */
	// Method to convert String input data type into respective Direction type
	public static Direction stringToDirection(String direction) {
		if (direction.contentEquals("horizontal")) {
			return Direction.Horizontal;
		} else if (direction.contentEquals("vertical")) {
			return Direction.Vertical;
		} else
			return null;
	}

	private static String blackPlayerData(GamePosition gamePosition) {
		String data = "";
		data += "B:" + (char) (gamePosition.getBlackPosition().getTile().getColumn() + 96);
		data += String.valueOf((gamePosition.getBlackPosition().getTile().getRow()));
		for (int i = 0; i < gamePosition.getBlackWallsOnBoard().size(); i++) {
			data += ","
					+ (char) (gamePosition.getBlackWallsOnBoard().get(i).getMove().getTargetTile().getColumn() + 96);
			data += (gamePosition.getBlackWallsOnBoard().get(i).getMove().getTargetTile().getRow());
			data += convertWallDir(gamePosition.getBlackWallsOnBoard().get(i).getMove().getWallDirection());
		}
		return data;
	}

	private static String whitePlayerData(GamePosition gamePosition) {
		String data = "";
		data += "W:" + (char) (gamePosition.getWhitePosition().getTile().getColumn() + 96);
		data += String.valueOf((gamePosition.getWhitePosition().getTile().getRow()));
		for (int i = 0; i < gamePosition.getWhiteWallsOnBoard().size(); i++) {
			data += ","
					+ (char) (gamePosition.getWhiteWallsOnBoard().get(i).getMove().getTargetTile().getColumn() + 96);
			data += (gamePosition.getWhiteWallsOnBoard().get(i).getMove().getTargetTile().getRow());
			data += convertWallDir(gamePosition.getWhiteWallsOnBoard().get(i).getMove().getWallDirection());
		}
		return data;
	}

	private static String convertWallDir(Direction direction) {
		switch (direction) {
		case Horizontal:
			return "h";
		case Vertical:
			return "v";
		default:
			return null;
		}
	}
	private static boolean loadMove(int desMoveColumn, int desMoveRow, Player player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		int currentColumn=0;
		int currentRow=0;
		if(player.equals(quoridor.getCurrentGame().getBlackPlayer())) {
			currentColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
			currentRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		}else {
			currentColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
			currentRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		}
		int vertAbsDiff = Math.abs(desMoveRow-currentRow);
		int vertDiff = desMoveRow-currentRow;
		int horAbsDiff = Math.abs(desMoveColumn -currentColumn);
		int horDiff = desMoveColumn-currentColumn;
		if(vertAbsDiff+ horAbsDiff>2|| vertAbsDiff>2||horAbsDiff>2||vertAbsDiff+horAbsDiff==0) {
			return false;
		}
		PawnBehavior.MoveDirection dir = null;
		switch(vertDiff) {
			case 1:
			case 2:
				dir = PawnBehavior.MoveDirection.South;
				break;
			case -1:
			case -2:
				dir = PawnBehavior.MoveDirection.North;
			default:
				break;
		}
		switch(horDiff) {
			case -1:
				if(vertDiff<0) {
					dir = PawnBehavior.MoveDirection.NorthWest;
					break;
				}
				else if(vertDiff>0) {
					dir = PawnBehavior.MoveDirection.SouthWest;
					break;
				}
				else
					dir = PawnBehavior.MoveDirection.West;
					break;
			case -2:
				dir = PawnBehavior.MoveDirection.West;
				break;
			case 1:
				if(vertDiff<0) {
					dir = PawnBehavior.MoveDirection.NorthEast;
					break;
				}
				else if(vertDiff>0) {
					dir = PawnBehavior.MoveDirection.SouthEast;
					break;
				}
				else
					dir = PawnBehavior.MoveDirection.East;
					break;
			case 2:
				dir = PawnBehavior.MoveDirection.East;
				break;
			default:
				break;
		}
		return PawnBehavior.moveOrJump(dir);
	}

	private static Direction converToDir(String direction) {
		switch (direction) {
		case "v":
			return Direction.Vertical;
		case "h":
			return Direction.Horizontal;
		default:
			return null;
		}
	}
	
	private static String moveData(Move move){
		String data = "";
		data += (char) (move.getTargetTile().getColumn() + 96);
		data += String.valueOf((move.getTargetTile().getRow()));
		if(move.getClass()==WallMove.class) {
			WallMove move2 = (WallMove) move;
			data+= convertWallDir(move2.getWallDirection());
		}
		System.out.print(data);
		return data;
	}

	/**
	 * Random helper method
	 *
	 * @author arneetkalra
	 * @param row
	 * @param column
	 * @return
	 */
	public static boolean overlappingWallsExist(Integer row, Integer column) {

		// Get all the walls already on the board
		GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();

		List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
		List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();

		// Check black walls on board
		for (Wall wall : blackWallsOnBoard) { // For each wall placed by black player on the board
			if (row == wall.getMove().getTargetTile().getRow()) { // If row is the same
				if (Math.abs(column - wall.getMove().getTargetTile().getColumn()) <= 1) { // Check column
					return true; // If row and column same then same
				}
			}

			else if (column == wall.getMove().getTargetTile().getRow()) { // If row is the same
				if (Math.abs(row - wall.getMove().getTargetTile().getColumn()) <= 1) { // Check column
					return true; // If row and column same then same
				}
			}
		}

		// Check white walls on board
		for (Wall wall : whiteWallsOnBoard) { // For each wall placed by white player on the board
			if (row == wall.getMove().getTargetTile().getRow()) { // If row is the same
				if (Math.abs(column - wall.getMove().getTargetTile().getColumn()) <= 1) { // Check column
					return true; // If row and column same then same
				}
			}

			else if (column == wall.getMove().getTargetTile().getRow()) { // If row is the same
				if (Math.abs(row - wall.getMove().getTargetTile().getColumn()) <= 1) { // Check column
					return true; // If row and column same then same
				}
			}
		}

		return false; // Otherwise walls dont exist
	}
	// Wack stuff

	/**
	 * @author arneetkalra
	 */
	public static void resignGame() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Player resignedPlayer = currentGame.getCurrentPosition().getPlayerToMove();

		// Set the current game status to the winner
		if (resignedPlayer == currentGame.getBlackPlayer()) {
			currentGame.setGameStatus(GameStatus.WhiteWon);
		} else {
			currentGame.setGameStatus(GameStatus.BlackWon);
		}
	}

	/**
	 * @author arneetkalra
	 */
	public static void forfeitGame() {
		resignGame();
		reportFinalResult();
	}

	/**
	 * @author arneetkalra
	 */
	public static void whenGameIsNoLongerRunning() {
		QuoridorWindow window = QuoridorApplication.quoridorWindow;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();

		resignGame();

		if (currentGame.getGameStatus() != GameStatus.Running) {
			QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setGameAsBlack(null);
			QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setGameAsWhite(null);
		}


		if (currentGame.getGameStatus() == GameStatus.WhiteWon) {
			//window.notifyWhiteWon();
		} else if (currentGame.getGameStatus() == GameStatus.BlackWon) {
			//window.notifyBlackWon();
		} else if (currentGame.getGameStatus() == GameStatus.Draw) {
			//window.notifyDraw();
		}

		window.resultBeingDisplayed = true;
	}

	/**
	 * @author arneetkalra
	 */
	public static void reportFinalResult() {
		QuoridorWindow window = QuoridorApplication.quoridorWindow;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();

		resignGame();

		if (currentGame.getGameStatus() != GameStatus.Running) {
			QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setGameAsBlack(null);
			QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setGameAsWhite(null);
		}

		if (currentGame.getGameStatus() == GameStatus.WhiteWon) {
			window.notifyWhiteWon();
		} else if (currentGame.getGameStatus() == GameStatus.BlackWon) {
			window.notifyBlackWon();
		} else if (currentGame.getGameStatus() == GameStatus.Draw) {
			window.notifyDraw();
		}

		window.resultBeingDisplayed = true;
	}

	/**
	 * @author arneetkalra
	 * @return
	 */
	public static boolean isBlackPlayerTurn() {

		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove();
		Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();

		//Opposite because in window it runs when you click it, so its calling the previous player. Counter intuitive
		if (currentPlayer.equals(blackPlayer)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @author arneetkalra
	 * @return
	 */
	public static boolean isWhitePlayerTurn() {
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove();
		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();

		//Opposite because in window it runs when you click it, so its calling the previous player. Counter intuitive
		if (currentPlayer.equals(whitePlayer)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @author arneetkalra
	 * @return
	 */
	public static void destroyCurrentGame() {
		QuoridorApplication.getQuoridor().delete();
	}

	/**
	 * @author arneetkalra
	 * @return
	 */
	public static String displayRemainingTimeWhite() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();

        Time whitePlayerTime = currentGame.getWhitePlayer().getRemainingTime();

        @SuppressWarnings("deprecation")
        int seconds = whitePlayerTime.getSeconds();
        @SuppressWarnings("deprecation")
        int minutes = whitePlayerTime.getMinutes();

        String whiteDisplayedTime = minutes + ":" + seconds + "  ";
        return whiteDisplayedTime;
    }

    /**
     * @author arneetkalra
     * @return
     */
    public static String displayRemainingTimeBlack() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();

        Time blackPlayerTime = currentGame.getBlackPlayer().getRemainingTime();

        @SuppressWarnings("deprecation")
        int seconds = blackPlayerTime.getSeconds();
        @SuppressWarnings("deprecation")
        int minutes = blackPlayerTime.getMinutes();

        String whiteDisplayedTime = minutes + ":" + seconds + "  ";
        return whiteDisplayedTime;
    }


    /**
     * @author arneetkalra
     * @return
     */
    public static boolean gameIsStillRunning() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();
        GameStatus currentGameStatus = currentGame.getGameStatus();
        boolean isGameRunning;

        if ((currentGameStatus == GameStatus.BlackWon) || (currentGameStatus == GameStatus.WhiteWon)
                || (currentGameStatus == GameStatus.Draw) || (currentGameStatus == GameStatus.Replay)) {
            isGameRunning = false;
        }

        else {
            isGameRunning = true;
        }

        return isGameRunning;
    }

    /**
     * @author arneetkalra
     * @return
     */
    public static boolean isInReplayMode() {
        Quoridor q = QuoridorApplication.getQuoridor();
        if (q.getCurrentGame().getGameStatus() == GameStatus.Replay) {
            return true;
        }
        return false;
    }


    /**
     * Initiate the Replay Mode for the current game.
     *
     * @param currentGame
     * @return Game object with GameStatus set as Replay Mode.
     * @author Ali Tapan
     */
    public static Game initiateReplayMode(Game currentGame) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        if(currentGame == null) {
            //initialize new game with game status as replay mode
            Game newGame = new Game(GameStatus.Replay, MoveMode.PlayerMove, quoridor);
            return newGame;
        }
        else {
            currentGame.setGameStatus(GameStatus.Replay);
            return currentGame;
        }
    }

    /**
     * Switch back to the running game when the current user is in Replay mode.
     * If the current game is over, the user stays in Replay mode.
     *
     * @param currentGame
     * @return Game object with GameStatus set appropriately.
     * @author Ali Tapan
     */
    public static Game initiateContinueGame(Game currentGame) {
        if(currentGame.getGameStatus() == GameStatus.BlackWon
                || currentGame.getGameStatus() == GameStatus.WhiteWon
                ||currentGame.getGameStatus() == GameStatus.Draw)
        {
            currentGame.setGameStatus(GameStatus.Replay);
        }
        else {
            currentGame.setGameStatus(GameStatus.Running);
        }
        return currentGame;
    }

    /**
     * Sets the current position to the defined start position in the features.
     *
     * @param currentGame
     * @return Game object
     *
     * @author Ali Tapan
     */
    public static Game jumpToStartPosition(Game currentGame) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        List<GamePosition> position = currentGame.getPositions();
        Tile whiteTile = new Tile(9,5,quoridor.getBoard());
        Tile blackTile = new Tile(1,5,quoridor.getBoard());
        PlayerPosition whitePosition = new PlayerPosition(currentGame.getWhitePlayer(), whiteTile);
        PlayerPosition blackPosition = new PlayerPosition(currentGame.getBlackPlayer(), blackTile);
        GamePosition startPosition = new GamePosition(position.size(),whitePosition,blackPosition,currentGame.getCurrentPosition().getPlayerToMove(), currentGame);
        currentGame.setCurrentPosition(startPosition);
        currentGame.addPosition(startPosition);
        return currentGame;
    }


    /**
     * Sets current position the the defined final position in the features
     * @param currentGame
     * @return Game object
     *
     * @author Ali Tapan
     */
    public static Game jumpToFinalPosition(Game currentGame) {
    	currentGame.setCurrentPosition(currentGame.getPositions().get(currentGame.getPositions().size()-1));
    	return currentGame;
    }


    /**
     * Helper method for finding a path from the current location to the target destination
     * Performs recursive calls until the destination is reached, or until all possible options
     * from the starting square have been exhausted
     * @author Sam Perreault
     * @return a boolean value specifying whether you can or cannot reach the destination from
     *  the current position
     */
    public static boolean pathFinder(Player p, Tile currentTile)
    {
        // Determine if vertical or horizontal
        // Feed that information directly into the other methods
        List<Wall> walls = Controller.getAllWallsOnBoard();
        boolean[][] visited = new boolean[9][9];
        for(boolean[] b: visited)
        {
            Arrays.fill(b, false);
        }

        if(p.getDestination().getDirection().equals(Direction.Vertical))
            return pathFinderVert(p, currentTile.getRow(), currentTile.getColumn(), walls, visited);
        else
            return pathFinderHor(p, currentTile.getRow(), currentTile.getColumn(), walls, visited);
    }

    /**
     * Private helper method to calculate whether there exists a wall adjacent to the current tile
     * @author Sam Perreault
     * @param currentRow row of the queried tile
     * @param currentColumn column of the queried tile
     * @param walls a list of walls already placed on the board, and the candidate wall
     * @return a boolean array, ordering the values for the tile ABOVE, BELOW, EAST, and WEST of the
     * tile queried
     */
    private static boolean[] checkAdjacentWalls(int currentRow, int currentColumn, List<Wall> walls)
    {
        // 0,1,2,3 above below,east,west
        boolean[] sides= new boolean[4];
        Arrays.fill(sides,false);
        for(Wall w: walls)
        {
            int wCol= w.getMove().getTargetTile().getColumn();
            int wRow= w.getMove().getTargetTile().getRow();
            Direction dir = w.getMove().getWallDirection();
            if(dir.equals(Direction.Horizontal) && (wCol==currentColumn-1 || wCol==currentColumn))
            {
                if(wRow==currentRow-1)
                {
                    sides[0] = true;
                    continue;
                }
                if(wRow==currentRow) {
                    sides[1] = true;
                    continue;
                }
            }
            else if(dir.equals(Direction.Vertical)&& (wRow==currentRow-1|| wRow==currentRow)) {
                if(wCol==currentColumn-1)
                {
                    sides[3]=true;
                    continue;
                }
                if(wCol==currentColumn)
                {
                    sides[2]=true;
                    continue;
                }
            }
        }
        return sides;
    }

    /**
     * Helper method that searches for a path to the other side of the board
     * Optimizes movement for both vertical destination possibilities
     * @author Sam Perreault
     * @param p the current player to path to the end
     * @param currentRow the current row of the iteration
     * @param currentColumn the current column of the iteration
     * @param walls a list of already placed walls, as well as the candidate wall
     * @param visited a 2D matrix to reduce operations
     * @return a value of true if the current player can reach the destination
     */
    private static boolean pathFinderVert(Player p, int currentRow, int currentColumn, List<Wall> walls, boolean[][] visited)
    {
        if(p.getDestination().getTargetNumber()==currentRow)
            return true;
        if(currentColumn==0|| currentColumn==10|| currentRow==0|| currentRow==10||visited[currentRow-1][currentColumn-1])
            return false;
        visited[currentRow-1][currentColumn-1]=true;
        // Check the direction, then start checking for walls
        boolean success=false;
        boolean[] sides = Controller.checkAdjacentWalls(currentRow, currentColumn, walls);
        boolean above=sides[0], below=sides[1], east=sides[2], west=sides[3];
        //Towards the top of the board
        // Check if walls above first, then sides after try

        if(p.getDestination().getTargetNumber()==1)
        {
            // Tries each direction, and ends method calls if there is a successful path
            if(!above)
                success = pathFinderVert(p, currentRow-1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!east)
                success = pathFinderVert(p, currentRow, currentColumn+1, walls,visited);
            if(success)
                return true;
            if(!west)
                success = pathFinderVert(p, currentRow, currentColumn-1, walls,visited);
            if(success)
                return true;
            if(!below)
                success = pathFinderVert(p, currentRow+1, currentColumn, walls,visited);
            return success;
        }
        else if(p.getDestination().getTargetNumber()==9)
        {
            // Tries each direction, and ends method calls if there is a successful path
            if(!below)
                success = pathFinderVert(p, currentRow+1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!east)
                success = pathFinderVert(p, currentRow, currentColumn+1, walls,visited);
            if(success)
                return true;
            if(!west)
                success = pathFinderVert(p, currentRow, currentColumn-1, walls,visited);
            if(success)
                return true;
            if(!above)
                success = pathFinderVert(p, currentRow-1, currentColumn, walls,visited);
            return success;
        }
        // This line reach iff pathfinding fails
        return false;
    }

    /**
     * Helper method that searches for a path to the other side of the board
     * Optimizes movement for both horizontal destination possibilities
     * @author Sam Perreault
     * @param p the current player to path to the end
     * @param currentRow the current row of the iteration
     * @param currentColumn the current column of the iteration
     * @param walls a list of already placed walls, as well as the candidate wall
     * @param visited a 2D matrix to reduce operations
     * @return a value of true if the current player can reach the destination
     */
    private static boolean pathFinderHor(Player p, int currentRow, int currentColumn, List<Wall> walls, boolean[][] visited)
    {
        if(p.getDestination().getTargetNumber()==currentColumn)
            return true;
        if(currentColumn==0|| currentColumn==10|| currentRow==0|| currentRow==10||visited[currentRow-1][currentColumn-1])
            return false;
        visited[currentRow-1][currentColumn-1]=true;
        // Check the direction, then start checking for walls
        boolean success=false;
        boolean[] sides = Controller.checkAdjacentWalls(currentRow, currentColumn, walls);
        boolean above=sides[0], below=sides[1], east=sides[2], west=sides[3];
        //Towards the top of the board

        if(p.getDestination().getTargetNumber()==1)
        {
            // Tries each direction, and ends method calls if there is a successful path
            if(!west)
                success = pathFinderHor(p, currentRow, currentColumn-1, walls,visited);
            if(success)
                return true;
            if(!below)
                success = pathFinderHor(p, currentRow+1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!above)
                success = pathFinderHor(p, currentRow-1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!east)
                success = pathFinderHor(p, currentRow, currentColumn+1, walls,visited);
            return success;
        }
        else if(p.getDestination().getTargetNumber()==9)
        {
            // Tries each direction, and ends method calls if there is a successful path
            if(!east)
                success = pathFinderHor(p, currentRow, currentColumn+1, walls,visited);
            if(success)
                return true;
            if(!below)
                success = pathFinderHor(p, currentRow+1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!above)
                success = pathFinderHor(p, currentRow-1, currentColumn, walls,visited);
            if(success)
                return true;
            if(!west)
                success = pathFinderHor(p, currentRow, currentColumn-1, walls,visited);
            return success;
        }
        // This line reach iff pathfinding fails
        return false;
    }
    
    /**
     * @author arneetkalra
     * @return
     */
    public static Game getCurrentGame() {
    	Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
    	return currentGame;
    }
	/**
	 * @author Luke Barber
	 * Checks row, column, and time of current player and changes the game status to reflect the current game's win status
	 */
	public static void checkGameResult() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Time zero = new Time(0);
		if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().equals(quoridor.getCurrentGame().getWhitePlayer())) {
			if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime().equals(zero)) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.BlackWon);
			}
			else if (quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow() == 9) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
			}
			else if(quoridor.getCurrentGame().getMoves().size() >=8) {
				int size = quoridor.getCurrentGame().getMoves().size();
				ArrayList<Move> threeMovesWhite = new ArrayList<Move>();

				for (int i = 0; i < 9; i+=4) {
					threeMovesWhite.add(quoridor.getCurrentGame().getMove(size-1-i));
				}
				if ((threeMovesWhite.get(0).getTargetTile().getRow() == threeMovesWhite.get(1).getTargetTile().getRow() 
						&& (threeMovesWhite.get(0).getTargetTile().getRow() == threeMovesWhite.get(2).getTargetTile().getRow()))
						&& ((threeMovesWhite.get(0).getTargetTile().getColumn() == threeMovesWhite.get(1).getTargetTile().getColumn()) 
						&& (threeMovesWhite.get(0).getTargetTile().getColumn() == threeMovesWhite.get(2).getTargetTile().getColumn()))) {
					quoridor.getCurrentGame().setGameStatus(GameStatus.Draw);
				}
			}
		}
		else if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().equals(quoridor.getCurrentGame().getBlackPlayer())) {
			if (quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getRemainingTime().equals(zero)) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
			}	
			else if(quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow() == 1) {
				quoridor.getCurrentGame().setGameStatus(GameStatus.BlackWon);
			}
			else if(quoridor.getCurrentGame().getMoves().size() >=6) {
				int size = quoridor.getCurrentGame().getMoves().size()-1;
				ArrayList<Move> threeMovesBlack = new ArrayList<Move>();
				for (int i = 0; i < 9; i+=4) {
					threeMovesBlack.add(quoridor.getCurrentGame().getMove(size-i));
				}
				if ((threeMovesBlack.get(0).getTargetTile().getRow() == threeMovesBlack.get(1).getTargetTile().getRow() 
						&& (threeMovesBlack.get(0).getTargetTile().getRow() == threeMovesBlack.get(2).getTargetTile().getRow()))
						&& ((threeMovesBlack.get(0).getTargetTile().getColumn() == threeMovesBlack.get(1).getTargetTile().getColumn()) 
						&& (threeMovesBlack.get(0).getTargetTile().getColumn() == threeMovesBlack.get(2).getTargetTile().getColumn()))) {
					quoridor.getCurrentGame().setGameStatus(GameStatus.Draw);
				}
			}
			
		}
	}
	
	/**
	 * @author Luke Barber
	 * Changes string (either white or black) into being current player
	 */
	public static void stringToCurrentPlayer(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
		}
		else if (player.equals("black")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
		}
		Time time = new Time(60);
		quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().setRemainingTime(time);
	}
}
