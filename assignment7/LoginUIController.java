package assignment7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginUIController {
    public ClientMain c;

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
        String username = username_field.getText();
        String password = password_field.getText();
        String ip = ip_field.getText();

        try {
            if (username.equals("")) {
                throw new Exception();
            }

            /*
            if (ServerMain.online.contains(username)) {
                throw new Exception();
            }
             */

            c.setUpNetworking(ip);
            c.username = username;
            c.ip = ip;

            /*
            if (ClientMain.online.contains(username)) {
                throw new Exception();
            } else {
                ClientMain.online.add(username);
                for (String s : ClientMain.online) {
                    System.out.println(s);
                }
            }
             */

            // prepare the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/HomeScreen.fxml"));
            Parent root = loader.load();
            HomeScreenController controller = loader.getController();
            controller.c = c;
            c.incoming_broadcast = controller.broadcast_window;

            // PERSONALIZATION of UI
            controller.welcome_msg.setText(c.username + "!");
            controller.welcome_info.setText("Server IP: " + c.ip + " Welcome to the Lobby! To log out, close this window.");
            controller.scrollpane.vvalueProperty().bind(controller.broadcast_window.heightProperty());

            // Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("assignment7/HomeScreen.fxml"));
            Stage s = (Stage)login_button.getScene().getWindow();
            Scene scene = new Scene(root);
            s.setScene(scene);

            // System.out.println("new_user#" + c.username + " has joined the chat!\n");
            ClientMain.toServer.writeObject("new_user#" + c.username + " has joined the chat!");
            ClientMain.toServer.flush();
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
