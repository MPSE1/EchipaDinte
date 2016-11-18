package server;

import java.io.IOException;

public class Main {
	
	public static void main(String [] agrs) throws IOException{
		Server newServer = new Server(9191);
		new Thread(newServer).start();
		 
		try {
		    Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping newServer");
		newServer.stop();
		}
}
