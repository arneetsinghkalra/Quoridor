package ca.mcgill.ecse223.quoridor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import ca.mcgill.ecse223.quoridor.controller.Controller;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class QuoridorWindow extends JFrame {

	private static JPanel contentPane;
	private JTextField player1NameField;
	private JTextField textField_1;
	private JTextField minuteField;
	private JTextField secondField;
    private static JFrame f; 


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
		loadGameButton.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent a)
		      {
		    	  	Controller.StartNewGame();
		    	  	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		    		int returnValue = jfc.showOpenDialog(null);
		    		
		    		if (returnValue == JFileChooser.APPROVE_OPTION) {
		    			File selectedFile = jfc.getSelectedFile();
		    			try {
		    				Controller.loadPosition(selectedFile.getName());
		    			}catch (UnsupportedOperationException e) {
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
		defaultNamesComboBox.setModel(new DefaultComboBoxModel(new String[] {"or Choose a Player"}));
		setupPanel.add(defaultNamesComboBox);
		
		JButton startGameButton = new JButton("Start Game");
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, startGameButton, 0, SpringLayout.SOUTH, thinkingTimeBox);
		sl_setupPanel.putConstraint(SpringLayout.EAST, startGameButton, 0, SpringLayout.EAST, defaultNamesComboBox);
		startGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		setupPanel.add(startGameButton);
		
		JPanel activeGamePanel = new JPanel();
		contentPane.add(activeGamePanel, "activeGamePanel");
		activeGamePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("New label");
		activeGamePanel.add(lblNewLabel, BorderLayout.WEST);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		activeGamePanel.add(lblNewLabel_1, BorderLayout.EAST);
		
		Box gameOptionBox = Box.createVerticalBox();
		activeGamePanel.add(gameOptionBox, BorderLayout.SOUTH);
		
		Box titleTimeBox = Box.createVerticalBox();
		activeGamePanel.add(titleTimeBox, BorderLayout.NORTH);
		
		JLabel gameTitleLabel = new JLabel("QUORIDOR");
		gameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameTitleLabel.setFont(new Font("Cooper Black", Font.PLAIN, 40));
		titleTimeBox.add(gameTitleLabel);
		
		JPanel gameBoardPanel = new JPanel();
		activeGamePanel.add(gameBoardPanel, BorderLayout.CENTER);
		gameBoardPanel.setLayout(new GridLayout(9, 9, 5, 5));
	}

	// TODO add action/listener methods to actually progress the game and all that
}
