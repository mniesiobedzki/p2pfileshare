package main;

import gui.GuiWindower;
import org.apache.log4j.Logger;

/**
 * Klasa uruchamiająca całą aplikację w MVC
 * ąļć
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 * @version 0.9
 */
public class Start {

    public static final Logger LOG = Logger.getLogger(Start.class);

    public static void main(String[] args) {
        Start s = new Start();
        s.runMVC();
    }

    public void runMVC() {

        // ####### MODEL #######
        //TODO: Preferencje użytkownika,

        // ####### VIEW ########
        GuiWindower gui = new GuiWindower();

        // ####### CONTROLER ###
        Controller controller = new Controller(gui);

    }

    public void machineInfo(){
        LOG.info("Operating system name: " + System.getProperty("os.name"));
        LOG.info("Operating system version: " + System.getProperty("os.version"));
        LOG.info("Java version: " +  System.getProperty("java.version"));
    }
}
