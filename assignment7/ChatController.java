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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class ChatController {
    public String chatName;
    public String username;

    @FXML
    private Label chat_title;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    public TextFlow chat_window;

    @FXML
    private TextField chat_textfield;

    @FXML
    private Button send_button;

    @FXML
    void onEnter(ActionEvent event) {
        send_msg(event);
    }

    @FXML
    void send_msg(ActionEvent event) {
        if (!chat_textfield.getText().equals("")) {
            if (!chat_textfield.getText().contains("#")) {
                try {
                    ClientMain.toServer.writeObject("chatroom#" + chatName + "#" + username + "#" + chat_textfield.getText() + "#" + ClientMain.user_color + "#" + ClientMain.text_size);
                    ClientMain.toServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chat_textfield.setText("");
                chat_textfield.requestFocus();
            }
        }
    }
    
    

}
