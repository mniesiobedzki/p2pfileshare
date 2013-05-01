package main;

import file.File;
import folder.FolderTree;
import gui.GuiWindower;
import node.ClientP2Pnode;

/**
 * Main Controller
 * @author Marek Niesiobędzki
 *
 */
public class Controller {

	private GuiWindower gui;
	private FolderTree localFiles;
	private ClientP2Pnode clientP2Pnode;

	public Controller(GuiWindower gui, FolderTree folderTree,
			ClientP2Pnode clientP2Pnode) {
				this.gui = gui;
				this.localFiles = folderTree;
				this.clientP2Pnode = clientP2Pnode;
	}
	
	/**
	 * Metoda dla GUI do ustawiania serwera
	 * @param serverIP - IP boostrapserver
	 */
	public void setServerIP(String serverIP){
		clientP2Pnode.setServerIP(serverIP);
	}
	
	public void setServerPort(int serverPort){
		clientP2Pnode.serServerPort(serverPort);
	}
	
	public void setClientPort(int clientPort){
		clientP2Pnode.setClientPort(clientPort);
	}
	
	public void setClientName(String clientName){
		
	}
	
	

}
