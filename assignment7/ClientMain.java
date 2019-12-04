package assignment7;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.print.DocFlavor.URL;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.stage.Stage;
import sun.misc.*;

public class ClientMain extends Application {
	public TextFlow incoming_broadcast;

	// public BufferedReader reader;
	// public PrintWriter writer;

	public static ObjectInputStream fromServer;
	public static ObjectOutputStream toServer;

	public Color user_color = Color.WHITE;
	public String username;
	public String ip;
	public LoginUIController controller;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/Login_Screen.fxml"));
		Parent root = loader.load();
		controller = loader.getController();
		Scene scene = new Scene(root);
		controller.c = this;
		primaryStage.setOnCloseRequest(e->{
			Platform.exit();
			System.exit(0);
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void setUpNetworking(String ip) throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket();
		InetSocketAddress address = new InetSocketAddress(ip, 4242);
		sock.connect(address, 5000);

		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());

		// reader = new BufferedReader(streamReader);
		// writer = new PrintWriter(sock.getOutputStream());

		fromServer = new ObjectInputStream(sock.getInputStream());
		toServer = new ObjectOutputStream(sock.getOutputStream());

		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = (String)fromServer.readObject()) != null) {
					String finalMessage = message;

					if (message.startsWith("new_user#")) {
						// clients.add();
						toServer.writeObject("add_user#" + finalMessage.split("#")[1]);
						toServer.flush();

						Platform.runLater(() -> {
							user_color = Color.GOLDENROD;
							Text t = new Text(finalMessage);
							t.setFont(Font.font("System", 16));
							t.setFill(user_color);
							incoming_broadcast.getChildren().add(t);
							user_color = Color.CORNFLOWERBLUE;
						});
					} else if (message.startsWith("broadcast#")) {
						Platform.runLater(() -> {
							Text prefix = new Text(finalMessage.split("#")[1] + ":  ");
							prefix.setFill(Color.CORNFLOWERBLUE);
							prefix.setFont(Font.font("System", 16));
							Text t = new Text(finalMessage.split("#")[2] + "\n");
							t.setFont(Font.font("System", 16));
							t.setFill(Color.WHITE);
							incoming_broadcast.getChildren().add(prefix);
							incoming_broadcast.getChildren().add(t);
						});
					} else if (message.startsWith("new_chat#")) {
						String chatName = message.split("#")[1];
						FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/ChatScreen.fxml"));
						Parent root = loader.load();
						ChatController controller = loader.getController();
						controller.chatName = chatName;
						Platform.runLater(() -> {
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.setScene(scene);
							stage.show();
						});
					} else {
						Platform.runLater(() -> {
							Text t = new Text(finalMessage);
							t.setFont(Font.font("System", 16));
							t.setFill(Color.WHITE);
							incoming_broadcast.getChildren().add(t);
						});
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
