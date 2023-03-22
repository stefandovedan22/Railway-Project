package scene.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import y.logger.MyLogger;
import z.movement.M;

public class MapFieldSquare extends StackPane implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected static final double WIDTH = 36.0;
	protected static final double HEIGHT = 30.0;
	protected static final double objWIDTH = 23.0;
	protected static final double objHEIGHT = 20.0;
	
	protected ReentrantLock lock;
	
	protected Rectangle square; 
	protected Rectangle obj;
	
	public MapFieldSquare(javafx.scene.paint.Color color) {
		square = new Rectangle(WIDTH, HEIGHT);
		square.setFill(javafx.scene.paint.Color.TRANSPARENT); //javafx.scene.paint.Color.GHOSTWHITE
		square.setStroke(javafx.scene.paint.Color.BLACK);
		square.setArcHeight(0);
		square.setArcWidth(0);

		obj = new Rectangle(objWIDTH, objHEIGHT);
		obj.setFill(javafx.scene.paint.Color.TRANSPARENT);
		obj.setArcHeight(0);
		obj.setArcWidth(0);
		
		this.getChildren().add(square);
		this.getChildren().add(obj);
		this.setAlignment(Pos.CENTER);
	}
	
	//za stanicu(sa slikom)
	public MapFieldSquare(javafx.scene.paint.Color color, boolean border, String fl) { 
		try {
			if(border) {
				Image image = new Image(new FileInputStream("src"+File.separator+"scene"+File.separator+"main"+File.separator+
						"view"+File.separator+fl+".png"));
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(WIDTH*2);
				imageView.setFitHeight(HEIGHT*2);
				square = new Rectangle(WIDTH*2, HEIGHT*2);
				square.setStroke(javafx.scene.paint.Color.BLACK);
				square.setFill(color);	
				square.setArcHeight(0);
				square.setArcWidth(0);
				this.getChildren().add(square);
				this.getChildren().add(imageView);
				this.setAlignment(Pos.CENTER);
			} else {
				square = new Rectangle(WIDTH, HEIGHT);
				square.setFill(color);
				square.setArcHeight(0);
				square.setArcWidth(0);
				this.getChildren().add(square);
				this.setAlignment(Pos.CENTER);
			}
		} catch(FileNotFoundException e) {
			MyLogger.log(Level.WARNING, "FileNotFoundException", e);
		}
	}
	
	public synchronized void setColor(javafx.scene.paint.Color color, boolean voltage) {
		obj.setFill(color);
		if(voltage) {
			square.setStroke(javafx.scene.paint.Color.ORANGE);
		}
	}
	
	public synchronized void changeColorVoltage() {
		this.square.setStroke(javafx.scene.paint.Color.ORANGE);
	}
	
	public synchronized void resetColor(boolean voltage) {
		obj.setFill(javafx.scene.paint.Color.TRANSPARENT);
		if(voltage)
			square.setStroke(javafx.scene.paint.Color.BLACK);
	}
	
	public synchronized boolean startTrain() {
		return false;
	}
	
	public M getMove(String destination) { return null; }
	
	public void setRoadID(int roadID) {}
	public void setRoadDirection(M roadDirection) {}
	public int getRoadID() { return 0; }
	public boolean isRoadEnd() { return false; }
	
	public M getRoadDirection() { return null; }
	
	public M getRRoadDirection() { return null; }
	public M getRRailDirection(String destination) { return null; }
	
	
	public int getRailID() { return 0; }
	public boolean occupySquare() { return false; }
	public void resetOccupy() {}
	public int getRailRoadID() { return 0; }
	
	public void createLock() {
		this.lock = new ReentrantLock();
	}
	
	public ReentrantLock getLock() {
		return this.lock;
	}

	public boolean isOccupied() { return false; }
	public boolean crossingCheck() { return false; }
	public boolean closeCrossing() { return false; }

	public void resetCrossing() {}

	public void crossCrossing(String current, int next) {} 
	
}