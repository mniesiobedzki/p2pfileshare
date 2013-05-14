package test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

import folder.FolderTree;

public class TestTreeClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket sock;
		try {
			sock = new Socket("1.1.1.12",6789);
			System.out.println("Connecting...");
	    
			InputStream socketStream = sock.getInputStream();
			ObjectInputStream objectInput = new ObjectInputStream(new GZIPInputStream(socketStream));
			FolderTree ft = (FolderTree) objectInput.readObject();
			
			System.out.println(ft);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
