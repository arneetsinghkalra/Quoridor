package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.QuoridorController;
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

public class CucumberStepDefinitions {

	// ***********************************************
	// Background step definitions
	// ***********************************************
	private QuoridorController quoridorController;
	private boolean validationResult;

	
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
	/**
	 * Load the game from the file
	 * @author Yin
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
	 * @author Yin
	 * @param fileName
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
	 * @author Yin
	 * @param row, column
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
	 * @author Yin
	 * @param player, direction, row, column
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
	 * @author Yin
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
	 * @author Yin
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
  		quoridorController.savePosition(fileName);
  	}
  	
  	@Then("A file with {string} is created in the filesystem")
  	public void aFileWithIsCreatedInTheFilesystem(String fileName) {
  		File f = new File(fileName);
  		assertTrue(f.exists());
  	}
  	
  	@Given("File {string} exists in the filesystem")
  	public void fileExistsInTheFileSystem(String fileName) {
  		File f = new File(fileName);
  		if(f.exists()) {
  			throw new IllegalArgumentException("File name exists");
  		}
  	}
  	/**
  	 * @author Yin
  	 * The user confirm whether to overwrite the existing file
  	 * */
  	@And("The user confirms to overwrite existing file")
  	public void theUserConfirmsToOverwriteExistingFile() {
  		quoridorController.confirmsToOverWrite();
  	}
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
		assertEquals(quoridorBlackPlayerRow,quoridor1BlackPlayerRow);
		assertEquals(quoridorBlackPlayerColumn,quoridor1BlackPlayerColumn);
		assertEquals(quoridorWhitePlayerRow,quoridor1WhitePlayerRow);
		assertEquals(quoridorWhitePlayerColumn,quoridor1WhitePlayerColumn);
  	}
  	
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
