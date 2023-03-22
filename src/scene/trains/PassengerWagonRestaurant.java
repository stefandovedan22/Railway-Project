package scene.trains;

public class PassengerWagonRestaurant extends PassengerWagon {
	
	private static final long serialVersionUID = 1L;
	private String description; //broj zvezdica
	
	public PassengerWagonRestaurant() {
		super(javafx.scene.paint.Color.LIGHTSEAGREEN);
		this.description = "**";
	}
	
	public String getDescription() {
		return (" " + this.description);
	}
}
