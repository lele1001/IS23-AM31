package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.GUI.GUI;
import javafx.application.Application;


public class ClientMain {
    //private static final ClientController clientController = new ClientController();

    /**
     * Start of the player side game
     *
     * @param args CLI or GUI
     */
    public void run(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
            new CLI(new ClientController());
        } else {
            Application.launch(GUI.class);
        }
    }
    public static void main(String[] args) {
        new ClientMain().run(args);
    }
}

