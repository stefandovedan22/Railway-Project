package watcher;

import scene.trains.TrainComposition;
import scene.main.MainViewController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;

public class TrainWatcher extends Thread {
	
	Path directory;
	Path fileName;
	String path2;
	LinkedList<Thread> trains = MainViewController.getTrains();
	
	public TrainWatcher(String path, String path2) {
		this.path2 = path2;
		this.directory = Paths.get(path);
	}

	@Override
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			
			directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
			WatchKey key = null;
			while(true) {
				key = watcher.take();
				
				WatchEvent.Kind<?> kind = null; 
				for(WatchEvent<?> event : key.pollEvents()) {
					kind = event.kind();
					
					if(kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					} else if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
						System.out.println("detect");
						File file = new File(directory.toString()+File.separator+event.context().toString());
						sleep(300);
						readAndCheck(file);
						file.delete();
					}
				}
				boolean valid = key.reset();
				if(!valid) {
					break;
				}
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void readAndCheck(File file) throws IOException {
		BufferedReader br = null;
		try {
			FileReader fRead  = new FileReader(file);
			br = new BufferedReader(fRead);
			String[] line1, line2, line3, line4;
			line1 = br.readLine().split("\\s");
			line2 = br.readLine().split("\\s");
			line3 = br.readLine().split("\\s");
			line4 = br.readLine().split("\\s");
			br.close();
			boolean engines = false, wagons = false, speed = false, startDest = false;
			if("enginesCount".equalsIgnoreCase(line1[0]) && Integer.parseInt(line1[1]) > 0 ) {
				System.out.println("enginesWatcher");
				boolean pe = false, ce = false, ue = false, re = false;
				for(int i=0; i<Integer.parseInt(line1[1]); i++) {
					if("PE".equals(line1[2+i])) {
						pe = true;
					} else if("CE".equals(line1[2+i])) {
						ce = true;
					} else if("UE".equals(line1[2+i])) {
						ue = true;
					} else if("RE".equals(line1[2+i])) {
						re = true;
					}
				}
				
				if(pe == true & ce == true) return;
				else if(pe == true && re == true) return;
				else if(ce == true && re == true) return;
				else if(re && ue) return;
				
				engines = true;
			} else { return; }
			if("wagonCount".equalsIgnoreCase(line2[0]) && Integer.parseInt(line2[1]) >= 0) {
				if(Integer.parseInt(line2[1]) == 0) wagons = true;
				else {
					boolean pwb = false, pwr = false, pwse = false, pwsl = false, cw = false;
					
					for(int i=0; i<Integer.parseInt(line2[1]); i++) {
						if("PWB".equals(line2[2+i]) ) {
							pwb = true;
						} else if("PWR".equals(line2[2+i]) ) {
							pwr = true;
						} else if("PWSE".equals(line2[2+i]) ) {
							pwse = true;
						} else if("PWSL".equals(line2[2+i]) ) {
							pwsl = true;
						}  else if("CW".equals(line2[2+i]) ) {
							cw = true;
						}
					}
					
					if(pwb && cw) return;
					else if(pwr && cw) return;
					else if(pwse && cw) return;
					else if(pwsl && cw) return;
					
					wagons=true;
				}
			} else { return; }
			if("speed".equalsIgnoreCase(line3[0]) && Double.parseDouble(line3[1]) <= 5.0 ) {
				speed = true;
			} else { 
				return; 
			}
			
//			if(re && wagons) return;
//			else if(ce && (pwb || pwr || pwse || pwsl)) return;
//			else if(pe && (cw)) return;
			
			if("start".equalsIgnoreCase(line4[0]) && "destination".equalsIgnoreCase(line4[2])) {
				if(line4[1].equalsIgnoreCase(line4[3])) { return; }
				else if(!line4[1].equalsIgnoreCase("A") && !line4[1].equalsIgnoreCase("B") && !line4[1].equalsIgnoreCase("C") 
						&& !line4[1].equalsIgnoreCase("D") && !line4[1].equalsIgnoreCase("E")) {
					return;
				} else if(!line4[3].equalsIgnoreCase("A") && !line4[3].equalsIgnoreCase("B") && !line4[3].equalsIgnoreCase("C") 
						&& !line4[3].equalsIgnoreCase("D") && !line4[3].equalsIgnoreCase("E")) {
					return;
				}
				startDest = true;
			} else { return; }
			if(engines && wagons && speed && startDest) {
				TrainComposition train = new TrainComposition(line1, line2, line3, line4, path2);
				
				trains.add(train);
				System.out.println("Krece voz iz watchera");
				train.start();
			} else {
			}
		} catch (Exception e) {
			
		}
	}
	
}
