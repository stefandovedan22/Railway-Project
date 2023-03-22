package scene.trains;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;

import javafx.application.Platform;
import scene.main.MainViewController;
import scene.main.MapFieldSquare;
import scene.main.MapTrainStation;
import y.logger.MyLogger;
import z.movement.Drive;
import z.movement.M;

public class TrainComposition extends Thread {

	private int length, ID;
	private static MapFieldSquare[][] squares = MainViewController.getMainMap();
	private int enginesCount, wagonCount;
	private double speed, ogSpeed;
	private String startStation, destinationStation, currentStation, path;
	private boolean voltage = false, clearFlag = false, crossingFlag = false, once = false;
	private LinkedList<Train> train = new LinkedList<Train>();
	private static LinkedList<MapTrainStation> stations = MainViewController.getStations();
	
	private String serializeTrain="";
	
	private int x, y;
	M direction;
	
	public TrainComposition(String[] line1, String[] line2, String[] line3, String[] line4, String path) {
		this.enginesCount = Integer.parseInt(line1[1]);	
		this.wagonCount = Integer.parseInt(line2[1]);
		this.speed = Double.parseDouble(line3[1]);
		this.ogSpeed = Double.parseDouble(line3[1]);
		this.length = this.enginesCount + this.wagonCount;
		this.startStation = this.currentStation = line4[1];
		this.destinationStation = line4[3];
		this.path = path;
		this.ID = MainViewController.getTrainID();
		
		if("A".equalsIgnoreCase(startStation)) {
			x = 27;
			y = 2;
		} else if("B".equalsIgnoreCase(startStation)) {
			if("A".equalsIgnoreCase(destinationStation)) {
				x = 6;
				y = 6;
			} else {
				x = 6;
				y = 7;
			}
		} else if("C".equalsIgnoreCase(startStation)) {
			if("A".equalsIgnoreCase(destinationStation) || "B".equalsIgnoreCase(destinationStation)) {
				x = 12;
				y = 19;
			} else if("D".equalsIgnoreCase(destinationStation)) {
				x = 12;
				y = 20;
			} else {
				x = 13;
				y = 20;
			} 
		} else if("D".equalsIgnoreCase(startStation)) {
			x = 1;
			y = 26;
		} else {
			x = 25;
			y = 26;
		}
		
		Drive rand = Drive.values()[new Random().nextInt(Drive.values().length)]; //pogon lokomotive
		if(rand.equals(Drive.ELECTRIC)) {
			this.voltage = true;
		}
		
		for(int i=0; i<this.enginesCount; i++) { //formiranje kompozicije lokomotive i vagoni
			if("PE".equalsIgnoreCase(line1[2+i])) {
				train.add(new PassengerEngine());
			} else if("CE".equalsIgnoreCase(line1[2+i])) {
				train.add(new CargoEngine());
			} else if("UE".equalsIgnoreCase(line1[2+i])) {
				train.add(new UniversalEngine());
			} else if("RE".equalsIgnoreCase(line1[2+i])) {
				train.add(new RepairEngine());
			}
			((Engine)train.get(i)).setDrive(rand); //random tip pogona STEAM, DIESEL, ELECTRIC
		}
		
		for(int i=0; i<this.wagonCount; i++) {
			if("PWB".equalsIgnoreCase(line2[2+i])) {
				train.add(new PassengerWagonBeds());
			} else if("PWR".equalsIgnoreCase(line2[2+i])) {
				train.add(new PassengerWagonRestaurant());
			} else if("PWSE".equalsIgnoreCase(line2[2+i])) {
				train.add(new PassengerWagonSeats());
			} else if("PWSL".equalsIgnoreCase(line2[2+i])) {
				train.add(new PassengerWagonSleeping());
			} else if("CW".equalsIgnoreCase(line2[2+i])) {
				train.add(new CargoWagon());
			} else if("SW".equalsIgnoreCase(line2[2+i])) {
				train.add(new SpecialWagon());
			}
		}
		
		if(rand.equals(Drive.ELECTRIC)) {
			train.add(0, new Voltage());
			train.add(new Voltage());
		}
		train.getLast().setLastPart(); //oznacavamo zadnji vagon(polje pod naponom) kako bi smo mogli oslobadjati pruzne prelaze
		
		for(Train temp : train) {
			temp.setCords(x, y); //postavljanje koordinata na pocetnu stanicu
		}
		
	}
	
	@Override
	public void run() {
		do {
			if(isInStation()) { //provjera da li je voz u stanici
				if(this.currentStation.equals(this.destinationStation)) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = new Date();
					this.serializeTrain = serializeTrain + ("\nDosao na odrediste: " + formatter.format(date) + "\n");
					try {
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + 
								"\\trainLine"+this.ID+".ser"));
						out.writeObject(this.serializeTrain);
						out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				if(this.currentStation.equals(startStation)) {
					if(!once) {
						this.serializeTrain = serializeTrain + ("Pocetna stanica: " + this.startStation + " Odredisna stanica: " + 
								this.destinationStation +  "\n");
						this.once = true;
					}
				}
				this.crossingFlag = false;
				this.speed = this.ogSpeed;
				try {
					sleep(new Random().nextInt(1)*500);
				} catch (InterruptedException e) {}
				if("A".equalsIgnoreCase(currentStation)) {
					if(stations.get(0).isSectionAvailable(currentStation, destinationStation)) { 
						stations.get(0).deployTrain(destinationStation, this.speed);
						int next = calculateNextStation();
						this.speed = stations.get(next).getSpeed(this.currentStation, destinationStation);
						this.clearFlag = true;
						System.out.println("Moja brzina: " + this.speed); 
						if(this.speed == 0)
							this.speed = this.ogSpeed;
						this.x = stations.get(0).getX();
						this.y = stations.get(0).getY();
						for(Train temp : train) {
							temp.setCords(x, y);
						}
						
						if(this.currentStation.equals(startStation)) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + 
									" Vrijeme polaska: " + formatter.format(date) + "\n");
						} else {
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + "\n");
						}
						
						moveTrainFromStation(); 
					} else {
						try {
							sleep((long) this.speed*500); //pocetno vrijeme za kad krene u stanicu i kad stigne
						} catch (InterruptedException e) {}
					}
				} else if("B".equalsIgnoreCase(currentStation)) {
					if(stations.get(1).isSectionAvailable(currentStation, destinationStation)) {
						stations.get(1).deployTrain(destinationStation, this.speed);
						this.clearFlag = true;
						int next = calculateNextStation();
						this.speed = stations.get(next).getSpeed(this.currentStation, destinationStation);
						System.out.println("Moja brzina: " + this.speed); 
						if(this.speed == 0)
							this.speed = this.ogSpeed;
						this.x = stations.get(1).getX();
						this.y = stations.get(1).getY();
						for(Train temp : train) {
							temp.setCords(x, y);
						}
						if(this.currentStation.equals(startStation)) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + 
									" Vrijeme polaska: " + formatter.format(date) + "\n");
						} else {
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + "\n");
						}
						
						moveTrainFromStation();
					}
				} else if("C".equalsIgnoreCase(currentStation)) {
					if(stations.get(2).isSectionAvailable(currentStation, destinationStation)) {
						stations.get(2).deployTrain(destinationStation, this.speed);
						int next = calculateNextStation();
						this.speed = stations.get(next).getSpeed(this.currentStation, destinationStation);
						System.out.println("Moja brzina: " + this.speed); 
						if(this.speed == 0)
							this.speed = this.ogSpeed;
						this.x = stations.get(2).getX();
						this.y = stations.get(2).getY();
						for(Train temp : train) {
							temp.setCords(x, y);
						}
						if(this.currentStation.equals(startStation)) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + 
									" Vrijeme polaska: " + formatter.format(date) + "\n");
						} else {
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + "\n");
						}
						this.clearFlag = true;
						moveTrainFromStation();
					}
				} else if("D".equalsIgnoreCase(currentStation)) {
					if(stations.get(3).isSectionAvailable(currentStation, destinationStation)) {
						stations.get(3).deployTrain(destinationStation, this.speed);
						int next = calculateNextStation();
						this.speed = stations.get(next).getSpeed(this.currentStation, destinationStation);
						System.out.println("Moja brzina: " + this.speed); 
						if(this.speed == 0)
							this.speed = this.ogSpeed;
						this.x = stations.get(3).getX();
						this.y = stations.get(3).getY();
						for(Train temp : train) {
							temp.setCords(x, y);
						}
						if(this.currentStation.equals(startStation)) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + 
									" Vrijeme polaska: " + formatter.format(date) + "\n");
						} else {
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + "\n");
						}
						this.clearFlag = true;
						moveTrainFromStation();
					}
				} else if("E".equalsIgnoreCase(currentStation)) {
					if(stations.get(4).isSectionAvailable(currentStation, destinationStation)) {
						stations.get(4).deployTrain(destinationStation, this.speed);
						int next = calculateNextStation();
						this.speed = stations.get(next).getSpeed(this.currentStation, destinationStation);
						System.out.println("Moja brzina: " + this.speed); 
						if(this.speed == 0)
							this.speed = this.ogSpeed;
						this.x = stations.get(4).getX();
						this.y = stations.get(4).getY();
						for(Train temp : train) {
							temp.setCords(x, y);
						}
						if(this.currentStation.equals(startStation)) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + 
									" Vrijeme polaska: " + formatter.format(date) + "\n");
						} else {
							this.serializeTrain = serializeTrain + ("\nStanica: " + this.currentStation + "\n");
						}
						this.clearFlag = true;
						moveTrainFromStation();
					}
				} else {
					try {
						sleep((long) (1000));
					} catch (InterruptedException e) {}
				}
			} else {
				moveTrain();
			}
		} while(true); 
	}

	/**
	 * funkcija koja vrsi pomjeranje voza 
	 */
	private void moveTrain() {
		try {
			if(stations.get(calculateNextStation()).checkCrossing(squares[train.getLast().x][train.getLast().y].getRailID())) {
				freeCrossing(train.getLast().x, train.getLast().y);
			}
			for(Train component : train) {
				if(squares[component.x][component.y] instanceof MapTrainStation) {
				} else {
					Platform.runLater(() -> squares[component.x][component.y].setColor(component.getColor(), voltage));
				}
			}
			this.serializeTrain = serializeTrain + (train.getFirst().toString());
			if(!this.crossingFlag) {
				this.crossingFlag = holdCrossing(train.getFirst().x, train.getFirst().y);
			}
			sleep((long) (speed * 1000)); 

			for(Train component : train) {
				if(squares[component.x][component.y] instanceof MapTrainStation) {
				} else {
					if(component.checkNextPosition(component.x, component.y, destinationStation)) {
						squares[component.x][component.y].resetColor(voltage);
						squares[component.x][component.y].resetOccupy();
						if(component.getLastPart()) {
							squares[component.x][component.y].crossCrossing(this.currentStation, this.calculateNextStation());
						}
						component.changePosition(destinationStation);
					} else {
						sleep((long) (speed * 1000)); 
						return;
					}
				}
			}
		} catch (InterruptedException e) {
			MyLogger.log(Level.WARNING, "InterruptedException", e);
		}
	}
	/**
	 * funkcija koja vrsi pomjeranje voza iz stanice
	 */
	private synchronized void moveTrainFromStation() {
		try {
			this.clearFlag = true;
			for(int i=0; i<train.size(); i++) {
				for(int j=0; j<=i; j++) {
					Train component = train.get(j);
					Platform.runLater(() -> squares[component.x][component.y].setColor(component.getColor(), voltage));
				}
				if(!this.crossingFlag) {
					this.crossingFlag = holdCrossing(train.getFirst().x, train.getFirst().y);
				}
				this.serializeTrain = serializeTrain + (train.getFirst().toString());
				sleep((long) (speed * 1000));
				
				for(int j=0; j<=i; j++) {
					Train component = train.get(j);
					squares[component.x][component.y].resetColor(voltage);
					squares[component.x][component.y].resetOccupy();
					if(component.getLastPart()) {
						squares[component.x][component.y].crossCrossing(this.currentStation, this.calculateNextStation());
						}
					component.changePosition(destinationStation);
				}
				if(stations.get(calculateNextStation()).checkCrossing(squares[train.getLast().x][train.getLast().y].getRailRoadID())) {
					freeCrossing(train.getLast().x, train.getLast().y);
				}
			}
			stations.get(calculateNextStation()).clearStart(startStation);
		} catch (InterruptedException e) {
			MyLogger.log(Level.WARNING, "InterruptedException", e);
		}
	}
	
	private int calculateNextStation() { 
		if(currentStation.equalsIgnoreCase("A")) {
			return 1;
		} else if(currentStation.equals("B") && destinationStation.equals("A")) {
			return 0;
		} else if(currentStation.equals("B")) {
			return 2;
		} else if(currentStation.equals("C") && (destinationStation.equals("B") || destinationStation.equals("A"))) {
			return 1;
		} else if(currentStation.equals("C") && destinationStation.equals("D")) {
			return 3;
		} else if(currentStation.equals("C")) {
			return 4;
		} else if(currentStation.equals("D") || currentStation.equals("E")) {
			return 2;
		}
		return 0;
	}
	
	private boolean holdCrossing(int X, int Y) { //zatvaranje pruznog prelaza
		if(squares[X][Y].getRailID() == 1) {
			if(squares[20][2].closeCrossing() && squares[21][2].closeCrossing()) {
				return true;
			}
		} else if(squares[X][Y].getRailID() == 2) {
			if(squares[6][13].closeCrossing() && squares[6][14].closeCrossing()) {
				return true;
			}
		} else if(squares[X][Y].getRailID() == 4) {
			if(squares[20][26].closeCrossing() && squares[21][26].closeCrossing()) {
				return true;
			}
		} 
		return false;
	}
	
	private boolean freeCrossing(int X, int Y) { //oslobadjanje pruznog prelaza
		if(squares[X][Y].getRailID() == 1) {
			squares[20][2].resetCrossing();
			squares[21][2].resetCrossing();
			return true;
		} else if(squares[X][Y].getRailID() == 2) {
			squares[6][13].resetCrossing();
			squares[6][14].resetCrossing();
			return true;
		} else if(squares[X][Y].getRailID() == 4) {
			squares[20][26].resetCrossing();
			squares[21][26].resetCrossing();
			return true;
		} 
		return false;
	}
	
	public synchronized boolean isInStation() { //provjera da li je voz u stanici(svaka komponenta kompozicije) 
		for(Train temp : train) {
			if(temp.location() == false)
				return false;				
		}
		this.currentStation = ((MapTrainStation)squares[train.get(0).x][train.get(0).y]).getTrainStation();
		if(this.currentStation.equals(this.startStation) == false && this.clearFlag) {
			this.clearFlag = false;
			if(this.currentStation.equals("A")) {
				stations.get(0).clearSectionEntry(startStation);
			} else if(this.currentStation.equals("B")) {
				stations.get(1).clearSectionEntry(startStation);
			} else if(this.currentStation.equals("C")) {
				stations.get(2).clearSectionEntry(startStation);
			} else if(this.currentStation.equals("D")) {
				stations.get(3).clearSectionEntry(startStation);
			} else if(this.currentStation.equals("E")) {
				stations.get(4).clearSectionEntry(startStation);
			}
		}
		try {
			sleep((long) (speed * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void setTrainSpeed(double speed) {
		this.speed = speed;
	}
	
	public void resetSpeed() {
		this.speed = ogSpeed;
	}
		
	public int getLength() {
		return this.length;
	}

}
