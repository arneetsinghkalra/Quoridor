package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.controller.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.Controller;

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
	private Timer secondTimer;
	private JLabel turnLabel;
	private JLabel timeRemLabel;
	private static boolean confirms = true;

    private static JFrame f;

    //for the boards,tiles, and walls
    private JButton[][] tiles = new JButton[9][9];
    private JButton[][] wallCenters =  new JButton[8][8];
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
        contentPane.add(titleScreenPanel, "name_1049600133434900");
        SpringLayout sl_titleScreenPanel = new SpringLayout();
        titleScreenPanel.setLayout(sl_titleScreenPanel);

        JButton newGameButton = new JButton("New Game");
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
        sl_titleScreenPanel.putConstraint(SpringLayout.NORTH, loadGameButton, 207, SpringLayout.NORTH, titleScreenPanel);
        sl_titleScreenPanel.putConstraint(SpringLayout.WEST, loadGameButton, 74, SpringLayout.WEST, titleScreenPanel);
        sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, loadGameButton, -124, SpringLayout.SOUTH, titleScreenPanel);
        sl_titleScreenPanel.putConstraint(SpringLayout.SOUTH, newGameButton, -6, SpringLayout.NORTH, loadGameButton);
        sl_titleScreenPanel.putConstraint(SpringLayout.EAST, newGameButton, 0, SpringLayout.EAST, loadGameButton);
        loadGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
        loadGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent a) {
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
        sl_setupPanel.putConstraint(SpringLayout.NORTH, defaultNamesComboBox, 121, SpringLayout.NORTH, setupPanel);
        sl_setupPanel.putConstraint(SpringLayout.WEST, defaultNamesComboBox, 33, SpringLayout.EAST, player1NameBox);
        sl_setupPanel.putConstraint(SpringLayout.SOUTH, defaultNamesComboBox, 158, SpringLayout.NORTH, setupPanel);
        sl_setupPanel.putConstraint(SpringLayout.EAST, defaultNamesComboBox, 193, SpringLayout.EAST, player1NameBox);
        defaultNamesComboBox.setFont(new Font("Cooper Black", Font.PLAIN, 14));
        defaultNamesComboBox.setModel(new DefaultComboBoxModel(new String[]{"or Choose a Player"}));
        setupPanel.add(defaultNamesComboBox);

        JButton startGameButton = new JButton("Start Game");
        sl_setupPanel.putConstraint(SpringLayout.EAST, defaultNamesComboBox, 0, SpringLayout.EAST, startGameButton);
        sl_setupPanel.putConstraint(SpringLayout.EAST, startGameButton, -75, SpringLayout.EAST, setupPanel);
        sl_setupPanel.putConstraint(SpringLayout.SOUTH, startGameButton, 0, SpringLayout.SOUTH, thinkingTimeBox);
        startGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) (contentPane.getLayout());
                Controller.StartNewGame();
                Controller.initializeBoard();
                layout.show(contentPane, "activeGamePanel");
            }
        });
        setupPanel.add(startGameButton);

        JComboBox comboBox = new JComboBox();
        sl_setupPanel.putConstraint(SpringLayout.NORTH, comboBox, 150, SpringLayout.NORTH, setupPanel);
        sl_setupPanel.putConstraint(SpringLayout.WEST, comboBox, 33, SpringLayout.EAST, player1NameBox);
        sl_setupPanel.putConstraint(SpringLayout.SOUTH, comboBox, -66, SpringLayout.NORTH, startGameButton);
        sl_setupPanel.putConstraint(SpringLayout.EAST, comboBox, -75, SpringLayout.EAST, setupPanel);
        sl_setupPanel.putConstraint(SpringLayout.SOUTH, defaultNamesComboBox, -6, SpringLayout.NORTH, comboBox);
        comboBox.setFont(new Font("Cooper Black", Font.PLAIN, 14));
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"or Choose a Player"}));
        setupPanel.add(comboBox);

        JPanel activeGamePanel = new JPanel();
        contentPane.add(activeGamePanel, "activeGamePanel");
        activeGamePanel.setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel = new JLabel("New label");
        activeGamePanel.add(lblNewLabel, BorderLayout.WEST);

        JLabel lblNewLabel_1 = new JLabel("New label");
        activeGamePanel.add(lblNewLabel_1, BorderLayout.EAST);

        Box gameOptionBox = Box.createVerticalBox();
        gameOptionBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        activeGamePanel.add(gameOptionBox, BorderLayout.SOUTH);

        JButton btnSaveGame = new JButton("Save Game");
        btnSaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOptionBox.add(btnSaveGame);
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
                f.add(tf1);
                f.add(b1);
                f.setSize(300, 300);
                f.setLayout(null);
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
                            f.add(tf1);
                            f.add(b1);
                            f.add(b2);
                            f.setSize(300, 300);
                            f.setLayout(null);
                            f.setVisible(true);
                            b1.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent b) {
                                    confirms = true;
                                    f.setVisible(false);
                                    try {
                                        Controller.savePosition(tf1.getText(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(), confirms);
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
                                Controller.savePosition(tf1.getText(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition(), confirms);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOptionBox.add(btnNewGame);

        JButton btnLoadGame = new JButton("Load Game");
        btnLoadGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOptionBox.add(btnLoadGame);
        btnLoadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent a) {
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
                        f.add(tf1);
                        f.add(b1);
                        f.setSize(300, 300);
                        f.setLayout(null);
                        b1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent b) {
                                f.setVisible(false);
                            }
                        });
                    }
                }
            }
        });

        Box titleTimeBox = Box.createVerticalBox();
        activeGamePanel.add(titleTimeBox, BorderLayout.NORTH);

        JLabel gameTitleLabel = new JLabel("QUORIDOR");
        gameTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameTitleLabel.setVerticalAlignment(SwingConstants.TOP);
        gameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameTitleLabel.setFont(new Font("Cooper Black", Font.PLAIN, 40));
        titleTimeBox.add(gameTitleLabel);

        //board
        JPanel gameBoardPanel = new JPanel();
        activeGamePanel.add(gameBoardPanel, BorderLayout.CENTER);
        gameBoardPanel.setLayout(new GridBagLayout());


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
                    vWalls[i][j].setOpaque(false);
                    vWalls[i][j].setBackground(Color.black);
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
                    hWalls[i][j].setOpaque(false);
                    hWalls[i][j].setBackground(Color.black);
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
        //create a vertical wall at (3,3)
        //by setting opaque of box and color of the wall center, we can create walls
        //vWalls[2][2].setOpaque(true);
        //wallCenters[2][2].setBackground(Color.black);
        //vWalls[3][2].setOpaque(true);

        Box horizontalBox = Box.createHorizontalBox();
        titleTimeBox.add(horizontalBox);

        turnLabel = new JLabel("It is");
        turnLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        turnLabel.setHorizontalAlignment(SwingConstants.LEFT);
        turnLabel.setFont(new Font("Cooper Black", Font.PLAIN, 14));
        horizontalBox.add(turnLabel);

        Component horizontalStrut = Box.createHorizontalStrut(100);
        horizontalBox.add(horizontalStrut);

        timeRemLabel = new JLabel("Time remaining: 99:99");
        timeRemLabel.setFont(new Font("Cooper Black", Font.PLAIN, 14));
        timeRemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        horizontalBox.add(timeRemLabel);

    }

    public String getTurnLabel()
    {
        return turnLabel.getText();
    }

    public boolean getIsTimerActive() {
        return secondTimer.isRunning();
    }
    public void setTimeRemaining(int timeRemaining)
    {
        timeRemaining /= 1000;
        int minutes, seconds;
        minutes = timeRemaining/60;
        seconds = timeRemaining % 60;
        // Change text of label to new time
        String min,sec;
        if(minutes/10 == 0)
            min="0"+minutes;
        else
            min =""+ minutes;
        if(seconds/10 == 0)
            sec = "0"+seconds;
        else
            sec = ""+ seconds;
        String tr = "Time remaining: "+minutes+":"+seconds;
        timeRemLabel.setText(tr);
    }

    public void setCurrentPlayer(String name)
    {
        turnLabel.setText(name+"'s turn");
    }

    public void subtractSecondFromView()
    {
        // pull text from label
        // get last 2 characters
        // change one by one
        //set new text
        String timeRem = timeRemLabel.getText();
        timeRem = timeRem.substring(timeRem.lastIndexOf(":")+1);
        String minRem = timeRem.substring(timeRem.lastIndexOf(":")-2, timeRem.lastIndexOf(":"));
        int timeRemInt = Integer.parseInt(timeRem)-1;
        int minRemInt = Integer.parseInt(minRem);
        if(timeRemInt == -1)
        {
            timeRemInt = 59;
            minRemInt--;
        }
        setTimeRemaining((60*minRemInt+timeRemInt)*1000);
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
        secondTimer = new Timer(1000,listener);
        secondTimer.setRepeats(true);
        secondTimer.start();
    }
}
