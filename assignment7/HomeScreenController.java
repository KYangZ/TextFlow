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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeScreenController {
    public ClientMain c;

    @FXML
    public Label welcome_msg;

    @FXML
    public Label welcome_info;

    @FXML
    public TextFlow broadcast_window;

    @FXML
    private TextField broadcast_textfield;

    @FXML
    private Button send_broadcast_button;

    @FXML
    public ScrollPane scrollpane;

    @FXML
    private TextField start_chat_box;

    @FXML
    private Button chat_button;

    @FXML
    void send_broadcast(ActionEvent event) {
        String msg = broadcast_textfield.getText();
        if (msg.startsWith("@")) {
            if (msg.substring(1).equals("online")) {
                try {
                    ClientMain.toServer.writeObject("request_online#" + ClientMain.username);
                    ClientMain.toServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                broadcast_textfield.setText("");
                broadcast_textfield.requestFocus();
            } else if (msg.substring(1).equals("help")) {
                Text t = new Text(
                        "---------------------------------------\n" +
                        "List of all commands: \n" +
                        "@help                  lists all commands\n" +
                        "@online                displays all online users\n" +
                        "@color#<hex value>     change your color\n" +
                        "@textsize#<int>        change the size of your messages\n" +
                        "---------------------------------------\n");
                t.setFont(Font.font("System", 16));
                t.setFill(Color.LIGHTGREEN);
                broadcast_window.getChildren().add(t);
            } else if (msg.startsWith("@color")) {
                try {
                    Color c = Color.web(msg.split("#")[1]);
                    ClientMain.user_color = msg.split("#")[1];
                    Text t = new Text("Your color has been changed.\n");
                    t.setFont(Font.font("System", 16));
                    t.setFill(Color.web(ClientMain.user_color));
                    broadcast_window.getChildren().add(t);
                } catch (Exception e) {
                    Text t = new Text("Wrong use of @color. Enter @help to see the usage.\n");
                    t.setFont(Font.font("System", 16));
                    t.setFill(Color.RED);
                    broadcast_window.getChildren().add(t);
                }
            } else if (msg.startsWith("@textsize")) {
                try {
                    ClientMain.text_size = Integer.parseInt(msg.split("#")[1]);
                    Text t = new Text("Your text size has been changed.\n");
                    t.setFont(Font.font("System", ClientMain.text_size));
                    t.setFill(Color.web(ClientMain.user_color));
                    broadcast_window.getChildren().add(t);
                } catch (Exception e) {
                    Text t = new Text("Wrong use of @textsize. Enter @help to see the usage.\n");
                    t.setFont(Font.font("System", 16));
                    t.setFill(Color.RED);
                    broadcast_window.getChildren().add(t);
                }
            } else {
                Text t = new Text("Invalid command. Enter @help for a list of all commands.\n");
                t.setFont(Font.font("System", 16));
                t.setFill(Color.RED);
                broadcast_window.getChildren().add(t);
            }
            broadcast_textfield.setText("");
            broadcast_textfield.requestFocus();
        } else if (msg.equals("") || msg.contains("#")) {
            if (msg.contains("#")) {
                Text t = new Text("Invalid message. Please note you cannot use # in messages\n");
                t.setFont(Font.font("System", 16));
                t.setFill(Color.RED);
                broadcast_window.getChildren().add(t);
            }
        } else {
            try {
                ClientMain.toServer.writeObject("broadcast#" + c.username + "#" + broadcast_textfield.getText() + "#" + ClientMain.user_color + "#" + ClientMain.text_size);
                ClientMain.toServer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            broadcast_textfield.setText("");
            broadcast_textfield.requestFocus();
        }
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) {
        send_broadcast(actionEvent);
    }

    @FXML
    void start_chat(ActionEvent event) {
        String partner = start_chat_box.getText();

        if (partner.equals("") || partner.equals(c.username)) {
            System.out.println("invalid partner");
        } else {
            try {

                List<String> r = new ArrayList<String>();
                r.add(c.username);
                r.add(partner);
                Collections.sort(r);
                String room_name = "";
                for (String s : r) {
                    room_name = room_name + s + " ";
                }
                ClientMain.toServer.writeObject("new_chat#" + ClientMain.username + "#" + room_name);
                ClientMain.toServer.flush();
            } catch (Exception e) {
                // tell the user the other person is offline
                e.printStackTrace();
            }

        }
    }
}
