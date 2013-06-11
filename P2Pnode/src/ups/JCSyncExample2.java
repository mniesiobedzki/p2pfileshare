package ups;

import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.core.NetworkObject;
import pl.edu.pjwstk.mteam.core.Node;
import pl.edu.pjwstk.mteam.core.NodeCallback;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncAbstractSharedObject;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncStateListener;
import pl.edu.pjwstk.mteam.jcsync.core.consistencyManager.DefaultConsistencyManager;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;
import pl.edu.pjwstk.mteam.p2pm.tests.tests.tests.jcsyncbasic.OperationDetails;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class JCSyncExample2 {

    public static final Logger LOG = Logger.getLogger(JCSyncExample2.class);

    private P2PNode p2pNode;
    private NodeCallback p2pNodeCallback = new NodeCallback() {
        @Override
        public void onDisconnect(Node node) {
        }

        @Override
        public void onUserLookup(Node node, Object userInfo) {
        }

        @Override
        public void onObjectLookup(Node node, Object object) {
        }

        @Override
        public void onTopicNotify(Node node, Object topicID, byte[] message, boolean historical, short eventType) {
        }

        @Override
        public void onTopicCreate(Node node, Object topicID) {
        }

        @Override
        public void onTopicCreate(Node node, Object topicID, int transID) {
        }

        @Override
        public void onTopicRemove(Node node, Object topicID) {
        }

        @Override
        public void onTopicSubscribe(Node node, Object topicID) {
        }

        @Override
        public void onTopicSubscribe(Node node, Object topicID, int transID) {
        }

        @Override
        public void onTopicUnsubscribe(Node node, Object topicID, int respCode) {
        }

        @Override
        public void onInsertObject(Node node, NetworkObject object) {
        }

        @Override
        public void onJoin(Node node) {

            initJCSync();
            //An event/information that layer is initialized should probably go here also.

        }

        @Override
        public void onOverlayError(Node node, Object sourceID, int errorCode) {
        }

        @Override
        public void onOverlayError(Node node, Object sourceID, int errorCode, int transID) {
        }

        @Override
        public void onPubSubError(Node node, Object topicID, short operationType, int errorCode) {
        }

        @Override
        public void onPubSubError(Node node, Object topicID, short operationType, int errorCode, int transID) {
        }

        @Override
        public boolean onDeliverRequest(List<NetworkObject> objectList) {
            return false;
        }

        @Override
        public boolean onForwardingRequest(List<NetworkObject> objectList) {
            return false;
        }

        @Override
        public void onBootstrapError(Node node, int errorCode) {
        }

        @Override
        public void onMessageDelivery(String s, List<NetworkObject> networkObjects) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


    };

    private JCSyncCore jcsyncCore;
    private JCSyncHashMap<String, OperationDetails> collection;
    private SharedCollectionObject collection_so;
    private Observer collectionObserver = new Observer() {


        @Override
        public void update(Observable o, Object arg) {

            //System.out.println("UPDATE w OBSERWER");

            //String args_ = (String)arg;
            //LOG.trace("[Update o=" + o + "] " + arg);
            LOG.debug("OBSERWER OBSERWER OBSERWER Update o=" + o + "] " + arg);
        }
    };
    private JCSyncStateListener collectionListener = new JCSyncStateListener() {
        public void onLocalStateUpdated(JCSyncAbstractSharedObject object, String methodName, Object retVal) {
            LOG.debug("LOCAL LOCAL LOCAL collection onLocalStateUpdated callback invoked method=" + methodName + ": " + collection);
            // System.out.println(" LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL ");
        }

        public void onRemoteStateUpdated(JCSyncAbstractSharedObject object, String methodName, Object retVal) {
            LOG.debug("REMOTE REMOTE REMOTE collection onRemoteStateUpdated callback invoked method=" + methodName + ": " + collection);
            //System.out.println(" REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE ");
        }
    };


    public void initLayer(String bootIP, int bootPort, String userName, int tcpPort) throws Exception {

        LOG.debug("Initializing layer");

        this.p2pNode = new P2PNode(this.p2pNodeCallback, P2PNode.RoutingAlgorithm.SUPERPEER);
        this.p2pNode.setBootIP(bootIP);
        this.p2pNode.setBootPort(bootPort);
        this.p2pNode.setUserName(userName);
        this.p2pNode.setTcpPort(tcpPort);

        this.p2pNode.networkJoin();

    }

    public void initJCSync() {

        LOG.debug("Initializing JCSync");

        try {

            LOG.trace("Initializing JCSyncCore");
            this.jcsyncCore = new JCSyncCore(this.p2pNode, this.p2pNode.getTcpPort() + 20);
            this.jcsyncCore.init();
            this.collection = new JCSyncHashMap<String, OperationDetails>();
            LOG.trace("Creating the collection");
            try {
                this.collection_so = new SharedCollectionObject("collection", this.collection, this.jcsyncCore, DefaultConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.debug("Collection already exists -- getting it...");
                this.collection_so = (SharedCollectionObject) SharedCollectionObject.getFromOverlay("collection", this.jcsyncCore);
                this.collection = (JCSyncHashMap<String, OperationDetails>) this.collection_so.getNucleusObject();
                LOG.debug("I got the collection: " + this.collection);

            }
            this.collection_so.addObserver(this.collectionObserver);
            this.collection_so.addStateListener(this.collectionListener);

        } catch (Throwable e) {
            LOG.error("Error while initializing JCSync: " + e);
        }

        doStuff();

    }

    public void doStuff() {
        int i = 0;


        //snooze(10000);
        do {
            LOG.info("DOING STUFF");
            String userName = this.p2pNode.getUserName();
            LOG.trace("Invoking operation: " + this.collection + " " + new OperationDetails(userName, userName + "@0", System.currentTimeMillis()));
            this.collection.put("key" + i, new OperationDetails(userName, userName + "@0", System.currentTimeMillis()));
            LOG.info("Collection after the operation: " + this.collection);
            snooze(3000);
            i++;
        } while (true);

    }

    public static void main(String args[]) {

        Thread t = Thread.currentThread();
        t.setName("Node2Thread");
        System.out.println("Current thread : " + t.getName() + " " + t);

        JCSyncExample2 example = new JCSyncExample2();

        try {
            // example.initLayer(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            example.initLayer("127.0.0.1", 21000, "Node2", 21221);
        } catch (Throwable e) {
            LOG.error("Error while initializing layer: " + e);
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

}