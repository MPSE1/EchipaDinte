package client;

public class State {
	Integer posX = 0;
	Integer posY = 0;
	
	Integer lifes = Main.startingLives;
	
	Integer doctor = 0;//pac-man or ghost
	
	Integer gameStart = 0;
	
	public State(int parseInt, int parseInt2, int parseInt3, int parseInt4, int gameStart2) {
		posX = parseInt;
		posY = parseInt2;
		lifes = parseInt3;
		doctor = parseInt4;
		gameStart = gameStart2;
	}


	public State() {
		posX = 0;
		posY = 0;
		lifes = Main.startingLives;
		doctor = 0;//pac-man or ghost
	}


	public String toString(){
		return posX + " " + posY + " " + lifes + " " + doctor;
	}
	
}
