package assignment7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;

public class ChatRoom extends Observable {
    Hashtable<String, ClientObserver> members = new Hashtable<String, ClientObserver>();
    String name;

    public ChatRoom(ArrayList<ClientObserver> people, String name) {
        for (ClientObserver obs : people) {
            this.addObserver(obs);
        }
        this.name = name;
        setChanged();
        notifyObservers("new_chat#" + name);
    }

    public void initObservers(String room_name) {
        setChanged();
        notifyObservers("new_chat#" + name);
    }


    public void sendMessage(String msg) {
        setChanged();
        notifyObservers(msg);
    }
}
