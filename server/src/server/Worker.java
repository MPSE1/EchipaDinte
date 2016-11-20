package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class Worker implements Runnable {
	
	protected Socket clntSocket = null;
	protected String txtFrmSrvr = null;
	protected State state;
	protected int number = -1;
	protected int minPlayers = 4;
	protected float gameTime = 1.0f;
	public static Vector<State> allStates = new Vector<State>();
	public static Object lock = new Object();
	public static long start;
	public static long startNebun;
	public boolean gameStart = false;
	protected static int nebun = -1;
	protected float changeNebun = 0.33f;
	public boolean first = true;
	Random rand = new Random();

	private String mapFileName;

	public Worker(Socket clntSocket, String txtFrmSrvr, int numberOfPlayer, String fileName) {
		
		this.clntSocket = clntSocket;
		this.txtFrmSrvr = txtFrmSrvr;
		this.number = numberOfPlayer;
		System.out.println("numarul " + number);
		allStates.add(new State());

		// Random map
		mapFileName = fileName;
	}

	public void run() {
		state = new State();
		try {

			System.out.println("Just connected to " + clntSocket.getRemoteSocketAddress());
			DataInputStream in = new DataInputStream(clntSocket.getInputStream());

			System.out.println(in.readUTF());
			DataOutputStream out = new DataOutputStream(clntSocket.getOutputStream());

			// Send the random map to each client
			out.writeUTF(mapFileName);
			while (true) {
				state.posX = Integer.parseInt(in.readUTF());
				state.posY = Integer.parseInt(in.readUTF());
				state.lifes = Integer.parseInt(in.readUTF());
				state.doctor = Integer.parseInt(in.readUTF());
				state.gameStart = Integer.parseInt(in.readUTF());
				allStates.set(number, new State(state.posX, state.posY, state.lifes, state.doctor, state.gameStart));
				// make a decision
				
				out.writeUTF("" + number);
				out.writeUTF("" + allStates.size());
				
				for (int i = 0; i < allStates.size(); i++) {
					State sta = allStates.get(i);
					
					if (allStates.size() != minPlayers) {
						first = false;
						if (number == 3) {
							out.writeUTF("" + 0);
							out.writeUTF("" + 0);
						} else if (number == 2) {
							out.writeUTF("" + 575);
							out.writeUTF("" + 0);
						} else if (number == 1) {
							out.writeUTF("" + 575);
							out.writeUTF("" + 575);
						} else if (number == 0) {
							out.writeUTF("" + 0);
							out.writeUTF("" + 575);
						} else {
							out.writeUTF("" + 0);
							out.writeUTF("" + 0);
						}

					} else {
						out.writeUTF("" + sta.posX);
						out.writeUTF("" + sta.posY);
					}
					
					out.writeUTF("" + sta.lifes);
					
					synchronized (lock) {
						
						if (i == nebun) {
							out.writeUTF("" + 0);
						} else
							out.writeUTF("" + 1);
					}
					if (allStates.size() == minPlayers) {
						if (sta.gameStart == 0 && !gameStart)
							synchronized (lock) {
								start = System.currentTimeMillis();
								startNebun = System.currentTimeMillis();
								gameStart = true;
								if (number == 0)
									nebun = rand.nextInt(minPlayers);
								System.out.println(nebun);
							}
						
						if ((System.currentTimeMillis() - startNebun) / 60.0f / 1000.0f > changeNebun && number == 0)
							synchronized (lock) {
								startNebun = System.currentTimeMillis();
								nebun = rand.nextInt(minPlayers);
								while (allStates.get(nebun).posX == 1000) {
									nebun = rand.nextInt(minPlayers);
								}
								System.out.println(nebun);
							}
						if ((System.currentTimeMillis() - start) / 60.0f / 1000.0f > gameTime) {
							out.writeUTF("" + 2);
						}	
						else {
							out.writeUTF("" + 1);
						}
						
					} else {
						out.writeUTF("" + sta.gameStart);
					}
					Thread.sleep(1);
				}

				Thread.sleep(1);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}