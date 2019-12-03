package assignment7;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javafx.application.Platform;

public class ServerMain extends Observable {

	// public volatile static ObservableList<ChatClient> users = FXCollections.observableArrayList();
	// public volatile static ObservableList<String> usernames = FXCollections.observableArrayList();

	// public static ArrayList<String> online = new ArrayList<String>();
	HashMap<String, ClientHandler> online = new HashMap<String, ClientHandler>();
	ArrayList<ChatRoom> chats = new ArrayList<ChatRoom>();

	public ArrayList<Observable> users;
	public ArrayList<String> usernames;

	public static void main(String[] args) {
		try {
			ServerMain c = new ServerMain();
			c.setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ServerMain() {
		usernames = new ArrayList<String>();
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(new ObjectInputStream(clientSocket.getInputStream()), writer));
			t.start();
			this.addObserver(writer);

			System.out.println("got a connection");
			/*
			for (String str : ChatServer.usernames) {
				System.out.println(str);
			}

			 */

		}
	}

	/*
	public boolean addUser(ChatClient user) {
		users.add(user);
		usernames.add(user.username);
	}

	 */
	
	public void remove(ClientMain user) {
		Platform.runLater(() -> {
			users.remove(user);
			usernames.remove(user.username);
		});
	}
	
	
	class ClientHandler implements Runnable {
		ObjectInputStream serverInput;
		ObjectOutputStream toServer;
		ClientObserver observer;
		String username;

		Socket socket;
		ObjectInputStream input;

		public ClientHandler(ObjectInputStream serverInput, ClientObserver observer) {
			this.serverInput = serverInput;
			this.observer = observer;
		}

		public void run() {
			String message;
			try {
				while ((message = (String)serverInput.readObject()) != null) {
					if (message.startsWith("add_user#")) {
						String u = message.split("#")[1];
						online.put(u, this);
					} else if (message.startsWith("new_chat#")) {
						String receive_list = message.split("#")[1];
						String[] receivers = receive_list.split(" ");
						ArrayList<ClientObserver> ppl = new ArrayList<ClientObserver>();
						for (String s : receivers) {
							ppl.add(online.get(s).observer);
						}
						ChatRoom chat_room = new ChatRoom(ppl);
						chats.add(chat_room);
					} else {
						setChanged();
						notifyObservers(message);
					}
					System.out.println("server read " + message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
