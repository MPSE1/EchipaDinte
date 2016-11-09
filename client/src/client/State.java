package client;

public class State {

	Integer posX = 0;
	Integer posY = 0;
	
	Integer lifes = 1;
	
	Integer doctor = 0;//pac-man or ghost
	
	
	public State(int parseInt, int parseInt2, int parseInt3, int parseInt4) {
		posX = parseInt;
		posY = parseInt2;
		lifes = parseInt3;
		doctor = parseInt4;
	}


	public State() {
		posX = 0;
		posY = 0;
		lifes = 1;
		doctor = 0;//pac-man or ghost
	}


	public String toString(){
		return posX + " " + posY + " " + lifes + " " + doctor;
	}
	
}
