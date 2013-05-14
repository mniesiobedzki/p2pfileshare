package test;

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
		System.err.println("Buja");
		Socket sock;
		try {
			Thread.sleep(5000);
			System.err.println("Podejmuje probe");
			sock = new Socket(serverIP,serverPort);
			System.out.println("Connecting...");
	    
			InputStream socketStream = sock.getInputStream();
			ObjectInputStream objectInput = new ObjectInputStream(socketStream);
			while(true){//tu przetestowa� co si� dzieje bo nie wiemy jak dzia�a objectinputstream 
				ft = (FolderTree) objectInput.readObject();
				changed = true;
				System.out.println(ft);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public TestTreeClient(String serverIP, int serverPort){
		this.serverPort = serverPort;
		this.serverIP 	= serverIP;
	}

}
