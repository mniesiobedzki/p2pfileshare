package node;

import pl.edu.pjwstk.mteam.core.NetworkObject;
import pl.edu.pjwstk.mteam.core.Node;
import pl.edu.pjwstk.mteam.core.NodeCallback;

import java.util.List;

/**
 * Callback-i
 *
 * @author marek niesiobędzki
 */
public class ClientP2PnodeCallback implements NodeCallback {

    @Override
    public boolean onDeliverRequest(List<NetworkObject> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onDisconnect(Node arg0) {
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " disconnected");

    }

    @Override
    public boolean onForwardingRequest(List<NetworkObject> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onBootstrapError(Node node, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onMessageDelivery(String s, List<NetworkObject> networkObjects) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void onInsertObject(Node arg0, NetworkObject arg1) {
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " inserts new object");

    }

    @Override
    public void onJoin(Node arg0) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " connected");

    }

    @Override
    public void onObjectLookup(Node arg0, Object arg1) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onObjectLookup.");

    }

    @Override
    public void onOverlayError(Node arg0, Object arg1, int arg2) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onOverlayError.");

    }

    @Override
    public void onOverlayError(Node arg0, Object arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onOverlayError.");

    }

    @Override
    public void onPubSubError(Node arg0, Object arg1, short arg2, int arg3) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onPubSubError.");

    }

    @Override
    public void onPubSubError(Node arg0, Object arg1, short arg2, int arg3,
                              int arg4) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onPubSubError.");

    }

    @Override
    public void onTopicCreate(Node arg0, Object arg1) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicCreate.");

    }

    @Override
    public void onTopicCreate(Node arg0, Object arg1, int arg2) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicCreate.");

    }

    @Override
    public void onTopicNotify(Node arg0, Object arg1, byte[] arg2,
                              boolean arg3, short arg4) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicNotify.");

    }

    @Override
    public void onTopicRemove(Node arg0, Object arg1) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicRemove.");

    }

    @Override
    public void onTopicSubscribe(Node arg0, Object arg1) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicSubscribe.");

    }

    @Override
    public void onTopicSubscribe(Node arg0, Object arg1, int arg2) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicSubscribe.");

    }

    @Override
    public void onTopicUnsubscribe(Node arg0, Object arg1, int arg2) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onTopicUnsubscribe.");

    }

    @Override
    public void onUserLookup(Node arg0, Object arg1) {
        // TODO Auto-generated method stub
        System.out.println("NodeCallback: Node " + arg0.getUserName() + " -> onUserLookup.");

    }

}
