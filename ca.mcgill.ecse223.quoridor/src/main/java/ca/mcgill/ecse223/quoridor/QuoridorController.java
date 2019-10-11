package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public class QuoridorController implements Controller{

	@Override
	public Quoridor loadPosition(Quoridor quoridor, String fileName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");
	}

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
