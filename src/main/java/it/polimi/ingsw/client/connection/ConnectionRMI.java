package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;

public class ConnectionRMI extends ConnectionClient {
    ClientController controller;

    public ConnectionRMI(ClientController controller) {
        this.controller = controller;
    }
}
