package main;

import node.ClientP2Pnode;
import file.File;
import gui.GuiWindower;

public class Start {

	//

	public static void main(String[] args) {

	}

	public void runMVC() {

		// ####### MODEL #######
		// P2P
		ClientP2Pnode clientP2Pnode = new ClientP2Pnode();
		// Local files on the computer
		File localFiles = new File(clientP2Pnode);

		// ####### VIEW #######
		GuiWindower gui = new GuiWindower();

		// ####### CONTROLER #######
		Controller controller = new Controller(gui, localFiles, clientP2Pnode);

	}

}
