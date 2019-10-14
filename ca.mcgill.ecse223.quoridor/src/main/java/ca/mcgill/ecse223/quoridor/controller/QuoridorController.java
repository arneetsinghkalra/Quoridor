package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.Wall;

public interface QuoridorController{

	/**
     * @author Luke Barber
     * Grabs a given wall and holds it so that it is ready for use. 
     * @param wall The wall that will be grabbed
     */
	public void grabWall(Wall wall);
		// TODO Auto-generated method stub

	
	/**
     * @author Luke Barber
     * Rotates a given wall that is on the board. 
     * @param wall The wall that will be rotated
     */
	public void rotateWall(Wall wall);


}
