package main;

import node.ClientP2Pnode;
import node.ClientP2PnodeCallback;
import file.File;
import folder.FolderTree;
import gui.GuiWindower;

public class Start {

	//

	public static void main(String[] args) {

	}

	public void runMVC() {

		// ####### MODEL #######
		// P2P
		ClientP2PnodeCallback clientP2PnodeCallback = new ClientP2PnodeCallback();
		ClientP2Pnode clientP2Pnode = new ClientP2Pnode(clientP2PnodeCallback);
		
		// Local files on the computer
		FolderTree folderTree = new FolderTree();

		// ####### VIEW #######
		GuiWindower gui = new GuiWindower();

		// ####### CONTROLER #######
		Controller controller = new Controller(gui, folderTree, clientP2Pnode);

	}
	

}
