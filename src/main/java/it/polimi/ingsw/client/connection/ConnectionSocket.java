package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;

import java.io.IOException;

public class ConnectionSocket extends ConnectionClient {
    ClientController controller;

    /**
     * Initialize the RMI connection to the server
     *
     * @param controller ClientController on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    public ConnectionSocket(ClientController controller, String address, int port) throws IOException {
        super(controller, address, port);
    }

    @Override
    public void startConnection() {

    }
}
