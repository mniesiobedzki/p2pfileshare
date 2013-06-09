package main;

import file.FileServer;
import folder.FolderServer;
import folder.FolderTree;
import gui.GuiWindower;
import node.ClientP2Pnode;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Main Controller from MVC
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 */
public class Controller {

    public static final Logger LOG = Logger.getLogger(Controller.class);
    private GuiWindower gui;
    private FolderTree folderTree;
    private ClientP2Pnode clientP2Pnode;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    /* Action Listener */
    private ActionListener guziki = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Akcja " + e.getActionCommand());
            LOG.info(" wybrany interface ma ip: " + gui.getClientIp());
            if (e.getActionCommand().equals("Rozpocznij")) {


                clientP2Pnode.initializeJCSyncHashMap(Integer.parseInt(gui.getClientPort()), gui.getServerAddress(), Integer.parseInt(gui.getServerPort()), gui.getClientName());

                while (!clientP2Pnode.isConnected()) {
                    LOG.info("Node still connecting");
                    snooze(1000);
                }
                LOG.info("Node connected");

                // Podpięcie foleru do wymiany plików
                LOG.info("Tworze nowy obiekt folder Tree z argumentami(" + gui.getFolderPath() + ", " + gui.getClientName() + ", drzewo-JCSync, " + gui.getClientIp() + ", " + gui.getPortIn() + ")");
                folderTree = new FolderTree(gui.getFolderPath(), gui.getClientName(), clientP2Pnode.getJCSyncHashMap(), gui.getClientIp(), gui.getPortIn());
                LOG.info("Tworze nowy obiekt FileServer z argumentami(folderTree" + ", " + gui.getClientName() + ", " + gui.getPortIn() + ")");
                FolderServer fs = new FolderServer(folderTree, folderTree.localUser, gui.getFolderPath());
                new  Thread(fs).start();
                FileServer server = new FileServer(folderTree, gui.getClientName(), gui.getPortIn());
                updateTree();

            }
        }
    };


    public Controller(GuiWindower guiWindower) {
        this.gui = guiWindower;
        this.gui.addButtonActionListener(guziki); // podpięcie guzików
        this.addPropertyChangeListener(this.gui); // podpięcie zmian

        clientP2Pnode = new ClientP2Pnode(this);

        LOG.debug("Controller - created");
    }

    private void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void snooze(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {

        }
    }

    /**
     * Updates files in the folder
     */
    public void updateTree() {
        LOG.info("Updating Tree with files. MSG from Listener");
            folderTree.update();
    }
}
