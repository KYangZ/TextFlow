package assignment7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class HomeScreenController {

    public ChatClient c;

    @FXML
    public void initialize(){
        welcome_msg.setText(Main.username + "!");
        welcome_info.setText("Server IP: " + Main.ip + " Welcome to the Lobby! To log out, close this window.");
        scrollpane.vvalueProperty().bind(broadcast_window.heightProperty());
    }

    @FXML
    private Label welcome_msg;

    @FXML
    private Label welcome_info;

    @FXML
    public TextFlow broadcast_window;

    @FXML
    private TextField broadcast_textfield;

    @FXML
    private Button send_broadcast_button;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    void send_broadcast(ActionEvent event) {
        if (!broadcast_textfield.getText().equals("")) {
            c.writer.println(Main.username + ":  " + broadcast_textfield.getText());
            c.writer.flush();
            broadcast_textfield.setText("");
            broadcast_textfield.requestFocus();
        }
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) {
        send_broadcast(actionEvent);
    }
}
