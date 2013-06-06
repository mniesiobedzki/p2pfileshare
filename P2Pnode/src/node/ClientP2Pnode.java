package node;

import folder.Nod;
import main.Controller;
import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncAbstractSharedObject;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncStateListener;
import pl.edu.pjwstk.mteam.jcsync.core.consistencyManager.DefaultConsistencyManager;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.SharedObservableObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

import java.util.Observable;
import java.util.Observer;

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
    //private Observable observable;
    private JCSyncObservable jcSyncObservable;
    private ClientP2PnodeCallback nodeCallback;
    private SharedObservableObject observable_so;
    private String collID = "P2PFileshareTreeMapCollection";
    private JCSyncHashMap<String, Nod> jcSyncHashMap;
    private SharedCollectionObject jcSyncHashMap_sharedCollectionObject;

    /**
     * Collection Listeners
     */
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

    /**
     * Collection Observer
     */
    private Observer collectionObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER OBSERVER ");
            LOG.trace("[Observer o=" + o + "] " + arg);
        }
    };


    public ClientP2Pnode(Controller controller) {

        this.controller = controller;
        this.nodeCallback = new ClientP2PnodeCallback();

        LOG.debug("ClientP2Pnode - created");
    }

    public void initializeJCSyncHashMap(int portOut, String serverIP, int serverPort, String nodeName) {

        LOG.info("Initializing connection to bootstrap - JCSyncCore HashMap");

        this.node = this.connect(serverIP, serverPort, nodeName, portOut, this.nodeCallback);

        while (!this.node.isConnected()) {
            LOG.info("Node " + nodeName + ": Not connected :(");
            snooze(500);
        }
        LOG.info("Node " + nodeName + ": Connected !!");


        this.jcSyncCore = new JCSyncCore(this.node, serverPort);
        LOG.info("Initializing JCSyncCore HashMap");
        try {
            this.jcSyncCore.init();

            this.jcSyncHashMap = new JCSyncHashMap<String, Nod>();

            try {
                LOG.info("Creating the new collection");
                this.jcSyncHashMap_sharedCollectionObject = new SharedCollectionObject(collID, this.jcSyncHashMap, this.jcSyncCore, DefaultConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.info("Collection JCSyncHashMap exists -> Connecting to the collection JCSyncHashMap");
                this.jcSyncHashMap_sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject.getFromOverlay(collID, this.jcSyncCore);
                this.jcSyncHashMap = (JCSyncHashMap<String, Nod>) this.jcSyncHashMap_sharedCollectionObject.getNucleusObject();
            }

            this.jcSyncHashMap_sharedCollectionObject.addObserver(this.collectionObserver);
            this.jcSyncHashMap_sharedCollectionObject.addStateListener(this.collectionListener);
            LOG.info("Collection based on JCSyncHashMap (" + collID + ") has been initialized. Listener and Observer has been added.");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Collection not initialized !!");
        }
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
