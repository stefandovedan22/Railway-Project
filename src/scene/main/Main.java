package scene.main;

import java.io.IOException;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
//import main.scene.Main;
import y.logger.MyLogger;

public class Main extends Application {
	private static Stage primaryStage, secondaryStage;
	private static AnchorPane mainLayout, secondaryLayout;
	private static boolean once = false;

	@Override
	public void start(Stage primaryStage) throws IOException { //primarni stage

		MyLogger.setup();

		try {
			Main.primaryStage = primaryStage;
			Main.primaryStage.setTitle("JavaRailwayProject");
			Main.primaryStage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MainView.fxml"));
			mainLayout = loader.load();
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			MainViewController controller = loader.getController();
			controller.showGUI();
			
		} catch(IOException e) {
			MyLogger.log(Level.WARNING, "IOException", e);
		}
	}
	
	public static void showMovementLinesScene() { //sekundarni stage
		try {
			Main.secondaryStage = new Stage();
			Main.secondaryStage.setTitle("MovementLines");
			Main.secondaryStage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SecondaryView.fxml"));
			secondaryLayout = loader.load();
			Scene scene = new Scene(secondaryLayout);
			secondaryStage.setScene(scene);
			secondaryStage.show();
			
			if(!once) {
				SecondaryViewController cont = loader.getController();
				cont.initialize();
				once = true;
			}
		} catch(IOException e) {
			MyLogger.log(Level.WARNING, "IOException", e);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
