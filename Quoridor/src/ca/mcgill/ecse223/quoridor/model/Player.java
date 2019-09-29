package ca.mcgill.ecse223.quoridor.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4677.5b9d0a5c4 modeling language!*/



// line 2 "model.ump"
// line 88 "model.ump"
public class Player
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private String name;
  private boolean isTurn;
  private int wallsLeft;
  private int position;
  private String colour;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(String aName, boolean aIsTurn, int aWallsLeft, int aPosition, String aColour)
  {
    name = aName;
    isTurn = aIsTurn;
    wallsLeft = aWallsLeft;
    position = aPosition;
    colour = aColour;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsTurn(boolean aIsTurn)
  {
    boolean wasSet = false;
    isTurn = aIsTurn;
    wasSet = true;
    return wasSet;
  }

  public boolean setWallsLeft(int aWallsLeft)
  {
    boolean wasSet = false;
    wallsLeft = aWallsLeft;
    wasSet = true;
    return wasSet;
  }

  public boolean setPosition(int aPosition)
  {
    boolean wasSet = false;
    position = aPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean setColour(String aColour)
  {
    boolean wasSet = false;
    colour = aColour;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public boolean getIsTurn()
  {
    return isTurn;
  }

  public int getWallsLeft()
  {
    return wallsLeft;
  }

  public int getPosition()
  {
    return position;
  }

  public String getColour()
  {
    return colour;
  }

  public void delete()
  {}

  // line 12 "model.ump"
   public void decrementWall(){
    
  }

  // line 15 "model.ump"
   public int checkWinCondition(){
	return 0;
    
  }

  // line 19 "model.ump"
   public void resign(){
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "isTurn" + ":" + getIsTurn()+ "," +
            "wallsLeft" + ":" + getWallsLeft()+ "," +
            "position" + ":" + getPosition()+ "," +
            "colour" + ":" + getColour()+ "]";
  }
}