package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;



public interface Controller {
	
	
	public static void selectExistingUserName(Player player) throws UnsupportedOperationException  {
	}
	
	public static Game InitNewGame() throws UnsupportedOperationException {
		Game game = null;
		return game;
	}
	
	public static void enterNewUserName(String username) throws UnsupportedOperationException {
		
	}
	
	public static void startClock() throws UnsupportedOperationException {
		
	}
	
	public static void setTimer(Game game) throws UnsupportedOperationException {
		
	}
	
}
