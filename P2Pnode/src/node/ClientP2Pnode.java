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
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;

import java.util.Observable;
import java.util.Observer;

/**
 * Connects application with P2P Arctite platform (http://p2pm.sourceforge.net)
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 * @version 0.9
 */
public class ClientP2Pnode {

    public static final Logger LOG = Logger.getLogger(ClientP2Pnode.class);
    private Controller controller;
    private P2PNode node;
    private ClientP2PnodeCallback nodeCallback;
    private JCSyncHashMap<String, Nod> jcSyncHashMap;
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

        LOG.trace("JCSyncCore jcSyncCore = new JCSyncCore(this.node, "+serverPort+this.node.getTcpPort()+");");
        JCSyncCore jcSyncCore = new JCSyncCore(this.node, serverPort+this.node.getTcpPort());
        LOG.info("Initializing JCSyncCore HashMap");
        try {
            jcSyncCore.init();

            this.jcSyncHashMap = new JCSyncHashMap<String, Nod>();

            SharedCollectionObject jcSyncHashMap_sharedCollectionObject;
            String collID = "P2PFileshareTreeMapCollection";
            try {
                LOG.info("Creating the new collection; User:"+this.node.getUserName());
                jcSyncHashMap_sharedCollectionObject = new SharedCollectionObject(collID, this.jcSyncHashMap, jcSyncCore, DefaultConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.info("Collection JCSyncHashMap exists -> Connecting to the collection JCSyncHashMap; User:"+this.node.getUserName());
                jcSyncHashMap_sharedCollectionObject = (SharedCollectionObject) SharedCollectionObject.getFromOverlay(collID, jcSyncCore);
                this.jcSyncHashMap = (JCSyncHashMap<String, Nod>) jcSyncHashMap_sharedCollectionObject.getNucleusObject();
            }

            jcSyncHashMap_sharedCollectionObject.addObserver(this.collectionObserver);
            jcSyncHashMap_sharedCollectionObject.addStateListener(this.collectionListener);
            LOG.info("Collection based on JCSyncHashMap (" + collID + ") has been initialized. Listener and Observer has been added.");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Collection not initialized !!");
        }
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
