package assignment7;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	
	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client
	// UPDATEUSERS to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int UPDATEUSERS = 0, MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String message;
	
	// constructor
	public ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// getters
	public int getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
}
