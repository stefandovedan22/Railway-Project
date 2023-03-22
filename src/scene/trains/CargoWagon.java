package scene.trains;

import java.util.Random;

public class CargoWagon extends Wagon {
	
	private static final long serialVersionUID = 1L;
	private double maxCapacity;
	
	public CargoWagon() {
		super(javafx.scene.paint.Color.BLUE);
		this.maxCapacity = new Random().nextDouble() * 2000.0 + 100.0;
	}
	
	public double getMaxCapacity() {
		return this.maxCapacity;
	}

}
