package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
	
	
	//Global Variables for the features//
	private User user;
	private Player player;
	private Player player1, player2;
	private Quoridor quoridor;
	private Game game;
	
	//---------------------------------------------------------------------//
	//Feature 1 - Implemented by Ali Tapan - 260556540
	//---------------------------------------------------------------------//
	
	 @When("A new game is being initialized")
	 public void aNewGameIsBeingInitialized() throws IllegalArgumentException{ 
		 Controller.startNewGame();
	 }
	 
	 @And("White player chooses a username")
	 public void whitePlayerChoosesAUsername() throws IllegalArgumentException {
		 Controller.selectExistingUserName(player1);
	 }
	 
	 @And("Black player chooses a username")
	 public void blackPlayerChoosesAUsername() throws IllegalArgumentException {
		 Controller.selectExistingUserName(player2);
	 } 
	 
	 @And("Total thinking time is set")
	 public void totalThinkingTimeIsSet() throws IllegalArgumentException {
		Controller.setTimer();
	 } 
		 
	 @Then("The game shall become ready to start")
	 public void theGameIsReadyToStart() {
		 assertEquals(GameStatus.ReadyToStart, game.getGameStatus());
	 }
		 
	 @Given("The game is ready to start")
	 public void theGameIsReadyToStart$() {
		 game.setGameStatus(GameStatus.ReadyToStart);
	 }
	 
	 @When("I start the clock")
	 public void iStartTheClock() throws Throwable {
		 Controller.startClock();
	 }
	 
	 @Then("The game shall be running")
	 public void theGameShallbeRunning() {
		 assertEquals(GameStatus.Running, game.getGameStatus());
	 }
	 
	 @And("The board shall be initialized")
	 public void theBoardShallBeInitialized() {
		 theGameIsRunning();
		 
	 }
	 
	//---------------------------------------------------------------------//
	//Feature 2 - Implemented by Ali Tapan - 260556540					   //
	//---------------------------------------------------------------------//
	 
	 @Given("A new game is initializing")
	 public void aNewGameIsInitializing() {
		 quoridor = QuoridorApplication.getQuoridor();
		 game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, player, player, quoridor);
		 quoridor.setCurrentGame(game);
		//game.setGameStatus(GameStatus.Initializing);
	 }
	 
	 @Given("Next player to set user name is {string}")
	 public void nextPlayerToSetUserNameIs(String color) {
		 if (color == "white")
		 {
			 player = game.getWhitePlayer();
			 user = player.getUser();
		 }
		 else if(color == "black")
		 {
			 player = game.getBlackPlayer();
			 user = player.getUser();
		 }
		 
	 }
	 
	 @And("There is existing user {string}")
	 public void thereIsExistingUser(String username) {
		 assertEquals(true, User.hasWithName(username));
	 }
	 
	 @When("The player selects existing {string}")
	 public void thePlayerSelectsExisting(String username) throws IllegalArgumentException {
		 Controller.selectExistingUserName(player);
		 try {
			 Controller.selectExistingUserName(player);
			 fail(); // We should not reach this statement
		 } catch (IllegalArgumentException e){
			 // OK, the expected exception was thrown
		 }
	 }
	 
	 @Then("The name of player {string} in the new game shall be {string}")
	 public void theNameOfPlayerInTheNewGameShallBe(String color, String username) {
		 assertEquals(username, user.getName());
	 }
	 
	 @And("There is no existing user {string}")
	 public void thereIsNoExistingUser(String username) {
		 assertEquals(false, User.hasWithName(username));
	 }
	 
	 @When("The player provides new user name: {string}")
	 public void thePlayerProvidesNewUserName(String username) throws IllegalArgumentException {
		 Controller.enterNewUserName(username);
		 try {
			 Controller.enterNewUserName(username);
			 fail(); // We should not reach this statement
		 } catch (IllegalArgumentException e){
			 // OK, the expected exception was thrown
		 }
	 }
	 
	 @Then("The player shall be warned that {string} already exists")
	 public void thePlayerShallBeWarnedThatAlreadyExists(String color, String username) {
		 user.notify();
	 }
	 
	 @And("Next player to set user name shall be {string}")
	 public void nextPlayerToSetUserNameShallBe(String color){
		 player.getNextPlayer();
	 }
	 
	//---------------------------------------------------------------------//
	
	

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

}
