package test;

import java.util.LinkedList;

public class TestTreeClientHandler implements Runnable{
	public static LinkedList<TestTreeClientWBootstrap> clients = new LinkedList<TestTreeClientWBootstrap>();
	NodTest nod;
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
			
			System.out.println("users "+nod.users.keySet());
			for(User u: nod.users.values()){
				if(!u.name.equals(nod.u.name)){
					TestTreeClientWBootstrap klient = new TestTreeClientWBootstrap(u.ip, u.treePort);
					clients.add(klient);
				}
			}
			LinkedList<TestTreeClientWBootstrap> remove = new LinkedList<TestTreeClientWBootstrap>();
			for (TestTreeClientWBootstrap c : clients) {
				if(c.kill){
					remove.add(c);
				}
			}
			clients.removeAll(remove);
		}
	}
	public TestTreeClientHandler(NodTest nod) {
		super();
		this.nod = nod;
	}

}
