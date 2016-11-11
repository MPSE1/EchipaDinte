package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application{
	public static Rectangle r;
	public static final int speed = 5;
	public static final int playerSize = 25;
	public static Vector<Rectangle> players = new Vector<Rectangle>();
	public static Pane root;
	public static int [][] map = new int[800][800];
	
	@Override
	public void start(Stage primaryStage) {
        primaryStage.setTitle("Game");
        r.setX(0);
        r.setY(0);
        r.setWidth(playerSize);
        r.setHeight(playerSize);
        r.setArcWidth(20);
        r.setArcHeight(20);
        r.setFill(Color.AQUA);

        
        root = new Pane();
        Rectangle rectangle = new Rectangle(600,600,Color.LIGHTGREY);
		root.getChildren().add(rectangle);
		readMap("map1.txt");
        root.getChildren().add(r);
        
        Scene scene = new Scene(root, 1280, 720);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
                   case UP:    
                	   if (checkCollisions(KeyCode.UP))
                		   Connect.state.posY -= speed; 
                	   break;
                   case DOWN:  
                	   if (checkCollisions(KeyCode.DOWN))
                		   Connect.state.posY += speed; 
                	   break;
                   case LEFT:  
                	   if (checkCollisions(KeyCode.LEFT))
                		   Connect.state.posX -= speed;
                	   break;
                   case RIGHT: 
                	   if (checkCollisions(KeyCode.RIGHT))
                		   Connect.state.posX += speed;
                	   break;
				default:
					break;
				}
			}

			private boolean checkCollisions(KeyCode up) {
				switch (up) {
				case UP:
					int x = Connect.state.posX;
					int y = Connect.state.posY - speed;
					if( x< 0 || x >= 600 || y<0 || y>= 600)
						return false;
					if (map[x][y] == 1)
						return false;
					
					if (map[x + playerSize][y] == 1)
						return false;
					break;
				case DOWN:
					 x = Connect.state.posX ;
					 y = Connect.state.posY+ speed;
					 System.out.println(x + " ****************" + y);
					 if( x< 0 || x >= 600 || y<0 || y>= 600 || y+playerSize >600)
							return false;
					if (map[x+ playerSize][y+ playerSize] == 1)
						return false;
					
					if (map[x ][y+ playerSize] == 1)
						return false;
					break;
					
				case LEFT:
					 x = Connect.state.posX - speed;
					 y = Connect.state.posY ;
					 if( x< 0 || x >= 600 || y<0 || y>= 600)
							return false;
					if (map[x][y] == 1)
						return false;
					
					if (map[x ][y+ playerSize] == 1)
						return false;
					break;
				case RIGHT:
					 x = Connect.state.posX  + speed;
					 y = Connect.state.posY;
					 if( x< 0 || x >= 600 || y<0 || y>= 600 || x+playerSize >=601)
							return false;
					if (map[x+ playerSize][y] == 1)
						return false;
					
					if (map[x + playerSize][y + playerSize] == 1)
						return false;
					break;	
				default:
					break;
				}
				return true;
			}
		});
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public void readMap(String fileName){
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int nr = Integer.parseInt(br.readLine());
			
			for (int i = 0; i < nr; i++) {
				String[] line = br.readLine().split(" ");
				int x,y,width,height;
				x = Integer.parseInt(line[0]);
				y = Integer.parseInt(line[1]);
				width = Integer.parseInt(line[2]);
				height = Integer.parseInt(line[3]);
				Rectangle r = new Rectangle(width, height, Color.MAGENTA);
				r.setX(x);
				r.setY(y);
				root.getChildren().add(r);
				addToMap(x,y,width,height);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addToMap(int x, int y, int width, int height) {
		for (int i = x+1; i < x+ width-1; i++) {
			for (int j = y+1; j < y+height-1; j++) {
				map[i][j] = 1 ;
			}
		}
		
	}

	public static void main(String [] args) throws IOException{
        r = new Rectangle();
		Connect connection = new Connect();
		new Thread(connection).start();
		launch(args);
	}
}
