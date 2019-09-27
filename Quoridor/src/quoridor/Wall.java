package quoridor;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4677.5b9d0a5c4 modeling language!*/



// line 83 "model.ump"
// line 113 "model.ump"
public class Wall
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Attributes
  private int position;
  private String direction;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(int aPosition, String aDirection)
  {
    position = aPosition;
    direction = aDirection;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPosition(int aPosition)
  {
    boolean wasSet = false;
    position = aPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean setDirection(String aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public int getPosition()
  {
    return position;
  }

  public String getDirection()
  {
    return direction;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "position" + ":" + getPosition()+ "," +
            "direction" + ":" + getDirection()+ "]";
  }
}