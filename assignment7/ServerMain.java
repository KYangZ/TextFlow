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

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javafx.application.Platform;

public class ServerMain extends Observable {

	static Hashtable<String, ClientObserver> online = new Hashtable<>();
	static Hashtable<String, ChatRoom> chats = new Hashtable<>();
	static Hashtable<String, String> pwd = new Hashtable<>();

	public static void main(String[] args) {
		try {
			ServerMain c = new ServerMain();
			c.setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ServerMain() {
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
		}
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
						setChanged();
						notifyObservers("new_user_msg#" + message.split("#")[2]);
					} else if (message.startsWith("new_chat#")) {
						String receive_list = message.split("#")[2];
						String sender = message.split("#")[1];
						String[] receivers = receive_list.split("\\s+");
						ArrayList<ClientObserver> ppl = new ArrayList<ClientObserver>();
						List<String> r = Arrays.asList(receivers);
						Collections.sort(r);
						String room_name = "";
						for (String s : r) {
							room_name += s;
							room_name += " ";
						}

						boolean allUsersOnline = true;
						for (String s : receivers) {
							if (online.get(s) == null) {
								setChanged();
								notifyObservers("private#" + sender + "#" + s + " is not currently online\n");
								allUsersOnline = false;
								continue;
							}
							ppl.add(online.get(s));
						}

						if (allUsersOnline) {
							if (chats.containsKey(room_name)) {
								setChanged();
								notifyObservers("private#" + sender + "#" + "That chat is already active\n");
							} else {
								ChatRoom chat_room = new ChatRoom(ppl, room_name, receivers);
								chats.put(room_name, chat_room);
							}
						}
					} else if (message.startsWith("chatroom#")) {
						String id = message.split("#")[1];
						chats.get(id).sendMessage(message.split("#")[2] + "#" + message.split("#")[3] + "#" + message.split("#")[4] + "#" + message.split("#")[5]);
					} else if (message.startsWith("broadcast#")) {
						setChanged();
						notifyObservers(message);
					} else if (message.startsWith("close_chat#")) {
						String id = message.split("#")[1];
						chats.get(id).closeWindow(id);
						chats.remove(id);
					} else if (message.startsWith("remove_user#")) {
						deleteObserver(online.get(message.split("#")[1]));
						online.remove(message.split("#")[1]);
						setChanged();
						notifyObservers("user_left_msg#" + message.split("#")[1] + " has left the chat.");
					} else if (message.startsWith("request_online#")) {
						String online_users = "Users currently online: \n";
						for (String u : online.keySet()) {
							online_users += u + "\n";
						}
						setChanged();
						notifyObservers("private#" + message.split("#")[1] + "#" + online_users);
					} else if (message.startsWith("login_request#")) {
						String username = message.split("#")[1];
						String password = message.split("#")[2];

						if (!pwd.containsKey(username)) {
							System.out.println(username);
							pwd.put(username, password);
							setChanged();
							notifyObservers("login_success#" + username);
						} else if (!pwd.get(username).equals(password) || (pwd.get(username).equals(password) && online.containsKey(username))) {
							setChanged();
							notifyObservers("wrong_password#" + username);
						} else {
							setChanged();
							notifyObservers("login_success#" + username);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
