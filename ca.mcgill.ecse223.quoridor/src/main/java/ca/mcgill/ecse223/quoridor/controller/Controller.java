package ca.mcgill.ecse223.quoridor.controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;



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
   * Sets the starting thinking time for the players. Time is accepted as minutes and seconds,
   * and is converted to milliseconds.
   * @param minute The number of minutes allowed to each player
   * @param second The number of seconds allowed to each player
   */
  public static void  setPlayerThinkingTime(int minute, int second) {
}

  /**
   * @author Sam Perreault
   * Generates the board, and sets the starting position and walls of each player.
   * In addition, sets white/player 1 as the player to move, and starts counting down
   * the white player's thinking time.
   */
  public static void initializeBoard() {
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
		List<String> lines = Collections.emptyList(); 
	    try
	    { 
	      lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8); 
	    } 
	    catch (IOException e) 
	    { 
	      // do something 
	      e.printStackTrace(); 
	    }
	    String firstLine;
	    String secondLine;
	    try {
		    firstLine = lines.get(0);
		    secondLine = lines.get(1);
	    }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
	    }
        StringTokenizer first = new StringTokenizer(firstLine, ",");
        StringTokenizer second = new StringTokenizer(secondLine, ",");
        

	    Player blackPlayer;
	    Player whitePlayer;
	    blackPlayer.setGameAsBlack(quoridor.getCurrentGame());
	    whitePlayer.setGameAsWhite(quoridor.getCurrentGame());
	    first.nextToken();
	    while(first.hasMoreTokens()) {
		    Wall wall;
		    Tile tile = new Tile();
		    first.nextToken();
		    wall.getMove().setTargetTile(aNewTargetTile);
		    wall.setOwner(blackPlayer);
		    wall.getMove().setWallDirection(aWallDirection);
		    quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard().add(wall);
	    }
	    for() {
	    		Wall wall;
		    wall.getMove().setTargetTile(aNewTargetTile);
		    wall.setOwner(whitePlayer);
		    wall.getMove().setWallDirection(aWallDirection);
		    quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard().add(wall);
	    }
	    quoridor.getCurrentGame().getCurrentPosition().setBlackPosition(aNewBlackPosition);
	    quoridor.getCurrentGame().getCurrentPosition().setWhitePosition(aNewBlackPosition);
	    quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(aNewPlayerToMove);



		    
		return quoridor;
		
		
	return null;
}
	
	
	/**
	 * Save the game into a game file
	 * @author Yin
	 * @param fileName
	 * */
	public static void savePosition(String fileName, GamePosition gamePosition) {

        File file = new File("/Users/pankaj/"+fileName+".txt");
        FileWriter fr = null;
        BufferedWriter br = null;
        String data = "";
        if (file.exists() && !file.isDirectory()) {
        		if(gamePosition.getPlayerToMove().getUser().getName()==gamePosition.getBlackPosition().getPlayer().getUser().getName()) {
        			data += blackPlayerData(gamePosition)+"\n";
        			data += whitePlayerData(gamePosition);
	        }
	        else {
		        	data += whitePlayerData(gamePosition)+"\n";
	    			data += blackPlayerData(gamePosition);
	        }
            try{
                fr = new FileWriter(file);
                br = new BufferedWriter(fr);
                	br.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    br.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    		}else {
	        	if(gamePosition.getPlayerToMove().getUser().getName()==gamePosition.getBlackPosition().getPlayer().getUser().getName()) {
	    			data += blackPlayerData(gamePosition)+"\n";
	    			data += whitePlayerData(gamePosition);
	        }
	        else {
		        	data += whitePlayerData(gamePosition)+"\n";
	    			data += blackPlayerData(gamePosition);
	        }
	        try{
	            fr = new FileWriter(file);
	            br = new BufferedWriter(fr);
	            	br.write(data);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                br.close();
	                fr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
        }
		
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
	
	private static String blackPlayerData(GamePosition gamePosition) {
		String data ="";
		data += "B:"+gamePosition.getBlackPosition().getTile().toString();
		for(int i = 0; i<gamePosition.getBlackWallsOnBoard().size(); i++) {
	        data += ","+gamePosition.getBlackWallsOnBoard().get(i).getMove().getTargetTile().toString();
	        data += convertWallDir(gamePosition.getBlackWallsOnBoard().get(i).getMove().getWallDirection());
	        }
		String blackWallRemaining = String.valueOf(gamePosition.getBlackWallsInStock().size());
		data += blackWallRemaining;
		return data;
	}
	
	private static String whitePlayerData(GamePosition gamePosition) {
		String data ="";
		data += "W:"+gamePosition.getWhitePosition().getTile().toString();
		for(int i = 0; i<gamePosition.getWhiteWallsOnBoard().size(); i++) {
	        data += ","+gamePosition.getWhiteWallsOnBoard().get(i).getMove().getTargetTile().toString();
	        data += convertWallDir(gamePosition.getWhiteWallsOnBoard().get(i).getMove().getWallDirection());
	        }
		String whiteWallRemaining = String.valueOf(gamePosition.getWhiteWallsInStock().size());
		data += whiteWallRemaining;
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
}


