package main;

import gui.GuiWindower;
import org.apache.log4j.Logger;

/**
 * <p>Main class with executes P2Pnode applications.</p>
 * <p/>
 * <p>The P2Pnode application is designed to share local folders throughout connected nodes.
 * It requires a Bootstrap Server to connect to the network.
 * Communication between nodes is compliant with P2P protocol.</p>
 * <p/>
 * <p>Licence type: GNU GPL version 2</p>
 *
 * @author Maciej Hepner - Student PJIIT s7712@pjwstk.edu.pl
 * @author Marek Adam Niesiobędzki - Student PJIIT s6264@pjwstk.edu.pl
 * @author Michał Stawarz - Student PJIIT s7319@pjwstk.edu.pl
 * @author Marcin Weiss - Student PJIIT s8035@pjwstk.edu.pl
 * @version 1.0
 */
public class Start {

    public static final Logger LOG = Logger.getLogger(Start.class);

    public static void main(String[] args) {
        Start s = new Start();
        s.runMVC();
    }

    /**
     * Model-View-Controler
     */
    public void runMVC() {

        machineInfo();

        // ####### MODEL #######
        // user settings

        // ####### VIEW ########
        GuiWindower gui = new GuiWindower();

        // ####### CONTROLER ###
        Controller controller = new Controller(gui);

    }

    /**
     * Logs machine information
     */
    public void machineInfo() {
        LOG.info("Operating system name: " + System.getProperty("os.name"));
        LOG.info("Operating system version: " + System.getProperty("os.version"));
        LOG.info("Java version: " + System.getProperty("java.version"));
    }
}
