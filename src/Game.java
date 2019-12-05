import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Game {
	JFrame mainFrame;
	
	Player player1 = new PlayerX();
	Player player2 = new PlayerO();
	
	Player currentPlayer = player1;
	boolean isRunning = true;
	
	UI ui = new UI(this);
	
	/**
	 * Create the game object and load the last game state if available.
	 * @param mainFrame the main frame.
	 */
	public Game(JFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		load();
	}
	
	/**
	 * Advances to the next player, but first will check if the current player has won.
	 * If the current player won the game, set the running state to false and do not set the next player as current player.
	 * The current player will remain the same when the game is won.
	 */
	public void advance() {
		// Did the player win the game?
		if (isWin()) {
			setRunning(false);
			return;
		}
		
		if (currentPlayer == player1)
			currentPlayer = player2;
		else
			currentPlayer = player1;
	}
	
	/**
	 * Exits the game and closes the main frame.
	 */
	public void exit() {
		onExit();
		
		// Close the main frame.
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * We want to save the current game state on exiting.
	 */
	public void onExit() {
		save();
	}
	
	/**
	 * Resets the game.
	 */
	public void reset() {
		ui.reset();
		currentPlayer = player1;
		setRunning(true);
	}
	
	/**
	 * Check if there is a winner.
	 * @return if the game has been won.
	 */
	protected boolean isWin() {
		String matrix[][] = ui.getMatrix();
		
		// For a horizontal win, we need 3 in a row.
		if (!matrix[0][0].equals(" ") && matrix[0][0].equals(matrix[0][1]) && matrix[0][0].equals(matrix[0][2]))
			return true;
		else if (!matrix[1][0].equals(" ") && matrix[1][0].equals(matrix[1][1]) && matrix[1][0].equals(matrix[1][2]))
			return true;
		else if (!matrix[2][0].equals(" ") && matrix[2][0].equals(matrix[2][1]) && matrix[2][0].equals(matrix[2][2]))
			return true;
		
		// For a vertical win, we need 3 in a column.
		if (!matrix[0][0].equals(" ") && matrix[0][0].equals(matrix[1][0]) && matrix[0][0].equals(matrix[2][0]))
			return true;
		else if (!matrix[0][1].equals(" ") && matrix[0][1].equals(matrix[1][1]) && matrix[0][1].equals(matrix[2][1]))
			return true;
		else if (!matrix[0][2].equals(" ") && matrix[0][2].equals(matrix[1][2]) && matrix[0][2].equals(matrix[2][2]))
			return true;
		
		// For a diagonal win, we need 3 diagonally.
		if (!matrix[0][0].equals(" ") && matrix[0][0].equals(matrix[1][1]) && matrix[0][0].equals(matrix[2][2]))
			return true;
		else if (!matrix[2][0].equals(" ") && matrix[2][0].equals(matrix[1][1]) && matrix[2][0].equals(matrix[0][2]))
			return true;
		
		// No winning condition fulfilled.
		return false;
	}
	
	/**
	 * Checks if all buttons do already have a symbol.
	 * @return if all buttons do already have a symbol.
	 */
	protected boolean isNoMoreChoice() {
		String matrix[][] = ui.getMatrix();
		
		for (int row = 0; row < UI.GAME_SIZE; ++row) {
			for (int column = 0; column < UI.GAME_SIZE; ++column) {
				// If this button is empty, there is a choice.
				if (matrix[row][column].equals(" "))
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Saves the current state of the game to the file "state.txt".
	 */
	protected void save() {
		try {
			// Create the file writer.
			BufferedWriter writer = new BufferedWriter(new FileWriter("state.txt"));

			// Get the matrix and write it to the file in a plain flat format.
			String matrix[][] = ui.getMatrix();
			for (int row = 0; row < matrix.length; ++row) {
				for (int column = 0; column < matrix[row].length; ++column) {
					writer.write(matrix[row][column]);
				}
			}
			
			writer.flush();
			writer.close();
		} catch (java.io.IOException e) {
			System.out.println("An unexpected error occured: " + e.getMessage());
		}
	}
	
	/**
	 * Loads the current game state if "state.txt" is available and properly formatted.
	 * If no state file found or is improperly formatted, the game will have a fresh start.
	 */
	protected void load() {
		try {
			// Read the state file.
			BufferedReader reader = new BufferedReader(new FileReader("state.txt"));

			int count1 = 0, count2 = 0;
			String matrix[][] = new String[UI.GAME_SIZE][UI.GAME_SIZE];
			for (int row = 0; row < matrix.length; ++row) {
				for (int column = 0; column < matrix[row].length; ++column) {
					// Parse the symbol.
					String value = String.valueOf((char)reader.read());
					
					matrix[row][column] = value;
					
					// Count the symbols so we can determine the next player.
					if (value.equals("X"))
						++count1;
					else if (value.equals("O"))
						++count2;
					
					// The state file contains an unknown symbol.
					else if (!value.equals(" ")) {
						System.out.println("State file not properly formatted, reset the game.");
						reader.close();
						return;
					}
				}
			}

			reader.close();
			
			// Load the state into the UI.
			ui.loadState(matrix);

			// Player 1 is the default starting player, but if he already set his symbol, we need Player 2 to be the starting one.
			if (count1 > count2)
				currentPlayer = player2;
			
			// Check if someone already won the game.
			if (isWin()) {
				if (count1 == count2)
					currentPlayer = player2;
				
				setRunning(false);
				ui.askRestartIfWon();
			}
		} catch (java.io.IOException e) {
			System.out.println("No saved state was loaded.");
		}
	}
	
	/**
	 * Returns the player 1 object.
	 * @return the player 1 object.
	 */
	public Player getPlayer1() {
		return player1;
	}
	
	/**
	 * Returns the player 2 object.
	 * @return the player 2 object.
	 */
	public Player getPlayer2() {
		return player2;
	}
	
	/**
	 * Returns the current player.
	 * @return the current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Set the current running state.
	 * @param isRunning running state.
	 */
	protected void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	/**
	 * Is the game running?
	 * @return the running state of the game.
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Returns the UI object.
	 * @return the UI object.
	 */
	public UI getUI() {
		return ui;
	}
}
