import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pl.edu.pjwstk.mteam.core.NetworkObject;
import pl.edu.pjwstk.mteam.core.Node;
import pl.edu.pjwstk.mteam.core.NodeCallback;
import pl.edu.pjwstk.mteam.jcsync.exception.ObjectNotExistsException;

import node.ClientP2Pnode;

public class MainMarek implements NodeCallback {

	private Observable obs;

	public MainMarek() {
		
		
		//this.obs = obs;
		ClientP2Pnode clientP2Pnode = new ClientP2Pnode(6061, "127.0.0.1",
				6060, "testowy1");
		
		//this.onJoin(clientP2Pnode.getNode());
		

	}

	
	public static void main(String[] args) {
		MainMarek mm = new MainMarek();
		
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

	@Override
	public boolean onDeliverRequest(List<NetworkObject> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDisconnect(Node arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onForwardingRequest(List<NetworkObject> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onInsertObject(Node arg0, NetworkObject arg1) {
		// TODO Auto-generated method stub
		System.out.println("Nowy insert obiekt");
		
	}

	@Override
	public void onJoin(Node arg0) {
		// TODO Auto-generated method stub
		System.out.println("Nowy Node");
		
	}

	@Override
	public void onObjectLookup(Node arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOverlayError(Node arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOverlayError(Node arg0, Object arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPubSubError(Node arg0, Object arg1, short arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPubSubError(Node arg0, Object arg1, short arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicCreate(Node arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicCreate(Node arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicNotify(Node arg0, Object arg1, byte[] arg2,
			boolean arg3, short arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicRemove(Node arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicSubscribe(Node arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicSubscribe(Node arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTopicUnsubscribe(Node arg0, Object arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserLookup(Node arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
