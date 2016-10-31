package communication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int fWidth;
	private int fHeight;
	private int fPlayersNo;
	private char[][] fMap;
	
	public GameState(String fileName) {
		try {
			String line;
			String[] lineContents;
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			line = reader.readLine();
			lineContents = line.split(" ");
			if (lineContents.length < 3) {
				error(fileName);
				return;
			}
			
			fWidth = Integer.parseInt(lineContents[0]);
			fHeight = Integer.parseInt(lineContents[1]);
			fPlayersNo = Integer.parseInt(lineContents[2]);
			if (fWidth <= 0 || fHeight <= 0) {
				error(fileName);
				return;
			}
			
			fMap = new char[fWidth][fHeight];
			for (int i = 0; i < fHeight; i++) {
				
				line = reader.readLine();
				if (line.length() != fWidth) {
					error(fileName);
					return;
				}
				
				for (int j = 0; j < fWidth; j++) {
					char c = line.charAt(j);
					fMap[i][j] = c;
					
				}
			}

			System.out.println("Managed to read map(" + fWidth + ", " + fHeight + ", " + fPlayersNo + "): ");
			reader.close();			
		} catch (IOException e) {
			error(fileName);
			e.printStackTrace();
		}
	}
	
	public int getWidth() {
		return fWidth;
	}
	
	public int getHeight() {
		return fHeight;
	}
	
	public int getPlayersNumber() {
		return fPlayersNo;
	}
	
	public char[][] getMap() {
		return fMap;
	}
	
	private void error(String fileName) {
		System.err.println("Could not read game state from " + fileName);
	}
	
	public String toString() {
		
		StringBuilder result = new StringBuilder("<html>");
		
		for (int i = 0; i < fHeight; i++) {
			for (int j = 0; j < fWidth; j++) {
				result.append(fMap[i][j] + "  ");
			}
			result.append("<br>");
		}
		
		result.append("</html>");
		return result.toString();
	}
}
