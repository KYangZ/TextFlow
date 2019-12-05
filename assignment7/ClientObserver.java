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
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends ObjectOutputStream implements Observer, Serializable {

	public ClientObserver(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			this.writeObject(arg);
			this.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
