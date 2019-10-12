package ca.mcgill.ecse223.quoridor.controller;


import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;



public interface Controller {
	
  /**
   * <p> Start New Game <p>
   * <p> Initialize a new Game object.
   * @return the created initialized Game object
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public Game StartNewGame();
  
  /**
   * <p> Provide or Select User Name <p>
   * <p> Lets the user select an existing user name that was used in a previous game or
   * enter a new one.
   * @param user 
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public void provideOrSelectUserName(User user);
  	
  /**
   * <p> Set Total Thinking Time <p>
   * <p> Set the total thinking time for player.
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public void setTotalThinkingTime();
  
  
  /**
   * <p> Start the Clock <p>
   * <p> Initiates the game timer which starts when the game is running.
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public void startClock();
  
  /**
   * <p> Select an Existing Username <p>
   * <p> The user selects an existing user name that was previously used in a game
   * @param username is a String that is the existing user name
   * 
   * @author Ali Tapan
   * @version 1.0
   */
  public void selectExistingUsername(String username);
  
  
  /**
   * <p> Provide a New Username <p>
   * <p> The user selects/enters a new user name that was not previously used in a game
   * @param username is a String that is the new user name
   * 
   * @author Ali Tapan
   * @verison 1.0
   */
  public void provideNewUsername(String username);
  
}


