package server;

public class State {

	Integer posX;
	Integer posY;
	
	Integer lifes;
	
	Integer doctor;//pac-man or ghost
	
	Integer playerNumb = -1;
	
	public State(int parseInt, int parseInt2, int parseInt3, int parseInt4) {
		posX = parseInt;
		posY = parseInt2;
		lifes = parseInt3;
		doctor = parseInt4;
	}
	
	public State() {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
		return posX + " " + posY + " " + lifes + " " + doctor;
		
	}
}
