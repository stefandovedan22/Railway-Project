package watcher;

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
import java.util.Properties;

public class VehiclesWatcher extends Thread {
	
	private int r1c=0, r2c=0, r3c=0, temp1, temp2, temp3;
	private double r1s, r2s, r3s;
	private String pathTrains, pathTrainline;
	
	@Override
	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get("src"+File.separator+"z"+File.separator+"config"+File.separator);
			WatchKey key = directory.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
			
			while(true) {
				for(WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					
					if(kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}else if(kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY) {
						readConfig();
					}		
				}
				if(!key.reset()) {
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void readConfig() throws IOException {
		FileReader reader = new FileReader("src"+File.separator+"z"+File.separator+"config"+File.separator+"Config"); 
		Properties properties = new Properties();
		properties.load(reader);
		this.r1s = Double.parseDouble(properties.getProperty("road1speed")); //r1
		this.temp1 = Integer.parseInt(properties.getProperty("road1count"));
		if(this.temp1 > this.r1c)
			r1c = temp1;
		this.r2s = Double.parseDouble(properties.getProperty("road2speed")); //r2
		this.temp2 = Integer.parseInt(properties.getProperty("road2count"));
		if(this.temp2 > this.r2c)
			r2c = temp2;
		this.r3s = Double.parseDouble(properties.getProperty("road3speed")); //r3
		this.temp3 = Integer.parseInt(properties.getProperty("road3count"));
		if(this.temp3 > this.r3c)
			r3c = temp3;
		this.pathTrains = properties.getProperty("pathTrains");
		this.pathTrainline = properties.getProperty("pathTrainline");
		System.out.println(this.pathTrains);
		System.out.println(this.pathTrainline);
	}
	
	public int getR1C() {
		return this.r1c;
	}
	
	public int getR2C() {
		return this.r2c;
	}
	public int getR3C() {
		return this.r3c;
	}
	
	public double getR1S() {
		return this.r1s;
	}
	
	public double getR2S() {
		return this.r2s;
	}
	
	public double getR3S() {
		return this.r3s;
	}
	
	public String getPathTrains() {
		return this.pathTrains;
	}
	
	public String getPathTrainline() {
		return this.pathTrainline;
	}
}
