/**
 * @author arneetkalra
 * 
 */


package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public interface Controller {

	/**
	 * moveWall method that allows a player to move the wall that they have in their hand over the board. 
	 * It will be allowed to move over the rows and columns of the board and also give an error when it is placed in an incorrect position.
	 *
	 * @author arneetkalra
	 * @param wallMoveCandidate 
	 * @param side references the Wall that player will have in their hand
	 * @return void method but allows player to manipulate wall over board 
	 * @Version 1.0
	 */
	public static void moveWall(WallMove wallMoveCandidate, String side) {
		throw new IllegalStateException(); 	}
	
	/**
	 * dropWall method that allows player to place the wall after navigating to the designated (and valid) area in order to register
	 * the wall placement as a move for the player. 
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 * @Version 1.0
	 */
	public static void dropWall(Wall aWall) {
		throw new IllegalStateException(); 
	}
	
	/**
	 * dropWall method that allows player to place the wall after navigating to the designated (and valid) area in order to register
	 * the wall placement as a move for the player. 
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 * @Version 1.0
	 */
	public static boolean isWallMoved(WallMove movedWall)  {
		throw new IllegalStateException(); 	
	}
	
	/**
	 * dropWall method that allows player to place the wall after navigating to the designated (and valid) area in order to register
	 * the wall placement as a move for the player. 
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 * @Version 1.0
	 */
	public static boolean isWallMovedTo(WallMove movedWall, int row, int col) {
		throw new IllegalStateException(); 
	}
}


