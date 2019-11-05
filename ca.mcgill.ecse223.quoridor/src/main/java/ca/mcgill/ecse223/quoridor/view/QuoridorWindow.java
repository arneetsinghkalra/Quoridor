package ca.mcgill.ecse223.quoridor.view;

import javax.swing.*;

import javax.swing.border.EmptyBorder;

import javax.swing.border.LineBorder;

import javax.swing.filechooser.FileSystemView;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;

import ca.mcgill.ecse223.quoridor.controller.Controller;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Wall;

import java.awt.*;

import java.awt.event.ActionListener;

import java.io.File;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.awt.event.ActionEvent;

public class QuoridorWindow extends JFrame {

	private static JPanel contentPane;

	private JTextField player1NameField;

	private JTextField textField_1;

	private JTextField minuteField;

	private JTextField secondField;

	public Boolean wallSelected = false;

	private Timer secondTimer;

	private JLabel turnLabel;

	private JLabel timeRemLabel;

	private static boolean confirms = true;

	private static JFrame f;

	// for the boards,tiles, and walls

	private JButton[][] tiles = new JButton[9][9];

	private JButton[][] wallCenters = new JButton[8][8];

	private Box[][] hWalls = new Box[9][9];

	private Box[][] vWalls = new Box[9][9];

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {

					QuoridorWindow frame = new QuoridorWindow();

					frame.setVisible(true);

				} catch (Exception e) {

					e.printStackTrace();

				}

			}

		});

	}

	/**
	 * Create the frame.
	 */

	public QuoridorWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 600, 450);

		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		contentPane.setLayout(new CardLayout(0, 0));

		JPanel titleScreenPanel = new JPanel();

		titleScreenPanel.setBackground(new Color(255, 255, 0));

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

		sl_titleScreenPanel.putConstraint(SpringLayout.NORTH, newGameButton, 131, SpringLayout.NORTH, titleScreenPanel);

		sl_titleScreenPanel.putConstraint(SpringLayout.WEST, newGameButton, 74, SpringLayout.WEST, titleScreenPanel);

		newGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		titleScreenPanel.add(newGameButton);

		JButton loadGameButton = new JButton("Load Game");

		sl_titleScreenPanel.putConstraint(SpringLayout.NORTH, loadGameButton, 207, SpringLayout.NORTH,
				titleScreenPanel);

		sl_titleScreenPanel.putConstraint(SpringLayout.WEST, loadGameButton, 74, SpringLayout.WEST, titleScreenPanel);

		sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, loadGameButton, -124, SpringLayout.SOUTH,
				titleScreenPanel);

		sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, newGameButton, -6, SpringLayout.NORTH, loadGameButton);

		sl_titleScreenPanel.putConstraint(SpringLayout.EAST, newGameButton, 0, SpringLayout.EAST, loadGameButton);

		loadGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		loadGameButton.addActionListener(new ActionListener()

		{

			public void actionPerformed(ActionEvent a)

			{

				Controller.initializeBoard();

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = jfc.getSelectedFile();

					try {

						Controller.loadPosition(selectedFile.getName());

					} catch (UnsupportedOperationException e) {

						JDialog d = new JDialog(f, "Cannot load game due to invalid position");

						d.setVisible(true);

					}

				}

			}

		});

		titleScreenPanel.add(loadGameButton);

		JLabel titleLabel = new JLabel("QUORIDOR");

		sl_titleScreenPanel.putConstraint(SpringLayout.EAST, loadGameButton, -57, SpringLayout.WEST, titleLabel);

		sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, titleLabel, -187, SpringLayout.SOUTH, titleScreenPanel);

		sl_titleScreenPanel.putConstraint(SpringLayout.EAST, titleLabel, -63, SpringLayout.EAST, titleScreenPanel);

		titleLabel.setFont(new Font("Cooper Black", Font.PLAIN, 40));

		titleScreenPanel.add(titleLabel);

		JPanel activeGamePanel = new JPanel();

		contentPane.add(activeGamePanel, "activeGamePanel");

		activeGamePanel.setLayout(new BorderLayout(0, 0));

		Box verticalBox_2 = Box.createVerticalBox();

		activeGamePanel.add(verticalBox_2, BorderLayout.WEST);

		JLabel lblNewLabel = new JLabel("Black Player");

		verticalBox_2.add(lblNewLabel);

		lblNewLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		JLabel lblWallsLeft = new JLabel("Walls Left = ");

		verticalBox_2.add(lblWallsLeft);

		JLabel lblTimeLeft = new JLabel("Time Left: ");

		verticalBox_2.add(lblTimeLeft);

		JLabel lblNewLabel_2 = new JLabel("New label");

		verticalBox_2.add(lblNewLabel_2);

		Box verticalBox_3 = Box.createVerticalBox();

		activeGamePanel.add(verticalBox_3, BorderLayout.EAST);

		JLabel lblNewLabel_1 = new JLabel("White Player");

		verticalBox_3.add(lblNewLabel_1);

		JLabel label = new JLabel("Walls Left = ");

		verticalBox_3.add(label);

		JLabel label_1 = new JLabel("Time Left: ");

		verticalBox_3.add(label_1);

		JLabel label_2 = new JLabel("New label");

		verticalBox_3.add(label_2);

		Box gameOptionBox = Box.createVerticalBox();

		activeGamePanel.add(gameOptionBox, BorderLayout.SOUTH);

		Box horizontalBox = Box.createHorizontalBox();

		gameOptionBox.add(horizontalBox);

		/** @author Luke Barber */
		JButton btnGrabButton = new JButton("Grab wall");
		btnGrabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quoridor quoridor = QuoridorApplication.getQuoridor();
				if (quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size() == 0) {
					warningNoMoreWalls();
				} else {
					Controller.grabWall(quoridor.getCurrentGame().getCurrentPosition().getWhiteWallsInStock(0));
					setWallSelected(true);
				}
			}

		});
		horizontalBox.add(btnGrabButton);

		Box verticalBox = Box.createVerticalBox();

		horizontalBox.add(verticalBox);

		JButton btnNewGame = new JButton("New Game");

		verticalBox.add(btnNewGame);

		btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnSaveGame = new JButton("Save Game");

		verticalBox.add(btnSaveGame);

		btnSaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnLoadGame = new JButton("Load Game");

		verticalBox.add(btnLoadGame);

		btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnLoadGame.addActionListener(new ActionListener()

		{

			public void actionPerformed(ActionEvent a)

			{

				Controller.initializeBoard();

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

						b1.setBounds(100, 200, 100, 50);

						f.getContentPane().add(tf1);

						f.getContentPane().add(b1);

						f.setSize(300, 300);

						f.getContentPane().setLayout(null);

						b1.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent b) {

								f.setVisible(false);

							}

						});

					}

				}

			}

		});

		btnSaveGame.addActionListener(new ActionListener()

		{

			public void actionPerformed(ActionEvent a)

			{

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

		activeGamePanel.add(titleTimeBox, BorderLayout.NORTH);

		JLabel gameTitleLabel = new JLabel("QUORIDOR");

		gameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		gameTitleLabel.setFont(new Font("Cooper Black", Font.PLAIN, 40));

		titleTimeBox.add(gameTitleLabel);

		JLabel currentPlayerName = new JLabel("Current Player : ");

		titleTimeBox.add(currentPlayerName);

//board

		JPanel gameBoardPanel = new JPanel();

		activeGamePanel.add(gameBoardPanel, BorderLayout.CENTER);

		gameBoardPanel.setLayout(new GridBagLayout());

		JPanel setupPanel = new JPanel();

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

		player1NameField = new JTextField();

		player1HorBox.add(player1NameField);

		player1NameField.setColumns(10);

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

		textField_1 = new JTextField();

		player2HorBox.add(textField_1);

		textField_1.setColumns(10);

		Component rigidArea_2 = Box.createRigidArea(new Dimension(10, 10));

		player2HorBox.add(rigidArea_2);

		Component rigidArea = Box.createRigidArea(new Dimension(10, 10));

		player1NameBox.add(rigidArea);

		Box thinkingTimeBox = Box.createVerticalBox();

		sl_setupPanel.putConstraint(SpringLayout.NORTH, thinkingTimeBox, 63, SpringLayout.SOUTH, player1NameBox);

		sl_setupPanel.putConstraint(SpringLayout.WEST, thinkingTimeBox, 0, SpringLayout.WEST, player1NameBox);

		setupPanel.add(thinkingTimeBox);

		JLabel setTimerLabel = new JLabel("Set Timer Amount");

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

		JComboBox defaultNamesComboBox = new JComboBox();

		sl_setupPanel.putConstraint(SpringLayout.NORTH, defaultNamesComboBox, 0, SpringLayout.NORTH, player1NameBox);

		sl_setupPanel.putConstraint(SpringLayout.WEST, defaultNamesComboBox, 29, SpringLayout.EAST, player1NameBox);

		sl_setupPanel.putConstraint(SpringLayout.SOUTH, defaultNamesComboBox, 144, SpringLayout.NORTH, setupPanel);

		sl_setupPanel.putConstraint(SpringLayout.EAST, defaultNamesComboBox, 189, SpringLayout.EAST, player1NameBox);

		defaultNamesComboBox.setFont(new Font("Cooper Black", Font.PLAIN, 14));

		defaultNamesComboBox.setModel(new DefaultComboBoxModel(new String[] { "or Choose a Player" }));

		setupPanel.add(defaultNamesComboBox);

		JButton startGameButton = new JButton("Start Game");

		sl_setupPanel.putConstraint(SpringLayout.SOUTH, startGameButton, 0, SpringLayout.SOUTH, thinkingTimeBox);

		sl_setupPanel.putConstraint(SpringLayout.EAST, startGameButton, -55, SpringLayout.EAST, setupPanel);

		startGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		startGameButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				CardLayout layout = (CardLayout) (contentPane.getLayout());

				layout.show(contentPane, "activeGamePanel");

			}

		});

		setupPanel.add(startGameButton);

		JComboBox comboBox = new JComboBox();

		sl_setupPanel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, defaultNamesComboBox);

		sl_setupPanel.putConstraint(SpringLayout.WEST, comboBox, 0, SpringLayout.WEST, defaultNamesComboBox);

		sl_setupPanel.putConstraint(SpringLayout.EAST, comboBox, 0, SpringLayout.EAST, defaultNamesComboBox);

		comboBox.setFont(new Font("Dialog", Font.PLAIN, 14));

		setupPanel.add(comboBox);

		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				tiles[i][j] = new JButton();

				tiles[i][j].setBackground(Color.white);

				GridBagConstraints c = new GridBagConstraints();

				c.gridx = j * 2;

				c.gridy = i * 2;

				c.weightx = 1;

				c.weighty = 1;

				c.ipadx = 10;

				c.ipady = 10;

				c.fill = GridBagConstraints.BOTH;

//set click event for tiles here(eg.movepawn)

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

					wallCenters[i][j].setBackground(Color.lightGray);
					// For loop helper
					@SuppressWarnings("deprecation")
					final Integer newI = new Integer(i);
					@SuppressWarnings("deprecation")
					final Integer newJ = new Integer(j);

					wallCenters[i][j].addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {

							// Drop Wall
							Controller.dropWall(
									QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate());
							// If horizontal, highlight horizontal walls
							if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
									.getWallDirection() == Direction.Horizontal) {
								hWalls[newI][newJ].setBackground(Color.black);

								wallCenters[newI][newJ].setBackground(Color.black);

								hWalls[newI][newJ + 1].setBackground(Color.black);
							}

							else if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate()
									.getWallDirection() == Direction.Horizontal) { // ELse highlight vertically
								vWalls[newI][newJ].setOpaque(true);

								wallCenters[newI][newJ].setBackground(Color.black);

								vWalls[newI + 1][newJ].setOpaque(true);
							}

							else { // No wall move candidate exists
								JOptionPane.showMessageDialog(null, "Grab a Wall first!", "",
										JOptionPane.WARNING_MESSAGE);
							}

						}

					});

					c = new GridBagConstraints();

					c.gridx = j * 2 + 1;

					c.gridy = i * 2 + 1;

					c.weightx = 1;

					c.weighty = 1;

					c.ipadx = -5;

					c.ipady = -5;

					c.fill = GridBagConstraints.BOTH;

//TODO: set click event for walls here--eg.dropwall

					gameBoardPanel.add(wallCenters[i][j], c);

				}

			}

		}

//test to set text

		currentPlayerName.setText("Current Player: White");

//create a vertical wall at (3,3)

//by setting opaque of box and color of the wall center, we can create walls

//vWalls[2][2].setOpaque(true);

//wallCenters[2][2].setBackground(Color.black);

//vWalls[3][2].setOpaque(true);

	}

	public void setTimeRemaining(int timeRemaining)

	{

		timeRemaining /= 1000;

		int minutes, seconds;

		minutes = timeRemaining / 60;

		seconds = timeRemaining % 60;

		// Change text of label to new time

		String min, sec;

		if (minutes / 10 == 0)

			min = "0" + minutes;

		else

			min = "" + minutes;

		if (seconds / 10 == 0)

			sec = "0" + seconds;

		else

			sec = "" + seconds;

		String tr = "Time remaining: " + minutes + ":" + seconds;

		timeRemLabel.setText(tr);

	}

	public void setCurrentPlayer(String name)

	{

		turnLabel.setText(name + "'s turn");

	}

	public void subtractSecondFromView()

	{

		// pull text from label

		// get last 2 characters

		// change one by one

		// set new text

		String timeRem = timeRemLabel.getText();

		timeRem = timeRem.substring(timeRem.lastIndexOf(":") + 1);

		String minRem = timeRem.substring(timeRem.lastIndexOf(":") - 2, timeRem.lastIndexOf(":"));

		int timeRemInt = Integer.parseInt(timeRem) - 1;

		int minRemInt = Integer.parseInt(minRem);

		if (timeRemInt == -1)

		{

			timeRemInt = 59;

			minRemInt--;

		}

		setTimeRemaining((60 * minRemInt + timeRemInt) * 1000);

	}

	public void createSecondTimer()

	{

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
	public void setWallSelected(boolean selected) {
		wallSelected = selected;
	}

	/** @author Luke Barber */
	public boolean getWallSelected() {
		return this.wallSelected;
	}

	// TODO add action/listener methods to actually progress the game and all that
}
