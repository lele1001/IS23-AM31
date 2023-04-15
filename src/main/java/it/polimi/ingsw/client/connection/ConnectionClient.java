package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class ConnectionClient extends UnicastRemoteObject {
    ClientController controller;
    String address;
    int port;

    /**
     * Initialize the connection to the server
     *
     * @param controller ClientController on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    ConnectionClient(ClientController controller, String address, int port) throws RemoteException {
        this.controller = controller;
        this.address = address;
        this.port = port;
    }

    public void startConnection() throws Exception {

    }

    public ClientController getController() {
        return controller;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
