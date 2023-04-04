package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.Cli;

import java.util.Scanner;

public class Client {
    private ClientController clientController = new ClientController();


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("CLI")) {
            new Cli();
        } else {
            //Application.launch(Gui.class);
        }
    }
}

