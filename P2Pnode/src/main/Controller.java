package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import file.File;
import folder.FolderTree;
import gui.GuiWindower;
import node.ClientP2Pnode;

/**
 * Main Controller
 * 
 * @author Marek Niesiobędzki
 * 
 */
public class Controller {

	private GuiWindower gui;
	private FolderTree folderTree;
	private ClientP2Pnode clientP2Pnode;
	private File file;
	
	private PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);

	public Controller(GuiWindower gui, FolderTree folderTree, File file,
			ClientP2Pnode clientP2Pnode) {
		this.gui = gui;
		this.folderTree = folderTree;
		this.file = file;
		this.clientP2Pnode = clientP2Pnode;
		
		this.gui.addButtonActionListener(guziki); // podpięcie guzików
		this.addPropertyChangeListener(this.gui); // podpięcie zmian
	}
	
	private void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);		
	}

	/* Action Listener */
	private ActionListener guziki = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("nazwa")){
				//TODO: coś
			}
			
		}
	};

	/**
	 * Metoda dla GUI do ustawiania serwera
	 * 
	 * @param serverIP
	 *            - IP boostrapserver
	 */
	public void setServerIP(String serverIP) {
		clientP2Pnode.setServerIP(serverIP);
	}

	public void setServerPort(int serverPort) {
		//clientP2Pnode.setServerPort(serverPort);
	}

	public void setClientPort(int clientPort) {
		//clientP2Pnode.setClientPort(clientPort);
	}

	public void setClientName(String clientName) {
		//clientP2Pnode.setClientName(clientName);
	}

	public void setFolderTreePath(String path) {
		//
	}

	public boolean startSync() {
		return true;
	}

	public boolean stopSync() {
		return true;
	}
	
	

}
