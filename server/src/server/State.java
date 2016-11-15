package server;

public class State {

	Integer posX;
	Integer posY;
	
	Integer lifes;
	
	Integer doctor;//pac-man or ghost
	
	Integer playerNumb = -1;
	
	Integer gameStart = 0;
	
	public State(int parseInt, int parseInt2, int parseInt3, int parseInt4, Integer gameStart2) {
		posX = parseInt;
		posY = parseInt2;
		lifes = parseInt3;
		doctor = parseInt4;
		gameStart = gameStart2;
	}
	
	public State() {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
		return posX + " " + posY + " " + lifes + " " + doctor;
		
	}
}

