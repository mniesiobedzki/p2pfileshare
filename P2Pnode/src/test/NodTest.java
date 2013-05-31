package test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;

import folder.FolderTree;

public class NodTest implements Runnable{
	public String ip;
	public int port;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 Socket sock;
		 while(true){
			try {
				sock = new Socket(ip,port);
				System.out.println("Connecting...");
				OutputStream os = sock.getOutputStream();
				String key="post\n";
				os.write(key.getBytes(Charset.forName("UTF-8")));
				
				os.flush();
				os.close();
				
				
				os = sock.getOutputStream();
				ObjectOutput objectOutput = new ObjectOutputStream(os);
				objectOutput.writeObject(MegaTest.u);
				objectOutput.flush();
				objectOutput.close();
			
				os.flush();
				os.close();

				
				os = sock.getOutputStream();
				key="get\n";
				os.write(key.getBytes(Charset.forName("UTF-8")));
				
				os.flush();
				os.close();
				
				
				InputStream socketStream = sock.getInputStream();
				ObjectInputStream objectInput = new ObjectInputStream(socketStream);
				MegaTest.users.putAll((HashMap<String,User>) objectInput.readObject());
				MegaTest.u=MegaTest.users.get(MegaTest.u.name);
				
				objectInput.close();
				socketStream.close();
				
				Thread.sleep(60000);
				
				sock.close();
				
			}catch(Exception  e){
				e.printStackTrace();
			}
		 }
	}
	public NodTest(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
		new Thread(this).start();
	}
}
