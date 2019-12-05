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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;

public class ChatRoom extends Observable {
    Hashtable<String, ClientObserver> members = new Hashtable<String, ClientObserver>();
    String name;

    public ChatRoom(ArrayList<ClientObserver> people, String name, String[] members) {
        for (ClientObserver obs : people) {
            this.addObserver(obs);
            System.out.println("observer: " + obs);
        }
        this.name = name;
        setChanged();
        notifyObservers("new_chat#" + name);
        String starting_msg = "";
        int i = 0;
        for (String s : members) {
            starting_msg += s;
            if (i == members.length - 1) {
                starting_msg += ".";
            } else if (i == members.length - 2) {
                starting_msg += " and ";
            } else {
                starting_msg += ", ";
            }
            i++;
        }
        sendMessage("server#This is the start of a new chat between " + starting_msg);
    }

    public void sendMessage(String msg) {
        setChanged();
        notifyObservers(name + "#" + msg);
    }

    public void closeWindow(String room_id) {
        setChanged();
        notifyObservers("close_chat_window#" + room_id);
    }
}
