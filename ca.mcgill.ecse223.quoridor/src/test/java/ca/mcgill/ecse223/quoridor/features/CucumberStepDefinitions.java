package ca.mcgill.ecse223.quoridor.features;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    @And("I do not have a wall in my hand")
    public void iDoNotHaveAWallInMyHand() {
        // GUI-related feature -- TODO for later
    }

    @And("^I have a wall in my hand over the board$")
    public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
        // GUI-related feature -- TODO for later
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

    /*
     * TODO Insert your missing step definitions here
     *
     * Call the methods of the controller that will manipulate the model once they
     * are implemented
     *
     */

    // Parameters required for WallMove Object
    // Parameters for running game --ignore for now
    /*
     * private int moveNum = quoridor.getCurrentGame().numberOfMoves() + 1; private
     * int roundNum = (moveNum + 1) / 2 ;
     */

    // -----------------------------------------------------------------------------//
    // Feature 1 - StartNewGame - Implemented by Ali Tapan - 260556540
    // -----------------------------------------------------------------------------//

    /**
     *
     * @author Ali Tapan
     */
    @When("A new game is being initialized")
    public void aNewGameIsBeingInitialized() throws Throwable {
        Controller.StartNewGame();

    }

    /**
     *
     * @author Ali Tapan
     */
    @And("White player chooses a username")
    public void whitePlayerChoosesAUsername() throws Throwable {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        String username = "John";
        User user = quoridor.addUser(username);
        Player player = new Player(null, user, 9, Direction.Vertical);
        quoridor.getCurrentGame().setWhitePlayer(player);
    }

    /**
     *
     * @author Ali Tapan
     */
    @And("Black player chooses a username")
    public void blackPlayerChoosesAUsername() throws UnsupportedOperationException {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        String username = "Mel";
        User user = quoridor.addUser(username);
        Player player = new Player(null, user, 1, Direction.Vertical);
        quoridor.getCurrentGame().setBlackPlayer(player);
    }

    /**
     *
     * @author Ali Tapan
     */
    @And("Total thinking time is set")
    public void totalThinkingTimeIsSet() throws UnsupportedOperationException {
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
    public void iStartTheClock() throws Throwable {
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

    // -----------------------------------------------------------------------------//
    // Feature 2 - ProvideSelectUserName - Implemented by Ali Tapan - 260556540
    // -----------------------------------------------------------------------------//


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
     */
    @When("The player provides new user name: {string}")
    public void thePlayerProvidesNewUserName(String username) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Controller.provideNewUsername(username, quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
    }

    /**
     *
     * @author Ali Tapan
     */
    @Then("The player shall be warned that {string} already exists")
    public void thePlayerShallBeWarnedThatAlreadyExists(String username) {
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

    // ------------------------------3-4----------------------------------------------//

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

    /** @author Sam Perreault */
    @When("The initialization of the board is initiated")
    public void theInitializationOfTheBoardIsInitiated() {
        QuoridorWindow window = new QuoridorWindow();
        QuoridorApplication.quoridorWindow = window;
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
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertTrue(window.getIsTimerActive());
    }

    /** @author Sam Perreault */
    @And("It shall be shown that this is White's turn")
    public void itShallBeShownThatThisIsWhiteSTurn() {
        QuoridorWindow window = QuoridorApplication.quoridorWindow;
        assertEquals("userWhite's turn", window.getTurnLabel());
        // GUI method to be implemented later
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
        Tile player1StartPos = quoridor.getBoard().getTile(36);
        Tile player2StartPos = quoridor.getBoard().getTile(44);

        Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
        game.setWhitePlayer(players.get(0));
        game.setBlackPlayer(players.get(1));

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

        Player white = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
        Player black = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

        Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, quoridor);
        game.setWhitePlayer(white);
        game.setBlackPlayer(black);
        return game;
    }

    /** @author Luke Barber and Arneet Kalra */
    // Method to convert String input data type into respective Direction type
    private Direction stringToDirection(String direction) {
        if (direction == "horizontal") {
            return Direction.Horizontal;
        } else if (direction == "vertical") {
            return Direction.Vertical;
        } else
            return null;
    }
//
//	/** @author Luke Barber and Arneet Kalra */
//	// Method that makes WallMove Candidate from the given 3 parameters
//	private WallMove createWallMoveCandidate(Direction dir, int row, int col) {
////		targetTile = new Tile(row, col, currentBoard);
////		WallMove wallMoveCandidate = new WallMove(moveNum, roundNum, currentPlayer, targetTile, currentGame, dir,
////				currentWall);
////		return wallMoveCandidate;
//	}

    private Player createPlayer(String username) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        User user = quoridor.addUser(username);
        int thinkingTime = 180;
        Player player = new Player(new Time(thinkingTime), user, 9, Direction.Horizontal);
        return player;
    }
}
