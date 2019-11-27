/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.view.QuoridorWindow;

import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;

// line 5 "../../../../../PawnStateMachine.ump"
public class PawnBehavior {

	// ------------------------
	// ENUMERATIONS
	// ------------------------

	public enum MoveDirection {
		East, South, West, North, SouthEast, SouthWest, NorthEast, NorthWest
	}

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// PawnBehavior State Machines
	public enum PawnSM {
		NoAdjacentOpponent, AdjacentOpponent
	}

	public enum PawnSMNoAdjacentOpponent {
		Null, NoAdjacentWall, AdjacentWall
	}

	public enum PawnSMAdjacentOpponent {
		Null, WallBehindAdjacentOpponent
	}

	private PawnSM pawnSM;
	private PawnSMNoAdjacentOpponent pawnSMNoAdjacentOpponent;
	private PawnSMAdjacentOpponent pawnSMAdjacentOpponent;

	// PawnBehavior Associations
	private static Game currentGame;
	private Player player;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public PawnBehavior() {
		setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
		setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.Null);
		setPawnSM(PawnSM.NoAdjacentOpponent);
	}

	// ------------------------
	// INTERFACE
	// ------------------------

	public String getPawnSMFullName() {
		String answer = pawnSM.toString();
		if (pawnSMNoAdjacentOpponent != PawnSMNoAdjacentOpponent.Null) {
			answer += "." + pawnSMNoAdjacentOpponent.toString();
		}
		if (pawnSMAdjacentOpponent != PawnSMAdjacentOpponent.Null) {
			answer += "." + pawnSMAdjacentOpponent.toString();
		}
		return answer;
	}

	public PawnSM getPawnSM() {
		return pawnSM;
	}

	public PawnSMNoAdjacentOpponent getPawnSMNoAdjacentOpponent() {
		return pawnSMNoAdjacentOpponent;
	}

	public PawnSMAdjacentOpponent getPawnSMAdjacentOpponent() {
		return pawnSMAdjacentOpponent;
	}

	private boolean __autotransition1__() {
		boolean wasEventProcessed = false;

		PawnSM aPawnSM = pawnSM;
		switch (aPawnSM) {
		case NoAdjacentOpponent:
			if (isAdjacentOpponent()) {
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

	private boolean __autotransition6__() {
		boolean wasEventProcessed = false;

		PawnSM aPawnSM = pawnSM;
		switch (aPawnSM) {
		case AdjacentOpponent:
			if (!(isAdjacentOpponent())) {
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

	private boolean __autotransition7__() {
		boolean wasEventProcessed = false;

		PawnSM aPawnSM = pawnSM;
		switch (aPawnSM) {
		case AdjacentOpponent:
			if (isWallBehindOpponent()) {
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

	private boolean __autotransition2__() {
		boolean wasEventProcessed = false;

		PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
		switch (aPawnSMNoAdjacentOpponent) {
		case NoAdjacentWall:
			if (isAdjacentWall()) {
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

	private boolean __autotransition3__() {
		boolean wasEventProcessed = false;

		PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
		switch (aPawnSMNoAdjacentOpponent) {
		case NoAdjacentWall:
			if (!(isAdjacentWall())) {
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

	private boolean __autotransition4__() {
		boolean wasEventProcessed = false;

		PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
		switch (aPawnSMNoAdjacentOpponent) {
		case AdjacentWall:
			if (isAdjacentWall()) {
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

	private boolean __autotransition5__() {
		boolean wasEventProcessed = false;

		PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent = pawnSMNoAdjacentOpponent;
		switch (aPawnSMNoAdjacentOpponent) {
		case AdjacentWall:
			if (!(isAdjacentWall())) {
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

	private boolean __autotransition8__() {
		boolean wasEventProcessed = false;

		PawnSMAdjacentOpponent aPawnSMAdjacentOpponent = pawnSMAdjacentOpponent;
		switch (aPawnSMAdjacentOpponent) {
		case WallBehindAdjacentOpponent:
			if (!(isAdjacentOpponent())) {
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

	private void exitPawnSM() {
		switch (pawnSM) {
		case NoAdjacentOpponent:
			exitPawnSMNoAdjacentOpponent();
			break;
		case AdjacentOpponent:
			exitPawnSMAdjacentOpponent();
			break;
		}
	}

	private void setPawnSM(PawnSM aPawnSM) {
		pawnSM = aPawnSM;

		// entry actions and do activities
		switch (pawnSM) {
		case NoAdjacentOpponent:
			if (pawnSMNoAdjacentOpponent == PawnSMNoAdjacentOpponent.Null) {
				setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.NoAdjacentWall);
			}
			__autotransition1__();
			break;
		case AdjacentOpponent:
			if (pawnSMAdjacentOpponent == PawnSMAdjacentOpponent.Null) {
				setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.WallBehindAdjacentOpponent);
			}
			__autotransition6__();
			__autotransition7__();
			break;
		}
	}

	private void exitPawnSMNoAdjacentOpponent() {
		switch (pawnSMNoAdjacentOpponent) {
		case NoAdjacentWall:
			setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
			break;
		case AdjacentWall:
			setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent.Null);
			break;
		}
	}

	private void setPawnSMNoAdjacentOpponent(PawnSMNoAdjacentOpponent aPawnSMNoAdjacentOpponent) {
		pawnSMNoAdjacentOpponent = aPawnSMNoAdjacentOpponent;
		if (pawnSM != PawnSM.NoAdjacentOpponent && aPawnSMNoAdjacentOpponent != PawnSMNoAdjacentOpponent.Null) {
			setPawnSM(PawnSM.NoAdjacentOpponent);
		}

		// entry actions and do activities
		switch (pawnSMNoAdjacentOpponent) {
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

	private void exitPawnSMAdjacentOpponent() {
		switch (pawnSMAdjacentOpponent) {
		case WallBehindAdjacentOpponent:
			setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent.Null);
			break;
		}
	}

	private void setPawnSMAdjacentOpponent(PawnSMAdjacentOpponent aPawnSMAdjacentOpponent) {
		pawnSMAdjacentOpponent = aPawnSMAdjacentOpponent;
		if (pawnSM != PawnSM.AdjacentOpponent && aPawnSMAdjacentOpponent != PawnSMAdjacentOpponent.Null) {
			setPawnSM(PawnSM.AdjacentOpponent);
		}

		// entry actions and do activities
		switch (pawnSMAdjacentOpponent) {
		case WallBehindAdjacentOpponent:
			__autotransition8__();
			break;
		}
	}

	/* Code from template association_GetOne */
	public Game getCurrentGame() {
		return currentGame;
	}

	public boolean hasCurrentGame() {
		boolean has = currentGame != null;
		return has;
	}

	/* Code from template association_GetOne */
	public Player getPlayer() {
		return player;
	}

	public boolean hasPlayer() {
		boolean has = player != null;
		return has;
	}

	/* Code from template association_SetUnidirectionalOptionalOne */
	public boolean setCurrentGame(Game aNewCurrentGame) {
		boolean wasSet = false;
		currentGame = aNewCurrentGame;
		wasSet = true;
		return wasSet;
	}

	/* Code from template association_SetUnidirectionalOptionalOne */
	public boolean setPlayer(Player aNewPlayer) {
		boolean wasSet = false;
		player = aNewPlayer;
		wasSet = true;
		return wasSet;
	}

	public void delete() {
		currentGame = null;
		player = null;
	}

	/**
	 * Returns the current row number of the pawn
	 */
	// line 32 "../../../../../PawnStateMachine.ump"
	public int getCurrentPawnRow() {
		return 0;
	}

	/**
	 * Returns the current column number of the pawn
	 */
	// line 34 "../../../../../PawnStateMachine.ump"
	public int getCurrentPawnColumn() {
		return 0;
	}

	/**
	 * Returns if it is legal to step in the given direction
	 */
	// line 36 "../../../../../PawnStateMachine.ump"
	public static boolean isLegalStep(MoveDirection dir) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		currentGame = quoridor.getCurrentGame();
		int originalBlackColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
		int originalBlackRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
		int originalWhiteColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
		int originalWhiteRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
		Tile originalBlackTile = currentGame.getQuoridor().getBoard()
				.getTile((originalBlackRow - 1) * 9 + originalBlackColumn - 1);
		Tile originalWhiteTile = currentGame.getQuoridor().getBoard()
				.getTile((originalWhiteRow - 1) * 9 + originalWhiteColumn - 1);

		if (currentGame.getCurrentPosition().getPlayerToMove().equals(currentGame.getBlackPlayer())) {
			if (dir == MoveDirection.North) {
				int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() - 1;
				if (newRow < 1) {
					return false;
				}
			} else if (dir == MoveDirection.South) {
				int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() + 1;
				if (newRow > 9) {
					return false;
				}
			} else if (dir == MoveDirection.East) {
				int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() + 1;
				if (newColumn > 9) {
					return false;
				}
			} else if (dir == MoveDirection.West) {
				int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() - 1;
				if (newColumn < 1) {
					return false;
				}
			}
			for (int i = 0; i < currentGame.getCurrentPosition().getBlackWallsOnBoard().size(); i++) {
				Tile wallTile = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove()
						.getTargetTile();
				Direction direction = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove()
						.getWallDirection();
				if (direction == Direction.Horizontal) {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					if ((wallTile == originalBlackTile || wallTileNext == originalBlackTile)
							&& dir == MoveDirection.South) {
						return false;
					}
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					Tile wallTileNextBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileBelow == originalBlackTile || wallTileNextBelow == originalBlackTile)
							&& dir == MoveDirection.North) {
						return false;
					}
				} else {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					if ((wallTile == originalBlackTile || wallTileBelow == originalBlackTile)
							&& dir == MoveDirection.East) {
						return false;
					}
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					Tile wallTileBelowNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileNext == originalBlackTile || wallTileBelowNext == originalBlackTile)
							&& dir == MoveDirection.West) {
						return false;
					}
				}
			}
			for (int i = 0; i < currentGame.getCurrentPosition().getWhiteWallsOnBoard().size(); i++) {
				Tile wallTile = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove()
						.getTargetTile();
				Direction direction = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove()
						.getWallDirection();
				if (direction == Direction.Horizontal) {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					if ((wallTile == originalBlackTile || wallTileNext == originalBlackTile)
							&& dir == MoveDirection.South) {
						return false;
					}
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					Tile wallTileNextBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileBelow == originalBlackTile || wallTileNextBelow == originalBlackTile)
							&& dir == MoveDirection.North) {
						return false;
					}
				} else {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					if ((wallTile == originalBlackTile || wallTileBelow == originalBlackTile)
							&& dir == MoveDirection.East) {
						return false;
					}
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					Tile wallTileBelowNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileNext == originalBlackTile || wallTileBelowNext == originalBlackTile)
							&& dir == MoveDirection.West) {
						return false;
					}
				}
			}
		}

		// White Player Logic
		else {
			if (dir == MoveDirection.North) {
				int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() - 1;
				if (newRow < 1) {
					System.out.println("false because of north");
					return false;
				}

			} else if (dir == MoveDirection.South) {
				int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() + 1;
				if (newRow > 9) {
					System.out.println("false because of south");
					return false;
				}
			} else if (dir == MoveDirection.East) {
				int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() + 1;
				if (newColumn > 9) {
					System.out.println("false because of east");
					return false;
				}
			} else if (dir == MoveDirection.West) {
				int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() - 1;
				if (newColumn < 1) {
					System.out.println("false because of west");
					return false;
				}
			}

			for (int i = 0; i < currentGame.getCurrentPosition().getBlackWallsOnBoard().size(); i++) {
				Tile wallTile = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove()
						.getTargetTile();
				Direction direction = currentGame.getCurrentPosition().getBlackWallsOnBoard().get(i).getMove()
						.getWallDirection();
				if (direction == Direction.Horizontal) {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					if ((wallTile == originalWhiteTile || wallTileNext == originalWhiteTile)
							&& dir == MoveDirection.South) {
						return false;
					}
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					Tile wallTileNextBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileBelow == originalWhiteTile || wallTileNextBelow == originalWhiteTile)
							&& dir == MoveDirection.North) {
						return false;
					}
				} else {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					if ((wallTile == originalWhiteTile || wallTileBelow == originalWhiteTile)
							&& dir == MoveDirection.East) {
						return false;
					}
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					Tile wallTileBelowNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileNext == originalWhiteTile || wallTileBelowNext == originalWhiteTile)
							&& dir == MoveDirection.West) {
						return false;
					}
				}
			}
			for (int i = 0; i < currentGame.getCurrentPosition().getWhiteWallsOnBoard().size(); i++) {
				Tile wallTile = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove()
						.getTargetTile();
				Direction direction = currentGame.getCurrentPosition().getWhiteWallsOnBoard().get(i).getMove()
						.getWallDirection();
				if (direction == Direction.Horizontal) {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					if ((wallTile == originalWhiteTile || wallTileNext == originalWhiteTile)
							&& dir == MoveDirection.South) {
						return false;
					}
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					Tile wallTileNextBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					if ((wallTileBelow == originalWhiteTile || wallTileNextBelow == originalWhiteTile)
							&& dir == MoveDirection.North) {
						return false;
					}
				} else {
					int wallTileColumn = wallTile.getColumn();
					int wallTileRow = wallTile.getRow();
					Tile wallTileBelow = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn - 1);
					if ((wallTile == originalWhiteTile || wallTileBelow == originalWhiteTile)
							&& dir == MoveDirection.East) {
						return false;
					}
					Tile wallTileNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow - 1) * 9 + wallTileColumn);
					Tile wallTileBelowNext = currentGame.getQuoridor().getBoard()
							.getTile((wallTileRow) * 9 + wallTileColumn);
					System.out.print("this is to clear wherthet");
					if ((wallTileNext.equals(originalWhiteTile) || wallTileBelowNext.equals(originalWhiteTile))
							&& dir == MoveDirection.West) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Action to be called when an illegal move is attempted
	 */
	// line 41 "../../../../../PawnStateMachine.ump"
	public void illegalMove() {

	}

	// line 47 "../../../../../PawnStateMachine.ump"
	public boolean isAdjacentOpponent() {
		return false;
	}

	// line 49 "../../../../../PawnStateMachine.ump"
	public boolean isAdjacentWall() {
		return false;
	}

	// line 51 "../../../../../PawnStateMachine.ump"
	public boolean isWallBehindOpponent() {
		return false;
	}

	// line 53 "../../../../../PawnStateMachine.ump"

	// line 53 "../../../../../PawnStateMachine.ump"
	public static void ensureGrabWallDeleted() {
		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		GamePosition currentGamePosition = currentGame.getCurrentPosition();
		currentGame.getCurrentPosition().getPlayerToMove();
		QuoridorApplication.getQuoridor().getBoard();

		// Get current Wall Move and it's player
		WallMove wallMove = currentGame.getWallMoveCandidate();
		Player player = currentGamePosition.getPlayerToMove();
		QuoridorWindow window = QuoridorApplication.quoridorWindow;

		if (window.isGrabWall) {
			Controller.cancelWallMove();

		}
	}

	public static boolean movePawn(MoveDirection dir) {

		if (isLegalStep(dir)) {
			if (currentGame.getCurrentPosition().getPlayerToMove().equals(currentGame.getBlackPlayer())) {
				if (dir == MoveDirection.North) {
					int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() - 1;
					int column = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow - 1) * 9 + column - 1);
					currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
					Controller.switchCurrentPlayer();
					ensureGrabWallDeleted();

					return true;
				} else if (dir == MoveDirection.South) {
					int newRow = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow() + 1;
					int column = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn();
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow - 1) * 9 + column - 1);
					currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				} else if (dir == MoveDirection.East) {
					int Row = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
					int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() + 1;
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row - 1) * 9 + newColumn - 1);
					currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				} else if (dir == MoveDirection.West) {
					int Row = currentGame.getCurrentPosition().getBlackPosition().getTile().getRow();
					int newColumn = currentGame.getCurrentPosition().getBlackPosition().getTile().getColumn() - 1;
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row - 1) * 9 + newColumn - 1);
					currentGame.getCurrentPosition().getBlackPosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				}
			} else {
				if (dir == MoveDirection.North) {
					int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() - 1;
					int column = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow - 1) * 9 + column - 1);
					currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					//currentGame.addMove(new Move());
					return true;
				} else if (dir == MoveDirection.South) {
					int newRow = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow() + 1;
					int column = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn();
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((newRow - 1) * 9 + column - 1);
					currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				} else if (dir == MoveDirection.East) {
					int Row = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
					int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() + 1;
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row - 1) * 9 + newColumn - 1);
					currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				} else if (dir == MoveDirection.West) {
					int Row = currentGame.getCurrentPosition().getWhitePosition().getTile().getRow();
					int newColumn = currentGame.getCurrentPosition().getWhitePosition().getTile().getColumn() - 1;
					Tile newTile = currentGame.getQuoridor().getBoard().getTile((Row - 1) * 9 + newColumn - 1);
					currentGame.getCurrentPosition().getWhitePosition().setTile(newTile);
					ensureGrabWallDeleted();

					Controller.switchCurrentPlayer();
					return true;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	// line 55 "../../../../../PawnStateMachine.ump"
	/**
	 * 
	 * @author William Wang Returns if it is legal to jump in the given direction
	 */
	// line 38 "../../../../../PawnStateMachine.ump"
	public static boolean isLegalJump(MoveDirection dir) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		boolean isWhite = currentPlayer.hasGameAsWhite();
		// opponent direction
		MoveDirection oppDirection;
		Tile blackTile = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
		Tile whiteTile = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();

		Tile currentTile = blackTile;
		if (isWhite) {
			currentTile = whiteTile;
		}

		if ((whiteTile.getRow() == blackTile.getRow()) && (whiteTile.getColumn() - blackTile.getColumn()) == 1) {
			if (isWhite) {
				oppDirection = MoveDirection.West;
			} else {
				oppDirection = MoveDirection.East;
			}
		} else if ((whiteTile.getRow() == blackTile.getRow())
				&& (whiteTile.getColumn() - blackTile.getColumn()) == -1) {
			if (isWhite) {
				oppDirection = MoveDirection.East;
			} else {
				oppDirection = MoveDirection.West;
			}

		} else if ((whiteTile.getColumn() == blackTile.getColumn()) && (whiteTile.getRow() - blackTile.getRow()) == 1) {
			if (isWhite) {
				oppDirection = MoveDirection.North;
			} else {
				oppDirection = MoveDirection.South;
			}
		} else if ((whiteTile.getColumn() == blackTile.getColumn())
				&& (whiteTile.getRow() - blackTile.getRow()) == -1) {
			if (isWhite) {
				oppDirection = MoveDirection.South;
			} else {
				oppDirection = MoveDirection.North;
			}

		} else {
			// not adjacent
			return false;
		}

		// if legal
		if (isWallBehind(oppDirection)) {
			if (oppDirection == MoveDirection.East) {
				if (dir == MoveDirection.NorthEast) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() + 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Horizontal))
						return false;
					return true;
				}
				else if(dir == MoveDirection.SouthEast) {
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() + 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Horizontal))
						return false;
					return true;
				}
			} else if (oppDirection == MoveDirection.West) {
				if ((dir == MoveDirection.NorthWest)) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() -2, Direction.Horizontal))
						return false;
					return true;
				} else if ((dir == MoveDirection.SouthWest)) {
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 2, Direction.Horizontal))
						return false;
					return true;
				}
			} else if (oppDirection == MoveDirection.North) {
				if ((dir == MoveDirection.NorthEast)) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow() - 2, currentTile.getColumn(), Direction.Vertical))
						return false;
					return true;
				} else if ((dir == MoveDirection.NorthWest)) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow() - 2, currentTile.getColumn() - 1, Direction.Vertical))
						return false;
					return true;
				}
			} else if (oppDirection == MoveDirection.South) {
				if ((dir == MoveDirection.SouthWest) ) {
					if (isWallAt(currentTile.getRow() + 1, currentTile.getColumn() - 1, Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Vertical))
						return false;
					return true;
				}
				else if((dir == MoveDirection.SouthEast)){
					if (isWallAt(currentTile.getRow() + 1, currentTile.getColumn(), Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Vertical))
						return false;
					return true;
				}
			}
			return false;
		} else {
			if (dir == oppDirection) {
				if (dir == MoveDirection.East) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Vertical))
						return false;
				} else if (dir == MoveDirection.West) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Vertical))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Vertical))
						return false;
				} else if (dir == MoveDirection.North) {
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Horizontal))
						return false;
				} else if (dir == MoveDirection.South) {
					if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Horizontal))
						return false;
					if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Horizontal))
						return false;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean isWallAt(int row, int column, Direction dir) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Wall> blackWalls = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
		List<Wall> whiteWalls = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
		for (Wall w : blackWalls) {
			if (w.getMove().getTargetTile().getRow() == row && w.getMove().getTargetTile().getColumn() == column
					&& w.getMove().getWallDirection() == dir) {
				return true;
			}
		}
		for (Wall w : whiteWalls) {
			if (w.getMove().getTargetTile().getRow() == row && w.getMove().getTargetTile().getColumn() == column
					&& w.getMove().getWallDirection() == dir) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author William Wang Returns if it is there is a wall or edge behind opponent
	 */
	// line 38 "../../../../../PawnStateMachine.ump"
	public static boolean isWallBehind(MoveDirection dir) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		boolean isWhite = currentPlayer.hasGameAsWhite();
		// opponent position
		PlayerPosition oppPosition;
		if (isWhite) {
			oppPosition = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
		} else {
			oppPosition = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
		}
		Tile currentTile = oppPosition.getTile();
		if (dir == MoveDirection.North) {
			if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Horizontal))
				return true;
			if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Horizontal))
				return true;
			if (currentTile.getRow() == 1)
				return true;
		} else if (dir == MoveDirection.South) {
			if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Horizontal))
				return true;
			if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Horizontal))
				return true;
			if (currentTile.getRow() == 9)
				return true;
		} else if (dir == MoveDirection.East) {
			if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn(), Direction.Vertical))
				return true;
			if (isWallAt(currentTile.getRow(), currentTile.getColumn(), Direction.Vertical))
				return true;
			if (currentTile.getColumn() == 9)
				return true;

		} else if (dir == MoveDirection.West) {
			if (isWallAt(currentTile.getRow() - 1, currentTile.getColumn() - 1, Direction.Vertical))
				return true;
			if (isWallAt(currentTile.getRow(), currentTile.getColumn() - 1, Direction.Vertical))
				return true;
			if (currentTile.getColumn() == 1)
				return true;

		}

		return false;
	}

	/**
	 * <p>
	 * Jump Pawn
	 * <p>
	 * <p>
	 * Jump Pawn towards target direction
	 * <p>
	 * 
	 * @author William Wang
	 * @param dir input target direction
	 */
	public static boolean jumpPawn(MoveDirection dir) {
		if (!isLegalJump(dir)) {
			return false;
		}

		// legal jump
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Game currentGame = quoridor.getCurrentGame();
		GamePosition currentPosition = currentGame.getCurrentPosition();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		int moveNumber = -1;
		int roundNumber = -1;
		if(currentGame.hasMoves()) {
			moveNumber = currentGame.getMove(currentGame.getMoves().size()-1).getMoveNumber();
			roundNumber = currentGame.getMove(currentGame.getMoves().size()-1).getRoundNumber();
		}
		Tile targetTile = null;
		if (currentPlayer.hasGameAsWhite()) {
			int currentRow = currentPosition.getWhitePosition().getTile().getRow();
			int currentColumn = currentPosition.getWhitePosition().getTile().getColumn();
			if (dir == MoveDirection.East) {
				targetTile = quoridor.getBoard().getTile((currentRow - 1) * 9 + currentColumn + 1);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.West) {
				targetTile = quoridor.getBoard().getTile((currentRow - 1) * 9 + currentColumn - 3);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.South) {
				targetTile = quoridor.getBoard().getTile((currentRow + 1) * 9 + currentColumn - 1);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.North) {
				targetTile = quoridor.getBoard().getTile((currentRow - 3) * 9 + currentColumn - 1);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.NorthEast) {
				targetTile = quoridor.getBoard().getTile((currentRow - 2) * 9 + currentColumn);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.NorthWest) {
				targetTile = quoridor.getBoard().getTile((currentRow - 2) * 9 + currentColumn - 2);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.SouthEast) {
				targetTile = quoridor.getBoard().getTile((currentRow) * 9 + currentColumn);
				currentPosition.getWhitePosition().setTile(targetTile);
			} else if (dir == MoveDirection.SouthWest) {
				targetTile = quoridor.getBoard().getTile((currentRow) * 9 + currentColumn - 2);
				currentPosition.getWhitePosition().setTile(targetTile);
			}
			currentGame.addMove(new JumpMove(moveNumber+1, roundNumber+1, currentPlayer, targetTile, currentGame));
		} else {
			int currentRow = currentPosition.getBlackPosition().getTile().getRow();
			int currentColumn = currentPosition.getBlackPosition().getTile().getColumn();
			if (dir == MoveDirection.East) {
				targetTile = quoridor.getBoard().getTile((currentRow - 1) * 9 + currentColumn + 1);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.West) {
				targetTile = quoridor.getBoard().getTile((currentRow - 1) * 9 + currentColumn - 3);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.South) {
				targetTile = quoridor.getBoard().getTile((currentRow + 1) * 9 + currentColumn - 1);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.North) {
				targetTile = quoridor.getBoard().getTile((currentRow - 3) * 9 + currentColumn - 1);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.NorthEast) {
				targetTile = quoridor.getBoard().getTile((currentRow - 2) * 9 + currentColumn);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.NorthWest) {
				targetTile = quoridor.getBoard().getTile((currentRow - 2) * 9 + currentColumn - 2);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.SouthEast) {
				targetTile = quoridor.getBoard().getTile((currentRow) * 9 + currentColumn);
				currentPosition.getBlackPosition().setTile(targetTile);
			} else if (dir == MoveDirection.SouthWest) {
				targetTile = quoridor.getBoard().getTile((currentRow) * 9 + currentColumn - 2);
				currentPosition.getBlackPosition().setTile(targetTile);
			}
			currentGame.addMove(new JumpMove(moveNumber+1, roundNumber, currentPlayer, targetTile, currentGame));
		}
		ensureGrabWallDeleted();
		Controller.switchCurrentPlayer();
		return true;
	}

	public static boolean moveOrJump(MoveDirection dir) {
		// if target direction has an opponent
		boolean adjOpponent = false;
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		boolean isWhite = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite();
		Tile playerTile = null;
		Tile opponentTile = null;
		if (isWhite) {
			playerTile = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
			opponentTile = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
		} else {
			playerTile = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
			opponentTile = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
		}
		if (dir == MoveDirection.East) {
			if (((playerTile.getRow() - opponentTile.getRow()) == 0)
					&& ((playerTile.getColumn() - opponentTile.getColumn()) == -1))
				adjOpponent = true;
		} else if (dir == MoveDirection.West) {
			if (((playerTile.getRow() - opponentTile.getRow()) == 0)
					&& ((playerTile.getColumn() - opponentTile.getColumn()) == 1))
				adjOpponent = true;
		} else if (dir == MoveDirection.North) {
			if (((playerTile.getRow() - opponentTile.getRow()) == 1)
					&& ((playerTile.getColumn() - opponentTile.getColumn()) == 0))
				adjOpponent = true;
		} else if (dir == MoveDirection.South) {
			if (((playerTile.getRow() - opponentTile.getRow()) == -1)
					&& ((playerTile.getColumn() - opponentTile.getColumn()) == 0))
				adjOpponent = true;
		} else {
			adjOpponent = true;
		}

		if (adjOpponent) {
			return jumpPawn(dir);
		} else {
			return movePawn(dir);
		}
	}
}