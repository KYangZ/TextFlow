/*
 * EE422C Project 7 submission by
 * <Student1 Name> Kory Yang
 * <Student1 EID> ky4794
 * <Student1 5-digit Unique No.> 16185
 * <Student2 Name> Sophia Jiang
 * <Student2 EID> sj26792
 * <Student2 5-digit Unique No.> 16185
 * Slip days used: <1>
 * Fall 2019
 */

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
    public PasswordField password_field;

    @FXML
    public TextField username_field;

    @FXML
    public TextField ip_field;

    @FXML
    public Button login_button;

    @FXML
    public Label failure_msg;

    @FXML
    void login_action(ActionEvent event) {
        login_button.setDisable(true);
        username_field.setDisable(true);
        password_field.setDisable(true);
        ip_field.setDisable(true);

        String username = username_field.getText();
        String password = password_field.getText();
        String ip = ip_field.getText();

        try {
            if (username.equals("")) {
                failure_msg.setText("Please enter a username");
                throw new Exception();
            }

            if (username.contains(" ") || username.contains("#")) {
                failure_msg.setText("Do not use spaces or #");
                throw new Exception();
            }

            if (password.equals("")) {
                failure_msg.setText("Please enter a password\nYou will be registered if it is your first time logging in");
                throw new Exception();
            }

            failure_msg.setText("Failed to connect");
            c.setUpNetworking(ip);

            ClientMain.username = username;
            ClientMain.password = password;
            ClientMain.ip = ip;
            ClientMain.toServer.writeObject("login_request#" + username + "#" + password);
            ClientMain.toServer.flush();

            // c.setUpNetworking(ip);
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
