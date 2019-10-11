package ca.mcgill.ecse223.quoridor.controller;

import java.sql.Time;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;



public interface Controller {
	
    /**
     * This controller method lets the current player choose an existing name which was previously
     * used in a game.
     * @param player
     * @throws UnsupportedOperationException required exception type for Iteration 2 of ECSE 223 Project
     * @author Ali Tapan
	 * @version 1.0
     */
	public static void selectExistingUserName(Player player) throws UnsupportedOperationException  {
		
	}
	/**
	 * This controller method enables the Player 1 (White) to choose a user name. The control
	 * of the game has to be passed on to Player 1 in order for it to choose a name.
	 * @param game
	 * @throws UnsupportedOperationException required exception type for Iteration 2 of ECSE 223 Project
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void whiteSelectUserName(Game game) throws UnsupportedOperationException {
		
	}
	
	/**
	 * This controller method enables the Player 2 (Black) to choose a user name. The control
	 * of the game has to be passed on to Player 2 in order for it to choose a name.
	 * @param game
	 * @throws UnsupportedOperationException required exception type for Iteration 2 of ECSE 223 Project
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void blackSelectUserName(Game game) throws UnsupportedOperationException {
		
	}
	
	/**
	 * This creates an initialized instance of Game with users. It is used in Feature 1 and Feature 2 
	 * of the assignment mainly for precondition statements.
	 * @return game object with game status set as GameStatus.Initializing
	 * @throws UnsupportedOperationException required exception type for Iteration 2 of ECSE 223 Project
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static Game StartNewGame() throws UnsupportedOperationException {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		User user1 = quoridor.addUser("userWhite");
		User user2 = quoridor.addUser("userBlack");
		int thinkingTime = 180;
		
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);
		
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, player1, player2, quoridor);
		//Returns an initialized Game object//
		return game;
	}
	
	/**
	 * This controller method lets the current player enter a new user name to be used in quoridor.
	 * @param username a string containing the user name
	 * @throws UnsupportedOperationException
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static void enterNewUserName(String username) throws UnsupportedOperationException {
		
	}
	
	/**
	 * This controller method starts the game clock. The game shall be in a running state when the clock starts. This
	 * method ensures the game is initialized and proceeds to set the game status as GameStatus.Running
	 * @return game object with game status set as GameStatus.Running
	 * @param game
	 * @throws UnsupportedOperationException
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static Game startClock(Game game) throws UnsupportedOperationException {
		game.setGameStatus(GameStatus.Running);
		return game;
	}
	
	/**
	 * This controller method checks if the total thinking time is set, then proceeds to change the
	 * game status to GameStatus.ReadyToStart.
	 * @param game
	 * @return game object with game status set as GameStatus.ReadyToStart
	 * @throws UnsupportedOperationException
	 * @author Ali Tapan
	 * @version 1.0
	 */
	public static Game setTimer(Game game) throws UnsupportedOperationException {
		game.getBlackPlayer().getRemainingTime();
		game.getWhitePlayer().getRemainingTime();
		game.setGameStatus(GameStatus.ReadyToStart);
		return game;
	}
	
	
}
