import java.util.HashMap;

import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;


public class SimpleNode {
	
	P2PNode node1;
	JCSyncCore core1;
	
	public SimpleNode() {
		node1 = new P2PNode(null, P2PNode.RoutingAlgorithm.SUPERPEER);
		node1.setServerReflexiveAddress("127.0.0.1"); // kuku
		node1.setServerReflexivePort(5050);
		node1.setBootIP("192.168.80.182");
		node1.setBootPort(6060);
		node1.setUdpPort(4040);
		node1.networkJoin();
		
		while(!node1.isConnected()){
			System.out.println("not connected");
			try{
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("SimpleNode.SimpleNode()" + e);
			}
		}
		
		System.out.println("connected");
	}
	
	public static void main(String[] args) {
		SimpleNode node = new SimpleNode();
	}
	
	public void kolekcje(){
		String collID = "mojaMapa";
		try {
			JCSyncHashMap hm1 = createHashMap(collID, this.core1);
		} catch (ObjectExistsException e) {
			System.out.println("KOLECKA ISTNIEJE");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Metoda tworzenia nowej czystej koleckji
	 */
	public JCSyncHashMap createHashMap(String collID, JCSyncCore coreAlg) throws ObjectExistsException, Exception {
		//create collection
		JCSyncHashMap map = new JCSyncHashMap();
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(collID, map, coreAlg);
		return map;
	}
	
	public JCSyncHashMap createHashMap(String collID, JCSyncCore coreAlg, HashMap coreMap) throws ObjectExistsException, Exception {
		//create collection
		JCSyncHashMap map = new JCSyncHashMap(coreMap);
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(collID, map, coreAlg);
		return map;
	}

}
