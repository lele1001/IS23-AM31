package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.Cli;

public class ClientMain {
    private static ClientController clientController = new ClientController();


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
            new Cli(clientController);
        } else {
            //Application.launch(Gui.class);
        }
    }
}

