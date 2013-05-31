package test;

import java.util.LinkedList;

public class TestTreeClientHandler implements Runnable{
	public static LinkedList<TestTreeClientWBootstrap> clients = new LinkedList<TestTreeClientWBootstrap>();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for(User u: MegaTest.users.values()){

				TestTreeClientWBootstrap klient = new TestTreeClientWBootstrap(u.ip, u.treePort);
				clients.add(klient);
			}
			
			
			
		}
	}

}
