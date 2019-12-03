package assignment7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.ConnectException;

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
import javafx.scene.*;
import javafx.stage.Stage;
import sun.misc.*;

public class LoginUIController {

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField username_field;

    @FXML
    private TextField ip_field;

    @FXML
    private Button login_button;

    @FXML
    private Label failure_msg;

    @FXML
    void login_action(ActionEvent event) {
        login_button.setDisable(true);
        username_field.setDisable(true);
        password_field.setDisable(true);
        ip_field.setDisable(true);

        // Platform.runLater(() -> login_button.setText("Loading..."));
        Main.username = username_field.getText();
        Main.password = password_field.getText();
        Main.ip = ip_field.getText();

        ChatClient c = null;
        try {
            c = new ChatClient(Main.username, Main.password, Main.ip);
            ChatServer.users.add(c);
            ChatServer.userNames.add(Main.username);
            // prepare the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/HomeScreen.fxml"));
            Parent root = loader.load();
            HomeScreenController controller = loader.getController();
            // Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7/HomeScreen.fxml"));
            Stage s = (Stage)login_button.getScene().getWindow();
            Scene scene = new Scene(root);
            s.setScene(scene);

            controller.c = c;
            c.incoming_broadcast = controller.broadcast_window;
        } catch (Exception e) {
            failure_msg.setVisible(true);
            username_field.clear();
            password_field.clear();
            ip_field.clear();
            username_field.setDisable(false);
            password_field.setDisable(false);
            ip_field.setDisable(false);
            login_button.setDisable(false);
            e.printStackTrace();
        }
    }

}
