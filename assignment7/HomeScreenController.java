package assignment7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HomeScreenController {

    public ChatClient c;

    @FXML
    public void initialize(){
        welcome_msg.setText(Main.username + "!");
        welcome_info.setText("Server IP: " + Main.ip + "\n" + "Welcome to the Lobby!\nChat with everyone on the right, or\nset up 1-1 or group chats above!\nTo log out, close this window.");
        broadcast_window.setWrapText(true);
    }

    @FXML
    private Label welcome_msg;

    @FXML
    private Label welcome_info;

    @FXML
    public TextArea broadcast_window;

    @FXML
    private TextField broadcast_textfield;

    @FXML
    private Button send_broadcast_button;

    
    @FXML
    void send_broadcast(ActionEvent event) {
        c.writer.println(Main.username + ": " + broadcast_textfield.getText());
        c.writer.flush();
        broadcast_textfield.setText("");
        broadcast_textfield.requestFocus();
    }

}
