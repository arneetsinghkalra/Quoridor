package ca.mcgill.ecse223.quoridor.controller;

import java.sql.Time;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;



public interface Controller {
	
	
	public static void selectExistingUserName(Player player) throws UnsupportedOperationException  {
		
	}
	
	public static void whiteSelectUserName(Game game) throws UnsupportedOperationException {
		
	}
	
	public static void blackSelectUserName(Game game) throws UnsupportedOperationException {
		
	}
	
	public static Game StartNewGame() throws UnsupportedOperationException {
		
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		User user1 = quoridor.addUser("userWhite");
		User user2 = quoridor.addUser("userBlack");
		int thinkingTime = 180;
		
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);
		
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, player1, player2, quoridor);
		
		return game;
	}
	
	public static void enterNewUserName(String username) throws UnsupportedOperationException {
		
	}
	
	public static void startClock(Game game) throws UnsupportedOperationException {
		
	}
	
	public static void setTimer(Game game) throws UnsupportedOperationException {
		
	}
	
}
