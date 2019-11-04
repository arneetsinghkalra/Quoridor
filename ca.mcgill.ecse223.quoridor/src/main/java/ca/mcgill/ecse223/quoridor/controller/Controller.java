package ca.mcgill.ecse223.quoridor.controller;


import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;

import javax.swing.*;

import java.sql.Time;

import static ca.mcgill.ecse223.quoridor.QuoridorApplication.*;


public class Controller {
	
  /**
   * <p> Start New Game <p>
   * <p> Initialize a new Game object.
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
   * <p> Provide or Select User Name <p>
   * <p> Lets the user select an existing user name that was used in a previous game or
   * enter a new one.
   * @param user 
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public static void provideOrSelectUserName(User user) {
}
  	
  /**
   * <p> Set Total Thinking Time <p>
   * <p> Set the total thinking time for player.
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public static void setTotalThinkingTime() {

  }
  
  
  /**
   * <p> Start the Clock <p>
   * <p> Initiates the game timer which starts when the game is running.
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public static void startClock() {

}
  
  /**
   * <p> Select an Existing Username <p>
   * <p> The user selects an existing user name that was previously used in a game
   * @param username is a String that is the existing user name
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public static void selectExistingUsername(String username) {
}
  
  
  /**
   * <p> Provide a New Username <p>
   * <p> The user selects/enters a new user name that was not previously used in a game
   * @param username is a String that is the new user name
   * 
   * @author Ali Tapan
   * @verison 1.0
   */
  public static void provideNewUsername(String username) {
}

  /**
   * @author Sam Perreault
   * Generates the board, and sets the starting position and walls of each player.
   * In addition, sets white/player 1 as the player to move, and starts counting down
   * the white player's thinking time.
   */
  public static void initializeBoard() {
  	// white to move
	  // white in spot top middle
	  // black in spot bottom middle
	  // white has walls in stock
	  // black has walls in stock
	  // white's clock starts
	  // white's turn is shown
	  Quoridor q = getQuoridor();
	Board board = new Board(q);
	Tile t;
	for(int i=0;i<9;i++)
		for(int j=0; j<9; j++)
			t= new Tile(i,j,board);
	PlayerPosition whitePlayerPosition = new PlayerPosition(q.getCurrentGame().getWhitePlayer(), board.getTile(4));
	PlayerPosition blackPlayerPosition = new PlayerPosition(q.getCurrentGame().getWhitePlayer(), board.getTile(76));
	GamePosition gp = new GamePosition(0,whitePlayerPosition,blackPlayerPosition
			, q.getCurrentGame().getWhitePlayer(), q.getCurrentGame());
	q.getCurrentGame().setCurrentPosition(gp);
	for(int i=0;i<10;i++)
	{
		Wall a,b;
		a = new Wall(i, q.getCurrentGame().getWhitePlayer());
		b = new Wall(i+10, q.getCurrentGame().getBlackPlayer());
	}
	QuoridorWindow window = quoridorWindow;
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
		Quoridor q = getQuoridor();
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

  /**
   * @author Luke Barber
   * Grabs a given wall and holds it so that it is ready for use. 
   * @param wall The wall that will be grabbed
   */
	public static void grabWall(Wall wall) {
}
		// TODO Auto-generated method stub

	
	/**
   * @author Luke Barber
   * Rotates a given wall that is on the board. 
   * @param wall The wall that will be rotated
   */
	public static void rotateWall(Wall wall) {
		}
	
  /**
	 * <p> 7. Move Wall <p>
	 * <p>moveWall method that allows a player to move the wall that they have in their hand over the board. 
	 * It will be allowed to move over the rows and columns of the board and also give an error when it is placed in an incorrect position.<p>
	 *
	 * @author arneetkalra
	 * @param wallMoveCandidate 
	 * @param side references the Wall that player will have in their hand
	 * @return void method but allows player to manipulate wall over board 
	 */
	public static void moveWall(WallMove wallMoveCandidate, String side) {
}
	
	/**
	 * <p> 8. Drop Wall <p>
	 * <p>dropWall method that allows player to place the wall after navigating to the designated (and valid) area in order to register
	 * the wall placement as a move for the player. <p>
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 */
	public static void dropWall(WallMove wallMoveCandidate) {
	}
	/**
	 *<p> Boolean method that returns if a WallMove has been completed<p>
	 * @author arneetkalra
	 * @param moveWall
	 * @return boolean
	 */
	public static boolean isWallMoved(WallMove movedWall) {
		return false;
	}
	
	/**
	 * <p>Boolean method that can check if a wall was moved to a certain row and column <p>
	 * @author arneetkalra
	 * @param row the reference of the row 
	 * @param col the reference of the column
	 * @return boolean
	 */
	public static boolean isWallMovedTo(int row, int col) {
		return false;
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
	public static Quoridor loadPosition(Quoridor quoridor, String fileName) {
	return null;
}
	
	
	/**
	 * Save the game into a game file
	 * @author Yin
	 * @param fileName
	 * */
	public static void savePosition(String fileName, GamePosition gamePosition) {
	}
	
	/**
	 * @author Yin Zhang 260726999
  	 * The user confirm whether to overwrite the existing file
  	 * */
	public static void confirmsToOverWrite() {
	}
	
	  /**
	 * <p>11 Validate Position<p>
	 * <p>validate if the player positions and wall positions are valid 
	 * e.g. overlapping walls or outof-track pawn or wall positions. <p>
	 * 
	 * @author William Wang
	 * @param position the currentPosition object of the game
	 * @return the validation result, true for pass, false for error
	 */
	public static boolean validatePosition(GamePosition position) {
		return false;
	}
		
	/**
	 * <p>12. Switch player (aka. Update board)<p>
	 * <p>Switch current player and update clock <p>
	 * 
	 * @author William Wang
	 * @param game the current quoridor game
	 */
	public static void switchCurrentPlayer(Game game) {
	}
}


