package assignment7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class ChatRoom extends Observable {
    HashMap<String, ClientObserver> members = new HashMap<String, ClientObserver>();

    public ChatRoom(ArrayList<ClientObserver> people) {
        for (ClientObserver obs : people) {
            this.addObserver(obs);
        }
    }

    public void sendMessage() {

    }
}
