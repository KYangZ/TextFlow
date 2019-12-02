/* CRITTERS GUI <Main.java>
 * EE422C Project 7 submission by
 * <Student1 Name> Kory Yang
 * <Student1 EID> ky4794
 * <Student1 5-digit Unique No.> 16185
 * <Student2 Name> Sophia Jiang
 * <Student2 EID> sj26792
 * <Student2 5-digit Unique No.> 16185
 * Slip days used: <0>
 * Fall 2019
 */

package assignment7;
import javax.print.DocFlavor.URL;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.stage.Stage;
import sun.misc.*;

public class Main extends Application {

    public static Stage primaryStage;
    public static String username;
    public static String password;;
    public static String ip;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/Login_Screen.fxml"));
        Parent root = loader.load();
        LoginUIController controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 192.168.1.70
}

