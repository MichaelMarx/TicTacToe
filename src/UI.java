import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UI extends JPanel {
	private class ButtonClickedListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// We already have a winner, do not continue.
			if (!game.isRunning())
				return;
			
			// If the button does not already have a symbol, update it to the symbol of the current player.
			JButton button = (JButton)e.getSource();
			if (!button.getText().equals(" "))
				return;
			
			button.setText(game.getCurrentPlayer().getSymbol());
			
			// Check if the game is won and advance to next player if the game continues.
			game.advance();
			
			// If the game has been won, ask for a restart.
			askRestartIfWon();
		}
	}
	
	public final static int GAME_SIZE = 3;
	
	Game game;
	JButton buttons[][] = new JButton[GAME_SIZE][GAME_SIZE];
	
	/**
	 * Initializes the game UI.
	 * @param game the game object.
	 */
	public UI(Game game) {
		this.game = game;
		
		// We need 3 rows and 3 columns.
		setLayout(new GridLayout(3, 3));
		
		// Create the buttons, 3 per row.
		for (int row = 0; row < GAME_SIZE; ++row) {
			for (int column = 0; column < GAME_SIZE; ++column) {
				// Create a button.
				JButton button = new JButton();
				
				// Add the on click listener to the button.
				button.addActionListener(new ButtonClickedListener());
				
				// Add the button to the UI (grid layout automatically arranges the buttons.
				add(button);
				
				// Save the button for easier later use.
				buttons[row][column] = button;
			}
		}
		
		// Reset the UI.
		reset();
	}
	
	/**
	 * Reset the UI to an empty state.
	 */
	public void reset() {
		for (int row = 0; row < GAME_SIZE; ++row) {
			for (int column = 0; column < GAME_SIZE; ++column) {
				buttons[row][column].setText(" ");
			}
		}
	}
	
	/**
	 * Load a previously defined matrix as current state.
	 * @param matrix the games state.
	 */
	public void loadState(String[][] matrix) {
		for (int row = 0; row < GAME_SIZE; ++row) {
			for (int column = 0; column < GAME_SIZE; ++column) {
				buttons[row][column].setText(matrix[row][column]);
			}
		}
	}
	
	/**
	 * If the game has been won, ask if the players want to restart. If not, exit.
	 */
	public void askRestartIfWon() {
		if (game.isWin()) {
			int result = JOptionPane.showConfirmDialog(null,  "Player " + game.getCurrentPlayer().getSymbol() + " won the game. Do you want to restart?", "alert", JOptionPane.YES_NO_OPTION);
			
			if (result != 0)
				game.exit();
			else
				game.reset();
		} else if (game.isNoMoreChoice()) {
			int result = JOptionPane.showConfirmDialog(null,  "Draw. Do you want to restart?", "alert", JOptionPane.YES_NO_OPTION);
			
			if (result != 0)
				game.exit();
			else
				game.reset();
		}
	}
	
	/**
	 * Returns the currents game state matrix.
	 * @return the currents game state matrix.
	 */
	public String[][] getMatrix() {
		String matrix[][] = new String[GAME_SIZE][GAME_SIZE];
		
		for (int row = 0; row < GAME_SIZE; ++row) {
			for (int column = 0; column < GAME_SIZE; ++column) {
				matrix[row][column] = buttons[row][column].getText();
			}
		}
		
		return matrix;
	}
}
