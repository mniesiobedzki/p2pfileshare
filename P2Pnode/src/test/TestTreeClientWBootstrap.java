package test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import folder.FolderTree;

public class TestTreeClientWBootstrap implements Runnable{

	/**
	 * @param args
	 */
	String 	serverIP;
	int 	serverPort;
	boolean changed=false;
	boolean kill = false;
	FolderTree ft = null;

	@Override
	public void run() {
		//while (true) {
			Socket sock = null;
			boolean ok = false;
			while (!ok) {
				try {
					sock = new Socket(serverIP, serverPort);
					//System.out.println("TestTreeClient: Connecting...");
					ok = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}

			//System.out.println("TestTreeClient: Connected");
			try {
				if (sock != null) {

					InputStream socketStream = sock.getInputStream();
					ObjectInputStream objectInput = new ObjectInputStream(socketStream);
					ft = (FolderTree) objectInput.readObject();

					synchronized(ft){
						if(ft!=null){
							changed = true;
							System.out.println(">>>" + ft);
							objectInput.close();
							socketStream.close();
							sock.close();
						}
	//					Thread.sleep(1000);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	kill = true;	
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public FolderTree getFt() {
		return ft;
	}
	public void setFt(FolderTree ft) {
		this.ft = ft;
	}
	public TestTreeClientWBootstrap(String serverIP, int serverPort){
		this.serverPort = serverPort;
		this.serverIP 	= serverIP;
		System.err.println("nowy ttc " +serverIP +":"+serverPort);
		new Thread(this).start();
	}

}
