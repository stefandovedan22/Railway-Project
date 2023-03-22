package scene.trains;

import java.util.Random;

public class PassengerWagonBeds extends PassengerWagon {

	private static final long serialVersionUID = 1L;
	private int numOfBeds;
	
	public PassengerWagonBeds() {
		super(javafx.scene.paint.Color.MEDIUMSEAGREEN);
		this.numOfBeds = new Random().nextInt(10) + 10;
	}
	
	public int getNumOfBeds() {
		return this.numOfBeds;
	}
}
