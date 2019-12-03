
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
