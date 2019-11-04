package ca.mcgill.ecse223.quoridor.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import ca.mcgill.ecse223.quoridor.controller.Controller;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class QuoridorWindow extends JFrame {

	private JPanel contentPane;
	private JTextField player1Field;
	private JTextField player2Field;
	private JTextField minuteField;
	private JTextField secondField;

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
		
		
		//Combo Boxes to list the user names that were previously used in a game
		JComboBox existingUsernames1 = new JComboBox();
		sl_setupPanel.putConstraint(SpringLayout.NORTH, existingUsernames1, 121, SpringLayout.NORTH, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.WEST, existingUsernames1, 33, SpringLayout.EAST, player1NameBox);
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, existingUsernames1, 158, SpringLayout.NORTH, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.EAST, existingUsernames1, 193, SpringLayout.EAST, player1NameBox);
		existingUsernames1.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		existingUsernames1.setModel(new DefaultComboBoxModel(Controller.listExistingUsernames()));
		setupPanel.add(existingUsernames1);
		
		
		JButton startGameButton = new JButton("Start Game");
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, startGameButton, 0, SpringLayout.SOUTH, thinkingTimeBox);
		sl_setupPanel.putConstraint(SpringLayout.EAST, startGameButton, 0, SpringLayout.EAST, existingUsernames1);
		startGameButton.setFont(new Font("Cooper Black", Font.PLAIN, 20));
		
		
		JComboBox existingUsernames2 = new JComboBox();
		sl_setupPanel.putConstraint(SpringLayout.NORTH, existingUsernames2, 150, SpringLayout.NORTH, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.WEST, existingUsernames2, 33, SpringLayout.EAST, player1NameBox);
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, existingUsernames2, -66, SpringLayout.NORTH, startGameButton);
		sl_setupPanel.putConstraint(SpringLayout.EAST, existingUsernames2, -75, SpringLayout.EAST, setupPanel);
		sl_setupPanel.putConstraint(SpringLayout.SOUTH, existingUsernames2, -6, SpringLayout.NORTH, existingUsernames2);
		existingUsernames2.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		existingUsernames2.setModel(new DefaultComboBoxModel(Controller.listExistingUsernames()));
		setupPanel.add(existingUsernames2);
		
		
		startGameButton.addActionListener(new ActionListener() {
			
			/**
			 * <p> Start new game button <p>
			 * @author Ali Tapan
			 */
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) (contentPane.getLayout());
				
				String time ="";
				String seconds = "";
				String minutes = "";
	
				if((player1Field.getText().length() == 0 && existingUsernames1.getSelectedItem().equals("or select existing username..."))
					&& player2Field.getText().length() == 0 && existingUsernames2.getSelectedItem().equals("or select existing username..."))
				{
					JOptionPane.showMessageDialog(null, "Please provide a username!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if(minuteField.getText().length() == 0 && secondField.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Please provide user time!", "Invalid Remaining Time", JOptionPane.WARNING_MESSAGE);
					return;
				}

				layout.show(contentPane, "activeGamePanel");
				Controller.startNewGame();
				//Checks if the user has entered a valid user name
				//Shows a dialog box if the user name already exists from the previous games
				
				if(player1Field.getText().length() > 0 && existingUsernames1.getSelectedItem().equals("or select existing username..."))
				{
					if(!Controller.provideNewUsername(player1Field.getText(), Controller.initWhitePlayer("user1")))
					{
						JOptionPane.showMessageDialog(null, "This user name already exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				if(player2Field.getText().length() > 0 && existingUsernames2.getSelectedItem().equals("or select existing username..."))
				{
					if(!Controller.provideNewUsername(player2Field.getText(), Controller.initBlackPlayer("user2")))
					{
						JOptionPane.showMessageDialog(null, "This user name already exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				
				//Checks if the player enters an input and also selects an existing user name, if true will show a dialog box
				if(player1Field.getText().length() > 0 && !existingUsernames1.getSelectedItem().equals("or select existing username..."))
				{
					JOptionPane.showMessageDialog(null, "Cannot enter new user name and select an existing username at the same time!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
				}
				
				if(player2Field.getText().length() > 0 && existingUsernames2.getSelectedItem().equals("or select existing username..."))
				{
					JOptionPane.showMessageDialog(null, "Cannot enter new user name and select an existing username at the same time!", "Invalid Username", JOptionPane.WARNING_MESSAGE);
				}
				
				
				//Checks if the player selected an existing user name
				if(!existingUsernames1.getSelectedItem().equals("or select existing username..."))
				{
					Controller.provideNewUsername(existingUsernames1.getSelectedItem().toString(), Controller.initWhitePlayer("user1"));
				}
				
				//Checks if the player selected an existing user name
				if(!existingUsernames2.getSelectedItem().equals("or select existing username..."))
				{
					Controller.provideNewUsername(existingUsernames2.getSelectedItem().toString(), Controller.initBlackPlayer("user2"));
				}
				
				//Checks if the user has selected
				
				if(minuteField.getText().length() < 2)
				{
					minutes =  minutes + "0" + minuteField.getText();
				}
				else
				{
					minutes = minuteField.getText();
				}
				if(secondField.getText().length() < 2)
				{
					seconds = seconds + "0" + secondField.getText();
				}
				else
				{
					seconds = secondField.getText();
				}
				
				time = "00:" + minutes + ":" + seconds;
				
				Controller.setTotalThinkingTime(time);
				Controller.startClock();
				
			}
		});
		
		
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
