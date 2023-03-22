package scene.trains;

import scene.main.MainViewController;
import scene.main.MapFieldSquare;
import z.movement.M;

import scene.main.*;

public abstract class Train implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	protected int x, y;
	private static MapFieldSquare[][] squares = MainViewController.getMainMap();
	javafx.scene.paint.Color color;
	private boolean lastPart = false;
	
	protected Train(javafx.scene.paint.Color color) {
		this.color = color;
	}
	
	public javafx.scene.paint.Color getColor() {
		return this.color;
	}
	
	public void changePosition(String destination) { //promjena pozicije elementa
		M direction = squares[x][y].getMove(destination);
		
		if(direction == M.UP) {
			this.x = this.x-1;
		} else if(direction == M.RIGHT) {
			this.y = this.y + 1;
		} else if(direction == M.DOWN) {
			this.x = this.x + 1;
		} else {
			this.y = this.y - 1;
		}
	}
	
	public boolean checkNextPosition(int tempX, int tempY, String destination) { //provjera da li se moze pomjeriti na seledece polje
		if(squares[x][y] instanceof MapTrainStation) {
			return false;
		} else {
			M direction = squares[x][y].getMove(destination);
			if (direction == M.UP) {
				tempX = tempX - 1;
				if(squares[tempX][y] instanceof MapRailRoadSquare) {
					return true;
				} else {
					return squares[tempX][y].occupySquare();
				}
			} else if (direction == M.RIGHT) {
				tempY = tempY + 1;
				if(squares[x][tempY] instanceof MapRailRoadSquare) {
					return true;
				} else {
					return squares[x][tempY].occupySquare();
				}
			} else if (direction == M.DOWN) {
				tempX = tempX + 1;
				if(squares[tempX][y] instanceof MapRailRoadSquare) {
					return true;
				} else {
					return squares[tempX][y].occupySquare();
				}
			} else {
				tempY = tempY - 1;
				return squares[x][tempY].occupySquare();
			}
		}
	}
	
	public boolean location() {
		if(squares[x][y] instanceof MapTrainStation) {
			return true; //provjera elementa kompozicije da li je na polju stanice
		}
		return false;
	}
	
	public boolean tryLockCrossing(int X, int Y) {
		return squares[X][Y].occupySquare();
	}
	
	public void setCords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLastPart() {
		this.lastPart = true;
	}
	
	public boolean getLastPart() {
		return this.lastPart;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}
	
}
