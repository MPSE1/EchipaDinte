package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;

public class Worker implements Runnable {
	protected Socket clntSocket = null;
	protected String txtFrmSrvr = null;
	protected State state;
	protected int number = -1;
	public static Vector<State> allStates = new Vector<State>();

	public Worker(Socket clntSocket, String txtFrmSrvr, int numberOfPlayer) {
		this.clntSocket = clntSocket;
		this.txtFrmSrvr = txtFrmSrvr;
		this.number = numberOfPlayer;
		allStates.add(new State());
	}

	public void run() {
		state = new State();
		try {

			System.out.println("Just connected to " + clntSocket.getRemoteSocketAddress());
			DataInputStream in = new DataInputStream(clntSocket.getInputStream());

			System.out.println(in.readUTF());
			DataOutputStream out = new DataOutputStream(clntSocket.getOutputStream());
			out.writeUTF("Thank you for connecting to " + clntSocket.getLocalSocketAddress() + "\nwait for commands!");
			while (true) {
				state.posX = Integer.parseInt(in.readUTF());
				state.posY = Integer.parseInt(in.readUTF());
				state.lifes = Integer.parseInt(in.readUTF());
				state.doctor = Integer.parseInt(in.readUTF());
				allStates.set(number, new State(state.posX, state.posY, state.lifes, state.doctor));
				// make a decision

				out.writeUTF("" + number);
				out.writeUTF("" + allStates.size());
				System.out.println(allStates);
				for (State sta : allStates) {
					System.out.println("Sent " + sta);
					out.writeUTF("" + sta.posX);
					out.writeUTF("" + sta.posY);
					out.writeUTF("" + sta.lifes);
					out.writeUTF("" + sta.doctor);
				}

				Thread.sleep(1);
				System.out.println("for client " + number + " state is " + state);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}