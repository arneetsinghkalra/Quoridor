package ca.mcgill.ecse223.quoridor.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorPersistence;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class QuoridorWindow extends JFrame {

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
	private JLabel blackPlayerName;
	private JLabel whitePlayerName;
	private static boolean confirms = true;
	private int[] playerView = { 0, 0, 0, 0 };
	private static JFrame f;

	// for the boards,tiles, and walls
	private JButton[][] tiles = new JButton[9][9];
	private JButton[][] wallCenters = new JButton[8][8];
	private Box[][] hWalls = new Box[9][9];
	private Box[][] vWalls = new Box[9][9];

	/**
	 * Create the frame.
	 */
	public QuoridorWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 90, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		// JPanel titleScreenPanel = new JPanel();
		// titleScreenPanel.setBackground(new Color(255, 255, 0));

		ImagePanel titleScreenPanel = new ImagePanel(new ImageIcon("src/main/resources/quoridor.png").getImage());
		contentPane.add(titleScreenPanel, "name_1049600133434900");
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
						Controller.loadPosition(selectedFile.getName());
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
					Quoridor quoridor = QuoridorApplication.getQuordior();
					for(int i = 0;i<quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsOnBoard();i++) {
				        WallMove move = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(i).getMove();
				        QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),move.getTargetTile().getColumn(),move.getWallDirection());
				       }
				       for(int i = 0;i<quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsOnBoard();i++) {
				        WallMove move = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(i).getMove();
				        QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),move.getTargetTile().getColumn(),move.getWallDirection());
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
		contentPane.add(activeGamePanel, "activeGamePanel");
		activeGamePanel.setLayout(new BorderLayout(0, 0));

		Box verticalBox_2 = Box.createVerticalBox();
		verticalBox_2.setBackground(Color.PINK);
		activeGamePanel.add(verticalBox_2, BorderLayout.WEST);

		blackPlayerName = new JLabel("Black Player");
		blackPlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		blackPlayerName.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_2.add(blackPlayerName);
		blackPlayerName.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		JLabel lblWallsLeft = new JLabel("Walls Left = ");
		lblWallsLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWallsLeft.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_2.add(lblWallsLeft);

		JLabel lblTimeLeft = new JLabel("     Total Time Left:     ");
		lblTimeLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTimeLeft.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_2.add(lblTimeLeft);

		JLabel lblNewLabel_2 = new JLabel(" ;");
		lblNewLabel_2.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_2.add(lblNewLabel_2);

		Box verticalBox_3 = Box.createVerticalBox();
		verticalBox_3.setBackground(Color.PINK);
		activeGamePanel.add(verticalBox_3, BorderLayout.EAST);
		whitePlayerName = new JLabel("White Player");
		whitePlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		whitePlayerName.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_3.add(whitePlayerName);

		JLabel lblWallsLeft_1 = new JLabel("Walls Left = ");
		lblWallsLeft_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWallsLeft_1.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_3.add(lblWallsLeft_1);

		JLabel lblTotalTimeLeft = new JLabel("     Total Time Left:     ");
		lblTotalTimeLeft.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTotalTimeLeft.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_3.add(lblTotalTimeLeft);

		JLabel label_2 = new JLabel(";");
		label_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		label_2.setAlignmentY(Component.TOP_ALIGNMENT);
		label_2.setFont(new Font("Raanana", Font.PLAIN, 13));
		verticalBox_3.add(label_2);

		Box gameOptionBox = Box.createVerticalBox();
		activeGamePanel.add(gameOptionBox, BorderLayout.SOUTH);

		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBackground(Color.PINK);
		gameOptionBox.add(horizontalBox);

		/** @author Luke Barber */
		JButton btnGrabButton = new JButton("Grab wall");
		btnGrabButton.addActionListener(new ActionListener() {
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
							lblWallsLeft_1.setText("Walls Left = "
									+ quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
						}
					} else if (currentPlayer.equals(quoridor.getCurrentGame().getBlackPlayer())) {
						if (quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size() == 0) {
							warningNoMoreWalls();
						} else {
							Controller.grabWall(quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock(
									quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsInStock() - 1));
							lblWallsLeft.setText("Walls Left = "
									+ quoridor.getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
						}
					}
					isGrabWall = false;
					setWallSelected(true);
				} else {
					warningInvalidGrabWall();
				}
			}

		});
		horizontalBox.add(btnGrabButton);

		Box verticalBox = Box.createVerticalBox();
		horizontalBox.add(verticalBox);

		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setBackground(Color.PINK);
		verticalBox.add(btnNewGame);
		btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnSaveGame = new JButton("Save Game");
		verticalBox.add(btnSaveGame);
		btnSaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnLoadGame = new JButton("Load Game");
		verticalBox.add(btnLoadGame);

		btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						Controller.loadPosition(selectedFile.getName());
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
					}
					Quoridor quoridor = QuoridorApplication.getQuordior();
					for(int i = 0;i<quoridor.getCurrentGame().getCurrentPosition().numberOfBlackWallsOnBoard();i++) {
				        WallMove move = quoridor.getCurrentGame().getCurrentPosition().getBlackWallsOnBoard(i).getMove();
				        QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),move.getTargetTile().getColumn(),move.getWallDirection());
				       }
				       for(int i = 0;i<quoridor.getCurrentGame().getCurrentPosition().numberOfWhiteWallsOnBoard();i++) {
				        WallMove move = quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard(i).getMove();
				        QuoridorApplication.quoridorWindow.displayWall(move.getTargetTile().getRow(),move.getTargetTile().getColumn(),move.getWallDirection());
				       }
				}
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
						Path path = Paths.get("src/test/resources/savePosition/" + tf1.getText());
						if (Files.exists(path)) {
							JFrame f = new JFrame();
							JTextField tf1;
							JButton b1;
							JButton b2;
							tf1 = new JTextField();
							tf1.setText("confirms to overwrite");
							tf1.setBounds(50, 50, 150, 20);
							tf1.setEditable(false);
							b1 = new JButton("Yes");
							b1.setBounds(50, 200, 100, 50);
							b2 = new JButton("No");
							b2.setBounds(150, 200, 100, 50);
							f.getContentPane().add(tf1);
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
										Controller.savePosition(tf1.getText(),
												QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(),
												confirms);
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
								Controller.savePosition(tf1.getText(),
										QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(),
										confirms);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}

		});

		/** @author Luke Barber */
		JButton btnRotateButton = new JButton("Rotate Wall");
		btnRotateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				Controller.rotateWall(quoridor.getCurrentGame().getWallMoveCandidate().getWallPlaced());
				JOptionPane.showMessageDialog(null,
						"The current direction is "
								+ quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection(),
						"", JOptionPane.WARNING_MESSAGE);
			}
		});

		horizontalBox.add(btnRotateButton);
		Box horizontalBox_1 = Box.createHorizontalBox();
		gameOptionBox.add(horizontalBox_1);

		Box titleTimeBox = Box.createVerticalBox();
		titleTimeBox.setBackground(Color.PINK);
		activeGamePanel.add(titleTimeBox, BorderLayout.NORTH);

		JLabel gameTitleLabel = new JLabel("QUORIDOR");
		gameTitleLabel.setAlignmentX(0.5f);
		gameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameTitleLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		titleTimeBox.add(gameTitleLabel);

		currentPlayerName = new JLabel("Current Player : ");
		currentPlayerName.setAlignmentX(0.4f);
		currentPlayerName.setAlignmentY(1.5f);
		currentPlayerName.setFont(new Font("Raanana", Font.PLAIN, 13));
		titleTimeBox.add(currentPlayerName);

		// board
		JPanel gameBoardPanel = new JPanel();
		gameBoardPanel.setBackground(Color.PINK);
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

				tiles[i][j] = new JButton();
				tiles[i][j].setBackground(Color.YELLOW);
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = j * 2;
				c.gridy = i * 2;
				c.weightx = 1;
				c.weighty = 1;
				c.ipadx = 10;
				c.ipady = 10;
				c.fill = GridBagConstraints.BOTH;
				// set click event for tiles here(eg.movepawn)
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
					wallCenters[i][j].setBackground(Color.white);
					// For loop helper
					@SuppressWarnings("deprecation")
					final Integer newI = new Integer(i);
					@SuppressWarnings("deprecation")
					final Integer newJ = new Integer(j);
					wallCenters[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
			
							if (Controller.returnWallMoveDirection() == Direction.Horizontal) {
								// If drop wall is valid
								Controller.setDroppedWallTile(newI, newJ);
								
								Wall returnedWall = Controller.dropWall(Controller.returnWallMoveCandidate());
								
								if (returnedWall != null) {
									// Do drop wall and return the wall placed
									// Make a wall horizontally
									hWalls[newI][newJ].setBackground(Color.black);
									wallCenters[newI][newJ].setBackground(Color.black);
									hWalls[newI][newJ + 1].setBackground(Color.black);
									// Set Target tile to placed wall on board
									isGrabWall = true;
								} else {
									QuoridorWindow.notifyIllegalWallMove();
								}

							} else if (Controller.returnWallMoveDirection() == Direction.Vertical) {
								
								Controller.setDroppedWallTile(newI, newJ);

								Wall returnedWall = Controller.dropWall(Controller.returnWallMoveCandidate());

								// If drop wall is valid
								if (returnedWall != null) {
									// Do drop wall and return the wall placed
									// Make a wall vertically
									vWalls[newI][newJ].setBackground(Color.black);
									wallCenters[newI][newJ].setBackground(Color.black);
									vWalls[newI + 1][newJ].setBackground(Color.black);
								
									isGrabWall = true;
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
		horizontalStrut.setBackground(Color.PINK);
		horizontalBox.add(horizontalStrut);

		timeRemLabel = new JLabel("Time remaining: 00:00");
		timeRemLabel.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		timeRemLabel.setHorizontalAlignment(SwingConstants.CENTER);
		horizontalBox.add(timeRemLabel);
	}

	public void movePlayer(int whitex, int whitey, int blackx, int blacky) {
		int blackChar = 0x2B24;
		int whiteChar = 0x20DD;
		tiles[whitex][whitey].setText("O");
		tiles[blackx][blacky].setText("" + (char) blackChar);

		tiles[playerView[0]][playerView[1]].setText("");
		tiles[playerView[2]][playerView[3]].setText("");

		playerView[0] = whitex;
		playerView[1] = whitey;
		playerView[2] = blackx;
		playerView[3] = blacky;

	}

	public String getTurnLabel() {
		return currentPlayerName.getText();
	}

	public boolean getIsTimerActive() {
		return secondTimer.isRunning();
	}
	
	public void setPlayerNames(String white, String black)
	{
		blackPlayerName.setText(black);
		whitePlayerName.setText(white);
	}

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
		timeRemLabel.setText(tr);
	}

	public void setCurrentPlayer(String name) {
		currentPlayerName.setText(name + "'s turn");
	}

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

	public void createSecondTimer() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Deduct a second from view
				subtractSecondFromView();
				// Deduct a second from model
				Controller.subtractSecond();
			}
		};

		secondTimer = new Timer(1000, listener);
		secondTimer.setRepeats(true);
		secondTimer.start();
	}

	/**
	 * @author arneetkalra
	 */
	public static void notifyIllegalWallMove() {
		JOptionPane.showMessageDialog(null, "Illegal Wall Move!", "", JOptionPane.WARNING_MESSAGE);
	}

	/** @author Luke Barber */
	public static void warningNoMoreWalls() {
		JOptionPane.showMessageDialog(null, "No More Walls Available in Stock!", "", JOptionPane.WARNING_MESSAGE);
	}

	/** @author Luke Barber */
	public static void warningInvalidGrabWall() {
		JOptionPane.showMessageDialog(null, "Already Grabbed Wall!", "", JOptionPane.WARNING_MESSAGE);
	}

	/** @author Luke Barber */
	public void setWallSelected(boolean selected) {
		wallSelected = selected;
	}

	/** @author Luke Barber */
	public boolean getWallSelected() {
		return this.wallSelected;
	}

	// TODO add action/listener methods to actually progress the game and all that

	class ImagePanel extends JPanel {

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
	public void displayWall(int x, int y,Direction direction) {
		  if(direction == Direction.Horizontal) {
		   hWalls[x][y].setBackground(Color.black);
		   wallCenters[x][y].setBackground(Color.black);
		   hWalls[x][y + 1].setBackground(Color.black);
		  }
		  else {
		   vWalls[x][y].setBackground(Color.black);
		   wallCenters[x][y].setBackground(Color.black);
		   vWalls[x+1][y].setBackground(Color.black);
		  }

		 }
}
