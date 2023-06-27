package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.GUI.GUIApp;
import javafx.application.Application;

/**
 * Starting class for Client
 */
public class ClientMain {
    /**
     * Start of the player side game
     *
     * @param args CLI or GUI
     */
    public void run(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
            int port = 0;
            String conn;

            if (args.length == 3 && (args[1].equalsIgnoreCase("RMI") || args[1].equalsIgnoreCase("SOCKET"))) {
                try {
                    port = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    System.out.println("\n Wrong arguments");
                    System.exit(1);
                }

                conn = args[1].toLowerCase();
                new CLI(new ClientController(), port, conn);
            } else {
                new CLI(new ClientController());
            }
        } else {
            Application.launch(GUIApp.class);
        }
    }

    /**
     * Call the run method to start the GUI
     *
     * @param args put by the player at start
     */
    public static void main(String[] args) {
        new ClientMain().run(args);
    }
}

