package scene.trains;

import java.util.Random;

public abstract class Wagon extends Train {

	private static final long serialVersionUID = 1L;
	protected double length;
	protected static int ID = 0;
	protected String label;
	
	protected Wagon(javafx.scene.paint.Color color) {
		super(color);
		this.length = (new Random().nextDouble() * 7.0 + 8.0); //izmedju 8 i 15 metara
		this.label = "Wagon" + Wagon.ID++;
	}
	
	public String getLabel() {
		return this.label;
	}

}
