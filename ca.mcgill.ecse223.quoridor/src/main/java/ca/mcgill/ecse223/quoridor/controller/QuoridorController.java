/**
 * @author arneetkalra
 * 
 */


package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public interface QuoridorController {

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
	public void moveWall(WallMove wallMoveCandidate, String side);
	
	/**
	 * <p> 8. Drop Wall <p>
	 * <p>dropWall method that allows player to place the wall after navigating to the designated (and valid) area in order to register
	 * the wall placement as a move for the player. <p>
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 */
	public void dropWall(WallMove wallMoveCandidate);
	/**
	 *<p> Boolean method that returns if a WallMove has been completed<p>
	 * @author arneetkalra
	 * @param moveWall
	 * @return boolean
	 */
	public boolean isWallMoved(WallMove movedWall);
	
	/**
	 * <p>Boolean method that can check if a wall was moved to a certain row and column <p>
	 * @author arneetkalra
	 * @param row the reference of the row 
	 * @param col the reference of the column
	 * @return boolean
	 */
	public boolean isWallMovedTo(int row, int col);
}


