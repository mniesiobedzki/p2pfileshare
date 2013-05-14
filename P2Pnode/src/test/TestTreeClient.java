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

	@Override
	public void run() {
		Socket sock;
		try {
			sock = new Socket(serverIP,serverPort);
			System.out.println("Connecting...");
	    
			InputStream socketStream = sock.getInputStream();
			ObjectInputStream objectInput = new ObjectInputStream(socketStream);
			FolderTree ft = (FolderTree) objectInput.readObject();
			
			System.out.println(ft);
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
