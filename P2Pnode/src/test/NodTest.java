package test;

import java.io.IOException;
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
	public HashMap<String,User> users= new HashMap<String,User>();
	public User u;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 Socket sock;
		 boolean send = true;
		 while(true){
			try {
				System.out.println("³¹cze siê do bootstrapa przez port "+port);
				System.out.println("na ip "+ ip);
				sock = new Socket(ip,port);
				if(send){
					send(sock, u);
				}else{
					send(sock, null);
					get(sock);
				}
				
				

				sock.close();
				if(!send){
					Thread.sleep(10000);
				}else{
					Thread.sleep(500);
				}
				send=!send;
			}catch(Exception  e){
				e.printStackTrace();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		 }
	}
	public void send(Socket sock, User u){

		System.out.println("Connecting...");
		OutputStream os;
		try {
			os = sock.getOutputStream();
			ObjectOutput objectOutput = new ObjectOutputStream(os);
			objectOutput.writeObject(u);
			objectOutput.flush();
			//objectOutput.close();
		
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void get(Socket sock){
		InputStream socketStream;
		try {
			socketStream = sock.getInputStream();
			ObjectInputStream objectInput = new ObjectInputStream(socketStream);
			users.putAll((HashMap<String,User>) objectInput.readObject());
			System.out.println("users: "+users);
			u=users.get(u.name);
			
			
			objectInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public NodTest(String ip, int port, HashMap<String,User> users, User u) {
		super();
		this.ip = ip;
		this.port = port;
		this.users = users;
		this.u = u;
		new Thread(this).start();
	}
}
