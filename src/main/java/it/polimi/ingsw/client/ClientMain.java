package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.GUI.GUIApp;
import javafx.application.Application;


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

    public static void main(String[] args) {
        new ClientMain().run(args);
    }
}

