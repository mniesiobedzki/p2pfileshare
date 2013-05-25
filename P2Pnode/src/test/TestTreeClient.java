package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

import folder.FolderTree;

public class TestTreeClient implements Runnable{

	/**
	 * @param args
	 */
	String 	serverIP;
	int 	serverPort;
	boolean changed=false;
	FolderTree ft;

	@Override
	public void run() {
		while (true) {
			Socket sock = null;
			boolean ok = false;
			while (!ok) {
				try {
					sock = new Socket(serverIP, serverPort);
					System.out.println("Connecting...");
					ok = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
			InputStream socketStream;
			try {
				if (sock != null) {

					socketStream = sock.getInputStream();
					ObjectInputStream objectInput = new ObjectInputStream(
							socketStream);
					// tu przetestowa� co si� dzieje bo nie wiemy
					// jak dzia�a objectinputstream
					ft = null;
					ft = (FolderTree) objectInput.readObject();
					changed = true;
					System.out.println(">>>" + ft);
					objectInput.close();
					socketStream.close();
					sock.close();
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public TestTreeClient(String serverIP, int serverPort){
		this.serverPort = serverPort;
		this.serverIP 	= serverIP;
	}

}
