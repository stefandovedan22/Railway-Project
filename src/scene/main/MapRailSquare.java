package scene.main;

import z.movement.M;

public class MapRailSquare extends MapFieldSquare {

	private static final long serialVersionUID = 1L;
	private int railID;
	private boolean occupation = false;
	M move1, move2;
	
	public MapRailSquare(int railID, M move1, M move2) {
		super(javafx.scene.paint.Color.TRANSPARENT);
		this.createLock();
		this.railID = railID;
		this.move1 = move1;
		this.move2 = move2;
	}
	
	
	public void setRailID(int railID, M move1, M move2) {
		this.railID = railID;
		this.move1 = move1;
		this.move2 = move2;
	}
	
	public void setMove(M move1, M move2) {
		this.move1 = move1;
		this.move2 = move2;
	}
	
	@Override
	public int getRailID() {
		return this.railID;
	}
	
	@Override
	public boolean occupySquare() { 
		if(this.occupation == false) {
			this.occupation = true;
			return this.occupation;
		} else {
			return false;
		}
	}
	
	@Override
	public void resetOccupy() {
		this.occupation = false;
	}
	
	@Override
	public M getMove(String destination) {
		if(railID == 1 && "A".equalsIgnoreCase(destination)) {
			return this.move2; 
		} else if(railID == 1) {
			return move1;
		} else if(railID == 2 && ("A".equalsIgnoreCase(destination) || "B".equalsIgnoreCase(destination))) {
			return move2;
		} else if(railID == 2) {
			return move1;
		} else if(railID == 3 && "D".equalsIgnoreCase(destination)) {
			return move1;
		} else if(railID == 3) {
			return move2;
		} else if(railID == 4 && "E".equalsIgnoreCase(destination)) {
			return move1;
		} else if (railID == 4) {
			return move2;
		}
		return null;
	}

}
