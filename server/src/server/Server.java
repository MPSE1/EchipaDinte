package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.io.IOException;
 
public class Server implements Runnable{
    protected int          serverPortVal   = 8080;
    protected ServerSocket serverSocketVal = null;
    protected boolean      hasStopped    = false;
    protected Thread       movingThread= null;
    protected int numberOfPlayers = 0;
 
	private static final int MAP_NUMBER = 5;
	private static final String[] MAP_FILES = {"map1.txt", "map2.txt", "map3.txt", "map4.txt", "map5.txt"};
	private String mapFileName;
	
    public Server(int port){
        this.serverPortVal = port;
        mapFileName = MAP_FILES[new Random(System.nanoTime()).nextInt(MAP_NUMBER)];
    
    }
 
    public void run(){
        synchronized(this){
            this.movingThread = Thread.currentThread();
        }
        opnSvrSocket();
        while(! hasStopped()){
            Socket clntSocket = null;
            try {
                clntSocket = this.serverSocketVal.accept();
            } catch (IOException e) {
                if(hasStopped()) {
                    System.out.println("Server has Stopped...Please check") ;
                    return;
                }
                throw new RuntimeException(
                    "Client cannot be connected - Error", e);
            }
            new Thread(
                new Worker(clntSocket, "This is a multithreaded Server", numberOfPlayers, mapFileName)
            ).start();
            numberOfPlayers++;
        }
        System.out.println("Server has Stopped...Please check") ;
    }
    private synchronized boolean hasStopped() {
        return this.hasStopped;
    }
    public synchronized void stop(){
        this.hasStopped = true;
        try {
            this.serverSocketVal.close();
        } catch (IOException e) {
            throw new RuntimeException("Server can not be closed - Please check error", e);
        }
    }
    private void opnSvrSocket() {
        try {
            this.serverSocketVal = new ServerSocket(this.serverPortVal);
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port " + serverPortVal, e);
        }
    }
 
}
