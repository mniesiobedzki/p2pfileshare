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
			System.out.println("new bootstrap thread on port " + port);
			welcomeSocket = new ServerSocket(port);
			Socket sock = welcomeSocket.accept();

			System.out.println("Accepted connection : " + sock);
			OutputStream os = sock.getOutputStream();
			InputStream is = sock.getInputStream();

			while (true) {
				ObjectInputStream objectInput = new ObjectInputStream(is);
				User u;
				try {
					u = (User) objectInput.readObject();
					if(u!=null){
						System.out.println(u);
						Bootrstraper.users.put(u.name, u);
						os.flush();
						os.close();
						welcomeSocket.close();
						break;
					}else{

						ObjectOutput objectOutput = new ObjectOutputStream(os);
						objectOutput.writeObject(Bootrstraper.users);
						objectOutput.flush();
						objectOutput.close();
						os.flush();
						os.close();
						welcomeSocket.close();
						break;
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				

			}
			sock.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
