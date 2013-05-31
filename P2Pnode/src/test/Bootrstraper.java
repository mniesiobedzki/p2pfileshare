package test;

import java.util.HashMap;

public class Bootrstraper implements Runnable{

	public static HashMap<String,User> users = new HashMap<String,User>();
	public static int minPort =0;
	public static int maxPort =0;
	public Bootrstraper(int minPort, int maxPort) {
		super();
		if(maxPort<=minPort){
			this.minPort=minPort;
			this.maxPort=maxPort;
		}
		if(this.minPort<1024){
			this.minPort+=1024;
			this.maxPort+=1024;
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i =minPort; i<= maxPort; i++){
			new BootstrapThread(i);
		}
	}
	
}
