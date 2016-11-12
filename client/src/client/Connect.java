package client;

import java.net.*;
import java.util.Vector;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

import java.io.*;

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

				// try a move
				othersState.removeAllElements();
				Main.move();
				synchronized (Lock) {
					out.writeUTF("" + state.posX);
					out.writeUTF("" + state.posY);
					out.writeUTF("" + state.lifes);
					out.writeUTF("" + state.doctor);
				}
				synchronized (Lock) {

					playerNumber = Integer.parseInt(in.readUTF());
					numberOfPlayers = Integer.parseInt(in.readUTF());

					for (int i = 0; i < numberOfPlayers; i++) {
						System.out.println(othersState);
						if (i == playerNumber) {
							state.posX = Integer.parseInt(in.readUTF());
							state.posY = Integer.parseInt(in.readUTF());
							state.lifes = Integer.parseInt(in.readUTF());
							state.doctor = Integer.parseInt(in.readUTF());
							if (state.posX != null) {
								Main.playerRectangle.setX(state.posX);
								Main.playerRectangle.setY(state.posY);
							}
							othersState.add(new State(state.posX, state.posY, state.lifes, state.doctor));
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
						} else {
							othersState.add(new State(Integer.parseInt(in.readUTF()), Integer.parseInt(in.readUTF()),
									Integer.parseInt(in.readUTF()), Integer.parseInt(in.readUTF())));
							if (Main.players.size() <= i) {
								while (Main.players.size() <= i) {
									Rectangle rec = new Rectangle();
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											Main.root.getChildren().add(rec);
										}
									});
									Thread.sleep(30);
									Main.players.add(rec);
								}
							}
							Main.players.get(i).setX(othersState.lastElement().posX);
							Main.players.get(i).setY(othersState.lastElement().posY);
							Main.players.get(i).setWidth(Main.playerSize);
							Main.players.get(i).setHeight(Main.playerSize);
							Main.players.get(i).setArcWidth(20);
							Main.players.get(i).setArcHeight(20);
						}

					}

				}
				// apply move according to server response
				System.out.println(othersState);
				Thread.sleep(25);
			}

			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			stop = true;
		}

	}

}
