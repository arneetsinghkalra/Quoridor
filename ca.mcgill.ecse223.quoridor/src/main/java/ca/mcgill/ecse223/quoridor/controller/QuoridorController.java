package ca.mcgill.ecse223.quoridor.controller;

public interface QuoridorController {

    /**
     * @author Sam Perreault
     * Sets the starting thinking time for the players. Time is accepted as minutes and seconds,
     * and is converted to milliseconds.
     * @param minute The number of minutes allowed to each player
     * @param second The number of seconds allowed to each player
     */
    void setPlayerThinkingTime(int minute, int second);

    /**
     * @author Sam Perreault
     * Generates the board, and sets the starting position and walls of each player.
     * In addition, sets white/player 1 as the player to move, and starts counting down
     * the white player's thinking time.
     */
    void initializeBoard();
}
