package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

import javax.net.ssl.SSLEngineResult.Status;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Connect implements Runnable {

	String serverName = "127.0.0.1";
	public static State state;
	Vector<State> othersState = new Vector<State>();
	public boolean stop = false;
	public static Object Lock = new Object();
	public static int playerNumber = -1;
	public int numberOfPlayers;

	@Override
	public void run() {
		int port = 9191;
		state = new State();
		try {
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);

			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);

			String mapFileName = in.readUTF();
			System.out.println("Server says " + mapFileName);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.readMap(mapFileName);
				}
			});
			while (true) {

				if (stop)
					break;

				othersState.removeAllElements();
				// try a move
				if (state.gameStart == 1)
					move();
				synchronized (Lock) {
					out.writeUTF("" + state.posX);
					out.writeUTF("" + state.posY);
					out.writeUTF("" + state.lifes);
					out.writeUTF("" + state.doctor);
					out.writeUTF("" + state.gameStart);
				}
				Thread.sleep(1);
				synchronized (Lock) {

					playerNumber = Integer.parseInt(in.readUTF());

					numberOfPlayers = Integer.parseInt(in.readUTF());

					for (int i = 0; i < numberOfPlayers; i++) {
						// System.out.println(othersState);
						if (i == playerNumber) {
							state.posX = Integer.parseInt(in.readUTF());
							state.posY = Integer.parseInt(in.readUTF());
							state.lifes = Integer.parseInt(in.readUTF());
							state.doctor = Integer.parseInt(in.readUTF());
							state.gameStart = Integer.parseInt(in.readUTF());
							if (state.posX != null) {
//								System.out.println(state.posX + " " + state.posY);
								Main.playerRectangle.setX(state.posX);
								Main.playerRectangle.setY(state.posY);
							}
							othersState.add(new State(state.posX, state.posY, state.lifes, state.doctor, state.gameStart));
							if (Main.players.size() <= i) {
								while (Main.players.size() <= i) {
									Rectangle rec = new Rectangle();
									Thread.sleep(5);
									Main.players.add(rec);
								}
							}
							Main.players.get(i).setX(othersState.lastElement().posX);
							Main.players.get(i).setY(othersState.lastElement().posY);
							Main.players.get(i).setWidth(Main.playerSize);
							Main.players.get(i).setHeight(Main.playerSize);
							Main.players.get(i).setArcWidth(20);
							Main.players.get(i).setArcHeight(20);
							if (state.doctor == 0) {
								Main.playerRectangle.setFill(Color.RED);
								Main.players.get(i).setFill(Color.RED);
							} else {
								Thread.sleep(5);
								Main.players.get(i).setFill(Color.BLACK);
								Main.playerRectangle.setFill(Color.AQUA);
							}
						} else {

							String received;
							int posX, posY, lifes, doctor, gameStart;
							received = in.readUTF();
							if (received.compareTo("null") != 0)
								posX = Integer.parseInt(received);
							else
								posX = 0;
							received = in.readUTF();
							if (received.compareTo("null") != 0)
								posY = Integer.parseInt(received);
							else
								posY = 0;
							received = in.readUTF();
							if (received.compareTo("null") != 0)
								lifes = Integer.parseInt(received);
							else
								lifes = 10;
							received = in.readUTF();
							if (received.compareTo("null") != 0)
								doctor = Integer.parseInt(received);
							else
								doctor = 1;
							received = in.readUTF();
							if (received.compareTo("null") != 0)
								gameStart = Integer.parseInt(received);
							else
								gameStart = 1;
							// System.out.println("doctor :" + doctor);
							othersState.add(new State(posX, posY, lifes, doctor, gameStart));
							Thread.sleep(1);
							if (Main.players.size() <= i) {
								while (Main.players.size() <= i) {
									Rectangle rec = new Rectangle();
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											Main.root.getChildren().add(rec);
										}
									});
									Thread.sleep(70);
									Main.players.add(rec);
								}
							}
							Main.players.get(i).setX(othersState.lastElement().posX);
							Main.players.get(i).setY(othersState.lastElement().posY);
							Main.players.get(i).setWidth(Main.playerSize);
							Main.players.get(i).setHeight(Main.playerSize);
							Main.players.get(i).setArcWidth(20);
							Main.players.get(i).setArcHeight(20);
							if (othersState.lastElement().doctor == 0)
								Main.players.get(i).setFill(Color.RED);
							else
								Main.players.get(i).setFill(Color.BLACK);
						}

					}
					if (state.doctor != 0)
						checkBadGuyCollision();
					if (othersState.size() >= Main.minPlayers)
						checkGameEnded();
				}
				// apply move according to server response
				// System.out.println(othersState);
				Thread.sleep(50);
			}

			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			stop = true;
		}

	}

	private void checkGameEnded() {
		int alive = 0;
		for (State s : othersState)
			alive += s.lifes > 0 ? 1 : 0;
		if (alive == 1) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.endGame();
				}});
			try {
				Thread.currentThread().join();
			} catch (InterruptedException e) {}
		}
	}

	private void move() {
		switch (Main.moveDirection) {
		case UP:
			if (Main.checkCollisions(KeyCode.UP))
				state.posY -= Main.playerSpeed;
			break;
		case DOWN:
			if (Main.checkCollisions(KeyCode.DOWN))
				state.posY += Main.playerSpeed;
			break;
		case LEFT:
			if (Main.checkCollisions(KeyCode.LEFT))
				state.posX -= Main.playerSpeed;
			break;
		case RIGHT:
			if (Main.checkCollisions(KeyCode.RIGHT))
				state.posX += Main.playerSpeed;
			break;
		default:
			break;
		}
	}
	
	private void checkBadGuyCollision() {
		for (State s : othersState) {
			if (s.doctor == 0) {
				int playerSize = Main.playerSize;
				
				// Get bad guy center position
				int badX = s.posX + playerSize/2;
				int badY = s.posY + playerSize/2;
				
				// Get our center position
				int meX = state.posX + playerSize/2;
				int meY = state.posY + playerSize/2;
				
				// Check collision
				if (distance(badX, badY, meX, meY) < playerSize) { // I'm dead
					System.out.println("Player:" + state.posX + ", " + state.posY + "found collision");
					state.lifes--;
					respawnInRandomCorner();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Main.fPrimaryStage.setTitle("Lives: " + state.lifes);
						}
					});
				}
			}
		}
	}
	
	private void respawnInRandomCorner() {
		int posX, posY;
		int random = new Random(System.nanoTime()).nextInt(4);
		switch (random) {
		case 0:
			posX = 0;
			posY = 0;
			break;
		case 1:
			posX = 0;
			posY = 575;
			break;
		case 2:
			posX = 575;
			posY = 0;
			break;
		case 3:
			posX = 575;
			posY = 575;
			break;
		default:
			return;
		}
		Main.playerRectangle.setX(posX);
		Main.playerRectangle.setY(posY);
		state.posX = posX;
		state.posY = posY;
	}

	public double distance(int x1, int y1, int x2, int y2) {
	    return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
}
