package test;


import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.core.NetworkObject;
import pl.edu.pjwstk.mteam.core.Node;
import pl.edu.pjwstk.mteam.core.NodeCallback;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncAbstractSharedObject;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncCore;
import pl.edu.pjwstk.mteam.jcsync.core.JCSyncStateListener;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.SharedCollectionObject;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.SharedObservableObject;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;
import pl.edu.pjwstk.mteam.jcsync.exception.OperationForbiddenException;
import pl.edu.pjwstk.mteam.p2p.P2PNode;
import pl.edu.pjwstk.mteam.p2pm.tests.tests.tests.jcsyncbasic.ConsistencyManager;
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
        public void onMessageDelivery(List<NetworkObject> networkObjects) {
            //To change body of implemented methods use File | Settings | File Templates.
        }


    };

    private JCSyncCore jcsyncCore;
    private JCSyncObservable observable;
    private JCSyncHashMap<String, OperationDetails> collection;
    private SharedCollectionObject collection_so;

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
    private JCSyncStateListener collectionListener = new JCSyncStateListener() {
        public void onLocalStateUpdated(JCSyncAbstractSharedObject object, String methodName, Object retVal) {
            LOG.debug("collection onLocalStateUpdated callback invoked method=" + methodName + ": " + collection);
            System.out.println(" LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL LOCAL ");
        }

        public void onRemoteStateUpdated(JCSyncAbstractSharedObject object, String methodName, Object retVal) {
            LOG.debug("collection onRemoteStateUpdated callback invoked method=" + methodName + ": " + collection);
            System.out.println(" REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE REMOTE ");
        }
    };
    private SharedObservableObject observable_so;
    //private SharedObservableObject observable_so;


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
            this.jcsyncCore = new JCSyncCore(this.p2pNode, this.p2pNode.getTcpPort() + 2);
            this.jcsyncCore.init();
            this.observable = new JCSyncObservable();
            this.collection = new JCSyncHashMap<String, OperationDetails>();
            LOG.trace("Creating the collection");
            try {
                this.collection_so = new SharedCollectionObject("collection", this.collection, this.jcsyncCore, ConsistencyManager.class);
            } catch (ObjectExistsException e) {
                LOG.debug("Collection already exists -- getting it...");
                this.collection_so = (SharedCollectionObject) SharedCollectionObject.getFromOverlay("collection", this.jcsyncCore);
                this.collection = (JCSyncHashMap<String, OperationDetails>) this.collection_so.getNucleusObject();
                LOG.debug("I got the collection: " + this.collection);

            }

            this.observable = this.getObservable(jcsyncCore);

            this.observable.addObserver(this.collectionObserver);
            this.collection_so.addStateListener(this.collectionListener);


        } catch (Throwable e) {
            LOG.error("Error while initializing JCSync: " + e);
        }

        doStuff();

    }

    public void doStuff() {

        LOG.debug("Doing stuff");

        String userName = this.p2pNode.getUserName();
        LOG.trace("Invoking operation: " + this.collection + " " + new OperationDetails(userName, userName + "@0", System.currentTimeMillis()));
        this.collection.put("key", new OperationDetails(userName, userName + "@0", System.currentTimeMillis()));
        LOG.info("Collection after the operation: " + this.collection);

    }

    public static void main(String args[]) {

        JCSyncExample2 example = new JCSyncExample2();

        try {
            // example.initLayer(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            example.initLayer("127.0.0.1", 21000, "user3", 22021);
        } catch (Throwable e) {
            LOG.error("Error while initializing layer: " + e);
        }

    }

    public JCSyncObservable getObservable(JCSyncCore jcsynccore) {
        JCSyncObservable jcSyncObservable = new JCSyncObservable();
        try {
            this.observable_so = new SharedObservableObject("myCollection_obs",
                    jcSyncObservable, jcsynccore);
        } catch (ObjectExistsException e) {
            try {
                this.observable_so = (SharedObservableObject) SharedObservableObject
                        .getFromOverlay("myCollection_obs", jcsynccore);
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

            jcSyncObservable = (JCSyncObservable) this.observable_so
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
}