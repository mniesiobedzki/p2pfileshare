package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import node.ClientP2Pnode;
import file.FileServer;
import folder.FolderTree;
import gui.GuiWindower;

public class TestController {

	private GuiWindower gui;
    private FolderTree folderTree;
    //private ClientP2Pnode clientP2Pnode;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
	private ActionListener guziki = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("Akcja " + ae.getActionCommand());
            if (ae.getActionCommand().equals("Rozpocznij")) {
            	String usr = gui.getClientName();
        		String thisIp = gui.getClientIp();
        		String bootstrapIp = gui.getServerAddress();
        		int portOut= gui.getPortOut();
        		int portFile= gui.getPortIn();
        		int bootstrapPort= Integer.parseInt(gui.getServerPort());
        		String fname=gui.getFolderPath();
        		String path=gui.getFolderPath();
        		HashMap<String,User> users= new HashMap<String,User>();
        		User u = new User(usr, bootstrapIp, portFile, portOut);
        		users.put(usr, u);
        		
        		NodTest nod = new NodTest(bootstrapIp, bootstrapPort, users, u);
        		
        		TestTreeClientHandler klient = new TestTreeClientHandler(nod);
        		
        		TestTreeServer serwer = new TestTreeServer(null,fname, usr, path, portFile, portOut, thisIp);
        		new Thread(klient).start();
        		new Thread(serwer).start();
        		try {
        			Thread.sleep(1000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}

        		//serwer.ft.addUser("usr2", path);
        		@SuppressWarnings("unused")
        		FileServer server = new FileServer(serwer.ft, usr, portFile);
         		while(true){
         			synchronized(klient.clients){
						for (TestTreeClientWBootstrap tc : klient.clients) {

        				if(tc.changed){
        					System.err.println("Wszed³em w zmianê pliku. FolderTree -> changed = true");
        					synchronized(serwer.ft){
        						serwer.ft.update(tc.ft.getFolder());
        					}
        						tc.changed=false;
        					
        				}else{
        					//System.err.println("Nie by³o zmiany w pliku. FolderTree -> changed = false");
        				}
    					

    					try {
    						Thread.sleep(1000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        			}
         		}
         		}
            }
        }
    };
	public TestController(GuiWindower gui) {
		this.gui = gui;
	    this.gui.addButtonActionListener(guziki); // podpiÄ™cie guzikÃ³w
	    this.addPropertyChangeListener(this.gui); // podpiÄ™cie zmian
	    
        //clientP2Pnode = new ClientP2Pnode(this);
	}

    private void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

}
