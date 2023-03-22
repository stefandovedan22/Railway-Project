package scene.main;

import z.movement.M;

public class MapRoadSquare extends MapFieldSquare {

	private static final long serialVersionUID = 1L;
	private int roadID;
	private M roadDirection;
	private boolean roadEnd = false;
	private boolean roadStart = false;
			
	public MapRoadSquare(M roadDirection, int roadID) {
		super(javafx.scene.paint.Color.TRANSPARENT);
		this.roadID = roadID;
		this.roadDirection = roadDirection;
		this.createLock();
	}
	
	@Override
	public void setRoadID(int roadID) {
		this.roadID = roadID;
	}
	
	@Override
	public int getRoadID() {
		return this.roadID;
	}
	
	@Override
	public void setRoadDirection(M roadDirection) {
		this.roadDirection = roadDirection;
	}
	
	@Override
	public M getRoadDirection() {
		return this.roadDirection;
	}
	
	@Override
	public boolean occupySquare() { 
		return false;
	}
	
	@Override
	public boolean isOccupied() {
		return false;
	}
	
	public void setRoadEnd(boolean roadEnd) {
		this.roadEnd = roadEnd;
	}
	
	@Override
	public boolean isRoadEnd() {
		return this.roadEnd;
	}
	
	public void setRoadStart(boolean roadStart) {
		this.roadStart = roadStart;
	}
	
	public boolean isRoadStart() {
		return this.roadStart;
	}
}
