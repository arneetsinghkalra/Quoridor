package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;

public interface Controller {

	public boolean validatePosition(GamePosition position);
	
	public Tile getWallPostition(Wall wall);
	
	public void switchCurrentPlayer(Game game);
	
}
