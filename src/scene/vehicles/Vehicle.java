package scene.vehicles;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javafx.application.Platform;
import scene.main.*;
import y.logger.MyLogger;
import z.movement.M;

public abstract class Vehicle extends Thread {
	
	private String[] manufacturerCars = {"Renault", "Audi", "BMW", "Mazda", "Nissan", "Toyota", "Subaru", "Honda"};
	private String[] modelCars = {"Megane", "RS8", "M2 Competition", "MX-5", "GT-R", "Supra", "Impreza WRX", "Civic R"};
	private static MapFieldSquare[][] squares = MainViewController.getMainMap();
	
	protected String manufacturer;
	protected String model;
	protected int productionDate;
	protected double speed;
	protected int x, y, tempX, tempY;
	javafx.scene.paint.Color color;
	private boolean isEnd = false;
	
	public Vehicle(double speed, int x, int y, javafx.scene.paint.Color color) {
		this.speed = speed;
		int temp = RNG(0, 7);
		this.manufacturer = manufacturerCars[temp];
		this.model = modelCars[temp];
		this.productionDate = RNG(2010, 2021);
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	@Override
	public void run() {
		do { 
			try {
				if (squares[x][y].getLock().isHeldByCurrentThread() || 
						(squares[x][y].isOccupied()==false && squares[x][y].getLock().tryLock((long)(speed * 1000), TimeUnit.MILLISECONDS))) {
					Platform.runLater(() -> squares[x][y].setColor(color, false));
					
					sleep((long) (speed * 1000));
					
						isEnd = squares[x][y].isRoadEnd();
						if(isEnd) {
							Platform.runLater(() -> squares[x][y].resetColor(false));
							squares[x][y].getLock().unlock();
							break;
						}
					}
					if (checkNextPosition(x, y)) { 
						squares[x][y].resetColor(false);
						
						squares[x][y].getLock().unlock();
						changePosition();
					} 	
				 
			} catch (InterruptedException e1) {
				MyLogger.log(Level.WARNING, "InterruptedException", e1);			
			}
		} while (true);
	}
	
	private boolean checkNextPosition(int tempX, int tempY) { ///
		try {
			M direction = squares[x][y].getRoadDirection();
			if (direction == M.UP) {
				tempX = tempX - 1;
				return (squares[tempX][y].crossingCheck()==false && squares[tempX][y].getLock().tryLock((long)(speed * 1000), TimeUnit.MILLISECONDS));
			} else if (direction == M.RIGHT) {
				tempY = tempY + 1;
				return (squares[x][tempY].crossingCheck()==false && squares[x][tempY].getLock().tryLock((long)(speed * 1000), TimeUnit.MILLISECONDS));
			} else if (direction == M.DOWN) {
				tempX = tempX + 1;
				return (squares[tempX][y].crossingCheck()==false && squares[tempX][y].getLock().tryLock((long)(speed * 1000), TimeUnit.MILLISECONDS));
			} else {
				tempY = tempY - 1;
				return (squares[x][tempY].crossingCheck()==false && squares[x][tempY].getLock().tryLock((long)(speed * 1000), TimeUnit.MILLISECONDS));
			}
		} catch (InterruptedException e1) {
			MyLogger.log(Level.WARNING, "InterruptedException", e1);
		}
		return false;
	}
	
	private void changePosition() { ///
		M direction = squares[x][y].getRoadDirection();
		
		if(direction == M.UP) {
			this.x = this.x-1;
		} else if(direction == M.RIGHT) {
			this.y = this.y+1;
		} else if(direction == M.DOWN) {
			this.x = this.x+1;
		} else {
			this.y = this.y-1;
		}
	}

	public int RNG(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

}
