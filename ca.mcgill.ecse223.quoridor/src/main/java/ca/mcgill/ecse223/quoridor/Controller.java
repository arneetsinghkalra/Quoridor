package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

public interface Controller {
	public Quoridor loadPosition(Quoridor quoridor, String fileName);
	public void savePosition(String fileName);
	public boolean validatePosition(GamePosition gamePosition); 
}
