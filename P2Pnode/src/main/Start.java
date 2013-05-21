package main;

import gui.GuiWindower;

/**
 * Klasa uruchamiająca całą aplikację w MVC
 *
 * @author Marek Adam Niesiobędzki - Student PJWSTK s6264@pjwstk.edu.pl
 * @version 0.9
 */
public class Start {

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
}
