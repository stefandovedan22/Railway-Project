package scene.trains;

import java.util.Random;

public class PassengerWagonSeats extends PassengerWagon {

	private static final long serialVersionUID = 1L;
	private int numOfSeats;
	
	public PassengerWagonSeats() {
		super(javafx.scene.paint.Color.SEAGREEN);
		this.numOfSeats = new Random().nextInt(25) + 10;
	}
	
	public int getNumOfSeats() {
		return this.numOfSeats;
	}
	
}
