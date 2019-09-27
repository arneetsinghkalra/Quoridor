package quoridor;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.4677.5b9d0a5c4 modeling language!*/


import java.util.*;

// line 46 "model.ump"
// line 101 "model.ump"
public class State
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //State Attributes
  private int indexForListOfBoards;
  private int playerTurn;

  //State Associations
  private List<Board> boards;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public State(int aIndexForListOfBoards, int aPlayerTurn)
  {
    indexForListOfBoards = aIndexForListOfBoards;
    playerTurn = aPlayerTurn;
    boards = new ArrayList<Board>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIndexForListOfBoards(int aIndexForListOfBoards)
  {
    boolean wasSet = false;
    indexForListOfBoards = aIndexForListOfBoards;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayerTurn(int aPlayerTurn)
  {
    boolean wasSet = false;
    playerTurn = aPlayerTurn;
    wasSet = true;
    return wasSet;
  }

  public int getIndexForListOfBoards()
  {
    return indexForListOfBoards;
  }

  public int getPlayerTurn()
  {
    return playerTurn;
  }
  /* Code from template association_GetMany */
  public Board getBoard(int index)
  {
    Board aBoard = boards.get(index);
    return aBoard;
  }

  public List<Board> getBoards()
  {
    List<Board> newBoards = Collections.unmodifiableList(boards);
    return newBoards;
  }

  public int numberOfBoards()
  {
    int number = boards.size();
    return number;
  }

  public boolean hasBoards()
  {
    boolean has = boards.size() > 0;
    return has;
  }

  public int indexOfBoard(Board aBoard)
  {
    int index = boards.indexOf(aBoard);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBoards()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addBoard(Board aBoard)
  {
    boolean wasAdded = false;
    if (boards.contains(aBoard)) { return false; }
    boards.add(aBoard);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBoard(Board aBoard)
  {
    boolean wasRemoved = false;
    if (boards.contains(aBoard))
    {
      boards.remove(aBoard);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBoardAt(Board aBoard, int index)
  {  
    boolean wasAdded = false;
    if(addBoard(aBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBoards()) { index = numberOfBoards() - 1; }
      boards.remove(aBoard);
      boards.add(index, aBoard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBoardAt(Board aBoard, int index)
  {
    boolean wasAdded = false;
    if(boards.contains(aBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBoards()) { index = numberOfBoards() - 1; }
      boards.remove(aBoard);
      boards.add(index, aBoard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBoardAt(aBoard, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    boards.clear();
  }

  // line 53 "model.ump"
   public Board startNewGame(){
	return null;
    
  }

  // line 56 "model.ump"
   public Board loadGame(){
	return null;
    
  }

  // line 59 "model.ump"
   public void saveGame(){
    
  }

  // line 62 "model.ump"
   public void saveBoard(){
    
  }

  // line 65 "model.ump"
   public boolean checkDraw(){
	return false;
    
  }

  // line 68 "model.ump"
   public Board undo(){
	return null;
    
  }

  // line 71 "model.ump"
   public int switchTurns(){
	return 0;
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "indexForListOfBoards" + ":" + getIndexForListOfBoards()+ "," +
            "playerTurn" + ":" + getPlayerTurn()+ "]";
  }
  
  public static void main( String[] args ){
	   System.out.println("Helloworld");
	 }
}