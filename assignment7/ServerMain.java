package assignment7;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javafx.application.Platform;

public class ServerMain extends Observable {

	// public volatile static ObservableList<ChatClient> users = FXCollections.observableArrayList();
	// public volatile static ObservableList<String> usernames = FXCollections.observableArrayList();

	// public static ArrayList<String> online = new ArrayList<String>();
	//Hashtable<String, ClientHandler> online = new Hashtable<>();
	Hashtable<String, ClientObserver> online = new Hashtable<>();
	// ArrayList<ChatRoom> chats = new ArrayList<ChatRoom>();
	Hashtable<String, ChatRoom> chats = new Hashtable<>();

	final Object lock1 = new Object();

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

		public synchronized void run() {
			String message;
			try {
				while ((message = (String)serverInput.readObject()) != null) {
					System.out.println("server read " + message);

					if (message.startsWith("new_user#")) {
							String u = message.split("#")[1];
							online.put(u, this.observer);
					} else if (message.startsWith("new_chat#")) {
						// System.out.println(message);
							String receive_list = message.split("#")[1];
							String[] receivers = receive_list.split(" ");
							ArrayList<ClientObserver> ppl = new ArrayList<ClientObserver>();
							List<String> r = Arrays.asList(receivers);
							Collections.sort(r);
							String room_name = "";
							for (String s : r) {
								room_name += s;
								room_name += " ";
							}

							int i = 0;
							for (String s : receivers) {
								System.out.println(s);
								if (i == 0) {
									//ppl.add(this.observer);
									ppl.add(online.get(s));
									System.out.println(this.observer);
								} else {
									System.out.println(online);
									System.out.println(online.get(s));
									ppl.add(online.get(s)); //ppl.add(online.get(s).observer);
								}
								i++;
							}

							ChatRoom chat_room = new ChatRoom(ppl, room_name);
							chats.put(room_name, chat_room);
							chat_room.initObservers(room_name);
							// .out.println(chats);
					} else if (message.startsWith("chatroom#")) {

							String id = message.split("#")[1];
							chats.get(id).sendMessage(message.split("#")[2]);
	
					} else if (message.startsWith("broadcast#")) {
			
							setChanged();
							notifyObservers(message);
		
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
