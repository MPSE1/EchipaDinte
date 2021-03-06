package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Vector;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

	public static final int playerSpeed = 15;
	public static final int playerSize = 25;
	public static final int startingLives = 1;
	public static final int minPlayers = 4;

	// Small to make testing easier for now
	private static final int sceneWidth = 600;
	private static final int sceneHeight = 600;

	private static final int mapSize = 600;

	public static Rectangle playerRectangle;
	public static Vector<Rectangle> players = new Vector<Rectangle>();

	public static Pane root;

	public static int[][] collisionMap = new int[mapSize + playerSize][mapSize + playerSize];

	public static KeyCode moveDirection = KeyCode.A;
	public static final EnumSet<KeyCode> validMoves = EnumSet.of(KeyCode.RIGHT, KeyCode.LEFT, KeyCode.UP, KeyCode.DOWN);

	public static Stage fPrimaryStage;

	@Override
	public void start(Stage primaryStage) {

		fPrimaryStage = primaryStage;
		fPrimaryStage.setTitle("Lives: " + startingLives);
		playerRectangle.setX(0);
		playerRectangle.setY(0);

		playerRectangle.setWidth(playerSize);
		playerRectangle.setHeight(playerSize);
		playerRectangle.setArcWidth(20);
		playerRectangle.setArcHeight(20);
		playerRectangle.setFill(Color.AQUA);

		root = new Pane();
		Rectangle rectangle = new Rectangle(mapSize, mapSize, Color.LIGHTGREY);
		root.getChildren().add(rectangle);
		root.getChildren().add(playerRectangle);
		

		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// Only accept valid moves
				KeyCode key = event.getCode();
				if (validMoves.contains(key))
					moveDirection = key;

				if (key == KeyCode.ESCAPE)
					System.exit(0);
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static boolean checkCollisions(KeyCode direction) {

		switch (direction) {

		case UP:
			int x = Connect.state.posX;
			int y = Connect.state.posY - playerSpeed;
			if (x < 0 || x >= mapSize || y < 0 || y >= mapSize)
				return false;
			if (collisionMap[x][y] == 1)
				return false;

			if (collisionMap[x + playerSize][y] == 1)
				return false;
			break;

		case DOWN:
			x = Connect.state.posX;
			y = Connect.state.posY + playerSpeed;
			if (x < 0 || x >= mapSize || y < 0 || y >= mapSize || y + playerSize > mapSize)
				return false;
			if (collisionMap[x + playerSize][y + playerSize] == 1)
				return false;

			if (collisionMap[x][y + playerSize] == 1)
				return false;
			break;

		case LEFT:
			x = Connect.state.posX - playerSpeed;
			y = Connect.state.posY;
			if (x < 0 || x >= mapSize || y < 0 || y >= mapSize)
				return false;
			if (collisionMap[x][y] == 1)
				return false;

			if (collisionMap[x][y + playerSize] == 1)
				return false;
			break;

		case RIGHT:
			x = Connect.state.posX + playerSpeed;
			y = Connect.state.posY;
			if (x < 0 || x >= mapSize || y < 0 || y >= mapSize || x + playerSize > mapSize)
				return false;
			if (collisionMap[x + playerSize][y] == 1)
				return false;

			if (collisionMap[x + playerSize][y + playerSize] == 1)
				return false;
			break;

		default:
			break;
		}
		return true;
	}

	public static void readMap(String fileName) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			int nr = Integer.parseInt(br.readLine());
			for (int i = 0; i < nr; i++) {
				String[] line = br.readLine().split(" ");
				int x, y, width, height;
				x = Integer.parseInt(line[0]);
				y = Integer.parseInt(line[1]);
				width = Integer.parseInt(line[2]);
				height = Integer.parseInt(line[3]);
				Rectangle r = new Rectangle(width, height, Color.MAGENTA);
				r.setX(x);
				r.setY(y);
				addToCollisionMap(x, y, width, height);
				root.getChildren().add(r);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private static void addToCollisionMap(int x, int y, int width, int height) {

		for (int i = x + 1; i < x + width - 1; i++) {
			for (int j = y + 1; j < y + height - 1; j++) {
				collisionMap[i][j] = 1;
			}
		}

	}

	public static void main(String[] args) throws IOException {
		playerRectangle = new Rectangle();
		Connect connection = new Connect();
		new Thread(connection).start();
		launch(args);
	}

	public static void endGame(Integer playerIndex) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game has ended");
		alert.setHeaderText(null);
		alert.setContentText("Player " + playerIndex + " has won!");
		alert.showAndWait();
		System.exit(0);
	}
}
