package node;

import folder.Nod;
import main.Controller;
import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncAbstractSharedObject;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncStateListener;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncArrayList;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.SharedObservableObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

import java.util.Observable;
import java.util.TreeMap;

/**
 * Klasa odpowiedzialna za połączenie z platformą P2Pm
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 * @version 0.9
 */
public class ClientP2Pnode {

    public static final Logger LOG = Logger.getLogger(ClientP2Pnode.class);
    private Controller controller;
    private P2PNode node;
    // Podstawowa implementacja JCSync
    private JCSyncCore jcSyncCore;
    private JCSyncTreeMap<String, Nod> jcSyncTreeMap;
    private JCSyncArrayList<String> jcSyncArrayList;

    // KOLECKCJE APLIKACJI
    // private HashMap<String, String> myHashMap;
    // private ArrayList<String> myArrayList;
    // OBSERWACJA
    private Observable observable;
    private JCSyncObservable jcSyncObservable;
    private TestObserver testObserver;
    private ClientP2PnodeCallback nodeCallback;
    private SharedObservableObject observable_so;
    private JCSyncStateListener collectionListener = new JCSyncStateListener() {
        public void onLocalStateUpdated(JCSyncAbstractSharedObject object,
                                        String methodName, Object retVal) {
            LOG.debug("collection onLocalStateUpdated callback invoked method="
                    + methodName + ": " + object.getID());
            System.out
                    .println(" LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL ");
        }

        public void onRemoteStateUpdated(JCSyncAbstractSharedObject object,
                                         String methodName, Object retVal) {
            LOG.debug("collection onRemoteStateUpdated callback invoked method="
                    + methodName + ": " + object.getID());
            System.out
                    .println(" REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE ");

            // aktualizacja plików na dysku. Chciałbym to zrobić ładnie przez obserwatora, ale niestety nie działa.
            controller.updateTree();
        }
    };

    @SuppressWarnings("unchecked")
    public ClientP2Pnode(int portOut, String serverIP, int serverPort,
                         String nodeName, Controller controller) {
        System.out.println("Node " + nodeName + ": Initializing");

        this.nodeCallback = new ClientP2PnodeCallback();

        this.node = this.connect(serverIP, serverPort, nodeName, portOut, this.nodeCallback);

        // JCSyncObservable nn = new JCSyncObservable();
        // nn.addObserver(o)

        while (!this.node.isConnected()) {
            System.out.println("Node " + nodeName + ": Not connected :(");
            snooze(1000);
        }

        System.out.println("Node " + nodeName + ": Connected !!");

        // Jeżeli uda się połączyć to tworzę JCSyncCore
        this.jcSyncCore = new JCSyncCore(this.node, serverPort);

        try {
            this.jcSyncCore.init();
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * Logger.getLogger(BasicCollectionUsage.class.getName()).log(
			 * Level.SEVERE, null, e);
			 */
        }

        // Tworzenie lub podpinanie się pod kolekcję

        // HASH MAP
        String collID = "myMap";
        try {
            jcSyncTreeMap = createTreeMap(collID, this.jcSyncCore);
            System.out.println("Node " + nodeName
                    + ": Utworzono nową kolekcję o ID: " + collID);
        } catch (ObjectExistsException e) {
            System.out.println("Node " + nodeName + ": Kolekcja " + collID
                    + " już istnieje, zatem spróbuję się podpiąć");
            try {
                jcSyncTreeMap = (JCSyncTreeMap<String, Nod>) subscribeCollection(
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

        this.controller = controller;

        this.jcSyncObservable = this.getObservable2(this.jcSyncCore);

        CollectionListener cl = new CollectionListener(this.jcSyncObservable);

        this.observable_so.addStateListener(collectionListener);
    }

    public static void main(String[] args) {
        ClientP2Pnode nt = new ClientP2Pnode(6062, "1.1.1.4", 6060, "testowy2", null);
        // nt.testWrite();

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
    public JCSyncTreeMap createTreeMap(String collID, JCSyncCore jcSyncCore)
            throws ObjectExistsException, Exception {
        JCSyncTreeMap map = new JCSyncTreeMap<String, Nod>();
        SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
                collID, map, jcSyncCore);
        return map;
    }

    public JCSyncTreeMap createTreeMap(String collID, JCSyncCore jcSyncCore,
                                       TreeMap coreTreeMap) throws ObjectExistsException, Exception {
        JCSyncTreeMap map = new JCSyncTreeMap<String, Nod>(coreTreeMap);
        SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
                collID, map, jcSyncCore);
        return map;
    }

    public SharedCollectionObject subscribeCollection(String collectionName,
                                                      JCSyncCore coreAlg) throws ObjectNotExistsException,
            OperationForbiddenException, Exception {
        SharedCollectionObject sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject
                .getFromOverlay(collectionName, coreAlg);
        return sharedCollectionObject;
    }

    /**
     * NIE DZIAŁA PRZEZ BLĄD PLATFROMY P2Pm
     *
     * @param jcSyncCore
     * @return
     */
    public JCSyncObservable getObservable(JCSyncCore jcSyncCore) {
        String observableID = "myMap";
        SharedObservableObject sharedObservableObject = null;
        try {
            sharedObservableObject = (SharedObservableObject) SharedObservableObject
                    .getFromOverlay(observableID, jcSyncCore);
        } catch (ObjectNotExistsException e) {
            // TODO Auto-generated catch block
            // sharedObservableObject = new
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

    /**
     * Metoda z JCSyncBasicTest - OBSERWATOR
     * NIE DZIAŁA PRZEZ BLĄD PLATFROMY P2Pm
     *
     * @param jcsynccore
     * @return
     */
    public JCSyncObservable getObservable2(JCSyncCore jcsynccore) {
        JCSyncObservable jcSyncObservable = new JCSyncObservable();
        try {
            this.observable_so = new SharedObservableObject("myCollection_obs",
                    jcSyncObservable, this.jcSyncCore);
        } catch (ObjectExistsException e) {
            try {
                this.observable_so = (SharedObservableObject) SharedObservableObject
                        .getFromOverlay("myCollection_obs", this.jcSyncCore);
            } catch (ObjectNotExistsException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (OperationForbiddenException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            this.jcSyncObservable = (JCSyncObservable) this.observable_so
                    .getNucleusObject();
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        /*
		 * try { this.jcSyncHashMap_sharedObject = new
		 * SharedObservableObject("myMap", this.jcSyncHashMap, this.jcSyncCore,
		 * ConsistencyManager.class); } catch (ObjectExistsException e) {
		 * this.jcSyncHashMap_sharedObject =
		 * SharedCollectionObject.getFromOverlay("myMap", this.jcs) }
		 */

        return jcSyncObservable;

    }

    /**
     * @return
     * @throws ObjectNotExistsException
     */
    public JCSyncObservable getObservable() throws ObjectNotExistsException {
        return this.getObservable(jcSyncCore);
    }

    public void snooze(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
			/*
			 * Logger.getLogger(BasicCollectionUsage.class.getName()).log(
			 * Level.SEVERE, null, ex);
			 */
        }
    }

    /**
     * Zwraca czy node jest podłączony do sieci P2P
     *
     * @return true - node połączony do sieci P2P
     */
    public boolean isConnected() {
        return this.node.isConnected();
    }

    public JCSyncTreeMap getJCSyncTreeMap() {
        return this.jcSyncTreeMap;
    }

    /**
     * Initialize connection with bootstrap server.
     *
     * @param serverAddress         - server IP address or host
     * @param serverPort            - server port
     * @param clientName            - client name
     * @param clientPort            - outgoing client port number (int)
     * @param clientP2PnodeCallback - nodeCallback object
     * @return node - P2Pnode connected (or not) to P2P network
     */
    public P2PNode connect(String serverAddress, int serverPort,
                           String clientName, int clientPort, ClientP2PnodeCallback clientP2PnodeCallback) {

        P2PNode node = new P2PNode(clientP2PnodeCallback,
                P2PNode.RoutingAlgorithm.SUPERPEER);
        node.setServerReflexiveAddress(serverAddress);
        node.setServerReflexivePort(serverPort);
        node.setBootIP(serverAddress);
        node.setBootPort(serverPort);
        node.setUserName(clientName);
        node.setTcpPort(clientPort);

        node.networkJoin();

        return node;
    }

}
