package scene.trains;

public class CargoEngine extends Engine {

	private static final long serialVersionUID = 1L;

	public CargoEngine() {
		super("CargoEngine", javafx.scene.paint.Color.MIDNIGHTBLUE);
	}
	
	public javafx.scene.paint.Color getColor() {
		return javafx.scene.paint.Color.MIDNIGHTBLUE;
	} 

}
