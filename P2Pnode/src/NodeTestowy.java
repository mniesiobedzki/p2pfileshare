import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncArrayList;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.SharedObservableObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

public class NodeTestowy implements Observer {

	private P2PNode node;

	// Podstawowa implementacja JCSync
	private JCSyncCore jcSyncCore;

	private JCSyncHashMap<String, String> jcSyncHashMap;
	private JCSyncArrayList<String> jcSyncArrayList;
	
	// KOLECKCJE APLIKACJI
	private HashMap<String, String> myHashMap;
	private ArrayList<String> myArrayList;
	
	// OBSERWACJA
	private Observable observable;
	private JCSyncObservable jcSyncObservable;
	
	private BootstrapServerRunner bs;

	public NodeTestowy(int portOut, String serverIP, int serverPort,
			String nodeName) {
		
//		int bsPort = 6000; // c r e a t e s s impl e boot s t rap s e r v e r
//		this.bs = new BootstrapServerRunner(bsPort); // run bs
//		this.bs.start();
		
		System.out.println("Node " + nodeName + ": Initializing");
		
		this.node = new P2PNode(null, P2PNode.RoutingAlgorithm.SUPERPEER);
		this.node.setServerReflexiveAddress(serverIP);
		this.node.setServerReflexivePort(serverPort);
		this.node.setBootIP(serverIP);
		this.node.setBootPort(serverPort);
		this.node.setUserName(nodeName);
		this.node.setUdpPort(portOut);
		this.node.networkJoin();

		while (!this.node.isConnected()) {
			System.out.println("Node " + nodeName + ": Not connected :(");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("SimpleNode.SimpleNode()" + e);
				Logger.getLogger(BasicCollectionUsage.class.getName()).log(
						Level.SEVERE, null, e);
			}
		}
		System.out.println("Node " + nodeName + ": Connected !!");

		// Jeżeli uda się połączyć to tworzę JCSyncCore
		this.jcSyncCore = new JCSyncCore(this.node, serverPort);

		try {
			this.jcSyncCore.init();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(BasicCollectionUsage.class.getName()).log(
					Level.SEVERE, null, e);
		}

		// Tworzenie lub podpinanie się pod kolekcję

		// HASH MAP
		String collID = "mojaMapa";
		try {
			jcSyncHashMap = createHashMap(collID, this.jcSyncCore);
			System.out.println("Node " + nodeName
					+ ": Utworzono nową kolekcję o ID: " + collID);
		} catch (ObjectExistsException e) {
			System.out.println("Node " + nodeName + ": Kolekcja " + collID
					+ " już istnieje, zatem spróbuję się podpiąć");
			try {
				jcSyncHashMap = (JCSyncHashMap<String, String>) subscribeCollection(
						collID, this.jcSyncCore).getNucleusObject();
				System.out.println("Node " + nodeName + ": Kolekcja " + collID
						+ " już istnieje i pomyślnie się do niej podpieliśmy");
			} catch (ObjectNotExistsException e1) {
				System.err.println("Koleckcja od ID " + collID
						+ " jednak nie istnieje");
				e1.printStackTrace();
			} catch (OperationForbiddenException e1) {
				System.err.println("Dziwny błąd");
				e1.printStackTrace();
			} catch (Exception e1) {
				System.err.println("Dziwny błąd");
				e1.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Node " + nodeName + ": Nienany błąd");
			e.printStackTrace();
		}
		
		// ARRAYLIST
		String collIDa = "mojaLista";
		try {
			jcSyncArrayList = createArrayList(collIDa, jcSyncCore, null);
			System.out.println("Node " + nodeName
					+ ": Utworzono nową kolekcję o ID: " + collIDa);
		} catch (ObjectExistsException e) {
			System.out.println("Node " + nodeName + ": Kolekcja " + collIDa
					+ " już istnieje, zatem spróbuję się podpiąć");
			try {
				jcSyncArrayList = (JCSyncArrayList<String>) subscribeCollection(
						collIDa, this.jcSyncCore).getNucleusObject();
				System.out.println("Node " + nodeName + ": Kolekcja " + collIDa
						+ " już istnieje i pomyślnie się do niej podpieliśmy");
			} catch (ObjectNotExistsException e1) {
				System.err.println("Koleckcja od ID " + collIDa
						+ " jednak nie istnieje");
				e1.printStackTrace();
			} catch (OperationForbiddenException e1) {
				System.err.println("Dziwny błąd");
				e1.printStackTrace();
			} catch (Exception e1) {
				System.err.println("Dziwny błąd");
				e1.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Node " + nodeName + ": Nienany błąd");
			e.printStackTrace();
		}
		
		
		
		//this.observable.addObserver((Observer) this.getObservable(jcSyncArrayList));
	}

	/**
	 * Metoda tworzenia nowej czystej koleckji
	 * 
	 * @param collID
	 * @param jcSyncCore
	 * @return
	 * @throws ObjectExistsException
	 * @throws Exception
	 */
	public JCSyncHashMap createHashMap(String collID, JCSyncCore jcSyncCore)
			throws ObjectExistsException, Exception {
		// create collection
		JCSyncHashMap map = new JCSyncHashMap();
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
				collID, map, jcSyncCore);
		return map;
	}

	

	/**
	 * Metoda tworzenia nowej koleckji i przekazania jej już istniewjącej
	 * kolekcji
	 * 
	 * @param collID
	 * @param jcSyncCore
	 * @param coreHashMap
	 * @return
	 * @throws ObjectExistsException
	 * @throws Exception
	 */
	public JCSyncHashMap createHashMap(String collID, JCSyncCore jcSyncCore,
			HashMap coreHashMap) throws ObjectExistsException, Exception {
		// create collection
		JCSyncHashMap map = new JCSyncHashMap(coreHashMap);
		SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
				collID, map, jcSyncCore);
		return map;
	}
	
	/**
	 * Tworzenie nowej kolekcji ArrayList
	 * 
	 * @param collectionID
	 * @param jcSyncCore
	 * @param incomingArrayList
	 * @return
	 * @throws ObjectExistsException
	 * @throws Exception
	 */
	private JCSyncArrayList<String> createArrayList(String collectionID,
			JCSyncCore jcSyncCore, ArrayList<String> incomingArrayList)
			throws ObjectExistsException, Exception {
		// create collection instance
		if(incomingArrayList == null){
			incomingArrayList = new ArrayList<String>();
		}
		JCSyncArrayList<String> arrayList = new JCSyncArrayList<String>(incomingArrayList);
		// we need c r e a t e an Shar edCol l e c t ionObj e c t , that wi l l
		// be a s s i gned with -our c o l l e c t i o n .
		SharedCollectionObject sharedCollectionObject = new SharedCollectionObject(collectionID, arrayList,
				jcSyncCore);
		return (JCSyncArrayList<String>) sharedCollectionObject.getNucleusObject();
	}

	public SharedCollectionObject subscribeCollection(String collectionName,
			JCSyncCore coreAlg) throws ObjectNotExistsException,
			OperationForbiddenException, Exception {
		SharedCollectionObject sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject
				.getFromOverlay(collectionName, coreAlg);
		return sharedCollectionObject;
	}
	
	public JCSyncObservable getObservable(JCSyncCore jcSyncCore){
		String observableID = "mojObserwator";
		SharedObservableObject sharedObservableObject = null;
		try {
			sharedObservableObject = (SharedObservableObject) SharedObservableObject.getFromOverlay(observableID, jcSyncCore);
		} catch (ObjectNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationForbiddenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (JCSyncObservable) sharedObservableObject.getNucleusObject();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("OBSERVER");
		
	}
	
	public static void main(String[] args) {
		NodeTestowy nt = new NodeTestowy(6061, "127.0.0.1", 6060, "testowy1");
		//nt.testRead();
		nt.writeTest();
		nt.testWrite();
		}
	
	private void writeTest() {
		jcSyncArrayList.add("aa");
		
	}

	/**
	 * 
	 */
	public void testWrite() {
		while (true) {
			jcSyncArrayList.add(""+ new Date().getDate());
			snooze(500);
			
		}
	}
	
	public void testRead(){
		while(true){
			System.out.print("AL: size:" + jcSyncArrayList.size());
			if(jcSyncArrayList.size() > 0){
				System.out.print(" LAST:"+ jcSyncArrayList.get(jcSyncArrayList.size()-1));
			}
			System.out.println("");
			snooze(1000);
		}
	}
	
	public void snooze(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			Logger.getLogger(BasicCollectionUsage.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

}
