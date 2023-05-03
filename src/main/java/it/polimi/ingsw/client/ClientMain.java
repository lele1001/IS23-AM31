package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.client.view.Gui;
import javafx.application.Application;


public class ClientMain {
    //private static final ClientController clientController = new ClientController();

    /**
     * Start of the player side game
     *
     * @param args CLI or GUI
     */

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
            new Cli(new ClientController());
        } else {
            Application.launch(Gui.class);
        }
    }
}

