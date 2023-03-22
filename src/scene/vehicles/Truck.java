package scene.vehicles;

public class Truck extends Vehicle {

	private int capacity;
	
	public Truck(int capacity, double speed, int x, int y) {
		super(speed, x, y, javafx.scene.paint.Color.BROWN);
		this.capacity = capacity;
		this.x=x;
		this.y=y;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
}