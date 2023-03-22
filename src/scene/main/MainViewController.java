package scene.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import scene.trains.Section;
import scene.vehicles.Car;
import scene.vehicles.Truck;
import watcher.*;
import y.logger.MyLogger;
import z.movement.M;

/**
 * 
 * @author Stefan
 * Kontroller glavnog prozora
 */
public class MainViewController { 
	private static final int ROWS = 30;
	private static final int COLUMNS = 30;
	
	private static LinkedList<Thread> trains = new LinkedList<Thread>();
	private static LinkedList<MapTrainStation> stations = new LinkedList<MapTrainStation>();
	private static MapFieldSquare[][] squares = new MapFieldSquare[ROWS][COLUMNS];
	private static Section section = new Section();
	private static int trainID = 0;
	
	@FXML
	GridPane grid;
	@FXML
	Button btnStart;
	@FXML
	Button btnTrains;
	@FXML
	Label trainsLabel;
	@FXML
	private void showMovementLines() throws IOException {
		Main.showMovementLinesScene();
	}
	
	public void showGUI() { ///////////////////////////////////////////////////////////////////////////////////
		try {
			for(int i=0; i<ROWS; i++) {
				for(int j=0; j<COLUMNS; j++) {
					squares[i][j] = new MapFieldSquare(javafx.scene.paint.Color.TRANSPARENT);
				}
			}

			//put
			for(int i=0; i<ROWS; i++) {
				for(int j=13; j<=14; j++) {
					if(j==13) {
						squares[i][j] = new MapRoadSquare(M.DOWN, 2);
					} else {
						squares[i][j] = new MapRoadSquare(M.UP, 2);
					} 
				}
			}
			for(int i=20; i<=21; i++) { 
				for(int j=0; j<=8; j++) {
					if(i==20) {
						squares[i][j] = new MapRoadSquare(M.LEFT, 1);
					} else {
						squares[i][j] = new MapRoadSquare(M.RIGHT, 1);
					}
				}
			}
			for(int i=20; i<=21; i++) {
				for(int j=21; j<=29; j++) {
					if(i==20) {
						squares[i][j] = new MapRoadSquare(M.LEFT, 3);
					} else {
						squares[i][j] = new MapRoadSquare(M.RIGHT, 3);
					}
				}
			}
			for(int i=22; i<ROWS; i++) {
				for(int j=7; j<=8; j++) {
					if(j==7) {
						squares[i][j] = new MapRoadSquare(M.DOWN, 1);
					} else {
						squares[i][j] = new MapRoadSquare(M.UP, 1);
					}
				}
			}
			for(int i=22; i<ROWS; i++) {
				for(int j=21; j<=22; j++) {
					if(j==21) {
						squares[i][j] = new MapRoadSquare(M.DOWN, 3);
					} else {
						squares[i][j] = new MapRoadSquare(M.UP, 3);
					}
				}
			} 
			//fixovi smijera
			squares[21][8].setRoadDirection(M.UP);
			squares[21][7].setRoadDirection(M.DOWN);
			squares[20][21].setRoadDirection(M.DOWN);
			squares[21][21].setRoadDirection(M.DOWN);
			//pocetak-kraj puta
			((MapRoadSquare)squares[20][0]).setRoadEnd(true); //pu1
			((MapRoadSquare)squares[21][0]).setRoadStart(true);
			((MapRoadSquare)squares[29][7]).setRoadEnd(true);
			((MapRoadSquare)squares[29][8]).setRoadStart(true);

			((MapRoadSquare)squares[29][13]).setRoadEnd(true); //pu2
			((MapRoadSquare)squares[29][14]).setRoadStart(true);
			((MapRoadSquare)squares[0][14]).setRoadEnd(true);
			((MapRoadSquare)squares[0][13]).setRoadStart(true);

			((MapRoadSquare)squares[29][21]).setRoadEnd(true); //put3
			((MapRoadSquare)squares[29][22]).setRoadStart(true);
			((MapRoadSquare)squares[20][29]).setRoadStart(true);
			((MapRoadSquare)squares[21][29]).setRoadEnd(true); 

			//pruga
			for(int i=16; i<ROWS; i++) {
				squares[i][2] = new MapRailSquare(1, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[16][2]).setRailID(1, M.RIGHT, M.DOWN);

			squares[16][3] = new MapRailSquare(1, M.RIGHT, M.LEFT);

			squares[16][4] = new MapRailSquare(1, M.RIGHT, M.LEFT);

			for(int i=6; i<=16; i++) {
				squares[i][5] = new MapRailSquare(1, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[6][5]).setRailID(1, M.RIGHT, M.DOWN);
			((MapRailSquare)squares[16][5]).setRailID(1, M.UP, M.LEFT);

			for(int j=8; j<=19; j++) {
				squares[6][j] = new MapRailSquare(2, M.RIGHT, M.LEFT);
			}
			((MapRailSquare)squares[6][19]).setRailID(2, M.DOWN, M.LEFT);

			for(int i=7; i<=11; i++) {
				squares[i][19] = new MapRailSquare(2, M.DOWN, M.UP);
			}

			for(int i=14; i<=18; i++) {
				squares[i][20] = new MapRailSquare(4, M.DOWN, M.UP);
			}
			((MapRailSquare)squares[18][20]).setRailID(4, M.RIGHT, M.UP);

			for(int j=21; j<=26; j++) {
				squares[18][j] = new MapRailSquare(4, M.RIGHT, M.LEFT);
			}
			((MapRailSquare)squares[18][26]).setRailID(4, M.DOWN, M.LEFT);

			for(int i=19; i<=24; i++) { 
				squares[i][26] = new MapRailSquare(4, M.DOWN, M.UP);
			}

			for(int j=27; j<COLUMNS; j++) { 
				squares[25][j] = new MapRailSquare(4, M.RIGHT, M.LEFT);
			}

			for(int j=21; j<=26; j++) {
				squares[12][j] = new MapRailSquare(3, M.RIGHT, M.LEFT);
			}
			((MapRailSquare)squares[12][26]).setRailID(3, M.UP, M.LEFT);

			for(int i=9; i<=11; i++) { 
				squares[i][26] = new MapRailSquare(3, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[9][26]).setRailID(3, M.RIGHT, M.DOWN);

			squares[9][27] = new MapRailSquare(3, M.RIGHT, M.LEFT);

			for(int i=5; i<=9; i++) {
				squares[i][28] = new MapRailSquare(3, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[5][28]).setRailID(3, M.LEFT, M.DOWN);
			((MapRailSquare)squares[9][28]).setRailID(3, M.UP, M.LEFT);

			for(int j=23; j<=27; j++) {
				squares[5][j] = new MapRailSquare(3, M.LEFT, M.RIGHT);
			}
			((MapRailSquare)squares[5][23]).setRailID(3, M.UP, M.RIGHT);

			for(int i=3; i<=4; i++) {
				squares[i][23] = new MapRailSquare(3, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[3][23]).setRailID(3, M.LEFT, M.DOWN);

			for(int i=1; i<=3; i++) { 
				squares[i][22] = new MapRailSquare(3, M.UP, M.DOWN);
			}
			((MapRailSquare)squares[1][22]).setRailID(3, M.RIGHT, M.DOWN);
			((MapRailSquare)squares[3][22]).setRailID(3, M.UP, M.RIGHT);

			for(int j=23; j<=25; j++) {
				squares[1][j] = new MapRailSquare(3, M.RIGHT, M.LEFT);
			}

			//pruzni prelaz
			squares[20][2] = new MapRailRoadSquare(1, M.LEFT, M.UP, M.DOWN);
			squares[21][2] = new MapRailRoadSquare(1, M.RIGHT, M.UP, M.DOWN);

			squares[6][13] = new MapRailRoadSquare(2, M.DOWN, M.RIGHT, M.LEFT);
			squares[6][14] = new MapRailRoadSquare(2, M.UP, M.RIGHT, M.LEFT);

			squares[20][26] = new MapRailRoadSquare(3, M.LEFT, M.UP, M.DOWN);
			squares[21][26] = new MapRailRoadSquare(3, M.RIGHT, M.UP, M.DOWN);

			//zeljeznicka stanica
			MapTrainStation stationA = new MapTrainStation("A", true);
			stations.add(stationA);
			squares[27][1] = stationA; // za bordere
			squares[27][2] = new MapTrainStation("A", false);
			squares[28][1] = new MapTrainStation("A", false);
			squares[28][2] = new MapTrainStation("A", false);

			MapTrainStation stationB = new MapTrainStation("B", true);
			stations.add(stationB);
			squares[5][6] = stationB;
			squares[6][6] = new MapTrainStation("B", false);
			squares[5][7] = new MapTrainStation("B", false);
			squares[6][7] = new MapTrainStation("B", false);

			MapTrainStation stationC = new MapTrainStation("C", true);
			stations.add(stationC);
			squares[12][19] = stationC;
			squares[13][19] = new MapTrainStation("C", false);
			squares[12][20] = new MapTrainStation("C", false);
			squares[13][20] = new MapTrainStation("C", false);

			MapTrainStation stationD = new MapTrainStation("D", true);
			stations.add(stationD);
			squares[1][26] = stationD;
			squares[2][26] = new MapTrainStation("D", false);
			squares[1][27] = new MapTrainStation("D", false);
			squares[2][27] = new MapTrainStation("D", false);

			MapTrainStation stationE = new MapTrainStation("E", true);
			stations.add(stationE);
			squares[25][25] = stationE;
			squares[26][25] = new MapTrainStation("E", false);
			squares[25][26] = new MapTrainStation("E", false);
			squares[26][26] = new MapTrainStation("E", false);

			for(int i=0; i<ROWS; i++) {
				for(int j=0; j<COLUMNS; j++) {
					if(i==27 && j==1) {}
					else if(i==5 && j==6) {}
					else if(i==12 && j==19) {}
					else if(i==1 && j==26) {}
					else if(i==25 && j==25) {}
					else {
						grid.add(squares[i][j], j, i);
					}
				}
			}
			grid.add(squares[27][1], 1, 27, 2, 2); //stanica A
			grid.add(squares[5][6], 6, 5, 2, 2); //stanica B
			grid.add(squares[12][19], 19, 12, 2, 2); //stanica C
			grid.add(squares[1][26], 26, 1, 2, 2); //stanica D
			grid.add(squares[25][25], 25, 25, 2, 2); //stanica E
		} catch(FileNotFoundException e) {
			MyLogger.log(Level.WARNING, "FileNotFound", e);
		}
	}
	
	/**
	 * funkcija koja pokrece simulaciju
	 * @throws IOException
	 */
	@FXML
	private void START() {
		try {
			btnStart.setDisable(true);
			btnStart.setText("RUNNING");
			btnStart.setTextFill(javafx.scene.paint.Color.RED);

			VehiclesWatcher watcher = new VehiclesWatcher();
			watcher.readConfig();
			watcher.start();
			
			Arrays.stream(new File(watcher.getPathTrainline()).listFiles()).forEach(File::delete);
			
			TrainWatcher watcher2 = new TrainWatcher(watcher.getPathTrains(), watcher.getPathTrainline());
			watcher2.start();

			Thread t1 = new Thread(new Thread() { //auta1 road1count road1speed
				public void run() {
					int counter = 0;
					while(true) {
						if (counter < watcher.getR1C()) { //1-auto 2-kamion 1-gore 2-desno
							int temp = (int)(Math.random()*2 + 1), temp2= (int)(Math.random()*2 + 1);
							if(temp == 1 && temp2 == 2) {
								if(!squares[21][0].getLock().isLocked()) {
									counter++;
									Car car = new Car((int) (Math.random() * (5 - 3) + 3), Math.random()*(watcher.getR1S()-1.1)+1, 21, 0);
									car.setDaemon(true);
									car.start();
								} 
							} else if(temp == 1 && temp2 == 1) {
								if(!((MapRoadSquare)squares[29][8]).getLock().isLocked()) {
									counter++;
									Car car = new Car((int)(Math.random()*(5-3) + 3), Math.random()*(watcher.getR1S()-1.1)+1, 29, 8);
									car.setDaemon(true);
									car.start();
								} 
							} else if(temp == 2 && temp2 == 2) {
								if(!((MapRoadSquare)squares[21][0]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR1S()-1.1)+1, 21, 0);
									truck.setDaemon(true);
									truck.start();
								}
							} else if(temp == 2 && temp2 == 1){
								if(!((MapRoadSquare)squares[29][8]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR1S()-1.1)+1, 29, 8);
									truck.setDaemon(true);
									truck.start();
								} 
							}
						}
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			Thread t2 = new Thread(new Thread() { //auta2
				public void run() {
					int counter = 0;
					while(true) {
						if(counter < watcher.getR2C()) { //1-auto 2-kamion 1-gore 2-dole
							int temp = (int)(Math.random()*2 + 1), temp2= (int)(Math.random()*2 + 1);
							if(temp == 1 && temp2 == 1) {
								if(!((MapRoadSquare)squares[29][14]).getLock().isLocked()) {
									counter++;
									Car car = new Car((int)(Math.random()*(5-3) + 3), Math.random()*(watcher.getR2S()-1.1)+1, 29, 14);
									car.setDaemon(true);
									car.start();
								}
							} else if(temp == 1 && temp2 == 2) {
								if(!((MapRoadSquare)squares[0][13]).getLock().isLocked()) {
									counter++;
									Car car = new Car((int)(Math.random()*(5-3) + 3), Math.random()*(watcher.getR2S()-1.1)+1, 0, 13);
									car.setDaemon(true);
									car.start();
								}
							} else if(temp == 2 && temp2 == 1) {
								if(!((MapRoadSquare)squares[29][14]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR2S()-1.1)+1, 29, 14);
									truck.setDaemon(true);
									truck.start();
								}
							} else {
								if(!((MapRoadSquare)squares[0][13]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR2S()-1.1)+1, 0, 13);
									truck.setDaemon(true);
									truck.start();
								}
							}
						}
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}); 

			Thread t3 = new Thread(new Thread() { //auta3
				public void run() {
					int counter = 0;
					while(true) {
						if(counter <watcher.getR3C()) { //1-auto 2-kamion 1-gore 2-lijevo
							int temp = (int)(Math.random()*2 + 1), temp2= (int)(Math.random()*2 + 1);
							if(temp == 1 && temp2 == 1) {
								if(!((MapRoadSquare)squares[29][22]).getLock().isLocked()) {
									counter++;
									Car car = new Car((int) (Math.random() * (5 - 3) + 3), Math.random()*(watcher.getR3S()-1.1)+1, 29, 22);
									car.setDaemon(true);
									car.start();
								}
							} else if(temp == 1 && temp2 == 2) {
								if(!((MapRoadSquare)squares[20][29]).getLock().isLocked()) {
									counter++;
									Car car = new Car((int)(Math.random()*(5-3) + 3), Math.random()*(watcher.getR3S()-1.1)+1, 20, 29);
									car.setDaemon(true);
									car.start();
								}
							} else if(temp == 2 && temp2 == 1) {
								if(!((MapRoadSquare)squares[29][22]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR3S()-1.1)+1, 29, 22);
									truck.setDaemon(true);
									truck.start();
								}
							} else {
								if(!((MapRoadSquare)squares[20][29]).getLock().isLocked()) {
									counter++;
									Truck truck = new Truck((int)(Math.random()*(200-100) + 100), Math.random()*(watcher.getR3S()-1.1)+1, 20, 29);
									truck.setDaemon(true);
									truck.start();
								}
							}
						}
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			t1.start();
			t2.start();
			t3.start();

		} catch(IOException e) {
			MyLogger.log(Level.WARNING, "IOException", e);
		}
	}	
	
	public static MapFieldSquare[][] getMainMap() {
		return MainViewController.squares;
	}
	
	public static LinkedList<Thread> getTrains() {
		return MainViewController.trains;
	}
	
	public static LinkedList<MapTrainStation> getStations() {
		return MainViewController.stations;
	}
	
	public static Section getSection() {
		return MainViewController.section;
	}
	
	public static int getTrainID() {
		return MainViewController.trainID++;
	}

} 