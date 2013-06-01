package test;

import java.util.HashMap;

public class Bootrstraper implements Runnable{

	public static HashMap<String,User> users = new HashMap<String,User>();
	public static int minPort =0;
	public static int maxPort =0;
	public Bootrstraper(int minPort, int maxPort) {
		super();
		this.minPort=minPort;
		this.maxPort=maxPort;
		if(maxPort<=minPort){
			this.minPort=minPort;
			this.maxPort=maxPort;
		}
		if(minPort<1024){
			this.minPort+=1024;
			this.maxPort+=1024;
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		System.out.println("min port: "+minPort);
		System.out.println("max port: "+maxPort);
		// TODO Auto-generated method stub
		for(int i =minPort; i<= maxPort; i++){
			//System.out.println(Integer.toString(i));
			new BootstrapThread(i);
		}
	}

	public static void main(String[] args) {
		new Bootrstraper(21000,21024);
	}
	
}
