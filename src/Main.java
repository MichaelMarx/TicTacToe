import java.awt.event.*;
import javax.swing.*;

public class Main {

	/**
	 * Entry point of the application.
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		// Create the main frame.
		JFrame mainFrame = new JFrame("Tic Tac Toe");
		
		// Create the game instance.
		Game game = new Game(mainFrame);
		
		// Set default closing operation.
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add the closing listener to the frame to notify the game about exiting.
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				game.onExit();
			}
		});
		
		// Adds the games UI to the main frame.
		mainFrame.getContentPane().add(game.getUI());
		
		// Set the size and set the window visible.
		mainFrame.setBounds(200, 200, 300, 300);
		mainFrame.setVisible(true);
	}

}
