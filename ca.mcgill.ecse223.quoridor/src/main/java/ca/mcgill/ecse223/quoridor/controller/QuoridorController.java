package ca.mcgill.ecse223.quoridor.controller;

public interface QuoridorController{

	/**
     * @author Luke Barber
     * Grabs a given wall and holds it so that it is ready for use. 
     * @param wall The wall that will be grabbed
     */
	void grabWall(Wall wall);
	
	/**
     * @author Luke Barber
     * Rotates a given wall that is on the board. 
     * @param wall The wall that will be rotated
     */
	void rotateWall(Wall wall);

}
