package client;

import java.io.IOException;
import java.util.Vector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application{
	public static Rectangle r;
	public static Vector<Rectangle> players = new Vector<Rectangle>();
	public static Pane root;
	
	@Override
	public void start(Stage primaryStage) {
        primaryStage.setTitle("Game");
        r.setX(50);
        r.setY(50);
        r.setWidth(25);
        r.setHeight(25);
        r.setArcWidth(20);
        r.setArcHeight(20);
        r.setFill(Color.AQUA);

        
        root = new Pane();
        root.getChildren().add(r);
        Scene scene = new Scene(root, 800, 800);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
                   case UP:    Connect.state.posY -= 3; break;
                   case DOWN:  Connect.state.posY += 3; break;
                   case LEFT:  Connect.state.posX -= 3; break;
                   case RIGHT: Connect.state.posX += 3; break;
				default:
					break;
				}
			}
		});
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public static void main(String [] args) throws IOException{
        r = new Rectangle();
		Connect connection = new Connect();
		new Thread(connection).start();
		launch(args);
	}
}
