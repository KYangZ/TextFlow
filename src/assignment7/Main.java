
package assignment7;
import javax.print.DocFlavor.URL;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import sun.misc.Launcher;

public class Main extends Application{
	
    public static void main(String[] args) {
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ChatGUI.fxml")); // for Eclipse
		//Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ChatGUI.fxml")); // for intelliJ use only
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
