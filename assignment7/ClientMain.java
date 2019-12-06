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
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
	public Hashtable<String,TextFlow> chat_windows = new Hashtable<String, TextFlow>();
	public Hashtable<String,Stage> chat_stages = new Hashtable<String, Stage>();

	public static ObjectInputStream fromServer;
	public static ObjectOutputStream toServer;

	public static String user_color = "82AAFF";
	public static int text_size = 16;
	public static String username;
	public static String ip;
	public static String password;
	public LoginUIController controller;
	public boolean connected = false;

	static Media notif_sound;
	static Media broadcast_sound;

	static {
		try {
			notif_sound = new Media(ClientMain.class.getClassLoader().getResource("assignment7/private_notif.mp3").toURI().toString());
			broadcast_sound = new Media(ClientMain.class.getClassLoader().getResource("assignment7/notif_sound.mp3").toURI().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 // = new Media(new File("src/assignment7/private_notif.mp3").toURI().toString());
	// static Media step_sound_fx = new Media(new File("src/assignment5/sounds/step_sound_fx.mp3").toURI().toString());

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
			try {
				toServer.writeObject("remove_user#" + username);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Platform.exit();
			System.exit(0);
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void setUpNetworking(String ip) throws Exception {
		if (!connected) {
			@SuppressWarnings("resource")
			Socket sock = new Socket();
			InetSocketAddress address = new InetSocketAddress(ip, 4242);
			sock.connect(address, 5000);

			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());

			fromServer = new ObjectInputStream(sock.getInputStream());
			toServer = new ObjectOutputStream(sock.getOutputStream());

			System.out.println("networking established");
			connected = true;
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = (String)fromServer.readObject()) != null) {
					String finalMessage = message;
					// System.out.println(message);

					if (message.startsWith("login_success#") && message.split("#")[1].equals(username)) {
						Platform.runLater(() -> {
							// prepare the new scene
							FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/HomeScreen.fxml"));
							Parent root = null;
							try {
								root = loader.load();
							} catch (IOException e) {
								e.printStackTrace();
							}
							HomeScreenController home_controller = loader.getController();
							incoming_broadcast = home_controller.broadcast_window;

							// PERSONALIZATION of UI
							home_controller.welcome_msg.setText(username + "!");
							home_controller.welcome_info.setText("Server IP: " + ip);
							home_controller.scrollpane.vvalueProperty().bind(home_controller.broadcast_window.heightProperty());

							Stage s = (Stage)controller.login_button.getScene().getWindow();
							Scene scene = new Scene(root);
							s.setScene(scene);

							Text t = new Text("Welcome to the lobby! Close this window to log out.\n" +
									"To start a 1-1 or group chat, enter usernames in the text field separated by whitespace\n" +
									"Type in @online to see a list of online users.\n" +
									"Type in @help to see a list of commands.\n");
							t.setFont(Font.font("System", 16));
							t.setFill(Color.LIGHTGREEN);
							incoming_broadcast.getChildren().add(t);
						});

						// System.out.println("new_user#" + c.username + " has joined the chat!\n");
						ClientMain.toServer.writeObject("new_user#" + username + "#" + username + " has joined the chat!");
						ClientMain.toServer.flush();
					} else if (message.startsWith("wrong_password#")) {
						Platform.runLater(() -> {
							controller.failure_msg.setText("Wrong password or username taken");
							controller.failure_msg.setVisible(true);
							controller.username_field.clear();
							controller.password_field.clear();
							controller.ip_field.clear();
							controller.username_field.setDisable(false);
							controller.password_field.setDisable(false);
							controller.ip_field.setDisable(false);
							controller.login_button.setDisable(false);
						});
					} else if (message.startsWith("new_user_msg#") || message.startsWith("user_left_msg#")) {
						Platform.runLater(() -> {
							Text t = new Text(finalMessage.split("#")[1] + "\n");
							t.setFont(Font.font("System", 16));
							t.setFill(Color.GOLDENROD);
							incoming_broadcast.getChildren().add(t);
						});
					} else if (message.startsWith("broadcast#")) {
						Platform.runLater(() -> {
							Text prefix = new Text(finalMessage.split("#")[1] + ": ");
							prefix.setFill(Color.web(finalMessage.split("#")[3]));
							prefix.setFont(Font.font("System", Integer.parseInt(finalMessage.split("#")[4])));
							Text t = new Text(finalMessage.split("#")[2] + "\n");
							t.setFont(Font.font("System", Integer.parseInt(finalMessage.split("#")[4])));
							t.setFill(Color.web(finalMessage.split("#")[3]));
							incoming_broadcast.getChildren().add(prefix);
							incoming_broadcast.getChildren().add(t);
							MediaPlayer m = new MediaPlayer(broadcast_sound);
							m.setAutoPlay(true);
						});
					} else if (message.startsWith("private#")) {
						if (message.split("#")[1].equals(username)) {
							Platform.runLater(() -> {
								Text t = new Text(finalMessage.split("#")[2]);
								t.setFont(Font.font("System", 16));
								t.setFill(Color.GOLDENROD);
								incoming_broadcast.getChildren().add(t);
							});
						}
					} else if (message.startsWith("new_chat#")) {
						String chatName = message.split("#")[1];
						FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("assignment7/ChatScreen.fxml"));
						Parent root = loader.load();
						ChatController chat_controller = loader.getController();
						chat_controller.chatName = chatName;
						chat_controller.username = username;
						chat_controller.chat_title.setText("Chat");
						Platform.runLater(() -> {
							Scene scene = new Scene(root);
							Stage stage = new Stage();
							stage.setOnCloseRequest(e->{
								try {
									toServer.writeObject("close_chat#" + chatName);
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							});
							stage.setScene(scene);
							stage.show();
							chat_stages.put(chatName, stage);
						});
						chat_windows.put(chatName, chat_controller.chat_window);
					} else if (message.startsWith("close_chat_window#")) {
						String id = finalMessage.split("#")[1];
						Platform.runLater(() -> {
							// chat_windows.get(finalMessage.split("#")[0]).chat_window.getChildren().add(prefix);
							chat_stages.get(id).close();
							chat_windows.remove(id);
						});
					} else {
						Platform.runLater(() -> {
							// System.out.println(finalMessage);
							Color c = null;
							int textsize = 16;
							try {
								c = Color.web(finalMessage.split("#")[3]);
								textsize = Integer.parseInt(finalMessage.split("#")[4]);
							} catch (Exception e) {
								c = Color.GOLDENROD;
							}
							Text prefix = new Text(finalMessage.split("#")[1] + ": ");
							prefix.setFill(c);
							prefix.setFont(Font.font("System", textsize));
							Text t = new Text(finalMessage.split("#")[2] + "\n");
							t.setFont(Font.font("System", textsize));
							t.setFill(c);
							chat_windows.get(finalMessage.split("#")[0]).getChildren().add(prefix);
							chat_windows.get(finalMessage.split("#")[0]).getChildren().add(t);
							MediaPlayer m = new MediaPlayer(notif_sound);
							m.setAutoPlay(true);
						});
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
