package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;

public class QuoridorControllerImplementation implements Controller{

	@Override
	public Game StartNewGame() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}

	@Override
	public void provideOrSelectUserName(User user){
	    // TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}

	
	@Override
	public void setTotalThinkingTime() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}
	
	@Override
	public void startClock() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}
	
	@Override
	public void selectExistingUsername(String username) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}
	
	
	@Override
	public void provideNewUsername(String username) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented!");
	}

	@Override
    public void setPlayerThinkingTime(int minute, int second) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initializeBoard() {
        throw new UnsupportedOperationException();
    }
    
    @Override
	public void grabWall(Wall wall) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void rotateWall(Wall wall) {
		throw new UnsupportedOperationException();
	}
}
