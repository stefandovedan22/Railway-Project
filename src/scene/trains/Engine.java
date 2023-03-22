package scene.trains;

import java.util.Random;
import z.movement.Drive;

public abstract class Engine extends Train {
	
	private static final long serialVersionUID = 1L;
	protected int power;
	protected String label;
	private static int ID = 0;
	private Drive drive;
	
	protected Engine(String label, javafx.scene.paint.Color color) {
		super(color);
		this.power = (new Random().nextInt() * 600 + 400);
		this.label = label + ID++;
	}
	
	public void setDrive(Drive drive) {
		this.drive = drive;
	}
	
	public Drive getDrive() {
		return this.drive;
	}
	
}
