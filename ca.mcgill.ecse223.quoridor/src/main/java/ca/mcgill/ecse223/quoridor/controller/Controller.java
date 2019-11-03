package ca.mcgill.ecse223.quoridor.controller;


import java.util.List;

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
		}
		else {
			newPosition = new GamePosition(currentPosition.getId()+1, player1Position, player2Position, game.getWhitePlayer(), game);	
		}
		game.addPosition(currentPosition);
		game.setCurrentPosition(newPosition);
	}
}


