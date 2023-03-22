package scene.vehicles;

public class Car extends Vehicle  {
	
	private int numOfDoors;
	
	public Car(int numOfDoors, double speed, int x, int y) {
		super(speed, x, y, javafx.scene.paint.Color.ROSYBROWN);
		this.numOfDoors = numOfDoors;
		this.x=x;
		this.y=y;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public int getNumOfDoors() {
		return this.numOfDoors;
	}
	
}