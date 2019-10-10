package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;


public interface Controller {
	
	public static void selectExistingUserName(Player player) throws IllegalArgumentException  {
		
	}
	
	public static Game InitNewGame() throws IllegalArgumentException {
		Game game = null;
		return game;
	}
	
	public static void enterNewUserName(String username) throws IllegalArgumentException {
		
	}
	
	public static void startClock() throws IllegalArgumentException {
		
	}
	
	public static void setTimer(Game game) throws IllegalArgumentException {
		
	}
	
}
