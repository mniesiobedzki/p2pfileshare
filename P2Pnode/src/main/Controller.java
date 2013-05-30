package main;

import file.File;
import file.FileServer;
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
    private File file;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);
    /*
    public Controller(GuiWindower gui, FolderTree folderTree, File file,
			ClientP2Pnode clientP2Pnode) {
		this.gui = gui;
        LOG.debug("GUI - successful");

        this.folderTree = folderTree;
        LOG.debug("FolderTree - ");

        //TODO: Czy potrzbne?
        this.file = file;
        LOG.debug("File - successful");

        this.clientP2Pnode = clientP2Pnode;
        LOG.debug("clientP2Pnode - successful");

		this.gui.addButtonActionListener(guziki); // podpięcie guzików
		this.addPropertyChangeListener(this.gui); // podpięcie zmian
	}   */
    /* Action Listener */
    private ActionListener guziki = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Akcja " + e.getActionCommand());
            if (e.getActionCommand().equals("Rozpocznij")) {

                clientP2Pnode = new ClientP2Pnode(portStringToInt(gui.getClientPort()), gui.getServerAddress(), portStringToInt(gui.getServerPort()), gui.getClientName(), Controller.this);
                // TODO: coś
                while (!clientP2Pnode.isConnected()) {
                    LOG.info("Node still connecting");
                    snooze(1000);
                }
                LOG.info("Node connected");

                // Podpięcie foleru do wymiany plików
                folderTree = new FolderTree(gui.getFolderPath(), gui.getClientName(), clientP2Pnode.getJCSyncTreeMap(), gui.getClientIp(), gui.getPortIn());
        		FileServer server = new FileServer(folderTree, gui.getClientName(),gui.getPortIn());
                folderTree.update();
            }

        }
    };
    private int serverPort;
    private int nodePort;
    private String clientName;
    private String serverAddress;

    public Controller(GuiWindower gui2) {
        this.gui = gui2;
        this.gui.addButtonActionListener(guziki); // podpięcie guzików
        this.addPropertyChangeListener(this.gui); // podpięcie zmian
        LOG.debug("Controller - created");
    }

    public static void main(String[] args) {

    }

    /**
     * metoda sprawdzająca poprawność działania wyrażeń regularnych
     */
    private static void testRegEx() {
        String ValidIpAddressRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

        String ValidHostnameRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
        System.out.println(new String("1299serek.pjwstk.wp.pl")
                .matches(ValidHostnameRegex));
    }

    private void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Pobiera adres serwera z GUI i sprawdza czy pasuje do wzorców adresu IP lub hostu
     */
    public void getServerAddress() {
        String addressOrIP = this.gui.getServerAddress();
        if (isStringIPorHostName(addressOrIP)) {
            serverAddress = addressOrIP;
        } else {
            this.gui.displayError("Zły adres IP lub host");
        }
    }

    public void getServerPort() {
        String port = this.gui.getServerPort();
        if (isStringPortNumber(port)) {
            serverPort = Integer.parseInt(port);
        } else {
            this.gui.displayError("Zły numer portu serwera");
        }
    }

    public void getClientPort() {
        String port = this.gui.getClientPort();
        if (isStringPortNumber(port)) {
            nodePort = Integer.parseInt(port);
        } else {
            this.gui.displayError("Zły numer portu klienta");
        }
    }

    public void getClientName() {
        clientName = this.gui.getClientName();
    }

    public void setFolderTreePath(String path) {
        // TODO: uruchomienie nasłuchu zmian
        this.gui.getFolderPath();
    }

    public boolean startSync() {
        return true;
    }

    public boolean stopSync() {
        return true;
    }

    private boolean isStringPortNumber(String port) {
        return port
                .matches("(^[0-9]$)|(^[0-9][0-9]$)|(^[0-9][0-9][0-9]$)|(^[0-9][0-9][0-9][0-9]$)|((^[0-5][0-9][0-9][0-9][0-9]$)|(^6[0-4][0-9][0-9][0-9]$)|(^65[0-4][0-9][0-9]$)|(^655[0-2][0-9]$)|(^6553[0-5]$))");
    }

    private boolean isStringIPorHostName(String hostOrIP) {
        String ValidIpAddressRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        String ValidHostnameRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";
        return (hostOrIP.matches(ValidIpAddressRegex) || hostOrIP
                .matches(ValidHostnameRegex));
    }

    private int portStringToInt(String port) {
        return Integer.parseInt(port);
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

    /**
     * Updates files in the folder
     */
    public void updateTree() {
        folderTree.update();
    }
}
