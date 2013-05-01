package main;

import file.File;
import gui.GuiWindower;
import node.ClientP2Pnode;

/**
 * Main Controller
 * @author Marek Niesiobędzki
 *
 */
public class Controller {

	private GuiWindower gui;
	private File localFiles;
	private ClientP2Pnode clientP2Pnode;

	public Controller(GuiWindower gui, File localFiles,
			ClientP2Pnode clientP2Pnode) {
				this.gui = gui;
				this.localFiles = localFiles;
				this.clientP2Pnode = clientP2Pnode;
	}
	
	

}
