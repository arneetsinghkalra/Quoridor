/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import ca.mcgill.ecse223.quoridor.model.*;

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
  private Game currentGame;
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
  public boolean isLegalStep(MoveDirection dir){
    return false;
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
  public boolean movePawn(MoveDirection dir){
    return false;
  }

  // line 55 "../../../../../PawnStateMachine.ump"
  public boolean jumpPawn(MoveDirection dir){
    return false;
  }

}