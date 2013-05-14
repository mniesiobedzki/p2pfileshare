package main;

import file.File;
import gui.GuiWindower;
import node.ClientP2Pnode;
import node.ClientP2PnodeCallback;

public class Start {


	private Controller controller;

	public static void main(String[] args) {
		Start s = new Start();
		s.runMVC();
	}

	public void runMVC() {

		// ####### MODEL #######
		// P2P
		ClientP2PnodeCallback clientP2PnodeCallback = new ClientP2PnodeCallback();
		ClientP2Pnode clientP2Pnode = new ClientP2Pnode(clientP2PnodeCallback);
		
		// Local files on the computer
		//FolderTree folderTree = new FolderTree();
		File file = new File();

		// ####### VIEW #######
		GuiWindower gui = new GuiWindower();

		controller = new Controller(gui, null, file, clientP2Pnode);

	}
	

}
