package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class QuoridorController implements Controller{
	/**
	 * 
	 * Load the game from the game file. 
	 * load the correct player position and wall position
	 * @author Yin
	 * @param quoridor This is the quoridor you want to load the game into
	 * @param fileName This is the name of the file which stores the game
	 * 
	 * */
	@Override
	public Quoridor loadPosition(Quoridor quoridor, String fileName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");
	}
	
	/**
	 * Save the game into a game file
	 * @author Yin
	 * @param fileName
	 * */
	@Override
	public void savePosition(String fileName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");
	}
	@Override
	public boolean validatePosition(GamePosition gamePosition) {
		throw new UnsupportedOperationException("not implemented");
	}
	
	public Game readFileGame(String fileName) {
		
		throw new UnsupportedOperationException("not implemented");
	}
	
	public boolean ifPositionValid(GamePosition gamePosition) {
		throw new UnsupportedOperationException("not implemented");
	}
	
	public void confirmsToOverWrite() {
		throw new UnsupportedOperationException("not implemented");
	}
	
}
