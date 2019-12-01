package assignment7;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

import javax.swing.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.event.*;

public class ChatTesting {
	
//    
//    @FXML
//    void send_button_pressed(ActionEvent event) {
//    	writer.println(outgoing.getText());
//		writer.flush();
//		outgoing.setText("");
//		outgoing.requestFocus();
//    }
    
	private JTextArea incoming;
	//private JTextField outgoing;
	private BufferedReader reader;
	private PrintWriter writer;

	private String user;

	public void run() throws Exception {
		//initView();
		//setUpNetworking();
	}
	/*
	private void initView() {
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);
	}*/

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("10.147.220.148", 4242); // 128.62.61.202
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	/*
	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText());
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}*/

//	public static void main(String[] args) {
//		try {
//			ChatClient c = new ChatClient();
//			ChatServer.users.add(c);
//			c.run();
//			c.writer.println("A user has joined the chat");
//			c.writer.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
						incoming.append(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}