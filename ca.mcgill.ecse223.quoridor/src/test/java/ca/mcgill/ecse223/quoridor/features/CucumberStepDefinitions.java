package ca.mcgill.ecse223.quoridor.features;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
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

public class CucumberStepDefinitions {

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
    Controller.moveWall(wallMoveCandidate,side);
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
public void i_release_the_wall_in_my_hand(Wall aWall) {
    Controller.dropWall(aWall);
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

 //_-------_______-----__---__----___-----__-__-_-__-____-_-_--_-__-_-_--_-__-_--_----------__________________--------__
//Move wall

/*
	@When("I try to move the wall {string}")
	public void iTryToMoveTheWallSide(String side) {
		Game game = ModelQuery.getCurrentGame();
		WallMove move = game.getWallMoveCandidate();
		try {
			WallController.shiftWall(side, move);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}


	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand() {
		WallMove move = ModelQuery.getWallMoveCandidate();
		try{
			WallController.dropWall(move);
		} catch (UnsupportedOperationException e) {
			throw new PendingException();
		}
	}

	@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
	public void aWallMoveIsRegisteredWithDirAtPositionRowCol(String direction, int row, int col) {
		Game game = ModelQuery.getCurrentGame();
		Direction dir = this.stringToDirection(direction);
		int move_size = game.getMoves().size();

//		Check if at least one move has been registered
		assertTrue( move_size > 0);
		Move move = game.getMoves().get(move_size-1);

//		Check if the most recent move was a wall move
		assert move instanceof WallMove;
		WallMove wall_move = (WallMove) move;

//		Verify that the wall move is the same as the one just played
		assertEquals(wall_move.getWallDirection(),dir);
		assertEquals(wall_move.getTargetTile().getColumn(), col);
		assertEquals(wall_move.getTargetTile().getRow(), row);
	}
	*/
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


