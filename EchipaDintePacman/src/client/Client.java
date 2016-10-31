package client;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import communication.GameState;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JLabel fLabel;
	private GameState fGameState;
	
	private static final int fFontSize = 20;

	public Client() {
		
		/* We want this to be received from server not created here */
		fGameState = new GameState("resources/charactermap");

		setSize(new Dimension(1280, 720));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		fLabel = new JLabel(fGameState.toString(), SwingConstants.CENTER);
		fLabel.setPreferredSize(new Dimension(1280, 720));
		fLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fFontSize ));
		add(fLabel);
		
		setVisible(true);
	}
	
	private void updateGame() {
		// fGameState = receiveFromServer();
		fLabel.setText(fGameState.toString());
	}
	
	public static void main(String[] args) {
		Client c = new Client();
		c.updateGame();
	}

}
