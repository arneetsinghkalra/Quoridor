/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
// line 5 "../../../../../PawnStateMachine.ump"
public class PawnBehavior
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum MoveDirection { East, South, West, North }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehavior State Machines
  public enum PawnSM { NoAdjacentOpponent, AdjacentOpponent }
  public enum PawnSMNoAdjacentOpponent { Null, NoAdjacentWall, AdjacentWall }
  public enum PawnSMAdjacentOpponent { Null, WallBehindAdjacentOpponent }
  private PawnSM pawnSM;
  private PawnSMNoAdjacentOpponent pawnSMNoAdjacentOpponent;
  private PawnSMAdjacentOpponent pawnSMAdjacentOpponent;

  //PawnBehavior Associations
  private static Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehavior()
  {
    setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
    setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.Null);
    setPawnSM(PawnSM.NoAdjacentOpponent);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    if (pawnSMNoAdjacentOpponent != PawnSMNoAdjacentOpponent.Null) { answer += "." + pawnSMNoAdjacentOpponent.toString(); }
    if (pawnSMAdjacentOpponent != PawnSMAdjacentOpponent.Null) { answer += "." + pawnSMAdjacentOpponent.toString(); }
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public PawnSMNoAdjacentOpponent getPawnSMNoAdjacentOpponent()
  {
    return pawnSMNoAdjacentOpponent;
  }

  public PawnSMAdjacentOpponent getPawnSMAdjacentOpponent()
  {
    return pawnSMAdjacentOpponent;
  }

  private boolean __autotransition1__()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case NoAdjacentOpponent:
        if (isAdjacentOpponent())
        {
          exitPawnSM();
          setPawnSM(PawnSM.AdjacentOpponent);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition6__()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case AdjacentOpponent:
        if (!(isAdjacentOpponent()))
        {
          exitPawnSM();
          setPawnSM(PawnSM.NoAdjacentOpponent);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition7__()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case AdjacentOpponent:
        if (isWallBehindOpponent())
        {
          exitPawnSMAdjacentOpponent();
          setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.WallBehindAdjacentOpponent);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition2__()
  {
    boolean wasEventProcessed = false;
    
    PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
    switch (aPawnSMNoAdjacentOpponent)
    {
      case NoAdjacentWall:
        if (isAdjacentWall())
        {
          exitPawnSMNoAdjacentOpponent();
          setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.AdjacentWall);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition3__()
  {
    boolean wasEventProcessed = false;
    
    PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
    switch (aPawnSMNoAdjacentOpponent)
    {
      case NoAdjacentWall:
        if (!(isAdjacentWall()))
        {
          exitPawnSMNoAdjacentOpponent();
          setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.NoAdjacentWall);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition4__()
  {
    boolean wasEventProcessed = false;
    
    PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
    switch (aPawnSMNoAdjacentOpponent)
    {
      case AdjacentWall:
        if (isAdjacentWall())
        {
          exitPawnSMNoAdjacentOpponent();
          setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.AdjacentWall);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition5__()
  {
    boolean wasEventProcessed = false;
    
    PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
    switch (aPawnSMNoAdjacentOpponent)
    {
      case AdjacentWall:
        if (!(isAdjacentWall()))
        {
          exitPawnSMNoAdjacentOpponent();
          setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.NoAdjacentWall);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition8__()
  {
    boolean wasEventProcessed = false;
    
    PawnSMAdjacentOpponent aPawnSMAdjacentOpponent = pawnSMAdjacentOpponent;
    switch (aPawnSMAdjacentOpponent)
    {
      case WallBehindAdjacentOpponent:
        if (!(isAdjacentOpponent()))
        {
          exitPawnSM();
          setPawnSM(PawnSM.NoAdjacentOpponent);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitPawnSM()
  {
    switch(pawnSM)
    {
      case NoAdjacentOpponent:
        exitPawnSMNoAdjacentOpponent();
        break;
      case AdjacentOpponent:
        exitPawnSMAdjacentOpponent();
        break;
    }
  }

  private void setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;

    // entry actions and do activities
    switch(pawnSM)
    {
      case NoAdjacentOpponent:
        if (pawnSMNoAdjacentOpponent == PawnSMNoAdjacentOpponent.Null) { setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.NoAdjacentWall); }
        __autotransition1__();
        break;
      case AdjacentOpponent:
        if (pawnSMAdjacentOpponent == PawnSMAdjacentOpponent.Null) { setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.WallBehindAdjacentOpponent); }
        __autotransition6__();
        __autotransition7__();
        break;
    }
  }

  private void exitPawnSMNoAdjacentOpponent()
  {
    switch(pawnSMNoAdjacentOpponent)
    {
      case NoAdjacentWall:
        setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
        break;
      case AdjacentWall:
        setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
        break;
    }
  }

  private void setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent)
  {
    pawnSMNoAdjacentOpponent = aPawnSMNoAdjacentOpponent;
    if (pawnSM != PawnSM.NoAdjacentOpponent && aPawnSMNoAdjacentOpponent != PawnSMNoAdjacentOpponent.Null) { setPawnSM(PawnSM.NoAdjacentOpponent); }

    // entry actions and do activities
    switch(pawnSMNoAdjacentOpponent)
    {
      case NoAdjacentWall:
        __autotransition2__();
        __autotransition3__();
        break;
      case AdjacentWall:
        __autotransition4__();
        __autotransition5__();
        break;
    }
  }

  private void exitPawnSMAdjacentOpponent()
  {
    switch(pawnSMAdjacentOpponent)
    {
      case WallBehindAdjacentOpponent:
        setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.Null);
        break;
    }
  }

  private void setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent aPawnSMAdjacentOpponent)
  {
    pawnSMAdjacentOpponent = aPawnSMAdjacentOpponent;
    if (pawnSM != PawnSM.AdjacentOpponent && aPawnSMAdjacentOpponent != PawnSMAdjacentOpponent.Null) { setPawnSM(PawnSM.AdjacentOpponent); }

    // entry actions and do activities
    switch(pawnSMAdjacentOpponent)
    {
      case WallBehindAdjacentOpponent:
        __autotransition8__();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }

  public boolean hasCurrentGame()
  {
    boolean has = currentGame != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentGame(Game aNewCurrentGame)
  {
    boolean wasSet = false;
    currentGame = aNewCurrentGame;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPlayer(Player aNewPlayer)
  {
    boolean wasSet = false;
    player = aNewPlayer;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    currentGame = null;
    player = null;
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 32 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnRow(){
    return 0;
  }


  /**
   * Returns the current column number of the pawn
   */
  // line 34 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnColumn(){
    return 0;
  }


  /**
   * Returns if it is legal to step in the given direction
   */
  // line 36 "../../../../../PawnStateMachine.ump"
  public static boolean isLegalStep(MoveDirection dir){
	  Quoridor quoridor = QuoridorApplication.getQuordior();
	  currentGame = quoridor.getCurrentGame();
	  int originalBlackColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
	  int originalBlackRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
	  int originalWhiteColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
	  int originalWhiteRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
	  Tile originalBlackTile = currentGame.getQuoridor().getBoard().getTile((originalBlackRow-1)*9+originalBlackColumn-1);
	  Tile originalWhiteTile = currentGame.getQuoridor().getBoard().getTile((originalWhiteRow-1)*9+originalWhiteColumn-1);
	  if(currentGame.getCurrentPosition().getPlayerToMove().equals(currentGame.getBlackPlayer())) {
		  if(dir == MoveDirection.North) {
			  int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow()-1;
			  if(newRow < 1) {
				  return false;
			  }
		}
		  else if(dir == MoveDirection.South) {
			  int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow()+1;
			  if(newRow > 9) {
				  return false;
			  }
		  }
		  else if(dir == MoveDirection.East) {
			  int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn()+1;
			  if(newColumn > 9) {
				  return false;
			  }
		  }
		  else if(dir == MoveDirection.West) {
			  int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn()-1;
			  if(newColumn < 1) {
				  return false;
			  }
		  }
		  for(int i = 0; i< currentGame.getCurrentPosition().getBlackWallsOnBoard().size();i++) {
			  Tile wallTile =currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove().getTargetTile();
			  Direction direction = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove().getWallDirection();
			  if(direction == Direction.Horizontal) {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  if((wallTile==originalBlackTile||wallTileNext==originalBlackTile)&&dir==MoveDirection.South) {
					  return false;
				  }
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  Tile wallTileNextBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileBelow==originalBlackTile||wallTileNextBelow==originalBlackTile)&&dir==MoveDirection.North) {
					  return false;
				  }
			  }
			  else {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  if((wallTile==originalBlackTile||wallTileBelow==originalBlackTile)&&dir==MoveDirection.East) {
					  return false;
				  }
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  Tile wallTileBelowNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileNext==originalBlackTile||wallTileBelowNext==originalBlackTile)&&dir==MoveDirection.West) {
					  return false;
				  }
			  }
		  }
		  for(int i = 0; i< currentGame.getCurrentPosition().getWhiteWallsOnBoard().size();i++) {
			  Tile wallTile =currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove().getTargetTile();
			  Direction direction = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove().getWallDirection();
			  if(direction == Direction.Horizontal) {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  if((wallTile==originalBlackTile||wallTileNext==originalBlackTile)&&dir==MoveDirection.South) {
					  return false;
				  }
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  Tile wallTileNextBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileBelow==originalBlackTile||wallTileNextBelow==originalBlackTile)&&dir==MoveDirection.North) {
					  return false;
				  }
			  }
			  else {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  if((wallTile==originalBlackTile||wallTileBelow==originalBlackTile)&&dir==MoveDirection.East) {
					  return false;
				  }
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  Tile wallTileBelowNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileNext==originalBlackTile||wallTileBelowNext==originalBlackTile)&&dir==MoveDirection.West) {
					  return false;
				  }
			  }
		  }
	  }
	  else {
		  if(dir == MoveDirection.North) {
			  int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow()-1;
			  if(newRow < 1) {
				  System.out.println("false because of north");
				  return false;
			  }

		  }
		  else if(dir == MoveDirection.South) {
			  int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow()+1;
			  if(newRow > 9 ) {
				  System.out.println("false because of south");
				  return false;
			  }
		  }
		  else if(dir == MoveDirection.East) {
			  int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn()+1;
			  if(newColumn > 9) {
				  System.out.println("false because of east");
				  return false;
			  }
		  }
		  else if(dir == MoveDirection.West) {
			  int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn()-1;
			  if(newColumn < 1) {
				  System.out.println("false because of west");
				  return false;
			  }
		  }
		  for(int i = 0; i< currentGame.getCurrentPosition().getBlackWallsOnBoard().size();i++) {
			  Tile wallTile =currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove().getTargetTile();
			  Direction direction = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove().getWallDirection();
			  if(direction == Direction.Horizontal) {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  if((wallTile==originalWhiteTile||wallTileNext==originalWhiteTile)&&dir==MoveDirection.South) {
					  return false;
				  }
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  Tile wallTileNextBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileBelow==originalWhiteTile||wallTileNextBelow==originalWhiteTile)&&dir==MoveDirection.North) {
					  return false;
				  }
			  }
			  else {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  if((wallTile==originalWhiteTile||wallTileBelow==originalWhiteTile)&&dir==MoveDirection.East) {
					  return false;
				  }
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  Tile wallTileBelowNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileNext==originalWhiteTile||wallTileBelowNext==originalWhiteTile)&&dir==MoveDirection.West) {
					  return false;
				  }
			  }
		  }
		  for(int i = 0; i< currentGame.getCurrentPosition().getWhiteWallsOnBoard().size();i++) {
			  Tile wallTile =currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove().getTargetTile();
			  Direction direction = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove().getWallDirection();
			  if(direction == Direction.Horizontal) {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  if((wallTile==originalWhiteTile||wallTileNext==originalWhiteTile)&&dir==MoveDirection.South) {
					  return false;
				  }
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  Tile wallTileNextBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  if((wallTileBelow==originalWhiteTile||wallTileNextBelow==originalWhiteTile)&&dir==MoveDirection.North) {
					  return false;
				  }
			  }
			  else {
				  int wallTileColumn = wallTile.getColumn();
				  int wallTileRow = wallTile.getRow();
				  Tile wallTileBelow = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn-1);
				  if((wallTile==originalWhiteTile||wallTileBelow==originalWhiteTile)&&dir==MoveDirection.East) {
					  return false;
				  }
				  Tile wallTileNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow-1)*9+wallTileColumn);
				  Tile wallTileBelowNext = currentGame.getQuoridor().getBoard().getTile((wallTileRow)*9+wallTileColumn);
				  System.out.print("this is to clear wherthet");
				  if((wallTileNext.equals(originalWhiteTile)||wallTileBelowNext.equals(originalWhiteTile))&&dir==MoveDirection.West) {
					  return false;
				  }
			  }
		  }
	  }
	  
	  
	  return true;
  }


  /**
   * Returns if it is legal to jump in the given direction
   */
  // line 38 "../../../../../PawnStateMachine.ump"
  public boolean isLegalJump(MoveDirection dir){
    return false;
  }


  /**
   * Action to be called when an illegal move is attempted
   */
  // line 41 "../../../../../PawnStateMachine.ump"
  public void illegalMove(){
	  
    
  }

  // line 47 "../../../../../PawnStateMachine.ump"
  public boolean isAdjacentOpponent(){
    return false;
  }

  // line 49 "../../../../../PawnStateMachine.ump"
  public boolean isAdjacentWall(){
    return false;
  }

  // line 51 "../../../../../PawnStateMachine.ump"
  public boolean isWallBehindOpponent(){
    return false;
  }

  // line 53 "../../../../../PawnStateMachine.ump"
  public static boolean movePawn(MoveDirection dir){
	  if(isLegalStep(dir)) {
		  if(currentGame.getCurrentPosition().getPlayerToMove().equals(currentGame.getBlackPlayer())) {
			  if(dir == MoveDirection.North) {
				  int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow()-1;
				  int column = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow-1)*9+column-1);
				  currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getWhitePlayer());
				  return true;
			  }
			  else if(dir == MoveDirection.South) {
				  int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow()+1;
				  int column = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow-1)*9+column-1);
				  currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getWhitePlayer());
				  return true;
			  }
			  else if(dir == MoveDirection.East) {
				  int Row = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
				  int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn()+1;
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row-1)*9+newColumn-1);
				  currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getWhitePlayer());
				  return true;
			  }
			  else if(dir == MoveDirection.West) {
				  int Row = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
				  int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn()-1;
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row-1)*9+newColumn-1);
				  currentGame.getCurrentPosition().getBlackPosition().setTile(newTile); 
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getWhitePlayer());
				  return true;
			  }
		  }
		  else {
			  if(dir == MoveDirection.North) {
				  int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow()-1;
				  int column = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow-1)*9+column-1);
				  currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getBlackPlayer());

				  return true;
			  }
			  else if(dir == MoveDirection.South) {
				  int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow()+1;
				  int column = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow-1)*9+column-1);
				  currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getBlackPlayer());
				  return true;
			  }
			  else if(dir == MoveDirection.East) {
				  int Row = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
				  int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn()+1;
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row-1)*9+newColumn-1);
				  currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getBlackPlayer());
				  return true;
			  }
			  else if(dir == MoveDirection.West) {
				  int Row = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
				  int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn()-1;
				  Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row-1)*9+newColumn-1);
				  currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
				  currentGame.getCurrentPosition().setPlayerToMove(currentGame.getBlackPlayer());
				  return true;
			  }
		  }
		  return true;
	  }
	  else {
		  return false;
	  }
  }

  // line 55 "../../../../../PawnStateMachine.ump"
  public boolean jumpPawn(MoveDirection dir){
    return false;
  }

}