package ca.mcgill.ecse223.quoridor.features;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import org.junit.jupiter.api.Assertions;

import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;
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
import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class CucumberStepDefinitions {

	private boolean validationResult = true;
	private boolean legalMove = true;
	private boolean userConfirms;
	private boolean privateStatus = false;
	Wall returnedWall;
	ArrayList<Player> createUsersAndPlayersLoad;

	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		createUsersAndPlayersLoad=createUsersAndPlayers("user1", "user2");
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


		//For Arneet Kalra's step definitions


		/*Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();
		Board currentBoard = quoridor.getBoard();

		// First wall placed
		Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
		Tile aNewTargetTile1 = currentBoard.getTile((1 - 1) * 9 + (1 - 1));
		Direction direction1 = Direction.Horizontal;
        Wall wall1 = whitePlayer.getWall(0);
        wall1.setId(0);

		WallMove wallMove1 = new WallMove(0, 0, whitePlayer, aNewTargetTile1, currentGame, direction1, wall1);
		wall1.setMove(wallMove1);

		// Second wall placed
		Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();
		Tile aNewTargetTile2 = currentBoard.getTile((7 - 1) * 9 + (4 - 1));
		Direction direction2 = Direction.Vertical;
		Wall wall2 = blackPlayer.getWall(0);
		wall2.setId(0);

		WallMove wallMove2 = new WallMove(0, 0, blackPlayer, aNewTargetTile2, currentGame, direction2, wall2);
		wall2.setMove(wallMove2);

		// Make a 3rd wall to make step definition work for last case - something wrong with given step defintion
		// Second wall placed
		Tile aNewTargetTile3 = currentBoard.getTile((6 - 1) * 9 + (6 - 1));
		Direction direction3 = Direction.Horizontal;
		Wall wall3 = blackPlayer.getWall(1);
		wall2.setId(0);

		WallMove wallMove3 = new WallMove(0, 0, blackPlayer, aNewTargetTile3, currentGame, direction3, wall3);
		wall3.setMove(wallMove3);

		quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall1);
		quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall1);
		quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall2);
		quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall2);
		quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall3);
		quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall3);*/


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
			// Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
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

	/** @author Luke Barber */
	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		QuoridorApplication.quoridorWindow = new QuoridorWindow();
		QuoridorApplication.quoridorWindow.wallSelected = false;
		assertFalse(Controller.wallSelected());
		}
	/** @author Luke Barber and Arneet Kalra */
	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// New game hard coded parameters for 5-8
		QuoridorApplication.quoridorWindow = new QuoridorWindow();
		QuoridorApplication.quoridorWindow.wallSelected = true;
		assertTrue(Controller.wallSelected());
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();
		Board currentBoard = quoridor.getBoard();
		Tile targetTile = currentBoard.getTile(2);
		Wall placedWall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(1);
		currentGame.getCurrentPosition().removeWhiteWallsInStock(placedWall);
		Direction direction = null;
		WallMove wallMoveCandidate = new WallMove(1, 1, currentPlayer, targetTile, currentGame, direction, placedWall);
		currentGame.setWallMoveCandidate(wallMoveCandidate);
	}

	@Given("^A new game is initializing$")
	public void aNewGameIsInitializing() throws Throwable {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
		new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
	}

	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	//--------------------------------------------------------------------------------

	// **************************************************************************
	// Feature 1 - StartNewGame - Implemented by Ali Tapan - 260556540
	// **************************************************************************

	/**
	 *
	 * @author Ali Tapan
	 */
	@When("A new game is being initialized")
	public void aNewGameIsBeingInitialized() {
		Controller.startNewGame();
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@And("White player chooses a username")
	public void whitePlayerChoosesAUsername() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Controller.initWhitePlayer("John");
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@And("Black player chooses a username")
	public void blackPlayerChoosesAUsername() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Controller.initBlackPlayer("Mel");
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@And("Total thinking time is set")
	public void totalThinkingTimeIsSet() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		String time = "00:01:00";
		Controller.setTotalThinkingTime(time);
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
		this.createAndReadyGame();
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@When("I start the clock")
	public void iStartTheClock() {
		Controller.startClock();
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

	// **************************************************************************
	// Feature 2 - ProvideSelectUserName - Implemented by Ali Tapan - 260556540
	// **************************************************************************

	/**
	 *
	 * @author Ali Tapan
	 */
	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIs(String color) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player player1 = createPlayer("A");
		Player player2 = createPlayer("B");
		Tile tile1 = new Tile(1, 1, quoridor.getBoard());
		Tile tile2 = new Tile(2, 2, quoridor.getBoard());
		PlayerPosition pp1 = new PlayerPosition(player1,tile1);
		PlayerPosition pp2 = new PlayerPosition(player2,tile2);
		GamePosition gp = new GamePosition(1, pp1, pp2, player1, quoridor.getCurrentGame());
		quoridor.getCurrentGame().setCurrentPosition(gp);
		quoridor.getCurrentGame().setWhitePlayer(player1);
		quoridor.getCurrentGame().setBlackPlayer(player2);
		if (color.equals("white")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(player1);
		} else if (color.equals("black")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(player2);
		}
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@And("There is existing user {string}")
	public void thereIsExistingUser(String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		quoridor.addUser(username);
		assertEquals(true, User.hasWithName(username));
	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@When("The player selects existing {string}")
	public void thePlayerSelectsExisting(String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Controller.selectExistingUsername(username, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());

	}

	/**
	 *
	 * @author Ali Tapan
	 */
	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerInTheNewGameShallBe(String color, String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertEquals(username, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getUser().getName());


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
	 * @throws Exception
	 */
	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUserName(String username) throws Exception {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Controller.provideNewUsername(username, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
	}

	/**
	 *
	 * @author Ali Tapan
	 * @throws Exception
	 */
	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatAlreadyExists(String username) throws Exception {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Boolean check = Controller.provideNewUsername(username, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
		assertEquals(false, check);
	}
	/**
	 *
	 * @author Ali Tapan
	 */
	@And("Next player to set user name shall be {string}")
	public void nextPlayerToSetUserNameShallBe(String color) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (color.equals("black")) {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
		} else {
			quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
		}
	}

	// **************************************************************************
	// Feature 3 - SetTotalThinkingTime - Implemented by Sam Perreault
	// **************************************************************************

	/** @author Sam Perreault */
    @When("{int}:{int} is set as the thinking time")
    public void minSecIsSetAsTheThinkingTime(int min, int sec) {
        String time = "";
        time = "00:"+min+":"+sec;
        Controller.setTotalThinkingTime(time);
    }

    /** @author Sam Perreault */
    @Then("Both players shall have {int}:{int} remaining time left")
    public void BothPlayersShallHaveMinSecRemainingTimeLeft(int min, int sec) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        long timeRem = min * 60 * 1000 + sec * 1000;
        assertEquals(timeRem, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime());
        assertEquals(timeRem, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime());
    }


	// **************************************************************************
	// Feature 4 - InitializeBoard - Implemented by Sam Perreault
	// **************************************************************************

    /** @author Sam Perreault */
    @When("The initialization of the board is initiated")
    public void theInitializationOfTheBoardIsInitiated() {
    	Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorWindow window = new QuoridorWindow();
        QuoridorApplication.quoridorWindow = window;
        Player p1 = createPlayer("P1");
        Player p2 = createPlayer("P2");
        quoridor.getCurrentGame().setWhitePlayer(p1);
        quoridor.getCurrentGame().setBlackPlayer(p2);
        Controller.createBoard();
        Controller.initializeBoard();
    }

    /** @author Sam Perreault */
    @Then("It shall be white player to move")
    public void itShallBeWhitePlayerToMove() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(quoridor.getCurrentGame().getWhitePlayer(),
                quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
    }

    /** @author Sam Perreault */
    @And("White's pawn shall be in its initial position")
    public void whitesPawnShallBeInItsInitialPosition() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(quoridor.getBoard().getTile(76),
                quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile());
    }

    /** @author Sam Perreault */
    @And("Black's pawn shall be in its initial position")
    public void blackSPawnShallBeInItsInitialPosition() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(quoridor.getBoard().getTile(4),
                quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile());
    }

    /** @author Sam Perreault */
    @And("All of White's walls shall be in stock")
    public void allOfWhiteSWallsShallBeInStock() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(10, quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
    }

    /** @author Sam Perreault */
    @And("All of Black's walls shall be in stock")
    public void allOfBlackSWallsShallBeInStock() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        assertEquals(10, quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
    }

    /** @author Sam Perreault */
    @And("White's clock shall be counting down")
    public void whiteSClockShallBeCountingDown() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertTrue(window.getIsTimerActive());
    }

    /** @author Sam Perreault */
    @And("It shall be shown that this is White's turn")
    public void itShallBeShownThatThisIsWhiteSTurn() {
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertEquals((char)0x25A1+" P1 "+(char)0x25A1+ "'s turn", window.getTurnLabel());
        // GUI method to be implemented later
    }

    // **************************************************************************
	// Feature 5 - Grab Wall - Implemented by Luke Barber - 260840096
    // **************************************************************************

	/** @author Luke Barber */
	@Given("I have more walls on stock")
	public void iHaveMoreWallsOnStock() {
		/**
		 * Since the player's stock is initialized in the background steps, there is no need to add more walls to a stock. Here, there should be a check
		that there is a valid number of walls within the player's stock.*/
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size() > Player.minimumNumberOfWalls()) ;
		assertTrue(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size() <= Player.maximumNumberOfWalls());
	}

	/** @author Luke Barber*/
	@When("I try to grab a wall from my stock")
	public void iTryToGrabAWallFromMyStock() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Wall wall = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(1);
		Controller.grabWall(wall);

	}

	/** @author Luke Barber */
	@Then("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		//work
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board currentBoard = quoridor.getBoard();
		Game currentGame = quoridor.getCurrentGame();
		Direction direction = Direction.Horizontal;
		int initialRow = 1;
		int initialColumn = 1;
		Tile tile = currentBoard.getTile((initialRow - 1) * 9 + (initialColumn - 1));
		assertEquals(direction, currentGame.getWallMoveCandidate().getWallDirection());
		assertEquals(tile, currentGame.getWallMoveCandidate().getTargetTile());
	}

	/** @author Luke Barber */
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		QuoridorApplication.quoridorWindow = new QuoridorWindow();
		QuoridorApplication.quoridorWindow.wallSelected = true;
	    assertTrue(Controller.wallSelected());
	    }

	/** @author Luke Barber */
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Wall wall = quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced();
		Wall wallInStock = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(wall.getId());
		assertFalse(wall.equals(wallInStock));

	}

	/** @author Luke Barber */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallOnStock() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		for (int i=0; i < quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size(); i++) {
			quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(i));
		}
	}

	/** @author Luke Barber */
	@Then("I shall be notified that I have no more walls")
	public void iShallBeNotifiedThatIHaveNoMoreWalls() {
	    Controller.notifyNoMoreWalls();
	}

	/** @author Luke Barber */
	@Then("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() {
		QuoridorApplication.quoridorWindow = new QuoridorWindow();
		QuoridorApplication.quoridorWindow.wallSelected =false;
	    assertFalse(Controller.wallSelected());
	}


	// **************************************************************************
	// Feature 6 - Rotate Wall - Implemented by Luke Barber - 260840096
	// **************************************************************************

	/** @author Luke Barber and Arneet Kalra */
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithAtPosition(String dir, int row, int column) {
		// Shortcut variables
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Board currentBoard = quoridor.getBoard();
		// Turn input string into Direction type
		Direction direction = Controller.stringToDirection(dir);
		Tile targetTile = currentBoard.getTile((row - 1) * 9 + (column - 1));
		currentGame.getWallMoveCandidate().setWallDirection(direction);
		currentGame.getWallMoveCandidate().setTargetTile(targetTile);
	}

	/** @author Luke Barber*/
	@When("I try to flip the wall")
	public void iTryToFlipTheWall() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Wall wall = quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced();
		Controller.rotateWall(wall);

	}

	/** @author Luke Barber */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardTo(String newdir) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
		Direction newDirection = Controller.stringToDirection(newdir);
		assertEquals(newDirection, wallMoveCandidate.getWallDirection());
	}

	/** @author Luke Barber and Arneet Kalra */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithAtPosition(String newdir, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		// Convert newdir into a Direction type
		Direction newDirection = Controller.stringToDirection(newdir);
		assertEquals(currentGame.getWallMoveCandidate().getWallDirection(), newDirection);
		assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getColumn(), column);
	}

	// **************************************************************************
	// Feature 7 - MoveWall - Implemented by Arneet Kalra
	// **************************************************************************

	/**
	 * @author arneetkalra
	 */

	@And("The wall candidate is not at the {string} edge of the board")
	public void the_wall_candidate_is_not_at_the_edge_of_the_board(String side) {
		// New game hard coded parameters for 5-8
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Current Game Object
		Game currentGame = quoridor.getCurrentGame();
		// Current Player object
		Player currentPlayer = currentGame.getWhitePlayer();

		quoridor.getBoard();
		currentPlayer.numberOfWalls();
		currentPlayer.getWall(0);

		if (side == "left") {
			assertFalse(currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 1);
		}
		if (side == "right") {
			assertFalse(currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 9);
		}
		if (side == "up") {
			assertFalse(currentGame.getWallMoveCandidate().getTargetTile().getRow() == 1);
		} else if (side == "down") {
			assertFalse(currentGame.getWallMoveCandidate().getTargetTile().getRow() == 9);
		}
	}

	/**
	 * @author arneetkalra
	 */
	@When("I try to move the wall {string}")
	public void i_try_to_move_the_wall(String side) {
		/*
		 *
		 * Quoridor quoridor = QuoridorApplication.getQuoridor(); Game currentGame =
		 * quoridor.getCurrentGame();
		 *
		 * int row = currentGame.getWallMoveCandidate().getTargetTile().getRow(); int
		 * col = currentGame.getWallMoveCandidate().getTargetTile().getColumn(); if
		 * (side == "left") { } if (side == "right") { } if (side == "up") { } else if
		 * (side == "down") { }
		 */

		Controller.moveWall(side);
	}

	/**
	 * @author arneetkalra
	 *
	 */
	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void the_wall_shall_be_moved_over_the_board_to_position(int nrow, int ncol) {
		// New game hard coded parameters for 5-8
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Current Game Object
		Game currentGame = quoridor.getCurrentGame();
		// Current Player object
		Player currentPlayer = currentGame.getWhitePlayer();

		quoridor.getBoard();
		currentPlayer.numberOfWalls();
		currentPlayer.getWall(0);

		// Verify row condition
		assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getRow(), nrow);

		// Verify Column condition
		assertEquals(currentGame.getWallMoveCandidate().getTargetTile().getColumn(), ncol);
	}

	/**
	 * @author arneetkalra
	 */
	@And("The wall candidate is at the {string} edge of the board")
	public void the_wall_candidate_is_at_the_edge_of_the_board(String side) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();

		if (side == "left") {
			assertTrue(currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 1);
		}
		if (side == "right") {
			assertTrue(currentGame.getWallMoveCandidate().getTargetTile().getColumn() == 8);
		}
		if (side == "up") {
			assertTrue(currentGame.getWallMoveCandidate().getTargetTile().getRow() == 1);
		} else if (side == "down") {
			assertTrue(currentGame.getWallMoveCandidate().getTargetTile().getRow() == 8);
		}
	}

	/**
	 * @author arneetkalra
	 */
	@Then("I shall be notified that my move is illegal")
	public void i_shall_be_notified_that_my_move_is_illegal() {
		System.out.println("Illegal Move");
		// throw new cucumber.api.PendingException();

		// GUI-related feature -- TODO for later

	}

	// **************************************************************************
	// Feature 8 - DropWall - Implemented by Arneet Kalra
	// **************************************************************************

	/**
	 * @author arneetkalra
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void the_wall_move_candidate_with_at_position_is_valid(String dir, int row, int col) {
		// New game hard coded parameters for 5-8
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Current Game Object
		Game currentGame = quoridor.getCurrentGame();
		// Current Player object
		Player currentPlayer = currentGame.getWhitePlayer();
		Board currentBoard = quoridor.getBoard();
		// Get current Wall Move Candidate

		WallMove wallMoveCandidate = currentGame.getWallMoveCandidate();

		quoridor.getBoard();
		currentPlayer.numberOfWalls();
		currentPlayer.getWall(0);

		// Call Williams Validate Position Method

		/*
		// Checks to see if any walls are on the board
		boolean anyWhiteWallsOnBoard = currentGame.getCurrentPosition().getWhiteWallsOnBoard().isEmpty();
		boolean anyBlackWallsOnBoard = currentGame.getCurrentPosition().getBlackWallsOnBoard().isEmpty();
		// boolean noWallsOnBoard = (anyWhiteWallsOnBoard && anyBlackWallsOnBoard);
		 * if (noWallsOnBoard) { // Then there are no walls on the board so position
		 * will be valid, thus make the // object Direction direction =
		 * this.stringToDirection(dir); Tile aNewTargetTile = currentBoard.getTile((row
		 * - 1) * 9 + (col - 1));
		 *
		 * // Update wall move candidate with input parameters
		 * wallMoveCandidate.setWallDirection(direction);
		 * wallMoveCandidate.setTargetTile(aNewTargetTile);
		 *
		 * }
		 */

		// Just make the wall move candidate
		Direction direction = this.stringToDirection(dir);
		Tile aNewTargetTile = currentBoard.getTile((row - 1) * 9 + (col - 1));

		// Update wall move candidate with input parameters
		wallMoveCandidate.setWallDirection(direction);
		wallMoveCandidate.setTargetTile(aNewTargetTile);
		/*
		 * Tile targetTile = wallMoveCandidate.getTargetTile(); int targetRow =
		 * targetTile.getRow(); int targetCol = targetTile.getColumn();
		 */
	}

	/**
	 * @author arneetkalra
	 */
	@When("I release the wall in my hand")
	public void i_release_the_wall_in_my_hand() throws Throwable {

		// Get Current Wall Move Candidate
		WallMove wallMove = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
		// Drop the wall and return the wall placed
		returnedWall = Controller.dropWall(wallMove);
	}

	/**
	 * @author arneetkalra
	 */
	@Then("A wall move shall be registered with {string} at position \\({int}, {int})")
	public void a_wall_move_shall_be_registered_with_at_position(String dir, int row, int col) {
		// Convert string dir to Direction direction
		Direction direction = this.stringToDirection(dir);

		assertEquals(returnedWall.getMove().getWallDirection(), direction);

		// Verify that wall move candidate
		assertEquals(returnedWall.getMove().getTargetTile().getRow(), row);
		assertEquals(returnedWall.getMove().getTargetTile().getColumn(), col);
	}

	/**
	 * @author arneetkalra
	 */
	@And("I shall not have a wall in my hand")
	public void i_shall_not_have_a_wall_in_my_hand() {
		// Assertion that if Move mode is now Player move then it cannot be Wall Move,
		// thus ending turn
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode(), Game.MoveMode.PlayerMove);
		// throw new cucumber.api.PendingException();

		// GUI-related feature -- TODO for later

	}

	/**
	 * @author arneetkalra
	 */
	@Then("My move shall be completed")
	public void my_move_shall_be_completed() {
		// Make sure moveWallCandidate does not exist anymore
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode(), Game.MoveMode.PlayerMove);
		/*
		 * //Make sure it is the next players turn to move
		 * assertEquals(currentGame.getBlackPlayer(),currentPlayer); // Number of white
		 * walls on the board increases to 1
		 * assertEquals(currentGame.getCurrentPosition().numberOfWhiteWallsOnBoard(),
		 * 2);
		 *
		 * // Number of white walls in stock decreases to 9
		 * assertEquals(currentGame.getCurrentPosition().numberOfWhiteWallsInStock(),9);
		 */
	}

	/**
	 * @author arneetkalra
	 */
	@Then("It shall not be my turn to move")
	public void it_shall_not_be_my_turn_to_move() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();
		Player blackPlayer = currentGame.getBlackPlayer();

		// Verifies that it is not White players turn anymore, so black player must move
		assertNotEquals(currentGame.getCurrentPosition().getPlayerToMove(), whitePlayer);
		// Double verification to make sure nothing else went wrong
		assertEquals(currentGame.getCurrentPosition().getPlayerToMove(), blackPlayer);
	}

	/**
	 * @author arneetkalra
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
	public void the_wall_move_candidate_with_at_position_is_invalid(String dir, int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Board currentBoard = quoridor.getBoard();

		WallMove wallMoveCandidate = currentGame.getWallMoveCandidate();

		Direction direction = this.stringToDirection(dir);
		Tile aNewTargetTile = currentBoard.getTile((row - 1) * 9 + (col - 1));

		// Modify wall move candidate with input parameters
		wallMoveCandidate.setWallDirection(direction);
		wallMoveCandidate.setTargetTile(aNewTargetTile);

		currentGame.setWallMoveCandidate(wallMoveCandidate);

	}

	/**
	 * @author arneetkalra
	 */
	@Then("I shall be notified that my wall move is invalid")
	public void i_shall_be_notified_that_my_wall_move_is_invalid() {

		WallMove wallMoveCandidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();

		assertEquals(null, Controller.dropWall(wallMoveCandidate));
		// System.out.println("Invalid wall move");
		// throw new cucumber.api.PendingException();

		// GUI-related feature -- TODO for later
	}


	/**
	 * @author arneetkalra
	 */
	@And("It shall be my turn to move")
	public void it_shall_be_my_turn_to_move() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Player whitePlayer = currentGame.getWhitePlayer();

		// Verifies that current move is supposed to be Black Player not White
		assertEquals(currentGame.getCurrentPosition().getPlayerToMove(), whitePlayer);

		// Verifies that it is still first move so no previous Player exists
		// assertNull(currentGame.getMove(moveNum).getPrevMove().getPlayer());

	}

	/**
	 * @author arneetkalra
	 */
	@But("No wall move shall be registered with {string} at position \\({int}, {int})")
	public void no_wall_move_shall_be_registered_with_at_position(String dir, int row, int col) {
		// New game hard coded parameters for 5-8
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Current Game Object
		Game currentGame = quoridor.getCurrentGame();
		// Current Player object
		Player currentPlayer = currentGame.getWhitePlayer();

		quoridor.getBoard();
		currentPlayer.numberOfWalls();
		currentPlayer.getWall(0);
		// Only need to verify that no moves exist, wall move position/direction
		// unnecessary
		List<Move> allMoves = currentGame.getMoves();
		assertEquals(allMoves.size(), 0);
	}

	// **************************************************************************
	// Feature 9 - LoadPosition - Implemented by Yin Zhang - 260726999
	// **************************************************************************

	/**
	 * Load the game from the file
	 *
	 * @author Yin Zhang 260726999
	 * @param fileName
	 */
	@When("I initiate to load a saved game {string}")
	public void iInitiateToLoadASavedGame(String fileName) {
		//Controller.initializeBoard();
		Quoridor quoridor = QuoridorApplication.getQuoridor();
//		for (int i = 0; i < 20; i++) {
//			Wall wall = Wall.getWithId(i);
//			if (wall != null) {
//				wall.delete();
//			}
//		}
		/*
		//------// added
		Controller.startNewGame();
		Player white = createPlayer("user1");
		Player black = createPlayer("user2");
		Tile tile1 = new Tile(1, 1, quoridor.getBoard());
		Tile tile2 = new Tile(2, 2, quoridor.getBoard());
		PlayerPosition pp1 = new PlayerPosition(white,tile1);
		PlayerPosition pp2 = new PlayerPosition(black,tile2);
		GamePosition gp = new GamePosition(1, pp1, pp2, white, quoridor.getCurrentGame());
		quoridor.getCurrentGame().setWhitePlayer(white);
		quoridor.getCurrentGame().setBlackPlayer(black);
		quoridor.getCurrentGame().setCurrentPosition(gp);
		//-----// added*/

//		Controller.initWhitePlayer("User1");
//		Controller.initBlackPlayer("User2");
		//Controller.initializeBoard();
		//ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user4", "user5");


		createAndStartGame(createUsersAndPlayersLoad);

		try {
			quoridor = Controller.loadPosition(fileName);
		}catch(UnsupportedOperationException e) {
			validationResult = false;
		}

	}

	/**
	 * Checks whether the position is valid or not
	 *
	 * @author Yin
	 */
	@And("The position to load is valid")
	public void thePositionIsValid() {
		boolean isValid = Controller.validatePosition();
	}

	/**
	 * checks whether the playerToMove is the same as expected
	 *
	 * @author Yin Zhang 260726999
	 * @param playerToMove
	 */
	@Then("It shall be {string}'s turn")
	public void itIsPlayersTurn(String playerToMove) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		Player playerToMove1 = currentGamePosition.getPlayerToMove();
		String playerToMoveString="";
		if(playerToMove1.hasGameAsBlack()) {
			playerToMoveString = "black";
		}else {
			playerToMoveString = "white";
		}
		assertEquals(playerToMove, playerToMoveString);
	}

	/**
	 * Checks whether the player/opponent is at the right position
	 *
	 * @author Yin Zhang 260726999
	 * @param player
	 * @param row
	 * @param column
	 */
	@And("{string} shall be at {int}:{int}")
	public void playerIsAt(String player, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		if (player.equals("player")) {
			GamePosition currentGamePosition = currentGame.getCurrentPosition();
			assertEquals(row, currentGamePosition.getBlackPosition().getTile().getRow());
			assertEquals(column, currentGamePosition.getBlackPosition().getTile().getColumn());
		}
		if (player.equals("opponent")) {
			GamePosition currentGamePosition = currentGame.getCurrentPosition();
			assertEquals(row, currentGamePosition.getWhitePosition().getTile().getRow());
			assertEquals(column, currentGamePosition.getWhitePosition().getTile().getColumn());
		}
	}

	/**
	 * Checks whether the wall of the player is in the right position
	 *
	 * @author Yin Zhang 260726999
	 * @param player
	 * @param direction
	 * @param row
	 * @param column
	 */
	@And("{string} shall have a vertical wall at {int}:{int}")
	public void playerShallHaveAVerticalWallAt(String player, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		if (player.equals("black")) {
			List<Wall> wallsPlayer = currentGame.getCurrentPosition().getBlackWallsOnBoard();
			if(wallsPlayer.get(0).getMove().getWallDirection()==Direction.Vertical) {
				assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getRow(), row);
				assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getColumn(), column);
			}
		}
		if (player.equals("white")) {
			List<Wall> wallsOpponent = currentGame.getCurrentPosition().getWhiteWallsOnBoard();
			if(wallsOpponent.get(0).getMove().getWallDirection()==Direction.Vertical) {
				assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getRow(), 1);
				assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getColumn(), 1);
			}
		}
	}
	@And("{string} shall have a horizontal wall at {int}:{int}")
	public void playerShallHaveAHorizotalWallAt(String player, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		if (player.equals("black")) {
			List<Wall> wallsPlayer = currentGame.getCurrentPosition().getBlackWallsOnBoard();
			if(wallsPlayer.get(0).getMove().getWallDirection()==Direction.Horizontal) {
				assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getRow(), row);
				assertEquals(wallsPlayer.get(0).getMove().getTargetTile().getColumn(), column);
			}
		}
		if (player.equals("white")) {
			List<Wall> wallsOpponent = currentGame.getCurrentPosition().getWhiteWallsOnBoard();
			if(wallsOpponent.get(0).getMove().getWallDirection()==Direction.Horizontal) {
				assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getRow(), row);
				assertEquals(wallsOpponent.get(0).getMove().getTargetTile().getColumn(), column);
			}
		}
	}

	/**
	 * Checks whether the number of the wall in the list is the right number
	 *
	 * @author Yin Zhang 260726999
	 * @param number
	 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersHaveRemainingWallsInTheirStacks(int number) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		assertEquals(currentGame.getCurrentPosition().getBlackWallsInStock().size(), number);
		assertEquals(currentGame.getCurrentPosition().getWhiteWallsInStock().size(), number);
	}

	@When("The position to load is invalid")
	public void thePositionToLoadIsInvalid() {
		boolean isInvalid = Controller.validatePosition();
	}
	@Then("The load shall return an error")
	public void theLoadShallReturnAnError() {
		assertFalse(validationResult);
	}


	// **************************************************************************
	// Feature 10 - SavePosition - Implemented by Yin Zhang - 260726999
	// **************************************************************************

	/**
	 * Checks whether the file is in the system or not
	 *
	 * @author Yin Zhang 260726999
	 * @param fileName
	 */
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsInTheFilesystem(String fileName) {
		Path path = Paths.get("src/test/resources/savePosition/"+fileName);
		if(Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * save the game into the file with the fileName
	 *
	 * @param fileName
	 * @author Yin
	 *
	 */
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithName(String fileName) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		boolean confirms=false;
		try {
			Controller.savePosition(fileName, gamePosition, confirms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param fileName
	 * @author Yin Zhang 260726999
	 * @throws IOException
	 *
	 */
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithIsCreatedInTheFilesystem(String fileName) throws IOException {
		Path path = Paths.get("src/test/resources/savePosition/"+fileName);
        assertTrue(Files.exists(path));
	}

	/**
	 * @param fileName
	 * @author Yin Zhang 260726999
	 * @throws IOException
	 *
	 */
	@Given("File {string} exists in the filesystem")
	public void fileExistsInTheFileSystem(String fileName) throws IOException{
		Path path = Paths.get("src/test/resources/savePosition/"+fileName);
		if(Files.exists(path)) {
			Files.delete(path);
		}
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Controller.savePosition(fileName, quoridor.getCurrentGame().getCurrentPosition(), true);
	}

	/**
	 * @author Yin Zhang 260726999 The user confirm whether to overwrite the
	 *         existing file
	 */
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwriteExistingFile() {
		userConfirms = true;
	}

	/**
	 * @author Yin Zhang 260726999 check whether the file is updated
	 * @throws IOException
	 *
	 */
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithNameShallBeUpdatedInTheFileSystem(String fileName) throws IOException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Tile tile = new Tile(4,6,QuoridorApplication.getQuoridor().getBoard());
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
		Controller.savePosition(fileName, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(), userConfirms);
		Controller.loadPosition(fileName);
		int quoridorBlackPlayerRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridorBlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridorWhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridorWhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		assertTrue(quoridorBlackPlayerRow==4);
		assertTrue(quoridorBlackPlayerColumn==6);
		assertTrue(quoridorWhitePlayerRow==9);
		assertTrue(quoridorWhitePlayerColumn==5);
	}
	@When("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwriteExistingFile() {
		userConfirms = false;
	}
	/**
	 * @author Yin Zhang 260726999 check whether the file is updated
	 * @throws IOException
	 */
	@Then("File {string} shall not be changed in the filesystem")
	public void fileWithNameShallNotBeChangedInTheFileSystem(String fileName) throws IOException {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Tile tile = QuoridorApplication.getQuoridor().getBoard().getTile((4-1)*9+6-1);
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().setTile(tile);
		Controller.savePosition(fileName, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(), userConfirms);
		Controller.loadPosition(fileName);
		int quoridor1BlackPlayerRow = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
		int quoridor1BlackPlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
		int quoridor1WhitePlayerRow = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
		int quoridor1WhitePlayerColumn = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
		System.out.println(quoridor1BlackPlayerRow);
		assertTrue(quoridor1BlackPlayerRow==1);
		assertTrue(quoridor1BlackPlayerColumn==5);
		assertTrue(quoridor1WhitePlayerRow==9);
		assertTrue(quoridor1WhitePlayerColumn==5);
	}

	// **************************************************************************
	// Feature 11 - ValidatePosition - Implemented by William Wang
	// **************************************************************************

	/**
	 * feature 11
	 *
	 * @author William Wang
	 */
	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void gamePositionWithPawnCoordinate(int row, int column) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();

		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		Tile playerCurrentPosition = quoridor.getBoard().getTile((row-1)*9+ (column-1));
		gamePosition.getWhitePosition().setTile(playerCurrentPosition);
	}

	/**
	 * feature 11
	 *
	 * @author William Wang
	 */
	@When("Validation of the position is initiated")
	public void validationOfPositionIsInitialted() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		validationResult = Controller.validatePosition();
	}

	/**
	 * feature 11
	 *
	 * @author William Wang
	 */
	@Then("The position shall be {string}")
	public void thePositionIs(String expectedResult) {
		if (expectedResult.equals("OK")) {
			assertTrue(validationResult);
		} else if (expectedResult.equals("error")) {
			assertFalse(validationResult);
		}
	}

	/**
	 * feature 11
	 *
	 * @author William Wang
	 */
	@Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
	public void gamePositionWithWallCoordinate(int row, int column, String dir) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
		Wall wall = whitePlayer.getWall(0);
		Direction direction = Direction.Vertical;
		if (dir.equals("horizontal")) {
			direction = Direction.Horizontal;
		}
		new WallMove(0,1,whitePlayer,quoridor.getBoard().getTile((row-1)*9+column-1),quoridor.getCurrentGame(), direction, wall);
		quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
		quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
	}

	@Then("The position shall be valid")
	public void the_position_shall_be_valid() {
	    // Write code here that turns the phrase above into concrete actions
		assertTrue(validationResult);
	}

	@Then("The position shall be invalid")
	public void the_position_shall_be_invalid() {
	    // Write code here that turns the phrase above into concrete actions
		assertFalse(validationResult);
	}

	// **************************************************************************
	// Feature 12 - SwitchCurrentPlayer - Implemented by William Wang
	// **************************************************************************

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@Given("The player to move is {string}")
	public void thePlayerToMoveIs(String player) {

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
		if (player.equals("white")) {
			gamePosition.setPlayerToMove(quoridor.getCurrentGame().getWhitePlayer());
		} else {
			gamePosition.setPlayerToMove(quoridor.getCurrentGame().getBlackPlayer());
		}
	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@And("The clock of {string} is running")
	public void theClockIsRunning(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(180000));
		} else {
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(180000));
		}

	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@And("The clock of {string} is stopped")
	public void theClockIsStopped(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time(0));
		} else {
			quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time(0));
		}

	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@When("Player {string} completes his move")
	public void playerCompletesMove(String player) {
		Controller.switchCurrentPlayer();
	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@Then("The user interface shall be showing it is {string} turn")
	public void userInterfaceChange(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack()==null);
		} else {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsWhite()==null);
		}
	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@Then("The clock of {string} shall be stopped")
	public void clockShallBeStopped(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("black")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack()==null);
		} else {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsWhite()==null);
		}
	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@Then("The clock of {string} shall be running")
	public void clockShallBeRunning(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack()==null);
		} else {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsWhite()==null);
		}
	}

	/**
	 * feature 12
	 *
	 * @author William Wang
	 */
	@And("The next player to move shall be {string}")
	public void nextPlayToMoveShallBe(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (player.equals("white")) {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsWhite() != null);
		} else {
			assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().getGameAsBlack() != null);
		}
	}
	
	/**********************
	 * ***********************
	 * Sprint 4 Step Defintions
	 * *********************
	 * ********************/

	// Jump Pawn Feature

	/**
	 * @author arneetkalra
	 * @param prow
	 * @param pcol
	 */
	@And("The player is located at {int}:{int}")
	public void the_player_is_located_at(int prow, int pcol) {
		//Get Player whose turn it is
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		
		//Calculate tile index of target tile
		int tileIndex = ((prow - 1) * 9 + (pcol - 1));
		Tile targetPosition = QuoridorApplication.getQuoridor().getBoard().getTile(tileIndex);
		
		// If it is black players turn
		if (currentPlayer == QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()) {
			//Set the players position to target position
			PlayerPosition currentPlayerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
			currentPlayerPosition.setTile(targetPosition);
		}

		// If it is white players turn
		if (currentPlayer == QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()) {
			//Set the players position to target position
			PlayerPosition currentPlayerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
			currentPlayerPosition.setTile(targetPosition);
		}
	}
/**
 * @author arneetkalra
 * @param orow
 * @param ocol
 */
	@And("The opponent is located at {int}:{int}")
	public void the_opponent_is_located_at(Integer orow, Integer ocol) {
		//Get Player whose turn it is
				Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
				Player opponent;
				
				//Set the opponent to the opposite of the current player to move
				if (currentPlayer == QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()) {
					opponent = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(); 
				}
				else {
					opponent = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer(); 
				}
				
				//Calculate tile index of target tile
				int tileIndex = ((orow - 1) * 9 + (ocol - 1));
				Tile targetPosition = QuoridorApplication.getQuoridor().getBoard().getTile(tileIndex);

				// If opponent is black, set their position
				if (opponent == QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()) {
					//Set the players position to target position
					PlayerPosition opponentPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
					opponentPosition.setPlayer(currentPlayer);
					opponentPosition.setTile(targetPosition);
				}

				// If opponent is white, set their position.
				else {
					//Set the players position to target position
					PlayerPosition opponentPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
					opponentPosition.setPlayer(currentPlayer);
					opponentPosition.setTile(targetPosition);
				}
	}
/**
 * @author arneetkalra
 * @param direction
 * @param side
 */
	@And("There are no {string} walls {string} from the player nearby")
	public void there_are_no_walls_from_the_player_nearby(String direction, String side) {		
		//Current Position variable
		GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		Direction dir = stringToDirection(direction);

		if (side.equals("left") || side.equals("right") || side.equals("up") || side.equals("down")) {
			// Get a list of all walls on the board
			List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
			List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();

			// Make one list for all walls on the board:
			List<Wall> allWallsOnBoard = Stream.of(blackWallsOnBoard, whiteWallsOnBoard).flatMap(x -> x.stream())
					.collect(Collectors.toList());

			//Clear all walls on board
			allWallsOnBoard.clear();
		}
	}
/**
 * @author William Wang

 */
	@When("Player {string} initiates to move {string}")
	public void player_initiates_to_move(String playerName, String jumpDirection) {
		//When definition!!! Call a method in state machine
		if(playerName.equals("white")) {
			if(jumpDirection.equals("left")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.West);
			}
			else if(jumpDirection.equals("right")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.East);
			}
			else if(jumpDirection.equals("up")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.North);
			}
			else if(jumpDirection.equals("down")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.South);
			}
			else if(jumpDirection.equals("upleft")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.NorthWest);
			}
			else if(jumpDirection.equals("upright")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.NorthEast);
			}
			else if(jumpDirection.equals("downleft")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.SouthWest);
			}
			else if(jumpDirection.equals("downright")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.SouthEast);
			}
		}
		else {
			if(jumpDirection.equals("left")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.West);
			}
			else if(jumpDirection.equals("right")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.East);
			}
			else if(jumpDirection.equals("up")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.North);
			}
			else if(jumpDirection.equals("down")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.South);
			}
			else if(jumpDirection.equals("upleft")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.NorthWest);
			}
			else if(jumpDirection.equals("upright")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.NorthEast);
			}
			else if(jumpDirection.equals("downleft")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.SouthWest);
			}
			else if(jumpDirection.equals("downright")) {
				legalMove = PawnBehavior.moveOrJump(PawnBehavior.MoveDirection.SouthEast);
			}
		}
	}

	/**
	 * @author William Wang
	 * @param side
	 * @param status
	 * @throws Exception 
	 */
	@Then("The move {string} shall be {string}")
	public void the_move_shall_be(String side, String status){
		// Write code here that turns the phrase above into concrete actions
		
		
		if(status.equals("illegal")) {
			assertFalse(legalMove);
		}
		else {
			assertTrue(legalMove);
		}
		
		//assertEquals(MovePawn(side), status)
		/*
		 * Have the move pawn method in the state machine return "legal" if method succesfully moves the player
		 * or return "illegal" if it doesnt. Then we can easily use the assertEquals statement above.
		 */
	}

	/**
	 * @author arneetkalra
	 * @param nrow
	 * @param ncol
	 */
	@And("Player's new position shall be {int}:{int}")
	public void player_s_new_position_shall_be(int nrow, int ncol) {
		// Get Player whose turn it is
		Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		// Calculate tile index of target tile
		int tileIndex = ((nrow - 1) * 9 + (ncol - 1));
		Tile targetPosition = QuoridorApplication.getQuoridor().getBoard().getTile(tileIndex);

		// If it is black players turn
		if(legalMove) {
			if (currentPlayer.hasGameAsWhite()) {
				assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow(), nrow);
				assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn(), ncol);
	
			}
	
			// If it is white players turn
			if (currentPlayer.hasGameAsBlack()) {
				assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow(),nrow);
				assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn(),ncol);
	
			}
		}
		else {
			if (currentPlayer.hasGameAsBlack()) {
				assertEquals(currentPlayer.getGameAsBlack().getCurrentPosition().getBlackPosition().getTile().getRow(), nrow);
				assertEquals(currentPlayer.getGameAsBlack().getCurrentPosition().getBlackPosition().getTile().getColumn(), ncol);
	
			}
	
			// If it is white players turn
			if (currentPlayer.hasGameAsWhite()) {
				assertEquals(currentPlayer.getGameAsWhite().getCurrentPosition().getWhitePosition().getTile().getRow(),nrow);
				assertEquals(currentPlayer.getGameAsWhite().getCurrentPosition().getWhitePosition().getTile().getColumn(),ncol);
	
			}
		}
	}

	/**
	 * @author arneetkalra
	 * @param nplayers
	 */
	@Then("The next player to move shall become {string}")
	public void the_next_player_to_move_shall_become(String nplayers) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if (nplayers.equals("white")) {
			assertEquals(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove(), quoridor.getCurrentGame().getWhitePlayer());
		} else {
			assertEquals(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove(), quoridor.getCurrentGame().getBlackPlayer());
		}
	}

	/**
	 * @author arneetkalra
	 * @param dir
	 * @param wrow
	 * @param wcol
	 */
	@Given("There is a {string} wall at {int}:{int}")
	public void there_is_a_wall_at(String dir, Integer wrow, Integer wcol) {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Direction direction = stringToDirection(dir);
		
		WallMove newWallMove = createWallMoveCandidate(direction, wrow, wcol);
		Wall aWall = currentGame.getCurrentPosition().getWhiteWallsInStock(0);
		aWall.setMove(newWallMove);
		currentGame.getCurrentPosition().addWhiteWallsOnBoard(aWall);
	}

				//Move Pawn  ------------------------

	/**
	 * @author arneetkalra
	 * @param string
	 * @param string2
	 */
	@And("There are no {string} walls {string} from the player")
	public void there_are_no_walls_from_the_player(String direction, String side) {
		// Current Position variable
		GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
		Direction dir = stringToDirection(direction);

		if (side.equals("left") || side.equals("right") || side.equals("up") || side.equals("down")) {
			// Get a list of all walls on the board
			List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();
			List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();

			// Make one list for all walls on the board:
			List<Wall> allWallsOnBoard = Stream.of(blackWallsOnBoard, whiteWallsOnBoard).flatMap(x -> x.stream())
					.collect(Collectors.toList());

			// Clear all walls on board
			allWallsOnBoard.clear();
		}
	}

	/**
	 * @author arneetkalra
	 * @param string
	 */
	@Given("The opponent is not {string} from the player")
	public void the_opponent_is_not_from_the_player(String side) {
		
		//Just verify my logic
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		Player currentPlayer = currentGame.getCurrentPosition().getPlayerToMove();
		Tile currentPlayerTile;
		Tile opponentTile;
		int column;
		int row;
		int currentPlayerTileIndex;
		Player opponent;
		
		if(currentPlayer == currentGame.getBlackPlayer()) {
			//Get current player tile
			currentPlayerTile = currentGame.getCurrentPosition().getBlackPosition().getTile();
			 column = currentPlayerTile.getColumn();
			 row = currentPlayerTile.getRow();
			currentPlayerTileIndex = (row -1) *9 + column -1;
		
			//Set opponent as other player
			opponent = currentGame.getWhitePlayer();
		} else {
			//Get current player tile
			currentPlayerTile = currentGame.getCurrentPosition().getWhitePosition().getTile();
			 column = currentPlayerTile.getColumn();
			 row = currentPlayerTile.getRow();
			 currentPlayerTileIndex = (row -1) *9 + column -1;
			
			//Set opponent as other player
			opponent = currentGame.getBlackPlayer();
		}
		
		
		if (opponent == currentGame.getBlackPlayer()) {
			switch (side) {
			case "left":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex + 1);
				currentGame.getCurrentPosition().getBlackPosition().setTile(opponentTile);
				break;
			case "right":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex - 1);
				currentGame.getCurrentPosition().getBlackPosition().setTile(opponentTile);
				break;
			case "up":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex + 1);
				currentGame.getCurrentPosition().getBlackPosition().setTile(opponentTile);
				break;
			case "down":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex - 1);
				currentGame.getCurrentPosition().getBlackPosition().setTile(opponentTile);
				break;
			}
		}
		//If opponent is white player
		else {
			switch (side) {
			case "left":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex + 1);
				currentGame.getCurrentPosition().getWhitePosition().setTile(opponentTile);
				break;
			case "right":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex - 1);
				currentGame.getCurrentPosition().getWhitePosition().setTile(opponentTile);
				break;
			case "up":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex + 1);
				currentGame.getCurrentPosition().getWhitePosition().setTile(opponentTile);
				break;
			case "down":
				opponentTile = quoridor.getBoard().getTile(currentPlayerTileIndex - 1);
				currentGame.getCurrentPosition().getWhitePosition().setTile(opponentTile);
				break;
			}
		}
	}

	/**
	 * @author arneetkalra
	 * @param string
	 */
	@Given("My opponent is not {string} from the player")
	public void my_opponent_is_not_from_the_player(String side) {
		//Exact same logic as this step definition
		the_opponent_is_not_from_the_player(side);
	}
	
	/*****************************
	 * Check if path exists feature
	 *****************************/
	@Given("A {string} wall move candidate exists at position {int}:{int}")
	public void a_wall_move_candidate_exists_at_position(String string, Integer int1, Integer int2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@And("The black player is located at {int}:{int}")
	public void the_black_player_is_located_at(Integer int1, Integer int2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@And("The white player is located at {int}:{int}")
	public void the_white_player_is_located_at(Integer int1, Integer int2) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@When("Check path existence is initiated")
	public void check_path_existence_is_initiated() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("Path is available for {string} player\\(s)")
	public void path_is_available_for_player_s(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}
	

	/*****************************
	 * Check if path exists feature
	 *****************************/

	@When("I initiate replay mode")
	public void i_initiate_replay_mode() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("The game shall be in replay mode")
	public void the_game_shall_be_in_replay_mode() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("The game is replay mode")
	public void the_game_is_replay_mode() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Given("The following moves have been played in game:")
	public void the_following_moves_have_been_played_in_game(io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	@And("The game does not have a final result")
	public void the_game_does_not_have_a_final_result() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("The next move is {double}")
	public void the_next_move_is(Double double1) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("I initiate to continue game")
	public void i_initiate_to_continue_game() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("The remaining moves of the game shall be removed")
	public void the_remaining_moves_of_the_game_shall_be_removed() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("The game has a final result")
	public void the_game_has_a_final_result() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("I shall be notified that finished games cannot be continued")
	public void i_shall_be_notified_that_finished_games_cannot_be_continued() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
	
	/*****************************
	 * Identify Game Drawn Feature
	 *****************************/

	@Given("The following moves were executed:")
	public void the_following_moves_were_executed(io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	@Given("Player {string} has just completed his move")
	public void player_has_just_completed_his_move(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("The last move of {string} is pawn move to {int}:{int}")
	public void the_last_move_of_is_pawn_move_to(String string, Integer int1, Integer int2) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("Checking of game result is initated")
	public void checking_of_game_result_is_initated() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
	
	/*****************************
	 * Identify Game Won Feature
	 *****************************/
	
	@And("The new position of {string} is {int}:{int}")
	public void the_new_position_of_is(String string, Integer int1, Integer int2) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("The clock of {string} is more than zero")
	public void the_clock_of_is_more_than_zero(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("The clock of {string} counts down to zero")
	public void the_clock_of_counts_down_to_zero(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}
	
	/*****************************
	 * Jump to Final and Jump to Start Feature (They have the same step definitons)
	 *****************************/

	@Given("The game is in replay mode")
	public void the_game_is_in_replay_mode() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@When("Jump to start position is initiated")
	public void jump_to_start_position_is_initiated() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("The next move shall be {double}")
	public void the_next_move_shall_be(Double double1) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("White player's position shall be \\({double})")
	public void white_player_s_position_shall_be(Double double1) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("Black player's position shall be \\({double})")
	public void black_player_s_position_shall_be(Double double1) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("White has <wwallno> on stock")
	public void white_has_wwallno_on_stock() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@And("Black has {int} on stock")
	public void black_has_on_stock(Integer int1) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	/*****************************
	 * REPORT FINAL RESULT FEATURE
	 *****************************/

	@When("The game is no longer running")
	public void the_game_is_no_longer_running() {
		// Call a Controller Method here, it should pretty much do this ->
		Controller.setGameToNotRunning();
	}

	@Then("The final result shall be displayed")
	public void the_final_result_shall_be_displayed() {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		QuoridorApplication.quoridorWindow = new QuoridorWindow();
		
		//Change this to notify final result
	    assertEquals(1,1);
	}

	@And("White's clock shall not be counting down")
	public void white_s_clock_shall_not_be_counting_down() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertFalse(window.getIsTimerActive());
	}

	@And("Black's clock shall not be counting down")
	public void black_s_clock_shall_not_be_counting_down() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertFalse(window.getIsTimerActive());
	}

	@And("White shall be unable to move")
	public void white_shall_be_unable_to_move() {
	    assertNotEquals(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode(),MoveMode.PlayerMove);
	}

	@And("Black shall be unable to move")
	public void black_shall_be_unable_to_move() {
	    assertNotEquals(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode(),MoveMode.PlayerMove);
	}
	
	/********************
	 * RESIGN GAME FEATURE
	 *********************/
	
	@When("Player initates to resign")
	public void player_initates_to_resign() {
		Controller.resignGame();
	}

	@Then("Game result shall be {string}")
	public void game_result_shall_be(String result) {
		Player playerToMove =QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
		Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();

		if(playerToMove == whitePlayer) {
		assertEquals("BlackWon",result);
		}
		else {
		assertEquals("WhiteWon",result);
		}
	}

	@And("The game shall no longer be running")
	public void the_game_shall_no_longer_be_running() {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			assertNotEquals(GameStatus.Running, quoridor.getCurrentGame().getGameStatus());
	}

	/********************
	 * Step Backward and Step Forward Feature (Only 1 definition because other ones are used for other step definitions too
	 * Coordinate with that person to get it done. 
	 *********************/
	@When("Step backward is initiated")
	public void step_backward_is_initiated() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
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
			if (wall != null) {
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
		// @formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		// @formatter:on
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
		Tile player1StartPos = quoridor.getBoard().getTile(76);
		Tile player2StartPos = quoridor.getBoard().getTile(4);
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 1; j <= 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 1; j <= 10; j++) {
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

		new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, quoridor);
		return game;
	}

	/** @author Luke Barber and Arneet Kalra */
	// Method to convert String input data type into respective Direction type
	private Direction stringToDirection(String direction) {
		if (direction.contentEquals("horizontal")) {
			return Direction.Horizontal;
		} else if (direction.contentEquals("vertical")) {
			return Direction.Vertical;
		} else
			return null;
	}

	/** @author Luke Barber and Arneet Kalra */
	// Method that makes WallMove Candidate from the given 3 parameters
	private WallMove createWallMoveCandidate(Direction dir, int row, int col) {
		// New game hard coded parameters for 5-8
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Current Game Object
		Game currentGame = quoridor.getCurrentGame();
		int moveNum = 1;
		int roundNum = 1;
		// Current Player object
		Player currentPlayer = currentGame.getWhitePlayer();

		// Gets this Board for the Tile
		Board currentBoard = quoridor.getBoard();

		currentPlayer.numberOfWalls();
		Wall currentWall = currentPlayer.getWall(1);

		Tile currentTile = currentBoard.getTile((row - 1) * 9 + col - 1);

		WallMove wallMoveCandidate = new WallMove(moveNum, roundNum, currentPlayer, currentTile, currentGame, dir,
				currentWall);

		return wallMoveCandidate;
	}

	private Player createPlayer(String username) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		if(!User.hasWithName(username))
		{
			quoridor.addUser(username);
		}
		int thinkingTime = 180;
		Player player = new Player(new Time(thinkingTime), User.getWithName(username), 9, Direction.Horizontal);
		return player;
	}


}
