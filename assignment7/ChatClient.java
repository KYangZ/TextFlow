package assignment7;

import java.io.*;
import java.net.*;
import javafx.scene.control.*;

public class ChatClient {

	DataOutputStream toServer = null;
	DataInputStream fromServer = null;

	public TextArea incoming_broadcast;

	// private JTextArea incoming;
	// private JTextField outgoing;
	public BufferedReader reader;
	public PrintWriter writer;

	private String user;

	public ChatClient(String username, String password, String ip) throws Exception {
		setUpNetworking(ip);
	}

	/*
	public void run(String ip) throws Exception {
		// initView();
		setUpNetworking(ip);
	}

	 */

	/*
	private void initView() {
		JFrame frame = new JFrame("Broadcast Channel");
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
	}

	 */

	private void setUpNetworking(String ip) throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket();
		InetSocketAddress address = new InetSocketAddress(ip, 4242);
		sock.connect(address, 5000);


		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());

		// fromServer = new DataInputStream(sock.getInputStream()); // Create an output stream to send data to the server
		// toServer = new DataOutputStream(sock.getOutputStream());

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
	}
	 */

	public static void main(String[] args) {
		try {
			ChatClient c = new ChatClient("root","root", "42");
			ChatServer.users.add(c);
			// c.run();
			c.writer.println("A user has joined the chat");
			c.writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
						incoming_broadcast.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
