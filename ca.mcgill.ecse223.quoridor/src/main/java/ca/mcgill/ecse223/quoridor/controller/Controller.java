package ca.mcgill.ecse223.quoridor.controller;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;



public class Controller {
	
  /**
   * <p> Start New Game <p>
   * <p> Initialize a new Game object.
   * @return the created initialized Game object
   * 
   * @author Ali Tapan
   * @version 2.0
   */
  public static Game startNewGame() {
	 Quoridor quoridor = QuoridorApplication.getQuoridor();
	 Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);
	 
	 return game;
  }
  	
  /**
   * <p> Initialize White Player <p>
   * <p> Initializes a white player and assigns it <p>
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
   * <p> Initialize Black Player <p>
   * <p> Initialize a black player and assigns it <p>
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
   * <p> Set Total Thinking Time <p>
   * <p> Set the total thinking time for player.
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
				  remaining = new Time(remaining.getTime()-5*1000*3600);
			  }catch (IllegalArgumentException e) {
				 error=e.getMessage(); 
			  }
			  
	  quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(remaining);
	  quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(remaining);
	  quoridor.getCurrentGame().setGameStatus(GameStatus.ReadyToStart);
  }
  
  
  /**
   * <p> Start the Clock <p>
   * <p> Initiates the game timer which starts when the game is running.
   * 
   * @author Ali Tapan
   * @version 2.0
   */
  public static void startClock() {
	  
	  Quoridor quoridor = QuoridorApplication.getQuoridor();
	  quoridor.getCurrentGame().setGameStatus(GameStatus.Running);
}
  
  /**
   * <p> Select an Existing Username <p>
   * <p> The user selects an existing user name that was previously used in a game
   * @param username is a String that is the existing user name
   * @returns a Boolean, true if it successfully sets the username, false otherwise
   * @author Ali Tapan
   * @version 2.0
   */
  public static Boolean selectExistingUsername(String username, Player player) {
	  
	  if(username.equals(null))
	  {
		 return false;
	  }
	  if(User.hasWithName(username) == false)
	  {
		  return false;
	  }
	  
	  Quoridor quoridor = QuoridorApplication.getQuoridor();
	  //Iterate through the users to see if they match the user name provided
	  for(int i = 0; i < quoridor.numberOfUsers(); i++)
	  {
		  User user = quoridor.getUser(i);
		  if(user.getName().equals(username))
		  {
			 player.setUser(user);
			 return true;
		  }
	  }
	  return false;
}
  
  /**
   * <p> List of All Existing Usernames from the previous games <p>
   * 
   * @return Array of Usernames that were previosuly used
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public static String[] listExistingUsernames()
  {
	  Quoridor quoridor = QuoridorApplication.getQuoridor();
	  List<String> list = new ArrayList<String>();
	  
	  for(User u: quoridor.getUsers())
	  {
		  list.add(u.getName());
	  }
	  list.add(0, "or select existing username...");
	  return list.toArray(new String[list.size()]);
  }
  
  /**
   * <p> Provide a New Username <p>
   * <p> The user selects/enters a new user name that was not previously used in a game
   * @param username is a String that is the new user name
   * @return returns a Boolean, true if it successfully sets the username, false otherwise
   * @author Ali Tapan
   * @verison 2.0
   */
  public static Boolean provideNewUsername(String username, Player player) {
	  if(User.hasWithName(username) == true)
	  {
		  return false;
	  }
	  Quoridor quoridor = QuoridorApplication.getQuoridor();
	  player.getUser().setName(username);
	  //User user = quoridor.addUser(username);
	  //player.setUser(user);
	  return true;
}
  	
  	/**
  	 * @author Sam Perreault Creates the board object and its tiles
  	 */
  	public static void createBoard()
  	{
  		Quoridor q = QuoridorApplication.getQuoridor();
  		Board board = new Board(q);
  		Tile t;
  		for(int i=1;i<10;i++)
  			for(int j=1; j<10; j++)
  				t= new Tile(i,j,board);
  	}
	/**
	 * @author Sam Perreault Sets the starting position and
	 *         walls of each player. In addition, sets white/player 1 as the player
	 *         to move, and starts counting down the white player's thinking time.
	 */
	public static void initializeBoard() {
	Quoridor q = QuoridorApplication.getQuoridor();
  	Board board = q.getBoard();
	PlayerPosition whitePlayerPosition = new PlayerPosition(q.getCurrentGame().getWhitePlayer(), board.getTile(76));
	PlayerPosition blackPlayerPosition = new PlayerPosition(q.getCurrentGame().getBlackPlayer(), board.getTile(4));
	GamePosition gp = new GamePosition(0,whitePlayerPosition,blackPlayerPosition
			, q.getCurrentGame().getWhitePlayer(), q.getCurrentGame());
	q.getCurrentGame().setCurrentPosition(gp);
	for(int i=0;i<10;i++)
	{
		Wall a,b;
		a = new Wall(i, q.getCurrentGame().getWhitePlayer());
		q.getCurrentGame().getCurrentPosition().addWhiteWallsInStock(a);
		b = new Wall(i+10, q.getCurrentGame().getBlackPlayer());
		q.getCurrentGame().getCurrentPosition().addBlackWallsInStock(b);
	}
	QuoridorWindow window =  QuoridorApplication.quoridorWindow;
	window.createSecondTimer();
	window.setCurrentPlayer(q.getCurrentGame().getWhitePlayer().getUser().getName());
}

	/**
	 * @author Sam Perreault
	 * Subtracts a second from the remaining time of the current player. If the remaining time becomes zero,
	 * then the other player wins
	 */
	public static void subtractSecond()
	{
		Quoridor q = QuoridorApplication.getQuoridor();
		Player curPlayer = q.getCurrentGame().getCurrentPosition().getPlayerToMove();
		long remaining = curPlayer.getRemainingTime().getTime();
		remaining -= 1000L;
		if(remaining == 0)
		{
			if(curPlayer.equals(q.getCurrentGame().getWhitePlayer()))
				q.getCurrentGame().setGameStatus(GameStatus.BlackWon);
			else
				q.getCurrentGame().setGameStatus(GameStatus.WhiteWon);
			return;
		}
		curPlayer.setRemainingTime(new Time(remaining));
	}
	// Global variables to make life easier


	  /**
	   * @author Luke Barber
	   * Returns a boolean on whether a wall is selected in the stock
	   */
	  public static boolean wallSelected() {
		  boolean wallSelected = false;
		  if (QuoridorWindow.stockButtonSelected()) {
			wallSelected = true;
		  }
		  return wallSelected;
	  }
	  /**
	   * @author Luke Barber
	   * Checks the stock of the playerToMove and calls the notify method in the view if the player's stock is 0
	   */
	  public static void notifyNoMoreWalls() {
		  List<Wall> stock = null;
		  Quoridor quoridor =  QuoridorApplication.getQuoridor();
		  Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		  if(currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
				stock = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock();
		  }
		  else if(currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
				stock = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock();
		  }
		  
		  if (stock.size()==0) {
			  QuoridorWindow.warningNoMoreWalls();
		  }
	  }
	  /**
	   * @author Luke Barber
	   * Grabs a given wall and holds it so that it is ready for use. 
	   * @param wall The wall that will be grabbed
	   */
	  	public static boolean grabWall(Wall wall) {
	  		Quoridor quoridor =  QuoridorApplication.getQuoridor();
	  		boolean grabbed = false;
	  		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
	  		Player player;
	  		if(currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
	  			player = quoridor.getCurrentGame().getWhitePlayer();
			  	quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
	  		}
	  		else if(currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
	  			player = quoridor.getCurrentGame().getBlackPlayer();
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
	  		}
	  		else {
	  			return grabbed;
	  		}
	  		int initialRow = 1;
	  		int initialColumn = 1;
	  		Direction direction = Direction.Horizontal;
	  		Tile tile = quoridor.getBoard().getTile((initialRow - 1) * 9 + (initialColumn - 1));
			WallMove wallMoveCandidate = new WallMove(1, 1, player, tile, quoridor.getCurrentGame(), direction, wall);
			quoridor.getCurrentGame().setWallMoveCandidate(wallMoveCandidate);
			wall.setMove(wallMoveCandidate);
	  		grabbed = true;
		 	return grabbed;
	}

		
		/**
	   * @author Luke Barber
	   * Rotates a given wall that is on the board. 
	   * @param wall The wall to be rotated
	   */
		public static boolean rotateWall(Wall wall) {
			boolean rotated = false; //Boolean value signifying whether the wall has been rotated or not
			WallMove currentWallMove = wall.getMove(); //The wallMove associated with the given wall
			if(currentWallMove.getWallDirection().equals(Direction.Horizontal)) {
				currentWallMove.setWallDirection(Direction.Vertical);
			}
			else if (currentWallMove.getWallDirection().equals(Direction.Vertical)) {
				currentWallMove.setWallDirection(Direction.Horizontal);
			}
			else {
				return rotated;
			}
			wall.setMove(currentWallMove);
			QuoridorWindow.rotateWallDirection(wall);
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
	 * Load the game from the game file. 
	 * load the correct player position and wall position
	 * @author Yin
	 * @param quoridor This is the quoridor you want to load the game into
	 * @param fileName This is the name of the file which stores the game
	 * 
	 * */
	public static Quoridor loadPosition(String fileName) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<String> lines = Collections.emptyList(); 
	    try
	    { 
	      lines = Files.readAllLines(Paths.get("src/test/resources/savePosition/"+fileName), StandardCharsets.UTF_8); 
	    } 
	    catch (IOException e) 
	    { 
	      // do something 
	      e.printStackTrace(); 
	    }
	    String firstLine = null;
	    String secondLine = null;
	    try {
		    firstLine = lines.get(0);
		    secondLine = lines.get(1);
	    }catch(IndexOutOfBoundsException e){
			throw new UnsupportedOperationException("Invalid position");
	    }
        StringTokenizer first = new StringTokenizer(firstLine, ",");
        StringTokenizer second = new StringTokenizer(secondLine, ",");
        
        	String nextPlayer = first.nextToken();
	    String opponent = second.nextToken();
		int nextPlayerColumn = 0;
		int nextPlayerRow = 0;
		if(nextPlayer.toCharArray().length!=4||opponent.toCharArray().length!=4) {
			throw new UnsupportedOperationException("Invalid position");
		}
		try{
			nextPlayerColumn =convertToInt(nextPlayer.substring(2,3));
			nextPlayerRow = Integer.parseInt(nextPlayer.substring(3));
		}
		catch(IndexOutOfBoundsException e){
			throw new UnsupportedOperationException("Invalid position");
		}
		if(nextPlayerRow>9||nextPlayerRow<1||nextPlayerColumn>9||nextPlayerColumn<1) {
			throw new UnsupportedOperationException("Invalid position");
		}
		Tile nextPlayerTile = quoridor.getBoard().getTile((nextPlayerRow-1)*9+nextPlayerColumn-1);
		int opponentColumn = 0;
		int opponentRow = 0;
		try{
			opponentColumn =convertToInt(opponent.substring(2,3));
			opponentRow = Integer.parseInt(opponent.substring(3));
		}
		catch(IndexOutOfBoundsException e){
			throw new UnsupportedOperationException("Invalid position");
		}
		if(opponentRow>9||opponentRow<1||opponentColumn>9||opponentColumn<1) {
			throw new UnsupportedOperationException("Invalid position");
		}
		Tile opponentTile = quoridor.getBoard().getTile((opponentRow-1)*9+opponentColumn-1);
	    if(nextPlayer.substring(0,1).equals("B")){
    			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
		    quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(nextPlayerTile);
		    quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(opponentTile);
		    while(first.hasMoreTokens()) {
		    		int i = 0;
		    		Wall wall = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(i);
		    		System.out.println(wall);
				String direction = null;
				int column = 0;
				int row = 0;
				String wallPosition = first.nextToken();
				if(wallPosition.toCharArray().length!=3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try{
					column =convertToInt(wallPosition.substring(0,1));
					row = Integer.parseInt(wallPosition.substring(1,2));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					throw new UnsupportedOperationException("Invalid position");
				}
				if(row>8||row<1||column>8||column<1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				
				Tile tile = quoridor.getBoard().getTile((row-1)*9+column-1);
	    			WallMove move = new WallMove(0,1,quoridor.getCurrentGame().getBlackPlayer(),tile,quoridor.getCurrentGame(),converToDir(direction),wall);
				wall.setMove(move);
			    quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			    quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
			    i++;
		    }
		    while(second.hasMoreTokens()) {
				int i = 0;
				Wall wall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(i+1);
				System.out.println(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(9));
				int column = 0;
				int row = 0;
				String direction = null;
				String wallPosition = second.nextToken();
				if(wallPosition.toCharArray().length!=3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try{
					column =convertToInt(wallPosition.substring(0,1));
					row = Integer.parseInt(wallPosition.substring(1,2));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					throw new UnsupportedOperationException("Invalid position");
				}
				if(row>8||row<1||column>8||column<1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile((row-1)*9+column-1);
				WallMove move = new WallMove(0,1,quoridor.getCurrentGame().getWhitePlayer(),tile,quoridor.getCurrentGame(),converToDir(direction),wall);
				wall.setMove(move);
			    wall.setOwner(quoridor.getCurrentGame().getWhitePlayer());
			    wall.getMove().setWallDirection(converToDir(direction));
			    quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			    quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
			    i++;
		    }

	    }else if(nextPlayer.substring(0,1).equals("W")){
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
		    quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().setTile(nextPlayerTile);
		    quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().setTile(opponentTile);
		    while(first.hasMoreTokens()) {
		    		int i = 0;
		    		Wall wall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(i);
				String direction = null;
				int column = 0;
				int row = 0;
				String wallPosition = first.nextToken();
				if(wallPosition.toCharArray().length!=3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try{
					column =convertToInt(wallPosition.substring(0,1));
					row = Integer.parseInt(wallPosition.substring(1,2));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					throw new UnsupportedOperationException("Invalid position");
				}
				if(row>8||row<1||column>8||column<1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile(row*column);
				WallMove move = new WallMove(0,1,quoridor.getCurrentGame().getWhitePlayer(),tile,quoridor.getCurrentGame(),converToDir(direction),wall);
				wall.setMove(move);
			    first.nextToken();
			    wall.getMove().setWallDirection(converToDir(direction));
			    wall.setOwner(quoridor.getCurrentGame().getBlackPlayer());
			    quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard().add(wall);
			    quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().remove(i);
			    i++;
		    }
			while(second.hasMoreTokens()) {
		    		int i = 0;
		    		Wall wall = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(i);
				int column = 0;
				int row = 0;
				String direction = null;
				String wallPosition = second.nextToken();
				if(wallPosition.toCharArray().length!=3) {
					throw new UnsupportedOperationException("Invalid position");
				}
				try{
					column =convertToInt(wallPosition.substring(0));
					row = Integer.parseInt(wallPosition.substring(1));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					throw new UnsupportedOperationException("Invalid position");
				}
				if(row>8||row<1||column>8||column<1) {
					throw new UnsupportedOperationException("Invalid position");
				}
				Tile tile = quoridor.getBoard().getTile(row*column);
				WallMove move = new WallMove(0,1,quoridor.getCurrentGame().getBlackPlayer(),tile,quoridor.getCurrentGame(),converToDir(direction),wall);
			    second.nextToken();
				wall.setMove(move);
			    quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard().add(wall);
			    quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().remove(i);
			    i++;
		    }
	
			}else{
    				throw new UnsupportedOperationException("Invalid position");
			}
	    boolean sameRemainingWall = (quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size()==quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
	    if(validatePosition()&&sameRemainingWall) {
	    		return quoridor;
	    		}else {
	    			throw new UnsupportedOperationException("Invalid position");
	    }
}

	/**
	 * Save the game into a game file
	 * @author Yin
	 * @param fileName
	 * */
	public static void savePosition(String fileName, GamePosition gamePosition, boolean confirms)throws IOException {
		String data = "";
		if(gamePosition.getPlayerToMove().getUser().getName().equals(gamePosition.getBlackPosition().getPlayer().getUser().getName())) {
			data += blackPlayerData(gamePosition)+"\n";
			data += whitePlayerData(gamePosition);
			System.out.println(data);
		}
		else {
			data += whitePlayerData(gamePosition)+"\n";
			data += blackPlayerData(gamePosition);
			System.out.println(data);
		}
		Path path = Paths.get("src/test/resources/savePosition/"+fileName);
		if(Files.exists(path)) {
			if(confirms) {
				Files.delete(path);
				Files.createFile(path);
	            Files.write(path, data.getBytes());

			}
		}else {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
            Files.write(path, data.getBytes());
		}
	}


	/**
	 * @author Yin Zhang 260726999 The user confirm whether to overwrite the
	 *         existing file
	 */
	public static void confirmsToOverWrite() {
	}

	/**
	 * <p>11 Validate Position<p>
	 * <p>validate if the player positions and wall positions are valid 
	 * e.g. overlapping walls or outof-track pawn or wall positions. <p>
	 * 
	 * @author William Wang
	 * @return the validation result, true for pass, false for error
	 */
	public static boolean validatePosition() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		PlayerPosition whitePosition = gamePosition.getWhitePosition();
		PlayerPosition blackPosition = gamePosition.getBlackPosition();
		
		////validate player position
		Tile whiteTile = whitePosition.getTile();
		Tile blackTile = blackPosition.getTile();
		
		//check out of bound
		if((whiteTile.getRow()>9)||(whiteTile.getColumn()<1)) {
			return false;
		}
		if((blackTile.getRow()>9)||(whiteTile.getColumn()<1)) {
			return false;
		}
		
		//check overlapping
		if((whiteTile.getRow()==blackTile.getRow())&&(whiteTile.getColumn()==blackTile.getColumn())) {
			return false;
		}
		
		////validate wall position
		List<Wall> whiteWallsOnBoard = gamePosition.getWhiteWallsOnBoard();
		List<Wall> blackWallsOnBoard = gamePosition.getBlackWallsOnBoard();
		//validate white wall on board
		for(int i=0;i<whiteWallsOnBoard.size();i++) {
			//check overlapping with white walls
			for(int j=i+1;j<whiteWallsOnBoard.size();j++) {				
				if(!noOverlappingWalls(whiteWallsOnBoard.get(i).getMove(),whiteWallsOnBoard.get(j).getMove())) return false;
			}
			//check overlapping with black walls
			for(int j=0;j<blackWallsOnBoard.size();j++) {
				if(!noOverlappingWalls(whiteWallsOnBoard.get(i).getMove(),blackWallsOnBoard.get(j).getMove())) return false;
			}
		
			if((whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow()<1)||
					(whiteWallsOnBoard.get(i).getMove().getTargetTile().getRow()>8))return false;
			if((whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn()<1)||
					(whiteWallsOnBoard.get(i).getMove().getTargetTile().getColumn()>8))return false;
			
		}
		//validate black wall on board
		for(int i=0;i<blackWallsOnBoard.size();i++) {
			//dont need check overlapping with white walls--checked while validating white walls

			//check overlapping with black walls
			for(int j=i+1;j<blackWallsOnBoard.size();j++) {
				if(!noOverlappingWalls(blackWallsOnBoard.get(i).getMove(),blackWallsOnBoard.get(j).getMove())) return false;
			}
			if((blackWallsOnBoard.get(i).getMove().getTargetTile().getRow()<1)||
					(blackWallsOnBoard.get(i).getMove().getTargetTile().getRow()>8))return false;
			if((blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn()<1)||
					(blackWallsOnBoard.get(i).getMove().getTargetTile().getColumn()>8))return false;
		}
		
		return true;
	}

	/**
	 * @author arneetkalra && William Wang isWallAlreadyPresent checks to see if a wall is already
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

	/*
	 /**
		 * <p>Helper for validate move<p>
		 * <p>validate if two walls are overlapping<p>
		 *
		 * @author William Wang
		 * @return the validation result, true for not overlapping, false for overlapping
		 */
	private static boolean noOverlappingWalls(WallMove imove,WallMove jmove) {
		System.out.print(imove.getTargetTile().getRow()+","+imove.getTargetTile().getColumn());
		System.out.print(jmove.getTargetTile().getRow()+","+jmove.getTargetTile().getColumn());
		if(imove.getWallDirection()==Direction.Horizontal) {
			//
			if(jmove.getWallDirection()==Direction.Horizontal) {
				if(		(imove.getTargetTile().getRow()==jmove.getTargetTile().getRow())&&
						(Math.abs(imove.getTargetTile().getColumn()-jmove.getTargetTile().getColumn())<=1)){
					return false;
				}
			}
			else {
				if(		(imove.getTargetTile().getRow()==jmove.getTargetTile().getRow())&&
						(imove.getTargetTile().getColumn()==jmove.getTargetTile().getColumn())){
					return false;
				}
			}
		}
		else {
			if(jmove.getWallDirection()==Direction.Horizontal) {
				if(		(imove.getTargetTile().getRow()==jmove.getTargetTile().getRow())&&
						(imove.getTargetTile().getColumn()==jmove.getTargetTile().getColumn())){
					return false;
				}
			}
			else {
				if(		(Math.abs(imove.getTargetTile().getRow()-jmove.getTargetTile().getRow())<=1)&&
						(imove.getTargetTile().getColumn()==jmove.getTargetTile().getColumn())){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * <p>12. Switch player (aka. Update board)<p>
	 * <p>Switch current player and update clock <p>
	 * 
	 * @author William Wang
	 * @param game the current quoridor game
	 */
	public static void switchCurrentPlayer() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game game = quoridor.getCurrentGame();
		GamePosition currentPosition = quoridor.getCurrentGame().getCurrentPosition();
		List<GamePosition> positions = quoridor.getCurrentGame().getPositions();
		GamePosition newPosition;
		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), currentPosition.getWhitePosition().getTile());
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), currentPosition.getBlackPosition().getTile());
		if(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack()==null) {
			newPosition = new GamePosition(currentPosition.getId()+1, player1Position, player2Position, game.getBlackPlayer(), game);
			game.getWhitePlayer().setRemainingTime(new Time(0));
			game.getBlackPlayer().setRemainingTime(new Time(180000));
		}
		else {
			newPosition = new GamePosition(currentPosition.getId()+1, player1Position, player2Position, game.getWhitePlayer(), game);	
			game.getWhitePlayer().setRemainingTime(new Time(180000));
			game.getBlackPlayer().setRemainingTime(new Time(0));
		}
		game.addPosition(currentPosition);
		game.setCurrentPosition(newPosition);
		
		
	}

	//Helper Methods ----------------------------

	private static int convertToInt(String letter) {
		int number = (int)letter.charAt(0)-96;
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
		String data ="";
		data += "B:"+(char)(gamePosition.getBlackPosition().getTile().getColumn()+96);
		data += String.valueOf((gamePosition.getBlackPosition().getTile().getRow()));
		System.out.println(data);
		for(int i = 0; i<gamePosition.getBlackWallsOnBoard().size(); i++) {
	        data += ","+(char)(gamePosition.getBlackWallsOnBoard().get(i).getMove().getTargetTile().getColumn()+96);
	        data += ","+(gamePosition.getBlackWallsOnBoard().get(i).getMove().getTargetTile().getRow());
	        data += convertWallDir(gamePosition.getBlackWallsOnBoard().get(i).getMove().getWallDirection());
	        }
		return data;
	}
	
	private static String whitePlayerData(GamePosition gamePosition) {
		String data ="";
		data += "W:"+(char)(gamePosition.getWhitePosition().getTile().getColumn()+96);
		data += String.valueOf((gamePosition.getWhitePosition().getTile().getRow()));
		for(int i = 0; i<gamePosition.getWhiteWallsOnBoard().size(); i++) {
	        data += ","+(char)(gamePosition.getWhiteWallsOnBoard().get(i).getMove().getTargetTile().getColumn()+96);
	        data += ","+(gamePosition.getWhiteWallsOnBoard().get(i).getMove().getTargetTile().getRow());
	        data += convertWallDir(gamePosition.getWhiteWallsOnBoard().get(i).getMove().getWallDirection());
	        }
		return data;
	}
	
	private static String convertWallDir(Direction direction){
		switch(direction){
			case Horizontal:
				return "h";
			case Vertical:
				return "v";
			default:
				return null;
		}
	}

	private static Direction converToDir(String direction){
		switch (direction){
			case "v":
				return Direction.Vertical;
			case "h":
				return Direction.Horizontal;
			default:
				return null;
		}
	}
}