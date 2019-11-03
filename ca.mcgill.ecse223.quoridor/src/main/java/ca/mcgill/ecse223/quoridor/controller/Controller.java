package ca.mcgill.ecse223.quoridor.controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;

import static ca.mcgill.ecse223.quoridor.model.Direction.Horizontal;
import static ca.mcgill.ecse223.quoridor.model.Direction.Vertical;


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
	public static Quoridor loadPosition(String fileName) {
		
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		List<String> lines = Collections.emptyList(); 
	    try
	    { 
	      lines = Files.readAllLines(Paths.get("src/test/resources/savePosition/"+fileName+".txt"), StandardCharsets.UTF_8); 
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
            e.printStackTrace();
	    }
        StringTokenizer first = new StringTokenizer(firstLine, ",");
        StringTokenizer second = new StringTokenizer(secondLine, ",");
        
        	String nextPlayer = first.nextToken();
	    String opponent = second.nextToken();
		int nextPlayerColumn = 0;
		int nextPlayerRow = 0;
		try{
			nextPlayerColumn =convertToInt(nextPlayer.substring(2,3));
			nextPlayerRow = Integer.parseInt(nextPlayer.substring(3));
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		Tile nextPlayerTile = quoridor.getBoard().getTile((nextPlayerRow-1)*9+nextPlayerColumn-1);
		int opponentColumn = 0;
		int opponentRow = 0;
		try{
			opponentColumn =convertToInt(opponent.substring(2,3));
			opponentRow = Integer.parseInt(opponent.substring(3));
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
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
				try{
					column =convertToInt(wallPosition.substring(0,1));
					row = Integer.parseInt(wallPosition.substring(1,2));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
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
				try{
					column =convertToInt(wallPosition.substring(0,1));
					row = Integer.parseInt(wallPosition.substring(1,2));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
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
				try{
					column =convertToInt(wallPosition.substring(0));
					row = Integer.parseInt(wallPosition.substring(1));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
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
				try{
					column =convertToInt(wallPosition.substring(0));
					row = Integer.parseInt(wallPosition.substring(1));
					direction = wallPosition.substring(2);
				}
				catch(IndexOutOfBoundsException e){
					e.printStackTrace();
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
				//throws exception
			}
	    boolean sameRemainingWall = (quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size()==quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
	    if(validatePosition(quoridor.getCurrentGame().getCurrentPosition())&&sameRemainingWall) {
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
		Path path = Paths.get("src/test/resources/savePosition/"+fileName+".txt");
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
//        FileWriter fr = null;
//        BufferedWriter br = null;
//        String data = "";
//    		if(gamePosition.getPlayerToMove().getUser().getName().equals(gamePosition.getBlackPosition().getPlayer().getUser().getName())) {
//    			data += blackPlayerData(gamePosition)+"\n";
//    			data += whitePlayerData(gamePosition);
//        }
//        else {
//	        	data += whitePlayerData(gamePosition)+"\n";
//    			data += blackPlayerData(gamePosition);
//        }
//        try{
//            fr = new FileWriter(file);
//            br = new BufferedWriter(fr);
//            	br.write(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally{
//            try {
//                br.close();
//                fr.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
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
	public static void switchCurrentPlayer(Game game) {
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
				return Vertical;
			case "h":
				return Horizontal;
			default:
				return null;
		}
	}
	
	private static int convertToInt(String letter) {
		int number = (int)letter.charAt(0)-96;
		return number;
		
	}
}


