package ca.mcgill.ecse223.quoridor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorPersistence;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import java.util.List;

public class QuoridorWindow extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private JTextField player1Field;
	private JTextField player2Field;
	private JTextField minuteField;
	private JTextField secondField;
	public Boolean wallSelected = false;
	public boolean isGrabWall = true;
	private Timer secondTimer;
	private JLabel timeRemLabel;
	private JLabel currentPlayerName;
	private JLabel lblBlackPlayerName;
	private JLabel lblwhitePlayerName;
	private JLabel lblTimeBlack;
	private JLabel lblTimeWhite;
	private JLabel lblWallsLeftBlack;
	private JLabel lblWallsLeftWhite;
	private static boolean confirms = true;
	private int[] playerView = { 0, 0, 0, 0 };
	// for the boards,tiles, and walls
	private static JButton[][] tiles = new JButton[9][9];
	private static JButton[][] wallCenters = new JButton[8][8];
	private static JButton btnReplayMode;
	private static JButton btnReplayBackwards;
	private static JButton btnReplayForwards;
	private static JButton btnContinuePlaying;
	private static JButton btnGrabButtonBlack;
	private static JButton btnGrabButtonWhite;
	private static JButton btnRotateWallBlack;
	private static JButton btnRotateWallWhite;
	private static JButton btnResignGameBlack;
	private static JButton btnResignGameWhite;

	private static JButton btnReplayJumpToFinal;
	private static JButton btnReplayJumpToStart;


	private Box[][] hWalls = new Box[9][9];
	private Box[][] vWalls = new Box[9][9];
	//The shape of the pawn is as defined below
	public char blackPawn = (char) 0x25A0; 
	public char whitePawn = (char) 0x25A1;
	//Main colors used everywhere
	public Color mainScreenBackgroundColor = new Color(208,238,255); 
	public Color boardBackgroundColor = mainScreenBackgroundColor;
	public Color tileColor = new Color(255,206,202);
	
	public Color placedWallColor = Color.LIGHT_GRAY;
	public Color validPositionColor = Color.green;
	private int fontSize = 15;
	private String font = "Raanana";
	public boolean timerRunning = false;
	public boolean resultBeingDisplayed = false;
	private static  boolean inReplayMode = false;



	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public QuoridorWindow() {

		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 90, 790, 611); // New dimensions
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		// JPanel titleScreenPanel = new JPanel();
		// titleScreenPanel.setBackground(new Color(255, 255, 0));

		ImagePanel titleScreenPanel = new ImagePanel(new ImageIcon("src/main/resources/quoridor.png").getImage());
		contentPane.add(titleScreenPanel, "titleScreenPanel");
		SpringLayout sl_titleScreenPanel = new SpringLayout();
		titleScreenPanel.setLayout(sl_titleScreenPanel);

		JButton newGameButton = new JButton("New Game");
		newGameButton.setBackground(new Color(255, 0, 255));
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) (contentPane.getLayout());
				layout.show(contentPane, "setupPanel");
			}
		});

		sl_titleScreenPanel.putConstraint(SpringLayout.NORTH, newGameButton, 185, SpringLayout.NORTH, titleScreenPanel);
		sl_titleScreenPanel.putConstraint(SpringLayout.WEST, newGameButton, 350, SpringLayout.WEST, titleScreenPanel);
		newGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		titleScreenPanel.add(newGameButton);

		JButton loadGameButton = new JButton("Load Game");
		sl_titleScreenPanel.putConstraint(SpringLayout.NORTH, loadGameButton, 230, SpringLayout.NORTH,
				titleScreenPanel);
		sl_titleScreenPanel.putConstraint(SpringLayout.WEST, loadGameButton, 350, SpringLayout.WEST, titleScreenPanel);
		// sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, loadGameButton, -124,
		// SpringLayout.SOUTH, titleScreenPanel);
		// sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, newGameButton, -6,
		// SpringLayout.NORTH, loadGameButton);
		// sl_titleScreenPanel.putConstraint(SpringLayout.EAST, newGameButton, 0,
		// SpringLayout.EAST, loadGameButton);
		loadGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		loadGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				Controller.startNewGame();
				Controller.initBlackPlayer("Black");
				Controller.initWhitePlayer("White");
				Controller.setTotalThinkingTime("00:03:00");
				Controller.startClock();
				Controller.createBoard();
				Controller.initializeBoard();
				boolean wrong = false;
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						Controller.loadGame(selectedFile.getName());
					} catch (UnsupportedOperationException e) {
						JFrame f = new JFrame();
						JTextField tf1;
						JButton b1;
						tf1 = new JTextField();
						tf1.setText("Cannot load game due to invalid position");
						tf1.setEditable(false);
						b1 = new JButton("OK");
						tf1.setBounds(50, 100, 350, 50);
						b1.setBounds(100, 200, 100, 50);
						f.getContentPane().add(tf1);
						f.getContentPane().add(b1);
						f.setSize(400, 400);
						f.getContentPane().setLayout(null);
						f.setVisible(true);
						b1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent b) {
								f.setVisible(false);
							}
						});
						wrong = true;
						e.printStackTrace();
					}
				}
				if (!wrong) {
					Quoridor quoridor = QuoridorApplication.getQuoridor();
					for (int i = 0; i < quoridor.getCurrentGame().getCurrentPosition()
							.numberOfBlackWallsOnBoard(); i++) {
						WallMove move = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(i)
								.getMove();
						QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),
								move.getTargetTile().getColumn(), move.getWallDirection());
					}
					for (int i = 0; i < quoridor.getCurrentGame().getCurrentPosition()
							.numberOfWhiteWallsOnBoard(); i++) {
						WallMove move = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(i)
								.getMove();
						QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),
								move.getTargetTile().getColumn(), move.getWallDirection());
					}
					CardLayout layout = (CardLayout) (contentPane.getLayout());
					layout.show(contentPane, "activeGamePanel");
				} else {
					Quoridor quoridor = QuoridorApplication.getQuoridor();
					quoridor.delete();
					quoridor = new Quoridor();
				}
			}
		});

		titleScreenPanel.add(loadGameButton);
		

		/*
		 * JLabel titleLabel = new JLabel("QUORIDOR");
		 * sl_titleScreenPanel.putConstraint(SpringLayout.EAST, loadGameButton, -57,
		 * SpringLayout.WEST, titleLabel);
		 * sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, titleLabel, -187,
		 * SpringLayout.SOUTH, titleScreenPanel);
		 * sl_titleScreenPanel.putConstraint(SpringLayout.EAST, titleLabel, -63,
		 * SpringLayout.EAST, titleScreenPanel); titleLabel.setFont(new
		 * Font("Cooper Black", Font.PLAIN, 40)); titleScreenPanel.add(titleLabel);
		 */
		
		

		JPanel activeGamePanel = new JPanel();		
		activeGamePanel.setBackground(mainScreenBackgroundColor);
		activeGamePanel.setOpaque(true);
		contentPane.add(activeGamePanel, "activeGamePanel");
		activeGamePanel.setLayout(new BorderLayout(0, 0));

		Box blackPlayerInfoVerticalBox = Box.createVerticalBox();
		activeGamePanel.add(blackPlayerInfoVerticalBox, BorderLayout.WEST);


		JLabel lblExtraBlack = new JLabel(""+blackPawn);
		lblExtraBlack.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(lblExtraBlack);

		lblBlackPlayerName = new JLabel("Black Player");
		lblBlackPlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBlackPlayerName.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(lblBlackPlayerName);
		lblBlackPlayerName.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		lblWallsLeftBlack = new JLabel("Walls Left = 10");
		lblWallsLeftBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWallsLeftBlack.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(lblWallsLeftBlack);

		JLabel lblTotalTimeLeftBlack = new JLabel("    Total Time Left:    ");
		lblTotalTimeLeftBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTotalTimeLeftBlack.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(lblTotalTimeLeftBlack);

		lblTimeBlack = new JLabel("     ");
		lblTimeBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTimeBlack.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(lblTimeBlack);

		

		btnGrabButtonBlack = new JButton("Grab wall");
		btnGrabButtonBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGrabButtonBlack.setAlignmentY(Component.TOP_ALIGNMENT);
		btnGrabButtonBlack.setFont(new Font(font, Font.PLAIN, fontSize));
		blackPlayerInfoVerticalBox.add(btnGrabButtonBlack);
		
		btnRotateWallBlack = new JButton("Rotate Wall");
		btnRotateWallBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRotateWallBlack.setAlignmentY(Component.TOP_ALIGNMENT);
		btnRotateWallBlack.setFont(new Font(font, Font.PLAIN, fontSize));

		blackPlayerInfoVerticalBox.add(btnRotateWallBlack);
		
		btnResignGameBlack = new JButton("Forfeit Game");
		btnResignGameBlack.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnResignGameBlack.setAlignmentY(Component.TOP_ALIGNMENT);
		btnResignGameBlack.setFont(new Font(font, Font.PLAIN, fontSize));	
		blackPlayerInfoVerticalBox.add(btnResignGameBlack);
		
		/** @author Luke Barber */
		btnGrabButtonBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
				if (isGrabWall) {
					if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
						notifyNotYourTurn();
						return;

					} else if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
						if (quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size() == 0) {
							warningNoMoreWalls();
						} else {
							Controller.grabWall(quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(
									quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock() - 1));
							lblWallsLeftBlack.setText("Walls Left = "
									+ quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock());
						}
					}
					isGrabWall = false;
					setWallSelected(true);
				} else {
					warningInvalidGrabWall();
				}
			}

		});		
		
		btnRotateWallBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

				if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
				Controller.rotateWall(quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced());
				}
				else {
					notifyNotYourTurn();
				}
			}
		});

		btnResignGameBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

				if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
					Controller.forfeitGame();
				}
				else {
					notifyNotYourTurn();
				}
			}
		});
		
		// _________________ Start of White Player Info _________________________

		Box whitePlayerInfoVerticalBox = Box.createVerticalBox();
		activeGamePanel.add(whitePlayerInfoVerticalBox, BorderLayout.EAST);

		JLabel lblExtraWhite = new JLabel(""+whitePawn);
		lblExtraWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblExtraWhite.setFont(new Font(font, Font.PLAIN, fontSize));
		lblExtraWhite.setForeground(Color.white);
		whitePlayerInfoVerticalBox.add(lblExtraWhite);

		lblwhitePlayerName = new JLabel("White Player");
		lblwhitePlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblwhitePlayerName.setFont(new Font(font, Font.PLAIN, fontSize));
		whitePlayerInfoVerticalBox.add(lblwhitePlayerName);

		lblWallsLeftWhite = new JLabel("Walls Left = 10");
		lblWallsLeftWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWallsLeftWhite.setFont(new Font(font, Font.PLAIN, fontSize));
		whitePlayerInfoVerticalBox.add(lblWallsLeftWhite);

		JLabel lblTotalTimeLeftWhite = new JLabel("    Total Time Left:    ");
		lblTotalTimeLeftWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTotalTimeLeftWhite.setFont(new Font(font, Font.PLAIN, fontSize));
		whitePlayerInfoVerticalBox.add(lblTotalTimeLeftWhite);

		lblTimeWhite = new JLabel("     ");
		lblTimeWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTimeWhite.setFont(new Font(font, Font.PLAIN, fontSize));
		whitePlayerInfoVerticalBox.add(lblTimeWhite);


		btnGrabButtonWhite = new JButton("Grab wall");
		btnGrabButtonWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGrabButtonWhite.setAlignmentY(Component.TOP_ALIGNMENT);
		btnGrabButtonWhite.setFont(new Font(font, Font.PLAIN, fontSize));	
		whitePlayerInfoVerticalBox.add(btnGrabButtonWhite);
		
		btnRotateWallWhite = new JButton("Rotate Wall");
		btnRotateWallWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRotateWallWhite.setAlignmentY(Component.TOP_ALIGNMENT);
		btnRotateWallWhite.setFont(new Font(font, Font.PLAIN, fontSize));
		whitePlayerInfoVerticalBox.add(btnRotateWallWhite);
		
		btnResignGameWhite = new JButton("Forfeit Game");
		btnResignGameWhite.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnResignGameWhite.setAlignmentY(Component.TOP_ALIGNMENT);
		btnResignGameWhite.setFont(new Font(font, Font.PLAIN, fontSize));	
		whitePlayerInfoVerticalBox.add(btnResignGameWhite);
		
		/** @author Luke Barber */
		btnGrabButtonWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
				if (isGrabWall) {
					if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
						if (quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size() == 0) {
							warningNoMoreWalls();
						} else {
							Controller.grabWall(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(
									quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock() - 1));
							lblWallsLeftWhite.setText("Walls Left = "
									+ quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock());
						}
					} else if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
						notifyNotYourTurn();
						return;
					}
					isGrabWall = false;
					setWallSelected(true);
				} else {
					warningInvalidGrabWall();
				}
			}

		});		
		
		btnRotateWallWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

				if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
				Controller.rotateWall(quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced());
				}
				else {
					notifyNotYourTurn();
				}
				/*
				JOptionPane.showMessageDialog(null,
						"The current direction is "
								+ quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection(),
						"", JOptionPane.PLAIN_MESSAGE);
						*/
			}
		});

		btnResignGameWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();

				if (currentPlayer.equals(quoridor.getCurrentGame().getWhitePlayer())) {
					Controller.forfeitGame();
				}
				else {
					notifyNotYourTurn();
				}
			}
		});
		
		
		Box gameOptionBox = Box.createVerticalBox();
		activeGamePanel.add(gameOptionBox, BorderLayout.SOUTH);

		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBackground(Color.PINK);
		gameOptionBox.add(horizontalBox);

		Box verticalBox = Box.createVerticalBox();
		horizontalBox.add(verticalBox);

		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewGame.setFont(new Font(font, Font.PLAIN, fontSize));			
		verticalBox.add(btnNewGame);

		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				homeScreen();
			}
		});

		JButton btnSaveGame = new JButton("Save Game");
		btnSaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSaveGame.setFont(new Font(font, Font.PLAIN, fontSize));	
		verticalBox.add(btnSaveGame);

		JButton btnLoadGame = new JButton("Load Game");
		btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLoadGame.setFont(new Font(font, Font.PLAIN, fontSize));	
		verticalBox.add(btnLoadGame);


		btnReplayJumpToStart = new JButton((char) 0x2b60+" Jump to Start");
		btnReplayJumpToStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReplayJumpToStart.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnReplayJumpToStart);
		btnReplayJumpToStart.setVisible(false);

		btnReplayJumpToStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if(!Controller.isInReplayMode()) {
					notifyNotInReplayMode();
				}
				else {
					Controller.jumpToStartPosition();
				}
			}
		});

		btnReplayBackwards = new JButton(""+(char) 0x2b60);
		btnReplayBackwards.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReplayBackwards.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnReplayBackwards);
		btnReplayBackwards.setVisible(false);

		btnReplayBackwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if(!Controller.isInReplayMode()) {
					notifyNotInReplayMode();
				}
				else {
					//Go forwards method
					Controller.stepBackward();
					updatePositions();
				}
			}
		});

		btnReplayMode = new JButton("Replay Mode");
		btnReplayMode.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReplayMode.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnReplayMode);

		btnReplayMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				setBoardConitionsWhenEnteringReplayMode();
				Controller.initiateReplayMode(Controller.getCurrentGame());
			}
		});

		btnContinuePlaying = new JButton("Continue from Here");
		btnContinuePlaying.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnContinuePlaying.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnContinuePlaying);
		btnContinuePlaying.setVisible(false);

		btnContinuePlaying.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				setBoardConitionsWhenExitingReplayMode();
				Controller.initiateContinueGame(Controller.getCurrentGame());
			}
		});

		btnReplayForwards = new JButton(""+(char) 0x2b62);
		btnReplayForwards.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReplayForwards.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnReplayForwards);
		btnReplayForwards.setVisible(false);

		btnReplayForwards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if(!Controller.isInReplayMode()) {
					notifyNotInReplayMode();
				}
				else {
					//Go forwards method
					Controller.stepForward();
					updatePositions();

				}
			}
		});


		btnReplayJumpToFinal = new JButton("Jump to Final "+(char) 0x2b62);
		btnReplayJumpToFinal.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReplayJumpToFinal.setFont(new Font(font, Font.PLAIN, fontSize));
		horizontalBox.add(btnReplayJumpToFinal);
		btnReplayJumpToFinal.setVisible(false);

		btnReplayJumpToFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				if(!Controller.isInReplayMode()) {
					notifyNotInReplayMode();
				}
				else {
					Controller.jumpToFinalPosition();
				}
			}
		});


		btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				loadGame();
			}
		});

		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				JFrame f = new JFrame();
				JTextField tf1;
				JButton b1;
				tf1 = new JTextField();
				tf1.setBounds(50, 50, 150, 20);
				tf1.setEditable(true);
				b1 = new JButton("create");
				b1.setBounds(50, 200, 50, 50);
				f.getContentPane().add(tf1);
				f.getContentPane().add(b1);
				f.setSize(300, 300);
				f.getContentPane().setLayout(null);
				f.setVisible(true);
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent b) {
						f.setVisible(false);
						Path path = Paths.get("src/test/resources/saveGame/" + tf1.getText());
						if (Files.exists(path)) {
							JFrame f = new JFrame();
							JTextField tf2;
							JButton b1;
							JButton b2;
							tf2 = new JTextField();
							tf2.setText("confirms to overwrite");
							tf2.setBounds(50, 50, 150, 20);
							tf2.setEditable(false);
							b1 = new JButton("Yes");
							b1.setBounds(50, 200, 100, 50);
							b2 = new JButton("No");
							b2.setBounds(150, 200, 100, 50);
							f.getContentPane().add(tf2);
							f.getContentPane().add(b1);
							f.getContentPane().add(b2);
							f.setSize(300, 300);
							f.getContentPane().setLayout(null);
							f.setVisible(true);
							b1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent b) {
									confirms = true;
									f.setVisible(false);
									try {
										Controller.saveGame(tf1.getText(),confirms);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

							b2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent b) {
									f.setVisible(false);
								}
							});
						} else {
							try {
								Controller.saveGame(tf1.getText(),
										confirms);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}

		});



		Box titleTimeBox = Box.createVerticalBox();
		//titleTimeBox.setBackground(Color.white);
		activeGamePanel.add(titleTimeBox, BorderLayout.NORTH);

		JLabel gameTitleLabel = new JLabel("QUORIDOR");
		gameTitleLabel.setAlignmentX(0.5f);
		gameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameTitleLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		//gameTitleLabel.setForeground(Color.BLUE);
		titleTimeBox.add(gameTitleLabel);

		currentPlayerName = new JLabel("Current Player : ");
		currentPlayerName.setAlignmentX(0.4f);
		currentPlayerName.setAlignmentY(1.5f);
		currentPlayerName.setFont(new Font("Raanana", Font.PLAIN, 18));
		titleTimeBox.add(currentPlayerName);

		// board
		JPanel gameBoardPanel = new JPanel();
		gameBoardPanel.setBackground(boardBackgroundColor);
		gameBoardPanel.setOpaque(true);
		activeGamePanel.add(gameBoardPanel, BorderLayout.CENTER);
		gameBoardPanel.setLayout(new GridBagLayout());

		ImagePanel setupPanel = new ImagePanel(new ImageIcon("src/main/resources/setup.png").getImage());
		// JPanel setupPanel = new JPanel();
		contentPane.add(setupPanel, "setupPanel");
		SpringLayout sl_setupPanel = new SpringLayout();
		setupPanel.setLayout(sl_setupPanel);

		Box player1NameBox = Box.createVerticalBox();
		player1NameBox.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		sl_setupPanel.putConstraint(SpringLayout.NORTH, player1NameBox, 107, SpringLayout.NORTH, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.WEST, player1NameBox, 30, SpringLayout.WEST, setupPanel);
		setupPanel.add(player1NameBox);

		Component nameTopRigid = Box.createRigidArea(new Dimension(10, 10));
		player1NameBox.add(nameTopRigid);

		Box player1HorBox = Box.createHorizontalBox();
		player1NameBox.add(player1HorBox);

		Component nameP1leftRigid = Box.createRigidArea(new Dimension(10, 10));
		player1HorBox.add(nameP1leftRigid);

		JLabel lblPlayer1Name = new JLabel("Player 1 Name");
		lblPlayer1Name.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		player1HorBox.add(lblPlayer1Name);

		Component player1NamesStrut = Box.createHorizontalStrut(20);
		player1HorBox.add(player1NamesStrut);

		player1Field = new JTextField();
		player1HorBox.add(player1Field);
		player1Field.setColumns(10);

		Component nameP1RightRigid = Box.createRigidArea(new Dimension(10, 10));
		player1HorBox.add(nameP1RightRigid);

		Box player2HorBox = Box.createHorizontalBox();
		player1NameBox.add(player2HorBox);

		Component rigidArea_4 = Box.createRigidArea(new Dimension(10, 10));
		player2HorBox.add(rigidArea_4);

		JLabel lblPlayerName_1 = new JLabel("Player 2 Name");
		lblPlayerName_1.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		player2HorBox.add(lblPlayerName_1);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		player2HorBox.add(horizontalStrut_1);

		player2Field = new JTextField();
		player2HorBox.add(player2Field);
		player2Field.setColumns(10);

		Component rigidArea_2 = Box.createRigidArea(new Dimension(10, 10));
		player2HorBox.add(rigidArea_2);

		Component rigidArea = Box.createRigidArea(new Dimension(10, 10));
		player1NameBox.add(rigidArea);

		Box thinkingTimeBox = Box.createVerticalBox();
		sl_setupPanel.putConstraint(SpringLayout.NORTH, thinkingTimeBox, 63, SpringLayout.SOUTH, player1NameBox);
		sl_setupPanel.putConstraint(SpringLayout.WEST, thinkingTimeBox, 0, SpringLayout.WEST, player1NameBox);
		setupPanel.add(thinkingTimeBox);

		JLabel setTimerLabel = new JLabel("Set Timer - Minutes & Seconds ");
		setTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setTimerLabel.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		thinkingTimeBox.add(setTimerLabel);

		Box timefieldBox = Box.createHorizontalBox();
		thinkingTimeBox.add(timefieldBox);

		minuteField = new JTextField();
		minuteField.setColumns(2);
		minuteField.setHorizontalAlignment(SwingConstants.TRAILING);
		timefieldBox.add(minuteField);

		secondField = new JTextField();
		timefieldBox.add(secondField);
		secondField.setColumns(2);


		JComboBox existingUsernames1 = new JComboBox();
		sl_setupPanel.putConstraint(SpringLayout.NORTH, existingUsernames1, 0, SpringLayout.NORTH, player1NameBox);
		sl_setupPanel.putConstraint(SpringLayout.WEST, existingUsernames1, 10, SpringLayout.EAST, player1NameBox);
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, existingUsernames1, 144, SpringLayout.NORTH, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.EAST, existingUsernames1, 200, SpringLayout.EAST, player1NameBox);
		existingUsernames1.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		existingUsernames1.setModel(new DefaultComboBoxModel(Controller.listExistingUsernames()));
		setupPanel.add(existingUsernames1);

		JButton startGameButton = new JButton("Start Game");
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, startGameButton, 0, SpringLayout.SOUTH, thinkingTimeBox);
		sl_setupPanel.putConstraint(SpringLayout.EAST, startGameButton, -55, SpringLayout.EAST, setupPanel);
		startGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		JComboBox existingUsernames2 = new JComboBox();
		sl_setupPanel.putConstraint(SpringLayout.NORTH, existingUsernames2, 6, SpringLayout.SOUTH, existingUsernames1);
		sl_setupPanel.putConstraint(SpringLayout.WEST, existingUsernames2, 0, SpringLayout.WEST, existingUsernames1);
		sl_setupPanel.putConstraint(SpringLayout.EAST, existingUsernames2, 0, SpringLayout.EAST, existingUsernames1);
		existingUsernames2.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		existingUsernames2.setModel(new DefaultComboBoxModel(Controller.listExistingUsernames()));
		setupPanel.add(existingUsernames2);

		startGameButton.addActionListener(new ActionListener() {
			/**
			 * <p>
			 * Start new game button
			 * <p>
			 * 
			 * 
			 * @author Ali Tapan
			 * 
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) (contentPane.getLayout());

				String time = "";
				String seconds = "";
				String minutes = "";

				// Checks trivial inputs
				if ((player1Field.getText().length() == 0
						&& existingUsernames1.getSelectedItem().equals("or select existing username..."))) {
					JOptionPane.showMessageDialog(null, "Please provide a username!", "Invalid Username",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (player2Field.getText().length() == 0
						&& existingUsernames2.getSelectedItem().equals("or select existing username...")) {
					JOptionPane.showMessageDialog(null, "Please provide a username!", "Invalid Username",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (minuteField.getText().length() == 0 && secondField.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Please provide user time!", "Invalid Remaining Time",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (player1Field.getText().equals(player2Field.getText())) {
					JOptionPane.showMessageDialog(null, "Player 1 user name and Player 2 user name cannot be the same!",
							"Invalid Username", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (!existingUsernames1.getSelectedItem().equals("or select existing username...")
						&& !existingUsernames2.getSelectedItem().equals("or select existing username...")) {
					if (existingUsernames1.getSelectedItem().equals(existingUsernames2.getSelectedItem())) {
						JOptionPane.showMessageDialog(null,
								"Player 1 user name and Player 2 user name cannot be the same!", "Invalid Username",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				if (!minuteField.getText().matches("[0-9]*") || !secondField.getText().matches("[0-9]*")) {
					JOptionPane.showMessageDialog(null, "Please provide integers for user time!",
							"Invalid Remaining Time", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (minuteField.getText().length() > 2 || secondField.getText().length() > 2) {
					JOptionPane.showMessageDialog(null, "Please provide 2 digits or less for remaining time!",
							"Invalid Remaining Time", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Controller.startNewGame();
				// Provide layout
				layout.show(contentPane, "activeGamePanel");

				// Checks if the user has entered a valid user name
				if (player1Field.getText().length() > 0
						&& existingUsernames1.getSelectedItem().equals("or select existing username...")) {
					try {
						if (!Controller.provideNewUsername(player1Field.getText(),
								Controller.initWhitePlayer("user1"))) {
							JOptionPane.showMessageDialog(null, "This user name already exists!", "Invalid Username",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (player2Field.getText().length() > 0
						&& existingUsernames2.getSelectedItem().equals("or select existing username...")) {
					try {
						if (!Controller.provideNewUsername(player2Field.getText(),
								Controller.initBlackPlayer("user2"))) {
							JOptionPane.showMessageDialog(null, "This user name already exists!", "Invalid Username",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// Checks if the player enters an input and also selects an existing user name,
				// if true will show a dialog box
				if (player1Field.getText().length() > 0
						&& !existingUsernames1.getSelectedItem().equals(Controller.listExistingUsernames()[0])) {
					JOptionPane.showMessageDialog(null,
							"Cannot enter new user name and select an existing username at the same time!",
							"Invalid Username", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (player2Field.getText().length() > 0
						&& !existingUsernames2.getSelectedItem().equals(Controller.listExistingUsernames()[0])) {
					JOptionPane.showMessageDialog(null,
							"Cannot enter new user name and select an existing username at the same time!",
							"Invalid Username", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Checks if the player selected an existing user name
				if (!existingUsernames1.getSelectedItem().equals("or select existing username...")) {
					Controller.selectExistingUsername(existingUsernames1.getSelectedItem().toString(),
							Controller.initWhitePlayer("user1"));
				}

				// Checks if the player selected an existing user name
				if (!existingUsernames2.getSelectedItem().equals("or select existing username...")) {
					Controller.selectExistingUsername(existingUsernames2.getSelectedItem().toString(),
							Controller.initBlackPlayer("user2"));
				}

				// Checks if the user has selected
				if (minuteField.getText().length() < 2) {
					minutes = minutes + "0" + minuteField.getText();
				} else {
					minutes = minuteField.getText();
				}
				if (secondField.getText().length() < 2) {
					seconds = seconds + "0" + secondField.getText();
				} else {
					seconds = secondField.getText();
				}

				if (minuteField.getText().length() == 0) {
					minutes = minutes + "00";
				}
				if (secondField.getText().length() == 0) {
					seconds = seconds + "00";
				}

				time = "00:" + minutes + ":" + seconds;
				Controller.setTotalThinkingTime(time);
				Controller.startClock();
				Controller.createBoard();
				Controller.initializeBoard();
			}
		});
		setupPanel.add(startGameButton);


		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int curI=i, curJ=j;
				
				tiles[i][j] = new JButton();	
				tiles[i][j].setBounds(i, j, 20, 20);
				tiles[i][j].setOpaque(true);				
				tiles[i][j].setBackground(tileColor);
				tiles[i][j].setBorderPainted(false);

				tiles[i][j].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {

						//Only hover if not in replay mode
						if (!isNotReplayMode()) {
							// Calls pawnBehavior's isLegalMove/Jump, and determines if legal
							// Prompts user on failure
							Player curPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
									.getPlayerToMove();
							int playerRow = -1, playerColumn = -1;
							if (curPlayer.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
								playerRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
										.getBlackPosition().getTile().getRow() - 1;
								playerColumn = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
										.getBlackPosition().getTile().getColumn() - 1;
							} else if (curPlayer
									.equals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
								playerRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
										.getWhitePosition().getTile().getRow() - 1;
								playerColumn = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
										.getWhitePosition().getTile().getColumn() - 1;
							}
							int vertAbsDiff = Math.abs(playerRow - curI);
							int vertDiff = curI - playerRow;
							int horAbsDiff = Math.abs(playerColumn - curJ);
							int horDiff = curJ - playerColumn;

							if ((tiles[curI][curJ].getBackground() == Color.BLACK)
									|| (tiles[curI][curJ].getBackground() == Color.WHITE)) {
								// If its a player there, then its better not to change anything
							}

							// There isnt a player already at the tile, it is empty
							else {
								PawnBehavior.MoveDirection dir = null;

								if (vertAbsDiff + horAbsDiff > 2 || vertAbsDiff > 2 || horAbsDiff > 2
										|| vertAbsDiff + horAbsDiff == 0) {
									tiles[curI][curJ].setBackground(Color.red);

									return;
								}

								switch (vertDiff) {
								case 1:
									if (PawnBehavior.isLegalStep(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
										break;
									} else {
										tiles[curI][curJ].setBackground(Color.red);
										break;
									}

								case 2:
									dir = PawnBehavior.MoveDirection.South;
									if (PawnBehavior.isLegalJump(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
									} else {
										tiles[curI][curJ].setBackground(Color.red);
									}
									break;

								case -1:
									if (PawnBehavior.isLegalStep(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
									} else {
										tiles[curI][curJ].setBackground(Color.red);
									}
									break;

								case -2:
									dir = PawnBehavior.MoveDirection.North;
									if (PawnBehavior.isLegalJump(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
									} else {
										tiles[curI][curJ].setBackground(Color.red);
									}

								default:
									break;
								}

								switch (horDiff) {
								case -1:
									if (vertDiff < 0) {
										dir = PawnBehavior.MoveDirection.NorthWest;
										if (PawnBehavior.isLegalJump(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
										break;
									} else if (vertDiff > 0) {
										dir = PawnBehavior.MoveDirection.SouthWest;
										if (PawnBehavior.isLegalJump(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
										break;
									} else {
										dir = PawnBehavior.MoveDirection.West;
										if (PawnBehavior.isLegalStep(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
									}
									break;

								case -2:
									dir = PawnBehavior.MoveDirection.West;
									if (PawnBehavior.isLegalJump(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
									} else {
										tiles[curI][curJ].setBackground(Color.red);
									}
									break;

								case 1:
									if (vertDiff < 0) {
										dir = PawnBehavior.MoveDirection.NorthEast;
										if (PawnBehavior.isLegalJump(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
										break;
									} else if (vertDiff > 0) {
										dir = PawnBehavior.MoveDirection.SouthEast;
										if (PawnBehavior.isLegalJump(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
										break;
									} else {
										dir = PawnBehavior.MoveDirection.East;
										if (PawnBehavior.isLegalStep(dir)) {
											tiles[curI][curJ].setBackground(Color.green);
										} else {
											tiles[curI][curJ].setBackground(Color.red);
										}
									}

									break;

								case 2:
									dir = PawnBehavior.MoveDirection.East;
									if (PawnBehavior.isLegalJump(dir)) {
										tiles[curI][curJ].setBackground(Color.green);
									} else {
										tiles[curI][curJ].setBackground(Color.red);
									}
									break;

								default:
									break;
								}

							}
						}
					}

					public void mouseExited(MouseEvent e) {
						if (tiles[curI][curJ].getBackground() == Color.BLACK) {
							tiles[curI][curJ].setBackground(Color.BLACK);
						} else if (tiles[curI][curJ].getBackground() == Color.WHITE) {
							tiles[curI][curJ].setBackground(Color.WHITE);
						} else {
							tiles[curI][curJ].setBackground(tileColor);
						}
					}
				});


				tiles[i][j].addActionListener(new ActionListener() {
					/** @author Sam Perreault */
					public void actionPerformed(ActionEvent e) {


						updatePlayerWallAndForfeitButtonsVisually();


						// Calls pawnBehavior's isLegalMove/Jump, and determines if legal
						// Prompts user on failure
						Player curPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
						int playerRow=-1, playerColumn=-1;
						if(curPlayer.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
							playerRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow()-1;
							playerColumn = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn()-1;
						}
						else if(curPlayer.equals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
							playerRow = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow()-1;
							playerColumn = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn()-1;
						}
						int vertAbsDiff = Math.abs(playerRow- curI);
						int vertDiff = curI-playerRow;
						int horAbsDiff = Math.abs(playerColumn -curJ);
						int horDiff = curJ-playerColumn;
						if(vertAbsDiff+ horAbsDiff>2|| vertAbsDiff>2||horAbsDiff>2||vertAbsDiff+horAbsDiff==0) {
							JOptionPane.showMessageDialog(null, "Illegal move. Please try a different square","Illegal Move",JOptionPane.WARNING_MESSAGE);
							return;
						}
						PawnBehavior.MoveDirection dir = null;
						switch(vertDiff) {

						case 1:
						case 2:
							dir = PawnBehavior.MoveDirection.South;
							ensureNoWallInHand();

							break;
						case -1:
						case -2:
							dir = PawnBehavior.MoveDirection.North;
							ensureNoWallInHand();

						default:
							break;
						}
						switch (horDiff) {
						case -1:
							if (vertDiff < 0) {
								dir = PawnBehavior.MoveDirection.NorthWest;
								ensureNoWallInHand();

								break;
							} else if (vertDiff > 0) {
								dir = PawnBehavior.MoveDirection.SouthWest;
								ensureNoWallInHand();

								break;
							} else
								dir = PawnBehavior.MoveDirection.West;
							ensureNoWallInHand();

							break;
						case -2:
							dir = PawnBehavior.MoveDirection.West;
							ensureNoWallInHand();

							break;
						case 1:
							if (vertDiff < 0) {
								dir = PawnBehavior.MoveDirection.NorthEast;
								ensureNoWallInHand();

								break;
							} else if (vertDiff > 0) {
								dir = PawnBehavior.MoveDirection.SouthEast;
								ensureNoWallInHand();

								break;
							} else {
								dir = PawnBehavior.MoveDirection.East;
								ensureNoWallInHand();
							}
							break;
						case 2:
							dir = PawnBehavior.MoveDirection.East;
							ensureNoWallInHand();

							break;
						default:
							break;
						}
						// If dir isn't set to this point something went horribly wrong
						if(!PawnBehavior.moveOrJump(dir)) {
							JOptionPane.showMessageDialog(null, "Illegal move. Please select a different move", "Illegal Move", JOptionPane.WARNING_MESSAGE);
						}
						//Update Total Time Left Labels
						lblTimeWhite.setText(Controller.displayRemainingTimeWhite());
						lblTimeBlack.setText(Controller.displayRemainingTimeBlack());
						Controller.identifyIfGameWonOrDrawPosition();
						updatePlayerWallAndForfeitButtonsVisually();

					}

					


				});
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = j * 2;
				c.gridy = i * 2;
				c.weightx = 1;
				c.weighty = 1;
				c.ipadx = 10;
				c.ipady = 10;
				c.fill = GridBagConstraints.BOTH;
				
				
				gameBoardPanel.add(tiles[i][j], c);

				if (j < 8) {
					vWalls[i][j] = Box.createVerticalBox();
					vWalls[i][j].setOpaque(true);
					c = new GridBagConstraints();
					c.gridx = j * 2 + 1;
					c.gridy = i * 2;
					c.weightx = 1;
					c.weighty = 1;
					c.ipady = 10;
					c.ipadx = -5;
					c.fill = GridBagConstraints.BOTH;
					gameBoardPanel.add(vWalls[i][j], c);
				}

				if (i < 8) {
					hWalls[i][j] = Box.createHorizontalBox();
					hWalls[i][j].setOpaque(true);
					c = new GridBagConstraints();
					c.gridx = j * 2;
					c.gridy = i * 2 + 1;
					c.weightx = 1;
					c.weighty = 1;
					c.ipadx = 10;
					c.ipady = -5;
					c.fill = GridBagConstraints.BOTH;
					gameBoardPanel.add(hWalls[i][j], c);
				}

				if (i < 8 && j < 8) {
					wallCenters[i][j] = new JButton();
					wallCenters[i][j].setOpaque(true);
					wallCenters[i][j].setBackground(boardBackgroundColor);
					wallCenters[i][j].setBorderPainted(false);
					// For loop helper
					@SuppressWarnings("deprecation")
					final Integer xPos = new Integer(i);
					@SuppressWarnings("deprecation")
					final Integer yPos = new Integer(j);

					wallCenters[i][j].addMouseListener(new MouseAdapter() {


						void setWallToGreen(Integer x, Integer y) {
							if (Controller.returnWallMoveDirection() == Direction.Horizontal) { // Check if the wall in
																								// hand is horizontal
																								// Hover a wall
																								// horizontally
								hWalls[x][y].setBackground(Color.GREEN);
								wallCenters[x][y].setBackground(Color.GREEN);
								hWalls[x][y + 1].setBackground(Color.GREEN);
							} else { // Hover a wall vertically
								vWalls[x][y].setBackground(Color.GREEN);
								wallCenters[x][y].setBackground(Color.GREEN);
								vWalls[x + 1][y].setBackground(Color.GREEN);
							}
						}

						void setWallToBoardBackgroundColor(Integer x, Integer y) {
							if (Controller.returnWallMoveDirection() == Direction.Horizontal) { // Check if the wall in
																								// hand is horizontal
																								// Hover a wall
																								// horizontally
								hWalls[x][y].setBackground(boardBackgroundColor);
								wallCenters[x][y].setBackground(boardBackgroundColor);
								hWalls[x][y + 1].setBackground(boardBackgroundColor);
							} else { // Hover a wall vertically
								vWalls[x][y].setBackground(boardBackgroundColor);
								wallCenters[x][y].setBackground(boardBackgroundColor);
								vWalls[x + 1][y].setBackground(boardBackgroundColor);
							}
						}

						public void mouseEntered(MouseEvent e) {
							Tile currentTile = Controller.getDroppedWallTile(xPos, yPos);
							// If there is a wall in hand
							if (Controller.returnWallMoveCandidate() != null) { 
								// If it s a horizontal wall in hand
								if (Controller.returnWallMoveDirection() == Direction.Horizontal) { 
									// If it is in a valid position
									if (Controller.hoveredWallIsValid(currentTile, Controller.returnWallMoveDirection())) {
										//Set it to Green 
										setWallToGreen(xPos, yPos);
									} 
									// If it is not in a valid position
									else { 
										//Check to see if the conflict is with a horizontal wall, if so set the horizontal wall as red
										if (Controller.returnInvalidWallDirection(currentTile,Direction.Horizontal) == Direction.Horizontal) {
											//Gets horizontal wall coordinates
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Horizontal); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Horizontal); 
											// Set the conflict wall to black
											hWalls[row][column].setBackground(Color.red);
											wallCenters[row][column].setBackground(Color.red);
											hWalls[row][column + 1].setBackground(Color.red);
										}
										//Else if the conflict wall is Vertical, set vertical wall as red 
										else if (Controller.returnInvalidWallDirection(currentTile,Direction.Horizontal) == Direction.Vertical) {
											//Gets Vertical wall coordinates
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Vertical); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Vertical); 
											// Set the conflict wall to black
											vWalls[row][column].setBackground(Color.red);
											wallCenters[row][column].setBackground(Color.red);
											vWalls[row + 1][column].setBackground(Color.red);
										}
										//Else if the conflict is due to blocking the path for either player, set vertical wall as red
                                        else if(!Controller.checkIfPathExists(currentTile)) {
                                            // Sets based on current location
                                            hWalls[xPos][yPos].setBackground(Color.red);
                                            wallCenters[xPos][yPos].setBackground(Color.red);
                                            hWalls[xPos][yPos + 1].setBackground(Color.red);
                                        }
									}
								}
								
								// If its a vertical wall in hand
								else if (Controller.returnWallMoveDirection() == Direction.Vertical) { 
									//If its in a valid position
									if (Controller.hoveredWallIsValid(currentTile,Controller.returnWallMoveDirection()) == true) {
										//Set the vertical wall hover to green
										setWallToGreen(xPos, yPos);
									} 
									// If its not in a valid position
									else {
										//Check to see if the conflict is with a horizontal wall, if so set the horizontal wall as red
										if (Controller.returnInvalidWallDirection(currentTile,Direction.Vertical) == Direction.Horizontal) {
											//Gets horizontal wall coordinates
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Horizontal); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Horizontal); 
											// Set the conflict wall to black
											hWalls[row][column].setBackground(Color.red);
											wallCenters[row][column].setBackground(Color.red);
											hWalls[row][column + 1].setBackground(Color.red);
										}
										//Else if the conflict wall is Vertical, set vertical wall as red 
										else if (Controller.returnInvalidWallDirection(currentTile,Direction.Vertical) == Direction.Vertical) {
											//Gets Vertical wall coordinates
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Vertical); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Vertical); 
											// Set the conflict wall to black
											vWalls[row][column].setBackground(Color.red);
											wallCenters[row][column].setBackground(Color.red);
											vWalls[row + 1][column].setBackground(Color.red);
										}
										//Else if the conflict is due to blocking the path for either player, set vertical wall as red
										else if(!Controller.checkIfPathExists(currentTile)) {
											// Sets based on current location
											vWalls[xPos][yPos].setBackground(Color.red);
											wallCenters[xPos][yPos].setBackground(Color.red);
											vWalls[xPos+1][yPos].setBackground(Color.red);
										}
									}
								}
							} else { // If no wall move candidate, do nothing
								return;
							}
						}

						public void mouseExited(MouseEvent e) {
							Tile currentTile = Controller.getDroppedWallTile(xPos, yPos);
							
							// If there is a wall in hand meaning no wall was placed
							if (Controller.returnWallMoveCandidate() != null) {
								// If wall in hand is horizontal
								if (Controller.returnWallMoveDirection() == Direction.Horizontal) { 
									//If horizontal wall is in a valid position
									if (Controller.hoveredWallIsValid(currentTile, Direction.Horizontal)) {
										//Set it to board background color
										setWallToBoardBackgroundColor(xPos, yPos);
									}
									//Else if it is in an invalid position
									else if (!Controller.hoveredWallIsValid(currentTile, Direction.Horizontal)) {
										//Check to see if it conflicts with a Horizontal Wall
										if (Controller.returnInvalidWallDirection(currentTile,Direction.Horizontal) == Direction.Horizontal) {
											//Set that conflict horizontal wall to black
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Horizontal); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Horizontal); 
											hWalls[row][column].setBackground(placedWallColor);
											wallCenters[row][column].setBackground(placedWallColor);
											hWalls[row][column + 1].setBackground(placedWallColor);
										}
										//Otherwise, it conflicts with a vertical wall
										else if (Controller.returnInvalidWallDirection(currentTile,Direction.Horizontal) == Direction.Vertical) {
											//Set that conflict vertical wall to black
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Vertical); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Vertical); 
											vWalls[row][column].setBackground(placedWallColor);
											wallCenters[row][column].setBackground(placedWallColor);
											vWalls[row + 1][column].setBackground(placedWallColor);
										}
										// Otherwise, the wall blocks the path
										else if(!Controller.checkIfPathExists(currentTile)) {
											// Sets based on current location
											setWallToBoardBackgroundColor(xPos,yPos);
										}
									}
								}
								
								// Else the Wall in hand is Vertical 
								else { 
									//If it is in a valid position
									if (Controller.hoveredWallIsValid(currentTile, Direction.Vertical)) {
										//Set the vertical wall to board background again
										setWallToBoardBackgroundColor(xPos, yPos);
									} 
									//If it is an invalid position
									else if (!Controller.hoveredWallIsValid(currentTile, Direction.Vertical)) {
										//Check to see if it conflicts with a Horizontal Wall
										if (Controller.returnInvalidWallDirection(currentTile,Direction.Vertical) == Direction.Horizontal) {
											//Set that conflict horizontal wall to black
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Horizontal); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Horizontal); 
											hWalls[row][column].setBackground(placedWallColor);
											wallCenters[row][column].setBackground(placedWallColor);
											hWalls[row][column + 1].setBackground(placedWallColor);
										}
										//Otherwise, it conflicts with a vertical wall
										else if (Controller.returnInvalidWallDirection(currentTile,Direction.Vertical) == Direction.Vertical) {
											//Set that conflict vertical wall to black
											int row = Controller.returnInvalidWallRow(currentTile, Direction.Vertical); 
											int column = Controller.returnInvalidWallColumn(currentTile,Direction.Vertical); 
											vWalls[row][column].setBackground(placedWallColor);
											wallCenters[row][column].setBackground(placedWallColor);
											vWalls[row + 1][column].setBackground(placedWallColor);
										}
										// Otherwise, the wall blocks the path
										else if(!Controller.checkIfPathExists(currentTile)) {
											// Sets based on current location
											setWallToBoardBackgroundColor(xPos,yPos);
										}
									}
								}
							}
							
							// If no wall move candidate, it is either 1) No wall was grabbed or 2) Wall was placed
							else { 

								if (Controller.hoveredWallIsValid(currentTile, Direction.Horizontal) == true) {
									hWalls[xPos][yPos].setBackground(boardBackgroundColor);
									wallCenters[xPos][yPos].setBackground(boardBackgroundColor);
									hWalls[xPos][yPos + 1].setBackground(boardBackgroundColor);
								} else if (Controller.hoveredWallIsValid(currentTile, Direction.Vertical) == true) {
									vWalls[xPos][yPos].setBackground(boardBackgroundColor);
									wallCenters[xPos][yPos].setBackground(boardBackgroundColor);
									vWalls[xPos + 1][yPos].setBackground(boardBackgroundColor);
								}

								else if (Controller.hoveredWallIsValid(currentTile, Direction.Horizontal) == false
										|| Controller.hoveredWallIsValid(currentTile, Direction.Vertical) == false) {
									if (Controller.returnInvalidWallDirection(currentTile,
											Direction.Horizontal) == Direction.Horizontal) {
										int row = Controller.returnInvalidWallRow(currentTile, Direction.Horizontal); // Get the row of	 the	 wall it conflicts with
										int column = Controller.returnInvalidWallColumn(currentTile,
												Direction.Horizontal); // Get the column of the wall it conflicts with
										// Set the conflict wall to black
										hWalls[row][column].setBackground(placedWallColor);
										wallCenters[row][column].setBackground(placedWallColor);
										hWalls[row][column + 1].setBackground(placedWallColor);
									}

									else if (Controller.returnInvalidWallDirection(currentTile,
											Direction.Vertical) == Direction.Vertical) {
										int row = Controller.returnInvalidWallRow(currentTile, Direction.Vertical); // Get
																													// the
																													// row
																													// of
																													// the
																													// wall
																													// it
																													// conflicts
																													// with
										int column = Controller.returnInvalidWallColumn(currentTile,
												Direction.Vertical); // Get the column of the wall it conflicts with
										// Set the conflict wall to black
										vWalls[row][column].setBackground(placedWallColor);
										wallCenters[row][column].setBackground(placedWallColor);
										vWalls[row + 1][column].setBackground(placedWallColor);
									}

								}

							}

						}
					});

					wallCenters[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							// Tell user to grab a wall before placing it
							if (Controller.returnWallMoveCandidate() == null)
								QuoridorWindow.notifyGrabAWall();

							if (Controller.returnWallMoveDirection() == Direction.Horizontal) {
								// If drop wall is valid
								Controller.setDroppedWallTileToCandidate(xPos, yPos);

								Wall returnedWall = Controller.dropWall(Controller.returnWallMoveCandidate());

								if (returnedWall != null) {
									// Do drop wall and return the wall placed
									// Make a wall horizontally
									hWalls[xPos][yPos].setBackground(placedWallColor);
									wallCenters[xPos][yPos].setBackground(placedWallColor);
									hWalls[xPos][yPos + 1].setBackground(placedWallColor);
									// Set Target tile to placed wall on board
									isGrabWall = true;

									//Update Total Time Left Labels
									lblTimeWhite.setText(Controller.displayRemainingTimeWhite());
									lblTimeBlack.setText(Controller.displayRemainingTimeBlack());

									updatePlayerWallAndForfeitButtonsVisually();

								} else {
									QuoridorWindow.notifyIllegalWallMove();
								}

							}

							else if (Controller.returnWallMoveDirection() == Direction.Vertical) {

								Controller.setDroppedWallTileToCandidate(xPos, yPos);

								Wall returnedWall = Controller.dropWall(Controller.returnWallMoveCandidate());

								// If drop wall is valid
								if (returnedWall != null) {
									// Do drop wall and return the wall placed
									// Make a wall vertically
									vWalls[xPos][yPos].setBackground(placedWallColor);
									wallCenters[xPos][yPos].setBackground(placedWallColor);
									vWalls[xPos + 1][yPos].setBackground(placedWallColor);

									isGrabWall = true;

									//Update Total Time Left Labels
									lblTimeWhite.setText(Controller.displayRemainingTimeWhite());
									lblTimeBlack.setText(Controller.displayRemainingTimeBlack());

									updatePlayerWallAndForfeitButtonsVisually();

								} else { // No wall move candidate exists
									QuoridorWindow.notifyIllegalWallMove();
								}
							}
						}
					});


					c.gridx = j * 2 + 1;
					c.gridy = i * 2 + 1;
					c.weightx = 1;
					c.weighty = 1;
					c.ipadx = -5;
					c.ipady = -5;
					c.fill = GridBagConstraints.BOTH;
					// TODO: set click event for walls here--eg.dropwall
					gameBoardPanel.add(wallCenters[i][j], c);
				}
			}


		}

			Component horizontalStrut = Box.createHorizontalStrut(100);
			horizontalStrut.setBackground(Color.white);
			horizontalBox.add(horizontalStrut);

			timeRemLabel = new JLabel("Time remaining: 00:00");
			timeRemLabel.setFont(new Font("Cooper Black", Font.PLAIN, 14));
			timeRemLabel.setHorizontalAlignment(SwingConstants.CENTER);
			horizontalBox.add(timeRemLabel);
		
	}


	/**	Moves players graphically
	 *  @author Sam Perreault */
	public void placePlayer(int whitex, int whitey, int blackx, int blacky) {
		tiles[playerView[0]][playerView[1]].setBackground(tileColor);
		tiles[playerView[2]][playerView[3]].setBackground(tileColor);

		playerView[0] = whitex;
		playerView[1] = whitey;
		playerView[2] = blackx;
		playerView[3] = blacky;

		tiles[whitex][whitey].setBackground(Color.white);
		tiles[blackx][blacky].setBackground(Color.black);

	}

	/**
	 * @author arneetkalra
	 */
	public void placeValidPlayerPositions (int whitex, int whitey, int blackx, int blacky) {

		// A work in progress

		if (QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition()
				.getPlayerToMove() == QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()) {
			if (whitex>0) tiles[whitex - 1][whitey].setBackground(validPositionColor);
			if (whitex<9) tiles[whitex + 1][whitey].setBackground(validPositionColor);
			if (whitey<9) tiles[whitex][whitey - 1].setBackground(validPositionColor);
			if (whitex>0) tiles[whitex][whitey + 1].setBackground(validPositionColor);
		}
		else {
			if (blackx>0) tiles[blackx - 1][blacky].setBackground(validPositionColor);
			if (blackx<9) tiles[blackx + 1][blacky].setBackground(validPositionColor);
			if (blacky<9) tiles[blackx][blacky - 1].setBackground(validPositionColor);
			if (blacky>0) tiles[blackx][blacky + 1].setBackground(validPositionColor);

			tiles[whitex][whitey].setBackground(Color.white);
			tiles[blackx][blacky].setBackground(Color.black);
		}
	}

	/** @author Sam Perreault */
	public String getTurnLabel() {
		return currentPlayerName.getText();
	}

	/** @author Sam Perreault */
	public boolean getIsTimerActive() {
		return secondTimer.isRunning();
	}

	/** @author Sam Perreault */
	public void setPlayerNames(String white, String black) {	
		lblBlackPlayerName.setText(blackPawn +" "+ black +" "+blackPawn);
		lblwhitePlayerName.setText(whitePawn +" "+ white +" "+whitePawn);
	}

	/** @author Sam Perreault */
	public void setTimeRemaining(int timeRemaining) {
		timeRemaining /= 1000;
		int minutes, seconds;
		minutes = timeRemaining / 60;
		seconds = timeRemaining % 60;
		// Change text of label to new time
		String min, sec;
		if (minutes / 10 == 0) {
			min = "0" + minutes;
		} else {
			min = "" + minutes;
		}
		if (seconds / 10 == 0) {
			sec = "0" + seconds;
		} else {
			sec = "" + seconds;
		}
		String tr = "Time remaining: " + min + ":" + sec;
		//Changes Time remaining to red if less than 10 seconds left
		if (Integer.parseInt(min) == 0 && Integer.parseInt(sec) <= 10) {
			timeRemLabel.setForeground(Color.RED);
		}
		else {
			timeRemLabel.setForeground(Color.BLACK);
		}
		timeRemLabel.setText(tr);
		
	}

	/** @author Sam Perreault */
	public void setCurrentPlayer(String name) {
		currentPlayerName.setText(name + "'s turn");
	}

	/** @author Sam Perreault */
	public void subtractSecondFromView() {
		// pull text from label
		// get last 2 characters
		// change one by one
		// set new text
		String timeRem = timeRemLabel.getText();
		String minRem = timeRem.substring(timeRem.lastIndexOf(":") - 2, timeRem.lastIndexOf(":"));
		timeRem = timeRem.substring(timeRem.lastIndexOf(":") + 1);
		minRem = minRem.trim();
		timeRem = timeRem.trim();
		int timeRemInt = Integer.parseInt(timeRem) - 1;
		int minRemInt = Integer.parseInt(minRem);
		if (timeRemInt == -1) {
			timeRemInt = 59;
			minRemInt--;
		}
		setTimeRemaining((60 * minRemInt + timeRemInt) * 1000);

	}

	/** @author Sam Perreault */
	public void createSecondTimer() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Controller.gameIsStillRunning()) {

					// Deduct a second from view
					subtractSecondFromView();
					// Deduct a second from model
					Controller.subtractSecond();
					
				}
			}
		};
		if (Controller.gameIsStillRunning()) {
			secondTimer = new Timer(1000, listener);
			secondTimer.setRepeats(true);
			secondTimer.start();
			timerRunning = true;
		}
	}

	/**
	 * @author arneetkalra
	 */
	public static void notifyIllegalWallMove() {
		JOptionPane.showMessageDialog(null, "Illegal Wall Move!", "Do you even have eyes?", JOptionPane.WARNING_MESSAGE);
	}

	/** @author Luke Barber */
	public static void warningNoMoreWalls() {
		JOptionPane.showMessageDialog(null, "No More Walls Available in Stock! You have to move your player instead.", "Uh-oh...", JOptionPane.WARNING_MESSAGE);
	}

	/** @author Luke Barber */
	public static void warningInvalidGrabWall() {
		JOptionPane.showMessageDialog(null, "Already Grabbed Wall!", "You're good to go!", JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * @author arneetkalra
	 */
	public static void notifyNotYourTurn() {
		JOptionPane.showMessageDialog(null, "It's not your turn!", "Wait your turn bro", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * @author arneetkalra
	 */
	public static void notifyNotInReplayMode() {
		JOptionPane.showMessageDialog(null, "Enter Replay Mode if you want to do this!","Not in Replay Mode", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * @author arneetkalra
	 */
	public static void notifyAlreadyInReplayMode() {
		JOptionPane.showMessageDialog(null,"You're already in replay mode!", "Already Done", JOptionPane.PLAIN_MESSAGE);
	}


	

	/** @author Luke Barber */
	public void setWallSelected(boolean selected) {
		wallSelected = selected;
	}

	/** @author Luke Barber */
	public boolean getWallSelected() {
		return this.wallSelected;
	}

	/**
	 * @author arneetkalra
	 */
	public static void notifyGrabAWall() {
		JOptionPane.showMessageDialog(null, "Grab a Wall First!", "", JOptionPane.PLAIN_MESSAGE);
	}

	class ImagePanel extends JPanel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private Image img;

		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		public ImagePanel(Image img) {
			this.img = img;
			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}

		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}

	}

	public void displayWall(int x, int y, Direction direction) {
		if (direction == Direction.Horizontal) {
			hWalls[x][y].setBackground(placedWallColor);
			wallCenters[x][y].setBackground(placedWallColor);
			hWalls[x][y + 1].setBackground(placedWallColor);
		} else {
			vWalls[x][y].setBackground(placedWallColor);
			wallCenters[x][y].setBackground(placedWallColor);
			vWalls[x + 1][y].setBackground(placedWallColor);
		}

	}
	
	/**
	 * @author arneetkalra
	 */
	public void notifyWhiteWon() {
		Image whiteImage= new ImageIcon("src/main/resources/whitePawn.png").getImage().getScaledInstance(180, 300, Image.SCALE_SMOOTH);
		ImageIcon whitePawnIcon = new ImageIcon(whiteImage);

		int finalResultBotton = JOptionPane.YES_NO_CANCEL_OPTION;

		Object[] finalResultOptionButtons = {"Load Game", "Replay Mode", "Home Screen"};
		int finalResult = JOptionPane.showOptionDialog(null, "Congraulations "+lblwhitePlayerName.getText()+" , you win!", "White Wins",
				finalResultBotton,
				JOptionPane.PLAIN_MESSAGE,
				whitePawnIcon,
				finalResultOptionButtons,
				finalResultOptionButtons[2]);

		// Load Game Button
		if (finalResult == JOptionPane.YES_OPTION) {
			//loadGame();
			QuoridorApplication.getQuoridor().delete();
		}
		//Replay mode Button
		else if(finalResult == JOptionPane.NO_OPTION) {
			//Do replay mode here
			setBoardConitionsWhenEnteringReplayMode();
			Controller.initiateReplayMode(Controller.getCurrentGame());
		}
		//Home Screen button
		else {
			homeScreen();
		}

	}
	/**
	 * @author arneetkalra
	 */
	public void notifyBlackWon() {
		Image blackImage= new ImageIcon("src/main/resources/blackPawn.png").getImage().getScaledInstance(180, 300, Image.SCALE_SMOOTH);
		ImageIcon blackPawnIcon = new ImageIcon(blackImage);
		int finalResultBotton = JOptionPane.YES_NO_CANCEL_OPTION;
		Object[] finalResultOptionButtons = {"Load Game", "Replay Mode", "Home Screen"};

		int finalResult = JOptionPane.showOptionDialog(null, "Congraulations "+lblBlackPlayerName.getText()+" , you win!", "Black Wins",
				finalResultBotton, JOptionPane.PLAIN_MESSAGE, blackPawnIcon, finalResultOptionButtons,
				finalResultOptionButtons[2]);

		// Load Game Button
		if (finalResult == JOptionPane.YES_OPTION) {
			//loadGame();
			QuoridorApplication.getQuoridor().delete();
			

		}
		// Replay mode Button
		else if (finalResult == JOptionPane.NO_OPTION) {
			//Do replay mode here
			setBoardConitionsWhenEnteringReplayMode();
			Controller.initiateReplayMode(Controller.getCurrentGame());

		}
		// Home Screen button
		else {
			homeScreen();
		}
	}

	/**
	 * @author arneetkalra
	 */
	public void notifyDraw() {
		Image drawImage= new ImageIcon("src/main/resources/draw.png").getImage().getScaledInstance(180, 300, Image.SCALE_SMOOTH);
		ImageIcon drawIcon = new ImageIcon(drawImage);
		int finalResultBotton = JOptionPane.YES_NO_CANCEL_OPTION;

		Object[] finalResultOptionButtons = {"Load Game", "Replay Mode", "Home Screen"};

		int finalResult = JOptionPane.showOptionDialog(null, "What a Game! It's a draw!", "Draw",
				finalResultBotton,
				JOptionPane.PLAIN_MESSAGE,
				drawIcon,
				finalResultOptionButtons, finalResultOptionButtons[2]);

		// Load Game Button
		if (finalResult == JOptionPane.YES_OPTION) {
			loadGame();
		}
		// Replay mode Button
		else if (finalResult == JOptionPane.NO_OPTION) {
			//Do replay mode here
			setBoardConitionsWhenEnteringReplayMode();
			Controller.initiateReplayMode(Controller.getCurrentGame());
		}
		// Home Screen button
		else {
			homeScreen();
		}
	}

	/**
	 * @author arneetkalra
	 */
	public void homeScreen() {
		//Reset board if it was in replay mode when new game was pressed
		setBoardConitionsWhenExitingReplayMode();
		//Reset Previous inputs
		player1Field.setText("");
		player2Field.setText("");
		minuteField.setText("");
		secondField.setText("");

		//Reset the board:
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// Erase Colouring of board
				wallCenters[i][j].setBackground(boardBackgroundColor);
				hWalls[i][j].setBackground(boardBackgroundColor);
				vWalls[i][j].setBackground(boardBackgroundColor);
			}
		}

		//Reset Labels
		lblTimeWhite.setText("   ");
		lblTimeBlack.setText("   ");
		lblWallsLeftBlack.setText("Walls Left = 10");
		lblWallsLeftWhite.setText("Walls Left = 10");

		// Go back to main Screen
		CardLayout layout = (CardLayout) (contentPane.getLayout());
		layout.show(contentPane, "titleScreenPanel");
		// Delete all application data
		Controller.destroyCurrentGame();
	}

	/**
	 * @author arneetkalra
	 */
	public void loadGame() {
		homeScreen();

		Controller.startNewGame();
		Controller.initBlackPlayer("Black");
		Controller.initWhitePlayer("White");
		Controller.setTotalThinkingTime("00:03:00");
		Controller.startClock();
		Controller.createBoard();
		Controller.initializeBoard();
		boolean wrong = false;
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			try {
				Controller.loadGame(selectedFile.getName());
			} catch (UnsupportedOperationException e) {
				JFrame f = new JFrame();
				JTextField tf1;
				JButton b1;
				tf1 = new JTextField();
				tf1.setText("Cannot load game due to invalid position");
				tf1.setEditable(false);
				b1 = new JButton("OK");
				tf1.setBounds(50, 100, 350, 50);
				b1.setBounds(100, 200, 100, 50);
				f.getContentPane().add(tf1);
				f.getContentPane().add(b1);
				f.setSize(400, 400);
				f.getContentPane().setLayout(null);
				f.setVisible(true);
				b1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent b) {
						f.setVisible(false);
					}
				});
				wrong = true;
				e.printStackTrace();
			}
		}
		if (!wrong) {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			for (int i = 0; i < quoridor.getCurrentGame().getCurrentPosition()
					.numberOfBlackWallsOnBoard(); i++) {
				WallMove move = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(i)
						.getMove();
				QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),
						move.getTargetTile().getColumn(), move.getWallDirection());
			}
			for (int i = 0; i < quoridor.getCurrentGame().getCurrentPosition()
					.numberOfWhiteWallsOnBoard(); i++) {
				WallMove move = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(i)
						.getMove();
				QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),
						move.getTargetTile().getColumn(), move.getWallDirection());
			}
			CardLayout layout = (CardLayout) (contentPane.getLayout());
			layout.show(contentPane, "activeGamePanel");
		} else {
			Quoridor quoridor = QuoridorApplication.getQuoridor();
			quoridor.delete();
			quoridor = new Quoridor();
		}
	}
/**
	 * @author William Wang
	 */
	public void updatePositions() {

		//Reset the board:
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// Erase Colouring of board
				wallCenters[i][j].setBackground(boardBackgroundColor);
				hWalls[i][j].setBackground(boardBackgroundColor);
				vWalls[i][j].setBackground(boardBackgroundColor);
			}
		}

		Quoridor quoridor = QuoridorApplication.getQuoridor();
		GamePosition currentPosition = quoridor.getCurrentGame().getCurrentPosition();

		PlayerPosition WhitePosition = currentPosition.getWhitePosition();
		PlayerPosition BlackPosition = currentPosition.getBlackPosition();
		List<Wall> whiteWalls = currentPosition.getWhiteWallsOnBoard();
		List<Wall> blackWalls = currentPosition.getBlackWallsOnBoard();

		placePlayer(WhitePosition.getTile().getRow() - 1, WhitePosition.getTile().getColumn() - 1,
				BlackPosition.getTile().getRow() - 1, BlackPosition.getTile().getColumn() - 1);
		for (Wall wall : whiteWalls) {
			displayWall(wall.getMove().getTargetTile().getRow() - 1, wall.getMove().getTargetTile().getColumn() - 1,
					wall.getMove().getWallDirection());
		}
		for (Wall wall : blackWalls) {
			displayWall(wall.getMove().getTargetTile().getRow() - 1, wall.getMove().getTargetTile().getColumn() - 1,
					wall.getMove().getWallDirection());
		}
	}

	/**
	 * @author arneetkalra
	 */
	public static void setBoardConitionsWhenEnteringReplayMode() {

		//Set return buttons to visible
		btnReplayMode.setVisible(false);
		btnReplayForwards.setVisible(true);
		btnReplayBackwards.setVisible(true);
		btnContinuePlaying.setVisible(true);
		btnReplayJumpToStart.setVisible(true);
		btnReplayJumpToFinal.setVisible(true);

		//Make board unplayable for now
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j].setEnabled(false);
			}
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				wallCenters[i][j].setEnabled(false);
			}
		}
		btnGrabButtonBlack.setEnabled(false);
		btnGrabButtonWhite.setEnabled(false);
		btnRotateWallBlack.setEnabled(false);
		btnRotateWallWhite.setEnabled(false);
		btnResignGameBlack.setEnabled(false);
		btnResignGameWhite.setEnabled(false);

		//Set flag to true
		setInReplayMode(true);

		//Replay method goes here
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Replay);
	}
	/**
	 * @author arneetkalra
	 */
	public static void setBoardConitionsWhenExitingReplayMode() {
	btnReplayMode.setVisible(true);
	btnReplayForwards.setVisible(false);
	btnReplayBackwards.setVisible(false);
	btnContinuePlaying.setVisible(false);
	btnReplayJumpToStart.setVisible(false);
	btnReplayJumpToFinal.setVisible(false);


	//Make board unplayable for now
	for (int i = 0; i < 9; i++) {
		for (int j = 0; j < 9; j++) {
			tiles[i][j].setEnabled(true);
		}
	}

	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			wallCenters[i][j].setEnabled(true);
		}
	}
	btnGrabButtonBlack.setEnabled(true);
	btnGrabButtonWhite.setEnabled(true);
	btnRotateWallBlack.setEnabled(true);
	btnRotateWallWhite.setEnabled(true);
	btnResignGameBlack.setEnabled(true);
	btnResignGameWhite.setEnabled(true);

	//Set flag to false
	setInReplayMode(false);
	//Go back to playing mode , put continue method here
	QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
	}


	/**
	 * @author arneetkalra
	 * @return
	 */
	public static boolean isNotReplayMode() {
		return inReplayMode;
	}

	/**
	 * @author arneetkalra
	 * @param inReplayMode
	 * @return
	 */
	public static boolean setInReplayMode(boolean inReplayMode) {
		QuoridorWindow.inReplayMode = inReplayMode;
		return inReplayMode;
	}
	
	/**
	 * @author arneetkalra
	 */
	private void updatePlayerWallAndForfeitButtonsVisually() {
		if (Controller.isBlackPlayerTurn()) {
			btnGrabButtonBlack.setBackground(mainScreenBackgroundColor);
			btnRotateWallBlack.setBackground(mainScreenBackgroundColor);
			btnResignGameBlack.setBackground(mainScreenBackgroundColor);
			btnGrabButtonBlack.setForeground(placedWallColor);
			btnRotateWallBlack.setForeground(placedWallColor);
			btnResignGameBlack.setForeground(placedWallColor);
			btnGrabButtonBlack.setOpaque(true);
			btnRotateWallBlack.setOpaque(true);
			btnResignGameBlack.setOpaque(true);
			btnGrabButtonBlack.setBorderPainted(false);
			btnRotateWallBlack.setBorderPainted(false);
			btnResignGameBlack.setBorderPainted(false);
		} else {
			btnGrabButtonBlack.setOpaque(false);
			btnRotateWallBlack.setOpaque(false);
			btnResignGameBlack.setOpaque(false);
			btnGrabButtonBlack.setBorderPainted(true);
			btnRotateWallBlack.setBorderPainted(true);
			btnResignGameBlack.setBorderPainted(true);
			btnGrabButtonBlack.setForeground(Color.black);
			btnRotateWallBlack.setForeground(Color.black);
			btnResignGameBlack.setForeground(Color.black);
		}
		
		if (Controller.isWhitePlayerTurn()) {
			btnGrabButtonWhite.setBackground(mainScreenBackgroundColor);
			btnRotateWallWhite.setBackground(mainScreenBackgroundColor);
			btnResignGameWhite.setBackground(mainScreenBackgroundColor);
			btnGrabButtonWhite.setForeground(placedWallColor);
			btnRotateWallWhite.setForeground(placedWallColor);
			btnResignGameWhite.setForeground(placedWallColor);
			btnGrabButtonWhite.setOpaque(true);
			btnRotateWallWhite.setOpaque(true);
			btnResignGameWhite.setOpaque(true);
			btnGrabButtonWhite.setBorderPainted(false);
			btnRotateWallWhite.setBorderPainted(false);
			btnResignGameWhite.setBorderPainted(false);
		} else {
			btnGrabButtonWhite.setOpaque(false);
			btnRotateWallWhite.setOpaque(false);
			btnResignGameWhite.setOpaque(false);
			btnGrabButtonWhite.setBorderPainted(true);
			btnRotateWallWhite.setBorderPainted(true);
			btnResignGameWhite.setBorderPainted(true);
			btnGrabButtonWhite.setForeground(Color.black);
			btnRotateWallWhite.setForeground(Color.black);
			btnResignGameWhite.setForeground(Color.black);
		}
	}
	
	public void ensureNoWallInHand() {
		if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() != null) {
			Controller.returnWallToPlayer();
			lblWallsLeftWhite.setText("Walls Left = " + QuoridorApplication.getQuoridor().getCurrentGame()
					.getCurrentPosition().numberOfWhiteWallsInStock());
			lblWallsLeftBlack.setText("Walls Left = " + QuoridorApplication.getQuoridor().getCurrentGame()
					.getCurrentPosition().numberOfBlackWallsInStock());
		}
	}

}
