package scene.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import watcher.*;
import y.logger.MyLogger;

public class SecondaryViewController {
	
	private String pathTrainline;
	private boolean once = false;
	
	@FXML
	Label trainLabel;
	@FXML
	ComboBox<String> comboBox;

	
	public void initialize() throws IOException {
		if(!once) {
			VehiclesWatcher watcher = new VehiclesWatcher();
			watcher.readConfig();
			this.pathTrainline = watcher.getPathTrainline(); 
			File f = new File(this.pathTrainline);
			String[] pathnames;
			pathnames = f.list();
			comboBox.getItems().addAll(pathnames);
			once = true;
		}
	}
	
	@FXML
	void box() {
		int index = comboBox.getSelectionModel().getSelectedIndex();
		ObjectInputStream stream;
		try {
			stream = new ObjectInputStream(new FileInputStream(this.pathTrainline+ File.separator + "trainLine"+index+".ser"));
			String trainline = (String)stream.readObject();
			stream.close();
			trainLabel.setText(trainline);
		} catch(Exception e) {
			MyLogger.log(Level.WARNING, "Deserialization", e);
		}
	}
}