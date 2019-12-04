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
        if (!broadcast_textfield.getText().equals("")) {
            try {
                ClientMain.toServer.writeObject("broadcast#" + c.username + "#" + broadcast_textfield.getText());
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

    public void loadWindow() {

    }


    @FXML
    void start_chat(ActionEvent event) {
        String partner = start_chat_box.getText();

        if (partner.equals("") || partner.equals(c.username)) {
            System.out.println("invalid partner");
        } else {
            try {
                //TODO: check if the user is online!

                /*
                // prepare a new scene
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/ChatScreen.fxml"));
                Parent root = loader.load();
                ChatController controller = loader.getController();

                //c.incoming_broadcast = controller.chat_window;
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                 */


                List<String> r = new ArrayList<String>();
                r.add(c.username);
                r.add(partner);
                Collections.sort(r);
                String room_name = "";
                for (String s : r) {
                    room_name = room_name + s + " ";
                }
                ClientMain.toServer.writeObject("new_chat#" + room_name);
                ClientMain.toServer.flush();
            } catch (Exception e) {
                // tell the user the other person is offline
                e.printStackTrace();
            }

        }
    }
}
