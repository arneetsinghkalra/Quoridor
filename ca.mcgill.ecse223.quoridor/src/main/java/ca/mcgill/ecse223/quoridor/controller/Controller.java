/**
 * @author arneetkalra
 */


package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Wall;

public interface Controller {

	/**
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but allows player to manipulate wall over board 
	 */
	public static void moveWall(Wall aWall) {
		throw new IllegalStateException(); 	}
	
	/**
	 * @author arneetkalra
	 * @param aWall references the Wall that player will have in their hand
	 * @return void method but drops wall which prompts end of player turn
	 */
	public static void dropWall(Wall aWall) {
		throw new IllegalStateException(); 
	}
}
