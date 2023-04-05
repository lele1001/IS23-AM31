package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;

public class ConnectionSocket extends ConnectionClient {
    ClientController controller;

    public ConnectionSocket(ClientController controller) {
        this.controller = controller;
    }
}
