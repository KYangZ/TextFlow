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
    public ClientMain c;

    @FXML
    private Label chat_title;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TextFlow chat_window;

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
            try {
                ClientMain.toServer.writeObject(chat_textfield.getText());
                ClientMain.toServer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            chat_textfield.setText("");
            chat_textfield.requestFocus();
        }
    }

}
