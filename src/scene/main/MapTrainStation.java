package scene.main;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import scene.trains.Section;

public class MapTrainStation extends MapFieldSquare {

	private static final long serialVersionUID = 1L;
	private String trainStationName;
	private int stationCount = 0;
	private int xBase, yBase, x, y;
	
	LinkedList<Thread> trains = MainViewController.getTrains(); //lista vozova
	Section section = MainViewController.getSection(); //lista sekcija
	
	public MapTrainStation(String trainStationName) {
		super(javafx.scene.paint.Color.LIGHTSLATEGRAY);
		this.trainStationName = trainStationName;
		
		System.out.println("Stanica " + this.xBase + " " + this.yBase);
	}
	
	public MapTrainStation(String trainStationName, boolean border) throws FileNotFoundException {
		super(javafx.scene.paint.Color.LIGHTSLATEGRAY, border, trainStationName);
		this.trainStationName = trainStationName;
		switch(this.trainStationName) {
		case "A" :
			this.xBase = 1;
			this.yBase = 27;
		case "B" :
			this.xBase = 6;
			this.yBase = 5;
		case "C" :
			this.xBase = 19;
			this.yBase = 12;
		case "D" :
			this.xBase = 26;
			this.yBase = 1;
		case "E" :
			this.xBase = 25;
			this.yBase = 25;
			}
	}
	
	/**
	 * funkcija provjerava da li je trazena sekcija slobodna da voz krene sa stanice
	 * @param currentStation trenutna stanica na kojoj se nalazi voz
	 * @param destination destinacija
	 * @return
	 */
	public synchronized boolean isSectionAvailable(String currentStation, String destination) {
		if("A".equalsIgnoreCase(currentStation)) {
			if(section.getBA() == 0 && !section.getS1()) {
				section.incAB();
				section.incABR();
				section.incABR();
				section.setS1();
				return true;
			}
		} else if("B".equalsIgnoreCase(currentStation)) {
			if("A".equals(destination)) {
				if(section.getAB() == 0 && !section.getS1()) {
					section.incBA();
					section.incBAR();
					section.incBAR();
					section.setS1();
					return true;
				}
			} else {
				if(section.getCB() == 0 && !section.getS2()) {
					section.incBC();
					section.incBCR();
					section.incBCR();
					section.setS2();
					return true;
				}
			}
		} else if("C".equalsIgnoreCase(currentStation)) {
			if("A".equals(destination) || "B".equals(destination)) {
				if(section.getBC() == 0 && !section.getS2()) {
					section.incCB();
					section.incCBR();
					section.incCBR();
					section.setS2();
					return true;
				}
			} else if("D".equals(destination)) {
				if(section.getDC() == 0 && !section.getS3()) {
					section.incCD();
					section.setS3();
					return true;
				}
			} else {
				if(section.getEC() == 0 && !section.getS4()) {
					section.incCE();
					section.incCER();
					section.incCER();
					section.setS4();
					return true;
				}
			}
		} else if("D".equalsIgnoreCase(currentStation)) {
			if(section.getCD() == 0 && !section.getS3()) {
				section.incDC();
				section.setS3();
				return true;
			}
		} else if("E".equalsIgnoreCase(currentStation)) {
			if(section.getCE() == 0 && !section.getS4()) {
				section.incEC();
				section.incECR();
				section.incECR();
				section.setS4();
				return true;
			}
		}
		return false;
	} 
	
	/**
	 * funkcija daje vozu pocetne koordinate te upravlja brzinom sekcije
	 * @param destinationStation
	 * @param speed brzina voza
	 */
	public synchronized void deployTrain(String destinationStation, double speed) { 
		if("A".equalsIgnoreCase(this.trainStationName)) { 
			x = 26;
			y = 2;
			if(section.getSpeed(1) == 0) {
				section.setSpeed(1, speed);
			} else if(section.getSpeed(1) > speed && section.getAB() == 0) {
				section.setSpeed(1, speed);
			} else if(section.getSpeed(1) < speed) {
				section.setSpeed(1, speed);
			}
		} else if("B".equalsIgnoreCase(this.trainStationName)) {
			if("A".equalsIgnoreCase(destinationStation)) {
				x = 6;
				y = 5;
				if(section.getSpeed(1) == 0) {
					section.setSpeed(1, speed);
				} else if(section.getSpeed(1) > speed && section.getBA() == 0) {
					section.setSpeed(1, speed);
				} else if(section.getSpeed(1) < speed) {
					section.setSpeed(1, speed);
				}
			} else {
				x = 6;
				y = 8;
				if(section.getSpeed(2) == 0) {
					section.setSpeed(2, speed);
				} else if(section.getSpeed(2) > speed && section.getBC() == 0) {
					section.setSpeed(2, speed);
				} else if(section.getSpeed(2) < speed) {
					section.setSpeed(2, speed);
				}
			}
		} else if("C".equalsIgnoreCase(this.trainStationName)) {
			if("A".equalsIgnoreCase(destinationStation) || "B".equalsIgnoreCase(destinationStation)) {
				x = 11;
				y = 19;
				if(section.getSpeed(2) == 0) {
					section.setSpeed(2, speed);
				} else if(section.getSpeed(2) > speed && section.getCB() == 0) {
					section.setSpeed(2, speed);
				}  else if(section.getSpeed(2) < speed) {
					section.setSpeed(2, speed);
				}
			} else if("D".equalsIgnoreCase(destinationStation)) {
				x = 12;
				y = 21;
				if(section.getSpeed(3) == 0) {
					section.setSpeed(3, speed);
				} else if(section.getSpeed(3) > speed && section.getCD() == 0) {
					section.setSpeed(3, speed);
				} else if(section.getSpeed(3) < speed) {
					section.setSpeed(3, speed);
				}
			} else {
				x = 14;
				y = 20;
				if(section.getSpeed(4) == 0) {
					section.setSpeed(4, speed);
				} else if(section.getSpeed(4) > speed && section.getCE() == 0) {
					section.setSpeed(4, speed);
				} else if(section.getSpeed(4) < speed) {
					section.setSpeed(4, speed);
				}
			}
		} else if("D".equalsIgnoreCase(this.trainStationName)) {
			x = 1;
			y = 25;
			if(section.getSpeed(3) == 0) {
				section.setSpeed(3, speed);
			} else if(section.getSpeed(3) > speed && section.getDC() == 0) {
				section.setSpeed(3, speed);
			} else if(section.getSpeed(3) < speed) {
				section.setSpeed(3, speed);
			}
		} else {
			x = 24;
			y = 26;
			if(section.getSpeed(4) == 0) {
				section.setSpeed(4, speed);
			} else if(section.getSpeed(4) > speed && section.getEC() == 0) {
				section.setSpeed(4, speed);
			} else if(section.getSpeed(4) < speed) {
				section.setSpeed(4, speed);
			}
		}
	}
	
	public synchronized void clearStart(String startStation) { //kada voz kompletno napusti stanicu daje dozvolu da sledeci voz moze krenuti
		if("A".equalsIgnoreCase(trainStationName) ) {
			section.resetS1();
		} else if("B".equalsIgnoreCase(trainStationName)) {
			if("A".equalsIgnoreCase(startStation)) {
				section.resetS1();
			} else {
				section.resetS2();
			}
		} else if("C".equalsIgnoreCase(trainStationName)) {
			if("A".equalsIgnoreCase(startStation) || "B".equalsIgnoreCase(startStation)) {
				section.resetS2();
			} else if("D".equalsIgnoreCase(startStation)) {
				section.resetS3();
			} else {
				section.resetS4();
			}
		} else if("D".equalsIgnoreCase(trainStationName)) {
			section.resetS3();
		} else {
			section.resetS4();
			}
	}
	
	public synchronized boolean checkCrossing(int ID) { //provjerava da li voz moze osloboditi pruzni prelaz
		if("A".equalsIgnoreCase(this.getTrainStation())) {
			if(section.getBAR() == 0) {
				return true;
			}
		} else if("B".equalsIgnoreCase(this.getTrainStation())) {
			if(ID == 1) {
				if(section.getABR() == 0) {
					return true;
				} 
			} else if(ID == 2) {
				if(section.getCBR() == 0) {
					return true;
				}
			}
		} else if("C".equalsIgnoreCase(this.getTrainStation())) {
			if(ID == 2) {
				if(section.getBCR() == 0) {
					return true;
				}
			} else if(ID == 4) {
				if(section.getECR() == 0) {
					return true;
				}
			}
		} else if("E".equalsIgnoreCase(this.getTrainStation())) {
			if(section.getCER() == 0) {
				return true; 
			}
		}
		return false;
	}
	
	public synchronized void clearSectionEntry(String startStation) { //smanjuje brojac koliko ima vozova na trenutnoj sekciji
		if(this.trainStationName.equals("A")) {
			section.decBA();
			if(section.getBA() == 0) {
				section.setSpeed(1, 0);
			}
		} else if(this.trainStationName.equals("B")) {
			if(startStation.equals("A")) {
				section.decAB();
				if(section.getAB() == 0)
					section.setSpeed(1, 0);
			} else {
				section.decCB();
				if(section.getCB() == 0)
					section.setSpeed(2, 0);
			}
		} else if(this.trainStationName.equals("C")) {
			if(startStation.equals("A") || startStation.equals("B")) {
				section.decBC();
				if(section.getBC() == 0)
					section.setSpeed(2, 0);
			} else if(startStation.equals("D")) {
				section.decDC();
				if(section.getDC() == 0)
					section.setSpeed(3, 0);
			} else {
				section.decEC();
				if(section.getEC() == 0)
					section.setSpeed(4, 0);
			}
			System.out.println(section.getBC());
		} else if(this.trainStationName.equals("D")) {
			section.decCD();
			if(section.getCD() == 0)
				section.setSpeed(3, 0);
		} else {
			section.decCE();
			if(section.getCE() == 0)
				section.setSpeed(4, 0);
			}
		}
	
	public synchronized double getSpeed(String current, String destination) { //funkcija vraca brzinu odredjene sekcije na osnovu pozicije voza
		if(this.trainStationName.equals("A")) {
			return section.getSpeed(1);
		} else if(this.trainStationName.equals("B") && (destination.equals("A") || destination.equals("B"))) {
			return section.getSpeed(1);
		} else if(this.trainStationName.equals("B")) {
			return section.getSpeed(1);
		} else if(this.trainStationName.equals("C") && (current.equals("A") || current.equals("B"))) {
			return section.getSpeed(2);
		} else if(this.trainStationName.equals("C") && current.equals("D")) {
			return section.getSpeed(3);
		} else if(this.trainStationName.equals("C") && current.equals("E")) {
			return section.getSpeed(4);
		} else if(this.trainStationName.equals("D")) {
			return section.getSpeed(3);
		} else if(this.trainStationName.equals("E")) {
			return section.getSpeed(4);
		}
		return 0.0;
	}
	
	public synchronized int getStationCount() {
		return this.stationCount;
	}
	
	public synchronized void incStationCount() {
		this.stationCount++;
	}
	
	public synchronized void decStationCount() {
		this.stationCount--;
	}

	@Override
	public boolean occupySquare() { 
		return true;
	}
	
	public String getTrainStation() {
		return this.trainStationName;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
