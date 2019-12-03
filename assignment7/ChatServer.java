package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ChatServer extends Observable {

	public static ObservableList<String> userNames;
	public static ArrayList<ChatClient> users = new ArrayList<ChatClient>();
	
	public static void main(String[] args) {
		try {
			ChatServer c = new ChatServer();
			c.setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		userNames = FXCollections.observableArrayList();
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection");
		}
	}
	
	public void addUser(String user) {
		Platform.runLater(() -> {
			userNames.add(user);
		});
	}
	
	public void remove(String user) {
		Platform.runLater(() -> {
			userNames.remove(user);
		});
	}
	
	
	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("server read "+message);
					setChanged();
					notifyObservers(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
