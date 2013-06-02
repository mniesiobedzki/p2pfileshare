package node;

import folder.Nod;
import main.Controller;
import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncAbstractSharedObject;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncStateListener;
import pl.edu.pjwstk.mteam.jcsync.core.consistencyManager.DefaultConsistencyManager;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncArrayList;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.SharedObservableObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

/**
 * Klasa odpowiedzialna za połączenie z platformą P2Pm
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 * @version 0.9
 */
public class ClientP2Pnode {

    public static final Logger LOG = Logger.getLogger(ClientP2Pnode.class);
    private SharedCollectionObject jcSyncTreeMap_sharedCollectionObject;
    private Controller controller;
    private P2PNode node;
    private JCSyncCore jcSyncCore;
    private JCSyncTreeMap<String, Nod> jcSyncTreeMap;
    private JCSyncArrayList<String> jcSyncArrayList;
    private Observable observable;
    private JCSyncObservable jcSyncObservable;
    private ClientP2PnodeCallback nodeCallback;
    private SharedObservableObject observable_so;
    private String collID = "P2PFileshareTreeMapCollection";

    private JCSyncStateListener collectionListener = new JCSyncStateListener() {
        public void onLocalStateUpdated(JCSyncAbstractSharedObject object,
                                        String methodName, Object retVal) {
            LOG.debug("collection onLocalStateUpdated callback invoked method="
                    + methodName + ": " + object.getID());
            System.out
                    .println("LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL ");
        }

        public void onRemoteStateUpdated(JCSyncAbstractSharedObject object,
                                         String methodName, Object retVal) {
            LOG.info("collection onRemoteStateUpdated callback invoked method="
                    + methodName + ": " + object.getID());
            System.out
                    .println("REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE ");

            // aktualizacja plików na dysku. Chciałbym to zrobić ładnie przez obserwatora, ale niestety nie działa.
            controller.updateTree();
        }
    };


    private Observer collectionObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE UPDATE ");
            if (o.equals(observable)) {
                String args_ = (String) arg;
                LOG.trace("[Update o=" + o + "] " + arg);
            }
        }
    };

    private JCSyncHashMap<String, Nod> jcSyncHashMap;
    private SharedCollectionObject jcSyncHashMap_sharedCollectionObject;


    public ClientP2Pnode(Controller controller) {

        this.controller = controller;
        this.nodeCallback = new ClientP2PnodeCallback();

        LOG.debug("ClientP2Pnode - created");
    }

    public void initializeJCSync(int portOut, String serverIP, int serverPort, String nodeName) {
        this.node = this.connect(serverIP, serverPort, nodeName, portOut, this.nodeCallback);

        // JCSyncObservable nn = new JCSyncObservable();
        // nn.addObserver(o)

        while (!this.node.isConnected()) {
            System.out.println("Node " + nodeName + ": Not connected :(");
            snooze(1000);
        }
        LOG.info("Node " + nodeName + ": Connected !!");

        LOG.info("Initializing JCSyncCore");
        this.jcSyncCore = new JCSyncCore(this.node, serverPort);

        try {
            this.jcSyncCore.init();
            this.observable = new JCSyncObservable();
            //initCollectionTreeMap(nodeName, controller);
            this.jcSyncTreeMap = new JCSyncTreeMap<String, Nod>();
            LOG.trace("Creating the collection");
            try {
                this.jcSyncTreeMap_sharedCollectionObject = new SharedCollectionObject(collID, this.jcSyncTreeMap, this.jcSyncCore, DefaultConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.info("Collection already exists");
                LOG.info("Connecting to the collection");
                this.jcSyncTreeMap_sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject.getFromOverlay(collID, this.jcSyncCore);
                this.jcSyncTreeMap = (JCSyncTreeMap<String, Nod>) this.jcSyncTreeMap_sharedCollectionObject.getNucleusObject();
            }

            this.observable.addObserver(this.collectionObserver);
            LOG.info("Collection added to the Observer");
            this.jcSyncTreeMap_sharedCollectionObject.addStateListener(this.collectionListener);
            LOG.info("Collection added to the Listener");
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * Logger.getLogger(BasicCollectionUsage.class.getName()).log(
			 * Level.SEVERE, null, e);
			 */
        }
    }

    public void initializeJCSyncHashMap(int portOut, String serverIP, int serverPort, String nodeName) {
        LOG.info("Initializing connection to bootstrap - JCSyncCore HashMap");
        this.node = this.connect(serverIP, serverPort, nodeName, portOut, this.nodeCallback);

        while (!this.node.isConnected()) {
            LOG.info("Node " + nodeName + ": Not connected :(");
            snooze(1000);
        }
        LOG.info("Node " + nodeName + ": Connected !!");


        this.jcSyncCore = new JCSyncCore(this.node, serverPort);
        LOG.info("Initializing JCSyncCore HashMap");
        try {
            this.jcSyncCore.init();
            this.observable = new JCSyncObservable();
            //initCollectionTreeMap(nodeName, controller);
            this.jcSyncHashMap = new JCSyncHashMap<String, Nod>();

            try {
                LOG.info("Creating the new collection");
                this.jcSyncHashMap_sharedCollectionObject = new SharedCollectionObject(collID, this.jcSyncHashMap, this.jcSyncCore, DefaultConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.info("Collection JCSyncHashMap exists");
                LOG.info("Connecting to the collection JCSyncHashMap");
                this.jcSyncHashMap_sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject.getFromOverlay(collID, this.jcSyncCore);
                this.jcSyncHashMap = (JCSyncHashMap<String, Nod>) this.jcSyncHashMap_sharedCollectionObject.getNucleusObject();
            }
            LOG.info("Adding the Observer");
            this.observable.addObserver(this.collectionObserver);
            LOG.info("Observer Added");
            LOG.info("Adding the Listener");
            this.jcSyncHashMap_sharedCollectionObject.addStateListener(this.collectionListener);
            LOG.info("Listener added");
        } catch (Exception e) {
            e.printStackTrace();
            /*
             * Logger.getLogger(BasicCollectionUsage.class.getName()).log(
			 * Level.SEVERE, null, e);
			 */
            LOG.error("Collection not initialized !!");
        }
    }

    /**
     * Metoda z przykładu Simple chat, zastąpiona inną metodą
     *
     * @param nodeName
     * @param controller
     */
    private void initCollectionTreeMap(String nodeName, Controller controller) {
        // TreeMap
        LOG.trace("Creating the collection");
        //collID = "P2PfileshareTreeMap";
        try {
            this.jcSyncTreeMap = createTreeMap(collID, this.jcSyncCore);
            LOG.trace("Node " + nodeName
                    + ": Utworzono nowa kolekcje o ID: " + collID);
        } catch (ObjectExistsException e) {
            System.out.println("Node " + nodeName + ": Kolekcja " + collID
                    + " już istnieje, zatem spróbuję się podpiąć");
            try {
                this.jcSyncTreeMap = (JCSyncTreeMap<String, Nod>) subscribeCollection(
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

    private void initCollectionHashMap(String nodeName, Controller controller) {
        // TreeMap
        LOG.trace("Creating the collection");
        //collID = "P2PfileshareTreeMap";
        try {
            this.jcSyncHashMap = createHashMap(collID, this.jcSyncCore);
            LOG.trace("Node " + nodeName
                    + ": Utworzono nowa kolekcje o ID: " + collID);
        } catch (ObjectExistsException e) {
            System.out.println("Node " + nodeName + ": Kolekcja " + collID
                    + " już istnieje, zatem spróbuję się podpiąć");
            try {
                this.jcSyncHashMap = (JCSyncHashMap<String, Nod>) subscribeCollection(
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

    public JCSyncHashMap createHashMap(String collID, JCSyncCore jcSyncCore)
            throws ObjectExistsException, Exception {
        JCSyncHashMap map = new JCSyncHashMap<String, Nod>();
        SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
                collID, map, jcSyncCore);
        return map;
    }

    public JCSyncHashMap createHashMap(String collID, JCSyncCore jcSyncCore,
                                       HashMap coreTreeMap) throws ObjectExistsException, Exception {
        JCSyncHashMap map = new JCSyncHashMap<String, Nod>(coreTreeMap);
        SharedCollectionObject sharedCollectionObject_1 = new SharedCollectionObject(
                collID, map, jcSyncCore);
        return map;
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
        LOG.info("Node created -> joining to the network ....");
        return node;
    }

    public JCSyncHashMap<String, Nod> getJCSyncHashMap() {
        return this.jcSyncHashMap;
    }
}
