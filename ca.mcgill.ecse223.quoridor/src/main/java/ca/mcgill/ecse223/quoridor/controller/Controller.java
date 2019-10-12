package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;

public interface Controller {

	 /**
     * <p>11 Validate Position<p>
     * <p>validate if the player positions and wall positions are valid 
     * e.g. overlapping walls or outof-track pawn or wall positions. <p>
     * 
     * @author William Wang
     * @param position the currentPosition object of the game
     * @return the validation result, true for pass, false for error
     */
	public boolean validatePosition(GamePosition position);
		
	/**
     * <p>12. Switch player (aka. Update board)<p>
     * <p>Switch current player and update clock <p>
     * 
     * @author William Wang
     * @param game the current quoridor game
     */
	public void switchCurrentPlayer(Game game);
	
}
