package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BootstrapThread implements Runnable {
	public int port;

	public BootstrapThread(int port) {
		super();
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		ServerSocket welcomeSocket;
		while (true) {
			try {
				welcomeSocket = new ServerSocket(port);
				Socket sock = welcomeSocket.accept();

				System.out.println("Accepted connection : " + sock);
				OutputStream os = sock.getOutputStream();
				InputStream is = sock.getInputStream();
				java.util.Scanner s = new java.util.Scanner(is)
						.useDelimiter("\n");

				while (true) {
					while (!s.hasNext()) {
					}
					String msg = s.next();
					System.out.println(msg);
					if (msg.equals("get")) {
						ObjectOutput objectOutput = new ObjectOutputStream(os);
						objectOutput.writeObject(Bootrstraper.users);
						objectOutput.flush();
						os.flush();
						objectOutput.close();
						os.close();
						welcomeSocket.close();
						break;
					} else if (msg.equals("push")) {
						ObjectInputStream objectInput = new ObjectInputStream(
								is);
						User u;
						try {
							u = (User) objectInput.readObject();
							Bootrstraper.users.put(u.name, u);
							break;
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					sock.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
