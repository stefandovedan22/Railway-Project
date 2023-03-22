package scene.main;

import scene.trains.Section;
import z.movement.M;

public class MapRailRoadSquare extends MapFieldSquare {
	
	private static final long serialVersionUID = 1L;
	private int railRoadID;
	M move1, move2, move3;
	private boolean occupied = false, crossing = false;
	Section section = MainViewController.getSection();
	
	public MapRailRoadSquare(int railRoadID, M move1, M move2, M move3) {
		super(javafx.scene.paint.Color.TRANSPARENT);
		this.createLock();
		this.railRoadID = railRoadID;
		this.move1 = move1;
		this.move2 = move2;
		this.move3 = move3;
	}
	
	public synchronized void changeColorCar() {
		square.setFill(javafx.scene.paint.Color.ROSYBROWN);
	}
	
	public synchronized void changeColorTruck() {
		square.setFill(javafx.scene.paint.Color.BROWN);
	}
	
	public void setRailRoadID(int railRoadID) {
		this.railRoadID = railRoadID;
	}
	
	@Override
	public int getRailRoadID() {
		return this.railRoadID;
	}
	
	@Override
	public boolean occupySquare() { //kretanje voza
		if(this.occupied == false) {
			this.occupied = true;
			return this.occupied;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isOccupied() { //kretanje voza
		return this.occupied;
	}
	
	@Override
	public void resetOccupy() { //kretanje voza
		this.occupied = false;
	}
	
	@Override
	public void crossCrossing(String current, int next) { //kada voz predje preko pruznog prelaza
		if(next == 0) {
			section.decBAR();
		} else if(next == 1) {
			if("A".equalsIgnoreCase(current)) {
				section.decABR();
			} else if("C".equalsIgnoreCase(current)) {
				section.decCBR();
			}
		} else if(next == 2) {
			if("B".equalsIgnoreCase(current)) {
				section.decBCR();
			} else if("E".equalsIgnoreCase(current)) {
				section.decECR();
			}
		} else if(next == 4) {
			section.decCER();
		}
	}
	
	@Override
	public boolean crossingCheck() { //pruzni prelaz
		return this.crossing;
	}
	
	@Override
	public boolean closeCrossing() { //zatvori pruzni prelaz za auta
		this.crossing = true;
		return true;
	}
	
	@Override
	public void resetCrossing() { //pruzni prelaz
		this.crossing = false;
	}
	
	@Override
	public M getRoadDirection() { //fja za vracanja smjera sa puta
		return this.move1;
	}
	
	@Override
	public M getMove(String destination) { //fja za vracanja smjera sa pruge
		if(railRoadID == 1 && "A".equalsIgnoreCase(destination)) {
			return this.move3; 
		} else if(railRoadID == 1) {
			return move2;
		} else if(railRoadID == 2 && ("A".equalsIgnoreCase(destination) || "B".equalsIgnoreCase(destination))) {
			return move3;
		} else if(railRoadID == 2) {
			return move2;
		} else if(railRoadID == 3 && "E".equalsIgnoreCase(destination)) {
			return move3;
		} else if (railRoadID == 3) {
			return move2;
		}
		return null;
	}
}
