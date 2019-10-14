package ca.mcgill.ecse223.quoridor.features;



import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.controller.QuoridorControllerImplementation;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import ca.mcgill.ecse223.quoridor.controller.Controller;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class CucumberStepDefinitions {
	
	private QuoridorControllerImplementation controller = new QuoridorControllerImplementation();

	private QuoridorControllerImplementation quoridorController = new QuoridorControllerImplementation();
	private boolean validationResult;
	// ***********************************************
	// Background step definitions
	// ***********************************************


	
	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndStartGame(createUsersAndPlayers);
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
		
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}
	
	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}

	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */
//Quoridor Class
	private Quoridor quoridor = QuoridorApplication.getQuoridor();
	// Current Game Object
	private Game currentGame = quoridor.getCurrentGame();
		
	//Parameters required for WallMove Object
	//Parameters for running game --ignore for now
	/*
	private int moveNum = quoridor.getCurrentGame().numberOfMoves() + 1;
	private int roundNum = (moveNum + 1) / 2 ;
	*/
	
	//-----------------------------------------------------------------------------//
	//Feature 1 - StartNewGame - Implemented by Ali Tapan - 260556540
	//-----------------------------------------------------------------------------//
		
	 /**
	  * 
	  * @author Ali Tapan
	  */
	  @When("A new game is being initialized")
	  public void aNewGameIsBeingInitialized() throws Throwable { 
		  quoridorController.StartNewGame();
		  
	  }
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("White player chooses a username")
	 public void whitePlayerChoosesAUsername() throws Throwable {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 Player player = quoridor.getCurrentGame().getWhitePlayer();
		 quoridorController.provideOrSelectUserName(player.getUser());
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("Black player chooses a username")
	 public void blackPlayerChoosesAUsername() throws UnsupportedOperationException {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 Player player = quoridor.getCurrentGame().getBlackPlayer();
		 quoridorController.provideOrSelectUserName(player.getUser());
	 } 
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("Total thinking time is set")
	 public void totalThinkingTimeIsSet() throws UnsupportedOperationException {
		 quoridorController.setTotalThinkingTime();
	 } 
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Then("The game shall become ready to start")
	 public void theGameShallBecomeReadyToStart() {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 assertEquals(GameStatus.ReadyToStart, quoridor.getCurrentGame().getGameStatus());
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Given("The game is ready to start")
	 public void theGameIsReadyToStart() {
		 //this.initQuoridorAndBoard();
		 //this.createUsersAndPlayers("user_a", "user_b");
		 this.createAndReadyGame();
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @When("I start the clock")
	 public void iStartTheClock() throws Throwable {
		 quoridorController.startClock();
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Then("The game shall be running")
	 public void theGameShallbeRunning() {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 assertEquals(GameStatus.Running, quoridor.getCurrentGame().getGameStatus());
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("The board shall be initialized")
	 public void theBoardShallBeInitialized() {
		 // Check if the board has tiles, if it has tiles then the board is initialized
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 assertEquals(true, quoridor.getBoard().hasTiles());
	 }
	 
	//-----------------------------------------------------------------------------//
	//Feature 2 - ProvideSelectUserName - Implemented by Ali Tapan - 260556540
	//-----------------------------------------------------------------------------//
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Given("A new game is initializing")
	 public void aNewGameIsInitializing() {
		 this.initQuoridorAndBoard();
		 this.createUsersAndPlayers("player_white", "player_black");
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Given("Next player to set user name is {string}")
	 public void nextPlayerToSetUserNameIs(String color) {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 
		 if (color == "white")
		 {
			 Player player = quoridor.getCurrentGame().getWhitePlayer();
			 User user = player.getUser();
		 }
		 else if(color == "black")
		 {
			 Player player = quoridor.getCurrentGame().getBlackPlayer();
			 User user = player.getUser();
		 }
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("There is existing user {string}")
	 public void thereIsExistingUser(String username) {
		 assertEquals(true, User.hasWithName(username));
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @When("The player selects existing {string}")
	 public void thePlayerSelectsExisting(String username) throws Throwable {
		 quoridorController.selectExistingUsername(username);
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Then("The name of player {string} in the new game shall be {string}")
	 public void theNameOfPlayerInTheNewGameShallBe(String color, String username) {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 if(color == "black")
		 {
			 assertEquals(username, quoridor.getCurrentGame().getBlackPlayer().getUser().getName());
		 }
		 else
		 {
			 assertEquals(username, quoridor.getCurrentGame().getWhitePlayer().getUser().getName());
		 }
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("There is no existing user {string}")
	 public void thereIsNoExistingUser(String username) {
		 assertEquals(false, User.hasWithName(username));
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @When("The player provides new user name: {string}")
	 public void thePlayerProvidesNewUserName(String username) throws Throwable {
		 quoridorController.provideNewUsername(username);
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @Then("The player shall be warned that {string} already exists")
	 public void thePlayerShallBeWarnedThatAlreadyExists(String color, String username) {
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 quoridor.notifyAll();
	 }
	 
	 /**
	  * 
	  * @author Ali Tapan
	  */
	 @And("Next player to set user name shall be {string}")
	 public void nextPlayerToSetUserNameShallBe(String color){
		 Quoridor quoridor = QuoridorApplication.getQuoridor();
		 if(color == "black")
		 {
			 Player nextPlayer = quoridor.getCurrentGame().getBlackPlayer().getNextPlayer();
		 }
		 else
		 {
			 Player nextPlayer = quoridor.getCurrentGame().getWhitePlayer().getNextPlayer();
		 }
	 }
	 
	//------------------------------3-4----------------------------------------------//


    /** @author Sam Perreault */
    @When("{int}:{int} is set as the thinking time")
    public void minSecIsSetAsTheThinkingTime(int min, int sec) {
        quoridorController.setPlayerThinkingTime(min, sec);
    }

    /** @author Sam Perreault */
    @Then("Both players shall have {int}:{int} remaining time left")
    public void BothPlayersShallHaveMinSecRemainingTimeLeft(int min, int sec) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        long timeRem = min*60*1000+sec*1000;
        assertEquals(timeRem, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime());
        assertEquals(timeRem, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime());
    }

    /** @author Sam Perreault */
    @When("The initialization of the board is initiated")
    public void theInitializationOfTheBoardIsInitiated() {
        quoridorController.initializeBoard();
    }

    /** @author Sam Perreault */
    @Then("It shall be white player to move")
    public void itShallBeWhitePlayerToMove() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
    }

    /** @author Sam Perreault */
    @And("White's pawn shall be in its initial position")
    public void whitesPawnShallBeInItsInitialPosition() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), quoridor.getBoard().getTile(36)),
                quoridor.getCurrentGame().getCurrentPosition().getWhitePosition());
    }

    /** @author Sam Perreault */
    @And("Black's pawn shall be in its initial position")
    public void blackSPawnShallBeInItsInitialPosition() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), quoridor.getBoard().getTile(44)),
                quoridor.getCurrentGame().getCurrentPosition().getBlackPosition());
    }

    /** @author Sam Perreault */
    @And("All of White's walls shall be in stock")
    public void allOfWhiteSWallsShallBeInStock() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(10, quoridor.getCurrentGame().getWhitePlayer().getWalls().size());
    }

    /** @author Sam Perreault */
    @And("All of Black's walls shall be in stock")
    public void allOfBlackSWallsShallBeInStock() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(10, quoridor.getCurrentGame().getBlackPlayer().getWalls().size());
    }

    /** @author Sam Perreault */
    @And("White's clock shall be counting down")
    public void whiteSClockShallBeCountingDown() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        // Save start instant, compare with
        assertNotEquals((new Time(System.currentTimeMillis())),
                quoridor.getCurrentGame().getWhitePlayer().getRemainingTime());
    }

    /** @author Sam Perreault */
    @And("It shall be shown that this is White's turn")
    public void itShallBeShownThatThisIsWhiteSTurn() {
        // GUI method to be implemented later
	}
	
	//--------------------------------------------------5-6--------------------------------------------------
	//New game hard coded parameters
	private int moveNum = 1;
	private int roundNum = 1;
	//Current Player object
	private Player currentPlayer = currentGame.getWhitePlayer();
		
	//Gets this Board for the Tile
	Board currentBoard = quoridor.getBoard();
	Tile targetTile;
	WallMove wallMoveCandidate;
	int wallId = currentPlayer.numberOfWalls();
	Wall currentWall = currentPlayer.getWall(0);

	/** @author Luke Barber*/
	@Given("I have more walls on stock")
	public void iHaveMoreWallsOnStock() {
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().hasWhiteWallsInStock());
	}

	/** @author Luke Barber*/
	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition(){
		Direction direction = Direction.Horizontal;
		int initialRow = 0;
		int initialColumn = 0;
		wallMoveCandidate = createWallMoveCandidate(direction, initialRow, initialColumn);
		assertTrue(wallMoveCandidate!=null);
	}

	/** @author Luke Barber*/
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		int numberOfPlayerWalls = quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsOnBoard();
		assertEquals((currentPlayer.maximumNumberOfWalls() - currentPlayer.numberOfWalls()), numberOfPlayerWalls-1);
	}
	
	/** @author Luke Barber*/
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallOnStock() {
		assertFalse(quoridor.getCurrentGame().getCurrentPosition().hasWhiteWallsInStock());
	}

	/** @author Luke Barber and Arneet Kalra*/
	@Given("A wall move candidate exists with {String} at position ({int}, {int})")
	public void aWallMoveCandidateExistsWithAtPosition(String dir, int row, int column) {
		//Convert string into Direction type
		Direction direction = this.stringToDirection(dir);	
		//Create wallMoveCandidate using helper method below
		WallMove wallMoveCandidate = createWallMoveCandidate(direction,row,column);
		currentGame.setWallMoveCandidate(wallMoveCandidate);
	}

	/** @author Luke Barber*/
	@Then("The wall shall be rotated over the board to {String}")
	public void theWallShallBeRotatedOverTheBoardTo(String newdir, WallMove wall) {
		Direction newDirection = this.stringToDirection(newdir);
		assertEquals(newDirection, wall.getWallDirection());
	}
	/** @author Luke Barber and Arneet Kalra*/
	@And("A wall move candidate shall exist with {String} at position ({int}, {int})")
	public void aWallMoveCandidateShallExistWithAtPosition(String newdir, int row, int column) {
		Direction newDirection = this.stringToDirection(newdir);
		WallMove wallMoveCandidate = createWallMoveCandidate(newDirection,row,column);
		currentGame.setWallMoveCandidate(wallMoveCandidate);
		assertEquals(currentGame.getWallMoveCandidate().getWallDirection(), newDirection);
		assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getRow(), row);
		assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getColumn(), column);
	}	
		
	/** @author Luke Barber and Arneet Kalra */
	//Method to convert String input data type into respective Direction type
	private Direction stringToDirection(String direction){
		if (direction == "horizontal") {
			return Direction.Horizontal;
		}
		else if (direction == "vertical") {
			return Direction.Vertical;
		}
		else return null;
	}
	
		/** @author Luke Barber and Arneet Kalra */
	//Method that makes WallMove Candidate from the given 3 parameters
	private WallMove createWallMoveCandidate(Direction dir,int row, int col) {
		Tile targetTile = new Tile(row, col, currentBoard);	
		WallMove wallMoveCandidate = new WallMove(moveNum, roundNum, currentPlayer, targetTile, currentGame, dir, currentWall);
		return wallMoveCandidate;
	}
	
	// ***********************************************
	// M O V E  W A L L  F E A T U R E (7) 
	// ***********************************************
	
	/**
	 * @author arneetkalra
	 */
	
	
//Global Variables for the features
	//Quoridor Class
	private Quoridor quoridor = QuoridorApplication.getQuoridor();
	// Current Game Object
	private Game currentGame = quoridor.getCurrentGame();
	
	//Parameters required for WallMove Object
	//Parameters for running game --ignore for now
	/*
	private int moveNum = quoridor.getCurrentGame().numberOfMoves() + 1;
	private int roundNum = (moveNum + 1) / 2 ;
	*/
	
	//New game hard coded parameters
	private int moveNum = 1;
	private int roundNum = 1;
	//Current Player object
	private Player currentPlayer = currentGame.getWhitePlayer();

	
	//Gets this Board for the Tile
	Board currentBoard = quoridor.getBoard();
	Tile targetTile;
	WallMove wallMoveCandidate;
	int wallId = currentPlayer.numberOfWalls();
	Wall currentWall = currentPlayer.getWall(0);

	/**
	 * @author arneetkalra
	 */
	
@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
public void a_wall_move_candidate_exists_with_at_position(String dir, int row, int col) {
	//Convert string into Direction type
	Direction direction = this.stringToDirection(dir);	
	
	//Create wallMoveCandidate using helper method below
	WallMove wallMoveCandidate = createWallMoveCandidate(direction,row,col);
	currentGame.setWallMoveCandidate(wallMoveCandidate);
}

/**
 * @author arneetkalra
 */

@And("The wall candidate is not at the {string} edge of the board")
public void the_wall_candidate_is_not_at_the_edge_of_the_board(String side) {
	if (side == "left") {
		assertFalse (currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 1);
	} 
	if (side == "right") {
		assertFalse (currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 9);
	} 
	if (side == "up") {
		assertFalse (currentGame.getWallMoveCandidate().getTargetTile().getRow() == 1);
	} 
	else if (side == "down") {
		assertFalse (currentGame.getWallMoveCandidate().getTargetTile().getRow() == 9);
	}
}

/**
 * @author arneetkalra
 */
@When("I try to move the wall {string}")
public void i_try_to_move_the_wall(String side) {    
    controller.moveWall(wallMoveCandidate, side);
}

/**
 * @author arneetkalra
 */
@Then("The wall shall be moved over the board to position \\({int}, {int})")
public void the_wall_shall_be_moved_over_the_board_to_position(int nrow, int ncol) {
	//Verify row condition
	assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getRow(),
			nrow);
	
	//Verify Column condition
	assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getColumn(),
			ncol);
}

/**
 * @author arneetkalra
 */
@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
public void a_wall_move_candidate_shall_exist_with_at_position(String dir, int nrow, int ncol) {
	
	Direction direction = this.stringToDirection(dir);	
	
		//Checks to see if direction is correct
		assertEquals(
				currentGame.getWallMoveCandidate().getWallDirection(),
				direction);
		
		//Checks to see if row is correct
		assertEquals(
				currentGame.getWallMoveCandidate().getTargetTile().getRow(),
				nrow);
				
		//Checks to see if column is correct			
		assertEquals(
				currentGame.getWallMoveCandidate().getTargetTile().getColumn(),
				ncol);	
}

/**
 * @author arneetkalra
 */
@And("The wall candidate is at the {string} edge of the board")
public void the_wall_candidate_is_at_the_edge_of_the_board(String side) {
	if (side == "left") {
		assertTrue (currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 1);
	} 
	if (side == "right") {
		assertTrue (currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 9);
	} 
	if (side == "up") {
		assertTrue (currentGame.getWallMoveCandidate().getTargetTile().getRow() == 1);
	} 
	else if (side == "down") {
		assertTrue (currentGame.getWallMoveCandidate().getTargetTile().getRow() == 9);
	}
}

/**
 * @author arneetkalra
 */
@Then("I shall be notified that my move is illegal")
public void i_shall_be_notified_that_my_move_is_illegal() {
   
	throw new cucumber.api.PendingException();
    
	// GUI-related feature -- TODO for later

}

	//***********************************************
	// D R O P  W A L L  F E A T U R E
	// ***********************************************

/**
 * @author arneetkalra
 */
@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
public void the_wall_move_candidate_with_at_position_is_valid(String dir, int row, int col) {
	
	//Checks to see if any walls are on the board
	boolean anyWhiteWallsOnBoard = currentGame.getCurrentPosition().getWhiteWallsOnBoard().isEmpty();
	boolean anyBlackWallsOnBoard = currentGame.getCurrentPosition().getBlackWallsOnBoard().isEmpty();
	
	boolean noWallsOnBoard = (anyWhiteWallsOnBoard && anyBlackWallsOnBoard); 
	
	if (noWallsOnBoard) {
		//Then there are no walls on the board so position will be valid, thus make the object 
		Direction direction = this.stringToDirection(dir);	
		
		//Create wallMoveCandidate using helper method below
		WallMove wallMoveCandidate = createWallMoveCandidate(direction,row,col);
		currentGame.setWallMoveCandidate(wallMoveCandidate);	
	}
	
	/*
	Tile targetTile = wallMoveCandidate.getTargetTile();
	int targetRow = targetTile.getRow();
	int targetCol = targetTile.getColumn();
	*/
}
    	
/**
 * @author arneetkalra
 */
@When("I release the wall in my hand")
public void i_release_the_wall_in_my_hand(Wall aWall) throws Throwable {
    controller.dropWall(wallMoveCandidate);
}

/**
 * @author arneetkalra
 */
@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
public void a_wall_move_shall_be_registered_with_at_position(String dir, int row, int col) {
	
	//Verify properties of vallMoveCandidate
	assertEquals(wallMoveCandidate.getWallDirection(), dir);
	assertEquals(wallMoveCandidate.getTargetTile().getRow(), row);
	assertEquals(wallMoveCandidate.getTargetTile().getColumn(), col);
	
	//Verifies owner of wall placed from wall move was white player 
	assertEquals(wallMoveCandidate.getWallPlaced().getOwner(),currentPlayer);
	
	// Verify number of moves in game increase to 1 
	List<Move> numberOfMoves = currentGame.getMoves();
	assertEquals(numberOfMoves.size(), 1);
}

/**
 * @author arneetkalra
 */
@And("I shall not have a wall in my hand")
public void i_shall_not_have_a_wall_in_my_hand() {
    
	throw new cucumber.api.PendingException();
    
	// GUI-related feature -- TODO for later

}

/**
 * @author arneetkalra
 */
@Then("My move shall be completed")
public void my_move_shall_be_completed() {	
	// Make sure moveWallCandidate does not exist anymore
	assertNull(currentGame.getWallMoveCandidate());

	// Number of white walls on the board increases to 1 
	assertEquals(currentGame.getCurrentPosition().numberOfWhiteWallsOnBoard(), 1);

	// Number of white walls in stock decreases to 9
	assertEquals(currentGame.getCurrentPosition().numberOfWhiteWallsInStock(), 9);
}

/**
 * @author arneetkalra
 */
@Then("It shall not be my turn to move")
public void it_shall_not_be_my_turn_to_move() {
	// Verifies game got updated so that last move was made by White Player
	assertEquals(currentGame.getMove(moveNum + 1).getPrevMove().getPlayer(), currentPlayer);
		
	// Verifies that current move is supposed to be Black Player not White
	assertEquals(currentGame.getMove(moveNum + 1).getPlayer(), currentGame.getBlackPlayer());
}

/**
 * @author arneetkalra
 */
@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
public void the_wall_move_candidate_with_at_position_is_invalid(String dir, int row, int col) {
    
	//Checks to see if any walls are on the board
		boolean anyWhiteWallsOnBoard = currentGame.getCurrentPosition().getWhiteWallsOnBoard().isEmpty();
		boolean anyBlackWallsOnBoard = currentGame.getCurrentPosition().getBlackWallsOnBoard().isEmpty();
		assertFalse(anyWhiteWallsOnBoard || anyBlackWallsOnBoard);
			
		Direction direction = this.stringToDirection(dir);	
				
		//Create wallMoveCandidate using helper method below
		WallMove wallMoveCandidate = createWallMoveCandidate(direction,row,col);
		currentGame.setWallMoveCandidate(wallMoveCandidate);

}

/**
 * @author arneetkalra
 */
@Then("I shall be notified that my wall move is invalid")
public void i_shall_be_notified_that_my_wall_move_is_invalid() {
   
	throw new cucumber.api.PendingException();
    
	// GUI-related feature -- TODO for later

}

/**
 * @author arneetkalra
 */
@And("I shall have a wall in my hand over the board")
public void i_shall_have_a_wall_in_my_hand_over_the_board() {

	throw new cucumber.api.PendingException();
    
	// GUI-related feature -- TODO for later

}

/**
 * @author arneetkalra
 */
@And("It shall be my turn to move")
public void it_shall_be_my_turn_to_move() {		
	// Verifies that current move is supposed to be Black Player not White
	assertEquals(currentGame.getMove(moveNum).getPlayer(), currentPlayer);
	
	//Verifies that it is still first move so no previous Player exists
	assertNull(currentGame.getMove(moveNum).getPrevMove().getPlayer());
	
	

}

/**
 * @author arneetkalra
 */
@But("No wall move shall be registered with {string} at position \\({int}, {int})")
public void no_wall_move_shall_be_registered_with_at_position(String dir, int row, int col) {
    //Only need to verify that no moves exist, wall move position/direction unnecessary
		List<Move> allMoves = currentGame.getMoves();
		assertEquals(allMoves.size(), 0);
	}

	//-----------------------------9-10---------------------------
	/**
	 * Load the game from the file
	 * @author Yin Zhang 260726999
	 * @param fileName
	 */
	@When("I initiate to load a saved game {string}")
	public void iInitiateToLoadASavedGame(String fileName) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		quoridor=quoridorController.loadPosition(quoridor,fileName);
	}
	
	/**
	 * Checks whether the position is valid or not
	 * @author Yin
	 */
	@And("^The position to load is valid$")
	public void thePositionIsValid() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame=quoridor.getCurrentGame();
		GamePosition gamePosition= currentGame.getCurrentPosition();
		validationResult = quoridorController.validatePosition(gamePosition);
	}
	
	/**
	 * checks whether the playerToMove is the same as expected
	 * @author Yin Zhang 260726999
	 * @param  playerToMove
	 */
	@Then("It is {string} turn")
	public void itIsPlayersTurn(String playerToMove){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame=quoridor.getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		Player playerToMove1 = currentGamePosition.getPlayerToMove();
		assertEquals(playerToMove, playerToMove1.getUser().getName());
	}
	
	/**
	 * Checks whether the player/opponent is at the right position 
	 * @author Yin Zhang 260726999
	 * @param player
	 * @param row
	 * @param column
	 */
	@And("{string} is at {int}:{int}")
	public void playerIsAt(String player, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame=quoridor.getCurrentGame();
		if (player.equals("player")) {
			GamePosition currentGamePosition = currentGame.getCurrentPosition();
			assertEquals(row, currentGamePosition.getBlackPosition().getTile().getRow());
			assertEquals(column,currentGamePosition.getBlackPosition().getTile().getColumn());
		}
		if(player.equals("opponent")) {
			GamePosition currentGamePosition = currentGame.getCurrentPosition();
			assertEquals(row, currentGamePosition.getWhitePosition().getTile().getRow());
			assertEquals(column,currentGamePosition.getWhitePosition().getTile().getColumn());
		}
	}
	
	/**
	 * Checks whether the wall of the player is in the right position
	 * @author Yin Zhang 260726999
	 * @param player
	 * @param direction
	 * @param row
	 * @param column
	 */
	@And("{string} has a {string} wall at {int}:{int}")
	public void playerHasAPwOWallAt(String player,String direction, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame=quoridor.getCurrentGame();
		if(player.equals("player")) {
		List<Wall> wallsPlayer = currentGame.getBlackPlayer().getWalls();
		assertEquals(wallsPlayer.get(0).getOwner().getUser().getName(),"black");
		assertEquals(wallsPlayer.get(0).getMove().getWallDirection().toString(),direction);
		assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getRow(),row);
		assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getColumn(),column);
		}
		if(player.equals("opponent")) {
			List<Wall> wallsOpponent = currentGame.getWhitePlayer().getWalls();
			assertEquals(wallsOpponent.get(0).getOwner().getUser().getName(),"white");
			assertEquals(wallsOpponent.get(0).getMove().getWallDirection().toString(),direction);
			assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getRow(),row);
			assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getColumn(),column);
		}
	}
	
	/**
	 * Checks whether the number of the wall in the list is the right number
	 * @author Yin Zhang 260726999
	 * @param number
	 */
	@And("Both players have {int} in their stacks")
	public void bothPlayersHaveRemainingWallsInTheirStacks(int number) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame=quoridor.getCurrentGame();
		assertEquals(currentGame.getBlackPlayer().getWalls().size(),number);
		assertEquals(currentGame.getWhitePlayer().getWalls().size(),number);
	}
	
	
	//SavePosition
	/**
	 * Checks whether the file is in the system or not
	 * @author Yin Zhang 260726999
	 * @param fileName
	 */
	@Given("No file {string} exists in the filesystem")
  	public void noFileExistsInTheFilesystem(String fileName) {
  		File f = new File(fileName);
  		if(!f.exists()) {
  			throw new IllegalArgumentException("File name doesn't exist");
  		}
  	}
	/**
	 * save the game into the file with the fileName
	 * @param fileName
	 * @author Yin
	 *
	 * */
  	@When("The user initiates to save the game with name {string}")
  	public void theUserInitiatesToSaveTheGameWithName(String fileName) {
  		Quoridor quoridor = QuoridorApplication.getQuoridor();
  		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
  		quoridorController.savePosition(fileName,gamePosition);
  	}
	/**
	 * @param fileName
	 * @author Yin Zhang 260726999
	 *
	 * */
  	@Then("A file with {string} is created in the filesystem")
  	public void aFileWithIsCreatedInTheFilesystem(String fileName) {
  		File f = new File(fileName);
  		assertTrue(f.exists());
  	}
	/**
	 * @param fileName
	 * @author Yin Zhang 260726999
	 *
	 * */
  	@Given("File {string} exists in the filesystem")
  	public void fileExistsInTheFileSystem(String fileName) {
  		File f = new File(fileName);
  		if(f.exists()) {
  			throw new IllegalArgumentException("File name exists");
  		}
  	}
  	/**
	 * @author Yin Zhang 260726999
  	 * The user confirm whether to overwrite the existing file
  	 * */
  	@And("The user confirms to overwrite existing file")
  	public void theUserConfirmsToOverwriteExistingFile() {
  		quoridorController.confirmsToOverWrite();
  	}
  	/**
	 * @author Yin Zhang 260726999
  	 * check whether the file is updated
  	 * */
  	@Then("File with {string} shall be updated in the filesystem")
  	public void fileWithNameShallBeUpdatedInTheFileSystem(String fileName){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Quoridor quoridor1 = new Quoridor();
		quoridor1 = quoridorController.loadPosition(quoridor1,fileName);
		int quoridorBlackPlayerRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridorBlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridor1BlackPlayerRow = quoridor1.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridor1BlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridorWhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridorWhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		int quoridor1WhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridor1WhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		assertFalse(quoridorBlackPlayerRow==quoridor1BlackPlayerRow);
		assertFalse(quoridorBlackPlayerColumn==quoridor1BlackPlayerColumn);
		assertFalse(quoridorWhitePlayerRow==quoridor1WhitePlayerRow);
		assertFalse(quoridorWhitePlayerColumn==quoridor1WhitePlayerColumn);
  	}
  	
  	/**
	 * @author Yin Zhang 260726999
  	 * check whether the file is updated
  	 * */
  	@Then("File with {string} shall not be changed in the filesystem")
  	public void fileWithNameShallNotBeChangedInTheFileSystem(String fileName){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Quoridor quoridor1 = new Quoridor();
		quoridor1 = quoridorController.loadPosition(quoridor1,fileName);
		int quoridorBlackPlayerRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridorBlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridor1BlackPlayerRow = quoridor1.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridor1BlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridorWhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridorWhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		int quoridor1WhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridor1WhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		assertFalse(quoridorBlackPlayerRow == quoridor1BlackPlayerRow);
		assertFalse(quoridorBlackPlayerColumn== quoridor1BlackPlayerColumn);
		assertFalse(quoridorWhitePlayerRow==quoridor1WhitePlayerRow);
		assertFalse(quoridorWhitePlayerColumn==quoridor1WhitePlayerColumn);
  	}
  

	//-------------11-12-------------------------
  	/**
  	 * feature 11
	 * @author William Wang
  	 * */
	  @Given("A game position is supplied with pawn coordinate {int}:{int}")
	  public void gamePositionWithPawnCoordinate(int row,int column) {
		  
		  initQuoridorAndBoard();
		  ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		  createAndStartGame(createUsersAndPlayers);
		  
		  Quoridor quoridor = QuoridorApplication.getQuoridor();
  
		  GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		  Tile playerCurrentPosition = quoridor.getBoard().getTile(row+9*column);
		  gamePosition.getWhitePosition().setTile(playerCurrentPosition);
	  }
	  
	  /**
	  	 * feature 11
		 * @author William Wang
	  	 * */
	  @When("validation of the position is initiated")
	  public void validationOfPositionIsInitialted() {
		  Quoridor quoridor = QuoridorApplication.getQuoridor();
		  validationResult = quoridorController.validatePosition(quoridor.getCurrentGame().getCurrentPosition());
	  }
	  
	  /**
	  	 * feature 11
		 * @author William Wang
	  	 * */
	  @Then("The position is {string}")
	  public void thePositionIs(String expectedResult) {
		  if(expectedResult.equals("OK")) {
			  assertTrue(validationResult);
		  }
		  else if(expectedResult.equals("error")){
			  assertFalse(validationResult);
		  }
	  }
	  
	  /**
	  	 * feature 11
		 * @author William Wang
	  	 * */
	  @Given("A game position is supplied with wall coordinate {int}:{int}-\"{string}")
	  public void gamePositionWithWallCoordinate(int row,int column, String dir) {
		  
		  initQuoridorAndBoard();
		  ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		  createAndStartGame(createUsersAndPlayers);
		  
		  Quoridor quoridor = QuoridorApplication.getQuoridor();

		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		Tile wallCoordinate = quoridor.getBoard().getTile(row+9*column);
		Wall wall = gamePosition.getWhiteWallsInStock(0);
		Direction direction = Direction.Vertical;
		if(dir.equals("horizontal")) {direction = Direction.Horizontal;}
		WallMove wallMove = new WallMove(1,1,quoridor.getCurrentGame().getWhitePlayer(),wallCoordinate,quoridor.getCurrentGame(),direction,wall);
		wall.setMove(wallMove);
		gamePosition.removeWhiteWallsOnBoard(wall);
		gamePosition.addWhiteWallsOnBoard(wall);
	}
	
	/**
	 * feature 12
	 * @author William Wang
	 * */
	@Given("The player to move is {string}")
	public void thePlayerToMoveIs(String player) {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("white", "black");
		createAndStartGame(createUsersAndPlayers);
		
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		if(player.equals("white")) {
			gamePosition.setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
		}
		else {
			gamePosition.setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
		}
	}
	
	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@And("The clock of {string} is running")
	public void theClockIsRunning(String player){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(player.equals("white")) {
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(180000));
		}
		else {
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(180000));
		}
		
	}
	
	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@And("The clock of {string} is stopped")
	public void theClockIsStopped(String player){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(player.equals("white")) {
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(0));
		}
		else {
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(0));
		}
		
	}
	
	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@When("Player {string} completes his move")
	public void playerCompletesMove(String player){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		quoridorController.switchCurrentPlayer(quoridor.getCurrentGame());
	}

	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@Then("The user interface shall be showing it is {string} turn")
	public void userInterfaceChange(String player) {
		throw new UnsupportedOperationException("GUI related");
	}
	
	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@Then("The clock of {string} shall be stopped")
	public void clockShallBeStopped(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(player.equals("white")) {
			assertTrue(quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime()==0);
		}
		else {
			assertTrue(quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime()==0);
		}
	}
	
	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@Then("The clock of {string} shall be running")
	public void clockShallBeRunning(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(player.equals("white")) {
			assertFalse(quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime()==0);
		}
		else {
			assertFalse(quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime()==0);
		}
	}

	/**
  	 * feature 12
	 * @author William Wang
  	 * */
	@And("The next player to move shall be {string} ")
	public void nextPlayToMoveShallBe(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(player.equals("white")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsWhite()!=null);
		}
		else {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack()!=null);
		}
	}
	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		//@formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);
		
		return playersList;
	}

	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
	}
	
	private Game createAndReadyGame() {
		
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		User user1 = quoridor.addUser("userWhite");
		User user2 = quoridor.addUser("userBlack");
		int thinkingTime = 180;
		
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);
		
		Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, player1, player2, quoridor);
		return game;
	}

	//Method to convert String input data type into respective Direction type
	private Direction stringToDirection(String direction){
		if (direction == "horizontal") {
			return Direction.Horizontal;
		}
		else if (direction == "vertical") {
			return Direction.Vertical;
		}
		else return null;
	}
	
	//Method that makes WallMove Candidate from the given 3 parameters
	private WallMove createWallMoveCandidate(Direction dir,int row, int col) {
		targetTile = new Tile(row, col, currentBoard);	
		WallMove wallMoveCandidate = new WallMove(moveNum, roundNum, currentPlayer, targetTile, currentGame, dir, currentWall);
		return wallMoveCandidate;
	}
}


