import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

public class BasicCollectionUsage {

	//private BootstrapServerRunner bootstrapServerRunner;
	private JCSyncCore core1;
	private JCSyncCore core2;
	
	JCSyncHashMap hm1;

	public BasicCollectionUsage() {
		//initBootstrapServer(7080);
		initNode1(6060, 7080, "node1");
		initNode2(6070, 7080, "node2");

		String collID = "mojaMapa";
		try {
			hm1 = createHashMap(collID, this.core1);
		} catch (ObjectExistsException e) {
			System.out.println("KOLECKA ISTNIEJE");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		snooze(500);

		JCSyncHashMap hs_core2;

		// próba utowrzenia drugiej takiej samej koleckji przez node2
		try {
			hs_core2 = createHashMap(collID, this.core2);
			// wywali exception ObjectExistsException (kod ponizej)
		} catch (ObjectExistsException ex) {
			try {
				System.out.println("NODE2: Kolekcja już istnieje i się pod nią podpinam");
				// w takim wypadku spróbuje się dopisać do kolekcji
				hs_core2 = (JCSyncHashMap) subscribeCollection(collID, core2)
						.getNucleusObject();
				hs_core2.put("a", "b");
			} catch (ObjectNotExistsException ex_not_ex) {
				System.err.println("Koleckcja od ID " + collID
						+ " jednak nie istnieje");
			} catch (Exception e) {
				System.err.println("Dziwny błąd");
				System.err.println(e.getMessage());
			}
		} catch (Exception e) {
			System.err.println("Dziwny błąd");
			System.err.println(e.getMessage());
		}
		
		System.out.println("Kolecka hm1 " + hm1.get("a"));
		
	}

	/**
	 * Metoda tworzenia nowej czystej koleckji
	 * 
	 * @param collID
	 * @param coreAlg
	 * @return
	 * @throws ObjectExistsException
	 * @throws Exception
	 */
	public JCSyncHashMap createHashMap(String collID, JCSyncCore coreAlg)
			throws ObjectExistsException, Exception {
		// create collection
		JCSyncHashMap map = new JCSyncHashMap();
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
				collID, map, coreAlg);
		return map;
	}

	/**
	 * Metoda tworzenia nowej koleckji i przekazania jej już istniewjącej
	 * kolekcji
	 * 
	 * @param collID
	 * @param coreAlg
	 * @param coreMap
	 * @return
	 * @throws ObjectExistsException
	 * @throws Exception
	 */
	public JCSyncHashMap createHashMap(String collID, JCSyncCore coreAlg,
			HashMap coreMap) throws ObjectExistsException, Exception {
		// create collection
		JCSyncHashMap map = new JCSyncHashMap(coreMap);
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
				collID, map, coreAlg);
		return map;
	}

	public SharedCollectionObject subscribeCollection(String collectionName,
			JCSyncCore coreAlg) throws ObjectNotExistsException,
			OperationForbiddenException, Exception {
		SharedCollectionObject sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject
				.getFromOverlay(collectionName, coreAlg);
		return sharedCollectionObject;
	}

	public void snooze(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			Logger.getLogger(BasicCollectionUsage.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	/*private void initBootstrapServer(int i) {
		this.bootstrapServerRunner = new BootstrapServerRunner(i);
		this.bootstrapServerRunner.start();
	}*/

	private void initNode1(int portUDP, int bootPort, String nodeName) {
		P2PNode node1;
		node1 = new P2PNode(null, P2PNode.RoutingAlgorithm.SUPERPEER);
		node1.setServerReflexiveAddress("127.0.0.1");
		node1.setServerReflexivePort(bootPort);
		node1.setBootIP("127.0.0.1");
		node1.setBootPort(bootPort);
		node1.setUserName(nodeName);
		node1.setUdpPort(portUDP);
		node1.networkJoin();

		while (!node1.isConnected()) {
			System.out.println("Node " + nodeName + " not connected");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("SimpleNode.SimpleNode()" + e);
				Logger.getLogger(BasicCollectionUsage.class.getName()).log(
						Level.SEVERE, null, e);
			}
		}
		System.out.println("Node " + nodeName + " connected !!");
		
		//tworzenie nowego jcsync core ?!
		core1 = new JCSyncCore(node1, portUDP+2);
		
		try {
			core1.init();
		} catch (Exception ex) {
			Logger.getLogger(BasicCollectionUsage.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
	
	private void initNode2(int portUDP, int bootPort, String nodeName) {
		P2PNode node2;
		node2 = new P2PNode(null, P2PNode.RoutingAlgorithm.SUPERPEER);
		node2.setServerReflexiveAddress("127.0.0.1");
		node2.setServerReflexivePort(bootPort);
		node2.setBootIP("127.0.0.1");
		node2.setBootPort(bootPort);
		node2.setUserName(nodeName);
		node2.setUdpPort(portUDP);
		node2.networkJoin();

		while (!node2.isConnected()) {
			System.out.println("Node " + nodeName + " not connected");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("SimpleNode.SimpleNode()" + e);
				Logger.getLogger(BasicCollectionUsage.class.getName()).log(
						Level.SEVERE, null, e);
			}
		}
		System.out.println("Node " + nodeName + " connected !!");
		
		//tworzenie nowego jcsync core ?!
		core2 = new JCSyncCore(node2, portUDP+2);
		
		try {
			core2.init();
		} catch (Exception ex) {
			Logger.getLogger(BasicCollectionUsage.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String[] args) {
		BasicCollectionUsage bcu = new BasicCollectionUsage();
	}

}
